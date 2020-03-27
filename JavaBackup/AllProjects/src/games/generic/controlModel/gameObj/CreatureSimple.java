package games.generic.controlModel.gameObj;

import games.generic.ObjectNamedID;
import games.generic.controlModel.GModality;

public interface CreatureSimple extends AttributesHolder, WithLifeObject, MovingObject, GModalityHolder, ObjectNamedID {
// MovingObject extends TimedObject, ObjectInSpace, so it's comfortable

	public static final long MILLIS_REGEN_LIFE_MANA = 500;

	// NOTE: to perform a
//	public int getLifeRegenation
	public default int getHealingsTicksPerSeconds() {
		return 2; // 1000/MILLIS_REGEN_LIFE_MANA
	}

	public default void performAllHealings() {
		GModality gm;
		gm = getGameModality();
		receiveLifeHealing(gm, getLifeRegenation() / getHealingsTicksPerSeconds());
	}

	public long getAccumulatedTimeLifeRegen();

	public void setAccumulatedTimeLifeRegen(long newAccumulated);

	@Override
	public default void act(GModality modality, long milliseconds) {
		long tfinal;
		tfinal = getAccumulatedTimeLifeRegen() + milliseconds;
		if (tfinal > MILLIS_REGEN_LIFE_MANA) {
			setAccumulatedTimeLifeRegen(tfinal - MILLIS_REGEN_LIFE_MANA);
			performAllHealings();
		} else {
			setAccumulatedTimeLifeRegen(tfinal);
		}
	}

}