package tests.staticAbilities;

public class Creature extends SomethingWithIDName {
	private static final long serialVersionUID = 49874060323206L;
	private static long serialID = 0;
	/**
	 * An identifyer letting to distinghush from different instances of the same
	 * ability (i.e. having the same name)
	 */
	protected SwinHolder<AbstractAbility> abilities;

	public Creature(String name) {
		super(name);
		this.instanceID = Long.valueOf(serialID++);
		this.abilities = new SwinHolder<>();
	}

	//

}
