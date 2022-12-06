package tests.tDataStruct;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dataStructures.quadTree.Quadtree;

public class T_Quadtree_01<D> {
	public static final Point2D.Double[] INITIAL_POINTS = { //
			p(0, 7), //
			p(1, 4), //
			p(0, 2), //
			p(3, 7), //
			p(2, 3), //
			p(5, 0), //
			p(0, 8), //
			//
			p(3, 9), //
			p(5, 6), //
			p(6.5, 10.5), //
			p(7, 12), //
			p(6, 10.5), //
			p(0, 14), //
			//
			p(12, 12), //
			p(13, 14), //
			p(13, 4), //
			p(14, 13.5), //
			p(11, 12), //
			p(12, 12), //
			p(14, 14), //
			p(13.5, 14.5), //
			p(15, 9), //
			p(14.5, 12), //
			//
			p(32, 3), //
			p(17, 16), //
			p(25, 14), //
			p(30, 32), //

			//
			p(1, 1), //
			p(0.5, 3), //
			p(32, 32) //
	};

	public static Point2D.Double p(double x, double y) {
		return new Point2D.Double(x, y);
	}

	public T_Quadtree_01(Quadtree<Double, D> quadtree) {
		super();
		this.quadtree = quadtree;

		JPanel jpNorth;
		JButton jbPrint;
		fin = new JFrame("Quadtree");
		fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fin.setVisible(false);

		jp = new JPanel(new BorderLayout(2, 2));
		fin.add(jp);

		jpNorth = new JPanel(new FlowLayout());
		jp.add(jpNorth, BorderLayout.NORTH);
		jbPrint = new JButton("print");
		jpNorth.add(jbPrint);
		jbPrint.addActionListener(l -> {
			System.out.println(getQuadtree());
		});

		jpNorth.add(new JLabel("x:"));
		jtx = new JTextField("0");
		jpNorth.add(jtx);
		jpNorth.add(new JLabel("y:"));
		jty = new JTextField("0");
		jpNorth.add(jty);

		jbAdd = new JButton("Add point");
		jpNorth.add(jbAdd);
		jpNorth.add(new JLabel("max depth:"));
		jpNorth.add(jlDepth = new JLabel(Integer.toString(quadtree.getMaxDepth())));

		jpPaint = new JPanel() {
			private static final long serialVersionUID = 1L;

//			@Override
//			public void paintComponents(Graphics g) {
//				super.paintComponents(g);
//				paintTree(g);
//			}

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				paintTree(g);
			}
		};
		jpPaint.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		jp.add(jpPaint, BorderLayout.CENTER);

		//

		jbAdd.addActionListener(l -> {
			jbAdd.setEnabled(false);
			try {
				quadtree.addPoint(new Point2D.Double(Integer.parseInt(jtx.getText()), Integer.parseInt(jty.getText())),
						null);
				jlDepth.setText(Integer.toString(quadtree.getMaxDepth()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			jbAdd.setEnabled(true);
			fin.repaint();
		});

		//

		fin.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);
				jp.setSize(fin.getSize());
				jp.setPreferredSize(fin.getSize());
				jpPaint.setPreferredSize(jpPaint.getSize());
			}
		});
		fin.setSize(500, 500);
	}

	//

	final Quadtree<Point2D.Double, D> quadtree;
	//
	final JFrame fin;
	final JPanel jp, jpPaint;
	final JButton jbAdd;
	final JTextField jtx, jty;
	final JLabel jlDepth;

	/**
	 * @return the quadtree
	 */
	public Quadtree<Point2D.Double, D> getQuadtree() {
		return quadtree;
	}

	void paintTree(Graphics g) {
		Color c;
		final double ZOOM = 5.0;

		c = g.getColor();
		g.setColor(Color.RED);
		quadtree.forEachPointData((p, data, i, depth) -> {
			g.drawRect((int) (p.getX() * ZOOM), (int) (p.getY() * ZOOM), 2, 2);
		});
		g.setColor(c);
	}

	void show() {
		fin.setVisible(true);
	}

	//

	//

	public static void main(String[] args) {
		int maxPointsPerSubsection;
		Quadtree<Point2D.Double, Object> qt;
		maxPointsPerSubsection = 8;
		//

		System.out.println("start, adding " + INITIAL_POINTS.length + " points");

		qt = new Quadtree<>(i -> {
			return new Point2D.Double[i];
		}, INITIAL_POINTS, null, maxPointsPerSubsection);
		System.out.println("\n\n\ncreated");

		System.out.println(qt.toString());

		System.out.println("\n\n\n adding a very distant point\n");
		qt.addPoint(p(128, 128), null);
		System.out.println("results in ...");
		System.out.println(qt.toString());

		/*
		 */
		T_Quadtree_01<Object> t;
		t = new T_Quadtree_01<>(qt);
		t.show();

		//

		double x, y, w, h;
		ArrayList<Entry<Point2D.Double, Object>> queryResult;

		x = 4;
		y = 5;
		w = 12;
		h = 7; // 20;
		System.out.printf("starting query: x: %f, y: %f, w: %f, h: %f\n", x, y, w, h);

		queryResult = qt.query(x, y, w, h);
		System.out.println("collected " + queryResult.size() + " points:");
		for (Entry<Point2D.Double, Object> e : queryResult) {
			System.out.println("point: " + e.getKey() + " -> data: " + e.getValue());
		}
	}

}