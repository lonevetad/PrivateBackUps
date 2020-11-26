package games.theRisingAngel;

import games.generic.controlModel.gObj.CreaturesProvider;
import games.generic.controlModel.gObj.creature.BaseCreatureRPG;
import games.generic.controlModel.inventoryAbil.EquipItemProvider;
import games.generic.controlModel.inventoryAbil.EquipmentUpgradesProvider;
import games.generic.controlModel.misc.GMapProvider;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;

// todoooo

public class GameObjectsProvidersHolderTRAn extends GameObjectsProvidersHolderRPG {
	public GameObjectsProvidersHolderTRAn(GModalityRPG gModality) { super(gModality); }

	//

	@Override
	public EquipItemProvider newEquipItemProvider() { return new EquipItemProvider(); }

	@Override
	public EquipmentUpgradesProvider newEquipUpgradesProvider() { return new EquipmentUpgradesProvider(); }

	@Override
	public CreaturesProvider<BaseCreatureRPG> newCreatureProvider() { return new CreaturesProvider<>(); }

	@Override
	public GMapProvider newMapsProvider() { return new GMapProvider(); }
}