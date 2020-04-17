package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.GameObjectsManager;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.controlModel.player.UserAccountGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import games.theRisingAngel.misc.CurrencySetTRAr;

// TODO todo tons of stuffs
public class GModalityTRAr extends GModalityRPG {

	public GModalityTRAr(GController controller, String modalityName) {
		super(controller, modalityName);
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
	protected PlayerGeneric newPlayerInGame(UserAccountGeneric superPlayer) {
		return new PlayerTRAr(this);
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub

	}
}