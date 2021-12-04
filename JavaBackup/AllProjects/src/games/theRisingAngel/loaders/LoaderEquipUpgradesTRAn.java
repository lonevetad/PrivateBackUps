package games.theRisingAngel.loaders;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Map;

import games.generic.controlModel.GController;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.inventoryAbil.EquipmentUpgradesProvider;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.subimpl.LoaderEquipUpgrades;
import games.theRisingAngel.LoaderConfigurations;
import games.theRisingAngel.loaders.factories.FactoryEquipUpgrade;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CreatureAttributesTRAn;
import games.theRisingAngel.misc.EquipItemRaritiesTRAn;
import tools.json.JSONParser;
import tools.json.types.JSONArray;
import tools.json.types.JSONObject;

public class LoaderEquipUpgradesTRAn extends LoaderEquipUpgrades {

	public LoaderEquipUpgradesTRAn(GameObjectsProvider<EquipmentUpgrade> objProvider) { super(objProvider); }

	@Override
	public void loadInto(GController gc) {
		int[] index = { 0 };
		JSONArray equipments;
		final LoaderEquipUpgradesTRAn thisLoader = this;

		try {
			equipments = (JSONArray) JSONParser
					.parseFile(LoaderConfigurations.RESOURCE_REPOSITORY_PULL_FACT + "equipUpgrades.json");

			equipments.forEach((indexEquip, rawEquip) -> {
				FactoryEquipUpgrade factory;
				JSONObject equipEquipJSON, attributeModsJSON;
				AttributeModification[] attrMods;
				equipEquipJSON = (JSONObject) rawEquip;
				factory = new FactoryEquipUpgrade();
				factory.name = equipEquipJSON.getFieldValue("name").asString();
				factory.rarity = equipEquipJSON.getFieldValue("rarity").asInt();
				factory.bonusPriceSell = equipEquipJSON.getFieldValue("price").asArrayInt();

				attributeModsJSON = (JSONObject) equipEquipJSON.getFieldValue("attributeModifiers");
				attrMods = new AttributeModification[attributeModsJSON.getFieldsAmount()];
				index[0] = 0;
				attributeModsJSON.forEachField((fieldName, attrValueJSON) -> {
					AttributeIdentifier attribute;
					attribute = AttributesTRAn.valueOf(fieldName);
					attrMods[index[0]++] = new AttributeModification(attribute, attrValueJSON.asInt());
				});
				factory.attrMods = attrMods;
				if (equipEquipJSON.hasField("description")) {
					factory.description = equipEquipJSON.getFieldValue("description").asString();
				}

				thisLoader.saveObjectFactory(factory.name, factory.rarity, factory);
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int size;
		int[] rarities, positiveNegative;
//		LoaderEquipUpgradeFromFile leuff;
//		LinkedList<FactoryEquipUpgrade> factories;
//		FactoryEquipUpgrade fe;
		LoaderEquipUpgradesTRAn loader;
		EquipmentUpgradesProvider equipUpgradeProvider;
		Map<String, FactoryObjGModalityBased<EquipmentUpgrade>> allObjectFactories;
		final CreatureAttributes ca;

		equipUpgradeProvider = new EquipmentUpgradesProvider();
		loader = new LoaderEquipUpgradesTRAn(equipUpgradeProvider);
		ca = new CreatureAttributesTRAn();

		System.out.println("Starting reading Equip Upgrades TRAn");

		loader.loadInto(null);
		allObjectFactories = loader.objProvider.getObjectsIdentified();
		size = allObjectFactories.size();
		rarities = new int[EquipItemRaritiesTRAn.values().length];
		Arrays.fill(rarities, 0);
		positiveNegative = new int[] { 0, 0 };

//				Consumer<AttributeModification> attrModAdder;
//		leuff = new LoaderEquipUpgradeFromFile("TheRisingAngel\\", "equipUpgrades.json");
//		leuff.readAllFile();
//		size = leuff.factories.size();
		System.out.println("LoaderEquipUpgradeFromFile we read:");
//		factories = (LinkedList<FactoryEquipUpgrade>) leuff.factories;
//		attrModAdder = // am -> ca.applyAttributeModifier(am);
//				ca::applyAttributeModifier;
//		while (!factories.isEmpty()) {
//			fe = factories.removeFirst();
//			System.out.println();
//			System.out.println(fe);
//			rarities[fe.rarity]++;
//			if (fe.bonusPriceSell[0] >= 0) {
//				positive++;
//			} else {
//				negative++;
//			}
//			fe.attrMods.forEach(attrModAdder);
//		}
		allObjectFactories.forEach((name, factoryEquip) -> {
			FactoryEquipUpgrade fe;
			fe = (FactoryEquipUpgrade) factoryEquip;
			System.out.println();
			System.out.println(fe);

			rarities[fe.rarity]++;
			if (fe.bonusPriceSell[0] >= 0) {
				positiveNegative[0]++;
			} else {
				positiveNegative[1]++;
			}
			for (AttributeModification am : fe.attrMods) {
				ca.applyAttributeModifier(am);
			}
		});

		System.out.println("total: " + size);
		System.out.println(
				"Which " + positiveNegative[0] + " are positive, and " + positiveNegative[1] + " are negative.");
		System.out.println("RARITIES proportion:");
		System.out.println(Arrays.toString(rarities));
		System.out.println("Total attributes, summing all upgrades' modifications:");
		for (AttributesTRAn attTRan : AttributesTRAn.values()) {
			System.out.println(attTRan.getName() + " -> " + ca.getValue(attTRan));
		}
	}
}