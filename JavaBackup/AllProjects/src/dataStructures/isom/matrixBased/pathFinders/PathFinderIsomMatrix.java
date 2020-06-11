package dataStructures.isom.matrixBased.pathFinders;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import dataStructures.isom.InSpaceObjectsManagerImpl;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.PathFinderIsom;
import geometry.implementations.shapes.subHierarchy.ShapeFillable;
import tools.NumberManager;

public abstract class PathFinderIsomMatrix<Distance extends Number>
		implements PathFinderIsom<Point, ObjectLocated, Distance> {

	public PathFinderIsomMatrix(MatrixInSpaceObjectsManager<Distance> matrix) {
//this.isSynchronized = isSynchronized;boolean isSynchronized,
		this.matrix = matrix;
	}

//protected final boolean isSynchronized;

	protected final MatrixInSpaceObjectsManager<Distance> matrix;

//public boolean isSynchronized() {return isSynchronized;}

	@Override
	public InSpaceObjectsManagerImpl<Distance> getSpaceToRunThrough() {
		return matrix;
	}

//

	@Override
	public List<Point> getPath(ObjectShaped objPlanningToMove, Point destination,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester) {
		boolean prevFilled, isFillable;
		final MatrixInSpaceObjectsManager<Distance> m;
		NodeIsom start, dest;
		AbstractShape2D shape, sOnlyBorder;
		Point originalLocation;
		AbstractAdjacentForEacher forAdjacents;
		List<Point> l;
		shape = objPlanningToMove.getShape();
		originalLocation = new Point(objPlanningToMove.getLocation());
		m = matrix;
		start = m.getNodeAt(originalLocation);
		dest = m.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		// make the shape just a "border shape" to optimize the code
		isFillable = shape instanceof ShapeFillable;
		prevFilled = isFillable && ((ShapeFillable) shape).isFilled();
		sOnlyBorder = shape.toBorder();
		// perform the algorithm
		forAdjacents = newAdjacentConsumerForObjectShaped(sOnlyBorder, isWalkableTester, distanceManager);
		l = getPathGeneralized(start, dest, distanceManager, isWalkableTester, forAdjacents);
		/// restore previous state
		if (isFillable) {
			((ShapeFillable) shape).setFilled(prevFilled);
		}
		shape.setCenter(originalLocation);
		return l;
	}

	@Override
	public List<Point> getPath(Point startingPoint, Point destination, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester) {
		final MatrixInSpaceObjectsManager<Distance> m;
		NodeIsom start, dest;
		AbstractAdjacentForEacher forAdjacents;
		m = matrix;
		start = m.getNodeAt(startingPoint);
		dest = m.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		forAdjacents = newAdjacentConsumer(isWalkableTester, distanceManager);
		return getPathGeneralized(start, dest, distanceManager, isWalkableTester, forAdjacents);
	}

	protected List<Point> extractPath(NodeInfo start, NodeInfo end) {
		List<Point> l;
		if (end.father == null)
			return null;
		//
		l = new LinkedList<>();
		while(end != start) {
			l.add(end.thisNode.getLocation());
			end = end.father;
		}
		((LinkedList<Point>) l).addFirst(start.thisNode.getLocation());
		return l;
	}

// 

	protected abstract List<Point> getPathGeneralized(NodeIsom start, NodeIsom dest,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher forAdjacents);

//

	protected abstract AbstractAdjacentForEacher newAdjacentConsumer(Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager);

	protected abstract AbstractAdjacentForEacher newAdjacentConsumerForObjectShaped(AbstractShape2D shape,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager);

//

	protected class NodeInfo {
		protected NodePositionInFrontier color;
//		protected Distance distFromStart, distFromFather;
		protected NodeIsom thisNode;
		protected NodeInfo father;

		protected NodeInfo(NodeIsom thisNode) {
			this.thisNode = thisNode;
			color = NodePositionInFrontier.NeverAdded;
			father = null;
		}
	}

	public abstract class AbstractAdjacentForEacher implements BiConsumer<NodeIsom, Distance> {
		protected final Predicate<ObjectLocated> isWalkableTester;
		protected final NumberManager<Distance> distanceManager;
		protected NodeInfo currentNode;

		public AbstractAdjacentForEacher(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super();
			this.isWalkableTester = isWalkableTester;
			this.distanceManager = distanceManager;
		}

		public Predicate<ObjectLocated> getIsWalkableTester() {
			return isWalkableTester;
		}

		public NumberManager<Distance> getDistanceManager() {
			return distanceManager;
		}

		public NodeInfo getCurrentNode() {
			return currentNode;
		}

		public void setCurrentNode(NodeInfo currentNode) {
			this.currentNode = currentNode;
		}

		//

		public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
			return adjacentNode.isWalkable(isWalkableTester);
		}
	}
}