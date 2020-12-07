package games.generic.controlModel.inventoryAbil;

import java.util.Comparator;
import java.util.function.Function;

import games.generic.controlModel.misc.AmountNamed;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.Comparators;

/**
 * Each {@link EquipmentItem} could modify the {@link AttributesHolder}'s
 * attributes (i.e. {@link CreatureAttributes}) through applying this class's
 * instances by invoking
 * {@link CreatureAttributes#applyAttributeModifier(EquipmentAttributeModifier)}.
 */
public class AttributeModification extends AmountNamed {
	private static final long serialVersionUID = -88782140147L;
	public static final Function<AttributeModification, String> KEY_EXTRACTOR = eu -> eu.getAttributeModified()
			.getName();
	public static final Comparator<AttributeModification> COMPARATOR = (am1, am2) -> {
		if (am1 == am2)
			return 0;
		if (am1 == null)
			return -1;
		if (am2 == null)
			return 1;
		return Comparators.STRING_COMPARATOR.compare(KEY_EXTRACTOR.apply(am1), KEY_EXTRACTOR.apply(am2));
	};

	public static AttributeModification newEmpty(AttributeIdentifier attributeModified) {
		return new AttributeModification(attributeModified, 0);
	}

	public static AttributeModification[] newEmptyArray(AttributeIdentifier[] attributesModified) {
		int n;
		AttributeModification[] r;
		r = new AttributeModification[n = attributesModified.length];
		while (--n >= 0) {
			r[n] = newEmpty(attributesModified[n]);
		}
		return r;
	}

//	protected AttributeIdentifier attributeModified;

	public AttributeModification(AttributeIdentifier attributeModified, int value) {
		super(attributeModified, value);
//		this.attributeModified = attributeModified;
	}

	public AttributeIdentifier getAttributeModified() { return (AttributeIdentifier) super.type; }

	@Override
	public int getValue() { return value; }

	public void setAttributeModified(AttributeIdentifier attributeModified) {
//		this.attributeModified = attributeModified;
		super.type = attributeModified;
	}

	@Override
	public void setValue(int value) { this.value = value; }

	@Override
	public String toString() { return "AttributeModification [attr=" + getAttributeModified() + " : v=" + value + "]"; }
}