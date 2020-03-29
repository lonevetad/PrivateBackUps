package games.generic.controlModel.player;

import games.generic.controlModel.gObj.CreatureSimple;
import games.generic.controlModel.gObj.CurrencyHolder;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;

/** Base definition of a "player" of a "Rule Play Game" (RPG) */
public interface BasePlayerRPG extends EquipmentsHolder, CreatureSimple, CurrencyHolder {
}