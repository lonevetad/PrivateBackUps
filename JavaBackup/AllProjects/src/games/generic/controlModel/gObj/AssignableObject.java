package games.generic.controlModel.gObj;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import tools.ObjectWithID;

public interface AssignableObject extends GameObjectGeneric {

	public ObjectWithID getOwner();

	public void setOwner(ObjectWithID owner);

	// utilities

	public void resetStuffs();

	/**
	 * Override designed.<br>
	 * Performs some actions when the owner "gains" this ability. As example, if
	 * this instance is a {@link TimedObject} or a {@link GEventObserver}, then the
	 * method {@link GModality#addGameObject(ObjectWithID)} should be called.
	 */
	public default void onAddingToOwner(GModality gm) {
		if (gm == null) { return; }
		gm.addGameObject(this);
	}

	/**
	 * Override designed.<br>
	 * Perform clean-up actions, by default by calling {@link #resetAbility()}, in
	 * certain moments, like the death of the owner, the un-equipment of the
	 * belonging {@link EquipmentItem}, etc.
	 */
	public default void onRemovingFromOwner(GModality gm) {
		if (gm != null)
			gm.removeGameObject(this);
	}

	@Override
	public default void onRemovedFromGame(GModality gm) {
		resetStuffs();
		setOwner(null);
	}
}