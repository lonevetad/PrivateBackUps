package dataStructures.isom.pathFinders;

import java.awt.Point;
import java.util.function.BiFunction;

import tools.NumberManager;

public class AStarHeuristicManhattan<Distance extends Number> implements BiFunction<Point, Point, Distance> {

	public AStarHeuristicManhattan(NumberManager<Distance> distanceManager) {
		super();
		this.distanceManager = distanceManager;
	}

	protected NumberManager<Distance> distanceManager;

	public NumberManager<Distance> getDistanceManager() { return distanceManager; }

	public void setDistanceManager(NumberManager<Distance> distanceManager) { this.distanceManager = distanceManager; }

	@SuppressWarnings("unchecked")
	@Override
	public Distance apply(Point pStart, Point pEnd) {
		double distance;
		Class<?> dmClass;
		dmClass = distanceManager.getNumberClass();
		distance = Math.abs(pStart.x - pEnd.x) + Math.abs(pStart.y - pEnd.y);
		if (Double.class.isAssignableFrom(dmClass)) {
			return (Distance) Double.valueOf(distance);
		} else if (Integer.class.isAssignableFrom(dmClass)) {
			return (Distance) Integer.valueOf((int) distance);
		} else {
			return distanceManager.fromDouble(distance);
		}
	}
}