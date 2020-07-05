package games.generic.controlModel.misc;

import java.util.Comparator;

import tools.Comparators;
import tools.ObjectNamedID;

public interface CurableResourceType extends ObjectNamedID {
	public static final Comparator<CurableResourceType> COMPARATOR_CURABLE_RES_TYPE = (ht1, ht2) -> {
		if (ht1 == ht2)
			return 0;
		if (ht1 == null)
			return -1;
		if (ht2 == null)
			return 1;
		return Comparators.STRING_COMPARATOR.compare(ht1.getName(), ht2.getName());
	};

	/**
	 * Numeric domain restriction: if <code>true</code>, then this resource accepts
	 * negative values, otherwise it's constrained to accept only zero or more. See
	 * also {@link #acceptsZeroAsMaximum()}.
	 */
	public boolean acceptsNegative();

	/**
	 * Numeric domain restriction: if <code>true</code>, then this resource could
	 * have a zero maximal bound, otherwise every settings to a maximal amount is
	 * forced to be at least one (or <code>-1</code> if the value to be set is
	 * negative and {@link #acceptsNegative()} is <code>true</code>).
	 */
	public boolean acceptsZeroAsMaximum();
}