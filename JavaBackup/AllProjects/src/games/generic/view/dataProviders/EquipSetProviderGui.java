package games.generic.view.dataProviders;

import games.generic.controlModel.inventoryAbil.EquipmentSet;
import games.generic.controlModel.inventoryAbil.EquipmentsHolder;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.view.GameView;

public class EquipSetProviderGui {

	public EquipSetProviderGui(GameView view) {
		super();
		this.view = view;
	}

	protected GameView view;

	public GameView getView() {
		return view;
	}

	public void setView(GameView view) {
		this.view = view;
	}

	public EquipmentSet getEquipSet() {
		PlayerGeneric p;
		EquipmentsHolder eh;
		p = view.getCurrentPlayerInGame();
		if (!(p instanceof EquipmentsHolder))
			return null;
		eh = (EquipmentsHolder) p;
		return eh.getEquipmentSet();
	}
}