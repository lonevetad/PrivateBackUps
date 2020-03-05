package common.mainTools.mOLM;

import common.mainTools.MathUtilities;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;

public class ShapeRunners_OptimizingCircularShapes extends ShapeRunners {

	private static final long serialVersionUID = 41532058487L;

	protected ShapeRunners_OptimizingCircularShapes() {
		super();
	}

	private static ShapeRunners_OptimizingCircularShapes instanceShapeRunners_OCS = null;

	public static final ShapeRunners_OptimizingCircularShapes newInstance() {
		if (instanceShapeRunners_OCS == null) instanceShapeRunners_OCS = new ShapeRunners_OptimizingCircularShapes();
		return instanceShapeRunners_OCS;
	}

	//

	// TODO CIRCUMFERENCE

	//
	@Override
	protected boolean runOnCircumference_Unsafe(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		boolean ret;
		NodeMatrix[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		System.out.println("lol unsafe");
		if (ret = ray > 0) {
			if (ray == 1) {
				int tx;
				doswn.doOnNode(molm, matrix[y][tx = x + 1], tx, y);
				rigaSup = matrix[++y];
				doswn.doOnNode(molm, rigaSup[x], x, y);
				doswn.doOnNode(molm, rigaSup[tx = x + 2], tx, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, matrix[++y][++x], x, y);
			} else {
				// double rRay = ray + 0.5;
				boolean isDiffZero;
				int r = 0, c = 0, squareRay, diameter, xray1, yray1, halfRay = (ray >> 1) + (ray & 0x1), rInf, tx, ty,
						newc, diffx, rayRegressive, ray1, ry, rInfY, xStart, xStartSimmetric, yStart, yStartSimmetric;
				// disegno i punti cardinali
				squareRay = ray * ray;
				// xdiameter = x + (
				diameter = (ray << 1) + 1;
				rInf = diameter;
				yray1 = y + (rayRegressive = c = ray1 = ray + 1);
				xray1 = x + ray1;

				rigaSup = matrix[ty = ray1 + y];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, ty);
				// doswn.doOnRow(rigaSup, x + ray2);
				doswn.doOnNode(molm, rigaSup[tx = x + diameter], tx, ty);
				// doswn.doOnItem(matrix, ray + x, y);
				doswn.doOnNode(molm, matrix[y][tx = x + ray], tx, y);
				// doswn.doOnItem(matrix, ray + x, ray2 + y);
				doswn.doOnNode(molm, matrix[ty = diameter + y][tx = x + ray], tx, ty);

				r = -1;
				while (++r <= halfRay && doswn.canContinueCycling()) {

					rigaSup = matrix[ry = r + y];
					rigaInf = matrix[rInfY = rInf + y];

					/*
					 * idea: per ogni y "dall'alto verso il basso", calcolo il
					 * suo cirrispondente valore x, poi traccio una linea
					 * orizzontale tra questo x e quello calcolato con la y
					 * precedente
					 */
					// x*x + y*y = r*r ..
					// x = sqrt( r*r - y*y )
					rayRegressive--;
					newc = (int) Math.ceil(Math.sqrt(squareRay - (rayRegressive * rayRegressive)));

					// TODO FAre il ciclo principale :
					/*
					 * idea: fare un ciclo che vada da (rayx1-newc +1) a (x+c)
					 * in cui si doOnNode. Poi, oldc = (rayx1-newc)
					 */

					// pixelCountToPerform = ray
					isDiffZero = (diffx = (ray1 - c) - newc) == 0;
					System.out.println("diffx: " + diffx);
					// xc = x + c;

					xStart = xray1 - newc;
					xStartSimmetric = xray1 + newc;
					yStart = yray1 - newc;
					yStartSimmetric = yray1 + newc;
					while (doswn.canContinueCycling() && (diffx > 0 || isDiffZero)) {

						doswn.doOnNode(molm, rigaSup[tx = xStart + diffx], tx, ry);
						doswn.doOnNode(molm, rigaSup[tx = xStartSimmetric - diffx], tx, ry);
						doswn.doOnNode(molm, rigaInf[tx], tx, rInfY);
						doswn.doOnNode(molm, rigaInf[tx = xStart + diffx], tx, rInfY);

						// simmetrico
						rigaCenterSup = matrix[ty = yStart + diffx];
						doswn.doOnNode(molm, rigaCenterSup[tx = x + r], tx, ty);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + rInf], tx, ty);
						rigaCenterInf = matrix[ty = yStartSimmetric - diffx];
						doswn.doOnNode(molm, rigaCenterInf[tx], tx, ty);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + r], tx, ty);

						isDiffZero = false;
						diffx--;
					}

