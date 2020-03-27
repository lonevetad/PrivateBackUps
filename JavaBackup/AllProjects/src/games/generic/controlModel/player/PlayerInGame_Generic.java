package games.generic.controlModel.player;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;

public abstract class PlayerInGame_Generic extends PlayerGeneric implements GEventObserver {
	protected Integer ID;
	protected GModality gameModality;

	public PlayerInGame_Generic(GModality gameModality) {
		super();
		this.gameModality = gameModality;
		initializeID();
// TODO
	}

	protected void initializeID() {
		this.ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
	}

	//

	public GModality getGameModality() {
		return gameModality;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	public void setGameModality(GModality gameModality) {
		this.gameModality = gameModality;
	}

	/**
	 * Weird, but some {@link GModality} could need to set the identifier.
	 */
	public void setID(Integer iD) {
		ID = iD;
	}

	//

	//

	/**
	 * Perform a clean-up operation upon changing map, leaving the game, etc
	 */
	public abstract void onLeavingMap();

	public abstract void onEnteringInGame(GModality gm);

	//

	@Override
	public Integer getObserverID() {
		return this.getID();
	}
}