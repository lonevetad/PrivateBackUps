package dataStructures.graph;

import java.util.Comparator;

public interface MinSpanningTreeExtractor<E, Distance> {
	public GraphSimple<E, Distance> extractMinSpanningTree(GraphSimple<E, Distance> originalGraph,
			Comparator<Distance> distanceComp);
}
