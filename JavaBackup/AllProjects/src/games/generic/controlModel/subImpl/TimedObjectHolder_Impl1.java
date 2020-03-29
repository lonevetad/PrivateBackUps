package games.generic.controlModel.subImpl;

import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.ObjectWithID;
import games.generic.controlModel.gObj.TimedObject;
import tools.Comparators;

public class TimedObjectHolder_Impl1 extends TimedObjectHolder {

	protected MapTreeAVL<Integer, ObjectWithID> timedObjects;
	protected Set<ObjectWithID> timedObjects_Set;

	public TimedObjectHolder_Impl1() {
		this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		this.timedObjects_Set = this.timedObjects.toSetValue(ObjectWithID.KEY_EXTRACTOR);
	}

	@Override
	public Set<ObjectWithID> getObjects() {
		return timedObjects_Set;
	}

	@Override
	public boolean add(ObjectWithID o) {
		if (o == null || (!(o instanceof TimedObject)))
			return false;
		if (timedObjects.containsKey(o.getID()))
			return false;
		timedObjects.put(o.getID(), o);
		return true;
	}

	@Override
	public boolean remove(ObjectWithID o) {
		if (timedObjects.containsKey(o.getID())) {
			timedObjects.remove(o.getID());
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll() {
		this.timedObjects.clear();
		return true;
	}

	@Override
	public boolean contains(ObjectWithID o) {
		return timedObjects.containsKey(o.getID());
	}

	@Override
	public ObjectWithID get(Integer id) {
		return timedObjects.get(id);
	}

	@Override
	public void forEach(Consumer<ObjectWithID> action) {
		timedObjects.forEach((id, to) -> action.accept(to));
	}
}