					c = ray - newc;

					rInf--;
				} // fine ciclo r
			}
		}
		return ret;
	}

	protected boolean runOnCircumference_Unsafe_XEquazione_NOT_WORKING(MatrixObjectLocationManager molm, int x, int y,
			int ray, DoSomethingWithNode doswn) {
		boolean ret;
		NodeMatrix[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		System.out.println("lol unsafe");
		if (ret = ray > 0) {
			if (ray == 1) {
				int tx;
				doswn.doOnNode(molm, matrix[y][tx = x + 1], tx, y);
				rigaSup = matrix[++y];
				doswn.doOnNode(molm, rigaSup[x], x, y);
				doswn.doOnNode(molm, rigaSup[tx = x + 2], tx, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, matrix[++y][++x], x, y);
			} else {
				// double rRay = ray + 0.5;
				boolean isDiffZero;
				int r = 0, c = 0, squareRay, diameter, xray1, yray1, halfRay = (ray >> 1) + (ray & 0x1), rInf, tx, ty,
						newc, diffx, rayRegressive, ray1, ry, rInfY, xStart, xStartSimmetric, yStart, yStartSimmetric;
				// disegno i punti cardinali
				squareRay = ray * ray;
				// xdiameter = x + (
				diameter = (ray << 1) + 1;
				rInf = diameter;
				yray1 = y + (rayRegressive = c = ray1 = ray + 1);
				xray1 = x + ray1;

				rigaSup = matrix[ty = ray1 + y];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, ty);
				// doswn.doOnRow(rigaSup, x + ray2);
				doswn.doOnNode(molm, rigaSup[tx = x + diameter], tx, ty);
				// doswn.doOnItem(matrix, ray + x, y);
				doswn.doOnNode(molm, matrix[y][tx = x + ray], tx, y);
				// doswn.doOnItem(matrix, ray + x, ray2 + y);
				doswn.doOnNode(molm, matrix[ty = diameter + y][tx = x + ray], tx, ty);

				r = -1;
				while (++r <= halfRay && doswn.canContinueCycling()) {

					rigaSup = matrix[ry = r + y];
					rigaInf = matrix[rInfY = rInf + y];

					/*
					 * idea: per ogni y "dall'alto verso il basso", calcolo il
					 * suo cirrispondente valore x, poi traccio una linea
					 * orizzontale tra questo x e quello calcolato con la y
					 * precedente
					 */
					// x*x + y*y = r*r ..
					// x = sqrt( r*r - y*y )
					rayRegressive--;
					newc = (int) Math.ceil(Math.sqrt(squareRay - (rayRegressive * rayRegressive)));

					/**
					 * if ((diffx = c - newc) <= 1) {//<br>
					 * // solo 1 pixel//<br>
					 * // rigaSup = matrix[ty = y + r];//<br>
					 * doswn.doOnNode(molm, rigaSup[tx = rayx1 - newc], tx,
					 * ty);//<br>
					 * doswn.doOnNode(molm, rigaSup[tx = rayx1 + newc
					 * //diffx//<br>
					 * ], tx, ty);//<br>
					 * // rigaInf = matrix[ty = y + r];//<br>
					 * doswn.doOnNode(molm, rigaInf[tx], tx, ty);//<br>
					 * doswn.doOnNode(molm, rigaInf[tx = rayx1 - newc], tx,
					 * ty);//<br>
					 * // simmetrico//<br>
					 * rigaSup = matrix[ty = yCenter - newc];//<br>
					 * // TODO sistemare questa parte//<br>
					 * doswn.doOnNode(molm, rigaSup[tx = x + r], tx, ty);//<br>
					 * doswn.doOnNode(molm, rigaSup[tx = x + diameter - r], tx,
					 * ty);//<br>
					 * rigaInf = matrix[ty = yCenter + newc];//<br>
					 * doswn.doOnNode(molm, rigaSup[tx], tx, ty);//<br>
					 * doswn.doOnNode(molm, rigaSup[tx = x + r], tx, ty);//<br>
					 * } else {
					 */

					// TODO FAre il ciclo principale :
					/*
					 * idea: fare un ciclo che vada da (rayx1-newc +1) a (x+c)
					 * in cui si doOnNode. Poi, oldc = (rayx1-newc)
					 */

					// pixelCountToPerform = ray
					isDiffZero = (diffx = (ray1 - c) - newc) == 0;
					System.out.println("diffx: " + diffx);
					// xc = x + c;

					xStart = xray1 - newc;
					xStartSimmetric = xray1 + newc;
					yStart = yray1 - newc;
					yStartSimmetric = yray1 + newc;
					while (doswn.canContinueCycling() && (diffx > 0 || isDiffZero)) {

						doswn.doOnNode(molm, rigaSup[tx = xStart + diffx], tx, ry);
						doswn.doOnNode(molm, rigaSup[tx = xStartSimmetric - diffx], tx, ry);
						doswn.doOnNode(molm, rigaInf[tx], tx, rInfY);
						doswn.doOnNode(molm, rigaInf[tx = xStart + diffx], tx, rInfY);

						// simmetrico
						rigaCenterSup = matrix[ty = yStart + diffx];
						doswn.doOnNode(molm, rigaCenterSup[tx = x + r], tx, ty);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + rInf], tx, ty);
						rigaCenterInf = matrix[ty = yStartSimmetric - diffx];
						doswn.doOnNode(molm, rigaCenterInf[tx], tx, ty);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + r], tx, ty);

						isDiffZero = false;
						diffx--;
					}

					c = ray - newc;

					rInf--;
				} // fine ciclo r
			}
		}
		return ret;
	}

	protected boolean runOnCircumference_Unsafe_OLD_WORKING(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		boolean ret;
		NodeMatrix[] rigaSup = null, rigaInf = null, rigaCenterSup = null, rigaCenterInf = null;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		System.out.println("lol unsafe");
		if (ret = ray > 0) {
			if (ray == 1) {
				int tx;
				doswn.doOnNode(molm, matrix[y][tx = x + 1], tx, y);
				rigaSup = matrix[++y];
				doswn.doOnNode(molm, rigaSup[x], x, y);
				doswn.doOnNode(molm, rigaSup[tx = x + 2], tx, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, matrix[++y][++x], x, y);
			} else {
				// double rRay = ray + 0.5;
				int r = 0, c = 0, squareRay, ray2 = ray << 1, ray_1 = ray - 1, halfRay = (ray >> 1) + (ray & 0x1), rInf,
						ray1 = ray + 1, horizontalSymmetricOldC, tx, ty, trc, trr;
				// disegno i punti cardinali

				rigaSup = matrix[ty = ray + y];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, ty);
				// doswn.doOnRow(rigaSup, x + ray2);
				doswn.doOnNode(molm, rigaSup[tx = x + ray2], tx, ty);
				// doswn.doOnItem(matrix, ray + x, y);
				doswn.doOnNode(molm, matrix[y][tx = x + ray], tx, y);
				// doswn.doOnItem(matrix, ray + x, ray2 + y);
				doswn.doOnNode(molm, matrix[ty = ray2 + y][tx = x + ray], tx, ty);

				squareRay = ray * ray;
				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					rigaSup = matrix[r + y];
					rigaInf = matrix[rInf + y];

					while (0 < c && doswn.canContinueCycling() &&
					// (Math.hypot((ray - c), (ray - r)) < rRay)
							(((trc = ray - c) * trc + (trr = ray - r) * trr) < squareRay)) {

						// doswn.doOnRow(rigaSup, c + x);
						doswn.doOnNode(molm, rigaSup[tx = x + c], tx, ty = y + r);
						// doswn.doOnRow(rigaSup, horizontalSymmetricOldC + x);
						doswn.doOnNode(molm, rigaSup[tx = x + horizontalSymmetricOldC], tx, ty);
						// doswn.doOnRow(rigaInf, c + x);
						doswn.doOnNode(molm, rigaInf[tx = x + c], tx, ty = y + rInf);
						// doswn.doOnRow(rigaInf, horizontalSymmetricOldC + x);
						doswn.doOnNode(molm, rigaInf[tx = x + horizontalSymmetricOldC], tx, ty);

						// ottengo i puntatori alle righe per ottimizzare;
						rigaCenterSup = matrix[c + y];
						rigaCenterInf = matrix[horizontalSymmetricOldC + y];
						// coloro
						// doswn.doOnRow(rigaCenterSup, r + x);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + r], tx, ty = y + c);
						// doswn.doOnRow(rigaCenterSup, rInf + x);
						doswn.doOnNode(molm, rigaCenterSup[tx = x + rInf], tx, ty);
						// doswn.doOnRow(rigaCenterInf, r + x);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + r], tx, ty = y + horizontalSymmetricOldC);
						// doswn.doOnRow(rigaCenterInf, rInf + x);
						doswn.doOnNode(molm, rigaCenterInf[tx = x + rInf], tx, ty);

						horizontalSymmetricOldC++;
						c--;
					}
				} // fine ciclo r
			}
		}
		return ret;
	}

	/**
	 * Ray 1 is not OutOfBoundsSafe.<br>
	 * See {@link #drawCircle_WithoutCkecks(int[][], int, int, int, int)} for
	 * further informations.
	 * <p>
	 * Name of the original algorithm in this class :
	 * {@link drawCircle_WithoutCkecks};
	 */
	@Override
	protected boolean runOnCircumference_Safe(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		boolean canUsErigaSup, canUsErigaInf, canUsErigaCenterSup, canUsErigaCenterInf, ret;
		int r, c, ray2, ray_1, halfRay, rInf, ray1, horizontalSymmetricOldC, c_x, hc_x, r_y, rInf_y, c_y, hc_y, r_x,
				rInf_x;
		double rRay;
		NodeMatrix[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf, rrrr;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		System.out.println("lol safe");
		if (ret = ray > 0) {
			rigaSup = null;
			rigaInf = null;
			rigaCenterSup = null;
			rigaCenterInf = null;
			if (ray == 1) {
				int tx;
				doswn.doOnNode(molm, matrix[y][x], x, y);
				rigaSup = matrix[++y];
				// doswn.doOnRow(rigaSup, x);
				doswn.doOnNode(molm, rigaSup[x], x, y);
				// doswn.doOnRow(rigaSup, x + 2);
				doswn.doOnNode(molm, rigaSup[tx = ++x + 1], tx, y);
				// doswn.doOnItem(matrix, x + 1, y);
				// doswn.doOnItem(matrix, x + 1, y + 2);
				doswn.doOnNode(molm, matrix[++y][x], x, y);

			} else {
				r = c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + ray % 2;
				ray1 = ray + 1;
				rRay = ray + 0.5;

				{// disegno i punti cardinali
					int ray_y = ray + y, ray2_x, ray_x, ray2_y;
					if (ray_y >= 0 && ray_y < matrix.length) {
						rigaSup = matrix[ray_y];
						if (x >= 0 && x < rigaSup.length) {
							// doswn.doOnRow(rigaSup, x);
							doswn.doOnNode(molm, rigaSup[x], x, ray_y);
						}
						ray2_x = ray2 + x;
						if (ray2_x >= 0 && ray2_x < rigaSup.length) {
							// doswn.doOnRow(rigaSup, ray2);
							doswn.doOnNode(molm, rigaSup[ray2], ray2, ray_y);
						}
					}
					ray_x = ray + x;
					if (ray_x >= 0) {
						if (y >= 0 && y < matrix.length) {
							rrrr = matrix[y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								// doswn.doOnRow(rrrr, ray_x);
								doswn.doOnNode(molm, rrrr[ray_x], ray_x, y);
							}
						}
						ray2_y = ray2 + y;
						if (ray2_y >= 0 && ray2_y < matrix.length) {
							rrrr = matrix[ray2_y];
							if (ray_x >= 0 && ray_x < rrrr.length) {
								// doswn.doOnRow(rrrr, ray_x);
								doswn.doOnNode(molm, rrrr[ray_x], ray_x, ray2_y);
							}
						}
					}
				} // fine blocco punti cardinali

				horizontalSymmetricOldC = ray1;
				rInf = ray2;
				c = ray_1;
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					r_y = r + y;
					canUsErigaSup = r_y >= 0 && r_y < matrix.length;
					if (canUsErigaSup) {
						rigaSup = matrix[r_y];
					}

					rInf_y = rInf + y;
					canUsErigaInf = rInf_y >= 0 && rInf_y < matrix.length;
					if (canUsErigaInf) {
						rigaInf = matrix[rInf_y];
					}

					while (0 < c && doswn.canContinueCycling() && (Math.hypot(ray - c, (ray - r)) < rRay)) {

						c_x = c + x;
						hc_x = horizontalSymmetricOldC + x;
						if (canUsErigaSup) {
							if (c_x >= 0 && c_x < rigaSup.length) {
								// doswn.doOnRow(rigaSup, c_x);
								doswn.doOnNode(molm, rigaSup[c_x], c_x, r_y);
							}
							if (hc_x >= 0 && hc_x < rigaSup.length) {
								// doswn.doOnRow(rigaSup, hc_x);
								doswn.doOnNode(molm, rigaSup[hc_x], hc_x, r_y);
							}
						}
						if (canUsErigaInf) {
							if (c_x >= 0 && c_x < rigaInf.length) {
								// doswn.doOnRow(rigaInf, c_x);
								doswn.doOnNode(molm, rigaInf[c_x], c_x, rInf_y);
							}
							if (hc_x >= 0 && hc_x < rigaInf.length) {
								// doswn.doOnRow(rigaInf, hc_x);
								doswn.doOnNode(molm, rigaInf[hc_x], hc_x, rInf_y);
							}
						}

						c_y = c + y;
						canUsErigaCenterSup = c_y >= 0 && c_y < matrix.length;
						if (canUsErigaCenterSup) {
							rigaCenterSup = matrix[c_y];
						}
						hc_y = horizontalSymmetricOldC + y;
						canUsErigaCenterInf = hc_y >= 0 && hc_y < matrix.length;
						if (canUsErigaCenterInf) {
							rigaCenterInf = matrix[hc_y];
						}

						// coloro
						r_x = r + x;
						rInf_x = rInf + x;
						if (canUsErigaCenterSup) {
							if (r_x >= 0 && r_x < rigaCenterSup.length) {
								// doswn.doOnRow(rigaCenterSup, r_x);
								doswn.doOnNode(molm, rigaCenterSup[r_x], r_x, c_y);
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterSup.length) {
								// doswn.doOnRow(rigaCenterSup, rInf_x);
								doswn.doOnNode(molm, rigaCenterSup[rInf_x], rInf_x, c_y);
							}
						}
						if (canUsErigaCenterInf) {
							if (r_x >= 0 && r_x < rigaCenterInf.length) {
								// doswn.doOnRow(rigaCenterInf, r_x);
								doswn.doOnNode(molm, rigaCenterInf[r_x], r_x, hc_y);
							}
							if (rInf_x >= 0 && rInf_x < rigaCenterInf.length) {
								// doswn.doOnRow(rigaCenterInf, rInf_x);
								doswn.doOnNode(molm, rigaCenterInf[rInf_x], rInf_x, hc_y);
							}
						}

						horizontalSymmetricOldC++;
						c--;
					}

				} // fine ciclo r
			}
		}
		return ret;
	}

	//

	// TODO CIRCLE

	protected static void fillCircle_Safe_WorksPerfect(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, squareRay, ray2, rInf, ray1, ray_1, horizontalSymmetricC, oldc, oldHorizC, halfRay, l,
				yPlusDiameter/* , logray */, height, ytemp, xtemp, ty, trc, trr;
		// double rRay;
		NodeMatrix[] rigaSup, rigaInf, rigaCenterSup, rigaCenterInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;

		height = matrix.length;
		if (ray > 0) {
			if (ray == 1) {
				int tx;

				// il punto in alto
				if (y >= 0 && y < matrix.length) {
					if (allInside(molm, x + 1, y, height)) {
						// if (allInside(molm, x + 1, y, height)) {
						// doswn.doOnItem(matrix, x + 1, y);
						doswn.doOnNode(molm, matrix[y][x + 1], x + 1, y);
					}
				}
				// la riga in centro
				if (MathUtilities.isAtMostPositive(ty = y + 1, height)) {
					rigaSup = matrix[ty];
					if (allInside(molm, x, ty, height)) {
						// doswn.doOnRow(rigaSup, x);
						doswn.doOnNode(molm, rigaSup[x], x, ty);
					}
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 1);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
					if (allInside(molm, tx = x + 2, ty, height)) {
						// doswn.doOnRow(rigaSup, x + 2);
						doswn.doOnNode(molm, rigaSup[tx], tx, ty);
					}
				}
				// punto in basso
				if (MathUtilities.isAtMostPositive(ty = y + 2, height)) {
					if (allInside(molm, tx = x + 1, ty, height)) {
						// doswn.doOnItem(matrix, x + 1, y + 2);
						doswn.doOnNode(molm, matrix[ty][tx], tx, ty);
					}
				}
			} else {
				r = 0;
				c = 0;
				ray2 = ray << 1;
				ray_1 = ray - 1;
				halfRay = (ray >> 1) + (ray & 0x1);
				ray1 = ray + 1;
				// rRay = ray + 0.5;
				squareRay = ray * ray;

				// disegno i punti cardinali
				if (MathUtilities.isAtMostPositive(ytemp = y + ray, height)) {
					rigaSup = matrix[ytemp];
					if (allInside(molm, x, ytemp, height)) {
						// doswn.doOnRow(rigaSup, x);
						doswn.doOnNode(molm, rigaSup[x], x, ytemp);
					}
					if (allInside(molm, xtemp = ray2 + x, ytemp, height)) {
						// doswn.doOnRow(rigaSup, xtemp);
						doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
					}
				}
				xtemp = ray + x;
				if (allInside(molm, xtemp, y, height)) {
					// doswn.doOnItem(matrix, xtemp, y);
					doswn.doOnNode(molm, matrix[y][xtemp], xtemp, y);
				}
				if (allInside(molm, xtemp, ytemp = y + ray2, height)) {
					// doswn.doOnItem(matrix, xtemp, ytemp);
					doswn.doOnNode(molm, matrix[ytemp][xtemp], xtemp, y);
				}
				/*
				 * aggiusto le righe in quanto inizio non dalla riga centrale,
				 * ma dalla prima e dal centro di essa rigaSup = m[0]; rigaInf =
				 * m[ray2];
				 */
				horizontalSymmetricC = ray1;
				rInf = ray2;
				c = ray_1;
				yPlusDiameter = y + ray2;

				// logray = MetodiVari.bitMinNeeded(ray);
				for (r = 0; r < halfRay && doswn.canContinueCycling(); r++, rInf--) {

					// aggiorno le righe su cui devo lavorare

					rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r, height) ? matrix[ytemp] : null;
					rigaInf = MathUtilities.isAtMostPositive(ty = y + rInf, height) ? matrix[ty] : null;
					/*
					 * ora sposto il puntatore "c" dal centro verso l'origine,
					 * fino a quando non supero il cerchio ideale, al che
					 * rientro .. uso ray1 per questioni di approssimazione
					 * grafica.. dopodichè traccio la congiungente
					 */

					oldc = c;
					oldHorizC = horizontalSymmetricC;

					while (c > 0 && doswn.canContinueCycling() &&
					// (Math.hypot((ray - c), (ray - r)) < rRay)
							((trc = ray - c) * trc + (trr = ray - r) * trr) < squareRay) {
						/*
						 * Sapendo che il cerchio l'ho diviso in otto spicchi,
						 * coloro prima la fetta che dall'(abituale) angolo 90
						 * va fino al 135, poi tutti i simmetrici .. il primo è
						 * il più a sinistra di tutti, seguito da quello in
						 * range (90-45), a quello (270-225), poi (270-315)
						 */
						if (rigaSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
						}
						/*
						 * poi i VERTICALI D: ... prima (180-135) , poi (0-45),
						 * poi (180-225), poi (0-315)
						 */
						// ottengo i puntatori alle righe per ottimizzare;

						rigaCenterSup = MathUtilities.isAtMostPositive(ytemp = y + c, height) ? matrix[ytemp] : null;
						rigaCenterInf = MathUtilities.isAtMostPositive(ty = y + horizontalSymmetricC, height)
								? matrix[ty] : null;
						// coloro
						if (rigaCenterSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + r, rigaCenterSup.length)) {
								// doswn.doOnRow(rigaCenterSup, xtemp);
								doswn.doOnNode(molm, rigaCenterSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + rInf, rigaCenterSup.length)) {
								// doswn.doOnRow(rigaCenterSup, xtemp);
								doswn.doOnNode(molm, rigaCenterSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaCenterInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + r, rigaCenterInf.length)) {
								// doswn.doOnRow(rigaCenterInf, xtemp);
								doswn.doOnNode(molm, rigaCenterInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + rInf, rigaCenterInf.length)) {
								// doswn.doOnRow(rigaCenterInf, xtemp);
								doswn.doOnNode(molm, rigaCenterInf[xtemp], xtemp, ty);
							}
						}

						// disegno le righe vicine al centro

						// if (r <= logray) {
						l = (ray2 - (r << 1)) - 1;
						runHorizontalSpan_Safe(x + r + 1, y + c, l, molm, doswn);
						runHorizontalSpan_Safe(x + r + 1, yPlusDiameter - c, l, molm, doswn);
						// }
						horizontalSymmetricC++;
						c--;
					}

					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Safe(x + oldc + 1, r + y, l, molm, doswn);
					runHorizontalSpan_Safe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);
				} // fine ciclo r
					// last line, the horizontal diameter
				runHorizontalSpan_Safe(x + 1, y + ray, ray2 - 1, molm, doswn);
			}
		}
	}

	protected static void fillCircle_Safe_Tuned_OverRay8(MatrixObjectLocationManager molm, int x, int y, int ray,
			DoSomethingWithNode doswn) {
		int r, c, halfray, diameter, horizontalSymmetricC, oldc, oldHorizC, yPlusDiameter, l, height, ytemp, xtemp, ty,
				squareRay, trc, trr;
		NodeMatrix[] rigaSup, rigaInf;
		NodeMatrix[][] matrix;
		matrix = molm.matrix;
		if (ray > 0) {
			if (ray <= 8) {
				System.out.println("SWITCH TO LITTLER ONE : " + ray);
				fillCircle_Safe_WorksPerfect(molm, x, y, ray, doswn);
			} else {
				/**
				 * Idea : ciclo dalla cima fino a metà raggio, disegnando righe
				 * orizzontali. La sezione da metà raggio fino al "diametro
				 * orizzontale", è speculare alla prima, quindi prendo le
				 * coordinate similari e traccio le linee orizzontali rimanenti.
				 * Infatti, prima disegno la cima e il fondo del cerchio,
				 * espandendomi e avvicinandomi al centro, poi per simmetria
				 * disegno dai punti di estrema sinistra e destra avvicinandomi
				 * ai poli. <br>
				 * Infine, traccio il diametro orizzontale.<br>
				 * Per ogni riga, tengo traccia del punto (della "x") da cui
				 * sono partito e mi muovo in moto retrogrado fintanto che
				 * rimango "entro il cerchio". Nel mentre, disegno. Appena esco,
				 * traccio la linea orizzontale a partire da quel punto-traccia,
				 * poi lo aggiorno.
				 */
				halfray = (ray >> 1) - 1/* + (ray & 0x1) */;
				r = -1;
				c = oldc = ray - 1;
				horizontalSymmetricC = oldHorizC = ray + 1;
				diameter = (ray << 1) + 1;
				// rRay = ray + 0.5;
				squareRay = ray * ray;
				yPlusDiameter = (y + diameter) - 1;
				height = matrix.length;

				while (++r < halfray && doswn.canContinueCycling()) {

					rigaSup = MathUtilities.isAtMostPositive(ytemp = y + r, height) ? matrix[ytemp] : null;
					rigaInf = MathUtilities.isAtMostPositive(ty = yPlusDiameter - r, height) ? matrix[ty] : null;

					while (0 <= c && doswn.canContinueCycling() && (
					// Math.hypot(ray - c, (ray - r)) < rRay)
					squareRay > ((trc = ray - c) * trc + (trr = ray - r) * trr))) {

						if (rigaSup != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaSup.length)) {
								// doswn.doOnRow(rigaSup, xtemp);
								doswn.doOnNode(molm, rigaSup[xtemp], xtemp, ytemp);
							}
						}
						if (rigaInf != null) {
							if (MathUtilities.isAtMostPositive(xtemp = x + c, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
							if (MathUtilities.isAtMostPositive(xtemp = x + horizontalSymmetricC, rigaInf.length)) {
								// doswn.doOnRow(rigaInf, xtemp);
								doswn.doOnNode(molm, rigaInf[xtemp], xtemp, ty);
							}
						}

						/*
						 * ora disegno il corrispettivo simmetrico a partire
						 * dalle estremità sinistra e destra
						 */
						// temp = m[yOfCenter - c];
						// temp[x + r] = temp[xPlusDiameter - r] );

						l = diameter - (r << 1);
						runHorizontalSpan_Safe(x + r, y + c, l, molm, doswn);
						// drawHorizontalSpan_Safe(x + r, y +
						// (horizontalSymmetricC -
						// 1), l, molm, doswn);
						runHorizontalSpan_Safe(x + r, yPlusDiameter - c, l, molm, doswn);

						c--;
						horizontalSymmetricC++;
					}

					/*
					 * drawHorizontalSpan_Safe(x + oldc, y + r, ((ray - c) << 1)
					 * + 1, molm, doswn);
					 */
					l = (oldHorizC - oldc) - 1;
					runHorizontalSpan_Safe(x + oldc + 1, y + r, l, molm, doswn);
					runHorizontalSpan_Safe(x + oldc + 1, yPlusDiameter - r, l, molm, doswn);

					oldc = c;
					oldHorizC = horizontalSymmetricC;
				}
				// diametro orizzontale
				runHorizontalSpan_Safe(x, y + ray, diameter, molm, doswn);
			}
		}
	}
}