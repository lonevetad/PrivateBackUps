package videogamesOldVersion.games.theRisingArmy.abstractTRAr;

import java.io.Serializable;

import tools.predicatesExpressions.expressionEvaluator.AbstractEvaluator;
import videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC.MyAction;
import videogamesOldVersion.games.theRisingArmy.abilities.AbilityEnvironmentTrar;
import videogamesOldVersion.games.theRisingArmy.main.SharedStuffs_TRAr.TurnPhasesTRAr;

/**
 * Any kind of ability, like attack, create mana, static abilities (flying,
 * defender, shapeshifter, regeneration X, ..)<br>
 * The {@link #act(AbilityEnvironmentTrar)} 's parameter hold this ability's
 * owner/caster and the Main Controller, who holds the Model and the Game
 * Mechanism.
 */
public interface AbstractAbilityTRAr extends MyAction<AbilityEnvironmentTrar>, Serializable {

	// AbilitySource abilitySource;

	//

	// TODO GETTER

	public boolean getCanEffectBeStacked();

	/** The owner of this ability */
	// AN ABILITY SHOULD BE "STATELESS", in general
	// public GameObjectTRAr getAbilitySource();// {
	// return abilitySource;}

	//

	// TODO SETTER

	/**/
	public AbstractAbilityTRAr setCanEffectBeStacked(boolean canEffectBeStacked);

	// public AbstractAbilityTRAr setAbilitySource(GameObjectTRAr abilitySource);//
	// {
	// this.abilitySource = abilitySource;
	// return this;
	// }

	//

	// TODO ABSTRACT

	/**
	 * The returned value could be computed through {@link AbstractEvaluator} and
	 * its subclasses.
	 */
	// public abstract boolean canApplyAbilityTo(GameObjectTRAr target);
	public abstract boolean canCastAbility();

	public AbstractAbilityTRAr clone();

	/**
	 * Le abilit� possono contenere svariate variabili da cui dipende il
	 * funzionamento implementando molteplici interfacce , come
	 * "ActivationCostHolder", "DamageHolder", "ManaProductionHolder", "RayHolder"
	 * (queste ultime tre generalizzabili come IntHolder, ma non conviene) ed una
	 * abilit� (statica?) potrebbe modificarne il valore .. per esempio, potrebbe
	 * incrementare la cura apportata o il danno, o ridurlo, o convertire un costo
	 * di mana colorato in incolore, o ridurre il costo incolore, o ampliare il
	 * raggio di una esplosione, ecc
	 * <p>
	 * Per apportare questa modifica bisognerebbe usare questo metodo.
	 */
	/*
	 * public default void tryToAlterAbility(AbstractAbilityTRAr abilityToBeAltered)
	 * { }
	 */

	//

	// TODO OTHER

	/**
	 * {@link GameObjectTRAr} has a list of abilities and thanks to the "close-world
	 * logic" that class has some variables to tune some operations (more likely
	 * "checks") are tuned, like "can move", "can attack", "is creature X", ecc).
	 * <br>
	 * When this ability is added to the list, this method should be called.
	 */
	public default boolean onBeingGainedBy(GameObjectTRAr got) {
		return false;
	}

	/**
	 * Like {@link #onBeingGainedBy(GameObjectTRAr)}, but when this ability is
	 * removed (lost).
	 */
	public default boolean onBeingRemovedFrom(GameObjectTRAr got) {
		return false;
	}

	//

	/**
	 * Some abilities are inherited by all cards. In particular, all creatures can
	 * move or attack, so those implementations should override it and return
	 * <code>true</code>.
	 */
	public default boolean isDefault() {
		return false;
	}

	// TODO ACTIVATE ON

	// public default boolean activateOn(){return false;}

	public default boolean activateOnTurnPhaseChange(TurnPhasesTRAr turnPhase) {
		return false;
	}

	// moving

	public default boolean activateOnMoveStart(GameObjectTRAr objMoving, int lengthMovement, Object steps, //
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnMoveDone(GameObjectTRAr objMoving, int lengthMovement, Object steps, //
			Object otherParameters) {
		return false;
	}

	// attacks

	public default boolean activateOnAttackReceived(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object attackImplementation, //
			Object attackType, // physical, magic, holy, water, fire, dark, electro, ecc
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnAttackDone(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object attackImplementation, //
			Object attackType, // physical, magic, holy, water, fire, dark, electro, ecc
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnAttackStart(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object attackImplementation, //
			Object attackType, // physical, magic, holy, water, fire, dark, electro, ecc
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnAttackEnd(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object attackImplementation, //
			Object attackType, // physical, magic, holy, water, fire, dark, electro, ecc
			Object otherParameters) {
		return false;
	}

	// damage

	public default boolean activateOnDamageReceived(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnDamageDealt(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

	// heal

	public default boolean activateOnHealReceived(GameObjectTRAr dest, int amount, Object otherParameters) {
		return activateOnHealReceived(null, dest, amount, otherParameters);
	}

	public default boolean activateOnHealReceived(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnHealDone(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

	// stamina

	public default boolean activateOnStaminaReceived(GameObjectTRAr dest, int amount, Object otherParameters) {
		return activateOnStaminaReceived(null, dest, amount, otherParameters);
	}

	public default boolean activateOnStaminaReceived(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

	public default boolean activateOnStaminaLoss(GameObjectTRAr source, GameObjectTRAr dest, int amount,
			Object otherParameters) {
		return false;
	}

}