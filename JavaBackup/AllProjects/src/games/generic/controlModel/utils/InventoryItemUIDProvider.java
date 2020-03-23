package games.generic.controlModel.utils;

import games.generic.UniqueIDProvider;

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