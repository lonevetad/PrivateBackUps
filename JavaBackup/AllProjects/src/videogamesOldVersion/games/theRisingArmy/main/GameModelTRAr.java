package games.theRisingArmy.main;

import videogamesOldVersion.common.abstractCommon.AbstractSaveData;
import videogamesOldVersion.common.abstractCommon.GameModelGeneric;
import videogamesOldVersion.common.abstractCommon.GameModelGeneric.MementoGameModelGeneric;
import videogamesOldVersion.games.theRisingArmy.MainController_TheRisingArmy;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.GameObjectTRAr;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.TurnPhaseHolder;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.TurnPhaseHolder.TurnPhase;

public class GameModelTRAr extends GameModelGeneric implements TurnPhaseHolder {

	private static final long serialVersionUID = -965120781001808018L;

	public GameModelTRAr() { super(); }

	public GameModelTRAr(MainController_TheRisingArmy mainController) { super(mainController); }

	public GameModelTRAr(MementoGameModel_TRAR memento) { super(memento); }

	TurnPhase turnPhase;
	RedBlackTree<Integer, GameObjectTRAr> allObjectsInGame;

	//

	// TODO GETTER

	@Override
	public TurnPhase getTurnPhase() { return turnPhase; }

	/**
	 * All objects like players, runes, creatures, artifacts and enchantements
	 * should be placed it and fetched through
	 * {@link GameObjectTRAr#getIdTrarObject()}.
	 */
	public RedBlackTree<Integer, GameObjectTRAr> getAllObjectsInGame() { return allObjectsInGame; }

	//

	// TODO SETTER

	@Override
	public TurnPhaseHolder setTurnPhase(TurnPhase turnPhase) {
		this.turnPhase = turnPhase;
		return this;
	}

	//

	// TODO OTHER

	@Override
	public AbstractSaveData newSaves() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MementoGameModelGeneric createMemento() { return new MementoGameModel_TRAR(this); }

	//

	// TODO MEMENTO

	public static class MementoGameModel_TRAR extends MementoGameModelGeneric {
		private static final long serialVersionUID = 515061519L;

		public MementoGameModel_TRAR() { super(); }

		public MementoGameModel_TRAR(GameModelTRAr gmg) { super(gmg); }

		@Override
		public GameModelGeneric reinstanceFromMe() { return new GameModelTRAr(this); }
	}

}