package geometry.pointTools.impl;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import tools.MathUtilities;
import tools.MathUtilities.PointRelativeToLine;

public final class PolygonUtilities {

	public static void forEachPoint(Polygon p, Consumer<Point> action) {
		int i, len;
		len = p.npoints;
		i = -1;
		while (++i < len)
			action.accept(new Point(p.xpoints[i], p.ypoints[i]));
	}

	public static void forEachPoint(Polygon p, BiConsumer<Integer, Integer> action) {
		int i, len;
		len = p.npoints;
		i = -1;
		while (++i < len)
			action.accept(p.xpoints[i], p.ypoints[i]);
	}

	public static String polygonToString(Polygon p) {
		final int len;
		final StringBuilder sb;

		len = p.npoints;
		sb = new StringBuilder(len << 4);
		sb.append("Polygon with ").append(len).append(" corners:");
		forEachPoint(p, (x, y) -> sb.append("\n- ")/* .append(i) */.append(": (x: ").append(x).append(", y: ").append(y)
				.append(')'));
		return sb.toString();
	}

	/** Copied from {@link java.awt.Polygon} because it's a beautyfull method. */
	protected static boolean contains_FromPolygonClass(Polygon p, double x, double y) {
		if (p.npoints <= 2 /* || !p.getBoundingBox().contains(x, y) */) {
			return false;
		}
		// use the ray-casting-algorithm
		int hits = 0;
		int lastx = p.xpoints[p.npoints - 1];
		int lasty = p.ypoints[p.npoints - 1];
		int curx, cury;

		// Walk the edges of the polygon
		for (int i = 0; i < p.npoints; lastx = curx, lasty = cury, i++) {
			curx = p.xpoints[i];
			cury = p.ypoints[i];

			if (cury == lasty) {
				continue;
			}

			int leftx;
			if (curx < lastx) {
				if (x >= lastx) {
					continue;
				}
				leftx = curx;
			} else {
				if (x >= curx) {
					continue;
				}
				leftx = lastx;
			}

			double test1, test2;
			if (cury < lasty) {
				if (y < cury || y >= lasty) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - curx;
				test2 = y - cury;
			} else {
				if (y < lasty || y >= cury) {
					continue;
				}
				if (x < leftx) {
					hits++;
					continue;
				}
				test1 = x - lastx;
				test2 = y - lasty;
			}
			if (test1 < (test2 / (lasty - cury) * (lastx - curx)))
				hits++;
		}
		return ((hits & 1) != 0);
	}

	// Copyright 2000 softSurfer, 2012 Dan Sunday
	// This code may be freely used and modified for any purpose
	// providing that this copyright notice is included with it.
	// SoftSurfer makes no warranty for this code, and cannot be held
	// liable for any real or imagined damage resulting from its use.
	// Users of this code must verify correctness for their application.

	// a Point is defined by its coordinates {int x, y;}
	// ===================================================================

	// ===================================================================

