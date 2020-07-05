package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AbilityTimedGeneric;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.ObjectWithID;

public abstract class AbilityModifyingAttributesRealTime extends AbilityBaseImpl // EquipmentAbilityBaseImpl
		implements AbilityTimedGeneric { // , EquipItemAbility
	private static final long serialVersionUID = 56132035015L;
	public static final int MILLISEC_ATTRIBUTE_UPDATE = 500;

	public AbilityModifyingAttributesRealTime() { super(); }

	public AbilityModifyingAttributesRealTime(String name) { super(name); }

	public AbilityModifyingAttributesRealTime(String name, AttributeIdentifier[] attributesModified) {
		super(name);
		if (attributesModified != null) { setAttributesToModify(attributesModified); }
	}

	protected long accumulatedTimeElapsedForUpdating;
	/** Attributes this ability modifies. */
	protected AttributeModification[] attributesToModify;

	public AttributeModification[] getAttributesToModify() { return attributesToModify; }

	@Override
	public long getAccumulatedTimeElapsed() { return accumulatedTimeElapsedForUpdating; }

	@Override
	public long getTimeThreshold() { return MILLISEC_ATTRIBUTE_UPDATE; }

	public void setAttributesToModify(AttributeIdentifier[] attributesModified) {
//		this.attributesToModify = attributesToModify; // AttributeModification[] attributesToModify
		if (attributesModified != null) {
			attributesToModify = AttributeModification.newEmptyArray(attributesModified);
		}
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accumulatedTimeElapsedForUpdating = newAccumulated;
	}

	//

	protected void applyModifyingEffecsOnEquipping() {
		CreatureAttributes ca;
		ca = getAttributesOfOwner();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

	protected void removeModifyingEffecs() {
		CreatureAttributes ca;
		ca = getAttributesOfOwner();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
	}

	protected void removeAndNullifyModifyingEffecs() {
		CreatureAttributes ca;
		ca = getAttributesOfOwner();
		if (ca == null)
			return;
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
			am.setValue(0);
		}
	}

	@Override
	public void onAddingToOwner(GModality gm) {
//		super.onEquip(gm);
		AbilityTimedGeneric.super.onAddingToOwner(gm);
		applyModifyingEffecsOnEquipping();
	}

	@Override
	public void performAbility(GModality modality) {
//		EquipmentItem ei;
		BaseCreatureRPG ah;
		CreatureAttributes ca;
		ObjectWithID o;
		o = owner;
		if (o == null)
			return;
		ah = (o instanceof BaseCreatureRPG) ? ((BaseCreatureRPG) o) : null; // ei.getCreatureWearingEquipments();
		if (ah == null)
			return;
		ca = ah.getAttributes();
		if (ca == null)
			return;
		updateAttributes(modality, /* ei, */ ah, ca);
	}

	// jet done
//	public void onEquip(GModality gm) {gm.addGameObject(this); }
//	public void onUnEquipping(GModality gm) {gm.removeGameObject(this);}

	//

	/**
	 * Override designed.<br>
	 * Update one or more {@link CreatureAttributes}'s attribute (the ones in
	 * {@link #getAttributesToModify()}).
	 */
	protected void updateAttributes(GModality gm, /* EquipmentItem ei, */ CreatureSimple ah, CreatureAttributes ca) {
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
		updateAttributesModifiersValues(gm, /* ei, */ ah, ca);
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

//

	public abstract void updateAttributesModifiersValues(GModality gm, /* EquipmentItem ei, */ CreatureSimple ah,
			CreatureAttributes ca);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + ID +
//			+	"\n\t equipped to: " + (this.getEquipItem() == null ? "null" : this.getEquipItem().getName())
				",\n\t attributesToModify=" + Arrays.toString(attributesToModify) + "]";
	}
}