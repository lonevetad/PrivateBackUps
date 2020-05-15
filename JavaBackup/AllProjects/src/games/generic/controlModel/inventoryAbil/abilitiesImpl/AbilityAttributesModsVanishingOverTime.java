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
 * The vanishing effect could be defined by overriding {@link #vanishEffect()}.
 * Currently it is diminished depending on the result of
 * {@link #computeNewAmountOnVanishing(AttributeModification)}.
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

	public AbilityAttributesModsVanishingOverTime(String name, AttributeModification[] attributesMods) {
		super(name);
		setAttributesToModify(attributesMods);
		initAAMVOT();
	}

	protected boolean isCumulative; // , modificationsAppliedAtLeastOnce;
	/**
	 * Counter to count the amount of triggering events received between each
	 * "owner's attributes' updates over time" (updates used to make this ability
	 * "real time")
	 */
	protected volatile int eventsReceivedBeweenUpdateds, stackedTriggerCharges;
	protected int maxAmountStackedTriggerCharges;
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
//		this.modificationsAppliedAtLeastOnce = false;
		this.maxAmountStackedTriggerCharges = this.stackedTriggerCharges = 0;
	}

	//

	// TODO GETTER

	@Override
	public List<String> getEventsWatching() { return eventsWatching; }

	@Override
	public boolean isCumulative() { return isCumulative; }

	@Override
	public PhaseAbilityVanishing getPhaseAbilityCurrent() { return phaseAbilityCurrent; }

	@Override
	public int getAccumulatedTimeAbililtyVanishing() { return accumulatedTimeAbililtyVanishing; }

	/**
	 * Returns the amount of cumulated effect activation, if any. See
	 * {@link #getMaxAmountStackedTriggerCharges()}.
	 */
	public int getStackedTriggerCharges() { return stackedTriggerCharges; }

	/**
	 * If this ability {@link #isCumulative()}, then the ability can stack and the
	 * effects can be applied more than once; but the cumulated effect could be very
	 * huge. To limit this, set this bound, this cap of the stacked effects.<br>
	 * BEWARE: setting it to <code>0 (zero)</code> means <i>unbound</i> and negative
	 * numbers are forbidden (which are converted to <code>0 (zero)</code>).
	 * <p>
	 * It's almost parallel to {@link #isCumulative()} but cannot be confused: the
	 * difference exists to avoid integer overflow (very rare) and for faster
	 * checks. Even so, setting {@link #isCumulative()} to <code>true</code> and
	 * this value o <code>1</code> makes the same effect of setting the flag to
	 * <code>false</code>.
	 */
	public int getMaxAmountStackedTriggerCharges() { return maxAmountStackedTriggerCharges; }

	//

	/** See [{@link #getMaxAmountStackedTriggerCharges()}. */
	public void setMaxAmountStackedTriggerCharges(int maxAmountStackedTriggerCharges) {
		if (maxAmountStackedTriggerCharges < 0)
			this.maxAmountStackedTriggerCharges = 0;
		else
			this.maxAmountStackedTriggerCharges = maxAmountStackedTriggerCharges;
	}

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
	public void setPhaseAbilityCurrent(PhaseAbilityVanishing pav) { this.phaseAbilityCurrent = pav; }

	@Override
	public void setAccumulatedTimeAbililtyVanishing(int accumulatedTimeAbililtyVanishing) {
		this.accumulatedTimeAbililtyVanishing = accumulatedTimeAbililtyVanishing;
	}

	//

	protected void reinstanceAttributesOriginal() {
		int n;
		AttributeModification am;
		attributesToModifyOriginal = new AttributeModification[n = attributesToModify.length];
		while (--n >= 0) {
			am = attributesToModify[n];
			attributesToModifyOriginal[n] = new AttributeModification(am.getAttributeModified(), am.getValue());
			am.setValue(0); // ability is inactive now
		}
	}

	@Override
	public void resetAbility() {
		super.resetAbility();
		this.phaseAbilityCurrent = PhaseAbilityVanishing.Inactive;
		this.eventsReceivedBeweenUpdateds = 0;
		this.accumulatedTimeAbililtyVanishing = 0;
		this.stackedTriggerCharges = 0;
		removeAndNullifyEffects();
	}

	@Override
	public void onRemovingFromOwner(GModality gm) {
		super.onRemovingFromOwner(gm);
		// remove previously added attributes
//		if (modificationsAppliedAtLeastOnce) {
		// do not remove more than once
//			modificationsAppliedAtLeastOnce = false;
		removeAndNullifyEffects();
//		}
	}

	protected void removeAndNullifyEffects() {
		CreatureAttributes ca;
		ca = getAttributesOfOwner(); // getAttributesWearer();
		if (ca == null)
			return;
		for (AttributeModification am : attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

//	public void onEquip(GModality gm) {
//		modificationsAppliedAtLeastOnce = true;
//		super.onEquip(gm);
//	}

	protected boolean isAcceptableEvent(IGEvent e) { return this.eventsWatching.contains(e.getName()); }

	/**
	 * Override designed.<br>
	 * Default implementation returns the half of the current value.
	 */
	protected int computeNewAmountOnVanishing(AttributeModification am) { return am.getValue() >> 1; }

	/**
	 * It's really called in
	 * {@link AbilityModifyingAttributesRealTime#updateAttributesModifiersValues(GModality, EquipmentItem, CreatureSimple, CreatureAttributes)}.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void vanishEffect() {
		int newValue, oldValue;
		for (AttributeModification am : this.attributesToModify) {
			oldValue = am.getValue();
			if (oldValue != 0) {
				newValue = computeNewAmountOnVanishing(am);
				am.setValue(newValue);
			}
		}
	}

	@Override
	protected void applyModifyingEffecsOnEquipping() {
		// nullify this effect
	}

	@Override
	public void doUponAbilityActivated() {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		removeAndNullifyEffects();
		this.stackedTriggerCharges = 1;
	}

	@Override
	public void doUponAbilityEffectEnds() {
		this.stackedTriggerCharges = 0;
		// nullify modifications
		// finished or Inactive, don't care, it's ended
		setPhaseAbilityCurrent(PhaseAbilityVanishing.Inactive);
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
		int n;
		AttributeModification am;
		n = this.attributesToModify.length;
		if (isCumulative) {
			// add all activations caused by events between each update
			if (eventsReceivedBeweenUpdateds > 0) {
				while (--n >= 0) {
					am = this.attributesToModify[n];
					// add up linearly
					am.setValue(am.getValue()
							+ eventsReceivedBeweenUpdateds * this.attributesToModifyOriginal[n].getValue());
				}
			}
		} else { // reset to original value //
			while (--n >= 0) {
				this.attributesToModify[n].setValue(this.attributesToModifyOriginal[n].getValue());
			}
		}

		eventsReceivedBeweenUpdateds = 0;
	}

	@Override
	public void act(GModality modality, int timeUnits) {
		evolveAbilityStatus(modality, timeUnits);
		// first make the ability to evolve, then update values
//		AbilityVanishingOverTime.
//		AbilityModifyingAttributesRealTime.
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
			if (isCumulative && (this.stackedTriggerCharges < this.maxAmountStackedTriggerCharges
					|| this.maxAmountStackedTriggerCharges == 0)) {
				// count the activation (or refresh if the bound is not 1) if possible
				eventsReceivedBeweenUpdateds++;
				if (++this.stackedTriggerCharges < 0)
					this.stackedTriggerCharges = Integer.MAX_VALUE;
			} // but still allows refreshing the counter
			reActivateAbility(modality);
			if (accumulatedTimeAbililtyVanishing > 0) // just to be sure
				accumulatedTimeAbililtyVanishing = 0;
		}
	}

	@Override
	public void updateAttributesModifiersValues(GModality gm, /* EquipmentItem ei, */ CreatureSimple ah,
			CreatureAttributes ca) {
		PhaseAbilityVanishing phaseAbility;
		phaseAbility = phaseAbilityCurrent;
		if (phaseAbility == PhaseAbilityVanishing.Active) {
			updateActiveEffectModAttributes();
		} else if (phaseAbility == PhaseAbilityVanishing.Vanishing) {
			vanishEffect();
		} else { // if (phaseAbility == PhaseAbilityVanishing.Endend) {}
			for (AttributeModification am : this.attributesToModify) {
				am.setValue(0);
			}
		}
//		switch (phaseAbilityCurrent) {
//		case Active:
//			updateActiveEffectModAttributes();
//			break;
//		case Vanishing:
//			vanishEffect();
//			break;
//		default:
//		}
	}
}