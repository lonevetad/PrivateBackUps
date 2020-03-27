package games.theRisingAngel.creatures;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.GModalityET;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.GModalityTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;

public interface BaseCreatureTRAr extends CreatureOfRPGs {

	@Override
	public default int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public default int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAr.RigenLife.getIndex());
	}

	//

	@Override
	public default void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	@Override
	public default void setLifeRegenation(int lifeRegenation) {
		if (lifeRegenation > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(), lifeRegenation);
		}
	}

	//

	//

	@Override
	public default void receiveDamage(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		if (originalDamage.getDamageAmount() <= 0)
			return;
		int dr;
		// check the type
		dr = this.getAttributes().getValue(AttributesTRAr.DamageReductionPhysical.getIndex());
		if (dr < 0)
			dr = 0;
		setLife(getLife() - (originalDamage.getDamageAmount() - dr));
		fireDamageReceived(gm, originalDamage, source);
	}
	//

	// TODO FIRE EVENTS

	@Override
	public default void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		GModalityTRAr gmodtrar;
		GEventInterfaceTRAr geie1;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmodtrar = (GModalityTRAr) gm;
		geie1 = (GEventInterfaceTRAr) gmodtrar.getEventInterface();
		geie1.fireDamageReceivedEvent(gmodtrar, source, this, originalDamage);
	}
}