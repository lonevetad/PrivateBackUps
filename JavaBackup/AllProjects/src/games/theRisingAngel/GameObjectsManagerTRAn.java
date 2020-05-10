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
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GObjectsInSpaceManagerImpl;
import games.theRisingAngel.events.GEventInterfaceTRAn;

public class GameObjectsManagerTRAn implements GameObjectsManager {
	public static final int THRESHOLD_PROBABILITY_BASE_TO_HIT = 75,
			THRESHOLD_PROBABILITY_BASE_TO_HIT_PER_THOUSAND = THRESHOLD_PROBABILITY_BASE_TO_HIT * 10,
			MAX_PROBABILITY_VALUE = 100, MAX_PROBABILITY_VALUE_PER_THOUSAND = 10 * MAX_PROBABILITY_VALUE;

	public GameObjectsManagerTRAn(GModalityTRAn gmodalityTrar) {
		super();
		setGameModality(gmodalityTrar);
		this.goism = new GObjectsInSpaceManagerImpl();
	}

	protected GModalityTRAn gmodalityTran;
	protected GObjectsInSpaceManager goism;

	@Override
	public GModality getGameModality() {
		return gmodalityTran;
	}

	@Override
	public void setGameModality(GModality gameModality) {
		this.gmodalityTran = (GModalityTRAn) gameModality;
	}

	@Override
	public GObjectsInSpaceManager getGObjectInSpaceManager() {
		return goism;
	}

	@Override
	public GEventInterface getGEventInterface() {
		return gmodalityTran.getEventInterface();
	}

	@Override
	public void setGObjectsInSpaceManager(GObjectsInSpaceManager gisom) {
		this.goism = gisom;
	}

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
			r = source.getPercentageCriticalStrikeMultiplier(damageType); // used as a "temp"
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
			// in the end, the damage is ready to be delivered
			target.receiveDamage(gm, damage, source);
		} else {
			GEventInterfaceTRAn geiTran;
			geiTran = (GEventInterfaceTRAn) this.getGEventInterface();
			geiTran.fireDamageAvoidedEvent(gm, source, target, damage);
			geiTran.fireDamageMissedEvent(gm, source, target, damage);
		}
	}
}