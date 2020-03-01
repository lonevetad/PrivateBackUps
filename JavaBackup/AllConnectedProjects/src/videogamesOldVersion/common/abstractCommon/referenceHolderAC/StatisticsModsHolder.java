package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;
import java.util.Map;

import common.abstractCommon.StatisticField;
import common.abstractCommon.behaviouralObjectsAC.StatisticModifierInt;
import common.mainTools.Comparators;
import tools.RedBlackTree;

/***/
public interface StatisticsModsHolder extends Serializable {

	public static StatisticsModsHolder newDefault() {
		return new StatisticsModsHolderImpl();
	}

	//

	public Map<Integer, StatisticModifierInt> getStatMods();

	public StatisticsModsHolder setStatMods(Map<Integer, StatisticModifierInt> statsMods);

	//

	public default void applyEffect(StatisticsHolderIntOnly s) {
		// boolean changed;
		final int[] a;
		Map<Integer, StatisticModifierInt> mods;
		// changed = false;
		mods = getStatMods();
		if (s != null && mods != null) {
			a = s.getStatsInt();
			if (a != null && a.length > 0) mods.forEach((id, statMod) -> s.applyEffect(statMod));
			/*
			 * { StatisticField sf; sf = statMod.getStatField(); if (sf != null) a[sf.ordinal()] -=
			 * statMod.getValue(); }
			 */
		}
		// return changed;
	}

	public default void removeEffect(StatisticsHolderIntOnly s) {
		final int[] a;
		Map<Integer, StatisticModifierInt> mods;
		mods = getStatMods();
		if (s != null && mods != null) {
			a = s.getStatsInt();
			if (a != null && a.length > 0) mods.forEach((id, statMod) -> s.removeEffect(statMod));
		}
	}

	/**
	 * Add a {@link StatisticModifierInt} to the set through its
	 * {@link StatisticModifierInt#getIdStatAsInteger()}.
	 */
	public default boolean addStatMod(StatisticField statIdentifier, StatisticModifierInt statMod) {
		Map<Integer, StatisticModifierInt> mods;
		Integer idStat;
		if (statIdentifier != null && statMod != null && ((idStat = statIdentifier.getIdStatAsInteger()) != null)) {
			mods = getStatMods();
			if (mods != null) mods.put(idStat, statMod);
			return true;
		}
		return false;
	}

	/**
	 * Returns the previous {@link StatisticModifierInt}, if any.<br>
	 * See {@link #addStatMod(StatisticField, StatisticModifierInt)} for further informations.
	 */
	public default StatisticModifierInt removeStatMod(StatisticField statIdentifier) {
		Map<Integer, StatisticModifierInt> mods;
		Integer idStat;
		if (statIdentifier != null && ((idStat = statIdentifier.getIdStatAsInteger()) != null)) {
			mods = getStatMods();
			if (mods != null) return mods.remove(idStat);
		}
		return null;
	}

	//

	//

	public static class StatisticsModsHolderImpl implements StatisticsModsHolder {
		private static final long serialVersionUID = -560108940630L;

		public StatisticsModsHolderImpl() {
			super();
			statsMods = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
		}

		Map<Integer, StatisticModifierInt> statsMods;

		//

		@Override
		public Map<Integer, StatisticModifierInt> getStatMods() {
			return statsMods;
		}

		@Override
		public StatisticsModsHolder setStatMods(Map<Integer, StatisticModifierInt> statsMods) {
			this.statsMods = statsMods;
			return this;
		}
	}
}