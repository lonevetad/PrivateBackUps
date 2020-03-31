package games.generic;

import games.generic.controlModel.ObjectNamed;
import tools.ObjectWithID;

/**
 * Mark an object as identifiable through both its numerical identifier (i.e.
 * {@link #getID()}) and its textual identifier (i.e. {@link #getName()}).
 */
public interface ObjectNamedID extends ObjectWithID, ObjectNamed {

}