package games.generic.controlModel;

import java.util.Comparator;

import tools.Comparators;
import videogames.gridObjectManager.core.ObjectWithID;

public interface ObjectWIthID {
	public static final Comparator<ObjectWithID> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.getID(), o2.getID());
	};

	/** BEWARE of non uniqueness caused by bad management ! */
	public Integer getID();
}