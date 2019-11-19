package dataStructures.graph;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.BiFunction;

public interface NodeDistanceManager<T> extends Serializable {
	public Comparator<T> getComparator();

	public BiFunction<T, T, T> getAdder();

	/** Returns a constant-like value representing the "zero" for accumulators. */
	public T getZeroValue();
}