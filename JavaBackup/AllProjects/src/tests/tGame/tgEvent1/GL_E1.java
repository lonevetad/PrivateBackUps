package tests.tGame.tgEvent1;

import games.generic.controlModel.GameController;
import games.generic.controlModel.GameLauncher;
import games.generic.view.GameView;

public class GL_E1 extends GameLauncher {

	@Override
	public GameController newController() {
		return new GC_E1();
	}

	@Override
	public GameView newView(GameController gc) {
		return new GV_E1(gc);
	}
}