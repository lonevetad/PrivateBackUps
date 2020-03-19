package tests.tGame.tgEvent1;

import games.generic.controlModel.player.PlayerGeneric_Eample1;

public class Player_E1 extends PlayerGeneric_Eample1 {
	static final int LIFE_MAX = 100;

	public Player_E1() {
		super();
		this.life = LIFE_MAX;
		money = 0;
	}

}