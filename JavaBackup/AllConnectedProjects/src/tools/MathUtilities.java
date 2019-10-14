package tools;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class MathUtilities {

	private MathUtilities() {
	}

	public static final double justOne = 1.0, sqrtTwo = Math.sqrt(2.0)//
	// triangle
			, TRIANGLE_HEIGHT_COEFFICIENT = Math.sqrt(3.0) / 2.0 //
			, TRIANGLE_INNER_RADIUS_COEFFICIENT = Math.sqrt(3.0) / 6.0//
			,
			/**
			 * Coefficient to calculate the distance from the equilater triangle's center
			 * and one of its corner, knowing the triangle's border's length.
			 */
			TRIANGLE_EQUILATERAL_RADIUS_CIRCUMSCRIBED_COEFFICIENT = //
					// (Math.sqrt(3.0) / 2.0) - (Math.sqrt(3.0) / 6.0)// the
					// original calculs
					Math.sqrt(3.0) / 3.0
	//
	;

	public static enum PointRelativeToLine {
		Left, On, Right
	}

	public static enum TriangleOrientation {
		CounterClockWise, Degenerated, ClockWise
	}

	//

	// TODO USEFULL STUFFS

	private static double doubleOfAreaTriangle(double xFirstPoint, double yFirstPoint, double xSecondPoint,
			double ySecondPoint, double xPointToBeTested, double yPointToBeTested) {
		return (xSecondPoint - xFirstPoint) * (yPointToBeTested - yFirstPoint)
				- (xPointToBeTested - xFirstPoint) * (ySecondPoint - yFirstPoint);
	}

	/**
	 * Calls {@link #relationToLine(Point2D, Point2D, Point2D)} passing as first two
	 * parameters {@link Line2D#getP1()} and {@link Line2D#getP2()}.
	 */
	public static PointRelativeToLine relationToLine(Line2D.Double line, Point2D pointToBeTested) {
		return relationToLine(line.getP1(), line.getP2(), pointToBeTested);
	}

	/**
	 * Tests if a point, the third one, is Left|On|Right of an infinite line,
	 * defined by the first two points, as described on
	 * {@link #relationToLine(Line2D.Double, Point2D)}. Input: three points
	 * firstPoint, Point2secondPoint, and pointToBeTested.<br>
	 * Calls {@link #relationToLine(double, double, double, double, double, double)}
	 * passing those three point's coordinates.
	 * 
	 * @param firstPoint      first point of a line
	 * @param secondPoint     second point of a line
	 * @param pointToBeTested the point to be tested if it lies on the line defined
	 *                        by first two points
	 * @return:
	 *          <ul>
	 *          <li>n >0 for P2 left of the line through P0 and P1</li>
	 *          <li>n=0 for P2 on the line</li>
	 *          <li><0 for P2 right of the line</li>
	 *          </ul>
	 *          See: Algorithm 1 "Area of Triangles and Polygons" at
	 *          http://http://geomalgorithms.com/a01-_area.html
	 */
	public static PointRelativeToLine relationToLine(Point2D firstPoint, Point2D secondPoint, Point2D pointToBeTested) {
		return relationToLine(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(),
				pointToBeTested.getX(), pointToBeTested.getY());
	}

	/**
	 * Calls {@link #relationToLine(double, double, double, double, double, double)}
	 * casting those three point's coordinates to double.
	 */
	public static PointRelativeToLine relationToLine(int xFirstPoint, int yFirstPoint, int xSecondPoint,
			int ySecondPoint, int xPointToBeTested, int yPointToBeTested) {
		return relationToLine((double) xFirstPoint, (double) yFirstPoint, (double) xSecondPoint, (double) ySecondPoint,
				(double) xPointToBeTested, (double) yPointToBeTested);
	}

	/** See {@link #relationToLine(Point2D, Point2D, Point2D)}. */
	public static PointRelativeToLine relationToLine(double xFirstPoint, double yFirstPoint, double xSecondPoint,
			double ySecondPoint, double xPointToBeTested, double yPointToBeTested) {
		double t;
		t = (xSecondPoint - xFirstPoint) * (yPointToBeTested - yFirstPoint)
				- (xPointToBeTested - xFirstPoint) * (ySecondPoint - yFirstPoint);
		// removed to optimize avoiding one calls
		/*
		 * doubleOfAreaTriangle(xFirstPoint, yFirstPoint, xSecondPoint, ySecondPoint,
		 * xPointToBeTested, yPointToBeTested);
		 */
		return t > 0.0 ? PointRelativeToLine.Left : (t == 0.0 ? PointRelativeToLine.On : PointRelativeToLine.Right);
	}

	/**
	 * Compute if the three points P1(a,b), P2(m,n) and P3(x,y) are collinear, that
	 * means they belong to the same segment / line.
	 */
	public static boolean areCollinear(int x1, int y1, int x2, int y2, int x3, int y3) {
		return (x1 == x2 && x2 == x3) || //
				(y1 == y2 && y2 == y3) || //
				(y2 - y1) * (x3 - x2) == (y3 - y2) * (x2 - x1);
	}

	/**
	 * Compute if the three points P1(a,b), P2(m,n) and P3(x,y) are collinear, that
	 * means they belong to the same segment / line.
	 */
	public static boolean areCollinear(double x1, double y1, double x2, double y2, double x3, double y3) {
		return (x1 == x2 && x2 == x3) || //
				(y1 == y2 && y2 == y3) || //
				(y2 - y1) * (x3 - x2) == (y3 - y2) * (x2 - x1);
	}

	/**
	 * See {@link #areCollinear(int, int, int, int, int, int)}.<br>
	 * Effectively, it calls
	 * <code> areCollinear(pfirst.x, pfirst.y, psecond.x, psecond.y, pthird.x, pthird.y);</code>
	 * 
	 * @param pfirst  the first point
	 * @param psecond the second point
	 * @param pthird  the second point
	 * @return {@link #areCollinear(int, int, int, int, int, int)}.
	 */
	public static boolean areCollinear(Point pfirst, Point psecond, Point pthird) {
		if (pfirst == null || psecond == null || pthird == null) {
			return false;
		}
		return areCollinear(pfirst.x, pfirst.y, psecond.x, psecond.y, pthird.x, pthird.y);
	}

	/** Returns the area of a triangle identified by those three points. */
	public static double areaTriangle(Point2D firstPoint, Point2D secondPoint, Point2D pointToBeTested) {
		return areaTriangle(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(),
				pointToBeTested.getX(), pointToBeTested.getY());
	}

	/**
	 * Calls {@link #areaTriangle(double, double, double, double, double, double)}
	 * casting those three point's coordinates to double.
	 */
	public static double areaTriangle(int xFirstPoint, int yFirstPoint, int xSecondPoint, int ySecondPoint,
			int xPointToBeTested, int yPointToBeTested) {
		return areaTriangle((double) xFirstPoint, (double) yFirstPoint, (double) xSecondPoint, (double) ySecondPoint,
				(double) xPointToBeTested, (double) yPointToBeTested);
	}

	/** See {@link #areaTriangle(Point2D, Point2D, Point2D)}. */
	public static double areaTriangle(double xFirstPoint, double yFirstPoint, double xSecondPoint, double ySecondPoint,
			double xPointToBeTested, double yPointToBeTested) {
		return doubleOfAreaTriangle(xFirstPoint, yFirstPoint, xSecondPoint, ySecondPoint, xPointToBeTested,
				yPointToBeTested) / 2.0;
	}

	/**
	 * Calls
	 * {@link #orientation2DTriangle(double, double, double, double, double, double)}
	 * casting those three point's coordinates to double.
	 */
	public static TriangleOrientation orientation2DTriangle(int xFirstPoint, int yFirstPoint, int xSecondPoint,
			int ySecondPoint, int xPointToBeTested, int yPointToBeTested) {
		return orientation2DTriangle((double) xFirstPoint, (double) yFirstPoint, (double) xSecondPoint,
				(double) ySecondPoint, (double) xPointToBeTested, (double) yPointToBeTested);
	}

	/** See {@link #orientation2DTriangle(Point2D, Point2D, Point2D)}. */
	public static TriangleOrientation orientation2DTriangle(double xFirstPoint, double yFirstPoint, double xSecondPoint,
			double ySecondPoint, double xPointToBeTested, double yPointToBeTested) {
		PointRelativeToLine r;
		r = relationToLine(xFirstPoint, yFirstPoint, xSecondPoint, ySecondPoint, xPointToBeTested, yPointToBeTested);
		return (r == PointRelativeToLine.Left) ? TriangleOrientation.CounterClockWise
				: ((r == PointRelativeToLine.Right) ? TriangleOrientation.ClockWise : TriangleOrientation.Degenerated);
	}

	/** Returns the orientation of a triangle identified by those three points. */
	public static TriangleOrientation orientation2DTriangle(Point2D p0, Point2D p1, Point2D p2) {
		return orientation2DTriangle(p0.getX(), p0.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public static double slope(Point2D a, Point2D b) {
		return slope(a.getX(), a.getY(), b.getX(), b.getY());
	}

	public static double slope(double xa, double ya, double xb, double yb) {
		if (xb == xa)
			return Double.POSITIVE_INFINITY;
		return (yb == ya) ? 0.0 : ((yb - ya) / (xb - xa));
	}

	/**
	 * Test if the first point lies in the bounding box denote by the other two
	 * points.
	 */
	public static boolean isBetween(Point2D pToTest, Point2D p1, Point2D p2) {
		return isBetween(pToTest.getX(), pToTest.getY(), p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	/**
	 * Called {@link #isBetween(Point2D, Point2D, Point2D)} passing those points
	 * coordinates.
	 */
	public static boolean isBetween(double pxToTest, double pyToTest, double px1, double py1, double px2, double py2) {
		double w, h;
// taking inspiration from Rectangle's class
		w = px1 - px2;
		if (w < 0)
			w = -w;
		h = py1 - py2;
		if (h < 0)
			h = -h;
//use p1 as the left-top corner of the rectangle
// (the left-top corner is considered as the origin (0;0))
		if (px1 > px2)
			px1 = px2;//
		if (py1 > py2)
			py1 = py2;//
		if (pxToTest < px1 || pyToTest < py1) {
			return false;
		}
		w += px1;
		h += py1;
		// overflow || intersect
		return ((w < px1 || w > pxToTest) && (h < py1 || h > pyToTest));
	}

	//

	public static double distanceTwoPoints(Point2D p1, Point2D p2) {
		// the class "Point" extends the class "Point2D"
		double dist;
		dist = 0.0;
		if (p1 != null && p2 != null) {
			dist = distanceTwoPoints(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}
		return dist;
	}

	public static double distanceTwoPoints(double miaX, double miaY, double altraX, double altraY) {
		return Math.hypot(miaX - altraX, miaY - altraY);
	}

	public static double distanceLinePoint(Line2D l1, Point2D point) {
		double m;
		Point2D p1, p2;
		p1 = l1.getP1();
		p2 = l1.getP2();
		m = slope(p1, p2);
		if (m == Double.POSITIVE_INFINITY)
			return Math.abs(p1.getX() - point.getX());
		else if (m == 0.0)
			return Math.abs(p1.getY() - point.getY());
		return distanceLinePoint(m, (p1.getY() - (m * p1.getX())), p1); // note the minus
	}

	/**
	 * Returns the distance from a point to the line described as
	 * <code>y = m*x + q</code>
	 */
	public static double distanceLinePoint(double m, double q, Point2D p) {
		return distanceLinePoint(1, -m, -q, p);
	}

	/**
	 * Returns the distance from a point to the line described as
	 * <code>a*y + b*x + c = 0</code>
	 */
	public static double distanceLinePoint(double a, double b, double c, Point2D p) {
		return Math.abs(a * p.getX() + b * p.getY() + c) / Math.hypot(a, b);
	}

	/**
	 * Returns the angle, expressed in degrees, existing from the line passing
	 * through <code>(x1,y1)</code> and <code>(x2,y2)</code> and the horizontal
	 * axes.-<br>
	 * It's assumed that the origin (0,0) lies on the BOTTOM-left corner.
	 */
	public static double angleDeg(double x1, double y1, double x2, double y2) {
		boolean quadranteUno = ((x1 < x2) && (y1 > y2));
		boolean quadranteDue = ((x1 > x2) && (y1 > y2));
		boolean quadranteTre = ((x1 > x2) && (y1 < y2));
		boolean quadranteQuattro = ((x1 < x2) && (y1 < y2));
		double numerat, denom, tanAlpha = 0.0, alphaGrad = 0.0;
		numerat = y2 - y1;
		denom = x2 - x1;
		/*
		 * && (miaY < altraY) ; con la seconda condizione non non va
		 */
		try { // tanAlpha = Math.sqrt( Math.pow( numerat, 2)) / Math.sqrt(
				// Math.pow( denom, 2)) ;
			if (denom != 0.0) {
				tanAlpha = numerat / denom;
				alphaGrad = Math.toDegrees(Math.atan(tanAlpha));
			} else if (numerat == 0) {
				tanAlpha = 0.0;
			} else if (numerat < 0) {
				tanAlpha = 270.0;
			} else {
				tanAlpha = 90.0;
			}
			if (quadranteUno) {
				if (alphaGrad < 0) {
					alphaGrad = -alphaGrad;
				}
			}
			if (quadranteDue || quadranteTre) {
				alphaGrad = 180.0 - alphaGrad;
			}
			if (quadranteQuattro) {
				alphaGrad = 360.0 - alphaGrad;
			} // quadranteDue
			if (denom == 0) {
				if (numerat < 0) {
					alphaGrad = 90.0D;
				}
				if (numerat > 0) {
					alphaGrad = 270.0D;
				}
			}
			if (numerat == 0) {
				if (denom < 0) {
					alphaGrad = 180.0D;
				}
				if (denom > 0) {
					alphaGrad = 0.0D;
				}
			}
		} catch (ArithmeticException ex) {
			if (numerat > 0) {
				alphaGrad = 90.0D;
			}
			if (numerat < 0) {
				alphaGrad = 270.0D;
			}
		}
		return alphaGrad;
	}

	public static double deviaAngoloModulare(double angoloOriginale, double angoloObiettivo, double deviazioneMassima) {
		double ret = angoloOriginale;
		if (angoloObiettivo != angoloOriginale) {
			if ((angoloObiettivo %= 360.0) < 0.0) {
				angoloObiettivo += 360.0;
			}
			if ((ret %= 360.0) < 0.0) {
				ret += 360.0;
			}
			if (angoloObiettivo > angoloOriginale) {
				if ((angoloObiettivo - angoloOriginale) > 180.0) {
					if (Math.abs(angoloObiettivo - angoloOriginale) > deviazioneMassima) {
						ret = (ret - deviazioneMassima) % 360.0;
					} else {
						ret = angoloObiettivo;
					}
				} else {
					if (Math.abs(angoloObiettivo - angoloOriginale) > deviazioneMassima) {
						ret = ((ret + deviazioneMassima) % 360.0);
					} else {
						ret = angoloObiettivo;
					}
				}
			} else {
				if ((ret - angoloObiettivo) > 180.0) {
					if (Math.abs(angoloObiettivo - angoloOriginale) > deviazioneMassima) {
						ret = ((ret + deviazioneMassima) % 360.0);
					} else {
						ret = angoloObiettivo;
					}
				} else {
					if (Math.abs(angoloObiettivo - angoloOriginale) > deviazioneMassima) {
						ret = ((ret - deviazioneMassima) % 360.0);
					} else {
						ret = angoloObiettivo;
					}
				}
			}
		} else {
			ret = angoloObiettivo;
		}
		if (ret < 0.0) {
			ret = ret + 360.0;
		}
		return ret;
	}

	//

	// TODO INTERSECTIONS

	/**
	 * If two lines are intersecting, returns the {@link Point2D} where they
	 * intersects, <code>null</code> otherwise.<br>
	 * If they are overlapping, one of the line's point are taken
	 */
	public static Point2D getLineIntersection(Line2D l1, Line2D l2) {
		return areLinesIntersecting(l1.getP1(), l1.getP2(), l2.getP1(), l2.getP2());
	}

	/**
	 * Called by {@link #getLineIntersection(Line2D, Line2D)} by providing their
	 * points ("P1" and "P2" of each line)
	 */
	public static Point2D areLinesIntersecting(Point2D pStart1, Point2D pEnd1, Point2D pStart2, Point2D pEnd2) {
//		areLinesIntersecting_Point
		return getLineIntersection(pStart1.getX(), pStart1.getY(), pEnd1.getX(), pEnd1.getY(), pStart2.getX(),
				pStart2.getY(), pEnd2.getX(), pEnd2.getY());
	}

	/**
	 * Called by {@link #areLinesIntersecting(Point2D, Point2D, Point2D, Point2D)}
	 * by providing each of those point's coordinates.
	 */
	public static Point2D getLineIntersection(double pxStart1, double pyStart1, double pxEnd1, double pyEnd1,
			double pxStart2, double pyStart2, double pxEnd2, double pyEnd2) {
		double slope_ab, slope_cd, numerator, denominator, q_ab, q_cd, x, y;
		Point2D p;
		slope_ab = slope(pxStart1, pyStart1, pxEnd1, pyEnd1);
		slope_cd = slope(pxStart2, pyStart2, pxEnd2, pyEnd2);
		q_ab = (pyStart1 - slope_ab * pxStart1);
		q_cd = (pyStart2 - slope_cd * pxStart2);
		if ((slope_ab == slope_cd) // parallel?
				&& (
				// overlapping?
				((slope_ab == Double.POSITIVE_INFINITY || slope_ab == Double.NaN) && pxStart1 == pxStart2) //
						|| //
							// overlapping?
						(slope_ab == 0.0 && pyStart1 == pyStart2) //
						|| //
							// if different costant parts of lines, then parallel BUT not overlapping
						(q_ab == q_cd)//
				)) {
			if (MathUtilities.isBetween(pxStart2, pyStart2, pxStart1, pyStart1, pxEnd1, pyEnd1))
				return new Point2D.Double(pxStart2, pyStart2);
			if (MathUtilities.isBetween(pxEnd2, pyEnd2, pxStart1, pyStart1, pxEnd1, pyEnd1))
				return new Point2D.Double(pxEnd2, pyEnd2);
			if (MathUtilities.isBetween(pxStart1, pyStart1, pxStart2, pyStart2, pxEnd2, pyEnd2))
				return new Point2D.Double(pxStart1, pyStart1);
			if (MathUtilities.isBetween(pxEnd1, pyEnd1, pxStart2, pyStart2, pxEnd2, pyEnd2))
				return new Point2D.Double(pxEnd1, pyEnd1);
		}
		if (slope_ab == Double.POSITIVE_INFINITY) {
			// ab vertical line: all a&b's x-es are equals
			x = pxStart1;
			y = (q_cd + (slope_cd * x));
			// it's a cross
			if ((pyStart1 <= pyEnd1) ? (y < pyStart1 || y > pyEnd1)
					// point are reversed
					: (y > pyStart1 || y < pyEnd1))
				return null;
			if ((pxStart2 < pxEnd2) ? (pxStart2 <= x && x <= pxEnd2)//
					: (pxEnd2 <= x && x <= pxStart2))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		if (slope_cd == Double.POSITIVE_INFINITY) {
			// cd vertical line: all c&d's x-es are equals
			x = pxStart2;
			y = (q_ab + (slope_ab * x));
			// it's a cross
			if ((pyStart2 <= pyEnd2) ? (y < pyStart2 || y > pyEnd2)
					// point are reversed
					: (y > pyStart2 || y < pyEnd2))
				return null;
			// if the y lies inside the line a-b, then intersection
			if ((pxStart1 < pxEnd1) ? (pxStart1 <= x && x <= pxEnd1)//
					: (pxEnd1 <= x && x <= pxStart1))
				return new Point2D.Double(x, y);
			else
				return null;

		}
		// slopes cannot be infinity
		if (slope_ab == 0.0) {
			y = pyStart1;
			// slope_cd cannot be Infinity (second group of checks) and zero (first ones)
			x = (y - q_cd) / slope_cd;

			if ((pxStart1 <= pxEnd1) ? (x < pxStart1 || x > pxEnd1)
					// point are reversed
					: (x > pxStart1 || x < pxEnd1))
				return null;
			if ((pxStart2 <= pxEnd2) ? (x < pxStart2 || x > pxEnd2)
					// point are reversed
					: (x > pxStart2 || x < pxEnd2))
				return null;
			if ((pyStart2 < pyEnd2) ? (pyStart2 <= y && y <= pyEnd2)//
					: (pyEnd2 <= y && y <= pyStart2))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		if (slope_cd == 0.0) {
			y = pyStart2;
			// slope_ab cannot be Infinity (second group of checks) and zero (first ones)
			x = (y - q_ab) / slope_ab;

			if ((pxStart2 <= pxEnd2) ? (x < pxStart2 || x > pxEnd2)
					// point are reversed
					: (x > pxStart2 || x < pxEnd2))
				return null;

			if ((pxStart1 <= pxEnd1) ? (x < pxStart1 || x > pxEnd1)
					// point are reversed
					: (x > pxStart1 || x < pxEnd1))
				return null;
			if ((pyStart1 < pyEnd1) ? (pyStart1 <= y && y <= pyEnd1)//
					: (pyEnd1 <= y && y <= pyStart1))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		denominator = slope_cd - slope_ab;
		numerator = q_ab - q_cd;
		x = (numerator / denominator);
		y = (q_ab + (slope_ab * x));
		p = new Point2D.Double(x, y);
		if ((MathUtilities.isBetween(p.getX(), p.getY(), pxStart1, pyStart1, pxEnd1, pyEnd1)
				&& MathUtilities.isBetween(p.getX(), p.getY(), pxStart2, pyStart2, pxEnd2, pyEnd2)))
			return p;
		y = (q_cd + (slope_cd * x));
		p = new Point2D.Double(x, y);
		if ((MathUtilities.isBetween(p.getX(), p.getY(), pxStart1, pyStart1, pxEnd1, pyEnd1)
				&& MathUtilities.isBetween(p.getX(), p.getY(), pxStart2, pyStart2, pxEnd2, pyEnd2)))
			return p;

		return null;
	}

	/**
	 * As like as {@link #areLinesIntersecting(Point2D, Point2D, Point2D, Point2D)}.
	 */
	public static Point2D getLineIntersection_Point(Point2D point_a, Point2D point_b, Point2D point_c,
			Point2D point_d) {
		double slope_ab, slope_cd, numerator, denominator, q_ab, q_cd, x, y;
		Point2D p;
		slope_ab = slope(point_a, point_b);
		slope_cd = slope(point_c, point_d);
		q_ab = (point_a.getY() - slope_ab * point_a.getX());
		q_cd = (point_c.getY() - slope_cd * point_c.getX());
		if ((slope_ab == slope_cd) // parallel?
				&& ( //
						// overlapping?
				((slope_ab == Double.POSITIVE_INFINITY || slope_ab == Double.NaN) && point_a.getX() == point_c.getX()) //
						|| //
							// overlapping?
						(slope_ab == 0.0 && point_a.getY() == point_c.getY()) //
						|| //
							// if different costant parts of lines, then parallel BUT not overlapping
						(q_ab == q_cd)//
				)) {
			if (MathUtilities.isBetween(point_c, point_a, point_b))
				return point_c;
			if (MathUtilities.isBetween(point_d, point_a, point_b))
				return point_d;
			if (MathUtilities.isBetween(point_c, point_d, point_a))
				return point_a;
			if (MathUtilities.isBetween(point_c, point_d, point_b))
				return point_b;
		}
		if (slope_ab == Double.POSITIVE_INFINITY) {
			// ab vertical line: all a&b's x-es are equals
			x = point_a.getX();
			y = (q_cd + (slope_cd * x));
			// it's a cross
			if ((point_a.getY() <= point_b.getY()) ? (y < point_a.getY() || y > point_b.getY())
					// point are reversed
					: (y > point_a.getY() || y < point_b.getY()))
				return null;
			if ((point_c.getX() < point_d.getX()) ? (point_c.getX() <= x && x <= point_d.getX())//
					: (point_d.getX() <= x && x <= point_c.getX()))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		if (slope_cd == Double.POSITIVE_INFINITY) {
			// cd vertical line: all c&d's x-es are equals
			x = point_c.getX();
			y = (q_ab + (slope_ab * x));
			// it's a cross
			if ((point_c.getY() <= point_d.getY()) ? (y < point_c.getY() || y > point_d.getY())
					// point are reversed
					: (y > point_c.getY() || y < point_d.getY()))
				return null;
			// if the y lies inside the line a-b, then intersection
			if ((point_a.getX() < point_b.getX()) ? (point_a.getX() <= x && x <= point_b.getX())//
					: (point_b.getX() <= x && x <= point_a.getX()))
				return new Point2D.Double(x, y);
			else
				return null;

		}
		// slopes cannot be infinity
		if (slope_ab == 0.0) {
			y = point_a.getY();
			// slope_cd cannot be Infinity (second group of checks) and zero (first ones)
			x = (y - q_cd) / slope_cd;

			if ((point_a.getX() <= point_b.getX()) ? (x < point_a.getX() || x > point_b.getX())
					// point are reversed
					: (x > point_a.getX() || x < point_b.getX()))
				return null;
			if ((point_c.getX() <= point_d.getX()) ? (x < point_c.getX() || x > point_d.getX())
					// point are reversed
					: (x > point_c.getX() || x < point_d.getX()))
				return null;
			if ((point_c.getY() < point_d.getY()) ? (point_c.getY() <= y && y <= point_d.getY())//
					: (point_d.getY() <= y && y <= point_c.getY()))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		if (slope_cd == 0.0) {
			y = point_c.getY();
			// slope_ab cannot be Infinity (second group of checks) and zero (first ones)
			x = (y - q_ab) / slope_ab;

			if ((point_c.getX() <= point_d.getX()) ? (x < point_c.getX() || x > point_d.getX())
					// point are reversed
					: (x > point_c.getX() || x < point_d.getX()))
				return null;

			if ((point_a.getX() <= point_b.getX()) ? (x < point_a.getX() || x > point_b.getX())
					// point are reversed
					: (x > point_a.getX() || x < point_b.getX()))
				return null;
			if ((point_a.getY() < point_b.getY()) ? (point_a.getY() <= y && y <= point_b.getY())//
					: (point_b.getY() <= y && y <= point_a.getY()))
				return new Point2D.Double(x, y);
			else
				return null;
		}
		denominator = slope_cd - slope_ab;
		numerator = q_ab - q_cd;
		x = (numerator / denominator);
		y = (q_ab + (slope_ab * x));
		p = new Point2D.Double(x, y);
		if ((MathUtilities.isBetween(p, point_a, point_b) && MathUtilities.isBetween(p, point_c, point_d)))
			return p;
		y = (q_cd + (slope_cd * x));
		p = new Point2D.Double(x, y);
		if ((MathUtilities.isBetween(p, point_a, point_b) && MathUtilities.isBetween(p, point_c, point_d)))
			return p;

		return null;
	}

	public static List<Point2D> getLinePolygonIntersections(Line2D line, Polygon polygon) {
		int len, otherIndex, thisx, thisy, lastx, lasty;
		int[] xx, yy;
		Line2D linePolygon;
		Point2D pointIntersection;
		List<Point2D> list;
		if (line == null || polygon == null)
			return null;
		len = polygon.npoints;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		otherIndex = 0;
		lastx = xx[otherIndex];
		lasty = yy[otherIndex];
		list = null;
		linePolygon = new Line2D.Double(0, 0, lastx, lasty);
		while (--len >= 0) {
			pointIntersection = null;
			thisx = xx[len];
			thisy = yy[len];
			linePolygon.setLine(thisx, thisy, lastx, lasty);
			pointIntersection = MathUtilities.getLineIntersection(line, linePolygon);
			if (pointIntersection != null) {
				if (list == null)
					list = new LinkedList<>();
				list.add(pointIntersection);
			}
			lastx = thisx;
			lasty = thisy;
		}
		return list;
	}

	public static List<Point2D> computeIntersectionPoints(Polygon polygon, Polygon otherPolygon) {
		int len, otherIndex, thisx, thisy, lastx, lasty;
		int[] xx, yy;
		Line2D linePolygon;
		List<Point2D> list, intersectionsLinePolygon;
		if (otherPolygon == null || polygon == null)
			return null;
		// perform check on the tinier polygon and put in secondPoint
		len = polygon.npoints;
		xx = polygon.xpoints;
		yy = polygon.ypoints;
		otherIndex = 0;
		lastx = xx[otherIndex];
		lasty = yy[otherIndex];
		list = null;
		linePolygon = new Line2D.Double(0, 0, lastx, lasty);
		while (--len >= 0) {
			thisx = xx[len];
			thisy = yy[len];
			linePolygon.setLine(thisx, thisy, lastx, lasty);
			intersectionsLinePolygon = MathUtilities.getLinePolygonIntersections(linePolygon, otherPolygon);
			if (intersectionsLinePolygon != null && (!intersectionsLinePolygon.isEmpty())) {
				for (Point2D p : intersectionsLinePolygon) {
					if (list == null)
						list = new LinkedList<>();
					list.add(p);
				}
			}
			lastx = thisx;
			lasty = thisy;
		}
		return list;
	}

	/**
	 * Compute the intersection poins of the given two cirlces, each identified by
	 * the centre and the radius.
	 */
	public static List<Point2D> getCircleToCircleIntersection(Point2D centre0, double radius0, Point2D centre1,
			double radius1) {
		return getCircleToCircleIntersection(centre0.getX(), centre0.getY(), radius0, centre1.getX(), centre1.getY(),
				radius1);
	}

	/**
	 * Compute the intersections of two circles. having radius <code>radius0</code>
	 * and <code>radius1</code> (third and sixth parameters from left and centres
	 * <code>P0(cx0, cy0)</code> (the first two parameters) and
	 * <code>P1(cx1, cy1)</code> (the fourth and fifth parameters).
	 * <p>
	 * Code's explanation
	 * <ol>
	 * <li>We want to compute the intersection points. Just calculate one, calling
	 * it <code>P3</code>, and the other one (<code>P4</code>) is specular</li>
	 * <li>The two centres's distance <code>d</code> is the Pythagorean theorem:
	 * <code>d = sqrt(dx*dx + dy*dy)</code>, which can be split in two part:
	 * <code>d = a+b</code>, where <code>a</code> is the distance from one centre
	 * (the first one <code>P0</code>, for instance) to the intersection between the
	 * line touching all two centres and the line touching the intersections points.
	 * Call this line's intersection point <code>P2</code></li>
	 * <li>giving it, the distance from one of the intersection point and
	 * <code>P2</code> is <code>h</code>. It derives that the distance from either
	 * one intersection point (<code>P2</code>) and the centre <code>C0</code> can
	 * be found using the Pythagorean theorem and it's exactly the first radius:
	 * <code>radius0</code></li>
	 * <li>In a similar way, we can express <code>h</code> depending on the other
	 * radius and <code>b</code></li>
	 * <li>so put those two equations in equality and obtain
	 * <code>radius0^2 - a^2 = radius1^2 - b^2</code></li>
	 * <li>Substitute a manipulated definition of <code>d</code>, that is
	 * <code>a = d-b</code> to obtain
	 * <code>b = (radius1^2 - radius0^2 + d^2) / (2*d)</code>. Get the similar
	 * equation for <code>a</code>, where the two radii has opposite signs</li>
	 * <li>With the Pythagorean theorem get the value of <code>h</code></li>
	 * <li>Get the <code>P2</code> as:
	 * <code>P2(x: [cx2 +- (d*(dy/d))], y: [cy2 -+ (h*(dx/d)])</code></li>
	 * <li>The <code>P3</code> follows:
	 * <code>P3(x: [cx0+(a*(dx/d))], y: [cy0+(a*(dy/d)])</code></li>
	 * <li>Notice the alternance of <code>+-</code> and <code>-+</code> and the
	 * delta of xs and ys are un-intuitively reverted.</li>
	 * <li>There you go</li>
	 * </ul>
	 * 
	 * @return a {@link java.util.List} of points if there is an intersection,
	 *         holding just one point if the cirlces are just "kissing" or the same,
	 *         <code>null</code> otherwise.
	 */
	public static List<Point2D> getCircleToCircleIntersection(double cx0, double cy0, double radius0, double cx1,
			double cy1, double radius1) {
		double dx, dy, d, a, h, cx2, cy2, dx2, dy2, d0square;
		List<Point2D> l;
		l = null;
		// if they are coincident and equal, then return THE centre
		if (radius0 == radius1 && cx0 == cx1 && cy0 == cy1) {
			l = new ArrayList<>(1);
			l.add(new Point2D.Double(cx0, cy0));
			return l;
		}

		// 1)
		dx = cx1 - cx0;
		dy = cy1 - cy0;
		d = Math.hypot(dx, dy);
		if ((d > (radius0 + radius1)) // too distant
				|| (d < Math.abs(radius0 - radius1)) // too close but radius too different
		)
			return null;
		l = new LinkedList<>();
		d0square = radius0 * radius0;
		a = (d0square - (radius1 * radius1) + (d * d)) / (2.0 * d);
		h = Math.sqrt(d0square - (a * a));
		cx2 = cx0 + ((a * dx) / d);
		cy2 = cy0 + ((a * dy) / d);
		dx2 = h * (dy / d);
		dy2 = h * (dx / d);
		l.add(new Point2D.Double(//
				cx2 + dx2, //
				cy2 - dy2));
		l.add(new Point2D.Double(//
				cx2 - dx2, //
				cy2 + dy2));
		return l;
	}

	//

	// TODO END INTERSECTORS, start silly stuffs

	public static final double adjustDegAngle(double angleDeg) {
		if (angleDeg < 0.0) {
			if (angleDeg <= -360.0)
				angleDeg %= 360.0;
			angleDeg += 360.0;
		} else if (angleDeg >= 360.0)
			angleDeg %= 360.0;
		return angleDeg;
	}

	public static boolean isNotTrasparent(int n) {
		// using a mask
		// return ((n & 0xff000000) >>> 24) != 0;
		return ((n >> 24) & 0x000000ff) != 0;
	}

	public static boolean isInside(int xx, int yy, int width, int height) {
		return xx >= 0 && xx < width && yy >= 0 && yy < height;
	}

	public static boolean isAtMostPositive(int n, int max) {
		return n >= 0 && n < max;
	}

	/**
	 * If mod is equals or less than zero, 0 is returned.<br>
	 * 
	 * @param val the value to be snapped
	 *
	 * @param mod the returned int will be a multiple of mod
	 */
	public static int snapModule(int val, int mod) {
		int ret = 0;
		if (mod > 0) {
			int h = mod >> 1, r = val % mod;
			if (r > h) {
				ret = (val - r) + mod;
			} else {
				ret = (val - r);
			}
		}
		return ret;
	}

	/**
	 * restituisce un angolo in gradi che à la differenza tra i due in valore
	 * assoluto .. per esempio tra 25 e 330(ossia -30) ci sono 55 di differenza
	 */
	public static double deltaAbsoluteDegrees(double ang1, double ang2) {
		double d = Math.max(ang1, ang2) - Math.min(ang1, ang2);
		return ((d > 180.0) ? (360.0 - d) : d);
	}

	public static int negateInt(int n) {
		return n ^ 0xFFFFFFFF;
	}

	//

	public static class MathBeans {

		/**
		 * Come nel sistema di riferimento informatico, ossia con l'origine nel vertice
		 * in alto a sinistra.
		 */
		@Deprecated
		public static double angleDeg_YLeftTop(double miaX, double miaY, double altraX, double altraY) {
			return 360.0 - angleDeg(miaX, miaY, altraX, altraY);
		}

		public static double angularCoefficient(double miaX, double miaY, double altraX, double altraY) {
			double numerat, denom, tanAlpha = 0.0;
			numerat = altraY - miaY;
			denom = altraX - miaX;
			if (denom != 0.0) {
				tanAlpha = numerat / denom;
				// alphaGrad = Math.toDegrees(Math.atan(tanAlpha));
			} else if (numerat == 0) {
				tanAlpha = 0.0;
			} else if (numerat < 0) {
				tanAlpha = (Math.PI * 3.0) / 2.0;
			} else // if (numerat > 0)
			{
				tanAlpha = Math.PI / 2.0;
			}
			return tanAlpha;
		}

		public static double snapModulare(double valoreOriginario, double modul) {
			double ret = valoreOriginario;
			double m = Math.abs(valoreOriginario % modul);
			if (m > (modul / 2.0)) {
				if (ret > 0.0) {
					ret = (ret - m) + modul;
				} else if (ret < 0.0) {
					ret = (ret + m) - modul;
				}
			}
			return ret;
		}
	}
}