package games.theRisingAngel;

import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreaturesProvider;
import games.generic.controlModel.inventoryAbil.EquipItemProvider;
import games.generic.controlModel.inventoryAbil.EquipmentUpgradesProvider;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;

// todoooo

public class GameObjectsProvidersHolderTRAr extends GameObjectsProvidersHolderRPG {
	public GameObjectsProvidersHolderTRAr() {
		super();
	}

	//

	@Override
	public EquipItemProvider newEquipItemProvider() {
		return new EquipItemProvider();
	}

	@Override
	public EquipmentUpgradesProvider newEquipUpgradesProvider() {
		return new EquipmentUpgradesProvider();
	}

	@Override
	public CreaturesProvider<BaseCreatureRPG> newCreatureProvider() {
		return new CreaturesProvider<>();
	}
}