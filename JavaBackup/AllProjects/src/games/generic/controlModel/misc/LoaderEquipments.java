package games.generic.controlModel.misc;

import games.generic.controlModel.inventoryAbil.EquipmentItem;

public abstract class LoaderEquipments extends LoaderGameObjects<EquipmentItem> {

	public LoaderEquipments(ObjGModalityBasedProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}
}