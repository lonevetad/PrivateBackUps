package videogames.trar.gameObjects;

import videogames.trar.gameMechanism.ModelTRAr;

/**
 * Evaluate any kind of property an {@link EntityTRAr} can have, like:
 * canAttack, canMove, canFly, canBlock(?), canRegenarate, canUnTAP, and so on,
 * giving the owner EntityTRAr and the environment {@link ModelTRAr}.
 * <p>
 * Stuffs like this and, maybe, the whole model could be replaces by a knowledge
 * base built with languages like Prolog ...
 */
public interface EntityPropertyPredicate {
	public static final EntityPropertyPredicate TAUTOLOGY = (o, e) -> true, CONTRADDICTION = (o, e) -> false;

	/** See parent documentation: {@link EntityPropertyPredicate}. */
	public boolean evaluateProperty(EntityTRAr owner, ModelTRAr environment);
}