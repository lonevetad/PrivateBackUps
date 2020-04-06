package geometry;

import java.awt.geom.Point2D;
import java.util.List;

import dataStructures.graph.NodeDistanceManager;

public interface PathFinder<NodeType extends ObjectLocated, D extends Number> {
	public List<Point2D> getPath(NodeType start, NodeType dest, NodeDistanceManager<D> distanceManager);

	public List<Point2D> getPath(ObjectShaped objPlanningToMove, NodeType dest, NodeDistanceManager<D> distanceManager);
}