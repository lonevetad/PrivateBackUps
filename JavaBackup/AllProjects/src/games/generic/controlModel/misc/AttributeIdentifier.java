package games.generic.controlModel.misc;

/** Comfortable for defining set of attributes using enumerations. */
public interface AttributeIdentifier extends IndexableObject {

	/**
	 * Tells if the current attribute can or cannot be negative. If
	 * <code>true</code>, then it cannot be negative.
	 */
	public default boolean isStrictlyPositive() {
		return false;
	}
}