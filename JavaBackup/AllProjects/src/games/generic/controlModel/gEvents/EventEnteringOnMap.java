package games.generic.controlModel.gEvents;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.gObj.ObjectInSpace;

public class EventEnteringOnMap extends EventOnMap {
	private static final long serialVersionUID = 562151896502L;

	public EventEnteringOnMap(ObjectInSpace objectInvolved, ObjectNamed eventType) {
		super(objectInvolved, eventType != null ? eventType : ExampleGameEvents.ObjectAdded);
		this.objectInvolved = objectInvolved;
	}

	@Override
	public boolean isOnMapEventType(ObjectNamed eventType) {
		return eventType.getName() == ExampleGameEvents.ObjectAdded.getName();
	}
}