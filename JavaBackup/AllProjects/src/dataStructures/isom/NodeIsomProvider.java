package dataStructures.isom;

import java.awt.Point;
import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import geometry.AbstractShape2D;

public interface NodeIsomProvider<Distance extends Number> extends Serializable {

	/**
	 * Could seems odd, but it's useful for {@link PathFinderIsom}s'
	 * implementations.
	 */
	public NodeIsom<Distance> getNodeAt(Point location);

	/** See {@link #getNodeAt(Point)}. */
	public NodeIsom<Distance> getNodeAt(int x, int y);

	public void forEachNode(Consumer<NodeIsom<Distance>> action);

	public default void clearAllNodes() { forEachNode(n -> n.removeAllObjects()); }

	/**
	 * Perform an action to each adjacent of a given node. That action should take
	 * into account not only the adjacent node, but also the distance from the given
	 * node and the specific adjacent.
	 */
	public abstract void forEachAdjacents(NodeIsom<Distance> node,
			BiConsumer<NodeIsom<Distance>, Distance> adjacentDistanceConsumer);

	public void runOnShape(AbstractShape2D shape, NodeIsomConsumer<Distance> action);
}