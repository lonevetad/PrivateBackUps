package games.theRisingAngel;

import games.generic.controlModel.GController;
import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.player.PlayerInGame_Generic;
import games.generic.controlModel.player.PlayerOutside_Generic;
import games.generic.controlModel.subImpl.GModalityET;

public class GModalityTRAr extends GModalityET {

	public GModalityTRAr(GController controller, String modalityName) {
		super(controller, modalityName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GEventInterface newEventInterface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CurrencySet newCurrencyHolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected PlayerInGame_Generic newPlayerInGame(PlayerOutside_Generic superPlayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub

	}

}
