package games.generic.controlModel.misc.uidp;

import tools.UniqueIDProvider;

public class ObjInSpaceUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider oisBase;

	public ObjInSpaceUIDProvider() {
		oisBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return oisBase.getNewID();
	}
}