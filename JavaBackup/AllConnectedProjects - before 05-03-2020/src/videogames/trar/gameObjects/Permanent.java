package videogames.trar.gameObjects;

import videogames.trar.gameObjects.impl.CompositeEntityPropertyPredicate;

public abstract class Permanent extends EntityTRAr {

	// all conjunctive
	public static enum BasicPredicatesEntityTRAr {
		Attack, Move, RecoverLife, RecoverStamina, ActivateAbilities,;
		BasicPredicatesEntityTRAr() {
			this(false);
		}

		BasicPredicatesEntityTRAr(boolean isDisjunctive) {
			this.isDisjunctive = isDisjunctive;
		}

		final boolean isDisjunctive;
	}

	public static final BasicPredicatesEntityTRAr[] valuesBasicPredicatesEntityTRAr = BasicPredicatesEntityTRAr
			.values();

	public Permanent() {
		// TODO Auto-generated constructor stub
	}

	protected CompositeEntityPropertyPredicate[] predicates;

	//

	//

	// TODO da usare in qualche add, remove, compute, blabla
	protected void checkPredicates(BasicPredicatesEntityTRAr b) {
		int index;
		CompositeEntityPropertyPredicate cepp;
		if (b == null)
			return;
		if (predicates == null)
			this.predicates = new CompositeEntityPropertyPredicate[valuesBasicPredicatesEntityTRAr.length];
		index = b.ordinal();
		if (predicates[index] == null) {
			predicates[index] = cepp = new CompositeEntityPropertyPredicate(b.isDisjunctive);
			if (!b.isDisjunctive)
				cepp.addEntityPropertyPredicate(EntityPropertyPredicate.TAUTOLOGY);
		}

	}

}
