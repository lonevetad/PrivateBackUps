package common.tests.testsLittle;

import java.util.Deque;
import java.util.LinkedList;

import common.mainTools.mOLM.MatrixObjectLocationManager;
import common.mainTools.mOLM.NodeMatrix;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeRunner;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification;
import common.mainTools.mOLM.abstractClassesMOLM.ShapeSpecification.SS_Circular;

public class GeneratorDeltaRunnerCircleBorder {

	public static class RunnerCircularBorder_CachingDelta implements ShapeRunner {
		private static final long serialVersionUID = 85126504980620L;
		// static final ShapeRunner originalCircularRunnar;

		public RunnerCircularBorder_CachingDelta(short maxRay) {
			this.maxRay = maxRay;
			generateArray();
		}

		short maxRay;
		short[][] deltasx, deltasy;
		// y are the indexes

		void generateArray() {
			short i;
			if (deltasx == null || deltasx == null) {
				deltasx = new short[maxRay][];
				deltasy = new short[maxRay][];
				i = 0;
				while (i < maxRay) {
					setDeltas(deltasx, deltasy, ++i);
				}
			}
		}

		//

		public static int getDiameter(int ray) {
			if (ray > 0)
				return (ray << 1) + 1;
			else if (ray == 0)
				return 0;
			else
				return -1;
		}

		/** ignoring ray less than 0 */
		private static void setDeltas(short[][] allDeltaX, short[][] allDeltaY, short ray) {
			int iarray, lenArray, itemp;
			short x, y, newx, halfRay, ray_1;
			double hray, ray_1d;
			short[] sax, say, tempx, tempy;
			DequeShort[] allOctaX, allOctaY;
			DequeShort dx, dy;
			Short sx, sy, sxNegated, syNegated;
			/*
			 * 360° divided in 8 part of 45° each: 90->45, 45->0 and similar ..
			 * odd indexes (1,3,5,7) are stacks, others (0,2,4,6) are queues
			 */
			if (ray <= 0) return;
			if (ray > 1) {
				hray = ray + 0.5;
				halfRay = // (short) ((ray >> 1) + (ray & 1));
						(short) ((ray_1 = (short) (ray + 1)) >> 1);
				y = ray_1;// halfRay;// (short) (ray >> 1);
				ray_1d = ray_1;
				allOctaX = new DequeShort[8];
				allOctaY = new DequeShort[8];
				x = -1;
				while (++x < 8) {
					allOctaX[x] = new DequeShort();
					allOctaY[x] = new DequeShort();
				}
				x = 0;

				while (--y > halfRay) {
					sy = Short.valueOf(y);
					syNegated = Short.valueOf((short) -y);
					newx = (short) Math.round(Math.cos(Math.asin(y / ray_1d)));
					while (x < newx) {
						sx = Short.valueOf(x);
						sxNegated = Short.valueOf((short) -x);

						/*
						 * for now, I will not optimize row-acces on 2D matrix
						 * so the order "last-first" and of the circle's
						 * sections are not so important
						 */
						// 90->45
						allOctaX[0].addLast(sx);
						allOctaY[0].addLast(sy);
						// 0->45
						allOctaX[1].addFirst(sy);
						allOctaY[1].addFirst(sx);
						// 90->135
						allOctaX[2].addLast(sxNegated);
						allOctaY[2].addLast(sy);
						// 0->315
						allOctaX[3].addFirst(sy);
						allOctaY[3].addFirst(sxNegated);
						// 225->270
						allOctaX[4].addLast(sxNegated);
						allOctaY[4].addLast(syNegated);
						// 135->180
						allOctaX[5].addFirst(syNegated);
						allOctaY[5].addFirst(sx);
						// 270->315
						allOctaX[6].addLast(sx);
						allOctaY[6].addLast(syNegated);
						// 180->225
						allOctaX[7].addFirst(syNegated);
						allOctaY[7].addFirst(sxNegated);

						x++;
					}
				}

				lenArray = allOctaX[0].size() << 3;
				sax = new short[lenArray];
				say = new short[lenArray];
				allDeltaX[ray - 1] = sax;
				allDeltaY[ray - 1] = say;
				iarray = 0;
				for (int i = 0; i < 8; i++) {
					dx = allOctaX[i];
					dy = allOctaY[i];
					while (!dx.isEmpty()) {
						/*
						 * as before, .... for now, I will not optimize
						 * row-acces on 2D matrix so the order "last-first" and
						 * of the circle's sections are not so important
						 */
						sax[iarray] = dx.poll();
						say[iarray++] = dy.poll();
					}
				}
			}
		}

