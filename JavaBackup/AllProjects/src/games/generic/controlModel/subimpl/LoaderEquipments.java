package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGameObjects;

public abstract class LoaderEquipments extends LoaderGameObjects<EquipmentItem> {

	public LoaderEquipments(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}
}