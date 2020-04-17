package tests.tGame.tgEvent1;

import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.PlayerTRAr;

public class Player_E1 extends PlayerTRAr {
	private static final long serialVersionUID = 210054045201L;
	static final int LIFE_MAX = 100;

	public Player_E1(GModalityRPG gm) {
		super(gm);
		this.setLifeMax(LIFE_MAX);
		this.setLife(LIFE_MAX);
	}

	//

	// TODO FIRE EVENTS
}