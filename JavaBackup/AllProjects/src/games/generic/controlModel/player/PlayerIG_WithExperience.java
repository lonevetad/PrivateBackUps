package games.generic.controlModel.player;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.ExperienceLevelHolder;

public interface PlayerIG_WithExperience extends PlayerGeneric, ExperienceLevelHolder {

	public ExperienceLevelHolderImpl getExpLevelHolder();

	public void setExpLevelHolder(ExperienceLevelHolderImpl expLevelHolder);

//	protected ExperienceLevelHolderImpl expLevelHolder;

	@Override
	public default int acquireExperience(int amount) {
		return getExpLevelHolder().acquireExperience(amount);
	}

	@Override
	public default int getExpToLevelUp() {
		return getExpLevelHolder().getExpToLevelUp();
	}

	@Override
	public default ExperienceLevelHolderImpl setLevel(int level) {
		return getExpLevelHolder().setLevel(level);
	}

	@Override
	public default ExperienceLevelHolderImpl setExperienceNow(int experienceNow) {
		return getExpLevelHolder().setExperienceNow(experienceNow);
	}

	@Override
	public default ExperienceLevelHolderImpl setExpToLevelUp(int experienceRequiredToLevelUp) {
		return getExpLevelHolder().setExpToLevelUp(experienceRequiredToLevelUp);
	}

	//

	public default int getExp() {
		return this.getExpLevelHolder().getExperienceNow();
	}

	@Override
	public default int getLevel() {
		return this.getExpLevelHolder().getLevel();
	}

	/** See {@link ExperienceLevelHolderImpl#acquireExperience(int)}. */
	public default int gainExp(int exp) {
		int levelGained;
		GModality gm;
		gm = this.getGameModality();
		levelGained = this.getExpLevelHolder().acquireExperience(exp);
		fireExpGainedEvent(gm, exp);
		fireLevelGainedEvent(gm, levelGained);
		return levelGained;
	}

	//

	public abstract void fireExpGainedEvent(GModality gm, int expGained);

	public abstract void fireLevelGainedEvent(GModality gm, int levelGained);
}
