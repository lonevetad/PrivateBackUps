package dataStructures.graph;

import java.util.Comparator;

public interface GraphSimpleGenerator {
	public <E, D> GraphSimple<E, D> newGraph(boolean isDirected, PathFindStrategy<E, D> pathFinder,
			Comparator<E> comparatorElements);
}
