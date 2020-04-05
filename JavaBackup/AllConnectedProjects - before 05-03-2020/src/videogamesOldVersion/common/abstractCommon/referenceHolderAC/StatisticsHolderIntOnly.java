package common.abstractCommon.referenceHolderAC;

import java.io.Serializable;

import common.abstractCommon.StatisticField;
import common.abstractCommon.behaviouralObjectsAC.StatisticModifierInt;

/** Holder of a set of values. */
public interface StatisticsHolderIntOnly extends Serializable {

	public int getStatInt(StatisticField sf);

	public int[] getStatsInt();

	//

	public StatisticsHolderIntOnly setStatInt(StatisticField sf, int value);

	public StatisticsHolderIntOnly setStatsInt(int[] a);

	//

	public default StatisticsHolderIntOnly setNumberOfStats(int length) {
		int[] statsInt;
		if (length > 0) {
			statsInt = getStatsInt();
			if (statsInt == null)
				setStatsInt(new int[length]);
			else if (statsInt.length != length) {
				int[] a;
				a = new int[length];
				System.arraycopy(statsInt, 0, a, 0, Math.min(length, statsInt.length));
				setStatsInt(statsInt);
			}
		}
		return this;
	}

	public default boolean applyEffect(StatisticModifierInt stat) {
		int[] statsInt;
		StatisticField sf;
		if (stat != null && ((statsInt = getStatsInt()) != null) && statsInt.length > 0
				&& ((sf = stat.getStatField()) != null)) {
			statsInt[sf.ordinal()] += stat.getValue();
			return true;
		}
		return false;
	}

	public default boolean removeEffect(StatisticModifierInt stat) {
		int[] statsInt;
		StatisticField sf;
		if (stat != null && ((statsInt = getStatsInt()) != null) && statsInt.length > 0
				&& ((sf = stat.getStatField()) != null)) {
			statsInt[sf.ordinal()] -= stat.getValue();
			return true;
		}
		return false;
	}

}