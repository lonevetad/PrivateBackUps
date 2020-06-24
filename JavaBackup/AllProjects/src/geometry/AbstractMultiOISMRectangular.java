package geometry;

import java.awt.Point;
import java.util.Iterator;

import dataStructures.isom.PathFinderIsom;
import geometry.implementations.PathOptimizerPoint;
import geometry.implementations.ProviderShapeRunnerImpl;
import tests.tDataStruct.Test_MultiISOMRetangularMap_V1;
import tools.LoggerMessages;
import tools.NumberManager;

/** Refers to {@link Test_MultiISOMRetangularMap_V1}. */
public abstract class AbstractMultiOISMRectangular<Distance extends Number>
		implements AbstractObjectsInSpaceManager<Distance> {
	private static final long serialVersionUID = 1L;

	public AbstractMultiOISMRectangular() {
		log = LoggerMessages.LOGGER_DEFAULT;
		setProviderShapeRunner(ProviderShapeRunnerImpl.getInstance());
		setProviderShapesIntersectionDetector(ProviderShapesIntersectionDetector.getInstance());
		setPathOptimizer(PathOptimizerPoint.getInstance());
	}

	protected LoggerMessages log;
	protected NumberManager<Distance> numberManager;
	protected PathOptimizer<Point> pathOptimizer;
	protected PathFinderIsom<Distance> pathFinder;
	protected ProviderShapeRunner providerShapeRunner;
	protected ProviderShapesIntersectionDetector providerShapesIntersectionDetector;

	public LoggerMessages getLog() { return log; }

	public PathFinderIsom<Distance> getPathFinder() { return pathFinder; }

	public NumberManager<Distance> getWeightManager() { return numberManager; }

	public PathOptimizer<Point> getPathOptimizer() { return pathOptimizer; }

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() { // TODO Auto-generated method
																						// stub
		return providerShapesIntersectionDetector;
	}

	@Override
	public ProviderShapeRunner getProviderShapeRunner() { // TODO Auto-generated method stub
		return providerShapeRunner;
	}

	//

	public void setLog(LoggerMessages log) { this.log = log; }

	public void setPathFinder(PathFinderIsom<Distance> pathFinder) { this.pathFinder = pathFinder; }

	public void setWeightManager(NumberManager<Distance> numberManager) { this.numberManager = numberManager; }

	public void setPathOptimizer(PathOptimizer<Point> pathOptimizer) { this.pathOptimizer = pathOptimizer; }

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		this.providerShapesIntersectionDetector = providerShapesIntersectionDetector;
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) { // TODO Auto-generated method stub
		this.providerShapeRunner = providerShapeRunner;
	}

	@Override
	public Iterator<ObjectLocated> iterator() { // TODO Auto-generated method stub
		return null;
	}
}