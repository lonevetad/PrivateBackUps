package dataStructures.graph;

import java.io.Serializable;

public interface PathFindStrategy<E> extends Serializable {

	public abstract PathGraph<E> getPath(GraphSimple<E> graph, E start, E dest);

}