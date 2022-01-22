package games.generic.controlModel.player;

import java.util.List;

import games.generic.controlModel.GModality;
import tools.ObjectNamedID;

/**
 * Abstraction of a user "outside a specific game modality" (i.e.
 * {@link GModality}), or "in menu, checking options, trading, managing
 * characters and saves, etc".
 */
public abstract class UserAccountGeneric implements ObjectNamedID {
	private static final long serialVersionUID = 1L;

	public UserAccountGeneric() {
		// TODO
	}

	protected Long ID;
	protected String name;
	protected List<Object> allCharacters; // all characters played, all saves

	@Override
	public Long getID() { return ID; }

	@Override
	public String getName() { return name; }

	public void setID(Long iD) { ID = iD; }

	public void setName(String name) { this.name = name; }
}