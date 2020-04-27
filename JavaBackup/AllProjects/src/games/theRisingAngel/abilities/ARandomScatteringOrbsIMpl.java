package games.theRisingAngel.abilities;

import java.awt.Point;
import java.util.function.Predicate;

import games.generic.controlModel.gEvents.DamageReceiverGeneric;
import games.generic.controlModel.misc.DamageGeneric;
import games.theRisingAngel.GModalityTRAn;
import geometry.ObjectLocated;

public class ARandomScatteringOrbsIMpl extends ARandomScatteringOrbs {

	public ARandomScatteringOrbsIMpl(GModalityTRAn gameModality) {
		super(gameModality);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Predicate<ObjectLocated> getTargetsFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetAbility() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getMaxAmountScatteringOrbs() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public DamageGeneric getDamageOrbs(ScatteringOrb orb, DamageReceiverGeneric target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRadiusScattering() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Point getCenterOfScattering() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ScatteringOrb newOrb() {
		// TODO Auto-generated method stub
		return null;
	}

}
