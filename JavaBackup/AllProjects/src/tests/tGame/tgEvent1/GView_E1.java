package tests.tGame.tgEvent1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import games.generic.controlModel.GController;
import games.generic.view.GameView;

public class GView_E1 extends GameView {

	public GView_E1(GController gc) {
		super(gc);
	}

	JFrame fin;
	JButton jbCloseAll, jbStartPause;
	JPanel jpBigContainer;
	JPanel jpStartStop, jpPlayerStats;
	JProgressBar jpbPlayerLife;
	TextArea taPlayerStats;

	@Override
	public void initAndShow() {
//		Container panel;
		JPanel jpNorth;
		GController c;
		c = super.gc;

		fin = new JFrame("Test Event loop 1");
		fin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		panel = fin.getContentPane();
		jpBigContainer = new JPanel();
		jpBigContainer.setLayout(new BorderLayout());
		fin.add(jpBigContainer);

		//
		jpNorth = new JPanel();
		jpBigContainer.add(jpNorth, BorderLayout.NORTH);
		jbStartPause = new JButton("Start");
		jpNorth.add(jbStartPause);
		jbCloseAll = new JButton("CLOSE ALL");
		jpNorth.add(jbCloseAll);

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

		//

		jpPlayerStats = new JPanel(new FlowLayout());
		/*
		 * TODO add progress bar and textarea, then create a gui-thread to update player
		 * stats
		 */

		fin.setSize(500, 500);
		jpBigContainer.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	public static void main(String[] args) {
		GView_E1 view;
		view = new GView_E1(null);
		view.initAndShow();
	}
}