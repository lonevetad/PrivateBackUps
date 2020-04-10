package dataStructures.graph.pathfind;

import java.util.Comparator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimple.NodePositionInFrontier;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.NodeDistanceManager;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;

public class PathFindAStar<E, Distance> implements PathFindStrategy<E, Distance> {
	private static final long serialVersionUID = 56409561023300330L;

	public PathFindAStar(BiFunction<E, E, Distance> heuristic) {
		super();
		this.heuristic = heuristic;
	}

	protected BiFunction<E, E, Distance> heuristic;

	public BiFunction<E, E, Distance> getHeuristic() {
		return heuristic;
	}

	public PathFindAStar<E, Distance> setHeuristic(BiFunction<E, E, Distance> heuristic) {
		this.heuristic = heuristic;
		return this;
	}

	//

	@SuppressWarnings("unchecked")
	@Override
	public PathGraph<E, Distance> getPath(GraphSimple<E, Distance> graph, E start, E dest,
			NodeDistanceManager<Distance> distanceManager) {
//		final double distanceTotal;
		PathGraph<E, Distance> p;
		GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized s, d;
		NodeInfoAStar<E, Distance> ss, dd;
		final Map<E, NodeInfoAStar<E, Distance>> nodeInfos;
		PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier;
		UsynchronizedAdjacentForEacherAStar<E, Distance> forAdjacents;
		Comparator<Distance> comp;
		comp = distanceManager.getComparator();
		{ // scope for the heuristic, to pick the variable as soon as possible and then
			// free its space on the stack
			BiFunction<E, E, Distance> h;
			h = heuristic;
			if (h == null)
				return null;

			nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
					MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, graph.getComparatorElements());
			frontier = new PriorityQueueKey<>((i1, i2) -> graph.getCompNodeGraph().compare(i1.thisNode, i2.thisNode),
					comp, no -> no.fScore);
			forAdjacents = new UsynchronizedAdjacentForEacherAStar<E, Distance>(nodeInfos, frontier, h,
					distanceManager);
		}
		s = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(start);
		d = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;
		ss = new NodeInfoAStar<E, Distance>(s, distanceManager);
		ss.father = ss;
		ss.fScore =
//				Double.valueOf(0);
				ss.distFromFather = ss.distFromStart = distanceManager.getZeroValue(); // 0.0;
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoAStar<E, Distance>(d, distanceManager);
		nodeInfos.put(dest, dd);

		while ((!frontier.isEmpty()) && ((dd.father == null) ||
		/*
		 * continue if there's a path to reach the destination shorter to the already
		 * found one. This condition will force to exit from the cycle if the "minimum"
		 * node is already the destination because of the lack of equality check
		 */
				( // frontier.peekMinimum().getKey().fScore < dd.fScore)
				comp.compare(frontier.peekMinimum().getKey().fScore, dd.fScore) < 0))//
		) {
			final NodeInfoAStar<E, Distance> n;
			n = frontier.removeMinimum().getKey();
			n.color = NodePositionInFrontier.InFrontier;
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null ||
			// dd.fScore > n.fScore
					comp.compare(dd.fScore, n.fScore) > 0) {
				forAdjacents.setCurrentNode(n);
				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
		}
		if (dd.father == null)
			return null;
		//
		nodeInfos.clear();
		p = new PathGraph<E, Distance>(distanceManager);
//		distanceTotal = dd.distFromStart;
		while (dd != ss) {
			p.addStep(dd.thisNode.getElem(), dd.distFromFather);
			dd = dd.father;
		}
		p.setStartStep(s.getElem());
//		p.setDistanceTotal(distanceTotal);
		return p;
	}

//	protected static boolean existsAShorterPath()

	//

	// subclasses

	protected static class NodeInfoAStar<E, Distance> {
		protected NodePositionInFrontier color;
		protected Distance distFromStart, distFromFather, fScore;
		protected GraphSimple<E, Distance>.NodeGraph thisNode;
		protected NodeInfoAStar<E, Distance> father;

		protected NodeInfoAStar(GraphSimple<E, Distance>.NodeGraph thisNode,
				NodeDistanceManager<Distance> distanceManager) {
			this.thisNode = thisNode;
			color = NodePositionInFrontier.NeverAdded;
			distFromStart = distFromFather = distanceManager.getZeroValue();
			fScore = null;
			father = null;
		}
	}

	private static class UsynchronizedAdjacentForEacherAStar<E, Distance>
			implements BiConsumer<GraphSimple<E, Distance>.NodeGraph, Distance> {

		private NodeInfoAStar<E, Distance> currentNode;
		private final Map<E, NodeInfoAStar<E, Distance>> nodeInfos;
		private final PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier;
		private final BiFunction<E, E, Distance> heuristic;
		private final NumberManager<Distance> distanceManager;

		UsynchronizedAdjacentForEacherAStar(Map<E, NodeInfoAStar<E, Distance>> nodeInfos,
				PriorityQueueKey<NodeInfoAStar<E, Distance>, Distance> frontier, BiFunction<E, E, Distance> heuristic,
				NodeDistanceManager<Distance> distanceManager) {
			this.nodeInfos = nodeInfos;
			this.frontier = frontier;
			this.heuristic = heuristic;
			this.distanceManager = distanceManager;
		}

		public void setCurrentNode(NodeInfoAStar<E, Distance> n) {
			this.currentNode = n;
		}

//		public void accept(GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized nod, Integer distToAdj) {
		@SuppressWarnings("unchecked")
		@Override
		public void accept(GraphSimple<E, Distance>.NodeGraph nnn, Distance distToAdj) {
			Distance distToNo, distToAdjDouble;
			E e;
			GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized no;
			NodeInfoAStar<E, Distance> neighbourInfo;
			no = (GraphSimpleAsynchronized<E, Distance>.NodeGraphSimpleAsynchronized) nnn;
			e = no.getElem();
			if (nodeInfos.containsKey(e))
				neighbourInfo = nodeInfos.get(e);
			else
				nodeInfos.put(e, neighbourInfo = new NodeInfoAStar<E, Distance>(no, distanceManager));

//			if (neighbourInfo.color == NodePositionInFrontier.Closed) // equivalent of being in closed set
//				return;
			distToAdjDouble = distToAdj;
			distToNo =
					// distToAdjDouble + currentNode.distFromStart
					distanceManager.getAdder().apply(distToAdjDouble, currentNode.distFromStart);
			// create the new node or try to re-opening it
			if (neighbourInfo.father == null ||
			// distToNo < neighbourInfo.distFromStart
					distanceManager.getComparator().compare(distToNo, neighbourInfo.distFromStart) < 0) {
//				final Double newDistanceFromStart,
				Distance fScore;
				// update
//				newDistanceFromStart = Double.valueOf(distToNo);
				neighbourInfo.father = currentNode;
				neighbourInfo.distFromFather = distToAdjDouble;
				neighbourInfo.distFromStart = distToNo; // newDistanceFromStart;
				fScore = // newDistanceFromStart +
						distanceManager.getAdder().apply(distToNo,
								this.heuristic.apply(currentNode.thisNode.getElem(), neighbourInfo.thisNode.getElem()));
				if (neighbourInfo.color == NodePositionInFrontier.NeverAdded) {
					// track that's in open set, e.g. it has been seen almost one time
					neighbourInfo.color = NodePositionInFrontier.InFrontier;
					neighbourInfo.fScore = fScore;
					// add on queue
					frontier.put(neighbourInfo);
				} else // it's grey, it's actually in the queue / open set
					frontier.alterKey(neighbourInfo, nodd -> nodd.fScore = fScore);
			}
		}
	}
}