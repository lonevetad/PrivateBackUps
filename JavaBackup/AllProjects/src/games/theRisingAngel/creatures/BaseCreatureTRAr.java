package games.theRisingAngel.creatures;

import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subimpl.BaseCreatureRPGImpl;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import games.theRisingAngel.AttributesTRAr;
import games.theRisingAngel.GModalityTRAr;
import games.theRisingAngel.events.GEventInterfaceTRAr;
import tools.ObjectWithID;

public abstract class BaseCreatureTRAr extends BaseCreatureRPGImpl {
	private static final long serialVersionUID = -34551879021102L;

	public BaseCreatureTRAr(GModalityRPG gModRPG, String name) {
		super(gModRPG, name);
	}

	//

	@Override
	protected CreatureAttributes newAttributes() {
		return newAttributes(AttributesTRAr.VALUES.length);
	}

	@Override
	public int getLifeMax() {
		return this.getAttributes().getValue(AttributesTRAr.LifeMax.getIndex());
	}

	@Override
	public int getLifeRegenation() {
		return this.getAttributes().getValue(AttributesTRAr.RigenLife.getIndex());
	}

	//

	@Override
	public void setLifeMax(int lifeMax) {
		if (lifeMax > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.LifeMax.getIndex(), lifeMax);
			if (this.getLife() > lifeMax)
				this.setLife(lifeMax);
		}
	}

	@Override
	public void setLifeRegenation(int lifeRegenation) {
		if (lifeRegenation > 0) {
			this.getAttributes().setOriginalValue(AttributesTRAr.RigenLife.getIndex(), lifeRegenation);
		}
	}

	//

	//
//	public void receiveDamage....
//		dr = this.getAttributes().getValue(AttributesTRAr.DamageReductionPhysical.getIndex());
	//

	// TODO FIRE EVENTS

	@Override
	public void fireDamageReceived(GModality gm, DamageGeneric originalDamage, ObjectWithID source) {
		GModalityTRAr gmodtrar;
		GEventInterfaceTRAr geie1;
		if (gm == null || (!(gm instanceof GModalityET)))
			return;
		gmodtrar = (GModalityTRAr) gm;
		geie1 = (GEventInterfaceTRAr) gmodtrar.getEventInterface();
		geie1.fireDamageReceivedEvent(gmodtrar, source, this, originalDamage);
	}
}