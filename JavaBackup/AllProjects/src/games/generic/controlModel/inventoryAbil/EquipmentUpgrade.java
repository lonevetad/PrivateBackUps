package games.generic.controlModel.inventoryAbil;

import java.util.function.Function;

/**
 * Simple extension to mark an attribute upgrade designed for
 * {@link EquipmentItem}s.
 */
public interface EquipmentUpgrade extends AttributesUpgrade {
	public static final Function<EquipmentUpgrade, String> KEY_EXTRACTOR = eu -> eu.getName();

	public EquipmentItem getEquipmentAssigned();

	public void setEquipmentAssigned(EquipmentItem equipmentAssigned);
}