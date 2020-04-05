package dataStructures.isom.matrixBased;

import dataStructures.isom.NodeIsom;
import tools.ObjectWithID;

public abstract class NodeIsomSingleObj extends NodeIsom {
	private static final long serialVersionUID = 4052487990441744L;

	protected ObjectWithID objectLying;

//

//

	public tools.ObjectWithID getObjectLying() {
		return objectLying;
	}

	public void setObjectLying(tools.ObjectWithID objectLying) {
		this.objectLying = objectLying;
	}

	@Override
	public boolean addObject(ObjectWithID o) {
		setObjectLying(o);
		return true;
	}
}