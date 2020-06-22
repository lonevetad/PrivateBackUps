package games.theRisingAngel.misc;

import dataStructures.isom.InSpaceObjectsManager;
import games.generic.controlModel.subimpl.GObjectsInSpaceManagerImpl;
import games.theRisingAngel.GModalityTRAn;

public class GObjectsInSpaceManagerTRAn extends GObjectsInSpaceManagerImpl {

	public GObjectsInSpaceManagerTRAn(InSpaceObjectsManager<Double> isom) { super(isom); }

	@Override
	public int getSpaceSubunitsEachMacrounits() { // TODO Auto-generated method stub
		return GModalityTRAn.SPACE_SUB_UNITS_EVERY_UNIT_EXAMPLE_TRAN;
	}
}