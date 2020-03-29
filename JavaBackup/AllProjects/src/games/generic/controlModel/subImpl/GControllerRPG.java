package games.generic.controlModel.subImpl;

import games.generic.controlModel.GameObjectsManagerProvider;
import games.theRisingAngel.AbilityLoaderTRAr;
import games.theRisingAngel.CreatureLoaderTRAr;
import games.theRisingAngel.EquipLoaderTRAr;

public abstract class GControllerRPG extends GControllerET {
	// implements IGameWithAbililties {

	public GControllerRPG() {
		super();
		this.gameObjectsManagerProvider = newGameObjectsManagerProvider();
	}

	protected final GameObjectsManagerProvider gameObjectsManagerProvider;

	//

	protected abstract GameObjectsManagerProvider newGameObjectsManagerProvider();

	//

	public GameObjectsManagerProvider getGameObjectsManagerProvider() {
		return gameObjectsManagerProvider;
	}

	//

	@Override
	protected void onCreate() {
		super.onCreate();
		super.addGameObjectLoader(new AbilityLoaderTRAr(this.gameObjectsManagerProvider.getAbilitiesProvider()));
		super.addGameObjectLoader(new EquipLoaderTRAr(this.gameObjectsManagerProvider.getEquipmentsProvider()));
		super.addGameObjectLoader(new CreatureLoaderTRAr(this.gameObjectsManagerProvider.getCreaturesProvider()));
	}

}