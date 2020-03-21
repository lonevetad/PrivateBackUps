package tests.tGame.tgEvent1;

import games.generic.controlModel.player.PlayerOutside_Generic;
import games.generic.controlModel.subImpl.GameControllerET;

public class GC_E1 extends GameControllerET {

	public static final String GM_NAME = "TEST";

	public GC_E1() {
		super();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		this.getGameModalitiesFactories().put(GM_NAME, (name, gc) -> {
			return new GModality_E1(name, gc);
		});
	}

	@Override
	public void startGame() {
		super.setCurrentGameModality(this.newModalityByName(GM_NAME));
		super.startGame();
	}

	@Override
	public void closeAll() {
//		isAlive = false;
		super.closeAll();
//		this.getCurrentGameModality().closeAll(); // yet done in super
	}

	@Override
	protected PlayerOutside_Generic newPlayerOutside() {
		return null;
	}
}