package games.generic.controlModel.player;

import games.generic.controlModel.GModality;

public abstract class PlayerInGame_Generic extends PlayerGeneric {
	protected GModality gameModality;

	public PlayerInGame_Generic(GModality gameModality) {
		this.gameModality = gameModality;
		// TODO
	}

	public GModality getGameModality() {
		return gameModality;
	}

	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	//

	/*** Perform a clean-up operation upon changing map, leaving the game, etc */
	public abstract void onLeavingMap();
}