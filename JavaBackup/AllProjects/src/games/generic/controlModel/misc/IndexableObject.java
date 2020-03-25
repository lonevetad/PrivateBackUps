package games.generic.controlModel.misc;

/**
 * Identify an object that is unique through it index (beware: it's NOT an
 * "ID"!) and its name.
 */
public interface IndexableObject {
	public int getIndex();

	public String getName();
}