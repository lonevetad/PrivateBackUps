package common.abstractCommon.referenceHolderAC;

import java.util.Map;

import common.abstractCommon.StatisticField;
import tools.RedBlackTree;

/**
 * As it superclass {@link StatisticsHolderIntOnly}, but thought to hold a generic
 * {@link Number}s.<br>
 * Should not be used
 */
public interface StatisticsHolder extends StatisticsHolderIntOnly {

	@Override
	public default int getStatInt(StatisticField sf) {
		Number n;
		n = getStat(sf);
		return n == null ? -1 : n.intValue();
	}

	@Override
	public default StatisticsHolder setStatInt(StatisticField sf, int value) {
		setStat(sf, new Integer(value));
		return this;
	}

	//

	public Map<Integer, Number> getStats();

	public StatisticsHolder setStats(Map<Integer, Number> stats);

	//

	public default Number getStat(StatisticField sf) {
		Map<Integer, Number> m;
		m = getStats();
		// return (m != null) ? m.get(sf.getIdStatAsInteger()) : null;
		if (m != null) {
			return (m instanceof RedBlackTree<?, ?>)
					? ((RedBlackTree<Integer, Number>) m).fetch(sf.getIdStatAsInteger())
					: m.get(sf.getIdStatAsInteger());
		}
		return null;
	}

	public default StatisticsHolder setStat(StatisticField sf, Number value) {
		Map<Integer, Number> m;
		m = getStats();
		if (m != null) {
			if (m instanceof RedBlackTree<?, ?>) {
				if (value != null)
					((RedBlackTree<Integer, Number>) m).add(sf.getIdStatAsInteger(), value);
				else
					((RedBlackTree<Integer, Number>) m).delete(sf.getIdStatAsInteger());
			} else {
				if (value != null)
					m.put(sf.getIdStatAsInteger(), value);
				else
					m.remove(sf.getIdStatAsInteger());
			}
		}
		return this;
	}
}