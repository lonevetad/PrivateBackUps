package dataStructures.isom;

import java.awt.Point;
import java.io.Serializable;
import java.util.function.Consumer;

public interface NodeIsomProvider extends Serializable {

	/**
	 * Could seems odd, but it's useful for {@link PathFinderIsom}s'
	 * implementations.
	 */
	public NodeIsom getNodeAt(Point location);

	/** See {@link #getNodeAt(Point)}. */
	public NodeIsom getNodeAt(int x, int y);

	public void forEachNode(Consumer<NodeIsom> action);

	public default void clearAllNodes() { forEachNode(n -> n.removeAllObjects()); }
}