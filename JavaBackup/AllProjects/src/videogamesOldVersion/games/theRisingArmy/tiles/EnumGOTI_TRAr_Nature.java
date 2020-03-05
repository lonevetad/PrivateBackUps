package games.theRisingArmy.tiles;

import common.EnumGameObjectTileImage;
import common.abstractCommon.AbstractEnumElementGOTI;
import common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import common.abstractCommon.AbstractEnumGOTI;
import common.abstractCommon.MainController;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import games.theRisingArmy.MainController_TheRisingArmy;

public class EnumGOTI_TRAr_Nature extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -63300807044126307L;

	/// TODO START THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	private static EnumGOTI_TRAr_Nature instance = null;

	public EnumGOTI_TRAr_Nature() {
		super(Tiles_TRAr_Nature.values());
	}

	public static enum Tiles_TRAr_Nature implements AbstractEnumElementGameObjectTileImage_Delegating {
		// @SuppressWarnings("deprecation")
		// ZZZExampleTilemapSubclass_DO_NOT_USE_ME((main, imageFilename, b, ss) -> new
		// Example_TileMapSubclass(main, imageFilename, b, ss)),
		//

		// VEGETABLES - plant

		GrassSimple(AbstractEnumGOTI.newDefaultElement().setName("Grass Simple").setNotSolid(true)), //
		GrassLightFlower(AbstractEnumGOTI.newDefaultElement().setName("Grass Light Flower").setNotSolid(true)), //
		GrassFatSimple(AbstractEnumGOTI.newDefaultElement().setName("Grass fat Simple").setNotSolid(true)), //
		GrassFatCornerRightBottom(
				AbstractEnumGOTI.newDefaultElement().setName("Grass fat Corner RightBottom").setNotSolid(true)), //
		GrassFatSideRight(AbstractEnumGOTI.newDefaultElement().setName("Grass fat Side Right").setNotSolid(true)), //
		TinyRockPixel("Tiny Rock Pixel"), //
		TinyCactusPixel("Tiny Cactus Pixel"), //
		TinyCactusPixelWithFlower("Tiny Cactus Pixel With Flower"), //
		TreeOakPixelSimpleLittle("Tree Oak Pixel Simple Little"), //
		TreeOakPixelSimpleLittler(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE / 2, MainController.MICROPIXEL_EACH_TILE / 2))
						.setName("Tree Oak Pixel Simple Littler")), //
		TrunkWithRootsPixel(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 2))
						.setName("Trunk With Roots Pixel")), //
		TrunkWithRootsTinyPixel("Trunk With Roots Tiny Pixel"), //
		CherryTree(AbstractEnumGOTI.newDefaultElement(ShapeSpecification.newRectangle(true,
				MainController.MICROPIXEL_EACH_TILE * 3 / 2, MainController.MICROPIXEL_EACH_TILE * 3 / 2))
				.setName("Cherry Tree")), //

		// WATER
		WaterfallWall(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 2))
						.setName("Waterfall Wall")), //
		SeaWaveLateral(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 4))
						.setName("Sea Wave Lateral")), //
		BeachWavingTop1("Beach Waving Top 1"), //
		BeachWavingTop2("Beach Waving Top 2"), //
		BeachWavingTop3("Beach Waving Top 3"), //

		//

		// OTHER

		SandDesertPixel1(AbstractEnumGOTI.newDefaultElement().setName("Sand Desert Pixel 1").setNotSolid(true)), //
		SandDesertPixel2(AbstractEnumGOTI.newDefaultElement().setName("Sand Desert Pixel 2").setNotSolid(true)), //
		SandWallDesertPixel("Sand Wall Desert Pixel"), //
		RockWallBrownSimple("Rock Wall Brown Simple"), //
		LavaTiny("Lava Tiny")//

		;
		//

		Tiles_TRAr_Nature() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Nature(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Nature(AbstractEnumElementGOTI deleg) {
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

	} // TODO END ENUM

	public static EnumGOTI_TRAr_Nature getInstance() {
		return instance == null ? (instance = new EnumGOTI_TRAr_Nature()) : instance;
	}

	/// TODO END THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	public static void main(String[] args) {
		EnumGOTI_TRAr_Nature tl;
		MainController_TheRisingArmy mtrar;
		// LoaderGeneric loader;

		mtrar = new MainController_TheRisingArmy();
		tl = new EnumGOTI_TRAr_Nature();
		// loader = mtrar.getLoader();
		EnumGameObjectTileImageCollection_TRAr
				.printTM(tl.newTileImage(mtrar, Tiles_TRAr_Nature.WaterfallWall.getName()));
		EnumGameObjectTileImageCollection_TRAr.printTM(tl.newTileImage(mtrar, Tiles_TRAr_Nature.GrassSimple.getName()));
		System.out.println("\n\n now re-search water and example:\n");
		EnumGameObjectTileImageCollection_TRAr
				.printTM(tl.newTileImage(mtrar, Tiles_TRAr_Nature.WaterfallWall.getName()));
		EnumGameObjectTileImageCollection_TRAr.printTM(tl.newTileImage(mtrar, Tiles_TRAr_Nature.GrassSimple.getName()));
	}

}