	// cn_PnPoly(): crossing number test for a point in a polygon
//	      Input:   P = a point,
//	               V[] = vertex points of a polygon V[n+1] with V[n]=V[0]
//	      Return:  0 = outside, 1 = inside
	// This code is patterned after [Franklin, 2000]
	static int cn_PnPoly(Point2D pointToBeTested, Point2D[] V, int n) {
		int cn = 0; // the crossing number counter

		// loop through all edges of the polygon
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]
			if (((V[i].getY() <= pointToBeTested.getY()) && (V[i + 1].getY() > pointToBeTested.getY())) // an upward
																										// crossing
					|| ((V[i].getY() > pointToBeTested.getY()) && (V[i + 1].getY() <= pointToBeTested.getY()))) { // a
																													// downward
																													// crossing
				// compute the actual edge-ray intersect x-coordinate
				double vt = (pointToBeTested.getY() - V[i].getY()) / (V[i + 1].getY() - V[i].getY());
				if (pointToBeTested.getX() < V[i].getX() + vt * (V[i + 1].getX() - V[i].getX())) // P.getX() < intersect
					++cn; // a valid crossing of y=P.getY() right of P.getX()
			}
		}
		return (cn & 1); // 0 if even (out), and 1 if odd (in)

	}
	// ===================================================================

	/**
	 * isPointInsideThePolygon(): winding number test for a point in a polygon <br>
	 * 
	 * @param pointToBeTested            a point
	 * @param polygonAsConsecutivePoints array "V" of "n" vertex points of a
	 *                                   polygon, where <code>V[n] == V[0]</code>
	 *                                   implicitly
	 * 
	 * @return: wn = the winding number's computation (=0 only when P is outside)
	 */
	public static boolean isPointInsideThePolygon(Point2D pointToBeTested, Point2D[] polygonAsConsecutivePoints) {
		int wn = 0, nextIndex, n; // the winding number counter
		n = polygonAsConsecutivePoints.length;
		// loop through all edges of the polygon
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]

			nextIndex = i + 1;
			if (nextIndex >= n)// no overflowing
				nextIndex = 0;
			if (polygonAsConsecutivePoints[i].getY() <= pointToBeTested.getY()) {
				// start y <= P.getY()
				if (polygonAsConsecutivePoints[nextIndex//
				].getY() > pointToBeTested.getY()) // an upward crossing
					if (MathUtilities.relationToLine(polygonAsConsecutivePoints[i],
							polygonAsConsecutivePoints[nextIndex], pointToBeTested) == PointRelativeToLine.Left)
						// P left of edge
						++wn; // have a valid up intersect
			} else { // start y > P.getY() (no test needed)
				if (polygonAsConsecutivePoints[nextIndex].getY() <= pointToBeTested.getY())
					// a downward crossing
					if (MathUtilities.relationToLine(polygonAsConsecutivePoints[i],
							polygonAsConsecutivePoints[nextIndex], pointToBeTested) == PointRelativeToLine.Right)
						// P right of edge
						--wn; // have a valid down intersect
			}
		}
		return wn == 0;
	}

	/**
	 * isPointInsideThePolygon(): winding number test for a point in a polygon <br>
	 * 
	 * @param pointToBeTested a point
	 * @param polygon         array "V" of "n" vertex points of a polygon, where
	 *                        <code>V[n] == V[0]</code> implicitly
	 * 
	 * @return: wn = the winding number's computation (=0 only when P is outside)
	 */
	public static boolean isPointInsideThePolygon(Point2D pointToBeTested, Polygon polygon) {
		int n;
		n = polygon.npoints;
		if (n < 3 || (!polygon.getBounds().contains(((pointToBeTested instanceof Point) ? ((Point) pointToBeTested)
				: new Point((int) pointToBeTested.getX(), (int) pointToBeTested.getY()))))//
		)
			return false;
		return isPointInsideThePolygon(pointToBeTested.getX(), pointToBeTested.getY(), polygon);
	}

	/** See {@link #isPointInsideThePolygon(Point2D,Polygon)}. */
	public static boolean isPointInsideThePolygon(double px, double py, Polygon polygon) {
		int wn = 0, n, x, y; // the winding number counter
		double prevx, prevy;
		int[] xx, yy;
		n = polygon.npoints;
		if (n < 3 || (!polygon.getBounds().contains(px, py)))
			return false;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		// loop through all edges of the polygon
		// pointToBeTested used as previous
		prevx = xx[--n];
		prevy = yy[n];
		for (int i = 0; i < n; i++) { // edge from V[i] to V[i+1]
			x = xx[i];
			if ((y = yy[i]) <= py) { // start y <= P.getY()
				if ((prevy >= py)// an upward crossing
						&& MathUtilities.relationToLine(x, y, prevx, prevy, px, py) == PointRelativeToLine.Left)
					// P left of edge
					++wn; // have a valid up intersect
			} else { // start y > P.getY() (no test needed)
				if ((prevy <= py)// a downward crossing
						&& MathUtilities.relationToLine(x, yy[i], prevx, prevy, px, py) == PointRelativeToLine.Right)
					// P right of edge
					--wn; // have a valid down intersect
			}
			prevx = x;
			prevx = y;
		}
		return wn == 0;
	}

	@Deprecated
	public static double areaPolygon2D(Polygon p) {
		int n, i, j, k; // indices
		double area;
		int[] xx, yy;
		n = p.npoints;
		if (n < 3)
			return 0; // a degenerate polygon
		area = 0;
		n--;
		xx = p.xpoints;
		yy = p.ypoints;
		i = 1;
		j = 2;
		k = 0;
		while (i < n)
			area += xx[i++] * (yy[j++] - yy[k++]);
		area += xx[n] * (yy[1] - yy[n - 1]); // wrap-around term
		return area / 2.0;
	}
}