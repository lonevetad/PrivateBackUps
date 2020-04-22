package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGameObjects;

public abstract class LoaderEquipUpgrades extends LoaderGameObjects<EquipmentUpgrade> {

	public LoaderEquipUpgrades(GameObjectsProvider<EquipmentUpgrade> objProvider) {
		super(objProvider);
	}
}