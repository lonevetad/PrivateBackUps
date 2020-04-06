package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.LoaderAbilityTRAr;
import games.theRisingAngel.LoaderCreatureTRAr;
import games.theRisingAngel.LoaderEquipTRAr;

public abstract class GControllerRPG extends GControllerET {
	// implements IGameWithAbililties {

	public GControllerRPG() {
		super();
		this.gameObjectsProvidersHolderRPG = newGameObjectsManagerProvider();
	}

	protected final GameObjectsProvidersHolderRPG gameObjectsProvidersHolderRPG;

	//

	protected abstract GameObjectsProvidersHolderRPG newGameObjectsManagerProvider();

	//

	@Override
	protected GameObjectsProvidersHolder getGObjProvidersHolderForGModality(GModality gm) {
		return gameObjectsProvidersHolderRPG; // don't care of "gm", it's always this instance ..
	}

	public GameObjectsProvidersHolderRPG getGameObjectsManagerProvider() {
		return gameObjectsProvidersHolderRPG;
	}

	//

	@Override
	protected void onCreate() {
		super.onCreate();
		super.addGameObjectLoader(new LoaderAbilityTRAr(this.gameObjectsProvidersHolderRPG.getAbilitiesProvider()));
		super.addGameObjectLoader(new LoaderEquipTRAr(this.gameObjectsProvidersHolderRPG.getEquipmentsProvider()));
		super.addGameObjectLoader(new LoaderCreatureTRAr(this.gameObjectsProvidersHolderRPG.getCreaturesProvider()));
	}

}