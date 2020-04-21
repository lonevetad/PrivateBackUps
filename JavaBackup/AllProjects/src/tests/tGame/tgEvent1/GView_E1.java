package tests.tGame.tgEvent1;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import games.generic.controlModel.GController;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.misc.GThread.GTRunnable;
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
				addRepainterThread();
				System.out.println("STARTED");
				jbStartPause.setText("Pause");
			}
		});
		jbCloseAll.addActionListener(l -> {
			System.out.println("CLOSING ALL");
			c.closeAll();
			jbStartPause.setText("START");
		});

		//

		GridBagConstraints constr;
		constr = new GridBagConstraints();
		jpPlayerStats = new JPanel(new GridBagLayout());
		jpBigContainer.add(jpPlayerStats, BorderLayout.EAST);
		jpbPlayerLife = new JProgressBar(0, 100);
//		jpbPlayerLife.setMaximum(100);
		constr.gridx = 0;
		constr.gridy = 0;
		constr.gridwidth = 1;
		constr.gridheight = 1;
		constr.weightx = constr.weighty = 1;
		jpPlayerStats.add(jpbPlayerLife, constr);
		/*
		 * TODO add progress bar and textarea, then create a gui-thread to update player
		 * stats
		 */

		fin.setSize(500, 500);
		jpBigContainer.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	void repaintPlayerInfo() {
		Player_E1 p;
		GModality_E1 gmodalitye1;
		gmodalitye1 = (GModality_E1) super.gc.getCurrentGameModality();
		if (gmodalitye1 == null)
			return;
		p = gmodalitye1.getPlayerRPG();
		jpbPlayerLife.setMaximum(p.getLifeMax());
		jpbPlayerLife.setValue(p.getLife());
		jpbPlayerLife.repaint();
		// TODO
	}

	void addRepainterThread() {
		GModality_E1 gmodalitye1;
		GThread t;
		gmodalitye1 = (GModality_E1) super.gc.getCurrentGameModality();
		t = new GThread(new PlayerInfoRepainter(gmodalitye1));
		gmodalitye1.addGameThread(t);
		t.start();
	}

	class PlayerInfoRepainter implements GTRunnable {
		final GModality_E1 gmodalitye1;

		public PlayerInfoRepainter(GModality_E1 gmodalitye1) {
			super();
			this.gmodalitye1 = gmodalitye1;
		}

		@Override
		public void run() {
			while(gmodalitye1.isAlive()) {
				while(gmodalitye1.isRunningOrSleep()) {
					repaintPlayerInfo();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("END view repainter");
		}

		@Override
		public void stopAndDie() {
		}

	}

	public static void main(String[] args) {
		GView_E1 view;
		view = new GView_E1(null);
		view.initAndShow();
	}
}