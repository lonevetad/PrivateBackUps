package dataStructures.graph;

import java.io.Serializable;
import java.util.Map;

public interface SubcyclesCollector extends Serializable {
	public static final boolean PRESERVE_SOURCE_GRAPH = true, MODIFY_SOURCE_GRAPH = false;

	public default <E, Distance> Map<Integer, GraphSimple<E, Distance>> collectCycles(GraphSimpleGenerator gsg,
			GraphSimple<E, Distance> gsource) {
		return collectCycles(PRESERVE_SOURCE_GRAPH, gsg, gsource);
	}

	/**
	 * NOTE: if some big sub-cycle in the original graph contains an internal
	 * sub-cycle, then the shorter "side" of this last one's "sides" is picked and
	 * if two or more "sides" have the same length then one is picked in a
	 * unpredictable way, because it depends on the node's adjacents' order.
	 */
	public <E, Distance> Map<Integer, GraphSimple<E, Distance>> collectCycles(boolean keepSource,
			GraphSimpleGenerator gsg, GraphSimple<E, Distance> gsource);

}