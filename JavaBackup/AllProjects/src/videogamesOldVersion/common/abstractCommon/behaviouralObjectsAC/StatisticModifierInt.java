package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

import common.abstractCommon.StatisticField;
import common.abstractCommon.referenceHolderAC.StatisticsHolderIntOnly;

public interface StatisticModifierInt extends Serializable {

	public int getValue();

	public StatisticField getStatField();

	public StatisticModifierInt setValue(int value);

	public StatisticModifierInt setStatField(StatisticField statField);

	/** Override to optimize */
	public default boolean applyEffect(StatisticsHolderIntOnly stat) {
		// int value;
		StatisticField sf;
		sf = getStatField();
		// value = getValue();
		if (sf != null && stat != null) {
			return stat.applyEffect(this);
			// setStatInt(sf, stat.getStatInt(sf) + value);
		}
		return false;
	}

	/** Override to optimize */
	public default boolean removeEffect(StatisticsHolderIntOnly stat) {
		// int value;
		StatisticField sf;
		sf = getStatField();
		// value = getValue();
		if (sf != null && stat != null) {
			return stat.removeEffect(this);
			// setStatInt(sf, stat.getStatInt(sf) - value);
		}
		return false;
	}
}