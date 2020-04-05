package games.generic.controlModel.utils.uidp;

import tools.UniqueIDProvider;

public class EventObserverUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider eoBase;

	public EventObserverUIDProvider() {
		eoBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return eoBase.getNewID();
	}
}