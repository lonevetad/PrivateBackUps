package games.generic.controlModel.misc.uidp;

import tools.UniqueIDProvider;

public class EventUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider eventBase;

	public EventUIDProvider() {
		eventBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return eventBase.getNewID();
	}
}