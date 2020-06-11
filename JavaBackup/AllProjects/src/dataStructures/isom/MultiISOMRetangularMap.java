package dataStructures.isom;

import java.awt.Point;

import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractMultiOISMRectangular;
import geometry.ObjectLocated;
import geometry.PathFinderIsom;
import geometry.PathOptimizer;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.LoggerMessages;
import tools.NumberManager;

/**
 * TODO of 10/06/2020
 * <p>
 * Take inspiration from {@link MatrixInSpaceObjectsManager} to implement
 * {@link InSpaceObjectsManager} and {@link Test_MultiISOMRetangularMap_V1} to
 * implement {@link AbstractMultiOISMRectangular}.
 */
public class MultiISOMRetangularMap<Distance extends Number> extends AbstractMultiOISMRectangular
		implements InSpaceObjectsManager<Distance> {

	public MultiISOMRetangularMap() {}

	protected LoggerMessages log;
//	protected PathFinderIsomAdapter<NodeIsom, D> pathFinder;
	protected PathFinderIsom<Point, ObjectLocated, Distance> pathFinder;
	protected NumberManager<Distance> numberManager;
	protected PathOptimizer<Point> pathOptimizer;

	@Override
	public LoggerMessages getLog() { return log; }

	@Override
	public PathFinderIsom<Point, ObjectLocated, Distance> getPathFinder() { return pathFinder; }

	@Override
	public NumberManager<Distance> getNumberManager() { return numberManager; }

	@Override
	public PathOptimizer<Point> getPathOptimizer() { return pathOptimizer; }

	@Override
	public void setLog(LoggerMessages log) { this.log = log; }

	@Override
	public void setPathFinder(PathFinderIsom<Point, ObjectLocated, Distance> pathFinder) {
		this.pathFinder = pathFinder;
	}

	@Override
	public void setNumberManager(NumberManager<Distance> numberManager) { this.numberManager = numberManager; }

	@Override
	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { this.pathOptimizer = pathOptimizer; }

	//

	//

	//

}