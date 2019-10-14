package games.theRisingAngel.equipment;

import java.io.Serializable;
import java.util.function.BiConsumer;

import common.mainTools.Comparators;
import games.theRisingAngel.main.StatisticheTRAn;
import tools.RedBlackTree;

public abstract class AbstractEquipment implements Serializable {
	private static final long serialVersionUID = 89150190541L;

	public AbstractEquipment() {
		upgrades = null;// avoid wasting space
	}

	protected RedBlackTree<Integer, EquipmentUpgrade> upgrades;

	//

	public RedBlackTree<Integer, EquipmentUpgrade> getUpgrades() {
		return upgrades;
	}

	//
	/*
	 * public AbstractEquipment setUpgrades(RedBlackTree<Integer, EquipmentUpgrades> upgrades) {
	 * this.upgrades = upgrades; return this; }
	 */

	//

	public AbstractEquipment addUpgrade(EquipmentUpgrade upgrade) {
		if (upgrade != null) {
			if (upgrades == null) upgrades = new RedBlackTree<>(Comparators.INTEGER_COMPARATOR);
			upgrades.add(upgrade.getIdUpgrade(), upgrade);
		}
		return this;
	}

	//

	public AbstractEquipment applyEquipmentsEffects(StatisticheTRAn s) {
		if (upgrades != null && upgrades.size() > 0) {
			upgrades.forEach
			// doForEachEquipUpgrades//
			((id, e) -> {
				if (e != null) e.applyEffect(s);
			});
		}
		return this;
	}

	public AbstractEquipment removeEquipmentsEffects(StatisticheTRAn s) {
		if (upgrades != null && upgrades.size() > 0) {
			upgrades.forEach
			// doForEachEquipUpgrades//
			((id, e) -> {
				if (e != null) e.removeEffect(s);
			});
		}
		return this;
	}

	protected void doForEachEquipUpgrades(BiConsumer<Integer, EquipmentUpgrade> toDo) {
		if (upgrades != null && upgrades.size() > 0) {
			upgrades.forEach(toDo);
		}
	}
}