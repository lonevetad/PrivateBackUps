package games.theRisingAngel.abilities;

import java.awt.Point;
import java.util.function.Predicate;

import games.generic.controlModel.GModality;
import games.generic.controlModel.damage.DamageGeneric;
import games.generic.controlModel.damage.DamageReceiverGeneric;
import games.theRisingAngel.GModalityTRAn;
import geometry.ObjectLocated;

public class ARandomScatteringOrbsImpl extends ARandomScatteringOrbs {
	private static final long serialVersionUID = 1L;

	public ARandomScatteringOrbsImpl(GModalityTRAn gameModality) {
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

	@Override
	public void onAddingToOwner(GModality gm) {
		// TODO Auto-generated method stub
	}
}