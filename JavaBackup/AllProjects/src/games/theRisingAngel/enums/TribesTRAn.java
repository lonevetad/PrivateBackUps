
package games.theRisingAngel.enums;

import java.awt.Dimension;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.items.EquipmentItem;
import games.generic.controlModel.items.EquipmentUpgrade;
import games.generic.controlModel.misc.AttributeModification;
import games.generic.controlModel.misc.Currency;
import games.generic.controlModel.misc.CurrencySet;
import games.generic.controlModel.misc.IndexableObject;
import games.generic.controlModel.misc.IndexableObject.IndexToObjectBackmapping;
import games.generic.controlModel.subimpl.EquipmentUpgradeImpl;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.inventory.EINotJewelry;
import tools.Comparators;

public class TribesTRAn {

	public static final Tribe[] ALL_TRIBES;
	public static final IndexToObjectBackmapping INDEX_TO_TRIBE_TRAn;
	public static final Map<TribeReligion, Tribe> MAP_RELIGION_TO_TRIBE;

	public static final RaritiesTRAn RARITY_TRIBE_EQUIPMENT_PIECES;
	public static final Map<RaritiesTRAn, AttributesVariationTribeEquip> MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE;
	public static final Map<EquipmentTypesTRAn, PieceOfEquipmentSetData> MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE;
	public static final Set<EquipmentTypesTRAn> ALL_EQUIP_TYPES_ON_TRIBE_SETS;
	public static final Set<RaritiesTRAn> ALL_EQUIP_UPGRADES_RARITIES;

	// TODO : static initializer
	static {
		MapTreeAVL<RaritiesTRAn, AttributesVariationTribeEquip> mrta;
		MapTreeAVL<EquipmentTypesTRAn, PieceOfEquipmentSetData> metd;
		Map<TribeReligion, Tribe> mrtt;

		ALL_TRIBES = Tribe.values();
		INDEX_TO_TRIBE_TRAn = (int i) -> TribesTRAn.ALL_TRIBES[i];

		RARITY_TRIBE_EQUIPMENT_PIECES = RaritiesTRAn.HighQuality;

		mrta = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, RaritiesTRAn.COMPARATOR_RARITY_TRAn);

		mrta.put(RaritiesTRAn.Scrap, new AttributesVariationTribeEquip("Memory", 3, -1, new int[] { 4 }, true));
		mrta.put(RaritiesTRAn.Common, new AttributesVariationTribeEquip("Trade", 7, -2, new int[] { 8 }, true));
		mrta.put(RaritiesTRAn.WellManifactured,
				new AttributesVariationTribeEquip("Essence", 12, -3, new int[] { 15 }, true));
		mrta.put(RaritiesTRAn.HighQuality,
				new AttributesVariationTribeEquip("Original", 19, -5, new int[] { 20 }, false));
		ALL_EQUIP_UPGRADES_RARITIES = Collections.unmodifiableSet(mrta.keySet());
		MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE = Collections.unmodifiableMap(mrta);

