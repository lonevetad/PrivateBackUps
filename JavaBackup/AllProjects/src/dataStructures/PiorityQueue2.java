package dataStructures;

import java.util.Comparator;
import java.util.function.Function;

/** Not used */
public class PiorityQueue2<Key, ValueHoldingKey> {

	protected PiorityQueue2(Comparator<Key> compKey, Function<ValueHoldingKey, Key> keyExtractor,
			Comparator<ValueHoldingKey> compValueHoldingKey) {
		super();
		this.keyExtractor = keyExtractor;
		this.compKey = compKey;
		this.compValueHoldingKey = compValueHoldingKey;
	}

	private final Function<ValueHoldingKey, Key> keyExtractor;
	private final Comparator<Key> compKey;
	private final Comparator<ValueHoldingKey> compValueHoldingKey;

}