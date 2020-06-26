package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;
import tools.NumberManager;

public class PathFinderIsomBFS<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomBFS(NodeIsomProvider<Distance> matrix) { super(matrix); }

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, boolean returnPathToClosestNodeIfNotFound,
			NodeIsom<Distance> start, NodeIsom<Distance> dest) {
		Map<Integer, NodeInfoFrontierBased<Distance>> nodes;
		Queue<NodeInfoFrontierBased<Distance>> queue;
		NodeInfoFrontierBased<Distance> ss, ee, niCurrent;
		AAFE_BFS fa;
		// added for the boolean parameter
		NodeInfoFrontierBased<Distance> closestPointToDest = null;
		Distance lowestDistance = null;
		Point absoluteDestPoint;
		//
		queue = new LinkedList<>();
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		fa = (AAFE_BFS) forAdjacents;
		fa.queue = queue;
		fa.nodes = nodes;
		ss = new NodeInfoFrontierBased<Distance>(start);
		ee = null; // new NodeInfoFrontierBased<Distance>(dest);
		nodes.put(start.getID(), ss);
//		nodes.put(dest.getID(), ee);
		queue.add(ss);
		ss.color = NodePositionInFrontier.InFrontier;
		absoluteDestPoint = dest.getLocationAbsolute();
		while (!queue.isEmpty()) {
			niCurrent = queue.poll();
			if (niCurrent.thisNode == dest) {
				// found
				queue.clear();
				ee = niCurrent;
			} else {
				// added for the boolean parameter
				if (returnPathToClosestNodeIfNotFound) {
					Distance newDistance;
					newDistance = distanceBetweenPoints(niCurrent.thisNode.getLocationAbsolute(), absoluteDestPoint,
							distanceManager);
					if (closestPointToDest == null || //
							distanceManager.getComparator().compare(newDistance, lowestDistance) < 0) {
						lowestDistance = newDistance;
						closestPointToDest = niCurrent;
					}
				}
				fa.setCurrentNode(niCurrent);
				this.getNodeIsomProvider().forEachAdjacents(niCurrent.thisNode, fa);
				niCurrent.color = NodePositionInFrontier.Closed;
			}
		}
		return PathFinderIsom.extractPathFromEndOrClosest(ss, ee, returnPathToClosestNodeIfNotFound,
				closestPointToDest);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> nodeProvider,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new AAFE_BFS(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> nodeProvider, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape) {
		return new Shaped_AAFE_BFS(this, nodeIsomProvider, shape, isWalkableTester, distanceManager);
	}

	//

	protected class AAFE_BFS extends AbstractAdjacentForEacher<Distance> {
		protected Map<Integer, NodeInfoFrontierBased<Distance>> nodes;
		protected Queue<NodeInfoFrontierBased<Distance>> queue;

		public AAFE_BFS(Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance u) {
			NodeInfoFrontierBased<Distance> niAdj;
			Integer adjID;
			if (!isAdjacentNodeWalkable(adjacent))
				return;
			adjID = adjacent.getID();
			niAdj = null;
			if (nodes.containsKey(adjID) && (niAdj = nodes.get(adjID)).color != NodePositionInFrontier.NeverAdded)
				return; // unable to handle this adjacent
			if (niAdj == null) {
				// not contained
				niAdj = new NodeInfoFrontierBased<Distance>(adjacent);
				niAdj.color = NodePositionInFrontier.InFrontier;
				niAdj.father = currentNode;
				nodes.put(adjID, niAdj);
				queue.add(niAdj);
			} // else: discard it
		}
	}

	protected class Shaped_AAFE_BFS extends ShapedAdjacentForEacherBaseImpl {

		protected final AAFE_BFS afed;

		protected Shaped_AAFE_BFS(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AAFE_BFS(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom<Distance> adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected boolean ianw(NodeIsom<Distance> adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}