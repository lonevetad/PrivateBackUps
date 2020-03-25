package games.generic.controlModel.eventsGame;

import games.generic.controlModel.GEvent;

/**
 * Event putting in relations two objects: a "source doing something to a
 * target". It a very general class.
 * <p>
 * In future, the Source and the Target will be replace by a specific class and
 * the damage will be implemented to something more flexible.
 */
public class EventInfo_SourceToTarget<Source, Target> extends GEvent {

	protected int damage;
	protected Source source;
	protected Target target;
	protected final String nameEvent;

	public EventInfo_SourceToTarget(String name, Source source, Target target, int damage) {
		super();
		this.nameEvent = name;
		this.damage = damage;
		this.source = source;
		this.target = target;
	}

	public String getNameEvent() {
		return nameEvent;
	}

	public int getDamage() {
		return damage;
	}

	public Source getSource() {
		return source;
	}

	public Target getTarget() {
		return target;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public String getType() {
		return nameEvent;
	}
}