package games.generic.controlModel.subImpl;

import java.util.function.Consumer;

import games.generic.controlModel.GModel;
import games.generic.controlModel.gameObj.TimedObject;

public abstract class GameModelTimeBased extends GModel {
	public static final String TIMED_OBJECT_HOLDER_NAME = "toh";

	public GameModelTimeBased() {
		super();
		this.timedObjectHolder = newTimedObjectHolder();
		super.addObjHolder(TIMED_OBJECT_HOLDER_NAME, this.timedObjectHolder);
	}

	TimedObjectHolder timedObjectHolder;

	//

	/** Override designed */
	public TimedObjectHolder newTimedObjectHolder() {
		return new TimedObjectHolder_Impl1();
	}

	//

	//

	public void addTimedObject(TimedObject to) {
		super.add(to);
	}

//	public Set<TimedObject> getTimedObjects() { return timedObjectHolder.getObjects()  }

	public void forEachTimedObject(Consumer<TimedObject> action) {
		this.timedObjectHolder.getObjects().forEach(owidButItsTO -> {
			action.accept((TimedObject) owidButItsTO);
		});
	}
}