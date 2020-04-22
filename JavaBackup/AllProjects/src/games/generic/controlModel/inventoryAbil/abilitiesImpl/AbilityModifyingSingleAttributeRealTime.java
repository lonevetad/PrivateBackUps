package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.misc.AttributeIdentifier;

/** This class is originally designed as */
public abstract class AbilityModifyingSingleAttributeRealTime extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = 561320350155L;

	public AbilityModifyingSingleAttributeRealTime() {
		super();
	}

	public AbilityModifyingSingleAttributeRealTime(AttributeIdentifier attributeModified, String name) {
		super(new AttributeIdentifier[] { attributeModified }, name);
	}
}