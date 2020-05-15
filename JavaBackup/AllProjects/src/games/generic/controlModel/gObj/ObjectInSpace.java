package games.generic.controlModel.gObj;

import geometry.ObjectLocated;
import geometry.ObjectShaped;

/**
 * Defines an object designed to be put in a "space-based context". Implements:
 * {@link ObjectShaped} and {@link ObjectLocated}.
 */
public interface ObjectInSpace extends ObjectShaped, ObjectLocated, GameObjectGeneric {
}