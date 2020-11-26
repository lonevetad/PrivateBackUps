package games.generic.controlModel.gEvents;

import java.awt.Point;
import java.util.ArrayList;

import games.generic.controlModel.gObj.MovingObject;

public class EventMoviment extends GEvent {
	private static final long serialVersionUID = 369578984148920210L;

	private EventMoviment() {}

	protected MovingObject movingObject;
	protected Point from, to;

	//

	public MovingObject getMovingObject() { return movingObject; }

	public Point getFrom() { return from; }

	public Point getTo() { return to; }

	//

	@Override
	public void onProcessingEnded() { pool.add(this); }

	//

	//

	// TODO STATIC
	public static EventMoviment newEventMoviment(MovingObject movingObject, Point from, Point to) {
		EventMoviment em;
		em = pool.isEmpty() ? new EventMoviment() : pool.remove(pool.size() - 1);
		em.movingObject = movingObject;
		em.from = from;
		em.to = to;
		return em;
	}

	protected static ArrayList<EventMoviment> pool = new ArrayList<>(16);

}