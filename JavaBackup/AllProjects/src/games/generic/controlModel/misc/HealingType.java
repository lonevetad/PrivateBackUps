package games.generic.controlModel.misc;

import java.util.Comparator;

import tools.Comparators;
import tools.ObjectNamedID;

public interface HealingType extends ObjectNamedID {
	public static final Comparator<HealingType> COMPARATOR_HEALING_TYPE = (ht1, ht2) -> {
		if (ht1 == ht2)
			return 0;
		if (ht1 == null)
			return -1;
		if (ht2 == null)
			return 1;
		return Comparators.STRING_COMPARATOR.compare(ht1.getName(), ht2.getName());
	};

}