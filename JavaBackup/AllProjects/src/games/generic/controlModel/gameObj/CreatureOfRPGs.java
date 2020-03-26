package games.generic.controlModel.gameObj;

import games.generic.ObjectNamedID;
import games.generic.controlModel.inventory.EquipmentsHolder;

/**
 * Typical creature of Rule Play Games: one having a set of attributes and some
 * optional equipments (that could be dropped).
 */
public interface CreatureOfRPGs
		extends EquipmentsHolder, AttributesHolder, WithLifeObject, TimedObject, GModalityHolder, ObjectNamedID {
}