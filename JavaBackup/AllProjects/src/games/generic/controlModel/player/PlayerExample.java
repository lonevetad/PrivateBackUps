package games.generic.controlModel.player;

import games.generic.controlModel.CurrencyHolder;
import games.generic.controlModel.GameModality;
import games.generic.controlModel.gameObj.ObjectInSpace;
import games.generic.controlModel.gameObj.WithLifeObject;

public abstract class PlayerExample extends ObjectInSpace implements WithLifeObject {
	private static final long serialVersionUID = -777564684007L;

	int life, lifeMax;
	String name;
	ExperienceLevelHolder expLevelHolder;
	CurrencyHolder moneys;
	GameModality gameModalty;

	public PlayerExample(GameModality gm) {
		this.gameModalty = gm;
		this.life = 1;
		this.lifeMax = 1;
	}

	//

	public GameModality getGameModalty() {
		return gameModalty;
	}

	@Override
	public int getLife() {
		return this.life;
	}

	public String getName() {
		return name;
	}

	@Override
	public int getLifeMax() {
		return this.lifeMax;
	}

	public ExperienceLevelHolder getExpLevelHolder() {
		return expLevelHolder;
	}

	public int getLevel() {
		return this.getExpLevelHolder().getLevel();
	}

	public CurrencyHolder getMoneys() {
		return moneys;
	}

	public int getExp() {
		return this.getExpLevelHolder().getExperienceNow();
	}

	//

	//

	public void setGameModalty(GameModality gm) {
		this.gameModalty = gm;
	}

	public void setName(String name) {
		this.name = name;
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

	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder) {
		this.expLevelHolder = expLevelHolder;
	}

	public void setMoneys(CurrencyHolder moneys) {
		this.moneys = moneys;
	}

	//

	/** See {@link ExperienceLevelHolder#acquireExperience(int)}. */
	public int gainExp(int exp) {
		int levelGained, lgT, startingLevel; // , oldExp
		startingLevel = this.getExpLevelHolder().getLevel();
//		oldExp = this.getExpLevelHolder().getExperienceNow();
		levelGained = lgT = this.getExpLevelHolder().acquireExperience(exp);
		fireExpGainedEvent(exp, gameModalty);
		while(lgT-- >= 0) {
			fireLevelGainedEvent(++startingLevel, gameModalty);
		}
		return levelGained;
	}

	//

	public abstract void fireExpGainedEvent(int expGained, GameModality gm);

	public abstract void fireLevelGainedEvent(int newLevel, GameModality gm);

}