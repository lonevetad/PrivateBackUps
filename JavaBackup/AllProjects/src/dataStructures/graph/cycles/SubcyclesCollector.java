package dataStructures.graph.cycles;

import java.io.Serializable;
import java.util.List;

public interface SubcyclesCollector<E, Distance> extends Serializable {
	public static final boolean PRESERVE_SOURCE_GRAPH = true, MODIFY_SOURCE_GRAPH = false;

	public default List<GraphSimple<E, Distance>> collectCycles(GraphSimple<E, Distance> gsource,
			GraphSimpleGenerator<E, Distance> gsg) {
		return collectCycles(PRESERVE_SOURCE_GRAPH, gsource, gsg);
	}

	/**
	 * NOTE: if some big sub-cycle in the original graph contains an internal
	 * sub-cycle, then the shorter "side" of this last one's "sides" is picked and
	 * if two or more "sides" have the same length then one is picked in a
	 * unpredictable way, because it depends on the node's adjacents' order.
	 */
	public List<GraphSimple<E, Distance>> collectCycles(boolean keepSource, GraphSimple<E, Distance> gsource,
			GraphSimpleGenerator<E, Distance> gsg);

}