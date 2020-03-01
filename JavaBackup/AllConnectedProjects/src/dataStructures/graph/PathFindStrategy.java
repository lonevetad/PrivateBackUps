package dataStructures.graph;

import java.io.Serializable;

public interface PathFindStrategy<E, Distance> extends Serializable {

	public abstract PathGraph<E, Distance> getPath(GraphSimple<E, Distance> graph, E start, E dest,
			NodeDistanceManager<Distance> distanceManager);
}