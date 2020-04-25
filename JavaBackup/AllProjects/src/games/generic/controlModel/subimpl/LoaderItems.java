package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGameObjects;

public abstract class LoaderItems extends LoaderGameObjects<InventoryItem> {

	public LoaderItems(GameObjectsProvider<InventoryItem> objProvider) {
		super(objProvider);
	}
}