package games.theRisingArmy.abstractTRAr;

import java.io.Serializable;

public interface TurnPhaseHolder extends Serializable {
	public static enum TurnPhase implements Serializable {
		StartTurn,
		/** MTG's UNTAP */
		Recharge, Draw, Main, End;

		private static final long serialVersionUID = -965120781001808017L;
	}

	public TurnPhase getTurnPhase();

	public TurnPhaseHolder setTurnPhase(TurnPhase turnPhase);
}