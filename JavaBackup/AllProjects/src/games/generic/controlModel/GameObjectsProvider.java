package games.generic.controlModel;

import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.CreatureProvider;
import games.generic.controlModel.inventoryAbil.EquipItemProvider;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.InventoryItem;
import games.generic.controlModel.inventoryAbil.InventoryItemNotEquippable;
import games.generic.controlModel.misc.ObjGModalityBasedProvider;

/**
 * Huge class acting as a "database for instantiation" collecting a series of useful game objects. In particular, it
 * holds ("provides" through {@link ObjGModalityBasedProvider}) the list below
 * of superclasses and manages them, like calculating the drops and spawning
 * inside the game.
 * <ul>
 * <li>{@link InventoryItem} (More precisely, {@link EquipmentItem} and
 * {@link InventoryItemNotEquippable}).</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link CreatureSimple}</li>
 * </ul>
 * Manages: dropping items (for instance, upon killing an enemy or opening a
 * chest), defining abilities, etc.
 * <p>
 * Useful classes/interfaces used here:
 * <ul>
 * <li>{@link EquipmentItem}</li>
 * <li>{@link EquipItemProvider}</li>
 * <li>{@link AbilityGeneric}</li>
 * <li>{@link AbilitiesProvider}</li>
 * <li>{@link CreatureSimple}</li>
 * <li>{@link CreatureProvider}</li>
 * </ul>
 */
public abstract class GameObjectsProvider {

	public GameObjectsProvider() {
		this.abilitiesProvider = new AbilitiesProvider();
	}

	protected AbilitiesProvider abilitiesProvider;
	protected EquipItemProvider equipmentsProvider;
	protected CreatureProvider creaturesProvider;

	//

	public AbilitiesProvider getAbilitiesProvider() {
		return abilitiesProvider;
	}

	public EquipItemProvider getEquipmentsProvider() {
		return equipmentsProvider;
	}

	public CreatureProvider getCreaturesProvider() {
		return creaturesProvider;
	}

//

	public void setAbilitiesProvider(AbilitiesProvider ap) {
		this.abilitiesProvider = ap;
	}

	public void setEquipmentsProvider(EquipItemProvider equipmentsProvider) {
		this.equipmentsProvider = equipmentsProvider;
	}

	public void setCreaturesProvider(CreatureProvider creaturesProvider) {
		this.creaturesProvider = creaturesProvider;
	}

}