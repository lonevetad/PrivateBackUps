package games.generic.view;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.player.PlayerGeneric;

public abstract class GameView {
	protected GController gc;

	public GameView(GController gc) {
		super();
		this.gc = gc;
	}

	//

	public abstract void initAndShow();
	//

	//

	// TODO PROXY

	/** Proxy method, beware of nulls. */
	public GModality getCurrentModality() {
		return gc.getCurrentGameModality();
	}

	/** Proxy method, beware of nulls. */
	public PlayerGeneric getCurrentPlayerInGame() {
		return gc.getCurrentGameModality().getPlayer();
	}

}