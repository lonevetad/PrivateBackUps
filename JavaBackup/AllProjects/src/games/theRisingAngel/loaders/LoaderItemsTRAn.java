package games.theRisingAngel.loaders;

import java.awt.Dimension;
import java.util.Arrays;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderItems;
import games.theRisingAngel.inventory.InventoryItemBaseTRAn;

public class LoaderItemsTRAn extends LoaderItems {

	public LoaderItemsTRAn(GameObjectsProvider<InventoryItem> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gm) {
		InventoryItemFactory iif;
		iif = this::newItemByName;
		// TODO use it
	}

	protected InventoryItem newItemByName(GModality gm, String name) {
		// right noew, there's only one type of inventory iem
		return new InventoryItemBaseTRAn(name);
	}

	static interface InventoryItemFactory {
		public InventoryItem newItem(GModality gm, String name);
	}

	protected static class FactoryItems implements FactoryObjGModalityBased<InventoryItem> {
		int rarity;
		String name, description = "";
		Dimension dimensionInInventory = null;
		int[] price;
		InventoryItemFactory inventoryItemFactory = null;

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
				cs.setGameModaliy(null); // not needed
				n = price.length;
				while (--n >= 0)
					cs.setMoneyAmount(n, price[n]);
				ii.setSellPrice(cs);
			}
		}

		@Override
		public String toString() {
			return "FactoryEquip [\n name=" + name + ",\n rarity=" + rarity + ", sell price: " + Arrays.toString(price)
					+ ",\n description: " + description + ",\n dimensions: " + dimensionInInventory
					+ ",\n abilities=\n\t";
		}
	}
}