		metd = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn);

		metd.put(EquipmentTypesTRAn.Head,
				new PieceOfEquipmentSetData("Hat", EquipmentTypesTRAn.Head, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 35),
								new AttributeModification(AttributesTRAn.ManaMax, 20),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.PhysicalProbabilityPerThousandHit, -8),
								new AttributeModification(AttributesTRAn.MagicalProbabilityPerThousandHit, -8) }));
		metd.put(EquipmentTypesTRAn.Shoulder,
				new PieceOfEquipmentSetData("Cloak", EquipmentTypesTRAn.Shoulder, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 15),
								new AttributeModification(AttributesTRAn.ManaMax, 40),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.PhysicalProbabilityPerThousandAvoid, -12),
								new AttributeModification(AttributesTRAn.MagicalProbabilityPerThousandAvoid, -12) }));
		metd.put(EquipmentTypesTRAn.Chest,
				new PieceOfEquipmentSetData("Jacket", EquipmentTypesTRAn.Chest, new Dimension(3, 3), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 50),
								new AttributeModification(AttributesTRAn.ManaMax, 5),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 5),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 5),
								new AttributeModification(AttributesTRAn.Velocity, -15) }));
		metd.put(EquipmentTypesTRAn.Hands,
				new PieceOfEquipmentSetData("Gloves", EquipmentTypesTRAn.Hands, new Dimension(1, 1), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 10),
								new AttributeModification(AttributesTRAn.ManaMax, 45),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.VelocityAttackStrikePercentage, 10) }));
		metd.put(EquipmentTypesTRAn.Bracelet,
				new PieceOfEquipmentSetData("Sleeve", EquipmentTypesTRAn.Bracelet, new Dimension(1, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 45),
								new AttributeModification(AttributesTRAn.ManaMax, 10),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.VelocitySpellCastPercentage, 10) }));
		metd.put(EquipmentTypesTRAn.Legs,
				new PieceOfEquipmentSetData("Pants", EquipmentTypesTRAn.Legs, new Dimension(2, 3), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 30),
								new AttributeModification(AttributesTRAn.ManaMax, 25),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 3),
								new AttributeModification(AttributesTRAn.StaminaMax, 5),
								new AttributeModification(AttributesTRAn.StaminaRegen, 1) }));
		metd.put(EquipmentTypesTRAn.Feet,
				new PieceOfEquipmentSetData("Boots", EquipmentTypesTRAn.Feet, new Dimension(2, 2), //
						new AttributeModification[] { new AttributeModification(AttributesTRAn.LifeMax, 35),
								new AttributeModification(AttributesTRAn.ManaMax, 20),
								new AttributeModification(AttributesTRAn.PhysicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.MagicalDamageReduction, 2),
								new AttributeModification(AttributesTRAn.Velocity, 10) }));
		ALL_EQUIP_TYPES_ON_TRIBE_SETS = Collections.unmodifiableSet(metd.keySet());
		MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE = Collections.unmodifiableMap(metd);

		mrtt = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, TribeReligion.COMPARATOR_TRIBE_RELIGION);
		for (Tribe t : ALL_TRIBES) {
			mrtt.put(t.religion, t);
		}
		MAP_RELIGION_TO_TRIBE = Collections.unmodifiableMap(mrtt);
	}

	//

	// TODO : Tribe enum
	public static enum Tribe implements IndexableObject {
		Apebz, Asexiso, Buavoj, Bifod, Cobahir, Cugujab, Dokosok, Domonoleg, Drovovomir, Epewuv, Equaks, Fidnox,
		Folottuj, Gamaskov, Gizix, Gokuq, Guwaddarg, Hansyn, Hitifa, Hugupad, Icibiup, Innizay, Jaonuvup, Jawueqk,
		Jikitiwumo, Kantiw, Kefmah, Kemaf, Lamahaya, Lezerto, Lijislaer, Liwed, Maxibos, Mijumey, Muffinex, Muvuduk,
		Nangeco, Noguev, Ojenaff, Onuhzum, Poltodka, Pomaquox, Puwazok, Quapaq, Quazahnbokt, Ruhujiqk, Ruwizodup,
		Soxoxzeo, Suzurt, Tamawat, Telleno, Tevesimag, Tocomh, Udigutun, Ujuymey, Ulubaw, Vajyrfo, Viwuahz, Warfays,
		Weakimit, Wujoweg, Wunocogdi, Xakonokork, Xayewi, Xecadyma, Xenagon, Yaexuf, Yaqenohz, Yikipq, Zanomozosya,
		Zasitecyo, Zuwidon;

		static {
			Tribe[] tribes = Tribe.values();
			int indexTribe = 0;
			for (int praiseIndex = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE; praiseIndex <= AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE; praiseIndex++) {
				for (int hateIndex = AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE; hateIndex <= AttributesTRAn.LAST_INDEX_ATTRIBUTE_UPGRADABLE; hateIndex++) {
					if (praiseIndex != hateIndex) {
						tribes[indexTribe++].religion = new TribeReligion(AttributesTRAn.ALL_ATTRIBUTES[praiseIndex],
								AttributesTRAn.ALL_ATTRIBUTES[hateIndex]);
					}
				}
			}
		}

		//

//		Tribe(TribeReligion religion) { this.religion = religion; }

		protected TribeReligion religion;

		@Override
		public int getIndex() { return this.ordinal(); }

		@Override
		public String getName() { return this.name(); }

		@Override
		public Long getID() { return (long) this.ordinal(); }

		public TribeReligion getReligion() { return this.religion; }

		@Override
		public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_TRIBE_TRAn; }

		public String stringify() {
			StringBuilder sb;
			String n;
			sb = new StringBuilder(64);
			n = this.name();
			sb.append("Tribe:\t").append(super.toString());
			for (int rep = 3 - (n.length() >> 2); rep >= 0; rep--) {
				sb.append('\t');
			}
			return sb.append(", religion: ").append(this.getReligion().toString()).toString();
		}

		// GENERATORS

		public EquipmentUpgrade newEquipmentUpgradeForRarity(RaritiesTRAn rar, GModalityRPG gmrpg) {
			int n;
			EquipmentUpgrade eu;
			AttributesVariationTribeEquip variation;
			CurrencySet cs;
			Currency[] currencies;
			TribeReligion rel;

			rel = this.religion;
			variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rar);
			eu = new EquipmentUpgradeImpl(rar.getIndex(), getNameEquipUgradeFor(this, rar));

			eu.addAttributeModifier(new AttributeModification(rel.religionDevotedTo, variation.bonus));
			eu.addAttributeModifier(new AttributeModification(rel.religionHated, variation.malus));

			cs = gmrpg.newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gmrpg);
			n = variation.addedPrices.length;
			while (--n >= 0) {
				cs.setCurrencyAmount(currencies[n], variation.addedPrices[n]);
			}
			eu.setPricesModifications(cs);

			return eu;
		}

		protected static EquipmentItem newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType) {
			return newEquipItemFor(gmrpg, tribe, pieceType, null, null);
		}

		protected static EquipmentItem newEquipItemFor(GModalityRPG gmrpg, Tribe tribe, EquipmentTypesTRAn pieceType,
				PieceOfEquipmentSetData ped, AttributesVariationTribeEquip variation) {
			int n;
			AttributeModification[] allAttributes;
			EquipmentItem equipPiece;
			CurrencySet cs;
			Currency[] currencies;
			TribeReligion rel;

			Objects.requireNonNull(gmrpg);

			if (variation == null) {
				variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(RARITY_TRIBE_EQUIPMENT_PIECES);
			}
			if (ped == null) { ped = MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.get(pieceType); }

			rel = tribe.religion;
			allAttributes = new AttributeModification[2 + ped.additionalAttributeModifiers.length];
			allAttributes[0] = new AttributeModification(rel.religionDevotedTo, variation.bonus);
			allAttributes[1] = new AttributeModification(rel.religionHated, variation.malus);
			System.arraycopy(ped.additionalAttributeModifiers, 0, allAttributes, 2,
					ped.additionalAttributeModifiers.length);

			equipPiece = new EINotJewelry(gmrpg, pieceType, getNameEquipFor(tribe, pieceType), allAttributes);

			equipPiece.setDimensionInInventory(ped.dimensionInventory);

			cs = gmrpg.newCurrencyHolder();
			currencies = cs.getCurrencies();
			cs.setGameModaliy(gmrpg);
			n = variation.addedPrices.length;
			while (--n >= 0) {
				cs.setCurrencyAmount(currencies[n], variation.addedPrices[n] << 1);
			}
			equipPiece.setSellPrice(cs);
			return equipPiece;
		}

		public EquipmentItem newEquipmentPiece(GModalityRPG gmrpg, EquipmentTypesTRAn pieceType) {
			return newEquipItemFor(gmrpg, this, pieceType);
		}

		public MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> newEquipmentSet(GModalityRPG gmrpg) {
			final MapTreeAVL<EquipmentTypesTRAn, EquipmentItem> m;
			final AttributesVariationTribeEquip variation;
			final Tribe thisTribe;

			m = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					EquipmentTypesTRAn.COMPARATOR_EQUIP_TYPES_TRAn);

			thisTribe = this;
			variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(RARITY_TRIBE_EQUIPMENT_PIECES);

			MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.forEach( //
					(eqType, ped) -> m.put(eqType, newEquipItemFor(gmrpg, thisTribe, eqType, ped, variation)) //
			);

			return m;
		}

		public TribeWarStatus getWarsStatusWith(TribesTRAn.Tribe otherTribe) {
			TribeReligion tr, or;
			if (otherTribe == null || otherTribe == this) { return TribeWarStatus.Neutral; }
			tr = this.religion;
			or = otherTribe.religion;
			if (isWorstEnemy(otherTribe) || tr.religionDevotedTo == or.religionHated
					|| tr.religionHated == or.religionDevotedTo) {
				return TribeWarStatus.War;
			} else if (tr.religionDevotedTo == or.religionDevotedTo || tr.religionHated == or.religionHated) {
				return TribeWarStatus.Ally;
			}
			return TribeWarStatus.Neutral;
		}

		public boolean isWorstEnemy(TribesTRAn.Tribe otherTribe) {
			TribeReligion tr, or;
			tr = this.religion;
			or = otherTribe.religion;
			return (tr.religionDevotedTo == or.religionHated) && (tr.religionHated == or.religionDevotedTo);
		}
	}

