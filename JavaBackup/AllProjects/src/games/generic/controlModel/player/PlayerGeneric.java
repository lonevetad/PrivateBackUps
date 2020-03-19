package games.generic.controlModel.player;

/**
 * Holds data of the player.<br>
 * There are two main subclasses, having lots of differences:
 * {@link PlayerInGame_Generic} and {@link PlayerOutside_Generic} /
 * "in-menu".<br>
 * For instance, in a trading card game, the "outside" player could be
 * represented by all card's collection and the set of decks, while the "in
 * game" player is the player having life, a deck, a board, an hand, a
 * "graveyard", etc.
 */
public abstract class PlayerGeneric {

	// TODO invent stuffs
	protected String name;

	public PlayerGeneric() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}