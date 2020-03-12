package dataStructures.graph;

import java.util.Comparator;

public interface GraphSimpleGenerator<E, D> {
	public GraphSimple<E, D> newGraph(boolean isDirected, PathFindStrategy<E, D> pathFinder,
			Comparator<E> comparatorElements);
}