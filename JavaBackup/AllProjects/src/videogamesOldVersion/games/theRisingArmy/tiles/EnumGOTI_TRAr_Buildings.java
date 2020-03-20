package videogamesOldVersion.games.theRisingArmy.tiles;

import videogamesOldVersion.common.EnumGameObjectTileImage;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import videogamesOldVersion.common.abstractCommon.AbstractEnumGOTI;

public class EnumGOTI_TRAr_Buildings extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -63300807044126309L;

	/// TODO START THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	private static EnumGOTI_TRAr_Buildings instance = null;

	public EnumGOTI_TRAr_Buildings() {
		super(Tiles_TRAr_Buildings.values());
	}

	public static enum Tiles_TRAr_Buildings implements AbstractEnumElementGameObjectTileImage_Delegating {

		StreetRockGray(AbstractEnumGOTI.newDefaultElement().setName("Street Rock Gray").setNotSolid(true))//
		, StreetRockGrayCornerRightBottom(
				AbstractEnumGOTI.newDefaultElement().setName("Street Rock Gray Corner Right Bottom").setNotSolid(true))//
		, StreetRockGraySideRight(
				AbstractEnumGOTI.newDefaultElement().setName("Street Rock Gray Side Right").setNotSolid(true))//
		, StreetRockMoss1(AbstractEnumGOTI.newDefaultElement().setName("Street Rock Moss 1").setNotSolid(true))//
		, StreetRockMoss2(AbstractEnumGOTI.newDefaultElement().setName("Street Rock Moss 2").setNotSolid(true))//
		, StreetRockMoss3(AbstractEnumGOTI.newDefaultElement().setName("Street Rock Moss 3").setNotSolid(true))//
		, WallRockGraySimple("Wall Rock Gray Simple")//
		, WallMassBrown("Wall Mass Brown")//
		, WallMassBrownMultiple("Wall Mass Brown Multiple")//

		//
		;

		Tiles_TRAr_Buildings() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Buildings(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Buildings(AbstractEnumElementGOTI deleg) {
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
	}
	// TODO END ENUM

	public static EnumGOTI_TRAr_Buildings getInstance() {
		return instance == null ? (instance = new EnumGOTI_TRAr_Buildings()) : instance;
	}
}
