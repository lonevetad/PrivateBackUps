package games.generic.controlModel.misc.uidp;

import tools.UniqueIDProvider;

public class GMapUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider gmpapBase;

	public GMapUIDProvider() {
		gmpapBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return gmpapBase.getNewID();
	}
}