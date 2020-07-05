package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;

public interface BaseCreatureRPG extends EquipmentsHolder, CreatureSimple, ManaHavingObject {

	// implemented to let "this class implementor" to redefine and call it"
	@Override
	public default void act(GModality modality, int timeUnits) { CreatureSimple.super.act(modality, timeUnits); }

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