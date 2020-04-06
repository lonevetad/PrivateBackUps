package games.generic.controlModel.misc;

import games.generic.controlModel.inventoryAbil.EquipmentItem;

public abstract class LoaderEquipments extends LoaderGameObjects<EquipmentItem> {

	public LoaderEquipments(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}
}