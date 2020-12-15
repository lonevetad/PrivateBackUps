package games.theRisingAngel.misc;

import java.util.Random;

import games.generic.controlModel.misc.RarityHolder;
import tools.ObjWithRarityWeight;
import tools.RandomWeightedIndexes;

public enum EquipItemRaritiesTRAn implements RarityHolder, ObjWithRarityWeight {
	Scrap(200), Common(750), WellManifactured(280), HighQuality(150), Rare(60), Epic(25), Legendary(10);

	protected int howMuchEasy;

	EquipItemRaritiesTRAn() { this(0); }

	EquipItemRaritiesTRAn(int h) { setRarityWeight(h); }

	@Override
	public int getRarityIndex() { return ordinal(); }

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) { return this; }

	@Override
	public int getRarityWeight() { return this.howMuchEasy; }

	@Override
	public ObjWithRarityWeight setRarityWeight(int weight) {
		this.howMuchEasy = weight > 0 ? weight : 0;
		return this;
	}

	//

	private static RandomWeightedIndexes RANDOM_WEIGTHED_INDEXES = null;

	private static void checkAndReinstanceRWI(Random r) {
		if (RANDOM_WEIGTHED_INDEXES == null) {
			RANDOM_WEIGTHED_INDEXES = new RandomWeightedIndexes(EquipItemRaritiesTRAn.values(), r);
		}
	}

	private static void checkAndReinstanceRWI(long seed) {
		if (RANDOM_WEIGTHED_INDEXES == null) {
			RANDOM_WEIGTHED_INDEXES = new RandomWeightedIndexes(EquipItemRaritiesTRAn.values(), new Random(seed));
		}
	}

	public static RandomWeightedIndexes toWeightedIndexes() {
		checkAndReinstanceRWI(new Random());
		return RANDOM_WEIGTHED_INDEXES;
	}

	public static RandomWeightedIndexes toWeightedIndexes(long seed) {
		checkAndReinstanceRWI(seed);
		return RANDOM_WEIGTHED_INDEXES;
	}

	public static RandomWeightedIndexes toWeightedIndexes(Random r) {
		checkAndReinstanceRWI(r);
		return RANDOM_WEIGTHED_INDEXES;
	}
}