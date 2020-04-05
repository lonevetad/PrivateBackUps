package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.gObj.AttributesHolder;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * Each {@link EquipmentItem} could modify the {@link AttributesHolder}'s
 * attributes (i.e. {@link CreatureAttributes}) through applying this class's
 * instances by invoking
 * {@link CreatureAttributes#applyAttributeModifier(EquipmentAttributeModifier)}.
 */
public class AttributeModification {

	protected int value;
	protected AttributeIdentifier attributeModified;

	public AttributeModification(AttributeIdentifier attributeModified, int value) {
		super();
		this.attributeModified = attributeModified;
		this.value = value;
	}

	public AttributeIdentifier getAttributeModified() {
		return attributeModified;
	}

	public int getValue() {
		return value;
	}

	public void setAttributeModified(AttributeIdentifier attributeModified) {
		this.attributeModified = attributeModified;
	}

	public void setValue(int value) {
		this.value = value;
	}
}