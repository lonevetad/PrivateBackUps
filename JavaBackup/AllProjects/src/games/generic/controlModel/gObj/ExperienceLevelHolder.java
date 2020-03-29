package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.player.ExperienceLevelHolderImpl;
import tools.Stringable;

public interface ExperienceLevelHolder extends Stringable {

	public int getLevel();

	public int getExperienceNow();

	public int getExpToLevelUp();

	//

	public ExperienceLevelHolderImpl setLevel(int level);

	public ExperienceLevelHolderImpl setExperienceNow(int experienceNow);

	public ExperienceLevelHolderImpl setExpToLevelUp(int experienceRequiredToLevelUp);

	//

	public ExperienceLevelHolderImpl reset();

	/**
	 * Acquire the given experience and level up if able. <br>
	 * After this invocation, use the returned value to call (or not)
	 * {@link #fireLevelGainedEvent(GModality, int)}. Returns:
	 * <ul>
	 * <li>{@code < 0} : error (negative amount of experience)</li>
	 * <li>{@code >= 0} : amount of level gained</li>
	 * </ul>
	 */
	public default int acquireExperience(int amount) {
		int levelGained, expNeededToLevelUpOnce, ///
				expToLevelUp, level;
		if (amount < 0)
			return -1;
		levelGained = 0;
		expToLevelUp = getExpToLevelUp();
		level = getLevel();
		if (amount > 0) {
			expNeededToLevelUpOnce = expToLevelUp - getExperienceNow();
			if (expNeededToLevelUpOnce <= amount) {
				amount -= expNeededToLevelUpOnce;
				setExperienceNow(0);
				level++;
				levelGained++;
				recalculateExpToLevelUp();
				/*
				 * un livello completato, "la barra dell'esperienza ora e' azzerata" .. ora si
				 * fanno tutti i vari livelli intermedi
				 */
				while(amount >= expToLevelUp) {
					amount -= expToLevelUp;
					level++;
					levelGained++;
					recalculateExpToLevelUp();
				}
			}
			setLevel(level);
			setExperienceNow(getExperienceNow() + amount);
		}
		return levelGained;
	}

	/* Just an idea */
	/**
	 * Override designed.<br>
	 * Recalculate a cache-like variable to express the amount of experience needed
	 * to level-up.
	 */
	public void recalculateExpToLevelUp();

	//

//	public void fireLevelGainedEvent(GModality gm, int levelsGained);

}