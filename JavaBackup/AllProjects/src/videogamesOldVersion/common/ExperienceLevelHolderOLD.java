package videogamesOldVersion.common;

import java.util.Arrays;

import tools.Stringable;

public class ExperienceLevelHolderOLD implements Stringable {

	private static final long serialVersionUID = 114809520088009227L;
	public static final int DEFAULT_BASE_LINEAR_EXP_REQUIRED = 100;
	private static ExperienceLevelHolderOLD DEFAULT_INSTANCE = null;

	public static ExperienceLevelHolderOLD getDefaultInstance() {
		if (DEFAULT_INSTANCE == null) {
			DEFAULT_INSTANCE = new ExperienceLevelHolderOLD();
		}
		return DEFAULT_INSTANCE;
	}

	//

	public ExperienceLevelHolderOLD() {
		reset();
	}

	/** Copy constructor */
	public ExperienceLevelHolderOLD(ExperienceLevelHolderOLD e) {
		this.level = e.level;
		this.experienceNow = e.experienceNow;
		this.expToLevelUp = e.expToLevelUp;
	}

	protected int level, experienceNow, expToLevelUp;

	//

	// TODO GETTER

	public int getLevel() {
		return level;
	}

	public int getExperienceNow() {
		return experienceNow;
	}

	public int getExpToLevelUp() {
		return expToLevelUp;
	}

	//

	// TODO SETTER

	public ExperienceLevelHolderOLD setLevel(int level) {
		if (level >= 0)
			this.level = level;
		return this;
	}

	public ExperienceLevelHolderOLD setExperienceNow(int experienceNow) {
		if (experienceNow >= 0)
			this.experienceNow = experienceNow;
		return this;
	}

	public ExperienceLevelHolderOLD setExpToLevelUp(int experienceRequiredToLevelUp) {
		if (experienceRequiredToLevelUp > 0)
			this.expToLevelUp = experienceRequiredToLevelUp;
		return this;
	}

	//

	// TODO OTHER

	public static ExperienceLevelHolderOLD newDefaultInstance() {
		return (DEFAULT_INSTANCE != null) ? DEFAULT_INSTANCE.clone().reset()
				: (DEFAULT_INSTANCE = new ExperienceLevelHolderOLD());
	}

	public ExperienceLevelHolderOLD reset() {
		level = 0;
		experienceNow = 0;
		recalculateExpToLevelUp();
		return this;
	}

	/**
	 * Returns:
	 * <ul>
	 * <li>{@code < 0} : error (negative amount of experience)</li>
	 * <li>{@code >= 0} : amount of level gained</li>
	 * </ul>
	 */
	public int acquireExperience(int amount) {
		int levelGained, expNeededToLevelUpOnce;
		if (amount < 0)
			return -1;
		levelGained = 0;
		if (amount > 0) {
			expNeededToLevelUpOnce = expToLevelUp - experienceNow;
			if (expNeededToLevelUpOnce <= amount) {
				amount -= expNeededToLevelUpOnce;
				experienceNow = 0;
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
			experienceNow += amount;
		}
		return levelGained;
	}

	/* Just an idea */
	/**
	 * Override designed.<br>
	 * Recalculate a cache-like variable to express the amount of experience needed
	 * to level-up.
	 */
	public void recalculateExpToLevelUp() {
		expToLevelUp = (level + 1) * DEFAULT_BASE_LINEAR_EXP_REQUIRED;
	}

	@Override
	public String toString() {
		StringBuilder sb;
		toString(sb = new StringBuilder(), 0);
		return sb.toString();
	}

	@Override
	public ExperienceLevelHolderOLD clone() {
		return new ExperienceLevelHolderOLD(this);
	}

	@Override
	public void toString(StringBuilder sb, int tabLevel) {
		if (sb != null) {
			addTab(sb, tabLevel++);
			sb.append("ExperienceLevelHolder");
			addTab(sb, tabLevel);
			sb.append("level: ").append(level);
			addTab(sb, tabLevel);
			sb.append("experienceNow: ").append(experienceNow);
			addTab(sb, tabLevel);
			sb.append("\n\texperienceRequiredToLevelUp: ").append(expToLevelUp);
		}
	}

	//

	public static void main(String[] args) {
		int[] exps;
		ExperienceLevelHolderOLD elh;

		elh = ExperienceLevelHolderOLD.DEFAULT_INSTANCE;

		exps = new int[] { 1250, 700, 10, 50, 500, 666, 10000, 424 };
		System.out.println(Arrays.toString(exps));

		System.out.println(elh);
		for (int exp : exps) {
			System.out.println("\nadding " + exp);
			System.out.println("gaining " + elh.acquireExperience(exp) + " levels");
			System.out.println(elh);
		}

	}
}