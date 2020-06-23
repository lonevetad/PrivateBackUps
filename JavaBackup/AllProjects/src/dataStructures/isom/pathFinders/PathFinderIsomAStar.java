package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.isom.InSpaceObjectsManager;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderIsomAStar<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	protected final Comparator<NodeInfoAstar> COMPARATOR_NINFO = (ni1, ni2) -> {
		if (ni1 == ni2)
			return 0;
		if (ni1 == null)
			return -1;
		if (ni2 == null)
			return 1;
		return NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(ni1.thisNode, ni2.thisNode);
	};

	public PathFinderIsomAStar(InSpaceObjectsManager<Distance> matrix, BiFunction<Point, Point, Distance> heuristic) {
		super(matrix);
		this.heuristic = heuristic;
	}

	protected BiFunction<Point, Point, Distance> heuristic;

	public BiFunction<Point, Point, Distance> getHeuristic() { return heuristic; }

	public void setHeuristic(BiFunction<Point, Point, Distance> heuristic) { this.heuristic = heuristic; }

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> fa, NodeIsom start, NodeIsom dest) {
//		final NodeIsomProvider m;
		NodeInfoAstar ss, dd;
		final Map<NodeIsom, NodeInfoAstar> nodeInfos;
		final PriorityQueueKey<NodeInfoAstar, Distance> frontier;
		final Comparator<Distance> comp;
		AbstractAdjacentForEacherAstar forAdjacents;
//		m = isom;
		comp = distanceManager.getComparator();
		forAdjacents = (AbstractAdjacentForEacherAstar) fa;
		//
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, NodeIsom.COMPARATOR_NODE_ISOM_POINT);
		frontier = new PriorityQueueKey<>(COMPARATOR_NINFO, comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious,
				(NodeInfoAstar no) -> no.distFromStart);
		ss = new NodeInfoAstar(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoAstar(dest);
		nodeInfos.put(dest, dd);

		// set non-final parameters
		forAdjacents.nodeInfos = nodeInfos;
		forAdjacents.frontier = frontier;

		while ((!frontier.isEmpty()) && dd.father == null) {
			final NodeInfoAstar n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.distFromStart > n.distFromStart
					comp.compare(dd.fScore, n.fScore) > 0) {
				forAdjacents.setCurrentNode(n);
				getNodeIsomProvider().forEachAdjacents(n.thisNode, forAdjacents);
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
		return PathFinderIsom.extractPath(ss, dd);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> nodeProvider,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new AFEAStar_Point(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> nodeProvider, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape) {
		return new AFEAStar_Shape(this, nodeIsomProvider, shape, isWalkableTester, distanceManager);
	}

	//

	//

	protected class NodeInfoAstar extends NodeInfoFrontierBased<Distance> {
		protected Distance distFromStart, distFromFather, fScore;

		protected NodeInfoAstar(NodeIsom thisNode) {
			super(thisNode);
			distFromStart = distFromFather = null;
		}
	}

	protected class DistanceKeyAlteratorAStar extends DistanceKeyAlterator<NodeInfoAstar, Distance> {
		Distance fScore;

		@Override
		public void accept(NodeInfoAstar nodd) { nodd.fScore = fScore; }
	}

	protected abstract class AbstractAdjacentForEacherAstar extends AbstractAdjacentForEacher<Distance> {
		public AbstractAdjacentForEacherAstar(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.alterator = new DistanceKeyAlteratorAStar();
		}

		protected Map<NodeIsom, NodeInfoAstar> nodeInfos;
		protected PriorityQueueKey<NodeInfoAstar, Distance> frontier;
		protected final DistanceKeyAlteratorAStar alterator;

		@Override
		public void accept(NodeIsom nnn, Distance distToAdj) {
			Distance distStartToNeighbour, fScore;
			NodeInfoAstar noInfo;
			if (!isAdjacentNodeWalkable(nnn))
				return;
			if (nodeInfos.containsKey(nnn))
				noInfo = nodeInfos.get(nnn);
			else
				nodeInfos.put(nnn, noInfo = new NodeInfoAstar(nnn));

			if (noInfo.color == NodePositionInFrontier.Closed)
				return;
			distStartToNeighbour = this.distanceManager.getAdder().apply(distToAdj,
					((NodeInfoAstar) currentNode).distFromStart);
			if (noInfo.father == null
					|| distanceManager.getComparator().compare(distStartToNeighbour, noInfo.distFromStart) < 0) {
				// update
				noInfo.father = currentNode;
				noInfo.distFromFather = distToAdj;
				noInfo.distFromStart = distStartToNeighbour;
				fScore = this.distanceManager.getAdder().apply(distStartToNeighbour,
						heuristic.apply(currentNode.thisNode.getLocation(), noInfo.thisNode.getLocation()));

				if (noInfo.color == NodePositionInFrontier.NeverAdded) {
					// add on queue
					noInfo.color = NodePositionInFrontier.InFrontier;
					noInfo.distFromStart = distStartToNeighbour;
					noInfo.fScore = fScore;
					//
					frontier.put(noInfo);
				} else {
					// it's grey, it's actually in the queue
					alterator.fScore = fScore;
					frontier.alterKey(noInfo, alterator);
				}
			}
		}
	}

	protected class AFEAStar_Point extends AbstractAdjacentForEacherAstar {

		public AFEAStar_Point(
//				PathFInderAStar<Distance> pathFInderAStar_Matrix,
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
//			pathFInderAStar_Matrix.
			super(isWalkableTester, distanceManager);
		}
	}

	protected class AFEAStar_Shape extends ShapedAdjacentForEacherBaseImpl {
		protected AFEAStar_Shape(PathFinderIsom<Point, ObjectLocated, Distance> pathFinderIsom,
				NodeIsomProvider<Distance> m, AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AFEAStar_Point(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected final AFEAStar_Point afed;

		protected boolean ianw(NodeIsom adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void accept(NodeIsom adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}