package games.generic.controlModel.player;

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

	protected Integer ID;
	String name;

	@Override
	public Integer getID() {
		return ID;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}
}