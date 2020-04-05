package dataStructures.isom.matrixBased;

import java.awt.geom.Point2D;
import java.util.Comparator;

import dataStructures.graph.PathFindStrategy;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import geometry.AbstractShape2D;
import geometry.AbstractObjectsInSpaceManager;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.ProviderShapeRunnerImpl;
import tools.LoggerMessages;

public class MatrixInSpaceObjectsManager<IDowid> implements InSpaceObjectsManager {
	private static final long serialVersionUID = 6663104159265L;

	public MatrixInSpaceObjectsManager() {
	}

	protected Comparator<IDowid> IDOwidComparator;
	NodeIsom<IDowid>[][] matrix;

	//

	//

	@Override
	public LoggerMessages getLog(LoggerMessages log) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractShape2D getSpaceShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathFindStrategy<Point2D> getPathFinder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractObjectsInSpaceManager getProviderObjectsInSpace() {
		return null;
	}

	@Override
	public ProviderShapesIntersectionDetector getProviderShapesIntersectionDetector() {
		return null;
	}

	@Override
	public ProviderShapeRunnerImpl getProviderShapeRunner() {
		return null;
	}

	@Override
	public InSpaceObjectsManager setLog(LoggerMessages log) {
		return null;
	}

	@Override
	public void setPathFinder(PathFindStrategy<Point2D> pathFinder) {

	}

	public NodeIsom<IDowid>[] getRow(int y) {
		return matrix[y];
	}

	//

	// TODO SETTER

	@Override
	public void setObjectsInSpaceProvider(AbstractObjectsInSpaceManager abstractObjectsInSpaceManager) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunnerImpl providerShapeRunner) {
		// TODO Auto-generated method stub

	}

	@Override
	public LoggerMessages getLog() {
		// TODO Auto-generated method stub
		return null;
	}

}
