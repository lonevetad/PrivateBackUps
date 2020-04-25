package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GMap;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.BasePlayerRPG;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import games.theRisingAngel.misc.CurrencySetTRAr;
import games.theRisingAngel.misc.PlayerCharacterTypesHolder.PlayerCharacterTypes;
import tools.ObjectNamedID;

// TODO todo tons of stuffs
public class GModalityTRAr extends GModalityRPG {
	public static final int ATTRIBUTES_POINTS_GAINED_ON_LEVELING = 10;

	public GModalityTRAr(GController controller, String modalityName) {
		super(controller, modalityName);
	}

	@Override
	public int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p) {
		return ATTRIBUTES_POINTS_GAINED_ON_LEVELING;
	}

	@Override
	public GEventInterface newEventInterface() {
		return new GEventInterfaceTRAr();
	}

	@Override
	protected GameObjectsManager newGameObjectsManager(GEventInterface gei) {
		return new GameObjectsManagerTRAr(this);
	}

	@Override
	public CurrencySet newCurrencyHolder() {
		return new CurrencySetTRAr(this, 0);
	}

	@Override
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID characterType) {
		PlayerTRAr p;
		p = new PlayerTRAr(this, (PlayerCharacterTypes) characterType);
		setStartingBaseAttributes(p);
		return p;
	}

	@Override
	protected GMap newGameMap(String mapName) {
		GMapTRAr gmap;
		gmap = new GMapTRAr(mapName);
		gmap.setGOISMDelegated(getGObjectInSpaceManager());
		return gmap;
	}

	/** Given a {@link PlayerTRAr}, set its initial set of attributes */
	public void setStartingBaseAttributes(PlayerTRAr player) {
		player.getCharacterType().applyStartingAttributes(player);
	}

	@Override
	public void startGame() {
		super.startGame();
		// and then? TODO
	}

}