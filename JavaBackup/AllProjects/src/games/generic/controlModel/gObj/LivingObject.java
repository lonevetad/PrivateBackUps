package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageDealerGeneric;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.generic.controlModel.damage.EventDamage;
import games.generic.controlModel.heal.HealingObject;
import games.generic.controlModel.heal.IHealableResourceType;
import games.generic.controlModel.subimpl.GEventInterfaceRPG;
import games.generic.controlModel.subimpl.GModalityRPG;

public interface LivingObject extends DestructibleObject, DamageReceiverGeneric, HealingObject {

	/**
	 * Shorthand to get the life regeneration.<br>
	 * Could be intended as "amount per second".
	 */
	public int getLifeRegenation();

	/** See {@link #getLifeRegenation()}. */
	public void setLifeRegenation(int lifeRegenation);

	public IHealableResourceType getLifeResourceType();

	@Override
	public default boolean receiveLifeHealing(int amount) {
		boolean hasHealed;
		hasHealed = (amount <= 0); // DamageReceiverGeneric.super.receiveHealing(amount);
		if (hasHealed) { this.healMyself(newHealInstance(getLifeResourceType(), amount)); }
		return hasHealed;
	}

//	@Override
//	public default void act(GModality modality, int timeUnits) {
//		// override required to sub-instances to call the super implementation
//		ObjectHealing.super.act(modality, timeUnits);}

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
		return geie1.fireDamageReceivedEvent(gmodrpg, source, this, originalDamage, damageAmountToBeApplied);
//		gom.dealsDamageTo(source, this, originalDamage);// cannot "deals" damage because it's already dealt
	}
	// , int actualDamageReceived);

	// , int actualHealingReceived);

	//

	//

	@Override
	public default boolean shouldBeDestroyed() { return this.getLife() <= 0; }
}