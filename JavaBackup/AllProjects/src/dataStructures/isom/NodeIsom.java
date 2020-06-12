package dataStructures.isom;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import geometry.ObjectLocated;
import geometry.ObjectLocated.PointWrapper;
import tools.Comparators;

public abstract class NodeIsom extends PointWrapper implements Iterable<ObjectLocated>, Serializable {
	private static final long serialVersionUID = 4052487990441743L;
	public static final Comparator<NodeIsom> COMPARATOR_NODE_ISOM_ID = (n1, n2) -> {
		if (n1 == n2)
			return 0;
		if (n1 == null)
			return -1;
		if (n2 == null)
			return 1;
		return Comparators.INTEGER_COMPARATOR.compare(n1.getID(), n2.getID());
	}, //
			COMPARATOR_NODE_ISOM_POINT = Comparators.POINT_2D_COMPARATOR_LOWEST_LEFTMOST_FIRST::compare;

	public NodeIsom() { this(0, 0); }

	public NodeIsom(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Add the given object to this node. This node could store just a single object
	 * or a {@link Set }, it depends on implementation.
	 */
	public abstract boolean addObject(ObjectLocated o);

	public abstract int countObjectAdded();

	/** Return an object given an index */
	public abstract ObjectLocated getObject(int i);

	/** Return an object given an identifier */
	public abstract ObjectLocated getObject(Integer ID);

	/** Return an object satisfying a filer (no order is guaranteed)-. */
	public abstract ObjectLocated getObject(Predicate<ObjectLocated> filter);

	public abstract boolean removeObject(Integer ID);

	public abstract boolean removeObject(ObjectLocated o);

	public abstract boolean removeObject(Predicate<ObjectLocated> filter);

	public abstract boolean removeAllObjects();

	/**
	 * Optionally considering the zero or more {@link ObjectLocated} stored in this
	 * node, check if this node could be considered as a "walkable node" or just a
	 * "wall", depending on the given test checker (the parameter
	 * {@link Predicate}).<br>
	 * That predicate MUST return <code>true</code> if this node could be considered
	 * walkable.
	 */
	public abstract boolean isWalkable(Predicate<ObjectLocated> isWalkableTester);

	public abstract void forEachHeldObject(Consumer<ObjectLocated> action);

	/**
	 * Perform an action to all held objects that passes a given filter (the first
	 * parameter).
	 */
	public void forEachAcceptableObject(Predicate<ObjectLocated> objFilter, Consumer<ObjectLocated> action) {
		this.forEachHeldObject(o -> {
			if (objFilter == null || objFilter.test(o))
				action.accept(o);
		});
	}

//	public void setLocation(Point location) {
//		this.x = location.x;
//		this.y = location.y;
//	}
//	public void setLocation(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
}