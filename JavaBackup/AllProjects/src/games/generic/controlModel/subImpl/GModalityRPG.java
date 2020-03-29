package games.generic.controlModel.subImpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GameObjectsManagerProvider;

public abstract class GModalityRPG extends GModalityET {

	public GModalityRPG(GController controller, String modalityName, GameObjectsManagerProvider gomp) {
		super(controller, modalityName);
		this.gameObjectsManagerProvider = gomp;
	}

	/** Inherited from {@link GControllerRPG}. */
	protected final GameObjectsManagerProvider gameObjectsManagerProvider;

	//

	public GameObjectsManagerProvider getGameObjectsManagerProvider() {
		return gameObjectsManagerProvider;
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
	}
}