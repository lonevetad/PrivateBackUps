package tests.tGame.tgEvent1;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import games.generic.controlModel.GController;
import games.generic.view.GameView;

public class GView_E1 extends GameView {

	public GView_E1(GController gc) {
		super(gc);
	}

	JFrame fin;
	JButton jbCloseAll, jbStartPause;

	@Override
	public void initAndShow() {
		JPanel jp;
//		Container panel;
		GController c;
		c = super.gc;

		fin = new JFrame("Test Event loop 1");
		fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		panel = fin.getContentPane();
		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		fin.add(jp);
		jbStartPause = new JButton("Start");
		jp.add(jbStartPause);
		jbCloseAll = new JButton("CLOSE ALL");
		jp.add(jbCloseAll);

		jbStartPause.addActionListener(l -> {
			if (gc == null) {
				System.out.println("CIao");
				return;
			}
			System.out.println("gc.isAlive()? " + gc.isAlive());
			if (gc.isAlive()) {
				if (gc.isPlaying()) {
					gc.pauseGame();
					jbStartPause.setText("Resume");
				} else {
					gc.resumeGame();
					jbStartPause.setText("Pause");
				}
			} else {
				gc.startGame();
				jbStartPause.setText("Pause");
			}
		});
		jbCloseAll.addActionListener(l -> {
			System.out.println("CLOSING ALL");
			c.closeAll();
			jbStartPause.setText("START");
		});
		fin.setSize(500, 500);
		jp.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	public static void main(String[] args) {
		GView_E1 view;
		view = new GView_E1(null);
		view.initAndShow();
	}
}