package videogamesOldVersion.games.theRisingArmy.tiles;

import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import videogamesOldVersion.common.EnumGameObjectTileImage;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGOTI;
import videogamesOldVersion.common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import videogamesOldVersion.common.abstractCommon.AbstractEnumGOTI;
import videogamesOldVersion.common.abstractCommon.MainController;

public class EnumGOTI_TRAr_Magics extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -63300807044126307L;

	/// TODO START THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	private static EnumGOTI_TRAr_Magics instance = null;

	public EnumGOTI_TRAr_Magics() {
		super(Tiles_TRAr_Magics.values());
	}

	public static enum Tiles_TRAr_Magics implements AbstractEnumElementGameObjectTileImage_Delegating {
		ShieldMagicSphere(AbstractEnumGOTI
				.newDefaultElement(ShapeSpecification.newCircle(true, MainController.MICROPIXEL_EACH_TILE * 3 / 2))
				.setName("Shield Magic Spheric"))//
		,
		BlackHoleExploding(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 2))
						.setName("Black Hole Exploding"))//
		, WaterSpiral("Water Spiral")//
		, HealBasic("Heal Basic")//
		,
		HealGreat(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 2))
						.setName("Heal Great"))//
		,
		FireballMicro(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE / 2, MainController.MICROPIXEL_EACH_TILE / 2))
						.setName("Fireball Micro"))//
		,
		ThunderLittle(AbstractEnumGOTI.newDefaultElement(ShapeSpecification.newRectangle(true,
				MainController.MICROPIXEL_EACH_TILE, MainController.MICROPIXEL_EACH_TILE * 2))
				.setName("Thunder Little"))//
		, IceStalactite("Ice Stalactite")//
		,
		FireColumn(
				AbstractEnumGOTI
						.newDefaultElement(ShapeSpecification.newRectangle(true,
								MainController.MICROPIXEL_EACH_TILE * 2, MainController.MICROPIXEL_EACH_TILE * 3))
						.setName("Fire Column"))//
		,
		ExplosionLittle(AbstractEnumGOTI
				.newDefaultElement(ShapeSpecification.newCircle(true, MainController.MICROPIXEL_EACH_TILE))
				.setName("Explosion Little"))//
		//
		;

		Tiles_TRAr_Magics() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Magics(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Magics(AbstractEnumElementGOTI deleg) {
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

	public static EnumGOTI_TRAr_Magics getInstance() {
		return instance == null ? (instance = new EnumGOTI_TRAr_Magics()) : instance;
	}
}
