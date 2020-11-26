package games.generic.controlModel.gObj.creature;

import games.generic.controlModel.GModality;
import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.heal.resExample.ManaHavingObject;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;

public interface BaseCreatureRPG extends EquipmentsHolder, CreatureSimple, ManaHavingObject {

	// implemented to let "this class implementor" to redefine and call it"

	@Override
	default void addMeToGame(GModality gm) {
		EquipmentsHolder.super.addMeToGame(gm);
		CreatureSimple.super.addMeToGame(gm);
	}

	@Override
	default void onAddedToGame(GModality gm) {
		// nothing to do here righ now
	}

	@Override
	default void removeMeToGame(GModality gm) {
		EquipmentsHolder.super.removeMeToGame(gm);
		CreatureSimple.super.removeMeToGame(gm);
	}

	@Override
	default void onRemovedFromGame(GModality gm) {
		// nothing to do here righ now
	}
}