package geometry;

import java.util.List;

import dataStructures.graph.NodeDistanceManager;

public interface PathFinder<NodeType extends ObjectLocated, D extends Number> {
	public List<NodeType> getPath(NodeType start, NodeType dest, NodeDistanceManager<D> distanceManager);

	public List<NodeType> getPath(ObjectShaped objPlanningToMove, NodeType dest,
			NodeDistanceManager<D> distanceManager);
}