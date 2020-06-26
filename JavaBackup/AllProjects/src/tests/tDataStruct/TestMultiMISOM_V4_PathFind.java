package tests.tDataStruct;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dataStructures.MapTreeAVL;
import dataStructures.isom.MultiISOMRetangularCaching;
import dataStructures.isom.MultiISOMRetangularMap;
import dataStructures.isom.MultiISOMRetangularMap.MISOMLocatedInSpace;
import dataStructures.isom.ObjLocatedCollectorIsom;
import dataStructures.isom.matrixBased.MISOM_SingleObjInNode;
import dataStructures.isom.matrixBased.MatrixInSpaceObjectsManager;
import geometry.AbstractShape2D;
import geometry.ObjectShaped;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.impl.ObjCollector;
import stuffs.logic.AtomLogicProposition;
import tools.Comparators;
import tools.NumberManager;

public class TestMultiMISOM_V4_PathFind {
	public static final int MAXIMUM_SUBMAPS_EACH_SECTION = 4, MINIMUM_DIMENSION_MAP = 4, PIXEL_EACH_CELL = 20;

	public interface RectsProducersToTest {
		public MyRectangle[] produceTestSet();
	}

	protected static final RectsProducersToTest[] MyRectangle_TEST_PRODUCERS = {
			//
			() -> new MyRectangle[] { //
					new MyRectangle(-4, -2, 6, 7).setName("a"), //
					new MyRectangle(2, 1, 4, 3).setName("b"), //
					new MyRectangle(-3, 5, 3, 5).setName("c"), //
					new MyRectangle(0, 7, 4, 2).setName("d"), //
					new MyRectangle(4, 4, 3, 8).setName("e"), //
					new MyRectangle(-1, 11, 5, 1).setName("f"), //
					new MyRectangle(1, 5, 2, 1).setName("g"), //
					new MyRectangle(7, 0, 2, 6).setName("h") //
			}, //
			() -> new MyRectangle[] { //
					new MyRectangle(-4, -2, 6, 7).setName("a"), //
					new MyRectangle(2, 1, 4, 3).setName("b"), //
					new MyRectangle(-3, 5, 3, 5).setName("c"), //
					new MyRectangle(0, 7, 4, 2).setName("d"), //
					new MyRectangle(4, 4, 3, 8).setName("e"), //
					new MyRectangle(-1, 11, 5, 1).setName("f"), //
					new MyRectangle(1, 5, 2, 1).setName("g"), //
					new MyRectangle(7, 0, 2, 6).setName("h"), //
					new MyRectangle(-5, 8, 2, 6).setName("dx"), //
					new MyRectangle(10, -2, 5, 2).setName("dy"), //
					new MyRectangle(11, 0, 2, 4).setName("dz"), //
					new MyRectangle(13, 2, 3, 3).setName("ea"), //
					new MyRectangle(8, 8, 2, 5).setName("ed"), //
					new MyRectangle(10, 10, 2, 2).setName("ee"), //
					new MyRectangle(10, 7, 4, 2).setName("eg"), //
					new MyRectangle(13, 9, 3, 5).setName("eh"), //
					new MyRectangle(-3, 13, 5, 2).setName("ei"), //
					new MyRectangle(2, 13, 3, 1).setName("ej"), //
					new MyRectangle(5, 13, 2, 2).setName("ek"), //
					new MyRectangle(7, 14, 4, 1).setName("el"), //
					new MyRectangle(11, 13, 2, 2).setName("em"), //
					new MyRectangle(2, -5, 5, 4).setName("en"), //
					new MyRectangle(7, -5, 17, 2).setName("eo"), //
					new MyRectangle(19, -3, 2, 6).setName("ep"), //
					new MyRectangle(21, 1, 3, 8).setName("eq"), //
					new MyRectangle(18, 7, 3, 5).setName("er"), //
					new MyRectangle(16, 12, 4, 3).setName("es"), //
					new MyRectangle(13, 11, 1, 1).setName("et"), //
					new MyRectangle(21, 10, 2, 3).setName("eu"), //
					new MyRectangle(18, 4, 2, 2).setName("ev"), //
			}, //
			() -> new MyRectangle[] { //
					new MyRectangle(-4, -2, 6, 7).setName("a"), //
					new MyRectangle(2, 1, 4, 3).setName("b"), //
					new MyRectangle(-3, 5, 3, 5).setName("c"), //
					new MyRectangle(0, 7, 4, 2).setName("d"), //
					new MyRectangle(-1, 11, 5, 1).setName("f"), //
					new MyRectangle(1, 5, 2, 1).setName("g"), //
					new MyRectangle(7, 0, 2, 6).setName("h"), //
					new MyRectangle(-5, 8, 2, 6).setName("dx"), //
					new MyRectangle(10, -2, 5, 2).setName("dy"), //
					new MyRectangle(8, 8, 2, 5).setName("ed"), //
					new MyRectangle(10, 10, 2, 2).setName("ee"), //
					new MyRectangle(10, 7, 4, 2).setName("eg"), //
					new MyRectangle(13, 9, 3, 5).setName("eh"), //
					new MyRectangle(-3, 13, 5, 2).setName("ei"), //
					new MyRectangle(2, 13, 3, 1).setName("ej"), //
					new MyRectangle(5, 13, 2, 2).setName("ek"), //
					new MyRectangle(7, 14, 4, 1).setName("el"), //
					new MyRectangle(11, 13, 2, 2).setName("em"), //
					new MyRectangle(2, -5, 5, 4).setName("en"), //
					new MyRectangle(7, -5, 17, 2).setName("eo"), //
					new MyRectangle(19, -3, 2, 6).setName("ep"), //
					new MyRectangle(21, 1, 3, 8).setName("eq"), //
					new MyRectangle(18, 7, 3, 5).setName("er"), //
					new MyRectangle(16, 12, 4, 3).setName("es"), //
					new MyRectangle(21, 10, 2, 3).setName("eu"), //
					new MyRectangle(18, 4, 2, 2).setName("ev"), //
					new MyRectangle(3, -1, 2, 1).setName("dz"), //
					new MyRectangle(17, 0, 2, 2).setName("ea"), //
					new MyRectangle(16, -2, 2, 2).setName("eb"), //
					new MyRectangle(17, 7, 1, 2).setName("eg"), //
					new MyRectangle(9, 5, 5, 1).setName("eh"), //
					new MyRectangle(10, 0, 2, 4).setName("ej"), //
					new MyRectangle(12, 1, 3, 2).setName("ek"), //
					new MyRectangle(13, 4, 3, 1).setName("el"), //
					new MyRectangle(15, 5, 2, 3).setName("em"), //
					new MyRectangle(-2, -5, 2, 2).setName("en"), //
					new MyRectangle(-4, -4, 2, 2).setName("eo"), //
					new MyRectangle(4, 4, 3, 2).setName("ep"), //
					new MyRectangle(4, 8, 3, 4).setName("eq"), //
					new MyRectangle(5, 6, 2, 2).setName("er"), //
			}, //
			() -> new MyRectangle[] { // an heart
					new MyRectangle(12, 7, 3, 2).setName("c"), //
					new MyRectangle(11, 5, 2, 2).setName("d"), //
					new MyRectangle(14, 5, 2, 2).setName("e"), //
					new MyRectangle(8, 4, 3, 2).setName("f"), //
					new MyRectangle(6, 5, 2, 2).setName("g"), //
					new MyRectangle(4, 6, 2, 4).setName("h"), //
					new MyRectangle(5, 10, 2, 3).setName("i"), //
					new MyRectangle(7, 12, 2, 2).setName("l"), //
					new MyRectangle(8, 14, 2, 3).setName("m"), //
					new MyRectangle(10, 16, 2, 2).setName("n"), //
					new MyRectangle(12, 17, 3, 2).setName("o"), //
					new MyRectangle(15, 16, 2, 2).setName("p"), //
					new MyRectangle(17, 14, 2, 3).setName("q"), //
					new MyRectangle(18, 12, 2, 2).setName("r"), //
					new MyRectangle(20, 10, 2, 3).setName("s"), //
					new MyRectangle(21, 6, 2, 4).setName("t"), //
					new MyRectangle(19, 5, 2, 2).setName("u"), //
					new MyRectangle(16, 4, 3, 2).setName("v"), //
			}, //
			() -> new MyRectangle[] { //
					new MyRectangle(-4, -2, 6, 7).setName("a"), //
					new MyRectangle(2, 1, 4, 3).setName("b"), //
					new MyRectangle(-3, 5, 3, 5).setName("c"), //
					new MyRectangle(0, 7, 4, 2).setName("d"), //
					new MyRectangle(-1, 11, 5, 1).setName("f"), //
					new MyRectangle(1, 5, 2, 1).setName("g"), //
					new MyRectangle(-5, 8, 2, 6).setName("dx"), //
					new MyRectangle(10, -2, 5, 2).setName("dy"), //
					new MyRectangle(8, 8, 2, 5).setName("ed"), //
					new MyRectangle(10, 10, 2, 2).setName("ee"), //
					new MyRectangle(10, 7, 4, 2).setName("eg"), //
					new MyRectangle(13, 9, 3, 5).setName("eh"), //
					new MyRectangle(-3, 13, 5, 2).setName("ei"), //
					new MyRectangle(2, 13, 3, 1).setName("ej"), //
					new MyRectangle(5, 13, 2, 2).setName("ek"), //
					new MyRectangle(7, 14, 4, 1).setName("el"), //
					new MyRectangle(11, 13, 2, 2).setName("em"), //
					new MyRectangle(2, -5, 5, 4).setName("en"), //
					new MyRectangle(19, -3, 2, 6).setName("ep"), //
					new MyRectangle(21, 1, 3, 8).setName("eq"), //
					new MyRectangle(18, 7, 3, 5).setName("er"), //
					new MyRectangle(16, 12, 4, 3).setName("es"), //
					new MyRectangle(21, 10, 2, 3).setName("eu"), //
					new MyRectangle(18, 4, 2, 2).setName("ev"), //
					new MyRectangle(3, -1, 2, 1).setName("dz"), //
					new MyRectangle(17, 0, 2, 2).setName("ea"), //
					new MyRectangle(16, -2, 2, 2).setName("eb"), //
					new MyRectangle(17, 7, 1, 2).setName("eg"), //
					new MyRectangle(9, 5, 5, 1).setName("eh"), //
					new MyRectangle(10, 0, 2, 4).setName("ej"), //
					new MyRectangle(12, 1, 3, 2).setName("ek"), //
					new MyRectangle(13, 4, 3, 1).setName("el"), //
					new MyRectangle(15, 5, 2, 3).setName("em"), //
					new MyRectangle(-2, -5, 2, 2).setName("en"), //
					new MyRectangle(-4, -4, 2, 2).setName("eo"), //
					new MyRectangle(4, 4, 3, 2).setName("ep"), //
					new MyRectangle(4, 8, 3, 4).setName("eq"), //
					new MyRectangle(5, 6, 2, 2).setName("er"), //
					new MyRectangle(7, 2, 2, 5).setName("dw"), //
					new MyRectangle(24, -4, 2, 6).setName("dx"), //
					new MyRectangle(7, -5, 3, 2).setName("dy"), //
					new MyRectangle(22, -5, 2, 2).setName("eh"), //
					new MyRectangle(10, -4, 4, 1).setName("ei"), //
					new MyRectangle(14, -5, 2, 2).setName("ej"), //
					new MyRectangle(16, -4, 2, 1).setName("ek"), //
					new MyRectangle(17, -5, 5, 1).setName("el"), //
					new MyRectangle(21, -3, 2, 1).setName("em"), //
			}, //
	};

