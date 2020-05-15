package games.generic.controlModel.inventoryAbil;

import java.util.function.Function;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.AssignableObject;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.gObj.GameObjectGeneric;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.RarityHolder;
import tools.ObjectWithID;

public interface AbilityGeneric extends AssignableObject, RarityHolder, GameObjectGeneric {
	public static final Function<AbilityGeneric, String> NAME_EXTRACTOR = e -> e.getName();

	@Override
	public ObjectWithID getOwner();

	/** A.k.a. "the caster" */
	@Override
	public void setOwner(ObjectWithID owner);

	/** Perform the ability */
	public void performAbility(GModality gm);

	/**
	 * Detects and returns if the ability can be performed. Should be called inside
	 * {@link #performAbility(GModality)}
	 */
	public default boolean canBePerformed(GModality gm) {
		ObjectWithID o;
		o = getOwner();
		if (o == null)
			return true;
		if (o instanceof DestructibleObject)
			return !((DestructibleObject) o).isDestroyed();
		return true;
	}

	/**
	 * Reset the ability to its original state. For instance, if this ability tracks
	 * a set of objects, then clear that set, or if it acts over time then reset the
	 * timer(s) to zero.
	 */
	public void resetAbility();

	@Override
	public default void resetStuffs() { resetAbility(); }

	/**
	 * Clone the ability.
	 */
//	public AbilityGeneric cloneAbility();

	/**
	 * Returns by default <code>0</code>.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public default int getRarityIndex() { return 0; }

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) { return this; }

	@Override
	public default void onAddedToGame(GModality gm) {
		// nothing to do here, right now, except from ...
		resetAbility();
	}

	@Override
	public default void onAddingToOwner(GModality gm) {
		AssignableObject.super.onAddingToOwner(gm);
		resetAbility();
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
		AssignableObject.super.onRemovedFromGame(gm);
		resetAbility();
	}

//

	// TODO UTILS

	public default CreatureAttributes getAttributesOfOwner() {
		ObjectWithID o;
		o = getOwner();
		if (o == null || (!(o instanceof AttributesHolder)))
			return null;
		return ((AttributesHolder) o).getAttributes();
	}
}