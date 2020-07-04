package games.theRisingAngel.loaders;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentUpgrade;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.FactoryObjGModalityBased;
import games.generic.controlModel.misc.GameObjectsProvider;
import games.generic.controlModel.misc.LoaderGeneric;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.generic.controlModel.subimpl.LoaderEquipUpgrades;

public class LoaderEquipUpgradesTRAn extends LoaderEquipUpgrades {

	public LoaderEquipUpgradesTRAn(GameObjectsProvider<EquipmentUpgrade> objProvider) {
		super(objProvider);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadInto(GController gc) {
		LoaderEquipUpgradeFromFile leuff;
		LinkedList<FactoryEquipUpgrade> factories;
		FactoryEquipUpgrade fe;
		// TODO prendere ispirazione da LoaderEquipments

		// e poi
		leuff = new LoaderEquipUpgradeFromFile("", "equipUpgradesTRAr.json");
		leuff.readAllFile();
		factories = (LinkedList<FactoryEquipUpgrade>) leuff.factories;
		System.out.println("total equip-upgrades loaded: " + factories.size());
		while (!factories.isEmpty()) {
			fe = factories.removeFirst();
			objProvider.addObj(fe.name, fe.rarity, fe);
		}
	}

	protected static class LoaderEquipUpgradeFromFile extends JSONFileConsumer {
		List<FactoryEquipUpgrade> factories;

		protected LoaderEquipUpgradeFromFile(String subPath, String filename) { super(subPath, filename); }

		@Override
		protected void readAllFileImpl(String line) {
//			String temp;
			int indexComma;
			String[] splitted;
			FactoryEquipUpgrade fe;
			factories = new LinkedList<>();
			while (line.contains("{")) { // start of an equip, must start at the previous end if any
				fe = new FactoryEquipUpgrade();
				do {
					line = getLineReader().next().trim();
					indexComma = line.indexOf(':');
					if (indexComma >= 0) {
//						splitted = line.split(":"); //
						splitted = new String[] { line.substring(0, indexComma), line.substring(indexComma + 1) };
						trimAll(splitted);
						line = LoaderGeneric.removeQuotes(splitted[0]).trim();// as cache
						switch (line) {
						case "name":
							fe.name = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "description":
							fe.description = LoaderGeneric.removeQuotes(splitted[1]);
							break;
						case "modifiers":
							fe.attrMods = LoaderFunctionsTRAn.extractAttributeModifications(getLineReader());
							break;
						case "rarity":
							fe.rarity = LoaderGeneric.extractIntValue(splitted[1]);
							break;
						case "bonusPrice":
							fe.bonusPriceSell = LoaderFunctionsTRAn.extractSellPrices(splitted[1].trim());
							break;
						default:
							break;
						}
					}
				} while (!line.contains("}")); // to the end
				factories.add(fe);
			}
		}
	}

	protected static class FactoryEquipUpgrade implements FactoryObjGModalityBased<EquipmentUpgrade> {
		int rarity;
		int[] bonusPriceSell = null;
		String name, description;
		List<AttributeModification> attrMods = null;

		@Override
		public EquipmentUpgrade newInstance(GModality gm) {
			EquipmentUpgrade eu;
			eu = new EquipmentUpgradeImpl(rarity, name);
//			ei.description = description;
			if (attrMods != null) {
				for (AttributeModification am : attrMods)
					eu.addAttributeModifier(am);
			}
			if (bonusPriceSell != null) {
				int n;
				CurrencySet cs;
				cs = gm.newCurrencyHolder();
				cs.setGameModaliy(gm); // not needed
				n = bonusPriceSell.length;
				while (--n >= 0)
					cs.setCurrencyAmount(n, bonusPriceSell[n]);
				eu.setPricesModifications(cs);
			}
			return eu;
		}

		@Override
		public String toString() {
			return "FactoryEquipUpgrade [\n name=" + name + ",\n rarity=" + rarity + ",\n attrMods=\n\t" + attrMods
					+ "]";
		}
	}

	public static void main(String[] args) {
		int size;
		int[] rarities;
		LoaderEquipUpgradeFromFile leuff;
		LinkedList<FactoryEquipUpgrade> factories;
		FactoryEquipUpgrade fe;
		leuff = new LoaderEquipUpgradeFromFile("", "equipUpgradesTRAr.json");
		leuff.readAllFile();
		size = leuff.factories.size();
		System.out.println("LoaderEquipUpgradeFromFile we read:");
		rarities = new int[6];
		Arrays.fill(rarities, 0);
		factories = (LinkedList<FactoryEquipUpgrade>) leuff.factories;
		while (!factories.isEmpty()) {
			fe = factories.removeFirst();
			System.out.println();
			System.out.println(fe);
			rarities[fe.rarity]++;
		}
		System.out.println("total: " + size);
		System.out.println("RARITIES proportion:");
		System.out.println(Arrays.toString(rarities));
	}
}