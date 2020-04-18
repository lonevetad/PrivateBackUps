package games.theRisingAngel.loaders;

import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentItemImpl;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.generic.controlModel.subimpl.LoaderEquipments;
import games.theRisingAngel.inventory.ArmProtectionShieldingDamageByMoney;
import games.theRisingAngel.inventory.EquipmentTypesTRAr;
import games.theRisingAngel.inventory.HelmetOfPlanetaryMeteors;
import games.theRisingAngel.inventory.NecklaceOfPainRinvigoring;

public class LoaderEquipTRAr extends LoaderEquipments {

	public LoaderEquipTRAr(GameObjectsProvider<EquipmentItem> objProvider) {
		super(objProvider);
	}

	@Override
	public void loadInto(GController gc) {
		LoaderEquipFromFile leff;
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Physical));
//		objProvider.addObj(ADamageReductionCurrencyBased.NAME,
//				gmm -> new ADamageReductionCurrencyBased(DamageTypesTRAr.Magical));
//		objProvider.addObj(AMoreDamageReceivedMoreLifeRegen.NAME, gmm -> new AMoreDamageReceivedMoreLifeRegen());
		objProvider.addObj(ArmProtectionShieldingDamageByMoney.NAME,
				(gm) -> new ArmProtectionShieldingDamageByMoney((GModalityRPG) gm));
		objProvider.addObj(NecklaceOfPainRinvigoring.NAME, //
				(gm) -> new NecklaceOfPainRinvigoring((GModalityRPG) gm));

		objProvider.addObj(HelmetOfPlanetaryMeteors.NAME, (gm) -> new HelmetOfPlanetaryMeteors((GModalityRPG) gm));

		leff = new LoaderEquipFromFile("", "equipItems.json");
		leff.readAllFile();
		System.out.println("total equip loaded: " + leff.factories.size());
		for (FactoryEquip fe : leff.factories) {
			objProvider.addObj(fe.name, fe.rarity, fe);
		}
	}

	protected static class LoaderEquipFromFile extends JSONFileConsumer {
		List<FactoryEquip> factories;

		protected LoaderEquipFromFile(String subPath, String filename) {
			super(subPath, filename);
		}

		@Override
		protected void readAllFileImpl(String line) {
			String temp;
			String[] splitted;
			FactoryEquip fe;
			factories = new LinkedList<>();
			while(line.contains("{")) { // start of an equip, must start at the previous end if any
				fe = new FactoryEquip();
				do {
					line = lr.next().trim();
					splitted = line.split(":");
					trimAll(splitted);
					line = LoaderGeneric.removeQuotes(splitted[0]).trim();// as cache
					switch (line) {
					case "name":
						fe.name = LoaderGeneric.removeQuotes(splitted[1]);
						break;
					case "type":
						fe.type = EquipmentTypesTRAr.getEquipTypeTRArByName(LoaderGeneric.removeQuotes(splitted[1]));
						break;
					case "attributeMods":
						fe.attrMods = LoaderFunctionsTRAr.extractAttributeModifications(lr);
						break;
					case "abilities":
						temp = splitted[1].trim();
						fe.abilities = LoaderFunctionsTRAr.extractAbilitiesName(temp);
						break;
					case "rarity":
						fe.rarity = LoaderGeneric.extractIntValue(splitted[1]);
						break;
					default:
						break;
					}

				} while(!line.contains("}")); // to the end
				factories.add(fe);
			}
		}

	}

	protected static class FactoryEquip implements FactoryObjGModalityBased<EquipmentItem> {
		int rarity;
		String name;
		EquipmentTypesTRAr type;
		List<String> abilities = null;
		List<AttributeModification> attrMods = null;

		@Override
		public EquipmentItem newInstance(GModality gm) {
			EquipmentItem ei;
			ei = new EquipmentItemImpl((GModalityRPG) gm, type, name, abilities);
			if (attrMods != null) {
				for (AttributeModification am : attrMods)
					ei.addAttributeModifier(am);
			}
			return ei;
		}

		@Override
		public String toString() {
			return "FactoryEquip [\n name=" + name + ",\n type=" + type + ",\n rarity=" + rarity + ",\n abilities=\n\t"
					+ abilities + ",\n attrMods=\n\t" + attrMods + "]";
		}
	}

	public static void main(String[] args) {
		LoaderEquipFromFile leff;
		leff = new LoaderEquipFromFile("", "equipItems.json");
		leff.readAllFile();
		System.out.println("LoaderEquipFromFile we read:");
		for (FactoryEquip fe : leff.factories) {
			System.out.println();
			System.out.println(fe);
		}
		System.out.println("total: " + leff.factories.size());
	}
}