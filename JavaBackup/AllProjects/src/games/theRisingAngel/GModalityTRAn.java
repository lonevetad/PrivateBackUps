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
import games.theRisingAngel.events.GEventInterfaceTRAn;
import games.theRisingAngel.misc.CurrencySetTRAn;
import games.theRisingAngel.misc.PlayerCharacterTypesHolder.PlayerCharacterTypes;
import tools.ObjectNamedID;

// TODO todo tons of stuffs
public class GModalityTRAn extends GModalityRPG {
	public static final int ATTRIBUTES_POINTS_GAINED_ON_LEVELING = 5;
	/** See {@link GModalityRPG#SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE} */
	public static final int SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN = 20;
	/** Milliseconds :D */
	public static final int TIME_SUBUNITS_EACH_TIME_UNIT_TRAn = 1000;

	public GModalityTRAn(GController controller, String modalityName) { super(controller, modalityName); }

	@Override
	public int getAttributesPointGainedOnLevelingUp(BasePlayerRPG p) { return ATTRIBUTES_POINTS_GAINED_ON_LEVELING; }

	@Override
	public GEventInterface newEventInterface() { return new GEventInterfaceTRAn(); }

	@Override
	protected GameObjectsManager newGameObjectsManager(GEventInterface gei) { return new GameObjectsManagerTRAn(this); }

	@Override
	public CurrencySet newCurrencyHolder() { return new CurrencySetTRAn(this, 1); }

	@Override
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer, ObjectNamedID characterType) {
		PlayerTRAn p;
		p = new PlayerTRAn(this, (PlayerCharacterTypes) characterType);
		setStartingBaseAttributes(p);
		p.setCurrencies(newCurrencyHolder());
		return p;
	}

	@Override
	protected GMap newGameMap(String mapName) {
		GMapTRAn gmap;
		gmap = new GMapTRAn(mapName);
		gmap.setGOISMDelegated(getGObjectInSpaceManager());
		return gmap;
	}

	/** Given a {@link PlayerTRAn}, set its initial set of attributes */
	public void setStartingBaseAttributes(PlayerTRAn player) {
		player.getCharacterType().applyStartingAttributes(player);
	}

	@Override
	public void startGame() {
		this.getGameObjectsProviderHolderRPG().setgModality(this);
		super.startGame();
		// and then? TODO
	}

	// TODO to do definire un metodo di dropping degli oggetti, con abilità e
	// modificatori annessi

//

	// TODO DAMAGE CALCULATION

	//

	// TODO class

}