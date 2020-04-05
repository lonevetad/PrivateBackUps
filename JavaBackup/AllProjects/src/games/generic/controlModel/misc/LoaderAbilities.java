package games.generic.controlModel.misc;

import games.generic.controlModel.inventoryAbil.AbilityGeneric;

public abstract class LoaderAbilities extends LoaderGameObjects<AbilityGeneric> {

	public LoaderAbilities(ObjGModalityBasedProvider<AbilityGeneric> objProvider) {
		super(objProvider);
	}

}