package games.theRisingArmy.tiles;

import common.EnumGameObjectTileImage;
import common.abstractCommon.AbstractEnumElementGOTI;
import common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import common.abstractCommon.AbstractEnumGOTI;

public class EnumGOTI_TRAr_Battlefield extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -106845094L;

	private static EnumGOTI_TRAr_Battlefield instance = null;

	public EnumGOTI_TRAr_Battlefield() {
		super(Tiles_TRAr_Battlefield.values());
	}

	public static enum Tiles_TRAr_Battlefield implements AbstractEnumElementGameObjectTileImage_Delegating {
		ArmorRusted("Armor Rusted"), //
		PileWeapons(AbstractEnumGOTI.newDefaultElement().setName("Pile Weapons").setNotSolid(true)), //
		SoldierDead(AbstractEnumGOTI.newDefaultElement().setName("Soldier Dead").setNotSolid(true)), //
		ShieldBroken(AbstractEnumGOTI.newDefaultElement().setName("Shield Broken").setNotSolid(true)), //
		CatapultBroken("Catapult Broken"), //
		SoldierSkull(AbstractEnumGOTI.newDefaultElement().setName("Soldier Skull").setNotSolid(true)), //
		Flag, //
		FlagBroken("Flag Broken"), //
		SwordOnLand(AbstractEnumGOTI.newDefaultElement().setName("Sword On Land").setNotSolid(true))//
		;

		Tiles_TRAr_Battlefield() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Battlefield(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Battlefield(AbstractEnumElementGOTI deleg) {
			setDelegated(deleg);
			deleg.setID(ordinal());
		}

		private AbstractEnumElementGOTI delegated;

		@Override
		public AbstractEnumElementGOTI getDelegated() {
			return delegated;
		}

		@Override
		public AbstractEnumElementGameObjectTileImage_Delegating setDelegated(AbstractEnumElementGOTI delegated) {
			this.delegated = delegated;
			return this;
		}
	} // TODO
		// END
		// ENUM

	public static EnumGOTI_TRAr_Battlefield getInstance() {
		return instance == null ? (instance = new EnumGOTI_TRAr_Battlefield()) : instance;
	}
}
