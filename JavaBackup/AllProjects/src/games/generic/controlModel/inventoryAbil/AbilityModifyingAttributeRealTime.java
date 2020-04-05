package games.generic.controlModel.inventoryAbil;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.misc.AbilityTimedGeneric;
import games.generic.controlModel.misc.AttributeIdentifier;
import games.generic.controlModel.misc.CreatureAttributes;
import tools.UniqueIDProvider;

/** This class is originally designed as */
public abstract class AbilityModifyingAttributeRealTime implements AbilityTimedGeneric, EquipItemAbility {
	private static final long serialVersionUID = 56132035015L;
	public static final long MILLISEC_ATTRIBUTE_UPDATE = 500;

	public AbilityModifyingAttributeRealTime() {
		super();
		assignID();
	}

	public AbilityModifyingAttributeRealTime(AttributeIdentifier attributeModified, String name) {
		this();
		if (attributeModified != null)
			this.attributeToModify = new AttributeModification(attributeModified, 0);
		this.name = name;
	}

	protected long accTimeElapsed;
	protected Integer ID;
	protected String name;
	protected AttributeModification attributeToModify;
	protected EquipmentItem equipItem;

	public AttributeModification getAttributeToModify() {
		return attributeToModify;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name; // attributeToModify.getAttributeModified().getName();
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

	protected void assignID() {
		ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
	}

	/**
	 * Override designed.<br>
	 * Update one or more {@link CreatureAttributes}'s attribute.<br>
	 * Default implementation update just one value, but multiple values at once
	 * could be modified.
	 */
	protected void updateAttributes(GModality gm, EquipmentItem ei, CreatureSimple ah, CreatureAttributes ca) {
		ca.removeAttributeModifier(this.attributeToModify);
		updateAttributeModifiersAmount(gm, ei, ah, ca);
		ca.applyAttributeModifier(this.attributeToModify);
	}

	//

	public abstract void updateAttributeModifiersAmount(GModality gm, EquipmentItem ei, CreatureSimple ah,
			CreatureAttributes ca);
}