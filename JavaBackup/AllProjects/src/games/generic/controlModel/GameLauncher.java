package games.generic.controlModel;

import games.generic.view.GameView;

public abstract class GameLauncher {

	public abstract GameController newController();

	public abstract GameView newView(GameController gc);

	public static void initGame(GameLauncher gl) {
		GameController gc;
		GameView gv;
		gc = gl.newController();
		gc.init();
		gv = gl.newView(gc);
		gv.initAndShow();
	}
	
	// and ... the main? On concrete classes.
}