package games.generic.controlModel.subimpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;

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
//		return newGameObjectsManager(newEventInterface());
	}

	//

	public GameObjectsProvidersHolderRPG getGameObjectsProviderHolderRPG() {
		return (GameObjectsProvidersHolderRPG) gameObjectsProviderHolder;
	}

	//
}