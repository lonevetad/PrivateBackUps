package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import dataStructures.graph.PathFindStrategy;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.ProviderShapeRunnerImpl;
import tools.LoggerMessages;

public class MatrixInSpaceObjectsManager extends InSpaceObjectsManager<Double> {
	private static final long serialVersionUID = 6663104159265L;

	public MatrixInSpaceObjectsManager() {
	}

//	protected Comparator IDOwidComparator;
	NodeIsom[][] matrix;

	//

	//

	@Override
	public AbstractShape2D getSpaceShape() {
		// TODO Auto-generated method stub
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
	public void setLog(LoggerMessages log) {
	}

	@Override
	public void setPathFinder(PathFindStrategy<Point2D> pathFinder) {

	}

	public NodeIsom[] getRow(int y) {
		return matrix[y];
	}

	//

	// TODO SETTER

	@Override
	public void setProviderShapesIntersectionDetector(
			ProviderShapesIntersectionDetector providerShapesIntersectionDetector) {
		// TODO Auto-generated method stub

	}

	public void setProviderShapeRunner(ProviderShapeRunnerImpl providerShapeRunner) {
		// TODO Auto-generated method stub

	}
	//

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		ObjLocatedCollectorMatrix coll;
		coll = new ObjLocatedCollectorMatrix(this, objectFilter);
		return this.findInPath(areaToLookInto, coll, path);
	}

}