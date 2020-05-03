package games.generic.controlModel.inventoryAbil;

import java.util.function.Function;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.TimedObject;
import games.generic.controlModel.misc.RarityHolder;
import tools.ObjectNamedID;
import tools.ObjectWithID;

public interface AbilityGeneric extends RarityHolder, ObjectNamedID {
	public static final Function<AbilityGeneric, String> NAME_EXTRACTOR = e -> e.getName();

	public ObjectWithID getOwner();

	/** A.k.a. "the caster" */
	public void setOwner(ObjectWithID owner);

	/** Perform the ability */
	public void performAbility(GModality gm);

	/**
	 * Reset the ability to its original state. For instance, if this ability tracks
	 * a set of objects, then clear that set, or if it acts over time then reset the
	 * timer(s) to zero.
	 */
	public void resetAbility();
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
	public default int getRarityIndex() {
		return 0;
	}

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) {
		return this;
	}

	/**
	 * Override designed.<br>
	 * Performs some actions when the owner "gains" this ability. As example, if
	 * this instance is a {@link TimedObject} or a {@link GEventObserver}, then the
	 * method {@link GModality#addGameObject(ObjectWithID)} should be called.
	 */
	public void onAddingToOwner(GModality gm);

	/**
	 * Override designed.<br>
	 * Perform clean-up actions, by default by calling {@link #resetAbility()}, in
	 * certain moments, like the death of the owner, the un-equipment of the
	 * belonging {@link EquipmentItem} (especially in case of this instance is also
	 * an instance of the subclass {@link EquipItemAbility}), etc.
	 */
	public default void onRemoving(GModality gm) {
		resetAbility();
		setOwner(null);
	}
}