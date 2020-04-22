package common;

import java.util.Map;

import common.abstractCommon.StatisticField;
import common.abstractCommon.referenceHolderAC.StatisticsHolder;
import common.abstractCommon.referenceHolderAC.StatisticsHolderIntOnly;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tools.RedBlackTree;

public class StatisticsGeneric implements StatisticsHolder {
	private static final long serialVersionUID = -+952014041L;

	public StatisticsGeneric() {
	}

	RedBlackTree<Integer, Number> stats;

	//

	// TODO GETTER

	@Override
	public RedBlackTree<Integer, Number> getStats() {
		return stats;
	}

	@Override
	public int[] getStatsInt() {
		return null;
	}

	@Override
	public Number getStat(StatisticField sf) {
		RedBlackTree<Integer, Number> m;
		m = stats;
		return (m != null) ? m.fetch(sf.getIdStat()) : null;
	}

	//

	// TODO SETTER

	@Override
	public StatisticsHolder setStats(Map<Integer, Number> stats) {
		if (stats instanceof RedBlackTree<?, ?>) this.stats = (RedBlackTree<Integer, Number>) stats;
		return this;
	}

	@Override
	public StatisticsHolder setStat(StatisticField sf, Number value) {
		RedBlackTree<Integer, Number> m;
		m = stats;
		if (m != null) m.add(sf.getIdStatAsInteger(), value);
		return this;
	}

	@Override
	public StatisticsHolderIntOnly setStatsInt(int[] a) {
		throw new NotImplementedException();
	}
}