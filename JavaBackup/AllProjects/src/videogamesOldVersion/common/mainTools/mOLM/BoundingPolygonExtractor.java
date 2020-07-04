package videogamesOldVersion.common.mainTools.mOLM;

import java.awt.Polygon;
import java.io.Serializable;

import tools.ObjectWithID;
import videogamesOldVersion.common.abstractCommon.shapedObject.AbstractObjectRectangleBoxed;
import videogamesOldVersion.common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;

public class BoundingPolygonExtractor implements Serializable {
	private static final long serialVersionUID = -4708606026144643L;
	// private static BuoundingPolygonExtractor
	// instanceBuoundingPolygonExtractor;

	/**
	 * An enumeration enumerating a simple set of direction where the CrossWalker
	 * can move.<br>
	 * Their parameter are the delta of the x and y axes.
	 */
	public static enum CrossDirections implements Serializable {
		RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0), UP(0, -1);

		final int dx, dy, index;

		CrossDirections(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
			index = SF.n++;
		}

		private static class SF {
			static int n = 0;
		}
	}

	/** Just for a fast use. */
	private static final CrossDirections[] valuesCrossDirections = CrossDirections.values();

	private BoundingPolygonExtractor() {}

	/*
	 * public static BuoundingPolygonExtractor getInstance() { if
	 * (instanceBuoundingPolygonExtractor == null) instanceBuoundingPolygonExtractor
	 * = new BuoundingPolygonExtractor(); return instanceBuoundingPolygonExtractor;
	 * }
	 */

	//

	// ShapeSpecification.SS_Rectangular rect

	public static Polygon boundingPolygon(AbstractMatrixObjectLocationManager molm, int width, int height) {
		return boundingPolygon(molm, 0, 0, width, height);
	}

	public static Polygon boundingPolygon(AbstractMatrixObjectLocationManager molm, AbstractObjectRectangleBoxed orb) {
		return (molm == null || orb == null) ? null
				: boundingPolygon(molm, orb.getXLeftBottom(), orb.getYLeftBottom(), orb.getWidth(), orb.getHeight());
	}

	public static Polygon boundingPolygon(AbstractMatrixObjectLocationManager molm,
			ShapeSpecification.SS_Rectangular rect) {
		return (molm == null || rect == null) ? null
				: boundingPolygon(molm, rect.getXLeftBottom(), rect.getYLeftBottom(), rect.getWidth(),
						rect.getHeight());
	}

	public static Polygon boundingPolygon(AbstractMatrixObjectLocationManager molm, int xStart, int yStart, int w,
			int h) {
		int x, y;
		// boundingPolygon_UnckeckedParameters(int[][] m, int w, int h)
		boolean pointStartNotFound;
		Polygon ret = null;
		PointStack pl = null;
		LightWeightPoint p, peekPoint, primo, tempPoint;
		NodeMatrix[][] matrix;
		NodeMatrix[] row;
		Direction d;
		int[] xx, yy, mat[];

		if (molm == null)
			return null;
		matrix = molm.getMatrix();
		if (w > molm.getWidthMicropixel())
			w = molm.getWidthMicropixel();
		if (h > molm.getHeightMicropixel())
			w = molm.getHeightMicropixel();
		if (xStart < 0) {
			w += xStart;
			xStart = 0;
		}
		if (yStart < 0) {
			h += yStart;
			yStart = 0;
		}

		if (w > 0 && h > 0 /* && boundsTrasparent(molm, xStart, yStart, w, h) */) {
			/*
			 * firstly, checks the borders : if they are non empty, then the bounding
			 * polygon inside this area is just the border
			 */
			if (boundsNotTrasparent(molm, xStart, yStart, w, h)) {
				xx = new int[] { xStart, xStart + w, xStart, xStart + w };
				yy = new int[] { yStart, yStart, yStart + h, yStart + h };
				return new Polygon(xx, yy, 4);
			}

			/*
			 * looks for the starting point : the leftmost and lowest (nearest to zero)
			 * point that is opaque.
			 */
			y = x = -1;
			pointStartNotFound = true;
			while (pointStartNotFound && ++y < h) {
				x = -1;
				row = matrix[yStart + y];
				while (++x < w && (pointStartNotFound = isTrasparent(row[xStart + x])))
					;
			}
			if (!pointStartNotFound) {
				x = xStart;
				y = yStart;
				try {
					pl = new PointStack(primo = new LightWeightPoint(x, y));

					// se sono l'unico punto in circolazione, ho finito
					if (isTrasparent(matrix, x + 1, y) == 1 && isTrasparent(molm, x, y + 1) == 1) {
						/*
						 * questo controllo è ridondante : è stato fatto nei while
						 */
						// && isTrasparent(m ,x, y - 1)==1
						ret = new Polygon(new int[] { x }, new int[] { y }, 1);
						pl = null;
					} else {
						// il caso NORMALE : una immagine un po' piu piena
						d = new Direction();
						// segnamo il punto iniziale
						(peekPoint = (p = primo.clone()).clone()).move(d.direction);
						// tempPoint = p.clone();
						// THE CYCLE
						do {
							// verifico se p è opaco
							switch (isTrasparent(molm, peekPoint, xStart, yStart, w, h)) {
							case (-1): {
								// out of bounds
								/*
								 * considerando che mi muovo sguendo l'invariante, incontrando il "baratro"
								 * della matrice/immagine, devo svoltare .. in senso orario (è difficile da
								 * spiegare, l'immaginazione aiuta)
								 */
								tempPoint = p.illusionMoving(d.setToClockWise());
								peekPoint.set(tempPoint);
								// mi giro e muovo p
								// peekPoint.move(d.setToClockWise();
								pl.add(p);
								p.set(tempPoint);
								peekPoint.move(d.direction);
								break;
							}
							case (0): {
								// peek è opaco, verifico l'antiorario
								tempPoint = p.illusionMoving(d.getCounterclockWise());
								switch (isTrasparent(molm, tempPoint, xStart, yStart, w, h)) {
								case (-1):// out ob bounds
								case (1): {
									// trasp .. continuo caso 1");
									// TUTTO OK, proseguo senza
									// aggiungere
									p.set(peekPoint);
									peekPoint.move(d.direction);
									// peekPoint.toString());
									break;
								}
								case (0): {
									// sono in un angolo antiorario !
									pl.add(p);
									peekPoint.set(p);
									// mi giro e muovo p
									peekPoint.move(d.setToCounterclockWise());
									/*
									 * mi muovo verso tempPoint in quanto, essendo opaco, è sicuramente un punto
									 * valido
									 */
									p.set(tempPoint);
									peekPoint.move(d.direction);
									break;
								}
								}
								break;
							}
							case (1): {
								// peek è trasparente, verifico l'antiorario
								tempPoint = p.illusionMoving(d.getCounterclockWise());
								switch (isTrasparent(molm, tempPoint, xStart, yStart, w, h)) {
								case (0): {
									// ALTRO ANGOLO ANTIORARIO
									pl.add(p);
									peekPoint.set(p);
									// mi giro e muovo p
									peekPoint.move(d.setToCounterclockWise());
									// similmente dall'angolo sopra, mi
									// muovo
									p.set(tempPoint);
									peekPoint.move(d.direction);
									break;
								}
								case (-1):// out of bounds
								case (1): {
									/*
									 * cerco in senso orario da p .. se è opaco, nuovo angolo, altrimenti p era una
									 * corna e sto tornando indietro (ma è tutto ok)
									 */
									tempPoint = p.illusionMoving(d.getClockWise());
									switch (isTrasparent(molm, tempPoint)) {
									case (0): {
										// ccorner, ma ORARIO !
										pl.add(p);
										peekPoint.set(p);
										// i turn myself and move p
										peekPoint.move(d.setToClockWise());
										p.set(tempPoint);
										peekPoint.move(d.direction);
										break;
									}
									case (-1): // out of bounds
									case (1): {
										// "p" is a HORN, a point
										// .. come back
										pl.add(p);
										// turn to 180°
										d.setToClockWise();
										d.setToClockWise();
										peekPoint = p.illusionMoving(d.direction);
										break;
									}
									}
									break;
								}
								}// // end second switch
								break;
							} // end "if ( peek opaque )"

							}// end first switch
						} while (!primo.equals(p));
						// conversion list into polygon
						mat = pl.toArray();
						xx = mat[0];
						yy = mat[1];
						pl = null; // let the GC do its work
						matrix = null;
						/*
						 * share the array because they will be useless inside this method
						 */
						ret = new Polygon(xx, yy, yy.length);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}

	protected static boolean boundsTrasparent(AbstractMatrixObjectLocationManager molm, int xStart, int yStart, int w,
			int h) {
		boolean safe;
		int r, c, lastX;
		NodeMatrix[][] matrix;
		NodeMatrix[] row1, row2;

		safe = false;
		r = 0;
		matrix = molm.getMatrix();
		row1 = matrix[yStart];
		row2 = matrix[yStart + h];
		c = -1;
		lastX = xStart;
		/* firstly, horizontal lines */
		while ((++c < w) && (safe = isTrasparent(row1[lastX]) && isTrasparent(row2[lastX++])))
			;
		/* then, vertical lines */
		if (safe) {
			h--;
			r = 0;
			while (safe && ++r < h) {
				row1 = matrix[++yStart];
				safe = isTrasparent(row1[xStart]) && isTrasparent(row1[lastX]);
			}
		}
		return !safe;
	}

	protected static boolean boundsNotTrasparent(AbstractMatrixObjectLocationManager molm, int xStart, int yStart,
			int w, int h) {
		boolean notTransparent;
		int r, c, lastX;
		NodeMatrix[][] matrix;
		NodeMatrix[] row1, row2;

		notTransparent = false;
		r = 0;
		matrix = molm.getMatrix();
		row1 = matrix[yStart];
		row2 = matrix[yStart + h];
		c = -1;
		lastX = xStart;
		/* firstly, horizontal lines */
		while ((++c < w) && (notTransparent = !(isTrasparent(row1[lastX]) || isTrasparent(row2[lastX++]))))
			;
		/* then, vertical lines */
		if (notTransparent) {
			h--;
			r = 0;
			while (notTransparent && ++r < h) {
				row1 = matrix[++yStart];
				notTransparent = !(isTrasparent(row1[xStart]) && isTrasparent(row1[lastX]));
			}
		}
		return !notTransparent;
	}

	/**
	 * See {@link #isTrasparent(NodeMatrix[][], int, int)} for further informations.
	 */
	protected static int isTrasparent(NodeMatrix[][] matrix, LightWeightPoint p) {
		return isTrasparent(matrix, p.x, p.y);
	}

	/**
	 * See {@link #isTrasparent(MatrixObjectLocationManager, int, int)} for further
	 * informations.
	 */
	protected static int isTrasparent(AbstractMatrixObjectLocationManager matrix, LightWeightPoint p) {
		return isTrasparent(matrix, p.x, p.y);
	}

	/**
	 * @param matrix : the matrix to extract and test the pixel
	 * @param x      : the x-coordinate of the pixel to be tested
	 * @param y      : the y-coordinate of the pixel to be tested
	 * @return
	 *         <ul>
	 *         <li>1 if the given pixel is transparent (see
	 *         {@link #isTrasparent(int)} for further information)</li>
	 *         <li>0 if the given pixel is NOT transparent</li>
	 *         <li>-1 if the matrix (or the y-row) is NULL or the given pixel is OUT
	 *         OF BOUNDS</li>
	 *         </ul>
	 */
	public static int isTrasparent(NodeMatrix[][] matrix, int x, int y) {
		NodeMatrix[] r;
		if (matrix != null && x >= 0 && y >= 0 && y < matrix.length) {
			r = matrix[y];
			if (r != null && x < r.length) { return isTrasparent(r[x]) ? 1 : 0; }
		}
		return -1;
	}

	public static int isTrasparent(AbstractMatrixObjectLocationManager matrix, int x, int y) {
		if (matrix != null && x >= 0 && y >= 0 && y < matrix.getHeightMicropixel() && x < matrix.getWidthMicropixel()) {
			return isTrasparent(matrix.getNodeMatrix(x, y)) ? 1 : 0;
		}
		return -1;
	}

	/**
	 * Return TRUE if the color, expressed in ARGB value, has the alpha (8 leftmost
	 * bit) equals to zero.
	 */
	public static boolean isTrasparent(NodeMatrix n) { return n == null ? true : isTrasparent(n.item); }

	public static boolean isTrasparent(ObjectWithID o) { return o == null || o.isNotSolid(); }

	protected static int isTrasparent(AbstractMatrixObjectLocationManager matrix, LightWeightPoint p, int x, int y,
			int w, int h) {
		int xx, yy;
		xx = p.x;
		yy = p.y;
		return (xx >= x && xx < (x + w) && yy >= y && yy < (y + h)) ? isTrasparent(matrix, p.x, p.y) : -1;
	}

	//

	// TODO CLASSES

	private static final class PointStack implements Serializable {
		private static final long serialVersionUID = 560149840847963736L;
		Node head, tail;
		int size;

		PointStack(LightWeightPoint p) { this(p.x, p.y); }

		PointStack(int x, int y) {
			tail = head = new Node(x, y);
			size = 1;
		}

		void add(LightWeightPoint p) { add(p.x, p.y); }

		void add(int x, int y) {
			tail.next = new Node(x, y);
			tail = tail.next;
			size++;
		}

		class Node {
			Node next;
			int x, y;

			Node(int xx, int yy) {
				x = xx;
				y = yy;
				next = null;
			}
		}

		int[][] toArray() {
			int i;
			int[] m[], a, b;
			Node n;
			m = new int[][] { a = new int[size], b = new int[size] };
			i = 0;
			n = head;
			while (n != null) {
				a[i] = n.x;
				b[i++] = n.y;
				n = n.next;
			}
			return m;
		}
	}

	/**
	 * A simple class holding a <code>CrossDirections</code> instance and simple
	 * methods to change his value or get s clockwise and counter clockwise
	 * directions.
	 */
	private static final class Direction implements Serializable {
		private static final long serialVersionUID = 640654870800000004L;
		CrossDirections direction;

		Direction() { direction = CrossDirections.RIGHT; }

		CrossDirections getCounterclockWise() {
			int i = direction.index - 1;
			if (i < 0) { i = valuesCrossDirections.length - 1; }
			return valuesCrossDirections[i];
		}

		CrossDirections getClockWise() {
			int i = direction.index + 1;
			if (i >= valuesCrossDirections.length) { i = 0; }
			return valuesCrossDirections[i];
		}

		// setter
		CrossDirections setDirection(CrossDirections di) { return direction = di; }

		/** Set the current direction to their counter clockwise ones. */
		CrossDirections setToCounterclockWise() {
			return setDirection(getCounterclockWise());
		}

		CrossDirections setToClockWise() { return setDirection(getClockWise()); }

		@Override
		public String toString() { return direction.name(); }
	}

	private static final class LightWeightPoint implements Serializable {
		private static final long serialVersionUID = 54098051514909900L;
		int x, y;

		LightWeightPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}

		LightWeightPoint(LightWeightPoint p) { set(p); }

		void set(LightWeightPoint p) {
			// set(p.x, p.y);
			this.x = p.x;
			this.y = p.y;
		}

		// void set(int x, int y) { this.x = x; this.y = y; }

		LightWeightPoint move(CrossDirections d) {
			x += d.dx;
			y += d.dy;
			return this;
		}

		/** I clone myself, the i move (and then return) that clone. */
		LightWeightPoint illusionMoving(CrossDirections d) {
			return this.clone().move(d);
		}

		public boolean equals(LightWeightPoint p) { return x == p.x && y == p.y; }

		@Override
		public String toString() { return "LightWeightPoint [x=" + x + ", y=" + y + ']'; }

		@Override
		public LightWeightPoint clone() { return new LightWeightPoint(this); }
	}

}