package games.generic.controlModel;

import games.generic.controlModel.inventoryAbil.AbilitiesProvider;

public interface IGameWithAbililties {

	public AbilitiesProvider getAbilitiesProvider();

	public void setAbilitiesProvider(AbilitiesProvider ap);
}