package games.theRisingAngel.misc;

import java.util.Arrays;

import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.CreatureAttributes;
import games.theRisingAngel.GModalityTRAn;
import games.theRisingAngel.PlayerTRAn;
import tools.ObjectNamedID;

/**
 * TODO refactor giving a full set of starting values (like a set of
 * {@link AttributeModification}).
 */
public class PlayerCharacterTypesHolder {
	public static final int TOTAL_STARTING_ATTRIBUTES = 200, //
			HUMAN_MEAN_ATTRIBUTES = TOTAL_STARTING_ATTRIBUTES / AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT, //
			HUMAN_MEAN_EXCESS_TOTAL = TOTAL_STARTING_ATTRIBUTES
					- (HUMAN_MEAN_ATTRIBUTES * AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT);
	public static final AttributesTRAn[] ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS = { AttributesTRAn.Strength,
			AttributesTRAn.Intelligence, AttributesTRAn.Wisdom, AttributesTRAn.Defense, AttributesTRAn.Constitution,
			AttributesTRAn.Dexterity, AttributesTRAn.Health, AttributesTRAn.Precision, AttributesTRAn.Wisdom };

	private static final int[] HUMAN_STARTING_ATTRIBUTES;
	static {
		int[] attrMean;
		attrMean = new int[AttributesTRAn.ATTRIBUTES_UPGRADABLE_COUNT];
		Arrays.fill(attrMean, HUMAN_MEAN_ATTRIBUTES);
		for (int i = 0, n = HUMAN_MEAN_EXCESS_TOTAL; i < n; i++) {
			attrMean[ATTRIBUTES_HUMAN_RECEIVING_MEAN_EXCESS[i].getIndex()
					- AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE]++;
		}
		HUMAN_STARTING_ATTRIBUTES = attrMean;
	}

	private PlayerCharacterTypesHolder() {
	}

	public enum PlayerCharacterTypes implements ObjectNamedID {
		Human(HUMAN_STARTING_ATTRIBUTES), //
		Wizard(new int[] { 10, 10, 10, 10, 12, 12, 55, 43, 38 }), //
		Monk(new int[] { 12, 12, 11, 17, 14, 14, 30, 35, 55 }), //
		Ogre, Elf, Dwarf,
		/** A Necromancer-magus that uses its own blood and life to cast spells */
		Bloodgus;

		protected final int[] startingAttribues;

		private PlayerCharacterTypes() {
			this(null);
		}

		private PlayerCharacterTypes(int[] startingAttribues) {
			this.startingAttribues = startingAttribues;
		}

		public void applyStartingAttributes(PlayerTRAn player) {
			CreatureAttributes ca;
			ca = player.getAttributes();
			for (int i = 0, n = startingAttribues.length; i < n; i++) {
				ca.setOriginalValue(AttributesTRAn.FIRST_INDEX_ATTRIBUTE_UPGRADABLE + i, startingAttribues[i]);
			}
			ca.setOriginalValue(AttributesTRAn.Velocity.getIndex(),
					GModalityTRAn.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN * 2);
		}

		@Override
		public Integer getID() {
			return ordinal();
		}

		@Override
		public String getName() {
			return name();
		}
	}
}