	public static void main(String[] args) {
		MyRectangle[] rects;
		MultiISOMRetangularMap<Double> t;
		T_MISOM_GUI_V4 gui;
		Map<Integer, MyRectangle> mapIdToMyrect;
		Point ps, pe;
		List<Point> path;

		rects = MyRectangle_TEST_PRODUCERS[2].produceTestSet();
		for (MyRectangle r : rects) {
			System.out.println("- rectangle: " + r);
		}
		t = new MultiISOMRetangularCaching<>(4);
		t.setWeightManager(NumberManager.getDoubleManager());

		gui = new T_MISOM_GUI_V4(t);
		gui.rebuildGUI();
		gui.resetRects(rects);
		rects = null;
		mapIdToMyrect = gui.rects;
		ps = new Point(-3, -1);
		pe = new Point(4, 7);
		System.out.println("start pathfinding with points:\n\t ps: " + ps + "\n\t pe: " + pe);
		path = t.getPath(ps, pe);
		if (path != null) {
			System.out.println("\n\n\n found " + path.size() + " points");
			path.forEach(p -> {
				System.out.println("\t p: (x:" + p.x + ", y:" + p.y //
						+ ") - contained in: " + //
				nameRectContaining(t, p, mapIdToMyrect));
			});
			gui.showPath(path);
		}
	}

