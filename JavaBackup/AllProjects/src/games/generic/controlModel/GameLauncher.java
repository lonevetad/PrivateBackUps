package games.generic.controlModel;

import games.generic.view.GameView;

public abstract class GameLauncher {
	public GameLauncher() {
	}

	protected GController controller;
	protected GameView view;

	public GController getController() {
		return controller;
	}

	public GameView getView() {
		return view;
	}

	public abstract GController newController();

	public abstract GameView newView(GController gc);

	public static void initGame(GameLauncher gl) {
		GController gc;
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