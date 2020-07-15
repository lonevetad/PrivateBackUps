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

public class PathFinderIsomAStar_V2<Distance extends Number> extends PathFinderIsomBaseImpl<Distance> {

	public PathFinderIsomAStar_V2(NodeIsomProvider<Distance> matrix, BiFunction<Point, Point, Distance> heuristic) {
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
		NodeInfoAstar_V2 ss, dd;
		final Map<NodeIsom<Distance>, NodeInfoAstar_V2> nodeInfos;
//		final PriorityQueueKey<NodeInfoAstar_V2, Distance> frontier;
		final PriorityQueueKey<NodeInfoAstar_V2, NodeInfoAstar_V2> frontier;
		final Comparator<Distance> comp;
		final Comparator<NodeInfoAstar_V2> nodeInfoComp;
		AbstractAdjacentForEacherAstar_V2 forAdjacents;
		AbstractAdjacentForEacher<Distance> forAdjToBeUsed;
		// added for the boolean parameter
		NodeInfoFrontierBased<Distance> closestPointToDest = null;
		Distance lowestDistance = null;
		Point absoluteDestPoint;
		//
		comp = distanceManager.getComparator();
		forAdjacents = null;
		forAdjToBeUsed = fa;
		if (AFEAStar_Shape_V2.class.isAssignableFrom(fa.getClass())) {
			forAdjacents = ((AFEAStar_Shape_V2) fa).afed;
		} else if (AbstractAdjacentForEacherAstar_V2.class.isAssignableFrom(fa.getClass())) {
			forAdjacents = (AbstractAdjacentForEacherAstar_V2) fa;
		}
		//
		nodeInfoComp = (p1as, p2as) -> COMPARATOR_NINFO_AS_POINT.compare(p1as, p2as);
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, MapTreeAVL.BehaviourOnKeyCollision.Replace, //
				(n1, n2) -> NodeIsom.COMPARATOR_NODE_ISOM_POINT.compare(n1, n2));
//		frontier = new PriorityQueueKey<>(//
//				(p1as, p2as) -> COMPARATOR_NINFO.compare(p1as, p2as), //
//				comp, MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, (NodeInfoAstar_V2 no) -> no.fScore);
		frontier = new PriorityQueueKey<>(//
				nodeInfoComp, //
				(n1, n2) -> {
					int c;
					c = nodeInfoComp.compare(n1, n2); // first of all, no equal nodes allowed
					if (c == 0)
						return 0;
					c = comp.compare(n1.fScore, n2.fScore);
					if (c == 0) { // don't know WHY this causes bugs
						c = comp.compare(n1.distFromStart, n2.distFromStart);
					}
					return c;
				}, MapTreeAVL.BehaviourOnKeyCollision.Replace, (NodeInfoAstar_V2 no) -> no);
		ss = new NodeInfoAstar_V2(start);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = distanceManager.getZeroValue();
//		System.out.println("start: " + start.getLocationAbsolute());
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoAstar_V2(dest);
		nodeInfos.put(dest, dd);
		absoluteDestPoint = dest.getLocationAbsolute();
		// set non-final parameters
		forAdjacents.nodeInfos = nodeInfos;
		forAdjacents.frontier = frontier;
//		System.out.println("EEEEEEEEEEEEENDd: " + dest.getLocationAbsolute());

		while ((!frontier.isEmpty()) && dd.father == null) {
			final NodeInfoAstar_V2 n;
			int sb;
			sb = frontier.size();
//			System.out.println("__size of frontier before computing: " + sb);
//			System.out.println(frontier.getKeys());
			n = frontier.removeMinimum().getKey();
//			System.out.println(
//					"computing " + n.thisNode.getLocationAbsolute() + ", size of frontier: " + frontier.size());
//			System.out.println(frontier.getKeys());
			if (sb == frontier.size()) {
				System.out.println(frontier.getKeys());
//				throw new RuntimeException("WTF");
				System.exit(-1);
			}
			/*
			 * do not waste time computing nodes that have longer path of the already
			 * discovered ones
			 */
			n.color = NodePositionInFrontier.Closed;
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
		}
		nodeInfos.clear();
		return PathFinderIsom.extractPathFromEndOrClosest(ss, dd, returnPathToClosestNodeIfNotFound,
				closestPointToDest);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumer(final NodeIsomProvider<Distance> nodeProvider,
			Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
		return new AFEAStar_Point_V2(isWalkableTester, distanceManager);
	}

	@Override
	public AbstractAdjacentForEacher<Distance> newAdjacentConsumerForObjectShaped(
			final NodeIsomProvider<Distance> nodeProvider, Predicate<ObjectLocated> isWalkableTester,
			NumberManager<Distance> distanceManager, AbstractShape2D shape) {
		return new AFEAStar_Shape_V2(this, nodeIsomProvider, shape, isWalkableTester, distanceManager);
	}

