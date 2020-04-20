package games.generic.controlModel.player;

import games.generic.controlModel.gObj.ExperienceLevelHolder;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;

/** Delegates to a {@link ExperienceLevelHolderImpl} and add some features. */
public interface PlayerWithExperience extends PlayerGeneric, ExperienceLevelHolder {

	public ExperienceLevelHolder getExpLevelHolder();

	public void setExpLevelHolder(ExperienceLevelHolder expLevelHolder);

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
	public default ExperienceLevelHolder setLevel(int level) {
		return getExpLevelHolder().setLevel(level);
	}

	@Override
	public default ExperienceLevelHolder setExperienceNow(int experienceNow) {
		return getExpLevelHolder().setExperienceNow(experienceNow);
	}

	@Override
	public default ExperienceLevelHolder setExpToLevelUp(int experienceRequiredToLevelUp) {
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

	@Override
	public default void recalculateExpToLevelUp() {
		this.getExpLevelHolder().recalculateExpToLevelUp();
	}

	/** See {@link ExperienceLevelHolder#acquireExperience(int)}. */
	public default int gainExp(int exp) {
		int levelGained;
		GModalityET gm;
		GEventInterfaceRPG geirpg;
		gm = (GModalityET) this.getGameModality();
		levelGained = this.getExpLevelHolder().acquireExperience(exp);
		geirpg = (GEventInterfaceRPG) gm.getEventInterface();
		geirpg.fireExpGainedEvent(gm, exp);
		geirpg.fireLevelGainedEvent(gm, levelGained);
		return levelGained;
	}

	//

}
