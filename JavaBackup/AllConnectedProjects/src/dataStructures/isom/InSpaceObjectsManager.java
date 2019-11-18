package dataStructures.isom;

import java.awt.geom.Point2D;
import java.io.Serializable;

import dataStructures.graph.PathFindStrategy;
import geometry.AbstractShape2D;
import geometry.AbstractShapeRunner;
import geometry.ProviderObjectsInSpace;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.ProviderShapeRunnerImpl;
import tools.LoggerMessages;
import videogames.gridObjectManager.core.GomConsumer;

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
public interface InSpaceObjectsManager extends Serializable {

	//

	// TODO GETTER

	public LoggerMessages getLog(LoggerMessages log);

	/**
	 * Representation of the space managed by this instance.
	 */
	public AbstractShape2D getSpaceShape();

	//

	// TODO UTILITIES PROVIDERS GETTER

	public PathFindStrategy<Point2D> getPathFinder();

	public ProviderObjectsInSpace getProviderObjectsInSpace();

	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector();

	public ProviderShapeRunnerImpl getProviderShapeRunner();

	//

	// TODO SETTER

	public InSpaceObjectsManager setLog(LoggerMessages log);

	//

	// TODO UTILITIES PROVIDERS SETTER

	public void setPathFinder(PathFindStrategy<Point2D> pathFinder);

	public void setObjectsInSpaceProvider(ProviderObjectsInSpace providerObjectsInSpace);

	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector);

	public void setProviderShapeRunner(ProviderShapeRunnerImpl providerShapeRunner);

//

	// TODO OTHER
	public LoggerMessages getLog();

//	public AbstractShapeRunners/* _WithCoordinates */ getShapeRunners();

//	public AbstractPathFinder getPathFinder();

	//

	public default boolean isInside(Point2D p) {
		return isInside(p.getX(), p.getY());
	}

	public default boolean isInside(double x, double y) {
		AbstractShape2D s;
		s = getSpaceShape();
		return s == null ? false : s.contains((int) x, (int) y);
	}

	/**
	 * As for {@link #runOnShape(AbstractShape2D, GomConsumer)}, but giving the
	 * {@link AbstractShape2D} returned by {@link #getSpaceShape()}.
	 */
	public default void runOnShape(IsomConsumer action) {
		runOnShape(getSpaceShape(), action);
	}

	public default void runOnShape(AbstractShape2D shape, IsomConsumer action) {
		AbstractShapeRunner runner;
		if (shape == null || action == null)
			return;
		runner = this.getProviderShapeRunner().getShapeRunner(shape.getShapeImplementing());
		if (runner == null)
			return;
		runner.runShape(shape, action);
	}
}