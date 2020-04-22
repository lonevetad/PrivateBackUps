package games.theRisingArmy.tiles;

import common.EnumGameObjectTileImage;
import common.abstractCommon.AbstractEnumElementGOTI;
import common.abstractCommon.AbstractEnumElementGameObjectTileImage_Delegating;
import common.abstractCommon.AbstractEnumGOTI;

public class EnumGOTI_TRAr_Creatures extends EnumGameObjectTileImage {
	private static final long serialVersionUID = -63300807044126307L;

	/// TODO START THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES

	private static EnumGOTI_TRAr_Creatures instance = null;

	public EnumGOTI_TRAr_Creatures() {
		super(Tiles_TRAr_Creatures.values());
	}

	public static enum Tiles_TRAr_Creatures implements AbstractEnumElementGameObjectTileImage_Delegating {
		Rat
		// goblins
		, //
		GoblinSoldier("Goblin Soldier"), GoblinExplorer("Goblin Explorer"), GoblinChief("Goblin Chief"), GoblinLord(
				"Goblin Lord"), GoblinWarcaller("Goblin Warcaller")

		// elves
		, //
		ElfSoldier("Elf Soldier"), ElfExplorer("Elf Explorer"), ElfPriest("Elf Priest")
		//
		;

		Tiles_TRAr_Creatures() {
			this("none");
			delegated.setName(name());
		}

		Tiles_TRAr_Creatures(String imageFilename) {
			this(AbstractEnumGOTI.newDefaultElement().setName(imageFilename));
		}

		Tiles_TRAr_Creatures(AbstractEnumElementGOTI deleg) {
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

	public static EnumGOTI_TRAr_Creatures getInstance() {
		return instance == null ? (instance = new EnumGOTI_TRAr_Creatures()) : instance;
	}

	/// TODO END THE PART THAT IS EQUAL TO OTHER SIMILAR CLASSES
}