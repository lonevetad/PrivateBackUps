package tests.tGeom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.TextField;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import geometry.implementations.shapeRunners.ShapeRunnerCircleBorder;
import geometry.implementations.shapeRunners.ShapeRunnerCircleFilled;
import geometry.implementations.shapes.ShapeCircle;
import geometry.pointTools.PointConsumer;
import tools.GraphicTools;

public class TestRunnerCircleBorder extends TestGeneric {
	static final int PIXEL_SQUARE_POINT = 20; //

	public TestRunnerCircleBorder() {
	}

	protected class CircleRunnerBorderModel extends ShapeModel {
		protected CircleRunnerBorderModel() {
			super(new ShapeCircle(false));
		}

		BufferedImage bi;
		ShapeCircle c;
		MyObserver<ShapeCircle> cicleObserver;
		MyObserver<BufferedImage> imageObserver;

		@Override
		void init() {
			c = (ShapeCircle) super.s1;
			setDiameter(1);
		}

		public void setDiameter(int diameter) {
			int rr;
			if (diameter < 1)
				return;
			rr = 1 + (diameter >> 1);
			c.setDiameter(diameter);
			c.setCenter(rr, rr);
			bi = new BufferedImage(50 * PIXEL_SQUARE_POINT, 50 * PIXEL_SQUARE_POINT, BufferedImage.TYPE_INT_ARGB);
			if (imageObserver != null)
				imageObserver.update(bi);
			if (cicleObserver != null)
				cicleObserver.update(c);
		}

		void setCicleObserver(MyObserver<ShapeCircle> cicleObserver) {
			this.cicleObserver = cicleObserver;
		}

		void setImageObserver(MyObserver<BufferedImage> imageObserver) {
			this.imageObserver = imageObserver;
		}
	}

	protected class SquarePainter implements PointConsumer, MyObserver<BufferedImage> {
		private static final long serialVersionUID = 1L;
		CircleRunnerBorderModel m;
		Graphics g;
		BufferedImage bi;

		@Override
		public void accept(Point2D p) {
			int x, y, col;
			if (g == null)
				return;
			col = Color.BLUE.getRGB();
			y = (int) p.getY() * PIXEL_SQUARE_POINT;
			for (int r = 0; r < PIXEL_SQUARE_POINT; r++) {
				x = (int) p.getX() * PIXEL_SQUARE_POINT;
				for (int c = 0; c < PIXEL_SQUARE_POINT; c++) {
					bi.setRGB(x++, y, col);
				}
				y++;
			}
//			g.drawRect((int) p.getX() * PIXEL_SQUARE_POINT, 
//(int) p.getY() * PIXEL_SQUARE_POINT, //
//					PIXEL_SQUARE_POINT, PIXEL_SQUARE_POINT);
		}

		@Override
		public void update(BufferedImage bi) {
			if (bi == null)
				return;
			this.bi = bi;
			this.g = bi.getGraphics();
			System.out.println("Start running?");
			(m.c.isFilled() ? ShapeRunnerCircleFilled.SINGLETON : ShapeRunnerCircleBorder.SINGLETON).runShape(m.c,
					this);
			System.out.println("LOL");
		}
	}

	protected class CircleRunnerBorderView extends ShapeView {
		SquarePainter squarePainter;

		@Override
		void init() {
			GridBagConstraints c;
			CircleRunnerBorderModel m;
			JPanel jp;
			TextField tf;
			JButton jb;
			JSpinner js;
			JCheckBox jcb;
			m = (CircleRunnerBorderModel) model;
			squarePainter = new SquarePainter();
			squarePainter.m = m;
			m.setImageObserver(squarePainter);
			c = new GridBagConstraints();
			c.gridx = c.gridy = 0;
			c.weightx = 0;
			c.weighty = 2;
			c.gridwidth = 2;
			c.gridheight = 1;
			tf = new TextField("16");
			pContainer.add(tf, c);
			jcb = new JCheckBox("V = is filled");
			c.gridx = 2;
			pContainer.add(jcb, c);
			c.gridx = 4;
			jb = new JButton("Set the radius");
			pContainer.add(jb, c);
			c.gridx = 6;
			js = new JSpinner(new SpinnerNumberModel(16, 1, 1000, 1));
//			jb = new JButton("Set the radius");
			pContainer.add(js, c);

			jp = new JPanel() {
				private static final long serialVersionUID = -7775420L;

				@Override
				protected void paintComponent(Graphics g) {
					g.drawImage(squarePainter.bi, 0, 0, null);
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
			jp.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 5));
			c.gridx = 0;
			c.gridy = 1;
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
			jcb.addChangeListener(l -> {
				m.c.setFilled(jcb.isSelected());
			});
			jp.setLocation(0, 0);
			jp.setSize(JPANEL_DIMENSION, JPANEL_DIMENSION);
			jp.setPreferredSize(jp.getSize());
			jp.requestFocus();
			fin.setSize(jp.getSize());
			fin.setVisible(true);
		}

	}

	@Override
	ShapeModel newModel() {
		return new CircleRunnerBorderModel();
	}

	@Override
	ShapeView newView() {
		return new CircleRunnerBorderView();
	}

	@Override
	void init() {
	}

	public static void main(String[] args) {
		TestRunnerCircleBorder t;
		t = new TestRunnerCircleBorder();
		t.startTest();
	}
}