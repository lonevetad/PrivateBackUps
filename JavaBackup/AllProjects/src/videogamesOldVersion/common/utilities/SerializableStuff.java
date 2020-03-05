package common.utilities;

import java.io.Serializable;

public interface SerializableStuff extends Serializable {
	public void doAfterUnserialization();
}
