package games.generic.controlModel.subimpl;

import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGameObjects;

public abstract class LoaderCreatures extends LoaderGameObjects<CreatureSimple> {

	public LoaderCreatures(GameObjectsProvider<CreatureSimple> objProvider) {
		super(objProvider);
	}
}