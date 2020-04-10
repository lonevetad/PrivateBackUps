package dataStructures.isom;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.ListMapped;
import dataStructures.graph.PathFindStrategy;
import geometry.AbstractObjectsInSpaceManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathFinderIsom;
import geometry.implementations.shapes.ShapeLine;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.ObjectWithID;
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
	protected PathFinderIsom<NodeIsom, ObjectWithID, Distance> pathFinder;
	protected NumberManager<Distance> numberManager;

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
	public abstract NodeIsom getNodeAt(Point2D location);

	//

	// TODO SETTER

	public void setLog(LoggerMessages log) {
		this.log = log;
	}

	public PathFinder<NodeIsom, ObjectWithID, Distance> getPathFinder() {
		return pathFinder;
	}

	public void setPathFinder(PathFinderIsom<NodeIsom, ObjectWithID, Distance> pathFinder) {
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

//	public default boolean isInside(Point2D p) { return isInside(p.getX(), p.getY());}
//	public default boolean isInside(double x, double y) {
//		AbstractShape2D s;
//		s = getSpaceShape();
//		return s == null ? false : s.contains((int) x, (int) y);
//	}

	// TODO to-do path find

	public static List<Point2D> listNodeToPoint(List<NodeIsom> lni) {
		ListMapped<NodeIsom, Point2D> lmapping;
		List<Point2D> ret;
		lmapping = new ListMapped<>(lni, ni -> ni.getLocation()); // map them
		ret = new LinkedList<>();
		for (Point2D p : lmapping) {
			ret.add(p);
		}
		return ret;
	}

	public List<Point2D> getPath(Point2D start, Point2D destination) {
		List<NodeIsom> lni;
		lni = this.getPathFinder().getPath(getNodeAt(start), getNodeAt(destination), numberManager);
		return listNodeToPoint(lni);
	}

	/**
	 * Invoking {@link #getPath(Point2D, Point2D)} by giving as first parameter the
	 * result of {@link ObjectLocated#getLocation()}..
	 */
//	@Deprecated /*because it could confuse the dynamic invocation*/
//	public abstract List<Point2D> getPath(ObjectLocated objRequiringToMove, Point2D destination);

	/**
	 * Like {@link #getPath(PathFindStrategy, ObjectLocated, Point2D)}, but it's
	 * required to take care of the object's sizze (i.e.: the value returned by
	 * {@link ObjectShaped#getShape()} provided by the second parameter).
	 */
	public List<Point2D> getPath(ObjectShaped objRequiringTo, Point2D destination) {
		List<NodeIsom> lni;
		lni = this.getPathFinder().getPath(objRequiringTo, getNodeAt(destination), numberManager);
		return listNodeToPoint(lni);
	}

	// TODO todo add getPath with predicate filter

	/**
	 * Refers to
	 * {@link InSpaceObjectsManager#findInPath(AbstractShape2D, dataStructures.isom.ObjLocatedCollector, List)},
	 * <br>
	 * Sub-implementations of this class must provide a way to define
	 * {@link ObjLocatedCollector}.
	 */
	public abstract Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path);

	/**
	 * Queries all objects located in the given area, if any, moving that area along
	 * a specific path, that requires at least two point (the starting point must be
	 * provided, the last point is the end).
	 */
	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, ObjLocatedCollector collector,
			List<Point> path) {
		Iterator<Point> iter;
		ShapeLine subpath;
		Line2D line;
		Point2D pstart, pend;
		if (path == null || path.size() < 2)
			return null;
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

	@Override
	public <NodeType, NodeContent, D extends Number> List<NodeContent> getPath(
			PathFinder<NodeType, NodeContent, D> pathFinderStrategy, Point2D start, Point2D destination) {
		// TODO to do the adapter
		return null;
	}

	@Override
	public <NodeType, NodeContent, D extends Number> List<NodeContent> getPathForShapedObject(
			PathFinder<NodeType, NodeContent, D> pathFinderStrategy, ObjectShaped objRequiringTo, Point2D destination) {
		// TODO to do the adapter
		return null;
	}
}