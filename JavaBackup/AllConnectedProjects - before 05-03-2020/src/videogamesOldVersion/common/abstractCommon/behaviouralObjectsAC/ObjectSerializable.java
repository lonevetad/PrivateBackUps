package common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

/** Do stuffs with this instance using the given optional argument. */
public interface ObjectSerializable extends Serializable {

	/** See é{@link #doAfterDeserialization(Object)}, passing {@code null}. */
	public default String doAfterDeserialization() {
		return doAfterDeserialization(null);
	}

	/**
	 * See
	 * é{@link #doAfterDeserialization(Object, DoSomethingWithSomeoneElse)},
	 * passing {@code null} as first parameter.
	 */
	// public default String doAfterDeserialization(DoSomethingWithSomeoneElse
	// doSWSE) {
	// return this.doAfterDeserialization(null, doSWSE);
	// }

	/**
	 * See {@link ObjectSerializable}.<br>
	 * Links the default action described by this implementation with another
	 * action, that is the second parameter and is performed after this ones
	 */
	/**
	 * public default String doAfterDeserialization(Object arguments,
	 * DoSomethingWithSomeoneElse doSWSE) { //<br>
	 * String errorDelegated, myError; //<br>
	 * if (doSWSE == null) //<br>
	 * return "ERROR: on ObjectSerializable.doAfterDeserialization, nothing to
	 * be performed because it's null."; //<br>
	 * errorDelegated = doAfterDeserialization(arguments); //<br>
	 * myError = doSWSE.doSomethingWithSomeoneElse(this, arguments); //<br>
	 * if (errorDelegated != null) { //<br>
	 * if (myError == null) //<br>
	 * myError = errorDelegated; //<br>
	 * else //<br>
	 * myError = errorDelegated //<br>
	 * + "\nANOTHER ERROR after doAfterDeserialization, returned by the
	 * delegation parameter:\n" //<br>
	 * + myError; //<br>
	 * errorDelegated = null; //<br>
	 * } return myError; //<br>
	 * }
	 */

	/** See {@link ObjectSerializable}. */
	public String doAfterDeserialization(Object arguments);

}