package videogamesOldVersion.games.theRisingArmy.cards.permanentsAndCreatures;

import videogamesOldVersion.common.GameObjectInMap.MementoGameObjectInMap;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import videogamesOldVersion.games.theRisingArmy.abstractTRAr.GameObjectTRAr;
import videogamesOldVersion.games.theRisingArmy.main.SharedStuffs_TRAr.SuperTypePlayableObjectTRAr;
import videogamesOldVersion.games.theRisingArmy.main.StatisticsOfPermanentTRAr;

/**
 * Rune, creature, artefatti, incantesimi, giocatori, tutto
 * <p>
 * Beware of transient variables
 */
public class PermanentTRAR extends GameObjectTRAr {
	private static final long serialVersionUID = -396309029217071L;

	public PermanentTRAR() {
		super();
	}

	public PermanentTRAR(ShapeSpecification ss) {
		super(ss);
	}

	public PermanentTRAR(PermanentTRAR o) {
		super(o);
	}

	public PermanentTRAR(MementoGameObjectInMap o) {
		super(o);
	}

	//

	public boolean canBeHealed, canAttack, canMove;
	transient int lifeNow, staminaNow;
	StatisticsOfPermanentTRAr statisticsOriginal;
	transient StatisticsOfPermanentTRAr statisticsNow;

	//

	// TODO GETTER

	public int getLifeNow() {
		return lifeNow;
	}

	public int getStaminaNow() {
		return staminaNow;
	}

	public StatisticsOfPermanentTRAr getStatisticsNow() {
		return statisticsNow;
	}

	public StatisticsOfPermanentTRAr getStatisticsOriginal() {
		return statisticsOriginal;
	}

	//

	// TODO SETTER

	public PermanentTRAR setLifeNow(int lifeNow) {
		this.lifeNow = lifeNow;
		return this;
	}

	public PermanentTRAR setStaminaNow(int staminaNow) {
		this.staminaNow = staminaNow;
		return this;
	}

	public PermanentTRAR setStatisticsNow(StatisticsOfPermanentTRAr statisticsNow) {
		this.statisticsNow = statisticsNow;
		return this;
	}

	public PermanentTRAR setStatisticsOriginal(StatisticsOfPermanentTRAr statisticsOriginal) {
		this.statisticsOriginal = statisticsOriginal;
		return this;
	}

	@Override
	public PermanentTRAR addSupertype(SuperTypePlayableObjectTRAr playableObjectTypeTRAr) {
		if (playableObjectTypeTRAr != null && playableObjectTypeTRAr.isPermanent)
			super.addSupertype(playableObjectTypeTRAr);
		return this;
	}

	//

	// TODO OVERRIDE
}