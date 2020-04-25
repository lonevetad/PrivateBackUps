package games.theRisingAngel.loaders;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.AttributesUpgrade;
import games.generic.controlModel.misc.LoaderGeneric;
import games.theRisingAngel.misc.AttributesTRAn;

/** Set of utilities for loading stuffs on TRAr. */
public class LoaderFunctionsTRAn {

	public static List<String> extractAbilitiesName(String temp) {
		String[] splitted;
		List<String> l;
		temp = temp.trim();
		temp = temp.substring(temp.indexOf('[') + 1, temp.lastIndexOf(']')).trim();
		if (temp.length() > 0) {
			splitted = temp.split(",");
			LoaderGeneric.trimAll(splitted);
			for (int i = 0; i < splitted.length; i++) {
//				temp = splitted[i];
				splitted[i] = LoaderGeneric.removeQuotes(splitted[i]);
			}
			l = Arrays.asList(splitted);
		} else {
			l = null; // new LinkedList<>();
		}
		return l;
	}

	public static int[] extractSellPrices(String temp) {
		String[] splitted;
		int[] l;
		temp = temp.trim();
		temp = temp.substring(temp.indexOf('[') + 1, temp.lastIndexOf(']')).trim();
		if (temp.length() > 0) {
			splitted = temp.split(",");
			LoaderGeneric.trimAll(splitted);
			l = new int[splitted.length];
			for (int i = 0; i < splitted.length; i++) {
				l[i] = Integer.parseInt(splitted[i]);
			}
		} else {
			l = null; // new LinkedList<>();
		}
		return l;
	}

	public static Dimension extractDimensionInInventory(String temp) {
		Dimension d;
		String[] splitted;
		d = new Dimension();
		splitted = temp.split(",");
		for (int i = splitted.length - 1; i >= 0; i--) {
			temp = splitted[i];
			if (temp.contains("width")) {
				d.width = LoaderGeneric.extractIntValue(temp.split(":")[1].trim());
			} else if (temp.contains("height")) {
				d.height = LoaderGeneric.extractIntValue(temp.split(":")[1].trim());
			}
		}
		return d;
	}

	/**
	 * This function was extracted and generalized because the "equipment system"
	 * could provide a set of {@link AttributesUpgrade} (that is a parallel of
	 * {@link AttributeModification} because it's a named collection of them) AND
	 * the set of those "equip-mods/upgrades" could be saved in a JSON format,
	 * similar to the one used in {@link LoaderEquipTRAn}
	 */
	public static List<AttributeModification> extractAttributeModifications(LoaderGeneric.JSONLineReader lr) {
		int v;
		AttributesTRAn attr;
		List<AttributeModification> attrMods;
		String splitted[], temp;
		attrMods = new LinkedList<>();
		while((temp = lr.next().trim()).contains("{")) { // read the line and
			splitted = temp.split(",");
			LoaderGeneric.trimAll(splitted);
			temp = LoaderGeneric.trimAll(splitted[0].split(":"))[1].trim();// name of attribute
			temp = LoaderGeneric.removeQuotes(temp); // remove double quotes
			attr = AttributesTRAn.getAttributeTRArByName(temp);
			temp = LoaderGeneric.trimAll(splitted[1].split(":"))[1].trim();// value of attribute
			v = LoaderGeneric.extractIntValue(temp);
			attrMods.add(new AttributeModification(attr, v));
		}
		return attrMods;
	}
}