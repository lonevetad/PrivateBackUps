package tests.tGame.tgEvent1;

import games.generic.controlModel.GModality;
import games.theRisingAngel.PlayerTRAr;

public class Player_E1 extends PlayerTRAr {
	private static final long serialVersionUID = 210054045201L;
	static final int LIFE_MAX = 100;

	public Player_E1(GModality gm) {
		super(gm);
		this.setLife(LIFE_MAX);
	}

}