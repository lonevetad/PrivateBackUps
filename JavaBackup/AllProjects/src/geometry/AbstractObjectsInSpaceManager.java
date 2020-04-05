package geometry;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.graph.PathFindStrategy;
import dataStructures.isom.IsomConsumer;

/**
 * Abstract definition of a handler and manager for objects related to a "space"
 * concept. <br>
 * I
 */
public interface AbstractObjectsInSpaceManager extends Iterator<ObjectLocated>, Serializable {

	/**
	 * Representation of the space managed by this instance.
	 */
	public AbstractShape2D getBoundingShape();

	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector();

	public ProviderShapeRunner getProviderShapeRunner();

	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector);

	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner);

	//

	//

	/**
	 * Add the given {@link ObjectLocated} into the space managed by this manager.
	 * <br>
	 * Note: Should check if the given {@link ObjectLocated} lies inside the
	 * {@link AbstractShape2D} (provided by {@link #getBoundingShape()}) through its
	 * method {@link AbstractShape2D#contains(Point2D)}.
	 */
	public boolean add(ObjectLocated o);

	public boolean contains(ObjectLocated o);

	public boolean remove(ObjectLocated o);

	public default boolean clearArea(AbstractShape2D areaToClear) {
		return remove(areaToClear, null);
	}

	public default <E extends ObjectLocated> boolean remove(AbstractShape2D areaToClear, Predicate<E> objectFilter) {
		Set<ObjectLocated> objToRemove;
		boolean[] removed = { true };
		objToRemove = fetch(areaToClear, objectFilter);
		if (objToRemove == null || objToRemove.isEmpty())
			return false;
//		objToRemove.forEach(this::remove);
		objToRemove.forEach(o -> {
			removed[0] = remove(o);
		});
		return removed[0];
	}

	//

//	public Set<ObjectLocated> fetch(double x, double y, Predicate<ObjectLocated> objectFilter);
//	public default Set<ObjectLocated> fetch(double x, double y) {return fetch(x, y, null);}
//	public default Set<ObjectLocated> fetch(Point2D whereToLookFor, Predicate<ObjectLocated> objectFilter) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY(), objectFilter);}
//	public default Set<ObjectLocated> fetch(Point2D whereToLookFor) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY());}

	/**
	 * Like {@link #fetch(AbstractShape2D)}, but providing a filtering over the
	 * found objects. A <code>null</code> filter should suggests "retrieve all
	 * objects found".
	 */
	public <E extends ObjectLocated> Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto,
			Predicate<E> objectFilter);

	/** Queris all objects located in the given area, if any. */
	public default Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto) {
		return this.fetch(areaToLookInto, null);
	}

	//

	public void forEach(ObjectsInSpaceConsumer consumer);

	public default void runOnShape(AbstractShape2D shape, IsomConsumer action) {
		AbstractShapeRunner runner;
		if (shape == null || action == null)
			return;
		runner = this.getProviderShapeRunner().getShapeRunner(shape);
		if (runner == null)
			return;
		runner.runShape(shape, action);
	}

	/**
	 * Find a path from the starting point (second parameter) to the end (third
	 * parameter).
	 */
	public <NodeType extends ObjectLocated, D extends Number> List<Point2D> getPath(
			PathFinder<NodeType, D> pathFinderStrategy, Point2D start, Point2D destination);

	/**
	 * Invoking {@link #getPath(PathFindStrategy, Point2D, Point2D)} by giving as
	 * second parameter the result of {@link ObjectLocated#getLocation()}..
	 */
	public default <NodeType extends ObjectLocated, D extends Number> List<Point2D> getPath(
			PathFinder<NodeType, D> pathFinderStrategy, ObjectLocated objRequiringToMove, Point2D destination) {
		return getPath(pathFinderStrategy, objRequiringToMove.getLocation(), destination);
	}

	/**
	 * Like {@link #getPath(PathFindStrategy, ObjectLocated, Point2D)}, but it's
	 * required to take care of the object's sizze (i.e.: the value returned by
	 * {@link ObjectShaped#getShape()} provided by the second parameter).
	 */
	public <NodeType extends ObjectLocated, D extends Number> List<Point2D> getPathForShapedObject(
			PathFinder<NodeType, D> pathFinderStrategy, ObjectShaped objRequiringTo, Point2D destination);

	//

	// TODO OIS Consumer

	public interface ObjectsInSpaceConsumer extends BiConsumer<Point2D, ObjectLocated> {

		/**
		 * Filter the whole space to the given one. If <code>null</code>, then the whole
		 * space is used
		 */
		public AbstractShape2D getArea();

		/**
		 * Filter the objects provided. If <code>null</code>, then every objects are
		 * accepted.
		 */
		public Predicate<ObjectLocated> getObjectFilter();

		public ObjectsInSpaceConsumer setArea(AbstractShape2D areaToLookInto);

		public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter);

		@Override
		public default void accept(Point2D location, ObjectLocated objectInSpace) {
			AbstractShape2D areaToLookInto;
			Predicate<ObjectLocated> objectFilter;
			areaToLookInto = getArea();
			objectFilter = getObjectFilter();
			if (areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation())))
				return;
			if (objectFilter != null && (!objectFilter.test(objectInSpace)))
				return;
			acceptImpl(location, objectInSpace);
		}

		public void acceptImpl(Point2D t, ObjectLocated u);
	}

	//

	// TODO CLASSES

	public static abstract class ObjectsInSpaceConsumerImpl implements ObjectsInSpaceConsumer {
		protected AbstractShape2D areaToLookInto;
		protected Predicate<ObjectLocated> objectFilter;

		public ObjectsInSpaceConsumerImpl() {
			this(null, null);
		}

		public ObjectsInSpaceConsumerImpl(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
			super();
			this.areaToLookInto = areaToLookInto;
			this.objectFilter = objectFilter;
		}

		//

		@Override
		public AbstractShape2D getArea() {
			return areaToLookInto;
		}

		@Override
		public Predicate<ObjectLocated> getObjectFilter() {
			return objectFilter;
		}

		@Override
		public ObjectsInSpaceConsumer setArea(AbstractShape2D areaToLookInto) {
			this.areaToLookInto = areaToLookInto;
			return this;
		}

		@Override
		public ObjectsInSpaceConsumer setObjectFilter(Predicate<ObjectLocated> objectFilter) {
			this.objectFilter = objectFilter;
			return this;
		}

		@Override
		public final void accept(Point2D location, ObjectLocated objectInSpace) {
			if (areaToLookInto != null && (!areaToLookInto.contains(objectInSpace.getLocation())))
				return;
			if (objectFilter != null && (!objectFilter.test(objectInSpace)))
				return;
			acceptImpl(location, objectInSpace);
		}
	}
}