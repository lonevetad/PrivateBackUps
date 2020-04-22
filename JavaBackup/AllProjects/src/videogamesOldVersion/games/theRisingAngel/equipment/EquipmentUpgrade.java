package videogamesOldVersion.games.theRisingAngel.equipment;

import java.io.Serializable;
import java.util.Map;

import dataStructures.MapTreeAVL;
import videogamesOldVersion.common.mainTools.MyComparator;
import videogamesOldVersion.games.theRisingAngel.main.StatisticheTRAn;
import videogamesOldVersion.games.theRisingAngel.main.StatisticheTRAn.StatsTRAn;

/**
 * nota del 22/03/2020
 * <p>
 * Set of power ups, of statistics' modifiers (i.e., character's parameters'
 * modifiers).<br>
 * Instead of create an array (that mimics the character's statistics) and apply
 * to that array all modifiers, wasting memory in almost-empty arrays, just
 * collect all statistic modifications.
 */
public class EquipmentUpgrade implements Serializable {
	private static final long serialVersionUID = -845020L;

	public static final MyComparator<EquipmentUpgrade> COMPARATOR_EquipmentUpgrades = (e1, e2) -> {
		int o1, o2;
		if (e1 == e2)
			return 0;
		if (e1 == null)
			return -1;
		if (e2 == null)
			return 1;
		return
		// e1.compareTo(e2)
		((o1 = e1.idUpgrade) == (o2 = e2.idUpgrade)) ? 0 : (o1 > o2 ? 1 : -1);
	};

	public static enum EquipUpgradesAvaiable {
		Moonblessed(ni().setStatModification(StatsTRAn.RIGEN_MANA, 8)) //
		, Sunblessed(ni().setStatModification(StatsTRAn.RIGEN_VITA, 32))//
		, Pure(ni())//
		, Evil(ni())//
		, Good(ni())//
		, Bloody(ni())//
		, Blessed(ni())//
		, Doomed(ni())//
		, Dirty(ni())//
		, Precious(ni())//
		, Lovely(ni())//
		, Nice(ni())//
		, Spiky(ni())//

		//
		, OfWarrior(ni())//
		, OfPriest(ni())//
		, OfRanger(ni())//
		, OfKnight(ni())//
		, OfThief(ni())//
		, OfOverwatch(ni())//
		, OfElves(ni())//
		, OfOrcs(ni())//
		, OfWoodcutter(ni())//
		, OfKing(ni())//
		, OfQueen(ni())//
		, OfPaesant(ni())//
		, OfStudent(ni())//
		, OfMerchant(ni())//

		//
		, OfIron(ni())//
		, OfGold(ni())//
		, OfSilver(ni())//
		, OfCopper(ni())//
		, OfDiamond(ni())//
		, OfPlatinum(ni())//
		, OfAlluminium(ni())//
		//
		, OfFire(ni())//
		, OfWater(ni())//
		, OfThunder(ni())//
		, OfAir(ni())//
		, OfEarth(ni())//
		//
		, White(ni())//
		, Black(ni())//
		, Red(ni())//
		, Green(ni())//
		, Blue(ni())//
		, Yellow(ni())//
		, Brown(ni())//
		, Orange(ni())//
		, Purple(ni())//
		, Pink(ni().setStatModification(StatsTRAn.RIGEN_VITA, 1).setStatModification(StatsTRAn.FORTUNA_MIN, 1))//
		;
		EquipUpgradesAvaiable(EquipmentUpgrade eu) {
			equipmentUpgrade = eu;
			eu.idUpgrade = ordinal();
		}

		EquipmentUpgrade equipmentUpgrade;

		public EquipmentUpgrade getEquipmentUpgrade() {
			return equipmentUpgrade;
		}
	}

	protected static EquipmentUpgrade ni() {
		return new EquipmentUpgrade();
	}

	protected EquipmentUpgrade() {
		mods = MapTreeAVL<StatisticheTRAn.StatsTRAn, Integer>. .BehaviourOnDuplicate.OVERWRITE, //
				// Comparators.INTEGER_COMPARATOR
				StatisticheTRAn.StatsTRAn.COMPARATOR_STAT_TRAN);
	}

	Integer idUpgrade;
	Map<StatisticheTRAn.StatsTRAn, Integer> mods;

	//

	// TODO GETTER

	public Integer getIdUpgrade() {
		return idUpgrade;
	}

	public Map<StatisticheTRAn.StatsTRAn, Integer> getMods() {
		return mods;
	}

	//

	protected EquipmentUpgrade setStatModification(StatisticheTRAn.StatsTRAn stat, int val) {
		// Integer i;
		if (stat != null) {
			// i=mods.fetch(stat);
			// if()
			mods.add(stat, val);
		}
		return this;
	}

	public Integer getStatModification(StatisticheTRAn.StatsTRAn stat) {
		return mods.fetch(stat);
	}

	// StatisticheTRAn s;

	public void applyEffect(StatisticheTRAn s) {
		int[] a;
		a = s.getStatsInt();
		this.mods.forEach((enumStat, amount) -> {
			a[enumStat.ordinal()] += amount;
		});
	}

	public void removeEffect(StatisticheTRAn s) {
		int[] a;
		a = s.getStatsInt();
		this.mods.forEach((enumStat, amount) -> {
			a[enumStat.ordinal()] -= amount;
		});
	}
}
