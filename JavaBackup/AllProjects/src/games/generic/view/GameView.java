package games.generic.view;

import games.generic.controlModel.GController;

public abstract class GameView {
	protected GController gc;

	public GameView(GController gc) {
		super();
		this.gc = gc;
	}
	
	
	//
	
	//
	

	public abstract void initAndShow();
}