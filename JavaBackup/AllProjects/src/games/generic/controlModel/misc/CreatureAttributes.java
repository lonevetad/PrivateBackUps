package games.generic.controlModel.misc;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.inventoryAbil.EquipmentSet;

/**
 * Implements a set of attributes. "Creature" could be "the player's character",
 * a simple enemy or something else.<br>
 * Currently, attributes are treated as integers.<br>
 * it's advised to separate "original values" and "values obtained from
 * equipments, buffs, auras, etc".
 */
public abstract class CreatureAttributes {
	protected final int attributesCount;
	protected final int[] originalValues;
	protected CreatureAttributesBonusesCalculator bonusCalculator;

	public CreatureAttributes(int attributesCount) {
		if (attributesCount < 1) {
			throw new IllegalArgumentException("Cannot have less than 1 attributes: " + attributesCount);
		}
		this.originalValues = new int[this.attributesCount = attributesCount];
	}

	//

//	public int[] getOriginalValues() {
//		return originalValues;
//	}

	public int getAttributesCount() {
		return attributesCount;
	}

	/**
	 * Get the value of a specific attribute, identified by its index.<br>
	 * It's the original value, the ones associated with the object holding this
	 * class's instance (usually instances of {@link AttributesHolder} and
	 * consequently {@link CreatureOfRPGs}) and that defines them.
	 * <p>
	 * It differs from {@link #getValue(int)} because this methods does not take
	 * into account {@link EquipmentItem}'s {@link AttributeModification},
	 * abilities, spells, auras, etc.
	 */
	public int getOriginalValue(int index) {
		return this.originalValues[index];
	}

	/**
	 * Get the value of a specific attribute, identified by its index.<br>
	 * The index is usually provided through {@link AttributeIdentifier#getIndex()}.
	 * <br>
	 * Could be the result of some calculation, maybe based on
	 * {@link #getOriginalValue(int)} and something else.
	 * <p>
	 * It differs from {@link #getOriginalValue(int)} because this methods compute
	 * the sum of the original value and all of alterations provided by equipments
	 * (i.e.: {@link EquipmentItem} and their {@link AttributeModification}),
	 * effects, auras, magics, abilities, battlefield's influences, etc.
	 */
	public int getValue(int index) {
		return this.originalValues[index];
	}

	/**
	 * Calls {@link #getValue(int)} passing, as parameter, the value returned by the
	 * invocation of {@link AttributeIdentifier#getIndex()} over the first
	 * parameter.
	 */
	public int getValue(AttributeIdentifier identifier) {
		return getValue(identifier.getIndex());
	}

	public CreatureAttributesBonusesCalculator getBonusCalculator() {
		return bonusCalculator;
	}

	//

	public void setBonusCalculator(CreatureAttributesBonusesCalculator bonusCalculator) {
		this.bonusCalculator = bonusCalculator;
	}

	/**
	 * Set the attribute's (identified by the index: first parameter) value (second
	 * parameter).
	 */
	public void setOriginalValue(int index, int value) {
		this.originalValues[index] = value;
	}

	//

//	public abstract void addAttributeModification(int index, int value);
//	public abstract void removeAttributeModification(int index, int value);

	/**
	 * Apply (add) the given {@link AttributeModification} to this set of
	 * attributes.<br>
	 * Usually it's provided by {@link EquipmentItem#getBaseAttributeModifiers()}
	 * and usually this method is invoked during
	 * {@link EquipmentItem#onEquip(GModality)}, that is invoked by
	 * {@link EquipmentSet#addEquipmentItem(GModality, EquipmentItem)}.
	 * <p>
	 * It's advised to apply it to a transient "parallel instance" of
	 * {@link #getOriginalValues()} so that ALL modifiers merges there (making
	 * simple and easy to separate original {@link CreatureOfRPGs}'s value and the
	 * ones provided by abilities and the equipments.
	 */
	public abstract void applyAttributeModifier(AttributeModification eam);

	/** Opposite of {@link #applyAttributeModifier(AttributeModification)}. */
	public abstract void removeAttributeModifier(AttributeModification eam);

	@Override
	public String toString() {
		return "CreatureAttributes [originalValues=" + Arrays.toString(originalValues) + "]";
	}
}