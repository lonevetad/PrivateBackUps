package games.generic.controlModel.misc.uidp;

import tools.UniqueIDProvider;

public class InventoryItemUIDProvider implements UniqueIDProvider {
	private static final UniqueIDProvider singleton = UniqueIDProvider.newBasicIDProvider();

	public static Integer newID() {
		return singleton.getNewID();
	}

	protected final UniqueIDProvider.BaseUniqueIDProvider iiBase;

	public InventoryItemUIDProvider() {
		iiBase = new BaseUniqueIDProvider();
	}

	@Override
	public Integer getNewID() {
		return iiBase.getNewID();
	}
}