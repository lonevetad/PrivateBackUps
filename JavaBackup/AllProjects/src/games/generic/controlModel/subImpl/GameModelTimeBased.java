package games.generic.controlModel.subImpl;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GameModel;
import games.generic.controlModel.TimedObject;
import tools.Comparators;

public abstract class GameModelTimeBased extends GameModel {

	protected Map<Integer, TimedObject> timedObjects;

	public GameModelTimeBased() {
		super();
		this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
	}

	//

	public Map<Integer, TimedObject> getTimedObjects() {
		return timedObjects;
	}

	//

	//

	public void addTimeProgressingObject(TimedObject to) {
		this.timedObjects.put(to.getID(), to);
	}
}