package dataStructures.isom.matrixBased;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import dataStructures.isom.NodeIsom;
import geometry.ObjectLocated;

public class NodeIsomSingleObj extends NodeIsom {
	private static final long serialVersionUID = 4052487990441744L;

	public NodeIsomSingleObj() {
		super();
	}

	public NodeIsomSingleObj(int x, int y) {
		super(x, y);
	}

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
	public int countObjectAdded() {
		return objectLying == null ? 0 : 1;
	}

	@Override
	public ObjectLocated getObject(int i) {
		return objectLying;
	}

	@Override
	public ObjectLocated getObject(Integer ID) {
		return objectLying;
	}

	@Override
	public boolean removeObject(Integer ID) {
		if (ID == null || this.objectLying == null || (!ID.equals(this.objectLying.getID()))) {
			return false;
		}
		this.objectLying = null;
		return true;
	}

	@Override
	public boolean removeObject(ObjectLocated o) {
		if (this.objectLying != o) {
			return false;
		}
		this.objectLying = null;
		return true;
	}

	@Override
	public boolean removeAllObjects() {
		this.objectLying = null;
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

	@Override
	public Iterator<ObjectLocated> iterator() {
		return new IteratorNodeIsom();
	}

	protected class IteratorNodeIsom implements Iterator<ObjectLocated> {
		ObjectLocated temp;

		protected IteratorNodeIsom() {
			temp = objectLying;
		}

		@Override
		public boolean hasNext() {
			return temp != null;
		}

		@Override
		public ObjectLocated next() {
			ObjectLocated o;
			o = temp;
			temp = null;
			return o;
		}
	}
}