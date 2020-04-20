package games.generic.controlModel.subimpl;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.player.BasePlayerRPG;

public abstract class GModalityRPG extends GModalityET {

	public GModalityRPG(GController controller, String modalityName) {
		super(controller, modalityName);
//		this.gameObjectsProviderHolderRPG = gomp;
	}

//	protected final GameObjectsProviderHolderRPG gameObjectsProviderHolderRPG;

	protected abstract GameObjectsManager newGameObjectsManager(GEventInterface gei);

	/**
	 * After each levelling up, each player can gain points to spend to increase its
	 * attribues (as described in {@link CreatureAttributes} and
	 * {@link BasePlayerRPG#getAttributePointsLeftToApply()}).
	 */
	public abstract int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p);

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