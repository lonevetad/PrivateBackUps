package games.generic.controlModel.heal;

import java.util.Comparator;

import games.generic.controlModel.heal.resExample.ExampleHealingType;
import tools.Comparators;
import tools.ObjectNamedID;
import tools.ObjectWithID;
import tools.UniqueIDProvider;

/**
 * Identifier / marker interface, useful to denote an {@link Enum}'s set as a
 * resource. (like {@link ExampleHealingType}).
 * <p>
 * Disclaimer: to be honest, this interface (and every other ones and classes
 * derived from this or that uses this interface) should have been called
 * <i>"Curable"..</i>, but I kept <i>"Healable"...</i> even if it's an error
 * just to be +coherent to the package's name and the concept it represents (and
 * the word used inside the "game's world"): heal / healing.
 */
public interface IHealableResourceType extends ObjectNamedID {
	public static final Comparator<IHealableResourceType> COMPARATOR_CURABLE_RES_TYPE = (ht1, ht2) -> {
		int c;
		if (ht1 == ht2)
			return 0;
		if (ht1 == null)
			return -1;
		if (ht2 == null)
			return 1;
		c = Comparators.INTEGER_COMPARATOR.compare(ht1.getID(), ht2.getID());
		return (c != 0) ? c : Comparators.STRING_COMPARATOR.compare(ht1.getName(), ht2.getName());
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

	//

	public static IDHolderHealableResourceType newIDHolderHRT() { return new IDHolderHealableResourceType(); }

	public IDHolderHealableResourceType getIDHolderHRT();

	/**
	 * WARNING: You should <b>NOT</b> override this method. It's WITAL.<br>
	 * Otherwise, the comparator could get confuse due to the same number even if
	 * the "healable resources" are different.
	 * <p>
	 * Inherited Doc:<br>
	 * {@inheritDoc}
	 */
	@Override
	public default Integer getID() { return getIDHolderHRT().id; }

	//

	//

	public static class IDHolderHealableResourceType implements ObjectWithID {
		private static final long serialVersionUID = -701240240157L;
		private static final UniqueIDProvider UIDP_HEALABLE_RESOURCE_TYPE = UniqueIDProvider.newBasicIDProvider();
		protected final Integer id;

		public IDHolderHealableResourceType() { this.id = UIDP_HEALABLE_RESOURCE_TYPE.getNewID(); }

		@Override
		public Integer getID() { return id; }
	}
}