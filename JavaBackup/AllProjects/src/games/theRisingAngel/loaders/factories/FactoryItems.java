package games.theRisingAngel.loaders.factories;

import java.awt.Dimension;
import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;

public class FactoryItems implements FactoryObjGModalityBased<InventoryItem> {
	public int rarity;
	public String name;
	public String description = "";
	public Dimension dimensionInInventory = null;
	public int[] price;
	public InventoryItemFactory inventoryItemFactory = null;

	@Override
	public InventoryItem newInstance(GModality gm) {
		InventoryItem ii;
		ii = inventoryItemFactory.newItem(gm, name);
		ii.setDescription(description);
		this.setValues(gm, ii);
		return ii;
	}

	protected void setValues(GModality gm, InventoryItem ii) {
		ii.setRarityIndex(rarity);
		if (dimensionInInventory != null)
			ii.setDimensionInInventory(dimensionInInventory);
		if (price != null) {
			int n;
			CurrencySet cs;
			cs = gm.newCurrencyHolder();
			cs.setGameModaliy(gm); // not needed
			n = price.length;
			while (--n >= 0)
				cs.setCurrencyAmount(n, price[n]);
			ii.setSellPrice(cs);
		}
	}

	@Override
	public String toString() {
		return "FactoryEquip [\n name=" + name + ",\n rarity=" + rarity + ", sell price: " + Arrays.toString(price)
				+ ",\n description: " + description + ",\n dimensions: " + dimensionInInventory + ",\n abilities=\n\t";
	}
}