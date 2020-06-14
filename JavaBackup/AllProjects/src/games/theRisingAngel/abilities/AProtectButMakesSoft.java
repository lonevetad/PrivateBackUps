package games.theRisingAngel.abilities;

import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.theRisingAngel.events.EventDamageTRAn;
import games.theRisingAngel.events.EventsTRAn;
import games.theRisingAngel.misc.AttributesTRAn;

/**
 * Upon receiving a damage, it is halved, but at the cost of a similar amount of
 * malus of "damage reduction". This malus, after {@link #DURATION_EFFECT} it
 * vanish, reduced by the 25% (previously it halves) at every update.
 */
public class AProtectButMakesSoft extends ASimpleFixedBufferVanishingTRAn {
	private static final long serialVersionUID = -58986254522086021L;
	public static final String NAME = "Crystal Glass";
	public static final int RARITY = 2, DURATION_EFFECT = 500/* 2000 */, DURATION_VANISH = 5000;
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] {
			AttributesTRAn.DamageReductionPhysical, AttributesTRAn.DamageReductionMagical };

	public AProtectButMakesSoft() {
		super(NAME, AttributeModification.newEmptyArray(WHAT_TO_MODIFY));
		this.eventsWatching.add(EventsTRAn.DamageReceived.getName());
		this.setCumulative(false);
		super.setVanishingEffectDuration(DURATION_VANISH);
		super.setAbilityEffectDuration(DURATION_EFFECT);
		setRarityIndex(RARITY);
		malusVanishing = 0;
	}

	/**
	 * Upon receiving the second successive damage (before the activated ability
	 * fades out), force me to deactivate.<br>
	 * NOT USED: mechanism already exists because before firing the damage event,
	 * the damage amount is reduced by the appropriate creature's attribute
	 * "reduction damage of XYZ".
	 */
//	protected boolean isForcingDeactivation = false;
	/**
	 * The amount of damage reduced, equal but opposite to the reduction malus to be
	 * applied.
	 */
	protected int malusVanishing;

//		public CreatureSimple getCreatureReferred() {return creatureReferred;}

//

	@Override
	protected boolean isAcceptableEvent(IGEvent ge) {
		EventDamageTRAn dEvent;
		if (EventsTRAn.DamageReceived.getName() == ge.getName()) {
			dEvent = (EventDamageTRAn) ge;
			if (dEvent.getTarget() == this.getOwner() // this.getEquipItem().getCreatureWearingEquipments()
					// check equality because it's bounded to the "wearer"
					&& dEvent.getDamage().getDamageAmount() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int d;
//		super.notifyEvent(modality, ge);
		if (isAcceptableEvent(ge)) {
			if (++this.stackedTriggerCharges > 1) {
				setPhaseTo(PhaseAbilityVanishing.Finished);
			} else {
				EventDamageTRAn de;
				de = (EventDamageTRAn) ge;
				d = de.getDamageReducedByTargetArmors() >> 1;
				de.setDamageAmountToBeApplied(de.getDamageAmountToBeApplied() - d);
				malusVanishing = -d; // NEGATIVE
				setPhaseAbilityCurrent(PhaseAbilityVanishing.Active);
//				this.attributesToModify[0].setValue(-d);
//				this.attributesToModify[1].setValue(-d);
			}
		}
	}

//	public void doUponAbilityRefreshed() { setPhaseTo(PhaseAbilityVanishing.Finished); }

	@Override
	public void updateModAttributesDuringActivationEffect() {
		int d;
//		super.updateModAttributesDuringActiveEffect();
		d = malusVanishing;
		this.attributesToModify[0].setValue(d);
		this.attributesToModify[1].setValue(d);
//		malusVanishing = 0;
//		this.newModifiersAmountOnVanishing[0] = malusVanishing;
//		this.newModifiersAmountOnVanishing[1] = malusVanishing;
	}

	@Override
	public void updateModAttributesDuringVanishing() {
		super.updateModAttributesDuringVanishing();
		malusVanishing = 0;
	}

	@Override
	protected int computeNewAmountOnVanishing(AttributeModification am) {
		int av;
		av = am.getValue();
		av -= (av >> 2); // returns the 75%;
		return av;
	}
}