package dataStructures.isom.matrixBased;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.PathFinderIsom;
import geometry.ProviderShapeRunner;
import geometry.ProviderShapesIntersectionDetector;
import geometry.implementations.ProviderShapeRunnerImpl;
import tools.LoggerMessages;
import tools.NumberManager;
import tools.ObjectWithID;

/** Rectangular matrix-based implementation */
public class MatrixInSpaceObjectsManager<Distance extends Number> extends InSpaceObjectsManager<Distance> {
	private static final long serialVersionUID = 6663104159265L;
	protected static final Double justOne = 1.0, sqrtTwo = /* Math.max(justOne + 0.5, */Math.sqrt(2);
//	public static enum OperationOnShape {Add, Remove, Replace, Collect;}

	public static enum CoordinatesDeltaForAdjacentNodes {
		// TOP(0, -1), RIGHT(+1, 0), BOTTOM(0, 1), LEFT(-1, 0);
		NORD(0, -1, justOne), SUD(0, 1, justOne), OVEST(-1, 0, justOne), EST(1, 0, justOne)//
		, NORD_EST(1, -1, sqrtTwo), SUD_EST(1, 1, sqrtTwo), SUD_OVEST(-1, 1, sqrtTwo), NORD_OVEST(-1, -1, sqrtTwo)//
		;

		final int dx, dy;
		final Double weight;

		CoordinatesDeltaForAdjacentNodes(int dxx, int dyy, double w) {
			dx = dxx;
			dy = dyy;
			this.weight = w;
		}
	}

	public MatrixInSpaceObjectsManager(int width, int height, NumberManager<Distance> weightManager) {
		this.weightManager = weightManager;
	}

//	protected Comparator IDOwidComparator;
	protected NodeIsom[][] matrix;
	protected final NumberManager<Distance> weightManager;

	//

	//

	@Override
	public AbstractShape2D getBoundingShape() {
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

	@Override
	public void setLog(LoggerMessages log) {
	}

	@Override
	public void setPathFinder(PathFinderIsom<NodeIsom, ObjectWithID, Distance> pathFinder) {
		this.pathFinder = pathFinder;
	}

	//

	@Override
	public NodeIsom getNodeAt(Point2D p) {
		return matrix[(int) p.getY()][(int) p.getX()];
	}

	@Override
	public void forEachAdjacents(NodeIsom node, BiConsumer<NodeIsom, Distance> adjacentDistanceConsumer) {
		boolean xNotTooOnRight, xStrictlyPositive;
		int x;
		NodeIsom row[];
		x = node.x;
		xStrictlyPositive = x > 0;
		xNotTooOnRight = x < (matrix[0].length - 1);
		if (node.y > 0) {
			row = matrix[node.y - 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(sqrtTwo));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(justOne));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(sqrtTwo));
		}
		if (xStrictlyPositive)
			adjacentDistanceConsumer.accept(matrix[node.y][x - 1], weightManager.fromDouble(justOne));
		if (xNotTooOnRight)
			adjacentDistanceConsumer.accept(matrix[node.y][x + 1], weightManager.fromDouble(justOne));
		if (node.y < (matrix.length - 1)) {
			row = matrix[node.y + 1];
			if (xStrictlyPositive)
				adjacentDistanceConsumer.accept(row[x - 1], weightManager.fromDouble(sqrtTwo));
			adjacentDistanceConsumer.accept(row[x], weightManager.fromDouble(justOne));
			if (xNotTooOnRight)
				adjacentDistanceConsumer.accept(row[x + 1], weightManager.fromDouble(sqrtTwo));
		}

	}

	@Override
	public Set<ObjectLocated> findInPath(AbstractShape2D areaToLookInto, Predicate<ObjectLocated> objectFilter,
			List<Point> path) {
		ObjLocatedCollectorMatrix<Distance> coll;
		coll = new ObjLocatedCollectorMatrix<>(this, objectFilter);
		return this.findInPath(areaToLookInto, coll, path);
	}

	@Override
	public void setProviderShapeRunner(ProviderShapeRunner providerShapeRunner) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean add(ObjectLocated o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(ObjectLocated o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(ObjectLocated o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <E extends ObjectLocated> Set<ObjectLocated> fetch(AbstractShape2D areaToLookInto,
			Predicate<E> objectFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<ObjectLocated> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

}