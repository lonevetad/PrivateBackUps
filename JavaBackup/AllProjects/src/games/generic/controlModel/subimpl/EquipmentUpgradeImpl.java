package games.generic.controlModel.subimpl;

import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.misc.CurrencySet;
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
	protected CurrencySet priceModifications;

	@Override
	public Set<AttributeModification> getAttributeModifiers() { return attributeModifiers; }

	@Override
	public String getName() { return name; }

	@Override
	public int getRarityIndex() { return rarityIndex; }

	@Override
	public EquipmentItem getEquipmentAssigned() { return equipmentAssigned; }

	@Override
	public CurrencySet getPricesModifications() { return priceModifications; }

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		if (rarityIndex >= 0)
			this.rarityIndex = rarityIndex;
		return this;
	}

	@Override
	public void setEquipmentAssigned(EquipmentItem equipmentAssigned) { this.equipmentAssigned = equipmentAssigned; }

	@Override
	public void setPricesModifications(CurrencySet priceModifications) {
		this.priceModifications = priceModifications;
		if (priceModifications != null) { priceModifications.setCanFireCurrencyChangeEvent(false); }
	}

	@Override
	public String toString() {
		return "\tEquipmentUpgradeImpl [\n\t\tname=" + name + ", rarityIndex=" + rarityIndex
				+ ",\n\t\tpriceModifications=" + priceModifications + ",\n\t\tattributeModifiers="
				+ attributeModifiersToString() + "]";
	}

	public String attributeModifiersToString() {
		StringBuilder sb;
		if (attributeModifiers == null)
			return "null";
		if (attributeModifiers.isEmpty())
			return "";
		sb = new StringBuilder(16);
		attributeModifiers.forEach(am -> sb.append("\n\t\t\t").append(am));
		return sb.toString();
	}

}