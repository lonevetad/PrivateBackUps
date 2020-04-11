package dataStructures.isom;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.ListMapped;
import geometry.AbstractObjectsInSpaceManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathFinderIsom;
import geometry.PathOptimizer;
import geometry.implementations.shapes.ShapeLine;
import geometry.pointTools.impl.ObjCollector;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.PathFinder;

/**
 * This interface denotes a huge object managing a set of object placed in space
 * (2D or 3D, it depends), providing various utilities like adding, removing,
 * fetching or querying objects or performing tasks in some areas, like scanning
 * the area. Other utilities could be path finding, providing special subsets of
 * objects, like the ones in a specific area, sets of clustered objects, get the
 * bounding polygon of an area and so on.
 * <p>
 * Each separate groups of utilities (like {add, remove, fetch}, {path find},
 * {bounding polygon}, etc) should be provided by a separate interface,
 * obviously linked or bounded with this instance, to avoid the <i>God class
 * anti-pattern</i>.
 * <p>
 * Provided utilities:
 * <ul>
 * <li>{@link PathFinderIsom}</li>
 * </ul>
 */
public abstract class InSpaceObjectsManager<Distance extends Number>
//extends dataStructures.graph.GraphSimple<NodeIsom, D>
		implements AbstractObjectsInSpaceManager {
	private static final long serialVersionUID = 1L;

	public InSpaceObjectsManager() {
		super();
	}

	protected LoggerMessages log;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	protected PathFinderIsom<NodeIsom, ObjectLocated, Distance> pathFinder;
	protected NumberManager<Distance> numberManager;
	protected PathOptimizer<Point> pathOptimizer;

	//

	// TODO GETTER

	public LoggerMessages getLog() {
		return log;
	}

	public NumberManager<Distance> getNumberManager() {
		return numberManager;
	}

	/**
	 * Could seems odd, but it's useful for {@link PathFinderIsom}s'
	 * implementations.
	 */
	public abstract NodeIsom getNodeAt(Point location);

	public PathOptimizer<Point> getPathOptimizer() {
		return pathOptimizer;
	}

	//

	// TODO SETTER

	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) {
		this.pathOptimizer = pathOptimizer;
	}

	public void setLog(LoggerMessages log) {
		this.log = log;
	}

	public PathFinder<NodeIsom, ObjectLocated, Distance> getPathFinder() {
		return pathFinder;
	}

	public void setPathFinder(PathFinderIsom<NodeIsom, ObjectLocated, Distance> pathFinder) {
		this.pathFinder = pathFinder;
	}

	public void setNumberManager(NumberManager<Distance> numberManager) {
		this.numberManager = numberManager;
	}

	// TODO OTHER

	/**
	 * Perform an action to each adjacent of a given node. That action should take
	 * into account not only the adjacent node, but also the distance from the given
	 * node and the specific adjacent.
	 */
	public abstract void forEachAdjacents(NodeIsom node, BiConsumer<NodeIsom, Distance> adjacentDistanceConsumer);

//	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

//	public AbstractPathFinder getPathFinder();

	//

//	public default boolean isInside(Point p) { return isInside(p.getX(), p.getY());}
//	public default boolean isInside(double x, double y) {
//		AbstractShape2D s;
//		s = getSpaceShape();
//		return s == null ? false : s.contains((int) x, (int) y);
//	}

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
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public List<Point> getPath(Point start, Point destination, Predicate<ObjectLocated> isWalkableTester) {
//		List<NodeIsom> lni;
//		lni = this.getPathFinder().getPath(getNodeAt(start), getNodeAt(destination), numberManager);
//		return listNodeToPoint(lni);
		List<NodeIsom> path;
		path = this.getPath(getNodeAt(start), getNodeAt(destination), pathFinder, numberManager, isWalkableTester);
		return this.pathOptimizer.optimizePath(new ListMapped<>(path, ni -> ni.getLocation()));
	}

	/**
	 * See {@link #getPath(ObjectShaped, Point, Predicate)}.
	 */
	public List<Point> getPath(Point start, Point destination) {
		return this.getPath(start, destination, null);
	}

	/**
	 * See
	 * {@link PathFinder#getPath(ObjectShaped, Object, NumberManager, Predicate)}.
	 */
	public List<Point> getPath(ObjectShaped objRequiringTo, Point destination,
			Predicate<ObjectLocated> isWalkableTester) {
//		List<NodeIsom> lni;
//		lni = this.getPathFinder().getPath(objRequiringTo, getNodeAt(destination), numberManager);
//		return listNodeToPoint(lni);
		List<NodeIsom> path;
		path = this.getPath(objRequiringTo, getNodeAt(destination), pathFinder, numberManager, isWalkableTester);
		return this.pathOptimizer.optimizePath(new ListMapped<>(path, ni -> ni.getLocation()));
	}

	/**
	 * See {@link #getPath(ObjectShaped, Point, Predicate)}.
	 */
	public List<Point> getPath(ObjectShaped objRequiringTo, Point destination) {
		return this.getPath(objRequiringTo, destination, null);
	}

	// TODO todo add getPath with predicate filter

	/**
	 * Refers to
	 * {@link InSpaceObjectsManager#findInPath(AbstractShape2D, dataStructures.isom.ObjLocatedCollectorIsom, List)}
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
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjCollector<ObjectLocated> collector,
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
		while(iter.hasNext()) {
			pend = iter.next();
			line.setLine(pstart, pend);
			subpath = new ShapeLine(line);
			this.runOnShape(subpath, collector);
			pstart = pend;
		}
		return collector.getCollectedObjects();
	}
}