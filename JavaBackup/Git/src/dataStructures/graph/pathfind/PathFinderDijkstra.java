package dataStructures.graph.pathfind;

import java.util.Map;
import java.util.function.BiConsumer;

import dataStructures.MapTreeAVL;
import dataStructures.PriorityQueueKey;
import dataStructures.graph.GraphSimple;
import dataStructures.graph.GraphSimple.ColorDijkstra;
import dataStructures.graph.GraphSimpleAsynchronized;
import dataStructures.graph.GraphSimpleSynchronized;
import dataStructures.graph.PathFindStrategy;
import dataStructures.graph.PathGraph;

public class PathFinderDijkstra<E> implements PathFindStrategy<E> {
	private static final long serialVersionUID = -26510959489410020L;

	public PathFinderDijkstra() {
	}

	//

	@Override
	public PathGraph<E> getPath(GraphSimple<E> graph, E start, E dest) {
		if (graph == null)
			return null;
		return getPath(graph.isSynchronized(), graph, start, dest);
	}

	public PathGraph<E> getPath(boolean isSynchronized, GraphSimple<E> graph, E start, E dest) {
		if (graph == null)
			return null;
		return isSynchronized ? getPathSynchronized(graph, start, dest) : getPathASynchronized(graph, start, dest);
	}

