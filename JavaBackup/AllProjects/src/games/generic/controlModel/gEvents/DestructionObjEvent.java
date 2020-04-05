package games.generic.controlModel.gEvents;

import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.subimpl.GEvent;

public class DestructionObjEvent extends GEvent {

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
	public String getName() {
		return this.name;
	}

	public boolean isDestructionValid() {
		return isDestructionValid;
	}

	public DestructibleObject getDestructibleObject() {
		return destructibleObject;
	}

	//

	public void setDestructionValid(boolean isDestructionValid) {
		this.isDestructionValid = isDestructionValid;
	}

	public void setDestructibleObject(DestructibleObject destructibleObject) {
		this.destructibleObject = destructibleObject;
	}
}