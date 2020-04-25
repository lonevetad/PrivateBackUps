package games.generic.controlModel.inventoryAbil;

import java.awt.Dimension;
import java.awt.Point;

import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.RarityHolder;
import games.generic.controlModel.misc.uidp.InventoryItemUIDProvider;
import tools.ObjectNamedID;

/**
 * Marker interface for elements that can be placed into the inventory. <br>
 * Used in RPGS
 */
public abstract class InventoryItem implements RarityHolder, ObjectNamedID {
	private static final long serialVersionUID = 47104252L;
	protected int rarityIndex;
	protected Integer ID;
	protected String name;
	protected Point locationInInventory;
	protected Dimension dimensionInInventory;
	protected CurrencySet sellPrice;

	public InventoryItem(String name) {
		this.name = name;
		this.ID = InventoryItemUIDProvider.newID();
		this.dimensionInInventory = new Dimension(1, 1);
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRarityIndex() {
		return this.rarityIndex;
	}

	public Point getLocationInInventory() {
		return this.locationInInventory;
	}

	public Dimension getDimensionInInventory() {
		return dimensionInInventory;
	}

	public CurrencySet getSellPrice() {
		return sellPrice;
	}

	//

	public void setSellPrice(CurrencySet sellPrice) {
		this.sellPrice = sellPrice;
	}

	public void setDimensionInInventory(Dimension dimensionInInventory) {
		this.dimensionInInventory = dimensionInInventory;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		this.rarityIndex = rarityIndex;
		return this;
	}

	public void setLocationInInventory(Point locationInInventory) {
		this.locationInInventory = locationInInventory;
	}
}