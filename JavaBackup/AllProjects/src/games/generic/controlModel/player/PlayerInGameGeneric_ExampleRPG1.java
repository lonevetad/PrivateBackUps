package games.generic.controlModel.player;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.ObjectInSpace;
import games.generic.controlModel.gameObj.WithLifeObject;
import games.generic.controlModel.utils.CurrencyHolder;

/** Designed for Role Play Game. */
public abstract class PlayerInGameGeneric_ExampleRPG1 extends PlayerIG_WithExperience
		implements ObjectInSpace, WithLifeObject {
	private static final long serialVersionUID = -777564684007L;

	int life, lifeMax;
	CurrencyHolder moneys;
	GModality gameModalty;

	public PlayerInGameGeneric_ExampleRPG1(GModality gm) {
		super(gm);
		this.gameModalty = gm;
		this.life = 1;
		this.lifeMax = 1;
	}

	//

	public GModality getGameModalty() {
		return gameModalty;
	}

	@Override
	public int getLife() {
		return this.life;
	}

	@Override
	public int getLifeMax() {
		return this.lifeMax;
	}

	public CurrencyHolder getMoneys() {
		return moneys;
	}

	//

	//

	public void setGameModalty(GModality gm) {
		this.gameModalty = gm;
	}

	@Override
	public void setLife(int life) {
		if (life >= 0)
			this.life = life;
	}

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.lifeMax = lifeMax;
			if (this.life > lifeMax)
				this.setLife(lifeMax);
		}
	}

	public void setMoneys(CurrencyHolder moneys) {
		this.moneys = moneys;
	}

	//

	//

	/**
	 * Override designed. <br>
	 * When the game actually starts and the player "drops into the game", some
	 * actions could be performed.
	 */
	public abstract void onStaringGame(GModality mg);

	//

	// TODO EVENTS FIRING

}