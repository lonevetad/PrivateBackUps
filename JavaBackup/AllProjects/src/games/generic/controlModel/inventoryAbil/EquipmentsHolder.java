package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.gObj.GModalityHolder;

public interface EquipmentsHolder extends GModalityHolder {
	public EquipmentSet getEquipmentSet();

	public void setEquipmentSet(EquipmentSet equips);
}