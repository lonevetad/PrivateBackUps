package games.generic.controlModel.inventory;

import games.generic.controlModel.gameObj.GModalityHolder;

public interface EquipmentsHolder extends GModalityHolder {
	public EquipmentSet getEquipmentSet();

	public void setEquipmentSet(EquipmentSet equips);
}