	//

	//

	protected class NodeInfoAstar_V2 extends NodeInfoFrontierBased<Distance> {
		protected Distance fScore;

		protected NodeInfoAstar_V2(NodeIsom<Distance> thisNode) {
			super(thisNode);
			fScore = null;
		}

		@Override
		public String toString() {
			Point p;
			p = thisNode.getLocationAbsolute();
			return "[FS=" + fScore + ", LOC= (x=" + p.x + ",y=" + p.y
					+ ")" /*
							 * + ", COL=" + color + ", DFS=" + distFromStart
							 */ + "]";
		}
	}

	protected class DistanceKeyAlteratorAStar extends DistanceKeyAlterator<NodeInfoAstar_V2, Distance> {
		protected Distance fScore;

		@Override
		public void accept(NodeInfoAstar_V2 nodd) { nodd.fScore = fScore; }
	}

	protected abstract class AbstractAdjacentForEacherAstar_V2 extends AbstractAdjacentForEacher<Distance> {
		public AbstractAdjacentForEacherAstar_V2(Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(isWalkableTester, distanceManager);
			this.alterator = new DistanceKeyAlteratorAStar();
		}

		protected Map<NodeIsom<Distance>, NodeInfoAstar_V2> nodeInfos;
//		protected PriorityQueueKey<, Distance> frontier;
		protected PriorityQueueKey<NodeInfoAstar_V2, NodeInfoAstar_V2> frontier;
		protected final DistanceKeyAlteratorAStar alterator;

		@Override
		public void accept(NodeIsom<Distance> nnn, Distance distToAdjacent) {
			Distance distStartToNeighbour, fScore;
			NodeInfoAstar_V2 noInfo;
			if (!isAdjacentNodeWalkable(nnn))
				return;
			if (nodeInfos.containsKey(nnn))
				noInfo = nodeInfos.get(nnn);
			else
				nodeInfos.put(nnn, noInfo = new NodeInfoAstar_V2(nnn));

			if (noInfo.color == NodePositionInFrontier.Closed)
				return;
			distStartToNeighbour = this.distanceManager.getAdder().apply(distToAdjacent, currentNode.distFromStart);
//			System.out.println("\tadjacent: " + nnn.getLocationAbsolute());
			if (noInfo.father == null
					|| distanceManager.getComparator().compare(distStartToNeighbour, noInfo.distFromStart) < 0) {
				// update
				noInfo.father = currentNode;
				noInfo.distFromFather = distToAdjacent;
				noInfo.distFromStart = distStartToNeighbour;
				fScore = this.distanceManager.getAdder().apply(distStartToNeighbour, heuristic
						.apply(currentNode.thisNode.getLocationAbsolute(), noInfo.thisNode.getLocationAbsolute()));

				if (noInfo.color == NodePositionInFrontier.NeverAdded//
						&& (!frontier.containsKey(noInfo))//
				) {
					// add on queue
					noInfo.color = NodePositionInFrontier.InFrontier;
					noInfo.distFromStart = distStartToNeighbour;
					noInfo.fScore = fScore;
					//
//					System.out.println("\t\t putting");
//					frontier.megaprint();
					frontier.put(noInfo);
//					System.out.println("\t\t put");
				} else {
					// it's grey, it's actually in the queue
//					System.out.println("\t altering");
					noInfo.color = NodePositionInFrontier.InFrontier;
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

	protected class AFEAStar_Point_V2 extends AbstractAdjacentForEacherAstar_V2 {

		public AFEAStar_Point_V2(
//				PathFInderAStar<Distance> pathFInderAStar_Matrix,
				Predicate<ObjectLocated> isWalkableTester, NumberManager<Distance> distanceManager) {
//			pathFInderAStar_Matrix.
			super(isWalkableTester, distanceManager);
		}
	}

	protected class AFEAStar_Shape_V2 extends ShapedAdjacentForEacherBaseImpl {
		protected AFEAStar_Shape_V2(PathFinderIsom<Distance> pathFinderIsom, NodeIsomProvider<Distance> m,
				AbstractShape2D shape, Predicate<ObjectLocated> isWalkableTester,
				NumberManager<Distance> distanceManager) {
			super(pathFinderIsom, m, shape, isWalkableTester, distanceManager);
			this.afed = new AFEAStar_Point_V2(isWalkableTester, distanceManager) {
				@Override
				public boolean isAdjacentNodeWalkable(NodeIsom<Distance> adjacentNode) { return ianw(adjacentNode); }
			};
		}

		protected final AFEAStar_Point_V2 afed;

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