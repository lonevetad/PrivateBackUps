package dataStructures.isom;

import java.awt.Point;

import geometry.ObjectLocated;
import geometry.PathOptimizer;
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
public abstract class InSpaceObjectsManagerImpl<Distance extends Number> implements InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 1L;

	public InSpaceObjectsManagerImpl() { super(); }

	protected LoggerMessages log;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	protected PathFinderIsom<Distance> pathFinder;
	protected NumberManager<Distance> weightManager;
	protected PathOptimizer<Point> pathOptimizer;

	//

	// TODO GETTER

	@Override
	public LoggerMessages getLog() { return log; }

	@Override
	public NumberManager<Distance> getWeightManager() { return weightManager; }

	@Override
	public PathOptimizer<Point> getPathOptimizer() { return pathOptimizer; }

	@Override
	public PathFinder<Point, ObjectLocated, Distance> getPathFinder() { return pathFinder; }

	//

	// TODO SETTER

	@Override
	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { this.pathOptimizer = pathOptimizer; }

	@Override
	public void setLog(LoggerMessages log) { this.log = log; }

	@Override
	public void setPathFinder(PathFinderIsom<Distance> pathFinder) { this.pathFinder = pathFinder; }

	@Override
	public void setWeightManager(NumberManager<Distance> numberManager) { this.weightManager = numberManager; }

	//

	// TODO OTHER

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

}