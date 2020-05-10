package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.misc.AttributeIdentifier;

/** This class is originally designed as */
public abstract class AbilityModifyingSingleAttributeRealTime extends AbilityModifyingAttributesRealTime {
	private static final long serialVersionUID = 561320350155L;

	public AbilityModifyingSingleAttributeRealTime() {
		super();
	}

	public AbilityModifyingSingleAttributeRealTime(String name, AttributeIdentifier attributeModified) {
		super(name, new AttributeIdentifier[] { attributeModified });
	}
}