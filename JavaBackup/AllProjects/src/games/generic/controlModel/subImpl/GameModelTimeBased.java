package games.generic.controlModel.subImpl;

import java.util.Set;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModel;
import games.generic.controlModel.GObjectsHolder;
import games.generic.controlModel.gameObj.TimedObject;
import tools.Comparators;

public abstract class GameModelTimeBased extends GModel {
	protected static final String NAME_TIMED_OBJECT_HOLDER = "toh";
	TimedObjectHolder timedObjectHolder;

	public GameModelTimeBased() {
		super();
//		this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
//				Comparators.INTEGER_COMPARATOR);
		this.timedObjectHolder = newTimedObjectHolder();
		super.addObjHolder(NAME_TIMED_OBJECT_HOLDER, this.timedObjectHolder);
	}

	//

	public Set<TimedObject> getTimedObjects() {
		return timedObjectHolder.getObjects();
	}

	/** Override designed */
	public TimedObjectHolder newTimedObjectHolder() {
		return new TimedObjectHolder_Impl1();
	}

	//

	//

	public void addTimedObject(TimedObject to) {
		super.add(to);
//		this.timedObjects.put(to.getID(), to);
	}

	//

	//

	protected abstract class TimedObjectHolder implements GObjectsHolder<TimedObject> {
	}

	protected class TimedObjectHolder_Impl1 extends TimedObjectHolder {
		protected MapTreeAVL<Integer, TimedObject> timedObjects;
		protected Set<TimedObject> timedObjects_Set;

		public TimedObjectHolder_Impl1() {
			super();
			this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
					Comparators.INTEGER_COMPARATOR);
		}

		@Override
		public Set<TimedObject> getObjects() {
			return timedObjects_Set;
		}

		@Override
		public boolean add(TimedObject o) {
			if (timedObjects.containsKey(o.getID()))
				return false;
			timedObjects.put(o.getID(), o);
			return true;
		}

		@Override
		public boolean remove(TimedObject o) {
			if (timedObjects.containsKey(o.getID())) {
				timedObjects.remove(o.getID());
				return true;
			}
			return false;
		}

		@Override
		public boolean contains(TimedObject o) {
			return timedObjects.containsKey(o.getID());
		}

		@Override
		public void forEach(Consumer<TimedObject> action) {
			timedObjects.forEach((id, to) -> action.accept(to));
		}

	}
}