package games.generic.controlModel.subimpl;

import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGameObjects;

public abstract class LoaderAbilities extends LoaderGameObjects<AbilityGeneric> {

	public LoaderAbilities(GameObjectsProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

}