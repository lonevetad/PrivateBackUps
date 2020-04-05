package games.generic.controlModel.subimpl;

import games.generic.controlModel.GEventInterface;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.misc.DamageGeneric;

public interface GEventInterfaceRPG extends GEventInterface {

	// object living, moving and creature related

	public void fireDestructionObjectEvent(GModalityET gaModality, DestructibleObject desObj);

	public <SourceDamage> void fireDamageReceivedEvent(GModalityET gm, SourceDamage source, CreatureSimple target,
			DamageGeneric originalDamage);

	//

// misc

	public void fireMoneyChangeEvent(GModalityET gm, int currencyType, int oldValue, int newValue);

	public abstract void fireExpGainedEvent(GModalityET gm, int expGained);

	public abstract void fireLevelGainedEvent(GModalityET gm, int levelGained);
}