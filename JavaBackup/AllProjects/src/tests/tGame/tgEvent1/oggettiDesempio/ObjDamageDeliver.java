package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subImpl.TimedObjectSimpleImpl;
import games.theRisingAngel.DamageTypesTRAr;
import tests.tGame.tgEvent1.GEventInterface_E1;
import tests.tGame.tgEvent1.GModality_E1;

// TODO fare con GUI e affini
public class ObjDamageDeliver implements TimedObjectSimpleImpl {
	private static final long serialVersionUID = 4741714L;
	static final int MILLIS_EACH__DAMAGE = 1500;
	long timeElapsed, timeThreshold;
	int c, damageAmount;
	Integer ID;
	BaseCreatureRPG target;

	public ObjDamageDeliver(long timeThreshold) {
		ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		timeElapsed = 0;
		this.timeThreshold = timeThreshold;
		c = 0;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public long getAccumulatedTimeElapsed() {
		return timeElapsed;
	}

	@Override
	public long getTimeThreshold() {
		return timeThreshold;
	}

	public BaseCreatureRPG getTarget() {
		return target;
	}

	public int getDamageAmount() {
		return damageAmount;
	}

	//

	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}

	public void setTarget(BaseCreatureRPG target) {
		this.target = target;
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.timeElapsed = newAccumulated;
	}

	//

	@Override
	public void executeAction(GModality modality) {
		GModality_E1 gmodtrar;
		GEventInterface_E1 geie1;
		DamageGeneric d;
		d = new DamageGeneric(damageAmount, DamageTypesTRAr.Physical);
		System.out.println("Damage time" + c++);
		gmodtrar = (GModality_E1) modality;
		geie1 = (GEventInterface_E1) gmodtrar.getEventInterface();
		geie1.fireDamageDealtEvent(gmodtrar, this, target, d);
		this.target.receiveDamage(modality, d, this);
	}

//	public void act(GModality modality, long milliseconds) {
//		if (milliseconds > 0) {
//			if ((this.timeEnlapsed += milliseconds) > MILLIS_EACH__DAMAGE) {
//				this.timeEnlapsed %= MILLIS_EACH__DAMAGE;
//				// TODO perform the damage
//				System.out.println("Damage " + c++);
//			}
//		}
//	}

}