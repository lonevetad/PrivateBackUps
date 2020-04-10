package dataStructures.isom.matrixBased;

import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.isom.NodeIsom;
import geometry.ObjectLocated;

public class NodeIsomSingleObj extends NodeIsom {
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

	@Override
	public boolean isWalkable(Predicate<ObjectLocated> isWalkableTester) {
		return isWalkableTester == null || isWalkableTester.test(objectLying);
	}

	@Override
	public void forEachHeldObject(Consumer<ObjectLocated> action) {
		action.accept(objectLying);
	}
}