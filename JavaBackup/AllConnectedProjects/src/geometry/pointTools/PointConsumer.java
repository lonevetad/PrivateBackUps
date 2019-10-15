package geometry.pointTools;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.function.Consumer;

public interface PointConsumer extends Consumer<Point2D>, Serializable {
	public static boolean FORCE_EARLY_STOPPING = true;
	public static final PointConsumer POINT_PRINTER = p -> System.out.println(p);

	/**
	 * Specify if the consumer can continue consuming points (allowing the cycle to
	 * be stopped earlier).)
	 */
	public default boolean canContinue() {
		return true;
	}
}