package games.generic.controlModel.subImpl;

import games.generic.controlModel.GameObjectsProvider;
import games.theRisingAngel.LoaderAbilityTRAr;
import games.theRisingAngel.LoaderCreatureTRAr;
import games.theRisingAngel.LoaderEquipTRAr;

public abstract class GControllerRPG extends GControllerET {
	// implements IGameWithAbililties {

	public GControllerRPG() {
		super();
		this.gameObjectsProvider = newGameObjectsManagerProvider();
	}

	protected final GameObjectsProvider gameObjectsProvider;

	//

	protected abstract GameObjectsProvider newGameObjectsManagerProvider();

	//

	public GameObjectsProvider getGameObjectsManagerProvider() {
		return gameObjectsProvider;
	}

	//

	@Override
	protected void onCreate() {
		super.onCreate();
		super.addGameObjectLoader(new LoaderAbilityTRAr(this.gameObjectsProvider.getAbilitiesProvider()));
		super.addGameObjectLoader(new LoaderEquipTRAr(this.gameObjectsProvider.getEquipmentsProvider()));
		super.addGameObjectLoader(new LoaderCreatureTRAr(this.gameObjectsProvider.getCreaturesProvider()));
	}

}