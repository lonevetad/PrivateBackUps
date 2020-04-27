package games.generic.controlModel.gObj;

import games.generic.controlModel.GEventObserver;
import games.generic.controlModel.GModality;
import games.generic.controlModel.misc.AttributesHolder;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.RarityHolder;
import tools.ObjectNamedID;

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
 * <li>{@link RarityHolder}: NPC or enemy creatures could be spawned
 * randomly</li>
 * </ol>
 */
public interface CreatureSimple extends AttributesHolder, LivingObject, MovingObject, DamageDealerGeneric, RarityHolder,
		GModalityHolder, ObjectNamedID {

	public static final int TICKS_PER_SECONDS = 4, LOG_TICKS_PER_SECONDS = 2;
	public static final int MILLIS_REGEN_LIFE_MANA = 1000 / TICKS_PER_SECONDS;

	@Override
	public default int getRarityIndex() {
		return 0;
	}

	public int getTicks();

	public int getAccumulatedTimeLifeRegen();

	//

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) {
		return this;
	}

	public void setAccumulatedTimeLifeRegen(int newAccumulated);

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
		receiveLifeHealing(gm, temp, this);
	}

	@Override
	public default void act(GModality modality, int milliseconds) {
		int tfinal;
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