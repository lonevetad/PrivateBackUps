package games.generic.controlModel.subImpl;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GModel;
import games.generic.controlModel.gameObj.TimedObject;
import tools.Comparators;

public abstract class GameModelTimeBased extends GModel {

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

	public void addTimedObject(TimedObject to) {
		this.timedObjects.put(to.getID(), to);
	}
}