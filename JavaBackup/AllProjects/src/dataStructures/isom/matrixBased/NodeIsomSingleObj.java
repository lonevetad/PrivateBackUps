package dataStructures.isom.matrixBased;

import dataStructures.isom.NodeIsom;
import geometry.ObjectLocated;

public abstract class NodeIsomSingleObj extends NodeIsom {
	private static final long serialVersionUID = 4052487990441744L;

	protected ObjectLocated objectLying;

//

//

	public ObjectLocated getObjectLying() {
		return objectLying;
	}

	public void setObjectLying(ObjectLocated objectLying) {
		this.objectLying = objectLying;
	}

	@Override
	public boolean addObject(ObjectLocated o) {
//		setObjectLying(o);
		this.objectLying = o;
		return true;
	}
}