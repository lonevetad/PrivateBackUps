package common.abstractCommon;

import java.io.Serializable;
import java.util.stream.Stream;

public interface Streamable<T> extends Serializable {
	public Stream<T> stream();
}