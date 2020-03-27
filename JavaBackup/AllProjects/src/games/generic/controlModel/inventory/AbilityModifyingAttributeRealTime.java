package games.generic.controlModel.inventory;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.misc.AbilityTimedGeneric;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;

/** This class is originally designed as */
public abstract class AbilityModifyingAttributeRealTime implements AbilityTimedGeneric, EquipItemAbility {
	public static final long MILLISEC_ATTRIBUTE_UPDATE = 500;
	long accTimeElapsed;
	protected Integer ID;
	AttributeModification attributeToModify;
	EquipmentItem equipItem;

	public AbilityModifyingAttributeRealTime() {
		super();
		assignID();
	}

	public AbilityModifyingAttributeRealTime(AttributeIdentifier attributeModified) {
		this();
		if (attributeModified != null)
			this.attributeToModify = new AttributeModification(attributeModified, 0);
	}

	public AttributeModification getAttributeToModify() {
		return attributeToModify;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public EquipmentItem getEquipItem() {
		return equipItem;
	}

	@Override
	public long getAccumulatedTimeElapsed() {
		return accTimeElapsed;
	}

	@Override
	public long getTimeThreshold() {
		return MILLISEC_ATTRIBUTE_UPDATE;
	}

	@Override
	public String getName() {
		return attributeToModify.getAttributeModified().getName();
	}
	//

	public void setAttributeToModify(AttributeModification attributeToModify) {
		this.attributeToModify = attributeToModify;
	}

	@Override
	public void setEquipItem(EquipmentItem equipmentItem) {
		this.equipItem = equipmentItem;
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.accTimeElapsed = newAccumulated;
	}

	//

	@Override
	public void performAbility(GModality modality) {
		EquipmentItem ei;
		CreatureOfRPGs ah;
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

	@Override
	public void onEquip(GModality gm) {
		gm.addGameObject(this);
	}

	@Override
	public void onUnEquipping(GModality gm) {
		gm.removeGameObject(this);
	}

	//

	protected void assignID() {
		ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
	}

	/**
	 * Override designed.<br>
	 * Update one or more {@link CreatureAttributes}'s attribute.<br>
	 * Default implementation update just one value, but multiple values at once
	 * could be modified.
	 */
	protected void updateAttributes(GModality gm, EquipmentItem ei, CreatureOfRPGs ah, CreatureAttributes ca) {
		ca.removeAttributeModifier(this.attributeToModify);
		updateAttributeModifiersAmount(gm, ei, ah, ca);
		ca.applyAttributeModifier(this.attributeToModify);
	}

	//

	public abstract void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureOfRPGs ah,
			CreatureAttributes ca);
}