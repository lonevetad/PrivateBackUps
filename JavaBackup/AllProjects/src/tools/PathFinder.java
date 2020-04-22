package tools;

import java.util.List;
import java.util.function.Predicate;

import geometry.AbstractShape2D;
import geometry.ObjectLocated;
import geometry.ObjectShaped;

/**
 * Defines an algorithm for finding the shortest path from one starting location
 * to a destination. <br>
 * Three are the generic parameter types:
 * <ol>
 * <li>{@code NodeType}: the type of the graph's node to look path into. It's
 * mandatory.</li>
 * <li>{@code NodeContent}: relevant information of the node, its content.
 * Mandatory, could be the same as {@code NodeType}. Useful to filter nodes and
 * distinguish them from "walkable" and "wall" through {@link Predicate}. .</li>
 * <li>{@code Distance} the misure</li>
 * </ol>
 */
public interface PathFinder<NodeType, NodeContent, Distance extends Number> { // NodeContent extends Point2D not needed

	public static enum NodePositionInFrontier {
		NeverAdded, InFrontier, Closed;
	}

	/**
	 * Get the shortest path from one source to a destination (First two
	 * parameters), managing distances types through the third parameter and
	 * providing a way to recognize when a node is a "free, walkable node" or a
	 * "wall" (using the fourth parameter: a {@link Predicate}).
	 * 
	 * @param isWalkableTester predicate that tests the current node and returns
	 *                         <code>true</code> if it could be walked.
	 */
	public List<NodeType> getPath(NodeType start, NodeType dest, NumberManager<Distance> distanceManager,
			Predicate<NodeContent> isWalkableTester);

	/**
	 * Calls {@link #getPath(Object, Object, NumberManager, Predicate)} passing
	 * <code>null</code> to {@link Predicate}.
	 */
	public default List<NodeType> getPath(NodeType start, NodeType dest, NumberManager<Distance> distanceManager) {
		return getPath(start, dest, distanceManager, null);
	}

	/**
	 * As {@link #getPath(ObjectLocated, ObjectLocated, NumberManager, Predicate)},
	 * but the starting point depends on the first parameter's location, who has
	 * also a {@link AbstractShape2D} AND this implementation must take into account
	 * that shape, checking if the shape can "walk" over the specified space.
	 * 
	 * @param isWalkableTester predicate that tests the current node and returns
	 *                         <code>true</code> if it could be walked.
	 */
	public List<NodeType> getPath(ObjectShaped objPlanningToMove, NodeType dest,
			NumberManager<Distance> distanceManager, Predicate<NodeContent> isWalkableTester);

	/**
	 * Calls {@link #getPath(ObjectShaped, Object, NumberManager, Predicate)}
	 * passing <code>null</code> to {@link Predicate}.
	 */
	public default List<NodeType> getPath(ObjectShaped objPlanningToMove, NodeType dest,
			NumberManager<Distance> distanceManager) {
		return getPath(objPlanningToMove, dest, distanceManager, null);
	}
}