package common;

import java.util.Arrays;

import common.abstractCommon.behaviouralObjectsAC.PartOfToString;

public class ExperienceLevelHolder implements PartOfToString {

	private static final long serialVersionUID = 114809520088009227L;
	private static ExperienceLevelHolder DEFAULT_INSTANCE;

	public static final int DEFAULT_BASE_LINEAR_EXP_REQUIRED = 100;

	public ExperienceLevelHolder() {
		reset();
	}

	public ExperienceLevelHolder(ExperienceLevelHolder e) {
		this.level = e.level;
		this.experienceNow = e.experienceNow;
		this.experienceRequiredToLevelUp = e.experienceRequiredToLevelUp;
	}

	protected int level, experienceNow, experienceRequiredToLevelUp;

	//

	// TODO GETTER

	public int getLevel() {
		return level;
	}

	public int getExperienceNow() {
		return experienceNow;
	}

	public int getExperienceRequiredToLevelUp() {
		return experienceRequiredToLevelUp;
	}

	//

	// TODO SETTER

	public ExperienceLevelHolder setLevel(int level) {
		if (level >= 0) this.level = level;
		return this;
	}

	public ExperienceLevelHolder setExperienceNow(int experienceNow) {
		if (experienceNow >= 0) this.experienceNow = experienceNow;
		return this;
	}

	public ExperienceLevelHolder setExperienceRequiredToLevelUp(int experienceRequiredToLevelUp) {
		if (experienceRequiredToLevelUp > 0) this.experienceRequiredToLevelUp = experienceRequiredToLevelUp;
		return this;
	}

	//

	// TODO OTHER

	public static ExperienceLevelHolder newDefaultInstance() {
		return (DEFAULT_INSTANCE != null) ? DEFAULT_INSTANCE.clone().reset()
				: (DEFAULT_INSTANCE = new ExperienceLevelHolder());
	}

	public ExperienceLevelHolder reset() {
		level = 0;
		experienceNow = 0;
		recalculateExperienceRequiredToLevelUp();
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
		if (amount < 0) return -1;
		levelGained = 0;
		if (amount > 0) {
			expNeededToLevelUpOnce = experienceRequiredToLevelUp - experienceNow;
			if (expNeededToLevelUpOnce <= amount) {
				amount -= expNeededToLevelUpOnce;
				experienceNow = 0;
				level++;
				levelGained++;
				recalculateExperienceRequiredToLevelUp();
				/*
				 * un livello completato,
				 * "la barra dell'esperienza ora e' azzerata" .. ora si fanno
				 * tutti i vari livelli intermedi
				 */
				while (amount >= experienceRequiredToLevelUp) {
					amount -= experienceRequiredToLevelUp;
					level++;
					levelGained++;
					recalculateExperienceRequiredToLevelUp();
				}
			}
			experienceNow += amount;
		}
		return levelGained;
	}

	/* Just an idea */
	public void recalculateExperienceRequiredToLevelUp() {
		experienceRequiredToLevelUp = (level + 1) * DEFAULT_BASE_LINEAR_EXP_REQUIRED;
	}

	@Override
	public String toString() {
		StringBuilder sb;
		toString(sb = new StringBuilder(), 0);
		return sb.toString();
	}

	@Override
	public ExperienceLevelHolder clone() {
		return new ExperienceLevelHolder(this);
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
			sb.append("\n\texperienceRequiredToLevelUp: ").append(experienceRequiredToLevelUp);
		}
	}

	//

	public static void main(String[] args) {
		int[] exps;
		ExperienceLevelHolder elh;

		elh = ExperienceLevelHolder.DEFAULT_INSTANCE;

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