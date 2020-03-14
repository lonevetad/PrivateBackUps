package games.generic.controlModel.subImpl;

import java.util.Map;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GameModel;
import games.generic.controlModel.TimeProgressingObject;
import tools.Comparators;

public class GameModelTimeBased extends GameModel {

	protected Map<Integer, TimeProgressingObject> timedObjects;

	public GameModelTimeBased() {
		super();
		this.timedObjects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.INTEGER_COMPARATOR);
	}

	//

	public Map<Integer, TimeProgressingObject> getTimedObjects() {
		return timedObjects;
	}

	//

	@Override
	public void onCreate() {
	}

	//

	public void addTimeProgressingObject(TimeProgressingObject to) {
		this.timedObjects.put(to.getID(), to);
	}
}