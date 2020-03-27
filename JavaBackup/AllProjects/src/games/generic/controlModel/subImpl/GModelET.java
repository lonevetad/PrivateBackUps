package games.generic.controlModel.subImpl;

import games.generic.controlModel.GEventManager;

public class GModelET extends GameModelTimeBased {
	public static final String EVENT_MANAGER_OBSERVERS_HOLDER_NAME = "emo"; // event manager observers

	public GModelET() {
		super();
	}

	protected GEventManager eventManager;

	@Override
	public void onCreate() {
		// nothing to do
	}

	public void setEventManager(GEventManager eventManager) {
		this.eventManager = eventManager;
		super.addObjHolder(EVENT_MANAGER_OBSERVERS_HOLDER_NAME, eventManager);
	}
}