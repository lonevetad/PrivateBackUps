package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.IGEvent;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * See super-documentation of {@link AbilityVanishingOverTime}.<br>
 * The vanishing effect could be defined by overriding {@link #vanishEffect()},
 * currently is "divided by half".
 * <p>
 * The ability could simple be active (and each acceptable {@link IGEvent} reset
 * the "active-counter") or cumulative, meaning that the
 * {@link AttributeModification} can be applied more times, for instance one for
 * every event received while is not {@link PhaseAbilityVanishing#Inactive}
 * (plus one, to encode the triggering). To set this flag, call
 * {@link #setCumulative(boolean)},
 */
public abstract class AbilityAttributesModsVanishingOverTime extends AbilityModifyingAttributesRealTime
		implements GEventObserver, AbilityVanishingOverTime {
	private static final long serialVersionUID = 485154325880277L;

	public AbilityAttributesModsVanishingOverTime() {
		super();
		initAAMVOT();
	}

	public AbilityAttributesModsVanishingOverTime(AttributeModification[] attributesMods, String name) {
		super(name);
		setAttributesToModify(attributesMods);
		initAAMVOT();
	}

	protected boolean isCumulative;
	/**
	 * Counter to count the amount of triggering events received between each
	 * "owner's attributes' updates over time" (updates used to make this ability
	 * "real time")
	 */
	protected int eventsReceivedBeweenUpdateds;
	/**
	 * Differs from {@link #accumulatedTimeElapsedForUpdating} because this one is
	 * referred to the vanishing ability (both for base ability and vanishing
	 * effect)
	 */
	protected int accumulatedTimeAbililtyVanishing;
//	protected int abilityEffectDuration, vanishingEffectDuration;
	protected PhaseAbilityVanishing phaseAbilityCurrent;
	protected List<String> eventsWatching;
	/**
	 * Original values, used to set
	 * {@link AbilityModifyingAttributesRealTime#attributesToModify} during the
	 * ability evolution and act as a "original values cache"
	 */
	protected AttributeModification[] attributesToModifyOriginal;

	//

	//

	protected void initAAMVOT() {
		this.eventsWatching = new LinkedList<>();
		this.phaseAbilityCurrent = PhaseAbilityVanishing.Inactive;
		this.isCumulative = false;
		this.eventsReceivedBeweenUpdateds = 0;
		this.accumulatedTimeAbililtyVanishing = 0;
	}

	//

	// TODO GETTER

	@Override
	public List<String> getEventsWatching() {
		return eventsWatching;
	}

	@Override
	public boolean isCumulative() {
		return isCumulative;
	}

	@Override
	public PhaseAbilityVanishing getPhaseAbilityCurrent() {
		return phaseAbilityCurrent;
	}

	@Override
	public int getAccumulatedTimeAbililtyVanishing() {
		return accumulatedTimeAbililtyVanishing;
	}

	//

	@Override
	public void setCumulative(boolean isCumulable) {
		boolean oldValue;
		oldValue = this.isCumulative;
		this.isCumulative = isCumulable;
		if (oldValue != isCumulable) {
			if (isCumulable)
				reinstanceAttributesOriginal();
			else
				this.attributesToModifyOriginal = null; // remove cache
		}
	}

	public void setAttributesToModify(AttributeModification[] attributesMods) {
		this.attributesToModify = attributesMods;
		if (isCumulative)
			reinstanceAttributesOriginal();
	}

	@Override
	public void setPhaseAbilityCurrent(PhaseAbilityVanishing pav) {
		this.phaseAbilityCurrent = pav;
	}

	@Override
	public void setAccumulatedTimeAbililtyVanishing(int accumulatedTimeAbililtyVanishing) {
		this.accumulatedTimeAbililtyVanishing = accumulatedTimeAbililtyVanishing;
	}

	//

	protected void reinstanceAttributesOriginal() {
		int n;
		attributesToModifyOriginal = new AttributeModification[n = attributesToModify.length];
		while(--n >= 0) {
			attributesToModifyOriginal[n] = new AttributeModification(attributesToModify[n].getAttributeModified(),
					attributesToModify[n].getValue());
		}
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		this.phaseAbilityCurrent = PhaseAbilityVanishing.Inactive;
		this.eventsReceivedBeweenUpdateds = 0;
		this.accumulatedTimeAbililtyVanishing = 0;
	}

	protected boolean isAcceptableEvent(IGEvent e) {
		return this.eventsWatching.contains(e.getName());
	}

	@Override
	public void vanishEffect() {
		int newValue, oldValue;
		for (AttributeModification am : this.attributesToModify) {
			newValue = (oldValue = am.getValue()) >> 1;
			if (oldValue != 0) {
				if (oldValue == -1 || oldValue == 1)
					am.setValue(0);
				else
					am.setValue(newValue);
			}
		}
	}

	/**
	 * Override designed.<br>
	 * The usual (and current) implementation is to accumulate each effects for each
	 * event trigger if {@link #isCumulative()}, otherwise simply reset the effect's
	 * values.<br>
	 * Some subclasses could define their own proper methods and computations.
	 * <p>
	 * REMEMBER to reset {@link #eventsReceivedBeweenUpdateds}.
	 */
	public void updateActiveEffectModAttributes() {
		// add all activation caused by events between each update
		if (isCumulative) {
			int n;
			AttributeModification am;
			if (eventsReceivedBeweenUpdateds > 0) {
				n = this.attributesToModify.length;
				while(--n >= 0) {
					am = this.attributesToModify[n];
					// add up linearly
					am.setValue(am.getValue()
							+ eventsReceivedBeweenUpdateds * this.attributesToModifyOriginal[n].getValue());
				}
			}
		} /*
			 * else { // reset to original value // while(--n >= 0) {
			 * this.attributesToModify[n].setValue(this.attributesToModifyOriginal[n].
			 * getValue()); } }
			 */
		eventsReceivedBeweenUpdateds = 0;
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		evolveAbilityStatus(modality, timeUnits);
		super.act(modality, timeUnits);
	}

	/*
	 * moved to the upper interface: previously was using the instance fields, now
	 * calls the getters and setters
	 */
//	public void evolveAbilityStatus(GModality modality, int timeUnits) { .. }

	@Override
	public void notifyEvent(GModality modality, IGEvent ge) {
		if (isAcceptableEvent(ge)) {
			// activate the ability
			eventsReceivedBeweenUpdateds++;
			setPhaseTo(PhaseAbilityVanishing.Active);
			if (accumulatedTimeAbililtyVanishing > 0) // just to be sure
				accumulatedTimeAbililtyVanishing = 0;
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca) {
//		if (phaseAbility == PhaseAbilityVanishing.Active) {
//		} else if (phaseAbility == PhaseAbilityVanishing.Vanishing) {
//		} else if (phaseAbility == PhaseAbilityVanishing.Endend) {}
		switch (phaseAbilityCurrent) {
		case Active:
			updateActiveEffectModAttributes();
			break;
		case Vanishing:
			vanishEffect();
			break;
		case Finished:
			// nullify modifications
			for (AttributeModification am : this.attributesToModify) {
				am.setValue(0);
			}
			setPhaseTo(PhaseAbilityVanishing.Inactive);
			break;
		default:
		}
	}
}