package tests.tGame.tgEvent1;

import games.generic.controlModel.GModality;
import games.generic.controlModel.player.PlayerInGameGeneric_ExampleRPG1;
import geometry.AbstractShape2D;

public class Player_E1 extends PlayerInGameGeneric_ExampleRPG1 {
	private static final long serialVersionUID = 210054045201L;
	static final int LIFE_MAX = 100;

	public Player_E1(GModality gm) {
		super(gm);
		this.setLife(LIFE_MAX);
	}

	@Override
	public AbstractShape2D getAbstractShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAbstractShape(AbstractShape2D shape) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fireDestruction(GModality gm) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onStaringGame(GModality mg) {

	}

	@Override
	public Integer getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fireExpGainedEvent(GModality gm, int expGained) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireLevelGainedEvent(GModality gm, int newLevel) {
		// TODO Auto-generated method stub

	}

}