package dataStructures.isom;

import java.awt.geom.Point2D;
import java.util.List;

import dataStructures.graph.GraphSimple;
import dataStructures.graph.PathFindStrategy;
import geometry.AbstractObjectsInSpaceManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathFinder;
import tools.LoggerMessages;

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
 * <li></li>
 * </ul>
 */
public abstract class InSpaceObjectsManager<D extends Number> extends dataStructures.graph.GraphSimple<NodeIsom, D>
		implements AbstractObjectsInSpaceManager {

	public InSpaceObjectsManager() {
		super();
	}

	protected LoggerMessages log;
	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;

	//

	// TODO GETTER

	/**
	 * Could seems odd, but it's useful for {@link PathFinder}s' implementations.
	 */
	public abstract NodeIsom getNodeAt(Point2D location);

	//

	// TODO SETTER

	@Override
	public PathFinder<NodeIsom, D> getPathFinder() {
		return pathFinder;
	}

	public void setPathFinder(PathFinder<NodeIsom, D> pathFinder) {
		this.pathFinder = pathFinder;
	}

	@Override
	public GraphSimple<NodeIsom, D> setLog(LoggerMessages log) {
		this.log = log;
		return this;
	}

	@Override
	public void setPathFinder(PathFindStrategy<NodeIsom, D> pathFinder) {
		this.pathFinder = pathFinder;
	}

//

	// TODO OTHER
	@Override
	public LoggerMessages getLog();

//	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

//	public AbstractPathFinder getPathFinder();

	//

//	public default boolean isInside(Point2D p) { return isInside(p.getX(), p.getY());}
//	public default boolean isInside(double x, double y) {
//		AbstractShape2D s;
//		s = getSpaceShape();
//		return s == null ? false : s.contains((int) x, (int) y);
//	}

	/**
	 * As for {@link #runOnShape(AbstractShape2D, IsomConsumer)}, but giving the
	 * {@link AbstractShape2D} returned by {@link #getSpaceShape()}.
	 */
	public default void runOnShape(IsomConsumer action) {
		runOnShape(getBoundingShape(), action);
	}

	// TODO to-do path find

	public List<Point2D> getPath(Point2D start, Point2D destination);

	/**
	 * Invoking {@link #getPath(Point2D, Point2D)} by giving as first parameter the
	 * result of {@link ObjectLocated#getLocation()}..
	 */
	public default List<Point2D> getPath(ObjectLocated objRequiringToMove, Point2D destination) {
		return getPath(getPathFinder(), objRequiringToMove.getLocation(), destination);
	}

	/**
	 * Like {@link #getPath(PathFindStrategy, ObjectLocated, Point2D)}, but it's
	 * required to take care of the object's sizze (i.e.: the value returned by
	 * {@link ObjectShaped#getShape()} provided by the second parameter).
	 */
	public List<Point2D> getPath(ObjectShaped objRequiringTo, Point2D destination);

}