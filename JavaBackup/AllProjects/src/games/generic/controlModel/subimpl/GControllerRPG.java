package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;

public abstract class GControllerRPG extends GControllerET {
	// implements IGameWithAbililties {

	public GControllerRPG() {
		super();
		this.gameObjectsProvidersHolderRPG = newGameObjectsProvider();
	}

	protected final GameObjectsProvidersHolderRPG gameObjectsProvidersHolderRPG;

	//

	protected abstract GameObjectsProvidersHolderRPG newGameObjectsProvider();

	//

	@Override
	protected GameObjectsProvidersHolder getGObjProvidersHolderForGModality(GModality gm) {
		return gameObjectsProvidersHolderRPG; // don't care of "gm", it's always this instance ..
	}

	public GameObjectsProvidersHolderRPG getGameObjectsProvider() {
		return gameObjectsProvidersHolderRPG;
	}

	//

}