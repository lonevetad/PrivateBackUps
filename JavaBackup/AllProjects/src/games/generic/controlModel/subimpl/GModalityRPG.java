package games.generic.controlModel.subimpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.GameObjectsProvidersHolderRPG;

public abstract class GModalityRPG extends GModalityET {

	public GModalityRPG(GController controller, String modalityName) {
		super(controller, modalityName);
//		this.gameObjectsProviderHolderRPG = gomp;
	}

//	protected final GameObjectsProviderHolderRPG gameObjectsProviderHolderRPG;

	protected abstract GameObjectsManager newGameObjectsManager(GEventInterface gei);

	@Override
	protected GameObjectsManager newGameObjectsManager() {
		return newGameObjectsManager(getEventInterface());
	}

	//

	public GameObjectsProvidersHolderRPG getGameObjectsProviderHolderRPG() {
		return (GameObjectsProvidersHolderRPG) gameObjectsProviderHolder;
	}

	@Override
	public GameObjectsManager getGameObjectsManager() {
		return gomDelegated;
	}

	//
}