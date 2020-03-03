package dataStructures.isom;

import java.util.Comparator;

import tools.Comparators;

public abstract class ObjectIDInteger extends ObjectWithID<Integer> {
	private static final long serialVersionUID = -70321584892380L;
	private static int progressiveID = 0;
	public static final Comparator<ObjectIDInteger> COMPARATOR_OWID = (o1, o2) -> {
		if (o1 == o2)
			return 0;
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(o1.ID, o2.ID);
	};

	public static int getProgressiveID() {
		return progressiveID;
	}

	//

	//

	public ObjectIDInteger() {
		super();
		ID = ++progressiveID;
	}

	public ObjectIDInteger(ObjectIDInteger o) {
		super(o);
	}

}