	@SuppressWarnings("unchecked")
	public PathGraph<E> getPathSynchronized(GraphSimple<E> ggg, E start, E dest) {
		final int dr;
		PathGraph<E> p;
		GraphSimpleSynchronized<E> graph;
		GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized s, d;
		PriorityQueueKey<GraphSimple<E>.NodeGraph, Integer> frontier;
//				BiConsumer<NodeGraph, Integer> forAdjacents;
		if (!(ggg instanceof GraphSimpleSynchronized<?>))
			return null;
		graph = (GraphSimpleSynchronized<E>) ggg;
		//
		s = (GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) graph.getNode(start);
		d = (GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;
		frontier = new PriorityQueueKey<>(graph.getCompNodeGraph(), GraphSimple.INT_COMPARATOR,
				no -> ((GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) no).getDistFromStart());
		dr = graph.getPathFindRuns();
		s.checkAndReset(dr);
		d.checkAndReset(dr);
		s.setFather(s);
		s.setDistFromFather(0);
		s.setDistFromStart(0);
		frontier.put(s);

//				forAdjacents =
		while (!frontier.isEmpty()) {
			final GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized n;
			n = (GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (d.getFather() == null || d.getDistFromStart() > n.getDistFromStart())
				n.forEachAdjacents((nod, distToAdj) -> {
					int distToNo;
					GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized no;
					no = (GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) nod;
					no.checkAndReset(dr);
					if (no.getColor() == ColorDijkstra.Black)
						return;
					distToNo = distToAdj + n.getDistFromStart();
					if (no.getFather() == null || distToNo < no.getDistFromStart()) {
						final Integer newDistance;
						// update
						newDistance = Integer.valueOf(distToNo);
						no.setFather(n);
						no.setDistFromFather(distToAdj);
						if (no.getColor() == ColorDijkstra.White) {
							// add on queue
							no.setColor(ColorDijkstra.Grey);
							no.setDistFromStart(newDistance);
							frontier.put(no);
						} else // it's grey, it's actually in the queue
							frontier.alterKey(no,
									nodd -> ((GraphSimpleSynchronized<E>.NodeGraphSimpleSynchronized) nodd)
											.setDistFromStart(newDistance));
					}
				});
			else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
			n.setColor(ColorDijkstra.Black);
		}
		graph.setPathFindRuns(graph.getPathFindRuns() + 1);
		if (d.getFather() == null)
			return null;
		//

		p = new PathGraph<E>();

//		distanceTotal = d.getDistFromStart();
		while (d != s) {
			p.addStep(d.getElem(), d.getDistFromFather());
			d = d.getFather();
		}
		p.setStartStep(s.getElem());
//		p.setDistanceTotal(distanceTotal);
		return p;
	}

	//

	@SuppressWarnings("unchecked")
	public PathGraph<E> getPathASynchronized(GraphSimple<E> ggg, E start, E dest) {
//		final double distanceTotal;
		NodeInfoDikstra<E> ss, dd;
		final Map<E, NodeInfoDikstra<E>> nodeInfos;
		PriorityQueueKey<NodeInfoDikstra<E>, Double> frontier;
		PathGraph<E> p;
		GraphSimpleAsynchronized<E> graph;
		GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized s, d;
		UsynchronizedAdjacentForEacherDijkstra<E> forAdjacents;
		if (!(ggg instanceof GraphSimpleAsynchronized<?>))
			return null;
		graph = (GraphSimpleAsynchronized<E>) ggg;
		s = (GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized) graph.getNode(start);
		d = (GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized) graph.getNode(dest);
		if (s == null || d == null || d == s)
			return null;
		nodeInfos = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight,
				MapTreeAVL.BehaviourOnKeyCollision.KeepPrevious, graph.getComparatorElements());
		frontier = new PriorityQueueKey<>((i1, i2) -> graph.getCompNodeGraph().compare(i1.thisNode, i2.thisNode),
				GraphSimple.DOUBLE_COMPARATOR, no -> no.distFromStart);
		ss = new NodeInfoDikstra<E>(s);
		ss.father = ss;
		ss.distFromFather = ss.distFromStart = Double.valueOf(0);
		frontier.put(ss);
		nodeInfos.put(start, ss);
		dd = new NodeInfoDikstra<E>(d);
		nodeInfos.put(dest, dd);

		forAdjacents = new UsynchronizedAdjacentForEacherDijkstra<>(nodeInfos, frontier);

		while (!frontier.isEmpty()) {
			final NodeInfoDikstra<E> n;
			n = frontier.removeMinimum().getKey();
			/*
			 * do not waste time computing nodes that have longer path of the alredy
			 * discovered ones
			 */
			if (dd.father == null || dd.distFromStart > n.distFromStart) {
				forAdjacents.setCurrentNode(n);
				n.thisNode.forEachAdjacents(forAdjacents);
			} else
				/*
				 * destination has got a father and the node with MINIMUM "distance from start"
				 * has a distance greater than the destination itself -> iterating is useless ->
				 * empty the frontier
				 */
				frontier.clear();
			n.color = ColorDijkstra.Black;
		}
		if (dd.father == null)
			return null;
		//
		nodeInfos.clear();
		p = new PathGraph<E>();
//		distanceTotal = dd.distFromStart;
		while (dd != ss) {
			p.addStep(dd.thisNode.getElem(), dd.distFromFather);
			dd = dd.father;
		}
		p.setStartStep(s.getElem());
//		p.setDistanceTotal(distanceTotal);
		return p;
	}

	//

	// subclasses

	protected static class NodeInfoDikstra<E> {
		protected ColorDijkstra color;
		protected Double distFromStart, distFromFather;
		protected GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized thisNode;
		protected NodeInfoDikstra<E> father;

		protected NodeInfoDikstra(GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized thisNode) {
			this.thisNode = thisNode;
			color = ColorDijkstra.White;
			distFromStart = distFromFather = null;
			father = null;
		}
	}

	private static class UsynchronizedAdjacentForEacherDijkstra<E>
			implements BiConsumer<GraphSimple<E>.NodeGraph, Integer> {

		private NodeInfoDikstra<E> currentNode;
		private final Map<E, NodeInfoDikstra<E>> nodeInfos;
		private final PriorityQueueKey<NodeInfoDikstra<E>, Double> frontier;

		UsynchronizedAdjacentForEacherDijkstra(Map<E, NodeInfoDikstra<E>> nodeInfos,
				PriorityQueueKey<NodeInfoDikstra<E>, Double> frontier) {
			this.nodeInfos = nodeInfos;
			this.frontier = frontier;
		}

		public void setCurrentNode(NodeInfoDikstra<E> n) {
			this.currentNode = n;
		}

//		public void accept(GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized nod, Integer distToAdj) {
		@SuppressWarnings("unchecked")
		@Override
		public void accept(GraphSimple<E>.NodeGraph nnn, Integer distToAdj) {
			double distToNo;
			E e;
			GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized no;
			NodeInfoDikstra<E> noInfo;
			no = (GraphSimpleAsynchronized<E>.NodeGraphSimpleAsynchronized) nnn;
			e = no.getElem();
			if (nodeInfos.containsKey(e))
				noInfo = nodeInfos.get(e);
			else
				nodeInfos.put(e, noInfo = new NodeInfoDikstra<E>(no));

			if (noInfo.color == ColorDijkstra.Black)
				return;
			distToNo = distToAdj + currentNode.distFromStart;
			if (noInfo.father == null || distToNo < noInfo.distFromStart) {
				final Double newDistance;
				// update
				newDistance = Double.valueOf(distToNo);
				noInfo.father = currentNode;
				noInfo.distFromFather = Double.valueOf(distToAdj);
				if (noInfo.color == ColorDijkstra.White) {
					// add on queue
					noInfo.color = ColorDijkstra.Grey;
					noInfo.distFromStart = newDistance;
					frontier.put(noInfo);
				} else // it's grey, it's actually in the queue
					frontier.alterKey(noInfo, nodd -> (nodd).distFromStart = newDistance);
			}
		}
	}
}