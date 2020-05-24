package games.theRisingAngel;

import java.util.Random;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.DamageTypeGeneric;
import games.generic.controlModel.misc.HealGeneric;
import games.generic.controlModel.misc.HealingTypeExample;
import games.generic.controlModel.subimpl.GModalityET;
import games.theRisingAngel.creatures.BaseCreatureTRAn;
import games.theRisingAngel.events.GEventInterfaceTRAn;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.GObjectsInSpaceManagerTRAn;

public class GameObjectsManagerTRAn implements GameObjectsManager {
	public static final int THRESHOLD_PROBABILITY_BASE_TO_HIT = 75,
			THRESHOLD_PROBABILITY_BASE_TO_HIT_PER_THOUSAND = THRESHOLD_PROBABILITY_BASE_TO_HIT * 10,
			MAX_PROBABILITY_VALUE = 100, MAX_PROBABILITY_VALUE_PER_THOUSAND = 10 * MAX_PROBABILITY_VALUE;

	/** DO NOT TOUCH */
	public static final AttributesTRAn[] leechableResources = { AttributesTRAn.LifeLeechPercentage,
			AttributesTRAn.ManaLeechPercentage }; // add shield in future
	/** DO NOT TOUCH */
	public static final HealingTypeExample[] leechableHealingTypes = { HealingTypeExample.Life,
			HealingTypeExample.Mana };

	public GameObjectsManagerTRAn(GModalityTRAn gmodalityTrar) {
		super();
		setGameModality(gmodalityTrar);
		this.goism = new GObjectsInSpaceManagerTRAn();
	}

	protected GModalityTRAn gmodalityTran;
	protected GObjectsInSpaceManager goism;

	@Override
	public GModality getGameModality() { return gmodalityTran; }

	@Override
	public void setGameModality(GModality gameModality) { this.gmodalityTran = (GModalityTRAn) gameModality; }

	@Override
	public GObjectsInSpaceManager getGObjectInSpaceManager() { return goism; }

	@Override
	public GEventInterface getGEventInterface() { return gmodalityTran.getEventInterface(); }

	@Override
	public void setGObjectsInSpaceManager(GObjectsInSpaceManager gisom) { this.goism = gisom; }

	@Override
	public void setGEventInterface(GEventInterface gei) {
//		this.gei = (GEventInterfaceTRAr) gei;
		gmodalityTran.setEventInterface(gei);
	}

	//

	@Override
	public void dealsDamageTo(DamageDealerGeneric source, CreatureSimple target, DamageGeneric damage) {
		int r, thresholdToHitting;
		DamageTypeGeneric damageType;
		Random rand;
		EventDamage ed;
		GModalityET gm;
		rand = this.getGameModality().getRandom();
		r = rand.nextInt(MAX_PROBABILITY_VALUE_PER_THOUSAND);
		damageType = damage.getDamageType();
		// consider source and target chances
		thresholdToHitting = (THRESHOLD_PROBABILITY_BASE_TO_HIT_PER_THOUSAND
				+ source.getProbabilityPerThousandHit(damageType)) - target.getProbabilityPerThousandAvoid(damageType);
		gm = (GModalityET) getGameModality();
		if (r <= thresholdToHitting) {
//			GameObjectsManager.super.dealsDamageTo(source, target, damage);
			ed = this.getGEventInterface().fireDamageDealtEvent(gm, source, target, damage);
//			update the damage amount
			damage.setValue(ed.getDamageAmountToBeApplied());

			// does it crits?
			thresholdToHitting = source.getProbabilityPerThousandCriticalStrike(damageType); // use it as a "temp"
			// now uses "r" as a "temp"
			r = source.getPercentageCriticalStrikeMultiplier(damageType)
					- target.getPercentageCriticalStrikeReduction(damageType);
			if (thresholdToHitting > 0 && r > 0) { // no positive multiplier -> no crit applied
				thresholdToHitting -= rand.nextInt(MAX_PROBABILITY_VALUE_PER_THOUSAND);
				if (thresholdToHitting >= 0) {
					// crit dealt !
					damage.setValue((damage.getDamageAmount() * (100 + r)) / 100);
					ed = this.getGEventInterface().fireCriticalDamageDealtEvent(gm, source, target, damage);
					// update the damage amount
					damage.setValue(ed.getDamageAmountToBeApplied());
				}
			}
			// recycle "r" as "damage amount to be inflicted"
			r = damage.getDamageAmount();
			if (r > 0) {
				if (source instanceof BaseCreatureTRAn) {
					int i;
					BaseCreatureTRAn bc;
					HealGeneric healing;
					bc = (BaseCreatureTRAn) source;
					/*
					 * Recycle "thresholdToHitting" as "amount to leech". Also accepts negative
					 * values: some mechanism like "guilt".
					 */
					i = leechableResources.length;
					while (--i >= 0) {
						thresholdToHitting = bc.getAttributes().getValue(leechableResources[i]);
						if (thresholdToHitting != 0) {
							thresholdToHitting = (thresholdToHitting * r) / 100;
							if (thresholdToHitting != 0) {
								healing = new HealGeneric(leechableHealingTypes[i], thresholdToHitting);
								bc.receiveHealing(gm, source, healing);
							}
						}
					}
				}
				// in the end, the damage is ready to be delivered
				target.receiveDamage(gm, damage, source);
			}
		} else {
			GEventInterfaceTRAn geiTran;
			geiTran = (GEventInterfaceTRAn) this.getGEventInterface();
			geiTran.fireDamageAvoidedEvent(gm, source, target, damage);
			geiTran.fireDamageMissedEvent(gm, source, target, damage);
		}
	}
}