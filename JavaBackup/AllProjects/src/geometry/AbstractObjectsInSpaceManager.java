package geometry;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.IsomConsumer;
import dataStructures.isom.ObjLocatedCollectorIsom;
import geometry.pointTools.PointConsumer;
import geometry.pointTools.impl.ObjCollector;
import tools.NumberManager;
import tools.PathFinder;

/**
 * Abstract definition of a handler and manager for objects related to a "space"
 * concept. <br>
 * I
 */
public interface AbstractObjectsInSpaceManager extends Iterable<ObjectLocated>, Serializable {

	/**
	 * Representation of the space managed by this instance. Could be something
	 * simple or complex.<br>
	 * It's mandatory, usually.
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
	 * method {@link AbstractShape2D#contains(Point)}.
	 */
	public boolean add(ObjectLocated o);

	public boolean contains(ObjectLocated o);

	public boolean remove(ObjectLocated o);

	public default boolean clearArea(AbstractShape2D areaToClear) {
		return remove(areaToClear, null);
	}

	public default boolean remove(AbstractShape2D areaToClear, Predicate<ObjectLocated> objectFilter) {
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
//	public default Set<ObjectLocated> fetch(Point whereToLookFor, Predicate<ObjectLocated> objectFilter) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY(), objectFilter);}
//	public default Set<ObjectLocated> fetch(Point whereToLookFor) {
//		return this.fetch(whereToLookFor.getX(), whereToLookFor.getY());}

	public ObjLocatedCollectorIsom newObjLocatedCollector(Predicate<ObjectLocated> objectFilter);

	/**
	 * Like {@link #fetch(AbstractShape2D)}, but providing a filtering over the
	 * found objects. A <code>null</code> filter should suggests "retrieve all
	 * objects found".
	 */
	public default Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter) {
		ObjLocatedCollectorIsom c;
		c = newObjLocatedCollector(objectFilter);
		this.runOnShape(areaToLookInto, c);
		return c.getCollectedObjects();
	}

	/** Queris all objects located in the given area, if any. */
	public default Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto) {
		return this.fetch(areaToLookInto, null);
	}

	/**
	 * Queris all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end.
	 */
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
			List<Point> path);

	/**
	 * Queris all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end.
	 */
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, List<Point> path) {
		return this.findInPath(areaToLookInto, null, path);
	}

	//
	/**
	 * As for {@link #runOnShape(AbstractShape2D, IsomConsumer)}, but giving the
	 * {@link AbstractShape2D} returned by {@link #getSpaceShape()}.
	 */
	public default void runOnWholeMap(PointConsumer action) {
		runOnShape(getBoundingShape(), action);
	}

	public default void runOnShape(AbstractShape2D shape, PointConsumer action) {
		AbstractShapeRunner runner;
		if (shape == null || action == null)
			return;
		runner = this.getProviderShapeRunner().getShapeRunner(shape);
		if (runner == null)
			return;
		runner.runShape(shape, action);
	}

	/**
	 * Find a path from the starting point (first parameter) to the end (second
	 * parameter).<br>
	 * See {@link PathFinder#getPath(Object, Object, NumberManager, Predicate)}.
	 */
	public default <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(NodeType start,
			NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder, NumberManager<D> numManager,
			Predicate<NodeContent> isWalkableTester) {
		return pathFinder.getPath(start, destination, numManager, isWalkableTester);
	}

	/**
	 * See {@link #getPath(Point, Point, PathFinder, NumberManager, Predicate)}.
	 */
	public default <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(NodeType start,
			NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder, NumberManager<D> numManager) {
		return this.getPath(start, destination, pathFinder, numManager, null);
	}

	/**
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public default <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(
			ObjectShaped objRequiringToMove, NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder,
			NumberManager<D> numManager, Predicate<NodeContent> isWalkableTester) {
		return pathFinder.getPath(objRequiringToMove, destination, numManager, isWalkableTester);
	}

	/**
	 * See {@link PathFinder#getPath(ObjectShaped, Object, NumberManager)}.
	 */
	public default <NodeType extends Point, NodeContent, D extends Number> List<NodeType> getPath(
			ObjectShaped objRequiringToMove, NodeType destination, PathFinder<NodeType, NodeContent, D> pathFinder,
			NumberManager<D> numManager) {
		return this.getPath(objRequiringToMove, destination, pathFinder, numManager, null);
	}

	//

	// TODO OIS Consumer

}