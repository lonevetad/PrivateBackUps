package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.TimedObject;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;

/**
 * Marks an ability as triggered that activate itself for an amount of time and
 * then it vanish as the time goes on, until it deactivates itself .<br>
 * The vanishing effect could be defined by overriding {@link #vanishEffect()}.
 * <p>
 * The ability could simple be active or cumulative, meaning that the effect can
 * be applied more times. To set this flag, call
 * {@link #setCumulative(boolean)},
 */
public interface AbilityVanishingOverTime extends AbilityGeneric, TimedObject {
	static enum PhaseAbilityVanishing {
		Inactive, Active, Vanishing,
		/** Used to clean up upon the update process */
		Finished;
	}

	//

	/**
	 * The triggered ability can be simply activated or can be activated more times
	 * than one, once at each triggers. <br>
	 * Set <code>true</code> to this flag to mark this ability as cumulative, i.e.
	 * the ability is activated more than once during the effect (i.e. each time the
	 * event is received) and the effect grows depending of the amount of events
	 * received while active
	 */
	public boolean isCumulative();

	public PhaseAbilityVanishing getPhaseAbilityCurrent();

	/**
	 * Returns the amount of "time-units" that this ability lasts. Must be greater
	 * than zero.
	 */
	public int getAbilityEffectDuration();

	/**
	 * Similar to {@link #getAbilityEffectDuration()}, but "zero" means "the ability
	 * ends immediately".
	 */
	public int getVanishingEffectDuration();

	/** Counter for time progressing. */
	public int getAccumulatedTimeAbililtyVanishing();

	//

	/** Raw setting of a back-field, not triggering any functions calls. */
	public void setPhaseAbilityCurrent(PhaseAbilityVanishing pav);

	/**
	 * * See {@link #isCumulative()}.
	 */
	public void setCumulative(boolean isCumulable);

	/**
	 * See {@link #getAbilityEffectDuration()}.
	 */
	public abstract void setAbilityEffectDuration(int abilityEffectDuration);

	/**
	 * See {@link #getVanishingEffectDuration()}.
	 */
	public abstract void setVanishingEffectDuration(int vanishingEffectDuration);

	public void setAccumulatedTimeAbililtyVanishing(int accumulatedTimeAbililtyVanishing);

	//

	//

	/**
	 * Optional override designed. PROTECTED (but public because this is an
	 * interface).
	 * <p>
	 * Differs from {@link #setPhaseAbilityCurrent(PhaseAbilityVanishing)} because
	 * this method calls it AND performs actions upon activating (or re-activating,
	 * beware) the ability, or starting the de-activation process (i.e. "vanishing")
	 * or ending this last one.
	 */
	public default void setPhaseTo(PhaseAbilityVanishing pav) {
		if (pav == PhaseAbilityVanishing.Active) {
			if (getPhaseAbilityCurrent() != PhaseAbilityVanishing.Active)
				doUponAbilityActivated();
			else
				doUponAbilityRefreshed();
		} else if (pav == PhaseAbilityVanishing.Vanishing)
			doUponAbilityStartsVanishing();
		else if (pav == PhaseAbilityVanishing.Finished)
			doUponAbilityEffectEnds();
		setPhaseAbilityCurrent(pav);
		setAccumulatedTimeAbililtyVanishing(0);
	}

	public void vanishEffect();

	/** Perform something upon the ability activation */
	public abstract void doUponAbilityActivated();

	/**
	 * Variant of {@link #doUponAbilityActivated()}, performing actions if the
	 * ability has been refreshed.
	 */
	public abstract void doUponAbilityRefreshed();

	/** Perform something upon the ability enter in the vanishing */
	public abstract void doUponAbilityStartsVanishing();

	/** Perform something upon the ability ends the vanishing effects */
	public abstract void doUponAbilityEffectEnds();

	//

	@Override
	public default void act(GModality modality, int timeUnits) {
		evolveAbilityStatus(modality, timeUnits);
	}

	/**
	 * This ability evolves through time (from {@link PhaseAbilityVanishing#Active}
	 * to {@link PhaseAbilityVanishing#Vanishing} and later
	 * {@link PhaseAbilityVanishing#Finished} [at the end, it comes back to
	 * {@link PhaseAbilityVanishing#Inactive}) and this is the default
	 * implementation of this concept. <br>
	 * By default it's called by {@link #act(GModality, int)}, but could be reused
	 * for further implementations on subclasses.<br>
	 * This concept has been separated from "act" to respec the <i>Algorithm
	 * pattern</i>.
	 */
	public default void evolveAbilityStatus(GModality modality, int timeUnits) {
		int accumulatedTimeAbililty;
		PhaseAbilityVanishing phaseAbility;
		phaseAbility = getPhaseAbilityCurrent();
		if (phaseAbility == null)
			setPhaseAbilityCurrent(phaseAbility = PhaseAbilityVanishing.Inactive);
		if (phaseAbility == PhaseAbilityVanishing.Inactive || phaseAbility == PhaseAbilityVanishing.Finished)
			return;
		// act only if the ability is active, if no, then do not waste the time
		setAccumulatedTimeAbililtyVanishing(
				accumulatedTimeAbililty = timeUnits + getAccumulatedTimeAbililtyVanishing());
		// update, if necessary, the abiliy phase
		if (phaseAbility == PhaseAbilityVanishing.Active) {
			if (accumulatedTimeAbililty >= getAbilityEffectDuration()) {
				setPhaseTo(getVanishingEffectDuration() > 0 ? PhaseAbilityVanishing.Vanishing
						: PhaseAbilityVanishing.Finished);
			}
		} else if (phaseAbility == PhaseAbilityVanishing.Vanishing) {
			if (accumulatedTimeAbililty >= getVanishingEffectDuration()) {
				setPhaseTo(PhaseAbilityVanishing.Finished);
			}
		}
	}
}