package games.theRisingAngel.loaders;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderItems;
import games.theRisingAngel.inventory.InventoryItemBaseTRAn;

public class LoaderItemsTRAn extends LoaderItems {

	public LoaderItemsTRAn(GameObjectsProvider<InventoryItem> objProvider) { super(objProvider); }

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
}