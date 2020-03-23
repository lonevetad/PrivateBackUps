package games.theRisingAngel;

import java.awt.Point;

/** Marker interface for elements that can be placed into the inventory */
public abstract class InventoryItem {
	protected Point locationInInventory;

	public InventoryItem() {
	}

	public Point getLocationInInventory() {
		return this.locationInInventory;
	}

	public void setLocationInInventory(Point locationInInventory) {
		this.locationInInventory = locationInInventory;
	}
}