package games.theRisingAngel.misc;

import games.generic.controlModel.misc.RarityHolder;

public enum EquipItemRarities implements RarityHolder {
	Scrap, Common, WellManifactured, HighQuality, Rare, Epic, Legendary;

	@Override
	public int getRarityIndex() {
		return ordinal();
	}

	@Override
	public RarityHolder setRarityIndex(int rarityIndex) {
		return this;
	}
}