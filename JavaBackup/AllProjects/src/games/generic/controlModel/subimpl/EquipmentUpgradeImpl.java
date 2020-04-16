package games.generic.controlModel.subimpl;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.misc.RarityHolder;
import tools.Comparators;

public class EquipmentUpgradeImpl implements EquipmentUpgrade {

	public EquipmentUpgradeImpl(int rarityIndex, String name) {
		super();
		MapTreeAVL<String, AttributeModification> m;
		this.rarityIndex = rarityIndex;
		this.name = name;
		m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.STRING_COMPARATOR);
		this.attributeModifiers = m.toSetValue(AttributeModification.KEY_EXTRACTOR);
	}

	protected int rarityIndex;
	protected String name;
	protected EquipmentItem equipmentAssigned;
	protected final Set<AttributeModification> attributeModifiers;

	@Override
	public Set<AttributeModification> getAttributeModifiers() {
		return attributeModifiers;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getRarityIndex() {
		return rarityIndex;
	}

	@Override
	public EquipmentItem getEquipmentAssigned() {
		return equipmentAssigned;
	}

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		if (rarityIndex >= 0)
			this.rarityIndex = rarityIndex;
		return this;
	}

	@Override
	public void setEquipmentAssigned(EquipmentItem equipmentAssigned) {
		this.equipmentAssigned = equipmentAssigned;
	}

}