package dataStructures.isom;

import dataStructures.graph.PathFindStrategy;
import geometry.PathFinder;

/**
 * Implementing the Adapter Pattern, to make {@link PathFindStrategy}
 * implementations useful for {@link PathFinder} purposes.
 */
public interface PathFinderIsomAdapter<D extends Number> extends//
		PathFinder<NodeIsom, D>, PathFindStrategy<NodeIsom, D> {

	public InSpaceObjectsManager<D> getISOM();

	// TODO implement adaptation
}