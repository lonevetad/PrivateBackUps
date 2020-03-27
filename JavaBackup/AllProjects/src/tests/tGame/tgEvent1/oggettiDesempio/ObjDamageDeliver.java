package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.GModality;
import games.generic.controlModel.gameObj.CreatureOfRPGs;
import games.generic.controlModel.subImpl.TimedObjectSimpleImpl;

// TODO fare con GUI e affini
public class ObjDamageDeliver implements TimedObjectSimpleImpl {
	static final int MILLIS_EACH__DAMAGE = 1500;
	long timeElapsed;
	int c;
	Integer ID;
	CreatureOfRPGs target;

	public ObjDamageDeliver() {
		ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		timeElapsed = 0;
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
		return MILLIS_EACH__DAMAGE;
	}

	public CreatureOfRPGs getTarget() {
		return target;
	}

	//

	public void setTarget(CreatureOfRPGs target) {
		this.target = target;
	}

	@Override
	public void setAccumulatedTimeElapsed(long newAccumulated) {
		this.timeElapsed = newAccumulated;
	}

	//

	@Override
	public void executeAction(GModality modality) {
		System.out.println("Damage " + c++);

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