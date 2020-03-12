package games.generic.view;

import games.generic.controlModel.GameController;

public abstract class GameView {
	protected GameController gc;

	public GameView(GameController gc) {
		super();
		this.gc = gc;
	}
	
	
	//
	
	//
	

	public abstract void initAndShow();
}