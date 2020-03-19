package tests.tGame.tgEvent1;

import games.generic.controlModel.GController;
import games.generic.controlModel.GameLauncher;
import games.generic.view.GameView;

public class GL_E1 extends GameLauncher {

	@Override
	public GController newController() {
		return new GC_E1();
	}

	@Override
	public GameView newView(GController gc) {
		return new GV_E1(gc);
	}
}