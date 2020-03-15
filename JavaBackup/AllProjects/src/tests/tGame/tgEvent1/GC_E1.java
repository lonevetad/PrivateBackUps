package tests.tGame.tgEvent1;

import games.generic.controlModel.subImpl.GameControllerET;

public class GC_E1 extends GameControllerET {

	public static final String GM_NAME = "TEST";

	public boolean isAlive;

	public GC_E1() {
		super();
	}

	@Override
	protected void defineGameModalitiesFactories() {
		this.getGameModalitiesFactories().put(GM_NAME, (name, gc) -> {
			return new GModality_E1(name, gc);
		});
	}

	@Override
	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public void init() {
		isAlive = true;
		super.setCurrentGameModality(this.newModalityByName(GM_NAME));
	}

	@Override
	public void closeAll() {
		isAlive = false;
		this.getCurrentGameModality().closeAll();
	}

}