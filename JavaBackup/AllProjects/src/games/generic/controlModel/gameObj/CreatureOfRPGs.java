package games.generic.controlModel.gameObj;

import games.generic.controlModel.inventory.EquipmentItem;
import games.generic.controlModel.inventory.EquipmentSet;
import games.generic.controlModel.inventory.EquipmentsHolder;

/**
 * Typical creature of Rule Play Games: one having a set of attributes and some
 * optional equipments (that could be dropped).
 */
public interface CreatureOfRPGs extends EquipmentsHolder, CreatureSimple {
	public default void equip(EquipmentItem equipment) {
		EquipmentSet es;
		es = this.getEquipmentSet();
		if (es != null) {
			es.addEquipmentItem(getGameModality(), equipment);
		}
	}
}