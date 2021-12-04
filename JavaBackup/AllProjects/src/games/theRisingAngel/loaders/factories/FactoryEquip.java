package games.theRisingAngel.loaders.factories;

import java.util.Arrays;
import java.util.List;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AbilitiesProvider;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.GameObjectsProvidersHolderRPG;
import games.theRisingAngel.inventory.EquipmentTypesTRAn;

public class FactoryEquip implements FactoryObjGModalityBased<EquipmentItem> {
	FactoryItems fi = new FactoryItems();
	EquipmentTypesTRAn type;
	List<String> abilities = null;
	List<AttributeModification> attrMods = null;

	@Override
	public EquipmentItem newInstance(GModality gm) {
		EquipmentItem ei;
//		ei = new EquipmentItemImpl((GModalityRPG) gm, type, name);
		ei = type.factory.newEquipItem((GModalityRPG) gm, type, fi.name);
		setValues(gm, ei);
		return ei;
	}

	protected void setValues(GModality gm, EquipmentItem ei) {
//		EquipmentItem ei;
//		ei = (EquipmentItem) ii;
		fi.setValues(gm, ei);
		if (attrMods != null) {
			for (AttributeModification am : attrMods)
				ei.addAttributeModifier(am);
		}
		if (abilities != null) {
			GameObjectsProvidersHolderRPG gophRpg;
			AbilitiesProvider ap;
			gophRpg = (GameObjectsProvidersHolderRPG) gm.getGameObjectsProvider();
			ap = gophRpg.getAbilitiesProvider();
			for (String an : abilities) {
				ei.addAbility(ap.getAbilityByName(gm, an));
			}
		}
	}

	@Override
	public String toString() {
		return "FactoryEquip [\n name=" + fi.name + ", type=" + type + ",\n rarity=" + fi.rarity + ", sell price: "
				+ Arrays.toString(fi.price) + ",\n dimensions in inventory: " + fi.dimensionInInventory
				+ ",\n abilities=\n\t" + abilities + ",\n attrMods=\n\t" + attrMods + "]";
	}
}