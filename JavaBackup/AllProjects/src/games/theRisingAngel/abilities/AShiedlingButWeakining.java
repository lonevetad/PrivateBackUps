package games.theRisingAngel.abilities;

import java.util.ArrayList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.abilitiesImpl.AbilityModifyingAttributesRealTime;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.events.EventDamageTRAr;
import games.theRisingAngel.events.EventsTRAr;
import games.theRisingAngel.misc.AttributesTRAr;

/**
 * Upon taking damage, reduce it by
 * <code>a Q percentage of life regeneration * N</code> by reducing it by H for
 * a certain amount of time T. Each damage taken reset the counter.<br>
 * It's like <i>enduring the skin as a stone: it's harder, but also harder to
 * heal</i>.
 */
// Q = 25%
// H = 50%
// N = 1
// T = 5000 // milliseonds
public class AShiedlingButWeakining extends AbilityModifyingAttributesRealTime implements GEventObserver {
	private static final long serialVersionUID = -5898625452208602145L;
	public static final String NAME = "Stonefying Skin";
	public static final int DURATION_EFFECT = 2000; // 5000; // 750
	protected static final AttributeIdentifier[] WHAT_TO_MODIFY = new AttributeIdentifier[] { AttributesTRAr.RigenLife,
			AttributesTRAr.DamageReductionPhysical, AttributesTRAr.DamageReductionMagical };

	public AShiedlingButWeakining() {
		super(WHAT_TO_MODIFY, NAME);
		this.eventsWatching = new ArrayList<>(2);
		this.eventsWatching.add(EventsTRAr.DamageReceived.getName());
		ticks = 0;
		timeThreshold = DURATION_EFFECT;
		isAbilityActive = false;
	}

	protected boolean isAbilityActive;
	protected long ticks, timeThreshold;
	protected List<String> eventsWatching;

	@Override
	public Integer getObserverID() {
		return getID();
	}

//	public CreatureSimple getCreatureReferred() {return creatureReferred;}

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public long getTimeThreshold() {
		return timeThreshold;
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		ticks = 0;
		isAbilityActive = false;
		this.setAccumulatedTimeElapsed(0);
		performAbility(null); // to nullify it
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		if (isAbilityActive) {
			// act only if the ability is active
			super.act(modality, timeUnits);
		} // if no, the do not waste the time
	}

	@Override
	public void performAbility(GModality modality) {
		super.performAbility(modality);
		isAbilityActive = false;
	}

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		int lifeRegenOriginal;
		EventDamageTRAr<?> dEvent;
		BaseCreatureRPG creatureWearing;
		CreatureAttributes cAttr;
		AttributeModification am;
		if (EventsTRAr.DamageReceived.getName() == ge.getName()) {
			dEvent = (EventDamageTRAr<?>) ge;
			if (dEvent.getTarget() ==
			// check equality because it's bounded to the "wearer"
			(creatureWearing = this.getEquipItem().getCreatureWearingEquipments())
					&& dEvent.getDamage().getDamageAmount() > 0) {
				// activate the ability
				if (!this.isAbilityActive) {
					cAttr = creatureWearing.getAttributes();
					// activate the ability
					// compute the bonuses and malus
					lifeRegenOriginal = cAttr.getValue(AttributesTRAr.RigenLife.getIndex()); // Original
					(am = this.attributesToModify[0]).setValue(-(lifeRegenOriginal >> 1)); // the half
					cAttr.applyAttributeModifier(am);
					lifeRegenOriginal >>= 2; // recycle as a temp
					(am = this.attributesToModify[1]).setValue(lifeRegenOriginal);
					cAttr.applyAttributeModifier(am);
					(am = this.attributesToModify[2]).setValue(lifeRegenOriginal);
					cAttr.applyAttributeModifier(am);
				}
				this.setAccumulatedTimeElapsed(0);
				this.isAbilityActive = true;
			}
		}
	}

	@Override
	protected void updateAttributes(GModality gm, EquipmentItem ei, CreatureSimple ah, CreatureAttributes ca) {
		this.isAbilityActive = false;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
//		for (AttributeModification am : this.attributesToModify) {
//			am.setValue(0); // reset everyone
//		}
	}
}