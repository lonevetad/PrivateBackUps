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
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;
import tools.NumberManager;

public class PathFinderIsomBFS<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomBFS(MatrixInSpaceObjectsManager<Distance> matrix) { super(matrix); }

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> forAdjacents, NodeIsom start, NodeIsom dest) {
		Map<Integer, NodeInfoFrontierBased<Distance>> nodes;
		Queue<NodeInfoFrontierBased<Distance>> queue;
		NodeInfoFrontierBased<Distance> ss, ee, niCurrent;
		AAFE_BFS fa;
		queue = new LinkedList<>();
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		fa = (AAFE_BFS) forAdjacents;
		fa.queue = queue;
		fa.nodes = nodes;
		ss = new NodeInfoFrontierBased<Distance>(start);
		ee = new NodeInfoFrontierBased<Distance>(dest);
		nodes.put(start.getID(), ss);
		nodes.put(dest.getID(), ee);
		queue.add(ss);
		ss.color = NodePositionInFrontier.InFrontier;
		while (!queue.isEmpty()) {
			niCurrent = queue.poll();
			if (niCurrent == ee) {
				// found
				queue.clear();
			} else {
				this.getNodeIsomProvider().forEachAdjacents(niCurrent.thisNode, fa);
				niCurrent.color = NodePositionInFrontier.Closed;
			}
		}
		return PathFinderIsom.extractPath(ss, ee);
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
		public void accept(NodeIsom adjacent, Distance u) {
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

		protected Shaped_AAFE_BFS(PathFinderIsom<Point, ObjectLocated, Distance> pathFinderIsom,
				NodeIsomProvider<Distance> m, AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AAFE_BFS(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected boolean ianw(NodeIsom adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void accept(NodeIsom adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}