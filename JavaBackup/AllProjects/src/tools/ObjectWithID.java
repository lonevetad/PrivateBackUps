package tools;

import java.util.Comparator;
import java.util.function.Function;

public interface ObjectWithID extends ObjWithIDGeneric<Integer> {
	public static final Comparator<ObjectWithID> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.getID(), o2.getID());
	};
	public static final Function<ObjectWithID, Integer> KEY_EXTRACTOR = o -> o.getID();

	//

	// TODO methods

	/** BEWARE of non uniqueness caused by bad management ! */
	@Override
	public Integer getID();
}