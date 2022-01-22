package games.theRisingAngel;

import games.generic.controlModel.holders.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.misc.GMapProvider;
import games.generic.controlModel.objects.creature.BaseCreatureRPG;
import games.generic.controlModel.providers.CreaturesProvider;
import games.generic.controlModel.providers.EquipItemProvider;
import games.generic.controlModel.providers.EquipmentUpgradesProvider;
import games.generic.controlModel.subimpl.GModalityRPG;

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