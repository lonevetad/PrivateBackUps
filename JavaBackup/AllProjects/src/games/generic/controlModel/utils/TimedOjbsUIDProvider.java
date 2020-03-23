package games.generic.controlModel.utils;

import games.generic.UniqueIDProvider;

public class TimedOjbsUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider toBase;

	public TimedOjbsUIDProvider() {
		toBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return toBase.getNewID();
	}
}