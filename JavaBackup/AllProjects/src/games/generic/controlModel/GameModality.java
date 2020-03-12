package games.generic.controlModel;

/**
 * Examples:
 * <ul>
 * <li>PvsIA, 1v1, Multi_VS_Multi, etc</li>h
 * <li>dungeons, open world, YouVsWawesOfEnemies</li>
 * <li>chess like, usual RPG through maps, etc</li></li>
 */
public abstract class GameModality {
	static final long MIN_DELTA = 10L;

	//

	protected GameController gc;
	protected String modalityName;

	public GameModality(GameController gc, String modalityName) {
		this.gc = gc;
		this.modalityName = modalityName;
	}

	//

	// TODO ABSTRACT

	public abstract boolean isAlive();

	public abstract boolean isPlaying();

	// game object handler

	//

	// TODO CONCRETE METHODS

	public void runGame() {
		long start, lastDeltaElapsed;
		while(isAlive()) {
			lastDeltaElapsed = MIN_DELTA;
			while(isPlaying()) {
				start = System.currentTimeMillis();
				doOnEachCycle(lastDeltaElapsed);
				lastDeltaElapsed = Math.min(MIN_DELTA, System.currentTimeMillis() - start);
			}
		}
	}

	/**Designed to be overrided*/
	void doOnEachCycle(long millisecToElapse){
		for each TimeProgressingObject ao {
			ao.act(lastDeltaElapsed); // fai progredire QUALSIASI cosa: abilità che si ricaricano col tempo, rigenerazioni, movimento di proiettili e cose, etc
		}
	}

}