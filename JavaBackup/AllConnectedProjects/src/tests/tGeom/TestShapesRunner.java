package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import geometry.AbstractShape2D;
import geometry.AbstractShapeRunner;
import geometry.ProviderAbstractShape2D;
import geometry.ShapeRunnersImplemented;
import geometry.implementations.ProviderAbstractShape2DImpl;
import geometry.implementations.ProviderShapeRunnerImpl;
import geometry.implementations.shapeRunners.ShapeRunnerPolygonBorder;
import geometry.implementations.shapes.ShapeCircle;
import geometry.implementations.shapes.ShapePolygon;
import geometry.implementations.shapes.ShapeRectangle;
import geometry.pointTools.PointConsumer;
import tools.GraphicTools;

public class TestShapesRunner extends TestGeneric {
	static final int PIXEL_SQUARE_POINT = 5, MAX_SQUARE_PIXEL = 800; //

	public TestShapesRunner() {
	}

	protected class CircleRunnerModel extends ShapeModel {
		protected CircleRunnerModel() {
			super(new ShapeCircle(false));
			ctpoi = new ColorToPaintOnImage();

//			sPoly = new ShapePolygon(new Point2D[] { //
//					new Point2D.Double(5, 5), //
//					new Point2D.Double(37, 20), //
//					new Point2D.Double(25, 35), //
//					new Point2D.Double(100, 30), //
//					new Point2D.Double(29, 43), //
//					new Point2D.Double(24, 50), // 5
//					new Point2D.Double(77, 100), //
//					new Point2D.Double(88, 111), //
//					new Point2D.Double(15, 170), //
//					new Point2D.Double(10, 10) }, false);
//			sPoly = new ShapePolygon(new Point2D[] { //
//					new Point2D.Double(5, 5), //
//					new Point2D.Double(10, 10), //
//					new Point2D.Double(25, 10), //
//					new Point2D.Double(15, 8) }, false);
			sPoly = new ShapePolygon(new Point2D[] { //
					new Point2D.Double(8, 8), //
					new Point2D.Double(8, 16), //
					new Point2D.Double(16, 16), //
					new Point2D.Double(16, 8) }, false);
			sPoly.setCenter(10, 10);
			showWeirdRectangle = false;
		}

		boolean showWeirdRectangle;
		int diameter, angle;
		BufferedImage bi;
		MyObserver<AbstractShape2D> shapeObserver;
		MyObserver<ColorToPaintOnImage> imageObserver;
		final ColorToPaintOnImage ctpoi;
		ShapeRunnersImplemented[] shapesToTest;
		ProviderShapeRunnerImpl providerShapeRunner;
		ProviderAbstractShape2D providerShapes;
		AbstractShapeRunner runner;
		ShapeRunnersImplemented selectedShape;

		ShapePolygon sPoly;

		@Override
		void init() {
			providerShapes = ProviderAbstractShape2DImpl.getInstance();
			providerShapeRunner = ProviderShapeRunnerImpl.getInstance();
			shapesToTest = new ShapeRunnersImplemented[] { ShapeRunnersImplemented.Disk,
					ShapeRunnersImplemented.Circumference, ShapeRunnersImplemented.Line,
					// ShapeRunnersImplemented.PolygonBorder, // no way to set points
					ShapeRunnersImplemented.RectangleBorder, ShapeRunnersImplemented.Rectangle };
			ctpoi.bi = bi = new BufferedImage(MAX_SQUARE_PIXEL, MAX_SQUARE_PIXEL, BufferedImage.TYPE_INT_ARGB);
//			setDiameter(1);
		}

		void setCicleObserver(MyObserver<AbstractShape2D> cicleObserver) {
			this.shapeObserver = cicleObserver;
		}

		void setImageObserver(MyObserver<ColorToPaintOnImage> imageObserver) {
			this.imageObserver = imageObserver;
		}

		public AbstractShapeRunner gerRunner() {
			return runner;
		}

		public void setSelectedShape(ShapeRunnersImplemented selectedShape) {
			int rr;
			this.selectedShape = selectedShape;
//cancel out the previous
			if (s1 != null) {
				ctpoi.c = Color.LIGHT_GRAY;
				if (imageObserver != null)
					imageObserver.update(ctpoi);
			}
			//
			this.s1 = this.providerShapes.newEmptyShape(selectedShape);
			if (s1 == null) {
				System.out.println("S1 null with " + selectedShape);
				return;
			}
			if (s1 instanceof ShapeRectangle) {
				((ShapeRectangle) s1).setWidth(diameter).setHeight(diameter);
			}
			//
			this.runner = this.providerShapeRunner.getShapeRunner(selectedShape);
			// rr = 1 + (diameter >> 1);
			rr = (diameter * 3) >> 2;
			s1.setDiameter(diameter);
			s1.setCenter(rr, rr);
			s1.setAngleRotation(angle);
			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (shapeObserver != null)
				shapeObserver.update(s1);
		}

		public void setDiameter(int diameter) {
			int rr;
			if (diameter < 1)
				return;
			this.diameter = diameter;
			// rr = 1 + (diameter >> 1);
			rr = (diameter * 3) >> 2;
			ctpoi.c = Color.LIGHT_GRAY;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			s1.setDiameter(diameter);
			s1.setCenter(rr, rr);
			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (shapeObserver != null)
				shapeObserver.update(s1);
		}

		public void setAngle(int angle) {
			this.angle = angle;
			ctpoi.c = Color.LIGHT_GRAY;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			s1.setAngleRotation(angle);
			ctpoi.c = Color.BLUE;
			if (imageObserver != null)
				imageObserver.update(ctpoi);
			if (shapeObserver != null)
				shapeObserver.update(s1);
		}
	}

