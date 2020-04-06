package games.theRisingAngel;

import games.generic.controlModel.GameObjectsProvidersHolderRPG;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GControllerRPG;

public class GControllerTRAr extends GControllerRPG {

	public GControllerTRAr() {
		super();
	}

	@Override
	protected GameObjectsProvidersHolderRPG newGameObjectsManagerProvider() {
		return new GameObjectsProvidersHolderTRAr();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		// TODO Auto-generated method stub

	}

	@Override
	protected UserAccountGeneric newUserAccount() {
		// TODO Auto-generated method stub
		return null;
	}

}