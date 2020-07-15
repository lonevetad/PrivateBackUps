package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.isom.NodeIsom;
import dataStructures.isom.NodeIsomProvider;
import dataStructures.isom.PathFinderIsom;
import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import tools.NumberManager;

public class PathFinderIsomAStar_Naive<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomAStar_Naive(NodeIsomProvider<Distance> matrix, BiFunction<Point, Point, Distance> heuristic) {
		super(matrix);
		this.heuristic = heuristic;
	}

	protected BiFunction<Point, Point, Distance> heuristic;

	public BiFunction<Point, Point, Distance> getHeuristic() { return heuristic; }

	public void setHeuristic(BiFunction<Point, Point, Distance> heuristic) { this.heuristic = heuristic; }

	@Override
	public Distance distanceBetweenPoints(Point pStart, Point pEnd, NumberManager<Distance> distanceManager) {
		BiFunction<Point, Point, Distance> h;
		return ((h = heuristic) != null) ? h.apply(pStart, pEnd)
				: super.distanceBetweenPoints(pStart, pEnd, distanceManager);
	}

	@Override
	public List<Point> getPathGeneralized(final NodeIsomProvider<Distance> nodeProvider,
			NumberManager<Distance> distanceManager, Predicate<ObjectLocated> isWalkableTester,
			AbstractAdjacentForEacher<Distance> fa, boolean returnPathToClosestNodeIfNotFound, NodeIsom<Distance> start,
			NodeIsom<Distance> dest) {
		NodeInfoAstar ss, dd;
		final Map<NodeIsom<Distance>, NodeInfoAstar> nodeInfos;
		final PriorityQueueKey<NodeInfoAstar, Distance> frontier;
		final Comparator<Distance> comp;
		AbstractAdjacentForEacherAstar forAdjacents;
		AbstractAdjacentForEacher<Distance> forAdjToBeUsed;
		// added for the boolean parameter
		NodeInfoFrontierBased<Distance> closestPointToDest = null;
		Distance lowestDistance = null;
		Point absoluteDestPoint;
		//
		comp = distanceManager.getComparator();
		forAdjacents = null;
		forAdjToBeUsed = fa;
		if (AFEAStar_Shape.class.isAssignableFrom(fa.getClass())) {
			forAdjacents = ((AFEAStar_Shape) fa).afed;
		} else if (AbstractAdjacentForEacherAstar.class.isAssignableFrom(fa.getClass())) {
			forAdjacents = (AbstractAdjacentForEacherAstar) fa;
		}
		//
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, //
				(n1, n2) -> NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(n1, n2));
		frontier = new PriorityQueueKey<>(//
				(p1as, p2as) -> COMPARATOR_NINFO_AS_POINT.compare(p1as, p2as), //
				comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, (NodeInfoAstar no) -> no.fScore);
		ss = new NodeInfoAstar(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = null;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoAstar(dest);
		nodeInfos.put(dest, dd);
		absoluteDestPoint = dest.getLocationAbsolute();
		// set non-final parameters
		forAdjacents.nodeInfos = nodeInfos;
		forAdjacents.frontier = frontier;

		while ((!frontier.isEmpty()) && dd.father == null) {
			final NodeInfoAstar n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the already
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.distFromStart > n.distFromStart
					comp.compare(dd.fScore, n.fScore) > 0) {
				// added for the boolean parameter
				if (returnPathToClosestNodeIfNotFound) {
					Distance newDistance;
					newDistance = distanceBetweenPoints(n.thisNode.getLocationAbsolute(), absoluteDestPoint,
							distanceManager);
					if (closestPointToDest == null || //
							distanceManager.getComparator().compare(newDistance, lowestDistance) < 0) {
						lowestDistance = newDistance;
						closestPointToDest = n;
					}
				}
				forAdjToBeUsed.setCurrentNode(n);
				getNodeIsomProvider().forEachAdjacents(n.thisNode, forAdjToBeUsed);
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
		return PathFinderIsom.extractPathFromEndOrClosest(ss, dd, returnPathToClosestNodeIfNotFound,
				closestPointToDest);
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
		protected Distance fScore;

		protected NodeInfoAstar(NodeIsom<Distance> thisNode) {
			super(thisNode);
			fScore = null;
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

		protected Map<NodeIsom<Distance>, NodeInfoAstar> nodeInfos;
		protected PriorityQueueKey<NodeInfoAstar, Distance> frontier;
		protected final DistanceKeyAlteratorAStar alterator;

		@Override
		public void accept(NodeIsom<Distance> nnn, Distance distToAdjacent) {
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
			distStartToNeighbour = this.distanceManager.getAdder().apply(distToAdjacent, currentNode.distFromStart);
			if (noInfo.father == null
					|| distanceManager.getComparator().compare(distStartToNeighbour, noInfo.distFromStart) < 0) {
				// update
				noInfo.father = currentNode;
				noInfo.distFromFather = distToAdjacent;
				noInfo.distFromStart = distStartToNeighbour;
				fScore = this.distanceManager.getAdder().apply(distStartToNeighbour, heuristic
						.apply(currentNode.thisNode.getLocationAbsolute(), noInfo.thisNode.getLocationAbsolute()));

				if (noInfo.color == NodePositionInFrontier.NeverAdded) {
					// add on queue
					noInfo.color = NodePositionInFrontier.InFrontier;
					noInfo.distFromStart = distStartToNeighbour;
					noInfo.fScore = fScore;
					frontier.put(noInfo);
				} else {
					// it's grey, it's actually in the queue
//					System.out.println("\t altering");
					alterator.fScore = fScore;
					frontier.alterKey(noInfo, alterator);
//					System.out.println("\t altered");
				}
			}
			/*
			 * else { System.out.println("\t discarded"); }
			 */
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
		protected AFEAStar_Shape(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AFEAStar_Point(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom<Distance> adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected final AFEAStar_Point afed;

		protected boolean ianw(NodeIsom<Distance> adjacentNode) { return isAdjacentNodeWalkable(adjacentNode); }

		@Override
		public void setCurrentNode(NodeInfo<Distance> currentNode) {
			super.setCurrentNode(currentNode);
			afed.setCurrentNode(currentNode);
		}

		@Override
		public void accept(NodeIsom<Distance> adjacent, Distance distToAdj) { afed.accept(adjacent, distToAdj); }
	}
}