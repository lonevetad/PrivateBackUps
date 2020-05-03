package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.GModalityHolder;

public interface EquipmentsHolder extends GModalityHolder {
	public EquipmentSet getEquipmentSet();

	/**
	 * Remember to call
	 * {@link EquipmentSet#setCreatureWearingEquipments(BaseCreatureRPG)}
	 */
	public void setEquipmentSet(EquipmentSet equips);

	//

	public EquipmentSet newEquipmentSet();

	//

	public default void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) {
			es.addEquipmentItem(getGameModality(), equipment);
		}
	}
}