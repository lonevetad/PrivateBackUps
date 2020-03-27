package games.generic.controlModel;

import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import dataStructures.MapTreeAVL;
import games.generic.ObjectWithID;
import tools.Comparators;

public abstract class GModel implements GObjectsHolder {
	protected MapTreeAVL<Integer, ObjectWithID> allObjects_BackMap;
	protected Set<ObjectWithID> allObjects;
	protected MapTreeAVL<String, GObjectsHolder> objectsHoldersSpecialized;

	public GModel() {
		this.allObjects_BackMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		this.allObjects = this.allObjects_BackMap.toSetValue(ObjectWithID.KEY_EXTRACTOR);
		this.objectsHoldersSpecialized = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		onCreate();
	}

	//

	//

	public abstract void onCreate();

	/**
	 * BEWARE: returns just the object NOT held by some {@link GObjectsHolder} added
	 * via {@link #addObjHolder(String, GObjectsHolder)}.<br>
	 * To access those {@link ObjectWithID} instances , just use the
	 * {@link #get(Integer)} or {@link #forEach(Consumer)} as described in super
	 * documentation, inherited in the following paragraph.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Set<ObjectWithID> getObjects() {
		return allObjects;
	}

	@Override
	public boolean add(ObjectWithID o) {
		boolean added[];
		if (o == null)
			return false;
		System.out.println("############################################# ADDING OWID to model: " + o);
		/*
		 * to bypass the forEach restriction to non-pointers (i.e. non-final variables)
		 */
		added = new boolean[] { false };
		this.objectsHoldersSpecialized.forEach((s, h) -> {
			added[0] |= h.add(o);
		});
		if (!added[0]) {
			// no one has added it: so I add it
			this.allObjects.add(o);
		}
		return true;
	}

	@Override
	public boolean remove(ObjectWithID o) {
		RemoverOn_GObjectsHolder cc;
		if (o == null)
			return false;
		if (this.allObjects_BackMap.containsKey(o.getID()))
			return true;
//		if(this.objectsHoldersSpecialized.containsKey(o))
		cc = new RemoverOn_GObjectsHolder(o);
		this.objectsHoldersSpecialized.forEach(cc);
		return cc.removed;
	}

	@Override
	public boolean removeAll() {
		this.allObjects_BackMap.clear();
		this.objectsHoldersSpecialized.forEach((s, goh) -> goh.removeAll());
		return true;
	}

	@Override
	public boolean contains(ObjectWithID o) {
		if (o == null)
			return false;
		if (this.allObjects_BackMap.containsKey(o.getID()))
			return true;
//		if(this.objectsHoldersSpecialized.containsKey(o))
		for (Entry<String, GObjectsHolder> e : this.objectsHoldersSpecialized) {
			if (e.getValue().contains(o))
				return true;
		}
		return false;
	}

	@Override
	public ObjectWithID get(final Integer id) {
		ObjectWithID o;
		if (id == null)
			return null;
		o = this.allObjects_BackMap.get(id);
		if (o != null)
			return o;
		for (Entry<String, GObjectsHolder> e : this.objectsHoldersSpecialized) {
			ObjectWithID oo;
			oo = e.getValue().get(id);
			if (oo != null)
				return oo;
		}
		return null;
	}

	@Override
	public void forEach(final Consumer<ObjectWithID> action) {
		final MapTreeAVL<Integer, ObjectWithID> usedObjects;
		final Consumer<ObjectWithID> innerConsumer;
		usedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.Lightweight, Comparators.INTEGER_COMPARATOR);
		this.allObjects_BackMap.forEach((id, owid) -> {
			usedObjects.put(id, owid);
			action.accept(owid);
		});
		innerConsumer = owid -> {
			Integer id;
			id = owid.getID();
			if (!usedObjects.containsKey(id)) {
				usedObjects.put(id, owid);
				action.accept(owid);
			}
		};
		this.objectsHoldersSpecialized.forEach((str, goh) -> {
			goh.forEach(innerConsumer);
		});
	}

	/**
	 * Various classes may require (or be designed to) manage {@link ObjectWithID},
	 * but the collection of {@link ObjectWithID} should belong to the Model. A
	 * solution is to force those classes to implement {@link GObjectsHolder} so
	 * that the Model can really hold ALL data.<br>
	 * Those classes (that I repeat, are kind of "managers") can access to the
	 * desired instances through the Model itself (slow but correct, talking about
	 * the "Responsibility" question) or their "shortcut-pointers". Beware of the
	 * side effects of this last way: some subclasses of {@link GModel} may
	 * implements "code", triggers, controls, restrictions, etc and rely to
	 * particular methods like the {@link #add(ObjectWithID)} or
	 * {@link #remove(ObjectWithID)} overrides. Using those shortcuts may lead to
	 * bugs, usually hard to be found.
	 */
	public boolean addObjHolder(String nameIdentifier, GObjectsHolder goHolder) {
		if (!this.objectsHoldersSpecialized.containsKey(nameIdentifier)) {
			this.objectsHoldersSpecialized.put(nameIdentifier, goHolder);
			return true;
		}
		return false;
	}

	public boolean removeObjHolder(String nameIdentifier) {
		if (!this.objectsHoldersSpecialized.containsKey(nameIdentifier)) {
			this.objectsHoldersSpecialized.remove(nameIdentifier);
			return true;
		}
		return false;
	}

	public boolean containsObjHolder(String nameIdentifier) {
		return this.objectsHoldersSpecialized.containsKey(nameIdentifier);
	}

	public void forEachObjHolder(BiConsumer<String, GObjectsHolder> action) {
		this.objectsHoldersSpecialized.forEach(action);
	}

	public GObjectsHolder getObjHolder(String nameIdentifier) {
		return this.objectsHoldersSpecialized.get(nameIdentifier);
	}

	//

	//

	protected static class RemoverOn_GObjectsHolder implements BiConsumer<String, GObjectsHolder> {
		boolean removed;
		ObjectWithID target;

		public RemoverOn_GObjectsHolder(ObjectWithID target) {
			super();
			this.target = target;
			this.removed = false;
		}

		@Override
		public void accept(String t, GObjectsHolder goh) {
			removed |= goh.remove(target);
		}
	}
}