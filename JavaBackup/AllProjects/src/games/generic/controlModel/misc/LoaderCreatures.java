package games.generic.controlModel.misc;

import games.generic.controlModel.gObj.CreatureSimple;

public abstract class LoaderCreatures extends LoaderGameObjects<CreatureSimple> {

	public LoaderCreatures(GameObjectsProvider<CreatureSimple> objProvider) {
		super(objProvider);
	}
}