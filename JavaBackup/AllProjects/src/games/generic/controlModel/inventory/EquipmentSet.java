package games.generic.controlModel.inventory;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.player.PlayerInGame_Generic;

/**
 * Set of {@link EquipmentItem} equipped to a creature (like a the player:
 * {@link PlayerInGame_Generic}.
 */
public abstract class EquipmentSet {

	protected CreatureOfRPGs creatureWearingEquipments;

	public EquipmentSet() {
	}

//	public abstract EquipmentsHolder getEquipmentsHolder();
//	public abstract void setEquipmentsHolder(EquipmentsHolder equipmentsHolder);

	public CreatureOfRPGs getCreatureWearingEquipments() {
		return this.creatureWearingEquipments;
	}

	public void setCreatureWearingEquipments(CreatureOfRPGs creatureWearingEquipments) {
		this.creatureWearingEquipments = creatureWearingEquipments;
	}

	//

	/**
	 * Get the set of {@link EquipmentItem}.<br>
	 * NOTE: unused slots (i.e. "places" where there is no equipped objects for that
	 * type) are <code>null</code>.
	 */
	public abstract EquipmentItem[] getEquippedItems();

	/**
	 * Equip the given item into the set NOTE: Should check if is yet equipped an
	 * intem in that slot
	 */
	public abstract void addEquipmentItem(GModality gm, EquipmentItem ei);

	/**
	 * TODO use player's inventory and
	 * {@link EquipmentItem#getLocationInInventory()} (of the second parameter, if
	 * not <code>null</code>).
	 */
	public abstract void swapEquipmentItem(GModality gm, EquipmentItem newEI, EquipmentItem oldEI);
}