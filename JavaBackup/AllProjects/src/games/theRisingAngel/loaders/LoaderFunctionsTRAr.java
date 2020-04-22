package games.theRisingAngel.loaders;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.AttributesUpgrade;
import games.generic.controlModel.misc.LoaderGeneric;
import games.theRisingAngel.misc.AttributesTRAr;

/** Set of utilities for loading stuffs on TRAr. */
public class LoaderFunctionsTRAr {

	public static List<String> extractAbilitiesName(String temp) {
		String[] splitted;
		List<String> l;
		temp = temp.trim();
		temp = temp.substring(temp.indexOf('[') + 1, temp.lastIndexOf(']')).trim();
		if (temp.length() > 0) {
			splitted = temp.split(",");
			LoaderGeneric.trimAll(splitted);
			for (int i = 0; i < splitted.length; i++) {
				temp = splitted[i];
				splitted[i] = LoaderGeneric.removeQuotes(splitted[i]);
			}
			l = Arrays.asList(splitted);
		} else {
			l = null; // new LinkedList<>();
		}
		return l;
	}

	/**
	 * This function was extracted and generalized because the "equipment system"
	 * could provide a set of {@link AttributesUpgrade} (that is a parallel of
	 * {@link AttributeModification} because it's a named collection of them) AND
	 * the set of those "equip-mods/upgrades" could be saved in a JSON format,
	 * similar to the one used in {@link LoaderEquipTRAr}
	 */
	public static List<AttributeModification> extractAttributeModifications(LoaderGeneric.JSONLineReader lr) {
		int v;
		AttributesTRAr attr;
		List<AttributeModification> attrMods;
		String splitted[], temp;
		attrMods = new LinkedList<>();
		while((temp = lr.next().trim()).contains("{")) { // read the line and
			splitted = temp.split(",");
			LoaderGeneric.trimAll(splitted);
			temp = LoaderGeneric.trimAll(splitted[0].split(":"))[1].trim();// name of attribute
			temp = LoaderGeneric.removeQuotes(temp); // remove double quotes
			attr = AttributesTRAr.getAttributeTRArByName(temp);
			temp = LoaderGeneric.trimAll(splitted[1].split(":"))[1].trim();// value of attribute
			v = LoaderGeneric.extractIntValue(temp);
			attrMods.add(new AttributeModification(attr, v));
		}
		return attrMods;
	}
}