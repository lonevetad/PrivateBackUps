package games.generic.controlModel.subImpl;

import games.generic.controlModel.GameObjectsProvider;
import games.theRisingAngel.AbilityLoaderTRAr;
import games.theRisingAngel.CreatureLoaderTRAr;
import games.theRisingAngel.EquipLoaderTRAr;

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
		super.addGameObjectLoader(new AbilityLoaderTRAr(this.gameObjectsProvider.getAbilitiesProvider()));
		super.addGameObjectLoader(new EquipLoaderTRAr(this.gameObjectsProvider.getEquipmentsProvider()));
		super.addGameObjectLoader(new CreatureLoaderTRAr(this.gameObjectsProvider.getCreaturesProvider()));
	}

}