	static String nameRectContaining(MultiISOMRetangularMap<Double> t, Point p,
			Map<Integer, MyRectangle> mapIdToMyrect) {
		MISOMLocatedInSpace<Double> m;
		m = t.getMapLocatedContaining(p);
		if (m == null)
			return "null";
		return mapIdToMyrect.get(m.getID()).getName();
	}

	//

	//

	//

	//

	static class T_MISOM_GUI_V4 {
		static int idProgNewRect = 100;
		boolean isStartPathfind, isPointwisePathfind;
		JFrame win;
		JPanel jp;
		JScrollPane jsp;
		Map<Integer, MyRectangle> rects = null;
		MultiISOMRetangularMap<Double> t;
		MyRectangle newRect = null;
		Point pStartDrawningRect = null, pEndDrawningRect = null;
		Point startPathfind, endPathfind;
//		List<Point > pathFound;
		int[] xPath, yPath;

		public T_MISOM_GUI_V4(MultiISOMRetangularMap<Double> t) {
			super();
			this.t = t;
			isStartPathfind = true;
			startPathfind = endPathfind = null;
//			pathFound=null;
			xPath = yPath = null;
			isPointwisePathfind = true;
		}

		void rebuildGUI() {
			MouseAdapter ma;
			KeyAdapter ka;
			win = new JFrame("Test Multi ISOM");
			win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jp = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					paintRectsAndSections(g);
				}
			};
			jsp = new JScrollPane(jp);
			jsp.setViewportView(jp);
			win.add(jsp);
			win.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					Dimension d;
					d = win.getSize();
					d.width -= 10;
					d.height -= 35;
					jsp.setSize(d);
					jsp.setPreferredSize(d);
				}
			});
			ma = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) { onMouseClick(e); }

				@Override
				public void mousePressed(MouseEvent e) {
					onStartDrawningNewRectangle(e.getPoint());
					jp.repaint();
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					pEndDrawningRect = e.getPoint();
					pEndDrawningRect.x /= PIXEL_EACH_CELL;
					pEndDrawningRect.y /= PIXEL_EACH_CELL;
					applyMultiMapOffset(pEndDrawningRect);
					updateStartEndPoinNewRectangle();
					jp.repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					Point p;
					p = e.getPoint();
					p.x /= PIXEL_EACH_CELL;
					p.y /= PIXEL_EACH_CELL;
					onEndDrawningNewRectangle(p, e.getButton() == 1);
					jp.repaint();
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					Point p;
					p = e.getPoint();
					p.x /= PIXEL_EACH_CELL;
					p.y /= PIXEL_EACH_CELL;
					applyMultiMapOffset(p);
					win.setTitle("Mouse at: (x: " + p.x + ", y: " + p.y + ")");
				}
			};
			jp.addMouseListener(ma);
			jp.addMouseMotionListener(ma);
			ka = new KeyAdapter() {
//				@Override
//				public void keyTyped(KeyEvent e) { printRects(); }

				@Override
				public void keyReleased(KeyEvent e) { printRects(); }
			};
			jp.addKeyListener(ka);
			jsp.addKeyListener(ka);
			win.addKeyListener(ka);
			win.setSize(500, 500);
			win.setVisible(true);
		}

		//

		void printRects() {
			System.out.println("print rect:\n ()->new MyRectangle[] {//");
			rects.forEach((i, r) -> {
				System.out.println("new MyRectangle(" + r.x + ", " + r.y + ", " + r.width + ", " + r.height
						+ ").setName(\"" + r.name + "\"), //");
			});
			System.out.println("}, //");
		}

		void addRect(MyRectangle r) {
			MatrixInSpaceObjectsManager<Double> map;
			MISOMLocatedInSpace<Double> w;
			if (rects == null) {
				rects = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
						Comparators.INTEGER_COMPARATOR);
			}
			map = new MISOM_SingleObjInNode<Double>(true, r.width, r.height, NumberManager.getDoubleManager());