		static class DequeShort extends LinkedList<Short> implements Deque<Short> {
			private static final long serialVersionUID = -5106123270L;
		}

		protected static void setDeltas_Vers1NotWork(short[][] allDeltaX
		// , short[][] allDeltaY
				, short ray) {
			short i, x, y, halfRay, originalHalfRay;
			double hray;
			short[] dsx;// , dsy;

			if (ray <= 0) return;
			if (ray > 1) {
				hray = ray + 0.5;
				halfRay = // (short) ((ray >> 1) + (ray & 1));
						(short) ((ray + 1) >> 1);
				originalHalfRay = halfRay;// (short) (ray >> 1);
				dsx = new short[i = ray];
				// dsy = new short[ray];
				allDeltaX[ray - 1] = dsx;
				// allDeltaY[ray - 1] = dsy;
				x = ray;
				y = 0;
				/*
				 * optimize: divide the circle in 4 part (90° each) and compute
				 * the x-delta for the angle 0->90°. The other parts are similar
				 * ... cardinal points are made by hand
				 */
				while (y < halfRay) {
					do {
						dsx[--i] = x;
					} while (x > 0 && Math.hypot(x, ++y) < hray);
					// halfRay--;
					x--;
				}
				// will be wasted about ( (ray*sin(45°))-(ray/2) ) iterations
				x = i = 0;
				y = ray;
				while (x < halfRay) {
					do {
						dsx[i++] = y;
					} while (y > 0 && Math.hypot(++x, y) < hray);
					y--;
				}
			}
			/**
			 * else { //<br>
			 * dsx = new short[4]; //<br>
			 * // dsy = new short[4]; //<br>
			 * allDeltaX[ray - 1] = dsx; //<br>
			 * // allDeltaY[ray - 1] = dsy; //<br>
			 * dsx[0] = -1; //<br>
			 * dsx[1] = dsx[2] = 0; //<br>
			 * dsx[3] = 1; //<br>
			 * }
			 */
		}

		@Override
		public boolean runArea(MatrixObjectLocationManager molm, ShapeSpecification gs, DoSomethingWithNode dswn) {
			int ray, x, y, halfRay, originalHalfRay;
			short[] s;
			SS_Circular ssc;
			NodeMatrix[][] mat;
			if (molm == null || dswn == null || gs == null || (!(gs instanceof SS_Circular))) return false;
			ssc = (SS_Circular) gs;
			mat = molm.getMatrix();
			ray = ssc.getRay();
			x = ssc.getXCenter();
			y = ssc.getYCenter();
			if (ray > 1) {
				// before, cardinal points
				if ((y -= ray) >= 0) dswn.doOnNode(molm, mat[y][x], x, y);
				y += ray;
				if ((x -= ray) >= 0) dswn.doOnNode(molm, mat[y][x], x, y);
				x += ray;
				if ((x += ray) < molm.getWidth()) dswn.doOnNode(molm, mat[y][x], x, y);
				x -= ray;
				if ((y += ray) < molm.getHeight()) dswn.doOnNode(molm, mat[y][x], x, y);
				y -= ray;
				// then the rest : the octave
				halfRay = // (short) ((ray >> 1) + (ray & 1));
						((ray + 1) >> 1);
				originalHalfRay = halfRay;
				s = deltasx[ray - 1];

			} else if (ray == 1) {
				if (--y >= 0)
					dswn.doOnNode(molm, mat[y][x], x, y++);
				else
					y++;
				if (--x >= 0)
					dswn.doOnNode(molm, mat[y][x], x++, y);
				else
					x++;
				if (++x < molm.getWidth())
					dswn.doOnNode(molm, mat[y][x], x--, y);
				else
					x--;
				if (++y < molm.getHeight()) dswn.doOnNode(molm, mat[y][x], x, y);

			} else
				return false;
			return true;
		}
	}

	public static void main(String[] args) {

	}

}
