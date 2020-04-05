package games.generic.controlModel.subImpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.GameObjectsProvider;

public abstract class GModalityRPG extends GModalityET {

	public GModalityRPG(GController controller, String modalityName, GameObjectsProvider gomp) {
		super(controller, modalityName);
		this.gameObjectsProvider = gomp;
		this.gomDelegated = newGOMDelegated(super.getEventInterface());
	}

	/** Inherited from {@link GControllerRPG}. */
	protected final GameObjectsProvider gameObjectsProvider;
	protected final GameObjectsManager gomDelegated;

	protected abstract GameObjectsManager newGOMDelegated(GEventInterface gei);

	//

	public GameObjectsProvider getGameObjectsProvider() {
		return gameObjectsProvider;
	}

	public GameObjectsManager getGameObjectsManagerDelegated() {
		return gomDelegated;
	}

	//
}