//

	// TODO : TribeWarStatus enum
	public static enum TribeWarStatus implements IndexableObject {
		Neutral, War, Ally;

		public static final TribeWarStatus[] ALL_TRIBE_WAR_STATUS;
		public static final IndexToObjectBackmapping INDEX_TO_TRIBE_WAR_STATUS;

		static {
			ALL_TRIBE_WAR_STATUS = TribeWarStatus.values();
			INDEX_TO_TRIBE_WAR_STATUS = (int i) -> ALL_TRIBE_WAR_STATUS[i];
		}

		@Override
		public Long getID() { return (long) this.ordinal(); }

		@Override
		public String getName() { return this.name(); }

		@Override
		public int getIndex() { return this.ordinal(); }

		@Override
		public IndexToObjectBackmapping getFromIndexBackmapping() { return INDEX_TO_TRIBE_WAR_STATUS; }

	}

	// TODO : TribeWarStatusTRAn enum

	//

	// TODO : other stuffs

	public static Tribe getTribeByReligion(TribeReligion religion) {
		if (religion == null) { return null; }
		return MAP_RELIGION_TO_TRIBE.get(religion);
	}

	public static String getNameEquipUgradeFor(Tribe tribe, RaritiesTRAn rarity) {
		AttributesVariationTribeEquip variation;
		variation = MAP_RARITY_TO_ATTRIBUTE_UPGRADES_TRIBE.get(rarity);
		if (variation == null) { throw new IllegalArgumentException("Not accepted rarity: " + rarity); }
		return "of " + tribe.getName() + " Tribe " + variation.getVariationName();
	}

	public static String getNameEquipFor(Tribe tribe, EquipmentTypesTRAn equipType) {
		PieceOfEquipmentSetData ped;
		ped = MAP_EQUIPMENT_PIECE_TO_DATA_TRIBE.get(equipType);
		if (ped == null) { throw new IllegalArgumentException("Not accepted equipment type: " + equipType); }
		return ped.namePrefix + " Cosplay of " + tribe.getName() + " Tribe";
	}

	//

	public static final class TribeReligion {
		public static final Comparator<TribeReligion> COMPARATOR_TRIBE_RELIGION = (r1, r2) -> {
			int res;
			if (r1 == r2) { return 0; }
			if (r1 == null) { return -1; }
			if (r2 == null) { return 1; }
			res = Comparators.LONG_COMPARATOR.compare(r1.religionDevotedTo.getID(), r2.religionDevotedTo.getID());
			if (res != 0) { return res; }
			return Comparators.LONG_COMPARATOR.compare(r1.religionHated.getID(), r2.religionHated.getID());
		};

		public TribeReligion(AttributesTRAn religionDevotedTo, AttributesTRAn religionHated) {
			super();
			if (religionDevotedTo == null || religionHated == null || religionDevotedTo == religionHated) {
				throw new IllegalArgumentException("Can't praise and hate a null religion or the same religions: <"
						+ religionDevotedTo + ", " + religionHated + ">");
			}
			this.religionDevotedTo = religionDevotedTo;
			this.religionHated = religionHated;
		}

		protected final AttributesTRAn religionDevotedTo, religionHated;

		//
		public AttributesTRAn getReligionDevotedTo() { return religionDevotedTo; }

		public AttributesTRAn getReligionHated() { return religionHated; }

		@Override
		public String toString() {
			return "TribeReligion [religionDevotedTo=" + religionDevotedTo + ", religionHated=" + religionHated + "]";
		}
	}

	public static class AttributesVariationTribeEquip {
		public AttributesVariationTribeEquip(String variationName, int bonus, int malus, int[] addedPrices,
				boolean canBeEquipUpgrade) {
			super();
			this.variationName = variationName;
			this.bonus = bonus;
			this.malus = malus;
			this.addedPrices = addedPrices;
			this.canBeEquipUpgrade = canBeEquipUpgrade;
		}

		protected boolean canBeEquipUpgrade;
		protected int bonus;
		protected int malus;
		protected int[] addedPrices;
		protected String variationName;

		//
		public String getVariationName() { return variationName; }

		public int getBonus() { return bonus; }

		public int getMalus() { return malus; }

		public int[] getAddedPrices() { return addedPrices; }

		public boolean isCanBeEquipUpgrade() { return canBeEquipUpgrade; }
		//
//		public void setVariationName(String variationName) { this.variationName = variationName; }
//		public void setBonus(int bonus) { this.bonus = bonus; }
//		public void setMalus(int malus) { this.malus = malus; }
//		public void setAddedPrices(int[] addedPrices) { this.addedPrices = addedPrices; }
//		public void setCanBeEquipUpgrade(boolean canBeEquipUpgrade) { this.canBeEquipUpgrade = canBeEquipUpgrade; }
	}

	//

	public static class PieceOfEquipmentSetData {
		public PieceOfEquipmentSetData(String namePrefix, EquipmentTypesTRAn equipType, Dimension dimensionInventory,
				AttributeModification[] additionalAttributeModifiers) {
			super();
			this.namePrefix = namePrefix;
			this.equipType = equipType;
			this.dimensionInventory = dimensionInventory;
			this.additionalAttributeModifiers = additionalAttributeModifiers;
		}

		protected String namePrefix;
		protected EquipmentTypesTRAn equipType;
		protected Dimension dimensionInventory;
		protected AttributeModification[] additionalAttributeModifiers;

		//
		public String getNamePrefix() { return namePrefix; }

		public EquipmentTypesTRAn getEquipType() { return equipType; }

		public Dimension getDimensionInventory() { return dimensionInventory; }

		public AttributeModification[] getAdditionalAttributeModifiers() { return additionalAttributeModifiers; }
		//
		// public void setNamePrefix(String namePrefix) { this.namePrefix = namePrefix;
		// }
		// public void setEquipType(EquipmentTypesTRAn equipType) { this.equipType =
		// equipType; }
		// public void setDimensionInventory(Dimension dimensionInventory) {
		// this.dimensionInventory = dimensionInventory; }
		// public void setAdditionalAttributeModifiers(AttributeModification[]
		// additionalAttributeModifiers) { this.additionalAttributeModifiers =
		// additionalAttributeModifiers; }
	}

	public static void main(String[] args) {
		int i = 0;
		for (Tribe t : Tribe.values()) {
			System.out.println(i++ + "\t-> " + t.stringify());
		}
	}
}