package dataStructures.isom;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.isom.PathFinderIsomFrontierBased.NodeInfoFrontierBased;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;
import geometry.implementations.shapes.subHierarchy.ShapeFillable;
import geometry.pointTools.impl.PointConsumerRestartable;
import tools.NumberManager;
import tools.PathFinder;

public interface PathFinderIsom<NodeType, NodeContent, Distance extends Number>
		extends PathFinder<NodeType, NodeContent, Distance> { // NodeContent extends Point2D not needed

//	public InSpaceObjectsManager<Distance> getSpaceToRunThrough();
//	public default NodeIsomProvider getNodeIsomProvider() { return getSpaceToRunThrough(); }
	public NodeIsomProvider<Distance> getNodeIsomProvider();

	//

	// override from super interface

	public default List<Point> getPath(Point start, Point dest, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester) {
		return getPath(getNodeIsomProvider(), distanceManager, isWalkableTester, start, dest);
	}

	public default List<Point> getPath(ObjectShaped objPlanningToMove, Point dest,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester) {
		return getPath(getNodeIsomProvider(), distanceManager, isWalkableTester, objPlanningToMove, dest);
	}

	// here defined

	public default List<Point> getPath(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			ObjectShaped objPlanningToMove, Point destination) {
		boolean prevFilled, isFillable;
		NodeIsom start, dest;
		AbstractShape2D shape, sOnlyBorder;
		Point originalLocation;
		AbstractAdjacentForEacher<Distance> forAdjacents;
		List<Point> l;
		shape = objPlanningToMove.getShape();
		originalLocation = new Point(objPlanningToMove.getLocation());
		start = nodeProvider.getNodeAt(originalLocation);
		dest = nodeProvider.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		// make the shape just a "border shape" to optimize the code
		isFillable = shape instanceof ShapeFillable;
		prevFilled = isFillable && ((ShapeFillable) shape).isFilled();
		sOnlyBorder = shape.toBorder();
		// perform the algorithm
		forAdjacents = newAdjacentConsumerForObjectShaped(nodeProvider, isWalkableTester, distanceManager, sOnlyBorder);
		l = getPathGeneralized(nodeProvider, distanceManager, isWalkableTester, forAdjacents, start, dest);
		/// restore previous state
		if (isFillable) { ((ShapeFillable) shape).setFilled(prevFilled); }
		shape.setCenter(originalLocation);
		return l;
	}

	public default List<Point> getPath(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester, Point startingPoint,
			Point destination) {
		NodeIsom start, dest;
		AbstractAdjacentForEacher<Distance> forAdjacents;
		start = nodeProvider.getNodeAt(startingPoint);
		dest = nodeProvider.getNodeAt(destination);
		if (start == null || dest == null || dest == start)
			return null;
		forAdjacents = newAdjacentConsumer(nodeProvider, isWalkableTester, distanceManager);
		return getPathGeneralized(nodeProvider, distanceManager, isWalkableTester, forAdjacents, start, dest);
	}

	public static <D extends Number> List<Point> extractPath(NodeInfo<D> start, NodeInfo<D> end) {
		List<Point> l;
		if (end.father == null)
			return null;
		l = new LinkedList<>();
		while (end != start) {
			l.add(end.thisNode.getLocation());
			end = end.father;
		}
		((LinkedList<Point>) l).addFirst(start.thisNode.getLocation());
		return l;
	}

// 

	/**
	 * The real implementation part of the path-finding that will be implemented. If
	 * it's not fitted to the algorithm, just let it blank and override
	 * {@link #getPath(NodeIsomProvider, NumberManager, Predicate, Point, Point)}.
	 */
	public abstract List<Point> getPathGeneralized(final NodeIsomProvider<Distance> m,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, NodeIsom start, NodeIsom dest);

//

	/** Helper method instancing a part of the point-based strategy. */
	public abstract AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> m,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager);

	/**
	 * Helper method instancing a part of the {@link AbstractShape2D}-based
	 * strategy.
	 */
	public abstract AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> m, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape);

	//

	//

	//

	public static class NodeInfo<D extends Number> {
		// Compiler Error: "cannot make a static reference to the non-static type
		// Distance" .. so I redefine it
		public D distFromStart, distFromFather;
		public NodeIsom thisNode;
		public NodeInfo<D> father;

		public NodeInfo(NodeIsom thisNode) {
			this.thisNode = thisNode;
			this.father = null;
			this.distFromStart = this.distFromFather = null;
		}
	}

	public static class DistanceKeyAlterator<NIF extends NodeInfoFrontierBased<D>, D extends Number>
			implements Consumer<NIF> {
		public D distToNo;

		@Override
		public void accept(NIF nodd) { nodd.distFromStart = distToNo; }
	}

	//

	public static abstract class AbstractAdjacentForEacher<D extends Number> implements BiConsumer<NodeIsom, D> {
		protected final Predicate<ObjectLocated> isWalkableTester;
		protected final NumberManager<D> distanceManager;
		protected NodeInfo<D> currentNode;

		public AbstractAdjacentForEacher(Predicate<ObjectLocated> isWalkableTester, NumberManager<D> distanceManager) {
			super();
			this.isWalkableTester = isWalkableTester;
			this.distanceManager = distanceManager;
		}

		public Predicate<ObjectLocated> getIsWalkableTester() { return isWalkableTester; }

		public NumberManager<D> getDistanceManager() { return distanceManager; }

		public NodeInfo<D> getCurrentNode() { return currentNode; }

		public void setCurrentNode(NodeInfo<D> currentNode) { this.currentNode = currentNode; }

		public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
			return adjacentNode.isWalkable(isWalkableTester);
		}
	}

	//

	public static abstract class AbstractShapedAdjacentForEacher<NodeType, NodeContent, D extends Number>
			extends AbstractAdjacentForEacher<D> {

		public AbstractShapedAdjacentForEacher(PathFinderIsom<NodeType, NodeContent, D> pathFinderIsom,
				final NodeIsomProvider<D> m, AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<D> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.m = m;
			this.pathFinderIsom = pathFinderIsom;
			this.shape = shape;
			this.shapeWalkableChecker = new WholeShapeWalkableChecker<D>(m, isWalkableTester);
		}

		protected final NodeIsomProvider<D> m;
		protected final PathFinderIsom<NodeType, NodeContent, D> pathFinderIsom;
		protected final AbstractShape2D shape;
		protected final WholeShapeWalkableChecker<D> shapeWalkableChecker;

		@Override
		public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
			shapeWalkableChecker.restart();
			shape.setCenter(adjacentNode.x, adjacentNode.y);
//			pathFinderIsom.getSpaceToRunThrough()
			m.runOnShape(shape, shapeWalkableChecker);
			return shapeWalkableChecker.isWalkable();
		}
	}

	/** Utility class, for some reason (for example: "walk until I can get"). */
	public static class WholeShapeWalkableChecker<D extends Number>
			implements NodeIsomConsumer<D>, PointConsumerRestartable {
		private static final long serialVersionUID = -7895211411002L;
		protected boolean isWalkable;
//		protected InSpaceObjectsManager<D> isom;
		protected NodeIsomProvider<D> m;
		protected Predicate<ObjectLocated> isWalkableTester;

		public WholeShapeWalkableChecker(final NodeIsomProvider<D> m, Predicate<ObjectLocated> isWalkableTester) {
//			this.isom = isom;
			this.m = m;
			this.isWalkable = true;// assumption
			this.isWalkableTester = isWalkableTester;
		}

//		public InSpaceObjectsManager<D> getInSpaceObjectsManager() { return isom; }

		public boolean isWalkable() { return isWalkable; }

		public Predicate<ObjectLocated> getIsWalkableTester() { return isWalkableTester; }

//		public void setInSpaceObjectsManager(InSpaceObjectsManager<D> isom) { this.isom = isom; }

		public void setIsWalkableTester(Predicate<ObjectLocated> isWalkableTester) {
			this.isWalkableTester = isWalkableTester;
		}

		@Override
		public boolean canContinue() { return this.isWalkable; }

//		public void accept(Point t) { this.isWalkable &= this.isom.getNodeAt(t).isWalkable(isWalkableTester); }

		@Override
		public void restart() { this.isWalkable = true; }

		@Override
		public NodeIsomProvider<D> getNodeIsomProvider() { return m; }

		@Override
		public void consume(NodeIsom n) { this.isWalkable &= n.isWalkable(isWalkableTester); }

		@Override
		public void setNodeIsomProvider(NodeIsomProvider<D> nodeIsomProvider) {
//			if (nodeIsomProvider instanceof InSpaceObjectsManager<?>)
			this.m = nodeIsomProvider;
		}
	}
}