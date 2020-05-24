package games.generic.view.guiSwing;

import games.generic.controlModel.GController;
import games.generic.controlModel.GModality;

public class ISOMView {
	public static final int GRID_SIZE_DEFAULT = 25;

	public ISOMView(GController gc) {
		if (gc == null)
			throw new IllegalArgumentException("Game controller cannot be null");
		this.gc = gc;
	}

	final GController gc;

	public GModality getGameModality() { return gc.getCurrentGameModality(); }

	// proxies

	public int getGridSize() {
		GModality gm;
		gm = getGameModality();
		return gm == null ? GRID_SIZE_DEFAULT : gm.getSpaceSubunitsEachMacrounits();
	}

}