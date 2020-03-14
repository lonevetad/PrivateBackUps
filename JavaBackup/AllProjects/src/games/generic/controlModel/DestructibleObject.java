package games.generic.controlModel;

public interface DestructibleObject extends ObjectWIthID {
	public boolean isDestroyed();

	public boolean shouldBeDestroyed(); // semplice flag o cosa computata, per esempio verificando se vita<=0

	public void notifyDestruction(GameEventManager gem);

	/**
	 * Apply the destruction, maybe by calling
	 * {@link #notifyDestruction(GameEventManager)}
	 */
	public boolean destroy();
}