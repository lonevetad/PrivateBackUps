package common.abstractCommon.behaviouralObjectsAC;

import java.util.List;

/**
 * It's a {@link ObjectActingOnPassingTime} that do so many things on time passing, like rechanrging
 * abilities, healing, AI-computing, moving, animating equipment, ecc.
 */
public interface ObjectPerformingTasksOnPassingTime extends ObjectActingOnPassingTime {

	public List<ObjectActingOnPassingTime> getTasksList();

	public ObjectPerformingTasksOnPassingTime setTasksList(List<ObjectActingOnPassingTime> tasksList);

	// DEFAULT

	public default ObjectPerformingTasksOnPassingTime addTask(ObjectActingOnPassingTime o) {
		List<ObjectActingOnPassingTime> l;
		if (o != null && o != this && (l = getTasksList()) != null && (!l.contains(o))) {
			l.add(o);
			return this;
		}
		return null;
	}

	@Override
	public default void act(int milliseconds) {
		List<ObjectActingOnPassingTime> l;
		if ((l = getTasksList()) != null && l.size() > 0) {
			for (ObjectActingOnPassingTime o : l) {
				/* avoid null pointers and recursion */
				if (o != null && o != this) o.act(milliseconds);
			}
		}
	}
}
