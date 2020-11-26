package games.theRisingAngel.misc;

import games.generic.controlModel.misc.RarityHolder;
import tools.ObjWithRarityWeight;

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
}