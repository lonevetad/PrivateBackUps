package games.theRisingAngel;

import tools.UniqueIDProvider;

public class CreatureUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider cBase;

	public CreatureUIDProvider() {
		cBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return cBase.getNewID();
	}
}