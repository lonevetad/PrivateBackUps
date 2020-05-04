package games.old;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gEvents.DamageReceiverGeneric;
import games.generic.controlModel.gEvents.EventDamage;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.DamageDealerGeneric;
import games.generic.controlModel.gObj.DestructibleObject;
import games.generic.controlModel.gObj.LivingObject;
import games.generic.controlModel.misc.DamageGeneric;
import games.generic.controlModel.misc.HealGeneric;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityET;
import games.generic.controlModel.subimpl.GModalityRPG;
import tools.ObjectWithID;

public interface LivingObject_OLD extends DestructibleObject, DamageReceiverGeneric {

	/**
	 * Shorthand to get the life regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getLifeRegenation();

	/** See {@link #getLifeRegenation()}. */
	public void setLifeRegenation(int lifeRegenation);

	/**
	 * Make this object receiving a non-negative amount of healing, in a context
	 * expressed by {@link GModality}, which could be used to fire events.
	 */
	public default <SourceHealing extends ObjectWithID> void receiveLifeHealing(GModality gm, int healingAmount,
			SourceHealing source) {
		if (healingAmount > 0) {
			setLife(getLife() + healingAmount);
			fireLifeHealingReceived(gm, healingAmount, source);
		}
	}

	/**
	 * Similar to {@link #fireDestructionEvent(GModality)}, upon receiving damage
	 * (that means: "during the {@link #receiveDamage(GModality, int)} call") this
	 * event should be fired, in case of complex games, to notify all objects that
	 * "responds to a damage-received event" that this kind of event has
	 * occurred.<br>
	 * A reply/reaction to the "raw damage received" could be a damage reduction.
	 */
	public default EventDamage fireDamageReceived(GModality gm, DamageGeneric originalDamage,
			DamageDealerGeneric source, int damageAmountToBeApplied) {
		GModalityRPG gmodrpg;
		GEventInterfaceRPG geie1;
//		GameObjectsManager gom;
		if (gm == null || (!(gm instanceof GModalityRPG)))
			return null;
		gmodrpg = (GModalityRPG) gm;
//		gom = gmodrpg.getGameObjectsManagerDelegated(); 
		geie1 = (GEventInterfaceRPG) gmodrpg.getGameObjectsManager().getGEventInterface();

		//

		// TODO beware of the cast on LivingObject

		//

		return geie1.fireDamageReceivedEvent(gmodrpg, source, (LivingObject) this, originalDamage,
				damageAmountToBeApplied);
//		gom.dealsDamageTo(source, this, originalDamage);// cannot "deals" damage because it's already dealt
	}
	// , int actualDamageReceived);

	public HealGeneric newHealLifeInstance(int healAmount);

	/**
	 * Similar to {@link #fireDamageReceived( GModality, int, int)}, but about
	 * healing.
	 */
	public default <SourceHealing extends ObjectWithID> void fireLifeHealingReceived(GModality gm, int originalHealing,
			SourceHealing source) {
		GEventInterfaceRPG geiRpg;
		geiRpg = (GEventInterfaceRPG) this.getGameModality().getGameObjectsManager().getGEventInterface();
		geiRpg.fireHealReceivedEvent((GModalityET) gm, source, (CreatureSimple) this,
				newHealLifeInstance(originalHealing));
	}

	// , int actualHealingReceived);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() {
		return this.getLife() <= 0;
	}
}