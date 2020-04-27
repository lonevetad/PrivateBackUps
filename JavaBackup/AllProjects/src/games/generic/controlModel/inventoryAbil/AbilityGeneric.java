package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.RarityHolder;
import tools.ObjectNamedID;
import tools.ObjectWithID;

public interface AbilityGeneric extends RarityHolder, ObjectNamedID {
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
}