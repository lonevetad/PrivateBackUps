package games.generic.controlModel;

import games.generic.view.GameView;

public abstract class GameLauncher {
	public GameLauncher() {
	}

	protected GameController controller;
	protected GameView view;

	public GameController getController() {
		return controller;
	}

	public GameView getView() {
		return view;
	}

	public abstract GameController newController();

	public abstract GameView newView(GameController gc);

	public static void initGame(GameLauncher gl) {
		GameController gc;
		GameView gv;
		gc = gl.newController();
		gl.controller = gc;
		gc.init();
		gv = gl.newView(gc);
		gl.view = gv;
		gv.initAndShow();
	}

	// and ... the main? On concrete classes.
}