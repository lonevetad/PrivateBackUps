package tests.tDataStruct;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dataStructures.isom.MultiISOMPolygonalSubareas;
import dataStructures.isom.matrixBased.MISOM_SingleObjInNode;
import geometry.ObjectLocated;
import tools.NumberManager;
import tools.UniqueIDProvider;

public class TestMatrixISOMRotated {
	static final UniqueIDProvider uidp = UniqueIDProvider.newBasicIDProvider();

	//

	JFrame win;
	JPanel /* jp, jpNorthPart, */ jpDrawMultimap;
	//
	MultiISOMPolygonalSubareas<Double> multiPoly;

	//

	protected TestMatrixISOMRotated() {
		super();
		multiPoly = new MultiISOMPolygonalSubareas<>(4);
	}

	void buildAndShowGUI() {
//		KeyAdapter ka;
		win = new JFrame("Test Multi ISOM");
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jpDrawMultimap = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintRectsAndSections(g);
			}
		};
		win.add(jpDrawMultimap);

	}

	//

	public static void main(String[] args) {
		TestMatrixISOMRotated t;
		ColoredOL[] points;
		t = new TestMatrixISOMRotated();
		//
		t.multiPoly.addMap(//
				new MISOM_SingleObjInNode<Double>(false, 12, 8, NumberManager.getDoubleManager())
				//
				, 5, 7, 57.5);

		//
		points = new ColoredOL[] { //
				new ColoredOL(Color.BLUE, 10, 10), //
				new ColoredOL(Color.BLUE, 12, 10), //
				new ColoredOL(Color.BLUE, 7, 11), //
				new ColoredOL(Color.BLUE, 7, 7),//
		};
		for (ObjectLocated ol : points) {
			System.out.println(ol);
			t.multiPoly.add(ol);
			System.out.println("added\n");
		}
//		ol=new ColoredOL(Color.BLUE, location)
		t.buildAndShowGUI();
		System.out.println("FINE");
	}

//	protected	static class 

	protected static class ColoredOL implements ObjectLocated {
		Integer ID;
		Color color;
		Point location;

		private ColoredOL(Color color) {
			super();
			this.ID = uidp.getNewID();
			this.color = color;
		}

		protected ColoredOL(Color color, Point location) {
			this(color);
			this.location = location;
		}

		protected ColoredOL(Color color, int x, int y) {
			this(color);
			this.setLocation(x, y);
		}

		@Override
		public Integer getID() { return ID; }

		@Override
		public Point getLocation() { return location; }

		@Override
		public void setLocation(Point location) { this.location = location; }

		@Override
		public void setLocation(int x, int y) {
			if (location == null) {
				location = new Point(x, y);
			} else {
				location.x = x;
				location.y = y;
			}
		}

		@Override
		public String toString() { return "COL [ID=" + ID + ", color=" + color + ", location=" + location + "]"; }

	}

}