package dataStructures.isom.matrixBased;

import java.io.Serializable;
import java.util.Set;

import dataStructures.isom.ObjectWithID;

public abstract class NodeIsom<IDowid> implements Serializable {
	private static final long serialVersionUID = 4052487990441743L;

	public NodeIsom() {
	}

	protected int x, y;

//

//

	/**
	 * Add the given object to this node. This node could store just a single object
	 * or a {@link Set }, it depends on implementation.
	 */
	public abstract boolean addObject(ObjectWithID<IDowid> o);

}