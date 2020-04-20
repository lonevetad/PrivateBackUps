package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.subimpl.TimedObjectSimpleImpl;
import games.theRisingAngel.DamageTypesTRAr;
import games.theRisingAngel.GameObjectsManagerTRAr;
import tests.tGame.tgEvent1.GModality_E1;
import tools.UniqueIDProvider;

// TODO fare con GUI e affini
public class ObjDamageDeliverE1 implements TimedObjectSimpleImpl {
	private static final long serialVersionUID = 4741714L;
	static final int MILLIS_EACH__DAMAGE = 1500;
	long timeElapsed, timeThreshold;
	int c, damageAmount;
	Integer ID;
	BaseCreatureRPG target;

	public ObjDamageDeliverE1(long timeThreshold) {
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
		GameObjectsManagerTRAr gomTrar;
		DamageGeneric d;
		d = new DamageGeneric(damageAmount, DamageTypesTRAr.Physical);
		System.out.println("Damage time" + c++);
		gmodtrar = (GModality_E1) modality;
		gomTrar = (GameObjectsManagerTRAr) gmodtrar.getGameObjectsManager();
		gomTrar.dealsDamageTo(this, target, d);
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