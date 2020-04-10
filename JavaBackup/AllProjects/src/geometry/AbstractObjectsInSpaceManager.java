package geometry;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.isom.IsomConsumer;
import dataStructures.isom.ObjLocatedCollector;
import geometry.pointTools.PointConsumer;
import tools.PathFinder;
import videogamesOldVersion.common.mainTools.mOLM.PathFinderOLD;

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

	/**
	 * Queris all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end.
	 */
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjLocatedCollector collector,
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
	public default void runOnBoundingShape(PointConsumer action) {
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
	 * Find a path from the starting point (second parameter) to the end (third
	 * parameter).
	 */
	public <NodeType, NodeContent, D extends Number> List<NodeContent> getPath(
			PathFinder<NodeType, NodeContent, D> pathFinderStrategy, Point2D start, Point2D destination);

	/**
	 * Invoking {@link #getPath(PathFinderOLD, Point2D, Point2D)} by giving as
	 * second parameter the result of {@link ObjectLocated#getLocation()}..
	 */
	public default <NodeType, NodeContent, D extends Number> List<NodeContent> getPath(
			PathFinder<NodeType, NodeContent, D> pathFinderStrategy, ObjectLocated objRequiringToMove,
			Point2D destination) {
		return getPath(pathFinderStrategy, objRequiringToMove.getLocation(), destination);
	}

	/**
	 * Like {@link #getPath(PathFinderOLD, ObjectLocated, Point2D)}, but it's
	 * required to take care of the object's sizze (i.e.: the value returned by
	 * {@link ObjectShaped#getShape()} provided by the second parameter).
	 */
	public <NodeType, NodeContent, D extends Number> List<NodeContent> getPathForShapedObject(
			PathFinder<NodeType, NodeContent, D> pathFinderStrategy, ObjectShaped objRequiringTo, Point2D destination);

	//

	// TODO OIS Consumer

}