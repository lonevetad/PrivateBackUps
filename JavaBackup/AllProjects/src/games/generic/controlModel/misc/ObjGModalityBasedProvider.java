package games.generic.controlModel.misc;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.ObjectNamed;
import tools.Comparators;

public class ObjGModalityBasedProvider<E extends ObjectNamed> {
	protected MapTreeAVL<String, FactoryObjGModalityBased<E>> objsByName;

	public ObjGModalityBasedProvider() {
		this.objsByName = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
	}

	public void addObj(String name, FactoryObjGModalityBased<E> gm) {
		this.objsByName.put(name, gm);
	}

	public FactoryObjGModalityBased<E> getObjIdentifiedByName(String name) {
		return this.objsByName.get(name);
	}

	public FactoryObjGModalityBased<E> getAtIndex(int index) {
		return this.objsByName.getAt(index).getValue();
	}

	public Map<String, FactoryObjGModalityBased<E>> getObjectsIdentified() {
		return this.objsByName;
	}

	public int getObjectsIdentifiedCount() {
		return this.objsByName.size();
	}

	public void removeALl() {
		this.objsByName.clear();
	}
}