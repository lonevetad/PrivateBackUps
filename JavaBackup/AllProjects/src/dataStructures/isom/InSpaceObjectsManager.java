package dataStructures.isom;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.ListMapped;
import geometry.AbstractObjectsInSpaceManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathOptimizer;
import geometry.implementations.shapes.ShapeLine;
import geometry.pointTools.PointConsumer;
import geometry.pointTools.impl.ObjCollector;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.PathFinder;

public interface InSpaceObjectsManager<Distance extends Number>
//extends dataStructures.graph.GraphSimple<NodeIsom, D>
		extends AbstractObjectsInSpaceManager<Distance>, NodeIsomProvider<Distance>, ObjectShaped {

	//

	// TODO GETTER
	public LoggerMessages getLog();

	public NumberManager<Distance> getWeightManager();

	public PathOptimizer<Point> getPathOptimizer();

//	public PathFinder<Point, ObjectLocated, Distance> getPathFinder();
	public PathFinderIsom<Distance> getPathFinder();

	public Set<ObjectLocated> getAllObjectLocated();

	@Override
	public default AbstractShape2D getShape() { return getBoundingShape(); }

	public default int getWidth() { return getShape().getWidth(); }

	public default int getHeight() { return getShape().getHeight(); }

	public default Point getLocationAbsolute() { return getShape().getCenter(); }

	public default Point getTopLetCornerAbsolute() {
		Point c;
		c = getLocationAbsolute();
		return new Point(c.x - (getWidth() >> 1), c.y - (getHeight() >> 1));
	}

	//

	// TODO SETTER

	public void setLog(LoggerMessages log);

	public void setWeightManager(NumberManager<Distance> numberManager);

	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer);

	public void setPathFinder(PathFinderIsom<Distance> pathFinder);

	public default void setLocationAbsolute(Point p) { getShape().setCenter(p); }

	public default void setLocationAbsolute(int x, int y) { getShape().setCenter(x, y); }

	public default void setTopLetCornerAbsolute(Point lc) { setTopLetCornerAbsolute(lc.x, lc.y); }

	public default void setTopLetCornerAbsolute(int x, int y) {
		setLocationAbsolute(x + (getWidth() >> 1), y + (getHeight() >> 1));
	}

	//

	// TODO OTHER

	public abstract ObjectLocated getObjectLocated(Integer ID);

	public default boolean removeAllObjects() {
		clearAllNodes();
		return true;
	}

	@Override
	public default void runOnShape(AbstractShape2D shape, PointConsumer action) {
		AbstractObjectsInSpaceManager.super.runOnShape(shape, action);
	}

	@Override
	public default void runOnShape(AbstractShape2D shape, NodeIsomConsumer<Distance> action) {
		AbstractObjectsInSpaceManager.super.runOnShape(shape, action);
	}

	//

	// TODO to-do path find

	/** Convert the given list of internal node points to a list of points. */
	public static <OL extends ObjectLocated> List<Point> listNodeToPoint(List<OL> lni) {
		ListMapped<OL, Point> lmapping;
		List<Point> ret;
		lmapping = new ListMapped<>(lni, ni -> ni.getLocation()); // map them
		ret = new LinkedList<>();
		for (Point p : lmapping) {
			ret.add(p);
		}
		return ret;
	}

	/**
	 * Simplified version of path finding. <br>
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public default List<Point> getPath(Point start, Point destination, Predicate<ObjectLocated> isWalkableTester,
			boolean returnPathToClosestNodeIfNotFound) {
		List<Point> path;
		PathOptimizer<Point> po;
		if (getWeightManager() == null)
			throw new RuntimeException("getWeightManager() is null");
		if (getWeightManager().getComparator() == null)
			throw new RuntimeException("getWeightManager().getComparator() is null");
		path = this.getPath(
				// getNodeAt(start), getNodeAt(destination)
				start, destination//
				, getPathFinder(), getWeightManager(), isWalkableTester, returnPathToClosestNodeIfNotFound);
//		path = new ListMapped<ObjectLocated, Point>(this.getPath(getNodeAt(start), getNodeAt(destination),
//				getPathFinder(), getWeightManager(), isWalkableTester, returnPathToClosestNodeIfNotFound),
//				ni -> ni.getLocation());
		po = this.getPathOptimizer();
		if (po != null)
			path = po.optimizePath(path);
		return path;
	}

	/**
	 * Simplified version of path finding. <br>
	 * See {@link #getPath(Point, Point, Predicate, boolean)}.
	 */
	public default List<Point> getPath(Point start, Point destination) {
		return this.getPath(start, destination, null, true);
	}

	/**
	 * Simplified version of path finding. <br>
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public default List<Point> getPath(ObjectShaped objRequiringTo, Point destination,
			Predicate<ObjectLocated> isWalkableTester, boolean returnPathToClosestNodeIfNotFound) {
		List<Point> path;
		path = this.getPath(// objRequiringTo, getNodeAt(destination)
				objRequiringTo, destination//
				, getPathFinder(), getWeightManager(), isWalkableTester, returnPathToClosestNodeIfNotFound);
//		path = new ListMapped<ObjectLocated, Point>(
//				this.getPath(objRequiringTo, (Point) getNodeAt(destination), getPathFinder(),
//						getWeightManager(), isWalkableTester, returnPathToClosestNodeIfNotFound),
//				ni -> ni.getLocation());
		return this.getPathOptimizer().optimizePath(path);
	}

	/**
	 * See {@link #getPath(ObjectShaped, Point, Predicate)}.
	 */
	public default List<Point> getPath(ObjectShaped objRequiringTo, Point destination) {
		return this.getPath(objRequiringTo, destination, null, true);
	}

	// TODO todo add getPath with predicate filter

	/**
	 * Refers to
	 * {@link InSpaceObjectsManagerImpl#findInPath(AbstractShape2D, dataStructures.isom.ObjLocatedCollectorIsom, List)}
	 * . <br>
	 * Sub-implementations of this class must provide a way to define
	 * {@link ObjCollector}.
	 */
	public abstract Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path);

	/**
	 * Queries all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end).
	 */
	@Override
	public default Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
			List<Point> path) {
		Iterator<Point> iter;
		ShapeLine subpath;
		Line2D line;
		Point pstart, pend;
		if (collector == null || path == null || path.size() < 2)
			return null;
		collector.restart();
		line = new Line2D.Double();
		iter = path.iterator();
		pstart = iter.next();
		while (iter.hasNext()) {
			pend = iter.next();
			line.setLine(pstart, pend);
			subpath = new ShapeLine(line);
			this.runOnShape(subpath, collector);
			pstart = pend;
		}
		return collector.getCollectedObjects();
	}
}