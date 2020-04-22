package videogamesOldVersion.games.theRisingArmy.cards;

import videogamesOldVersion.common.GameObjectInMap.MementoGameObjectInMap;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import videogamesOldVersion.games.theRisingArmy.cards.permanentsAndCreatures.PermanentTRAR;
import videogamesOldVersion.games.theRisingArmy.main.SharedStuffs_TRAr.SuperTypePlayableObjectTRAr;

public class CrystalManaSourceTRAr extends PermanentTRAR {
	private static final long serialVersionUID = 4891091L;

	public CrystalManaSourceTRAr() {
		super();
		addSupertype(null);
	}

	public CrystalManaSourceTRAr(ShapeSpecification ss) {
		super(ss);
		addSupertype(null);
	}

	public CrystalManaSourceTRAr(CrystalManaSourceTRAr o) {
		super(o);
	}

	public CrystalManaSourceTRAr(MementoGameObjectInMap o) {
		super(o);
		addSupertype(null);
	}

	@Override
	public CrystalManaSourceTRAr addSupertype(SuperTypePlayableObjectTRAr playableObjectTypeTRAr) {
		if (super.getSupertypes() != null && super.getSupertypes().isEmpty())
			super.addSupertype(SuperTypePlayableObjectTRAr.CrystalManaSource);
		return this;
	}
}