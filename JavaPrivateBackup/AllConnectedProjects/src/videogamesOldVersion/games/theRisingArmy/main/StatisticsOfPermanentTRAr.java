package games.theRisingArmy.main;

import java.util.function.Consumer;

import common.StatisticsInt;
import common.abstractCommon.StatisticField;
import common.abstractCommon.behaviouralObjectsAC.MyComparator;

public class StatisticsOfPermanentTRAr extends StatisticsInt {
	private static final long serialVersionUID = 4022140784800231994L;

	/** ONLY THE MAX VALUES, NOT THE CURRENT ONES */
	public static enum StatsTRAr implements StatisticField {
		STRENGTH, LIFE_MAX, STAMINA_MAX, STAMINA_AT_START, // LIFE, STAMINA
		LIFE_EACH_RECHARGE_SUBPHASE, STAMINA_EACH_RECHARGE_SUBPHASE, //
		STAMINA_CONSUMED_EACH_ATTACK, STAMINA_CONSUMED_EACH_STEP_WALKED, //
		MICROPIXEL_EACH_STEP;
		//

		public static final MyComparator<StatsTRAr> COMPARATOR_STAT_TRAR = (e1, e2) -> {
			int o1, o2;
			if (e1 == e2) return 0;
			if (e1 == null) return -1;
			if (e2 == null) return 1;
			return
			// e1.compareTo(e2)
			((o1 = e1.ordinal()) == (o2 = e2.ordinal())) ? 0 : (o1 > o2 ? 1 : -1);
		};

		final boolean isBounded;
		Integer idStat;

		StatsTRAr() {
			this(false);
		}

		StatsTRAr(boolean b) {
			isBounded = b;
			this.idStat = ordinal();
		}

		@Override
		public boolean isBounded() {
			return isBounded;
		}

		@Override
		public Integer getIdStatAsInteger() {
			return idStat;
		}
	}

	public static final StatsTRAr[] valuesStatsTRAr = StatsTRAr.values();
	public static final int LENGTH_StatsTRAr = valuesStatsTRAr.length;

	public StatisticsOfPermanentTRAr() {
		super();
		setNumberOfStats(LENGTH_StatsTRAr);
	}

	//

	//

	public StatisticsOfPermanentTRAr forEachStat(Consumer<StatsTRAr> toDo) {
		if (toDo != null) {
			int i, len;
			StatsTRAr[] v;
			v = valuesStatsTRAr;
			len = LENGTH_StatsTRAr;
			i = -1;
			while (++i < len) {
				toDo.accept(v[i]);
			}
		}
		return this;
	}
}