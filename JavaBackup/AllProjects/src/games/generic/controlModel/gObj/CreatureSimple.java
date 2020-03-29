package games.generic.controlModel.gObj;

import games.generic.ObjectNamedID;
import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.CreatureAttributes;

/**
 * Base interface for a "creature" for almost ALL games (RPG, RTS, etc).
 * <p>
 * Useful classes/interfaces used here:
 * <ol>
 * <li>{@link AttributesHolder} (holding: {@link CreatureAttributes})</li>
 * <li>{@link MovingObject}</li>
 * <li>{@link TimedObject} (inherited by 2°)</li>
 * <li>{@link ObjectInSpace} (inherited by 2°)</li>
 * <li>{@link LivingObject}</li>
 * <li>{@link DestructibleObject} (inherited by 5°)</li>
 * <li>{@link GEventObserver} (inherited by 6° but independent in its
 * functionality and concept)</li>
 * </ol>
 */
public interface CreatureSimple extends AttributesHolder, LivingObject, MovingObject, GModalityHolder, ObjectNamedID {
// MovingObject extends TimedObject, ObjectInSpace, so it's comfortable

	public static final int TICKS_PER_SECONDS = 4, LOG_TICKS_PER_SECONDS = 2;
	public static final long MILLIS_REGEN_LIFE_MANA = 1000 / TICKS_PER_SECONDS;

	// NOTE: to perform a
//	public int getLifeRegenationCache	
	public int getTicks();

	public void setTicks(int ticks);

	//

	public default void performAllHealings() {
		int temp;
		GModality gm;
		gm = getGameModality();
		temp = getTicks() + 1;
		if (temp >= TICKS_PER_SECONDS) {
			setTicks(0);
			temp = getLifeRegenation();
			temp -= (TICKS_PER_SECONDS - 1) * (temp >> LOG_TICKS_PER_SECONDS);
		} else {
			setTicks(temp);
			temp = getLifeRegenation() >> LOG_TICKS_PER_SECONDS;
		}
		System.out.println("HEALIIIIIING of " + temp);
		receiveLifeHealing(gm, temp);
	}

	public long getAccumulatedTimeLifeRegen();

	public void setAccumulatedTimeLifeRegen(long newAccumulated);

	@Override
	public default void act(GModality modality, long milliseconds) {
		long tfinal;
		MovingObject.super.act(modality, milliseconds);
		tfinal = getAccumulatedTimeLifeRegen() + milliseconds;
		if (tfinal > MILLIS_REGEN_LIFE_MANA) {
			setAccumulatedTimeLifeRegen(tfinal - MILLIS_REGEN_LIFE_MANA);
			performAllHealings();
		} else {
			setAccumulatedTimeLifeRegen(tfinal);
		}
	}

}