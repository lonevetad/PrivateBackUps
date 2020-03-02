package tests.staticAbilities;

/** Model of the game, having some kind of Info */
public class GameModel {
	protected SwinHolder<Creature> creatures;

	public GameModel() {
		creatures = new SwinHolder<>();
	}

	public SwinHolder<Creature> getCreatures() {
		return creatures;
	}
}
