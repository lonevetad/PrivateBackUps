package games.generic.controlModel.inventoryAbil.abilitiesImpl;

import games.generic.controlModel.inventoryAbil.AbilityGeneric;
import tools.ObjectWithID;
import tools.UniqueIDProvider;

public abstract class AbilityBaseImpl implements AbilityGeneric {
	private static final long serialVersionUID = -8784886155L;

	public AbilityBaseImpl() { this(null); }

	public AbilityBaseImpl(String name) {
		this.name = name;
		assignID();
	}

	protected Integer ID;
	protected String name;
	protected ObjectWithID owner;

	protected void assignID() { ID = UniqueIDProvider.GENERAL_UNIQUE_ID_PROVIDER.getNewID(); }

	@Override
	public Integer getID() { return ID; }

	@Override
	public String getName() {
		return name; // attributeToModify.getAttributeModified().getName();
	}

	@Override
	public ObjectWithID getOwner() { return owner; }

	//

	@Override
	public void setOwner(ObjectWithID owner) { this.owner = owner; }
}