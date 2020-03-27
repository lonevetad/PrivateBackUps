package tests.tGame.tgEvent1;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityET;
import games.theRisingAngel.PlayerTRAr;

public class Player_E1 extends PlayerTRAr {
	private static final long serialVersionUID = 210054045201L;
	static final int LIFE_MAX = 100;

	public Player_E1(GModality gm) {
		super(gm);
		this.setLifeMax(LIFE_MAX);
		this.setLife(LIFE_MAX);
	}

	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		GModality_E1 gmodtrar;
		GEventInterface_E1 geie1;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmodtrar = (GModality_E1) gm;
		geie1 = (GEventInterface_E1) gmodtrar.getEventInterface();
		geie1.fireDamageReceivedEvent(gmodtrar, source, this, originalDamage);
	}

}