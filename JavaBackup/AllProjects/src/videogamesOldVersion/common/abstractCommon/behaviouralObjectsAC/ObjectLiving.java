package videogamesOldVersion.common.abstractCommon.behaviouralObjectsAC;

import java.io.Serializable;

public interface ObjectLiving extends Serializable {

	// getter

	public int getLifeMax();

	public int getLifeNow();

	public int getLifeOriginal();

	// setter

	public ObjectLiving setLifeMax(int life);

	public ObjectLiving setLifeNow(int life);

	public ObjectLiving setLifeOriginal(int life);

	//

	public ObjectLiving submitDamage(int damage);

	public ObjectLiving healMyself(int amount);
}