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
public interface CreatureSimple
		extends AttributesHolder, LivingObject, MovingObject, DamageDealerGeneric, AbilitiesHolder, ShieldHavingObject, //
		RarityHolder, GModalityHolder, ObjectNamedID {

	public static final int TICKS_PER_SECONDS = 4, LOG_TICKS_PER_SECONDS = 2;
	public static final int MILLIS_REGEN_LIFE_MANA = 1000 / TICKS_PER_SECONDS;

	@Override
	public default int getRarityIndex() {
		return 0;
	}

	//

	@Override
	public default RarityHolder setRarityIndex(int rarityIndex) {
		return this;
	}

	@Override
	public default void act(GModality modality, int timeUnits) {
		MovingObject.super.act(modality, timeUnits);
		LivingObject.super.act(modality, timeUnits); // this is the same of the below :
//		ObjectHealing.super.act(modality, timeUnits);
	}

}