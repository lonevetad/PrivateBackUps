package games.generic.controlModel.inventoryAbil;

import java.awt.Point;

import games.generic.ObjectNamedID;
import games.generic.controlModel.utils.uidp.InventoryItemUIDProvider;

/**
 * Marker interface for elements that can be placed into the inventory. <br>
 * Used in RPGS
 */
public abstract class InventoryItem implements ObjectNamedID {
	private static final long serialVersionUID = 47104252L;
	protected Integer ID;
	protected String name;
	protected Point locationInInventory;

	public InventoryItem(String name) {
		this.name = name;
		this.ID = InventoryItemUIDProvider.newID();
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	public Point getLocationInInventory() {
		return this.locationInInventory;
	}

	//

	public void setName(String name) {
		this.name = name;
	}

	public void setLocationInInventory(Point locationInInventory) {
		this.locationInInventory = locationInInventory;
	}
}