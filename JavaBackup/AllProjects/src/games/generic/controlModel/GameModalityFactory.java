package games.generic.controlModel;

public interface GameModalityFactory {
	/**
	 * Build a new {@link GameModality}. To add other parameters on that game
	 * modality, just design this interface's implementation.
	 */
	public GameModality newGameModality(GameController gc, String gameModalityName);
}