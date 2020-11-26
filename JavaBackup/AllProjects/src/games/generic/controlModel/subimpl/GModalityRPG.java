package games.generic.controlModel.subimpl;

import dataStructures.isom.InSpaceObjectsManagerImpl;
import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GObjectsInSpaceManager;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.MovingObject;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.player.BasePlayerRPG;

public abstract class GModalityRPG extends GModalityET {
	public static final int SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE = 10;

	public GModalityRPG(GController controller, String modalityName) {
		super(controller, modalityName);
//		this.gameObjectsProviderHolderRPG = gomp;
	}

	protected abstract GameObjectsManager newGameObjectsManager(GEventInterface gei);

	@Override
	protected GameObjectsManager newGameObjectsManager() {
		return newGameObjectsManager(getEventInterface());
//		return newGameObjectsManager(newEventInterface());
	}

//	protected final GameObjectsProviderHolderRPG gameObjectsProviderHolderRPG;

	public GameObjectsProvidersHolderRPG getGameObjectsProviderHolderRPG() {
		return (GameObjectsProvidersHolderRPG) gameObjectsProviderHolder;
	}

	/**
	 * After each levelling up, each player can gain points to spend to increase its
	 * attribues (as described in {@link CreatureAttributes} and
	 * {@link BasePlayerRPG#getAttributePointsLeftToApply()}).
	 */
	public abstract int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p);

	/**
	 * Concept useful for {@link GObjectsInSpaceManager} and
	 * {@link MovingObject}.<br>
	 * Each "space unit" (like <i>meters</i>) are composed by some "sub-units" (like
	 * <i>centimeters</i>) which could be used to build up
	 * {@link InSpaceObjectsManagerImpl} instances and to calculate object movements.
	 * <p>
	 * Defalut implementation returns {@link #SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE}
	 */
	public int getSpaceSubunitsEveryUnit() {
		return SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE;
	}

	//

	//

	// TODO PROXYS

	/** Just a proxy */
	public void dealsDamageTo(DamageDealerGeneric source, CreatureSimple target, DamageGeneric damage) {
		this.getGameObjectsManager().dealsDamageTo(source, target, damage);
	}

	/** Just a proxy */
	public void spawnObjInMap(ObjectInSpace ois) {
		this.getGameObjectsManager().addToSpace(ois);
	}
}