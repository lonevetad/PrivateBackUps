package tests.tGame.tgEvent1;

import games.generic.controlModel.GEvent;
import games.generic.controlModel.GModality;
import games.generic.controlModel.subImpl.PlayerInGameGeneric_ExampleRPG1;
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
	public void receiveDamage(GModality gm, int damage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveHealing(GModality gm, int healingAmount) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDestroyed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean destroy() {
		// TODO Auto-generated method stub
		fireDestruction(getGameModality());
		return false;
	}

	@Override
	public void onLeavingMap() {
		// TODO Auto-generated method stub

	}

	//

	@Override
	public void notifyEvent(GModality modality, GEvent ge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartingGame(GModality mg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fireDamageReceived(GModality gm, int originalDamage) { // , int actualDamageReceived
		// TODO Auto-generated method stub
	}

	@Override
	public void fireHealingReceived(GModality gm, int originalHealing) { // , int actualHealingReceived
		// TODO Auto-generated method stub

	}

	@Override
	public void fireDestruction(GModality gm) {
		// TODO Auto-generated method stub

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