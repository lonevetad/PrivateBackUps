package games.generic.controlModel.eventsGame;

import games.generic.controlModel.IGEvent;
import games.generic.controlModel.subImpl.GEvent;

/**
 * Event putting in relations two objects: a "source doing something to a
 * target". It a very general class.
 * <p>
 * In future, the Source and the Target will be replace by a specific class and
 * the damage will be implemented to something more flexible.
 */
public abstract class EventInfo_SourceToTarget<Source, Target> extends GEvent {

	protected Source source;
	protected Target target;
	protected final IGEvent eventIdentifier;

	public EventInfo_SourceToTarget(IGEvent eventIdentifier, Source source, Target target, int damage) {
		super();
		this.eventIdentifier = eventIdentifier;
		this.source = source;
		this.target = target;
	}

	public IGEvent getEventIdentifier() {
		return eventIdentifier;
	}

	public Source getSource() {
		return source;
	}

	public Target getTarget() {
		return target;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public String getName() {
		return eventIdentifier.getName();
	}
}