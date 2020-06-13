package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderIsomDijkstra<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomDijkstra(
			// MatrixInSpaceObjectsManager<Distance> matrix
			InSpaceObjectsManager<Distance> matrix) {
		super(matrix);
	}

	protected final Comparator<NodeInfoFrontierBased<Distance>> COMPARATOR_NINFO = (ni1, ni2) -> {
		if (ni1 == ni2)
			return 0;
		if (ni1 == null)
			return -1;
		if (ni2 == null)
			return 1;
		return NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(ni1.thisNode, ni2.thisNode);
	};

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager) {
		return new UsynchronizedAdjacentForEacherDijkstra(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(AbstractShape2D shape,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new ShapedAdjacentForEacherDijkstra(shape, isWalkableTester, distanceManager);
	}

	// TODO

	@Override
	public List<Point> getPathGeneralized(NodeIsom start, NodeIsom dest, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester, AbstractAdjacentForEacher<Distance> fa) {
		final InSpaceObjectsManager<Distance> m;
		// NodeIsomProvider m; // MatrixInSpaceObjectsManager<Distance> m;
		NodeInfoFrontierBased<Distance> ss, dd;
		final Map<NodeIsom, NodeInfoFrontierBased<Distance>> nodeInfos;
		final PriorityQueueKey<NodeInfoFrontierBased<Distance>, Distance> frontier;
		final Comparator<Distance> comp;
		AbstractAdjacentForEacherDijkstra forAdjacents;
		m = isom;
		comp = distanceManager.getComparator();
		forAdjacents = (AbstractAdjacentForEacherDijkstra) fa;
		//
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, NodeIsom.COMPARATOR_NODE_ISOM_POINT);
		frontier = new PriorityQueueKey<>(COMPARATOR_NINFO, comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious,
				(NodeInfoFrontierBased<Distance> no) -> no.distFromStart);
		ss = new NodeInfoFrontierBased<Distance>(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoFrontierBased<Distance>(dest);
		nodeInfos.put(dest, dd);

		// set non-final parameters
		forAdjacents.nodeInfos = nodeInfos;
		forAdjacents.frontier = frontier;

		while (!frontier.isEmpty()) {
			final NodeInfoFrontierBased<Distance> n;
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

	protected abstract class AbstractAdjacentForEacherDijkstra extends AbstractAdjacentForEacher<Distance> {

		protected Map<NodeIsom, NodeInfoFrontierBased<Distance>> nodeInfos;
		protected PriorityQueueKey<NodeInfoFrontierBased<Distance>, Distance> frontier;
		protected final DistanceKeyAlterator<NodeInfoFrontierBased<Distance>, Distance> alterator;

		public AbstractAdjacentForEacherDijkstra(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.alterator = new DistanceKeyAlterator<>();
		}

		@Override
		public void accept(NodeIsom adjacent, Distance distToAdj) {
			Distance distToNo;
			NodeInfoFrontierBased<Distance> noInfo;
			if (!isAdjacentNodeWalkable(adjacent))
				return;
			if (nodeInfos.containsKey(adjacent))
				noInfo = nodeInfos.get(adjacent);
			else
				nodeInfos.put(adjacent, noInfo = new NodeInfoFrontierBased<>(adjacent));

			if (noInfo.color == NodePositionInFrontier.Closed)
				return;
			distToNo = // distToAdj + currentNode.distFromStart;
					this.distanceManager.getAdder().apply(distToAdj,
							((NodeInfoFrontierBased<Distance>) currentNode).distFromStart);
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
// nothing more to do here
		protected UsynchronizedAdjacentForEacherDijkstra(//
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}
	}

	protected class ShapedAdjacentForEacherDijkstra extends ShapedAdjacentForEacherBaseImpl {

		public ShapedAdjacentForEacherDijkstra(AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(PathFinderIsomDijkstra.this, shape, isWalkableTester, distanceManager);
			this.afed = new UsynchronizedAdjacentForEacherDijkstra(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected final AbstractAdjacentForEacherDijkstra afed;

		protected boolean ianw(NodeIsom adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void accept(NodeIsom adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}