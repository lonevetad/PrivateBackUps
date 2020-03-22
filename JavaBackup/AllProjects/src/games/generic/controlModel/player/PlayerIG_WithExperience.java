package games.generic.controlModel.player;

import games.generic.controlModel.GModality;

public abstract class PlayerIG_WithExperience extends PlayerInGame_Generic {

	public PlayerIG_WithExperience(GModality gameModality) {
		super(gameModality);
	}

	protected ExperienceLevelHolder expLevelHolder;

	public ExperienceLevelHolder getExpLevelHolder() {
		return expLevelHolder;
	}

	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder) {
		this.expLevelHolder = expLevelHolder;
	}

	//

	public int getExp() {
		return this.getExpLevelHolder().getExperienceNow();
	}

	public int getLevel() {
		return this.getExpLevelHolder().getLevel();
	}

	/** See {@link ExperienceLevelHolder#acquireExperience(int)}. */
	public int gainExp(int exp) {
		int levelGained, lgT, startingLevel; // , oldExp
		startingLevel = this.getExpLevelHolder().getLevel();
//		oldExp = this.getExpLevelHolder().getExperienceNow();
		levelGained = lgT = this.getExpLevelHolder().acquireExperience(exp);
		fireExpGainedEvent(gameModality, exp);
		while(lgT-- >= 0) {
			fireLevelGainedEvent(gameModality, ++startingLevel);
		}
		return levelGained;
	}

	//

	public abstract void fireExpGainedEvent(GModality gm, int expGained);

	public abstract void fireLevelGainedEvent(GModality gm, int newLevel);
}