	protected class ColorToPaintOnImage {
		Color c;
		BufferedImage bi;
	}

	protected class SquarePainter implements PointConsumer, MyObserver<ColorToPaintOnImage> {
		private static final long serialVersionUID = 1L;
		CircleRunnerModel m;
		Graphics g;
		ColorToPaintOnImage ctpoi;

		@Override
		public void accept(Point2D p) {
			BufferedImage bi;
			int x, col, px, py;
			bi = this.ctpoi.bi;
			if (g == null || bi == null)
				return;
			col = ctpoi.c.getRGB();
			py = (int) p.getY() * PIXEL_SQUARE_POINT;
			x = (int) p.getX() * PIXEL_SQUARE_POINT;
			try {
				for (int r = 0; r < PIXEL_SQUARE_POINT; r++) {
					px = x;
					for (int c = 0; c < PIXEL_SQUARE_POINT; c++)
						bi.setRGB(px++, py, col);
					py++;
				}
			} catch (Exception e) {
			}
		}

		@Override
		public void update(ColorToPaintOnImage ctpoi) {
			BufferedImage bi;
			AbstractShapeRunner sr;
			if (ctpoi == null)
				return;
			this.ctpoi = ctpoi;
			bi = this.ctpoi.bi;
			if (bi == null)
				return;
			this.g = bi.getGraphics();
			sr = m.gerRunner();
			if (m.showWeirdRectangle)
				ShapeRunnerPolygonBorder.getInstance().runShape(m.sPoly, this);
			if (sr != null) {
				sr.runShape(m.s1, this);
			} else {
				System.out.println("RUNNER NULL");
			}
//			(m.c.isFilled() ? ShapeRunnerCircleFilled.getInstance() : ShapeRunnerCircleBorder.getInstance())
		}
	}

	protected class CircleRunnerView extends ShapeView {
		SquarePainter squarePainter;

		@Override
		void init() {
			GridBagConstraints c;
			CircleRunnerModel m;
			JPanel jp;
			TextField tf;
			JButton jb;
			JSpinner js;
			JSpinner jsAngle;
//			JCheckBox jcb;
			JComboBox<ShapeRunnersImplemented> jcbShapes;
			m = (CircleRunnerModel) model;
			squarePainter = new SquarePainter();
			squarePainter.m = m;
			m.setImageObserver(squarePainter);
			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = 0;
			c.weighty = 0;
			c.gridwidth = 2;
			c.gridheight = 1;
			tf = new TextField("16");
			pContainer.add(tf, c);
			c.gridx = 2;
//			jcb = new JCheckBox("V = is filled");
//			pContainer.add(jcb, c);
			jb = new JButton("Set the radius");
			pContainer.add(jb, c);
			c.gridx = 4;
			js = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
//			jb = new JButton("Set the radius");
			pContainer.add(js, c);

			jp = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					if (squarePainter.ctpoi != null)
						g.drawImage(squarePainter.ctpoi.bi, 0, 0, null);
					g.setColor(Color.GREEN);
					GraphicTools.paintGrid(g, JPANEL_DIMENSION, JPANEL_DIMENSION, PIXEL_SQUARE_POINT);
//					squarePainter.g = g;
				}

				@Override
				public Dimension getSize() {
					return new Dimension(getWidth(), getHeight());
				}

				@Override
				public int getWidth() {
					return JPANEL_DIMENSION;
				}

				@Override
				public int getHeight() {
					return JPANEL_DIMENSION;
				}
				/*
				 * @Override public int getX() { return 0; }
				 * 
				 * @Override public int getY() { return 0; }
				 */
			};

			jcbShapes = new JComboBox<>(m.shapesToTest);
			jcbShapes.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						m.setSelectedShape((ShapeRunnersImplemented) e.getItem());
					}
				}
			});
			c.gridx = 0;
			c.gridy = 2;
			pContainer.add(jcbShapes, c);

			c.gridx = 2;
			pContainer.add(new JLabel("Set the rotation angle:"), c);

			jsAngle = new JSpinner(new SpinnerNumberModel(0, 0, 360, 5));
			c.gridx = 4;
			pContainer.add(jsAngle, c);

			jp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = c.weighty = c.gridwidth = c.gridheight = 8;
			pContainer.add(jp, c);
			m.setCicleObserver(circ -> {
				jp.repaint();
				fin.repaint();
			});
			jb.addActionListener(l -> {
				m.setDiameter(Integer.parseInt(tf.getText()));
			});
			js.addChangeListener(e -> {
				m.setDiameter((Integer) js.getValue());
			});
//			jcb.addChangeListener(l -> {
//				if (m.s1 instanceof AbstractFillable)
//					((AbstractFillable) m.s1).setFilled(jcb.isSelected());
//			});
			jsAngle.addChangeListener(e -> {
				m.setAngle((Integer) jsAngle.getValue());
			});
			jp.setLocation(0, 0);
			jp.setSize(JPANEL_DIMENSION, JPANEL_DIMENSION);
			jp.setPreferredSize(jp.getSize());
			jp.requestFocus();
			fin.setSize(jp.getWidth() + 100, jp.getHeight() + 100);
			fin.setVisible(true);
		}

	}

	@Override
	ShapeModel newModel() {
		return new CircleRunnerModel();
	}

	@Override
	ShapeView newView() {
		return new CircleRunnerView();
	}

	@Override
	void init() {
	}

	public static void main(String[] args) {
		TestShapesRunner t;
		t = new TestShapesRunner();
		t.startTest();
	}
}
