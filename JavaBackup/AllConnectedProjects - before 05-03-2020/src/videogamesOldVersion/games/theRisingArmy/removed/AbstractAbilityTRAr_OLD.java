package games.theRisingArmy.removed;

import common.utilities.expressionEvaluer.AbstractEvaluator;
import games.theRisingArmy.abstractTRAr.AbstractAbilityTRAr;
import games.theRisingArmy.abstractTRAr.GameObjectTRAr;

public interface AbstractAbilityTRAr_OLD {

	// AbilitySource abilitySource;

	//

	// TODO GETTER

	public boolean getCanEffectBeStacked();

	/** The owner of this ability */
	public GameObjectTRAr getAbilitySource();// {
	// return abilitySource;}

	//

	// TODO SETTER

	public AbstractAbilityTRAr setCanEffectBeStacked(boolean canEffectBeStacked);

	public AbstractAbilityTRAr setAbilitySource(GameObjectTRAr abilitySource);// {
	// this.abilitySource = abilitySource;
	// return this;
	// }

	//

	// TODO ABSTRACT

	/**
	 * The returned value could be computed through {@link AbstractEvaluator} and its subclasses.
	 */
	// public abstract boolean canApplyAbilityTo(GameObjectTRAr target);
	public abstract boolean canCastAbility();

	public AbstractAbilityTRAr clone();

	/**
	 * Le abilità possono contenere svariate variabili da cui dipende il funzionamento implementando
	 * molteplici interfacce , come "ActivationCostHolder", "DamageHolder", "ManaProductionHolder",
	 * "RayHolder" (queste ultime tre generalizzabili come IntHolder, ma non conviene) ed una
	 * abilità (statica?) potrebbe modificarne il valore .. per esempio, potrebbe incrementare la
	 * cura apportata o il danno, o ridurlo, o convertire un costo di mana colorato in incolore, o
	 * ridurre il costo incolore, o ampliare il raggio di una esplosione, ecc
	 * <p>
	 * Per apportare questa modifica bisognerebbe usare questo metodo.
	 */

	public default void tryToAlterAbility(AbstractAbilityTRAr abilityToBeAltered) {
	}

	//

	// TODO OTHER

	//

	/**
	 * {@link GameObjectTRAr} has a list of abilities and thanks to the "close-world logic" that
	 * class has some variables to tune some operations (more likely "checks") are tuned, like "can
	 * move", "can attack", "is creature X", ecc). <br>
	 * When this ability is added to the list, this method should be called.
	 */
	public default boolean onBeingGainedBy(GameObjectTRAr got) {
		return false;
	}

	/** Like {@link #onBeingGainedBy(GameObjectTRAr)}, but when this ability is removed (lost). */
	public default boolean onBeingRemovedFrom(GameObjectTRAr got) {
		return false;
	}

	/**
	 * Some abilities are inherited by all cards. In particular, all creatures can move or attack,
	 * so those implementations should override it and return <code>true</code>.
	 */
	public default boolean isDefault() {
		return false;
	}
}