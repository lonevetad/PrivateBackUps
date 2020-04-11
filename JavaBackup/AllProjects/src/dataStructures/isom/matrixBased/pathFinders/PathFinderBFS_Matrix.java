package dataStructures.isom.matrixBased.pathFinders;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.Comparators;
import tools.NumberManager;

public class PathFinderBFS_Matrix<Distance extends Number> extends PathFinderIsomMatrix<Distance> {

	public PathFinderBFS_Matrix(MatrixInSpaceObjectsManager<Distance> matrix) {
		super(matrix);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Point> getPathGeneralized(NodeIsom start, NodeIsom dest, NumberManager<Distance> distanceManager,
			Predicate<ObjectLocated> isWalkableTester, AbstractAdjacentForEacher forAdjacents) {
		Map<Integer, NodeInfo> nodes;
		Queue<NodeInfo> queue;
		NodeInfo ss, ee, niCurrent;
		AAFE_BFS fa;
		queue = new LinkedList<>();
		nodes = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		fa = (AAFE_BFS) forAdjacents;
		fa.queue = queue;
		fa.nodes = nodes;
		ss = new NodeInfo(start);
		ee = new NodeInfo(dest);
		nodes.put(start.getID(), ss);
		nodes.put(dest.getID(), ee);
		queue.add(ss);
		ss.color = NodePositionInFrontier.InFrontier;
		while(!queue.isEmpty()) {
			niCurrent = queue.poll();
			if (niCurrent == ee) {
				// found
				queue.clear();
			} else {
				this.matrix.forEachAdjacents(niCurrent.thisNode, fa);
				niCurrent.color = NodePositionInFrontier.Closed;
			}
		}
		return extractPath(ss, ee);
	}

	@Override
	protected AbstractAdjacentForEacher newAdjacentConsumer(Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AbstractAdjacentForEacher newAdjacentConsumerForObjectShaped(AbstractShape2D shape,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		// TODO Auto-generated method stub
		return null;
	}

	//

	@Deprecated
	protected class NodeBFS extends NodeInfo {
		protected NodeBFS(NodeIsom thisNode) {
			super(thisNode);
		}
	}

	protected class AAFE_BFS extends AbstractAdjacentForEacher {
		protected Map<Integer, NodeInfo> nodes;
		protected Queue<NodeInfo> queue;

		public AAFE_BFS(Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
		}

		@Override
		public void accept(NodeIsom adjacent, Distance u) {
			NodeInfo niAdj;
			Integer adjID;
			adjID = adjacent.getID();
			niAdj = null;
			if (nodes.containsKey(adjID) && (niAdj = nodes.get(adjID)).color != NodePositionInFrontier.NeverAdded)
				return; // unable to handle this adjacent
			if (niAdj == null) {
				// not contained
				niAdj = new NodeInfo(adjacent);
				niAdj.color = NodePositionInFrontier.InFrontier;
				nodes.put(adjID, niAdj);
				queue.add(niAdj);
			} // else: discard it
		}
	}

	protected class Shaped_AAFE_BFS extends AAFE_BFS {

		public Shaped_AAFE_BFS(Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager,
				AbstractShape2D shape, WholeShapeWalkableChecker<Distance> shapeWalkableChecker) {
			super(isWalkableTester, distanceManager);
			this.shape = shape;
			this.shapeWalkableChecker = shapeWalkableChecker;
		}

		protected final AbstractShape2D shape;
		protected final WholeShapeWalkableChecker<Distance> shapeWalkableChecker;
	}
}