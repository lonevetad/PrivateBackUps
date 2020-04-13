package dataStructures.isom.matrixBased.pathFinders;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderDijkstra_Matrix<Distance extends Number> extends PathFinderIsomMatrix<Distance> {

	public PathFinderDijkstra_Matrix(MatrixInSpaceObjectsManager<Distance> matrix) {
		super(matrix);
	}

	protected final Comparator<NodeInfoDijkstra> COMPARATOR_NINFO = (ni1, ni2) -> {
		if (ni1 == ni2)
			return 0;
		if (ni1 == null)
			return -1;
		if (ni2 == null)
			return 1;
		return NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(ni1.thisNode, ni2.thisNode);
	};

	@Override
	protected AbstractAdjacentForEacher newAdjacentConsumer(Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager) {
		return new UsynchronizedAdjacentForEacherDijkstra(isWalkableTester, distanceManager);
	}

	@Override
	protected AbstractAdjacentForEacher newAdjacentConsumerForObjectShaped(AbstractShape2D shape,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new ShapedAdjacentForEacherDijkstra(shape, isWalkableTester, distanceManager);
	}

	// TODO

	@SuppressWarnings("unchecked")
	@Override
	protected List<Point> getPathGeneralized(NodeIsom start, NodeIsom dest, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester, AbstractAdjacentForEacher fa) {
		final MatrixInSpaceObjectsManager<Distance> m;
		NodeInfoDijkstra ss, dd;
		final Map<NodeIsom, NodeInfoDijkstra> nodeInfos;
		final PriorityQueueKey<NodeInfoDijkstra, Distance> frontier;
		final Comparator<Distance> comp;
		AbstractAdjacentForEacherDijkstra forAdjacents;
		m = matrix;
		comp = distanceManager.getComparator();
		forAdjacents = (AbstractAdjacentForEacherDijkstra) fa;
		//
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, NodeIsom.COMPARATOR_NODE_ISOM_POINT);
		frontier = new PriorityQueueKey<>(COMPARATOR_NINFO, comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious,
				(NodeInfoDijkstra no) -> no.distFromStart);
		ss = new NodeInfoDijkstra(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoDijkstra(dest);
		nodeInfos.put(dest, dd);

		// set non-final parameters
		forAdjacents.nodeInfos = nodeInfos;
		forAdjacents.frontier = frontier;

		while(!frontier.isEmpty()) {
			final NodeInfoDijkstra n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.distFromStart > n.distFromStart
					comp.compare(dd.distFromStart, n.distFromStart) > 0) {
				forAdjacents.setCurrentNode(n);
				m.forEachAdjacents(n.thisNode, forAdjacents);
//				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the nodes in the queue with MINIMUM
				 * "distance from start" has a distance greater than the destination itself ->
				 * iterating is useless -> exit: empty the frontier
				 */
				frontier.clear();
			n.color = NodePositionInFrontier.Closed;
		}
		nodeInfos.clear();
		return extractPath(ss, dd);
	}

	//

	// subclasses

	protected class NodeInfoDijkstra extends NodeInfo {
		protected Distance distFromStart, distFromFather;

//		protected NodeInfoDijkstra father;
//		public static final Comparator<NodeIsom> COMPARATOR_NINFO = NodeIsom.COMPARATOR_NODE_ISOM_POINT::compare;
//		protected NumberManager<Distance> distanceManager;
		protected NodeInfoDijkstra(NodeIsom thisNode) {
//			this.distanceManager = distanceManager;
			super(thisNode);
			distFromStart = distFromFather = null;
		}
	}

	protected abstract class AbstractAdjacentForEacherDijkstra extends AbstractAdjacentForEacher {

		protected Map<NodeIsom, NodeInfoDijkstra> nodeInfos;
		protected PriorityQueueKey<NodeInfoDijkstra, Distance> frontier;
		protected final DistanceKeyAlterator alterator;

		public AbstractAdjacentForEacherDijkstra(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.alterator = new DistanceKeyAlterator();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void accept(NodeIsom adjacent, Distance distToAdj) {
			Distance distToNo;
			NodeInfoDijkstra noInfo;
			if (!isAdjacentNodeWalkable(adjacent))
				return;
			if (nodeInfos.containsKey(adjacent))
				noInfo = nodeInfos.get(adjacent);
			else
				nodeInfos.put(adjacent, noInfo = new NodeInfoDijkstra(adjacent));

			if (noInfo.color == NodePositionInFrontier.Closed)
				return;
			distToNo = // distToAdj + currentNode.distFromStart;
					this.distanceManager.getAdder().apply(distToAdj, ((NodeInfoDijkstra) currentNode).distFromStart);
			if (noInfo.father == null ||
			// distToNo < noInfo.distFromStart
					distanceManager.getComparator().compare(distToNo, noInfo.distFromStart) < 0) {
				// update
				noInfo.father = currentNode;
				noInfo.distFromFather = distToAdj; // Double.valueOf(distToAdj);
				if (noInfo.color == NodePositionInFrontier.NeverAdded) {
					// add on queue
					noInfo.color = NodePositionInFrontier.InFrontier;
					noInfo.distFromStart = distToNo;
					frontier.put(noInfo);
				} else {
					// it's grey, it's actually in the queue
//					frontier.alterKey(noInfo, nodd -> (nodd).distFromStart = distToNo);
					alterator.distToNo = distToNo;
					frontier.alterKey(noInfo, alterator);
				}
			}
		}
	}

	protected class UsynchronizedAdjacentForEacherDijkstra extends AbstractAdjacentForEacherDijkstra {

		protected UsynchronizedAdjacentForEacherDijkstra(//
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}

		/** Generalize the check */
		@Override
		public boolean isAdjacentNodeWalkable(NodeIsom currentNode) {
			return currentNode.isWalkable(isWalkableTester);
		}
//		public void accept(GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized nod, Integer distToAdj) {

	}

	protected class ShapedAdjacentForEacherDijkstra extends AbstractAdjacentForEacherDijkstra {

		public ShapedAdjacentForEacherDijkstra(AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.shape = shape;
			this.shapeWalkableChecker = new WholeShapeWalkableChecker<>(matrix, isWalkableTester);
		}

		protected final AbstractShape2D shape;
		protected final WholeShapeWalkableChecker<Distance> shapeWalkableChecker;

		@Override
		public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) {
			shapeWalkableChecker.restart();
			shape.setCenter(adjacentNode.x, adjacentNode.y);
			matrix.runOnShape(shape, shapeWalkableChecker);
			return shapeWalkableChecker.isWalkable();
		}
	}

	/** Not used */
	protected class DistanceKeyAlterator implements Consumer<NodeInfoDijkstra> {
		Distance distToNo;

		@Override
		public void accept(NodeInfoDijkstra nodd) {
			nodd.distFromStart = distToNo;
		}
	}
}