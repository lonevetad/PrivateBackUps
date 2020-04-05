package videogamesOldVersion.common;

import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import videogamesOldVersion.common.abstractCommon.AbstractEnumGOTI;
import videogamesOldVersion.common.abstractCommon.MainController;

public class EnumTileImage_TRAr_Empty extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -633008070441263088L;

	/// TODO START THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	private static EnumTileImage_TRAr_Empty instance = null;

	public EnumTileImage_TRAr_Empty() {
		super(Tiles_TRAr_Empty.values());
	}

	public static enum Tiles_TRAr_Empty implements AbstractEnumElementGameObjectTileImage_Delegating {

		NOTHING("Just nothing"), //
		SOLIDITY(AbstractEnumGOTI.newDefaultElement().setName("SomethingWithASolidity").setNotSolid(true)), //
		RECTANGLE_GREATER_SHAPE(AbstractEnumGOTI
				.newDefaultElement(ShapeSpecification.newRectangle(true, MainController.MICROPIXEL_EACH_TILE * 2,
						MainController.MICROPIXEL_EACH_TILE * 2))
				.setName("SomethingWithARectangleShapeGreaterThanNormal")), //
		//
		;

		Tiles_TRAr_Empty() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Empty(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Empty(AbstractEnumElementGOTI deleg) {
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

	public static EnumTileImage_TRAr_Empty getInstance() {
		return instance == null ? (instance = new EnumTileImage_TRAr_Empty()) : instance;
	}

	//
}