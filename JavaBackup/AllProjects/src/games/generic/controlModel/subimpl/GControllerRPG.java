package games.generic.controlModel.subimpl;

import games.generic.controlModel.GModality;
import games.generic.controlModel.GameObjectsProvidersHolder;
import games.generic.controlModel.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.misc.LoaderGeneric;
import games.theRisingAngel.loaders.LoaderAbilityTRAr;
import games.theRisingAngel.loaders.LoaderCreatureTRAr;
import games.theRisingAngel.loaders.LoaderEquipTRAr;

public abstract class GControllerRPG extends GControllerET {
	// implements IGameWithAbililties {

	public GControllerRPG() {
		super();
		this.gameObjectsProvidersHolderRPG = newGameObjectsManagerProvider();
		this.loaderConfigurations = newLoaderConfigurations(this);
	}

	protected final LoaderGeneric loaderConfigurations;
	protected final GameObjectsProvidersHolderRPG gameObjectsProvidersHolderRPG;

	//

	protected abstract GameObjectsProvidersHolderRPG newGameObjectsManagerProvider();

	protected abstract LoaderGeneric newLoaderConfigurations(GControllerRPG cgRPG);

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
		this.loaderConfigurations.loadInto(this);
		super.addGameObjectLoader(new LoaderAbilityTRAr(this.gameObjectsProvidersHolderRPG.getAbilitiesProvider()));
		super.addGameObjectLoader(new LoaderEquipTRAr(this.gameObjectsProvidersHolderRPG.getEquipmentsProvider()));
		super.addGameObjectLoader(new LoaderCreatureTRAr(this.gameObjectsProvidersHolderRPG.getCreaturesProvider()));
	}

}