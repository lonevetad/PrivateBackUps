package games.generic.controlModel.inventory;

import games.generic.controlModel.gameObj.AttributesHolder;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * Each {@link EquipmentItem} could modify the {@link AttributesHolder}'s
 * attributes (i.e. {@link CreatureAttributes})
 */
public class EquipmentAttributeModifier {
	protected int value;

	protected AttributeIdentifier attributeModified;

	public EquipmentAttributeModifier(AttributeIdentifier attributeModified, int value) {
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