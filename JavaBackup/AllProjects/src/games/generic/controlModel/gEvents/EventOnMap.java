package games.generic.controlModel.gEvents;

import games.generic.controlModel.ObjectNamed;
import games.generic.controlModel.gObj.ObjectInSpace;
import games.generic.controlModel.subimpl.GEvent;

public abstract class EventOnMap extends GEvent {
	private static final long serialVersionUID = 562151896502L;

	public EventOnMap(ObjectInSpace objectInvolved, ObjectNamed eventType) {
		super();
		this.objectInvolved = objectInvolved;
		if (!isOnMapEventType(eventType))
			throw new IllegalArgumentException(
					"The given event type is not compatible for \"in map\" events: " + eventType);
		this.eventType = eventType;
	}

	protected ObjectNamed eventType;
	protected ObjectInSpace objectInvolved;

	public ObjectInSpace getObjectInvolved() {
		return objectInvolved;
	}

	public abstract boolean isOnMapEventType(ObjectNamed eventType);

	@Override
	public String getName() {
		return eventType.getName();
	}
}