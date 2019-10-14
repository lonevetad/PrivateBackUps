package tests.staticAbilities;

public abstract class AbstractAbility extends SomethingWithIDName {
	private static final long serialVersionUID = 54000L;
	private static long serialID = 0;

	public AbstractAbility(String name) {
		super(name);
		this.instanceID = Long.valueOf(serialID++);
	}

	public abstract boolean canApply(GameModel gm, Creature owner);

	public abstract void applyTo(GameModel gm, Creature owner);
}
