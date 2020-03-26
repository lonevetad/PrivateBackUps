package games.generic.controlModel.misc;

import games.generic.ObjectNamedID;

/**
 * Identify an object that is unique through it index (beware: it's NOT an
 * "ID"!) and its name.
 */
public interface IndexableObject extends ObjectNamedID {
	public int getIndex();
}