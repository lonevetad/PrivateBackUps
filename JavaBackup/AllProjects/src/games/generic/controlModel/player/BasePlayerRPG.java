package games.generic.controlModel.player;

import games.generic.controlModel.gObj.BaseCreatureRPG;
import games.generic.controlModel.gObj.CurrencyHolder;

/** Base definition of a "player" of a "Rule Play Game" (RPG) */
public interface BasePlayerRPG extends PlayerWithExperience, BaseCreatureRPG, CurrencyHolder {
}