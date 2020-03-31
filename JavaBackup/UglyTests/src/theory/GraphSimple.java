package theory;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.function.BiConsumer;

public class GraphSimple<E> {

	static enum ColorDijkstra {
		WHITE, GREY, BLACK;
	}

	static final double INFINITY = Double.POSITIVE_INFINITY;
	static final Comparator<Long> LONG_COMPARATOR = Long::compare;

	//

	public GraphSimple() {
		this(false);
	}

	public GraphSimple(boolean oriented) {
		this.oriented = oriented;
		idNextNode = 0;
		nodes = new TreeMap<>(LONG_COMPARATOR);
	}

	final boolean oriented;
	protected long idNextNode;
	TreeMap<Long, NodeGraph> nodes;
	protected final Comparator<InfoNodeGraph> weightComparator = (n1, n2) -> Double.compare(n1.weightSource,
			n2.weightSource);

	//

	//

	public Long addNode(E item) {
		NodeGraph n;
		n = new NodeGraph(item);
		nodes.put(n.idint, n);
		return n.idint;
	}

	public void addEdge(Long source, Long dest, double weight) throws IllegalArgumentException {
		NodeGraph s, d;
		if (weight <= 0.0) throw new IllegalArgumentException("Incorrect weight: " + weight);
		if (source == null) throw new IllegalArgumentException("Source ID null");
		if (dest == null) throw new IllegalArgumentException("Destination ID null");
		s = nodes.get(source);
		if (s == null) throw new IllegalArgumentException("Source null");
		d = nodes.get(dest);
		if (d == null) throw new IllegalArgumentException("Destination null");
		addEdge(s, d, weight);
	}

	public void addEdge(NodeGraph source, NodeGraph dest, double weight) throws IllegalArgumentException {
		if (weight <= 0.0) throw new IllegalArgumentException("Incorrect weight: " + weight);
		if (source == null) throw new IllegalArgumentException("Source null");
		if (dest == null) throw new IllegalArgumentException("Destination null");
		source.addEdge(dest, weight);
		if (!oriented) dest.addEdge(source, weight);
	}

	public List<E> shortestPath(Long source, Long dest) throws IllegalArgumentException {
		// List<E>
		NodeGraph s, d;
		if (source == null) throw new IllegalArgumentException("Source ID null");
		if (dest == null) throw new IllegalArgumentException("Destination ID null");
		s = nodes.get(source);
		if (s == null) throw new IllegalArgumentException("Source null");
		d = nodes.get(dest);
		if (d == null) throw new IllegalArgumentException("Destination null");
		return shortestPath(s, d);
	}

	/** Thread-safe, but a bit slow */
	public List<E> shortestPath(NodeGraph source, NodeGraph dest) throws IllegalArgumentException {
		List<E> l;
		// NodeGraph n;
		PriorityQueue<InfoNodeGraph> q;
		TreeMap<Long, InfoNodeGraph> visited;
		InfoNodeGraph infoDest, in;
		NodeGraph n;
		BiConsumer<? super Long, ? super ArcGraph> forEdges;
		if (source == null) throw new IllegalArgumentException("Source null");
		if (dest == null) throw new IllegalArgumentException("Destination null");
		l = new LinkedList<>();
		q = new PriorityQueue<>(weightComparator);
		visited = new TreeMap<>(LONG_COMPARATOR);
		infoDest = new InfoNodeGraph(dest);
		visited.put(dest.idint, infoDest);
		q.add(new InfoNodeGraph(source));

		forEdges = (id, e) -> {double sum;
			InfoNodeGraph idest;
			NodeGraph dd;
			idest = visited.get((dd = e.dest).id);
			if (idest == null)// never visited
				idest = new InfoNodeGraph(dd);
			if (idest.color != ColorDijkstra.BLACK) {
				
				sum=e.weight+
				if( ){
					
				}
				if (idest.color == ColorDijkstra.WHITE) {

					idest.color = ColorDijkstra.GREY;
				}
			}

		};

		while (infoDest.father == null && (!q.isEmpty())) {
			in = q.poll();
			in.color = ColorDijkstra.GREY;
			n = in.node;
			n.edges.forEach(forEdges);
		}

		return l;
	}

	//

	// TODO CLASSES

	protected class NodeGraph {

		NodeGraph(E e) {
			idint = Long.valueOf(id = idNextNode++);
			item = e;
			edges = new TreeMap<>(LONG_COMPARATOR);
		}

		final long id;
		final Long idint;
		final E item;
		final TreeMap<Long, ArcGraph> edges;

		void addEdge(NodeGraph dest, double w) {
			if (!edges.containsKey(dest.idint)) edges.put(dest.idint, new ArcGraph(this, dest, w));
		}
	}

	protected class ArcGraph {
		double weight;
		NodeGraph dest, source;

		public ArcGraph(NodeGraph source, NodeGraph dest, double weight) {
			super();
			this.dest = dest;
			this.source = source;
			this.weight = weight;
		}
	}

	protected class InfoNodeGraph {
		ColorDijkstra color;
		double weightSource;
		NodeGraph node, father;

		InfoNodeGraph(NodeGraph node) {
			this.node = node;
			color = ColorDijkstra.WHITE;
			weightSource = INFINITY;
			father = null;
		}
	}

	protected class EdgesIterartorDijikstra implements BiConsumer<Long, ArcGraph> {
		PriorityQueue<InfoNodeGraph> q;
		TreeMap<Long, InfoNodeGraph> visited;
		InfoNodeGraph infoDest, in, infoSourceEdge;
		NodeGraph sourceEdge;

		@Override
		public void accept(Long id, GraphSimple<E>.ArcGraph e) {
			boolean mustAdd;
			double sum;
			InfoNodeGraph idest;
			NodeGraph dd;
			mustAdd = false;
			idest = visited.get((dd = e.dest).id);
			if (idest == null)// never visited
				idest = new InfoNodeGraph(dd);
			if (idest.color != ColorDijkstra.BLACK) {

				sum = e.weight + infoSourceEdge.weightSource;
				if (idest.father == null || sum < idest.weightSource) {
					idest.weightSource = sum;
					idest.father = infoSourceEdge.node;
					mustAdd = true;
				}
				if (idest.color == ColorDijkstra.WHITE) {
					mustAdd = true;
					idest.color = ColorDijkstra.GREY;
				}
				if (mustAdd) {
					q.remove(idest);
				}
			}
		}

	}

	//

	//

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("asd: " + Double.compare(7, Double.POSITIVE_INFINITY));
		System.out.println("asd: " + Double.compare(Double.POSITIVE_INFINITY, 7));
	}

}