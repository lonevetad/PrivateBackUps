package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.PathFinderIsom;
import dataStructures.isom.PathFinderIsomFrontierBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public abstract class PathFinderIsomBaseImpl<Distance extends Number> implements PathFinderIsomFrontierBased<Distance> {

	public PathFinderIsomBaseImpl(
			// MatrixInSpaceObjectsManager<Distance>
			InSpaceObjectsManager<Distance> isom) {
//this.isSynchronized = isSynchronized;boolean isSynchronized,
		this.isom = isom;
	}

//protected final boolean isSynchronized;

	protected final InSpaceObjectsManager<Distance> isom; // MatrixInSpaceObjectsManager<Distance> matrix;

//public boolean isSynchronized() {return isSynchronized;}

	@Override
	public InSpaceObjectsManager<Distance> getSpaceToRunThrough() { return isom; }

	//

	protected abstract class AdjacentForEacherBaseImpl extends AbstractAdjacentForEacher<Distance> {

		protected AdjacentForEacherBaseImpl(//
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}
	}

	protected abstract class ShapedAdjacentForEacherBaseImpl
			extends AbstractShapedAdjacentForEacher<Point, ObjectLocated, Distance> {

		protected ShapedAdjacentForEacherBaseImpl(PathFinderIsom<Point, ObjectLocated, Distance> pathFinderIsom,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, shape, isWalkableTester, distanceManager);
		}
		/*
		 * @Override public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
		 * shapeWalkableChecker.restart(); shape.setCenter(adjacentNode.x,
		 * adjacentNode.y); isom.runOnShape(shape, shapeWalkableChecker); return
		 * shapeWalkableChecker.isWalkable(); }
		 */
	}
}