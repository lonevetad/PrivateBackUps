package games.generic.controlModel;

import java.util.Set;

import dataStructures.MapTreeAVL;
import tools.Comparators;

public abstract class GameModel {
	protected MapTreeAVL<Integer, ObjectWIthID> allObjects_BackMap;
	protected Set<ObjectWIthID> allObjects;

	public GameModel() {
		this.allObjects_BackMap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
		this.allObjects = this.allObjects_BackMap.toSetValue(o -> o.getID());
		onCreate();
	}

	//

	//

	public abstract void onCreate();

}