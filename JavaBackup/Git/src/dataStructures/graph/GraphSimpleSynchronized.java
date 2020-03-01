package dataStructures.graph;

import java.util.Comparator;
import java.util.function.BiConsumer;

public class GraphSimpleSynchronized<E> extends GraphSimple<E> {

	protected int pathFindRuns;

	//

	public GraphSimpleSynchronized(boolean isDirected, PathFindStrategy<E> pathFinder,
			Comparator<E> comparatorElements) {
		super(isDirected, pathFinder, comparatorElements);
		this.pathFindRuns = 0;
	}

	public GraphSimpleSynchronized(PathFindStrategy<E> pathFinder, Comparator<E> comparatorElements) {
		super(pathFinder, comparatorElements);
		this.pathFindRuns = 0;
	}

	@Override
	public final boolean isSynchronized() {
		return true;
	}

	@Override
	protected NodeGraph newNodeGraph(E e) {
		return new NodeGraphSimpleSynchronized(e);
	}

	public int getPathFindRuns() {
		return pathFindRuns;
	}

	public GraphSimpleSynchronized<E> setPathFindRuns(int pathFindRuns) {
		this.pathFindRuns = pathFindRuns;
		return this;
	}

	//

	@Override
	public String toString() {
		StringBuilder sb;
		BiConsumer<NodeGraph, Integer> adjPrinter;
		sb = new StringBuilder(1024);
		sb.append("Graph ").append(isDirected ? "directed" : "undirected").append(" having ").append(this.linksAmount)
				.append(" links and this nodes:\n");

		adjPrinter = (n, dist) -> sb.append("\n-\t").append(", dist: ").append(dist).append(" -- node: ")
				.append(n.toString());
		this.nodes.forEach((e, n) -> {
			sb.append("\n+ ").append(e).append(" ->");
			n.forEachAdjacents(adjPrinter);
		});

		return sb.toString();
	}

	//

	// subclasses

	public class NodeGraphSimpleSynchronized extends NodeGraph {
		// for dijkstra
		protected int pathFIndierIdRuns;
		protected ColorDijkstra color;
		private Integer distFromStart;
		private Integer distFromFather;
		private NodeGraphSimpleSynchronized father;

		protected NodeGraphSimpleSynchronized(E e) {
			super(e);
			pathFIndierIdRuns = 0;
			checkAndReset(-1);
		}

		public void checkAndReset(int dr) {
			if (dr != pathFIndierIdRuns) {
				pathFIndierIdRuns = dr;
				color = ColorDijkstra.White;
				distFromStart = distFromFather = null;
				father = null;
			}
		}

		@Override
		public String toString() {
			StringBuilder sb;
			sb = new StringBuilder(127);
			return sb.append(super.toString()).append(", color: ").append(this.color).append(", dist-start: ")
					.append(this.getDistFromStart()).append(", father: ")
					.append(this.getFather() == null ? "null" : String.valueOf(this.getFather().getElem())).toString();
		}

		public int getDijkstraRuns() {
			return pathFIndierIdRuns;
		}

		public ColorDijkstra getColor() {
			return color;
		}

		public Integer getDistFromStart() {
			return distFromStart;
		}

		public Integer getDistFromFather() {
			return distFromFather;
		}

		public NodeGraphSimpleSynchronized getFather() {
			return father;
		}

		public NodeGraphSimpleSynchronized setDijkstraRuns(int dijkstraRuns) {
			this.pathFIndierIdRuns = dijkstraRuns;
			return this;
		}

		public NodeGraphSimpleSynchronized setColor(ColorDijkstra color) {
			this.color = color;
			return this;
		}

		public NodeGraphSimpleSynchronized setDistFromStart(Integer distFromStart) {
			this.distFromStart = distFromStart;
			return this;
		}

		public NodeGraphSimpleSynchronized setDistFromFather(Integer distFromFather) {
			this.distFromFather = distFromFather;
			return this;
		}

		public NodeGraphSimpleSynchronized setFather(NodeGraphSimpleSynchronized father) {
			this.father = father;
			return this;
		}
	}
}
