package common;

import common.abstractCommon.StatisticField;
import common.abstractCommon.referenceHolderAC.StatisticsHolderIntOnly;

public class StatisticsInt implements StatisticsHolderIntOnly {
	private static final long serialVersionUID = 98456016519L;

	public StatisticsInt() {
	}

	protected int[] statsInt;

	//

	// TODO GETTER

	@Override
	public int[] getStatsInt() {
		return statsInt;
	}

	@Override
	public int getStatInt(StatisticField sf) {
		return (statsInt == null || sf == null) ? -1 : statsInt[sf.ordinal()];
	}

	//

	// TODO SETTER

	@Override
	public StatisticsInt setStatsInt(int[] statsInt) {
		if (statsInt == null || statsInt.length > 0) this.statsInt = statsInt;
		return this;
	}

	@Override
	public StatisticsInt setStatInt(StatisticField sf, int value) {
		if (statsInt != null && statsInt.length > 0) {
			statsInt[sf.ordinal()] = value;
		}
		return this;
	}

	//

	// TODO OTHER
}