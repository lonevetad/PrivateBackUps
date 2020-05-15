package games.generic.controlModel.gObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;

public interface BaseCreatureRPG extends EquipmentsHolder, CreatureSimple, ManaHavingObject {

	// implemented to let "this class implementor" to redefine and call it"
	@Override
	public default void act(GModality modality, int timeUnits) { CreatureSimple.super.act(modality, timeUnits); }

	@Override
	default void onRemovedFromGame(GModality gm) {
		EquipmentsHolder.super.onRemovedFromGame(gm);
		CreatureSimple.super.onRemovedFromGame(gm);
	}

	@Override
	default void onAddedToGame(GModality gm) {
		EquipmentsHolder.super.onAddedToGame(gm);
		CreatureSimple.super.onAddedToGame(gm);
	}
}