//			map.setPathFinder(t.getPathFinder());
			w = t.addMap(map, r.x, r.y);
			System.out.println("adding: " + r.name + ", and id: " + w.ID);
			rects.put(w.ID, r);
		}

		void resetRects(MyRectangle[] recs) {
			t.clear();
			for (MyRectangle r : recs) {
				addRect(r);
			}
			t.rebuild();
			jp.setSize(t.getWidth() * PIXEL_EACH_CELL + 200, t.getHeight() * PIXEL_EACH_CELL + 200);
			jp.setPreferredSize(jp.getSize());
		}

		void applyMultiMapOffset(Point p) {
//			if (t.getxLeftTop() < 0)
			p.x += t.getxLeftTop();
//			if (t.getyLeftTop() < 0)
			p.y += t.getyLeftTop();
		}

		// TODO paint
		void paintRectsAndSections(Graphics g) {
			Color c;
			int minx, miny;
			minx = t.getxLeftTop();
			miny = t.getyLeftTop();
			win.setTitle("MINX: " + minx + ", MINY: " + miny);
			c = Color.GREEN.darker();
			if (t != null && t.getMisomsHeld() != null) {
//				for (MyRectangle r : rects) {
				t.getMapsLocatedInSpace().forEach((id, wrapper) -> {
					MyRectangle r;
					r = rects.get(id);
					if (r != null) {
						g.setColor(c);
						g.fillRect(//
								(r.x - minx) * PIXEL_EACH_CELL//
						, (r.y - miny) * PIXEL_EACH_CELL//
						, (r.width) * PIXEL_EACH_CELL//
						, (r.height) * PIXEL_EACH_CELL//
						);
						g.setColor(Color.BLUE);
						g.drawRect(//
								(r.x - minx) * PIXEL_EACH_CELL//
						, (r.y - miny) * PIXEL_EACH_CELL//
						, (r.width) * PIXEL_EACH_CELL//
						, (r.height) * PIXEL_EACH_CELL//
						);
						g.setColor(Color.BLACK);
						g.drawString(r.name, (((r.x - minx) + (r.width >> 1)) * PIXEL_EACH_CELL),
								(((r.y - miny) + (r.height >> 1))) * PIXEL_EACH_CELL);
						g.drawString(r.name, (r.x - minx) * PIXEL_EACH_CELL, (r.y - miny) * PIXEL_EACH_CELL);
					}
//					else
//						System.out.println("can't find the r with id :" + id);
				}//
				);
			}
			if (this.newRect != null) {
				g.setColor(Color.RED);
				g.drawRect(//
						((t.getxLeftTop() >= 0) ? this.newRect.x : //
								this.newRect.x - t.getxLeftTop()) * PIXEL_EACH_CELL,
						((t.getyLeftTop() >= 0) ? this.newRect.y : //
								this.newRect.y - t.getyLeftTop()) * PIXEL_EACH_CELL, //
						(this.newRect.width) * PIXEL_EACH_CELL//
						, (this.newRect.height) * PIXEL_EACH_CELL);
			}
			if (t != null && t.getRoot() != null) { paintSubdivision(g); }
			if (xPath != null && xPath.length > 0) {
				g.setColor(Color.BLACK); // .darker()
				g.drawPolyline(xPath, yPath, xPath.length);
			}
//			GraphicTools.paintGrid(g, PIXEL_EACH_CELL * t.getWidth(), PIXEL_EACH_CELL * t.getHeight(), PIXEL_EACH_CELL);
		}

		void paintSubdivision(Graphics g) {
			int halfDepth, minx, miny;
			minx = t.getxLeftTop() - 1;
			miny = t.getyLeftTop() - 1;
//			NodeMultiISOMRectangular n;
			MultiISOMRetangularMap<Double>.NodeQuadtreeMultiISOMRectangular n;
			LinkedList<MultiISOMRetangularMap<Double>.NodeQuadtreeMultiISOMRectangular> nodes;
			nodes = new LinkedList<>();
			n = t.getRoot();
			nodes.add(n);
			while (!nodes.isEmpty()) {
				n = nodes.removeFirst();
				g.setColor(Color.RED);
				g.drawString(Integer.toString(n.getDepth())//
						, (n.getXMiddle() - minx) * PIXEL_EACH_CELL //
						, (n.getYMiddle() - miny) * PIXEL_EACH_CELL);
				// draw subsections
				if (!n.isLeaf()) {
					g.setColor(Color.LIGHT_GRAY);
					halfDepth = n.getDepth() >> 1;
					// horizontal
					g.fillRect(//
							(n.getX() - minx) * PIXEL_EACH_CELL//
							, (n.getYMiddle() - (halfDepth + miny)) * PIXEL_EACH_CELL//
							, n.getWidth() * PIXEL_EACH_CELL//
							, (1 + (t.getMaxDepth() - n.getDepth())) // * PIXEL_EACH_CELL
					);
//vertical
					g.fillRect(//
							(n.getXMiddle() - (halfDepth + minx)) * PIXEL_EACH_CELL //
							, (n.getY() - miny) * PIXEL_EACH_CELL
							//
							, (1 + (t.getMaxDepth() - n.getDepth())) // * PIXEL_EACH_CELL //
							, n.getHeight() * PIXEL_EACH_CELL);
					n.forEachSubsection((s) -> {
						if (s != null)
							nodes.add(s);
					});
				}
			}
		}

		//

		void onMouseClick(MouseEvent me) {
			int mouseButton;
			Point whereClicked;
//			MyRectangle mapFound;
			MISOMLocatedInSpace<Double> mapWrapper;
//			MatrixInSpaceObjectsManager<Double> map;
			whereClicked = me.getPoint();
			whereClicked.x /= PIXEL_EACH_CELL;
			whereClicked.y /= PIXEL_EACH_CELL;
			// consider the offset
			applyMultiMapOffset(whereClicked);
			System.out.println("finding a map on " + whereClicked + " ..");
//			map = t.getMISOMContaining(whereClicked);
			mapWrapper = t.getMapLocatedContaining(whereClicked);
			System.out.println(".. map found: " + (mapWrapper == null ? "null" : rects.get(mapWrapper.ID)));
			mouseButton = me.getButton();
			System.out.println("me.getButton(): " + mouseButton);
			if (mouseButton == MouseEvent.BUTTON1 || mouseButton == MouseEvent.BUTTON2) {
				performPathfind(whereClicked);
			} else if (mouseButton == MouseEvent.BUTTON3) {
				if (mapWrapper != null) {
					t.removeMap(mapWrapper);
					rects.remove(mapWrapper.ID);
				}
			}
//			mapFound =    rects.get(map.id);
		}

		void updateStartEndPoinNewRectangle() {
			int temp;
			Point ps, pe;
			if (this.pStartDrawningRect.x == this.pEndDrawningRect.x
					|| this.pStartDrawningRect.y == this.pEndDrawningRect.y)
				return;
			ps = new Point(this.pStartDrawningRect);
			pe = new Point(this.pEndDrawningRect);
			if (ps.x > pe.x) {
				temp = ps.x;
				ps.x = pe.x;
				pe.x = temp;
			}
			if (ps.y > pe.y) {
				temp = ps.y;
				ps.y = pe.y;
				pe.y = temp;
			}
			if (newRect == null) {
				this.newRect = new MyRectangle(ps, new Dimension( //
						pe.x - ps.x, //
						pe.y - ps.y));
				this.newRect.setName( // use the atom to make the name cool
						(new AtomLogicProposition(true, idProgNewRect++)).getName());
			} else {
				this.newRect.x = ps.x;
				this.newRect.y = ps.y;
				this.newRect.width = pe.x - ps.x;
				this.newRect.height = pe.y - ps.y;
			}
		}

		void onStartDrawningNewRectangle(Point pointStart) {
//			this.isRectangleDrawning = true;
			this.newRect = null;
			this.pEndDrawningRect = null;
			this.pStartDrawningRect = pointStart;
			pointStart.x /= PIXEL_EACH_CELL;
			pointStart.y /= PIXEL_EACH_CELL;
			// consider the offset
			applyMultiMapOffset(pointStart);
		}

		void onEndDrawningNewRectangle(Point pointEnd, boolean isLeftClick) {
			this.pEndDrawningRect = pointEnd;
			// consider the offset
			applyMultiMapOffset(pointEnd);
			updateStartEndPoinNewRectangle();
//			this.isRectangleDrawning = false;
			if (isLeftClick)
				buildAndAddMap();
			else {
//				collectAndDeleteObjects();
				collectAndDeleteMaps();
			}
			this.pStartDrawningRect = null;
			this.pEndDrawningRect = null;
		}

		void collectAndDeleteObjects() {
			ObjLocatedCollectorIsom<Double> c;
			ShapeRectangle shape;
			c = t.newObjLocatedCollector(null);
			shape = new ShapeRectangle(0, (int) newRect.getCenterX(), (int) newRect.getCenterY(), true,
					(int) newRect.getWidth(), (int) newRect.getHeight());
			t.// newNodeIsomProviderCaching().//
					runOnShape(shape, c);

			if (c.getCollectedObjects().size() > 0)
				c.getCollectedObjects().forEach(ol -> {
					MISOMLocatedInSpace<Double> w;
					System.out.println("\n\n got ol: " + ol);
					w = t.getMapLocatedContaining(ol.getLocation());
					System.out.println("and then removing object into map: " + (w == null ? "null" : w.ID));
//					t.removeMap(w);
//					this.rects.remove(w.IDInteger);
					w.misom.remove(ol);
				});
			else {
				System.out.println(" no objects found");
			}
		}

		void collectAndDeleteMaps() {
			MapRemover mr;
			ShapeRectangle shape;
//			c = t.newObjLocatedCollector(null);
			if (newRect == null)
				return;
			shape = new ShapeRectangle(0, (int) newRect.getCenterX(), (int) newRect.getCenterY(), true,
					(int) newRect.getWidth(), (int) newRect.getHeight());
			mr = new MapRemover(t);
			t.// newNodeIsomProviderCaching().//
					runOnShape(shape, mr);
			if (mr.mapsCollected.isEmpty()) {
				System.out.println("No map collected");
			} else {
				System.out.println("collected " + mr.mapsCollected.size() + " maps:");
				mr.mapsCollected.forEach(mlis -> {
					t.removeMap(mlis);
					System.out.println("removing: " + this.rects.get(mlis.ID).getName());
					this.rects.remove(mlis.ID);
				});
			}
//			if (c.getCollectedObjects().size() > 0)
//				c.getCollectedObjects().forEach(ol -> {
//					MISOMLocatedInSpace<Double> w;
//					System.out.println("\n\n got ol: " + ol);
//					w = t.getMISOMLocatedInSpaceContaining(ol.getLocation());
//					System.out.println("and then removing: " + (w == null ? "null" : w.ID));
//					t.removeMap(w);
//					this.rects.remove(w.IDInteger);
//				});
//			else {
//				System.out.println(" no objects found");
//			}
		}

		void buildAndAddMap() {
			if (this.newRect == null)
				return;
			System.out.println("\n\n adding new map: " + newRect);
//			t.addMap(newRect);
			addRect(newRect);
			this.newRect = null;
			jp.setSize(t.getWidth() * PIXEL_EACH_CELL + 200, t.getHeight() * PIXEL_EACH_CELL + 200);
			jp.setPreferredSize(jp.getSize());
			jp.repaint();
		}

		// TODO OOOOOOOOOOOOOOOOOOO
		// FARE PATH FINDING CHE IL CLICK SINISTRO IMPOSTA, IN MODO ALTERNATO,
		// INIZIO E FINE DEL PERCORSO .. E POI DISEGNA IL POLILINE

		void performPathfind(Point whereClicked) {
			List<Point> pathFound;
// consider the multi-map offset
//			applyMultiMapOffset(whereClicked); // YET APPLIED
			if (this.isStartPathfind) {
				isStartPathfind = false;
				this.startPathfind = whereClicked;
			} else {
				isStartPathfind = true;
				this.endPathfind = whereClicked;
			}
			if (this.startPathfind != null && this.endPathfind != null) {
				if (isPointwisePathfind) {
					pathFound = t.getPath(startPathfind, endPathfind);
				} else {
					ObjectShaped os;
					os = new ObjectShapedBase_V4();
					os.setShape(new ShapeRectangle(0, startPathfind.x + 10, startPathfind.y + 10, true, 20, 20));
					pathFound = t.getPath(os, endPathfind);
				}
				showPath(pathFound);
			} else
				System.out.println("no Path found: (from: " + this.startPathfind + ", to: " + this.endPathfind + ")");
		}

		void showPath(List<Point> pathFound) {
			int dx, dy;
			int len, halfPixelSize;
			final int[] i = { 0 };
			if (pathFound == null)
				return;
			len = pathFound.size();
			if (len == 0) {
				xPath = yPath = null;
				return;
			}
//			System.out.println("----- found path with " + len + " steps");
			xPath = new int[len];
			yPath = new int[len];
			dx = t.getxLeftTop();
			dy = t.getyLeftTop();
			halfPixelSize = PIXEL_EACH_CELL >> 1;
			pathFound.forEach(p -> {
				xPath[i[0]] = (p.x - dx) * PIXEL_EACH_CELL + halfPixelSize;
				yPath[i[0]++] = (p.y - dy) * PIXEL_EACH_CELL + halfPixelSize;
//				System.out.println("\t p: (x:" + p.x + ", y:" + p.y + ")");
			});
		}

		//

		protected static class ObjectShapedBase_V4 implements ObjectShaped {
			private static final long serialVersionUID = 65152087878L;
			AbstractShape2D shape;

			@Override
			public Integer getID() { return null; }

			@Override
			public void setShape(AbstractShape2D shape) { this.shape = shape; }

			@Override
			public AbstractShape2D getShape() { return shape; }
		}

		protected class MapRemover implements ObjCollector<MISOMLocatedInSpace<Double>> {
			private static final long serialVersionUID = 1L;

			protected MapRemover(MultiISOMRetangularMap<Double> nodeIsomProvider) {
				super();
				this.nodeIsomProvider = nodeIsomProvider;
				this.mapBackMapsCollected = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
						Comparators.INTEGER_COMPARATOR);
				this.mapsCollected = this.mapBackMapsCollected.toSetValue(m -> m.getID());
				filter = m -> m != null;
			}

			protected MultiISOMRetangularMap<Double> nodeIsomProvider;
			protected Set<MISOMLocatedInSpace<Double>> mapsCollected;
			protected MapTreeAVL<Integer, MISOMLocatedInSpace<Double>> mapBackMapsCollected;
			protected Predicate<MISOMLocatedInSpace<Double>> filter;

			// @Override
			public MultiISOMRetangularMap<Double> getNodeIsomProvider() { return nodeIsomProvider; }

//			@Override
			public void setNodeIsomProvider(MultiISOMRetangularMap<Double> nodeIsomProvider) {
				this.nodeIsomProvider = nodeIsomProvider;
			}

			@Override
			public Set<MISOMLocatedInSpace<Double>> getCollectedObjects() { return mapsCollected; }

			@Override
			public Predicate<MISOMLocatedInSpace<Double>> getTargetsFilter() { return filter; }

			@Override
			public MISOMLocatedInSpace<Double> getAt(Point location) {
				return nodeIsomProvider.getMapLocatedContaining(location);
			}
		}
	}
}