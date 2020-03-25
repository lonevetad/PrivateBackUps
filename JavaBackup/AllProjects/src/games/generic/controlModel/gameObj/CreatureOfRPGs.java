package games.generic.controlModel.gameObj;

import games.generic.controlModel.GModality;
import games.generic.controlModel.inventory.EquipmentsHolder;

/**
 * Typical creature of Rule Play Games: one having a set of attributes and some
 * optional equipments (that could be dropped).
 */
public interface CreatureOfRPGs extends EquipmentsHolder, AttributesHolder, WithLifeObject, TimedObject {

	public GModality getGameModality();

	public void setGameModality(GModality gameModality);
}