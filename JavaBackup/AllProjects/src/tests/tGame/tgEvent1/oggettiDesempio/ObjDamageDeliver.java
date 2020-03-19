package tests.tGame.tgEvent1.oggettiDesempio;

import games.generic.UniqueIDProvider;
import games.generic.controlModel.gameObj.TimedObject;

// TODO fare con GUI e affini
public class ObjDamageDeliver implements TimedObject {
	static final int MILLIS_EACH__DAMAGE = 500;
	Integer ID;
	int timeEnlapsed, c;

	public ObjDamageDeliver() {
		ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID();
		timeEnlapsed = 0;
		c = 0;
	}

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public void act(long milliseconds) {
		if (milliseconds > 0) {
			if ((this.timeEnlapsed += milliseconds) > MILLIS_EACH__DAMAGE) {
				this.timeEnlapsed %= MILLIS_EACH__DAMAGE;
				// TODO perform the damage
				System.out.println("Damage " + c++);
			}
		}
	}

}