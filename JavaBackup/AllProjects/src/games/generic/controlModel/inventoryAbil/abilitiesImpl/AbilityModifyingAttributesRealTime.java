package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import java.util.Arrays;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.inventoryAbil.AttributeModification;
import games.generic.controlModel.inventoryAbil.EquipItemAbility;
import games.generic.controlModel.inventoryAbil.EquipmentItem;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.subimpl.AbilityTimedGeneric;

public abstract class AbilityModifyingAttributesRealTime extends EquipmentAbilityBaseImpl
		implements AbilityTimedGeneric, EquipItemAbility {
	private static final long serialVersionUID = 56132035015L;
	public static final long MILLISEC_ATTRIBUTE_UPDATE = 500;

	public AbilityModifyingAttributesRealTime() {
		super();
	}

	public AbilityModifyingAttributesRealTime(AttributeIdentifier[] attributesModified, String name) {
		super(name);
		if (attributesModified != null) {
			setAttributesToModify(attributesModified);
		}
	}

	protected long accTimeElapsed;
	protected AttributeModification[] attributesToModify;

	public AttributeModification[] getAttributesToModify() {
		return attributesToModify;
	}

	@Override
	public long getAccumulatedTimeElapsed() {
		return accTimeElapsed;
	}

	@Override
	public long getTimeThreshold() {
		return MILLISEC_ATTRIBUTE_UPDATE;
	}

	public void setAttributesToModify(AttributeIdentifier[] attributesModified) {
//		this.attributesToModify = attributesToModify; // AttributeModification[] attributesToModify
		if (attributesModified != null) {
			int n;
			attributesToModify = new AttributeModification[n = attributesModified.length];
			while(--n >= 0) {
				attributesToModify[n] = new AttributeModification(attributesModified[n], 0);
			}
		}
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accTimeElapsed = newAccumulated;
	}

	//

	@Override
	public void performAbility(GModality modality) {
		EquipmentItem ei;
		BaseCreatureRPG ah;
		CreatureAttributes ca;
		ei = this.equipItem;
		if (ei == null)
			return;
		ah = ei.getCreatureWearingEquipments();
		if (ah == null)
			return;
		ca = ah.getAttributes();
		if (ca == null)
			return;
		updateAttributes(modality, ei, ah, ca);
	}

	// jet done
//	public void onEquip(GModality gm) {gm.addGameObject(this); }
//	public void onUnEquipping(GModality gm) {gm.removeGameObject(this);}

	//

	/**
	 * Override designed.<br>
	 * Update one or more {@link CreatureAttributes}'s attribute.<br>
	 * Default implementation update just one value, but multiple values at once
	 * could be modified.
	 */
	protected void updateAttributes(GModality gm, EquipmentItem ei, CreatureSimple ah, CreatureAttributes ca) {
		for (AttributeModification am : this.attributesToModify) {
			ca.removeAttributeModifier(am);
		}
		updateAttributesModifiersValues(gm, ei, ah, ca);
		for (AttributeModification am : this.attributesToModify) {
			ca.applyAttributeModifier(am);
		}
	}

//

	public abstract void updateAttributesModifiersValues(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca);

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", ID=" + ID + "\n\t equipped to: "
				+ (this.getEquipItem() == null ? "null" : this.getEquipItem().getName()) + ",\n\t attributesToModify="
				+ Arrays.toString(attributesToModify) + "]";
	}

}