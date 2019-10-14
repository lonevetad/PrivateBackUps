package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

/**
 * Perform an action over the first argument using the second (optional) argument.<br>
 * Returns a {@link String} in case of error describing it, {@code null} otherwise.
 */
@Deprecated
public interface DoSomethingWithSomeoneElse extends Serializable {

	/** See {@link DoSomethingWithSomeoneElse}. */
	public default String doSomethingWithSomeoneElse(Object from) {
		return doSomethingWithSomeoneElse(from, null);
	}

	/** See {@link DoSomethingWithSomeoneElse}. */
	public String doSomethingWithSomeoneElse(Object from, Object with);
}