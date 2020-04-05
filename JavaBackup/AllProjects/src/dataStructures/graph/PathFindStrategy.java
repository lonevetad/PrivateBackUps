package dataStructures.graph;

import java.io.Serializable;

public interface PathFindStrategy<NodeType, Distance> extends Serializable {
	public abstract PathGraph<NodeType, Distance> getPath(GraphSimple<NodeType, Distance> graph, NodeType start, NodeType dest,
			NodeDistanceManager<Distance> distanceManager);
}