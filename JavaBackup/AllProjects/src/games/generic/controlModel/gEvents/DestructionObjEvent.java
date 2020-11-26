package games.generic.controlModel.gEvents;

import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import games.generic.controlModel.inventoryAbil.EquipmentItem;

public class DestructionObjEvent extends GEvent {
	private static final long serialVersionUID = 50320237291343862L;

	public DestructionObjEvent(DestructibleObject desObj, String name) {
		super();
		this.isDestructionValid = true;
		this.destructibleObject = desObj;
		this.name = name;
	}

	protected boolean isDestructionValid;
	protected DestructibleObject destructibleObject;
	protected String name;

	@Override
	public String getName() { return this.name; }

	public boolean isDestructionValid() { return isDestructionValid; }

	public DestructibleObject getDestructibleObject() { return destructibleObject; }

	// damageReductedByArmour

	/**
	 * Mark if the target must be destroyed or not (i.e., if a
	 * {@link DestructibleObject} should invoke
	 * {@link DestructibleObject#destroy()}).<br>
	 * Some objects/abilities (like {@link EquipmentItem} and/or
	 * {@link AbilityGeneric}) could sacrifice themselves instead of the target (the
	 * owner?). This could be applied by setting this value as <code>false</code>.
	 */
	public void setDestructionValid(boolean isDestructionValid) { this.isDestructionValid = isDestructionValid; }

	public void setDestructibleObject(DestructibleObject destructibleObject) {
		this.destructibleObject = destructibleObject;
	}

	@Override
	public boolean isRequirigImmediateProcessing() { return true; }
}