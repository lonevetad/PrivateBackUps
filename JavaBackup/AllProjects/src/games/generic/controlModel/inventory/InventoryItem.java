package games.generic.controlModel.inventory;

import java.awt.Point;

import games.generic.ObjectWithID;
import games.generic.controlModel.utils.uidp.InventoryItemUIDProvider;

/** Marker interface for elements that can be placed into the inventory */
public abstract class InventoryItem implements ObjectWithID {
	protected Point locationInInventory;
	protected Integer ID;

	public InventoryItem() {
		this.ID = InventoryItemUIDProvider.newID();
	}

	@Override
	public Integer getID() {
		return ID;
	}

	public Point getLocationInInventory() {
		return this.locationInInventory;
	}

	public void setLocationInInventory(Point locationInInventory) {
		this.locationInInventory = locationInInventory;
	}
}