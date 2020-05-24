package tests.tGame.tgEvent1;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import games.generic.controlModel.GController;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.CurableResourceType;
import games.generic.controlModel.misc.GThread;
import games.generic.controlModel.misc.GThread.GTRunnable;
import games.generic.controlModel.misc.HealingTypeExample;
import games.generic.view.GameView;
import games.theRisingAngel.misc.AttributesTRAn;
import games.theRisingAngel.misc.CurrencySetTRAn;

public class GView_E1 extends GameView {

	public GView_E1(GController gc) { super(gc); }

	JFrame fin;
	JButton jbCloseAll, jbStartPause;
	JPanel jpBigContainer;
	JPanel jpStartStop, jpPlayerStats;
	JProgressBar[] jpbCurableResources; // jpbPlayerLife, jpbPlayerMana;
//	JTextArea jtaPlayerStats;
//	JScrollPane jspPlayerAttributes;
	JLabel jlMoneyText, jlMoneyValue;
	JLabel[] jlPlayerStatText, jlPlayerStatValue;
	CurableResourceType[] curableResource = { HealingTypeExample.Life, HealingTypeExample.Mana,
			HealingTypeExample.Shield };

	@Override
	public void initAndShow() {
//		Container panel;
		int n, startYStats;
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

		jpbCurableResources = new JProgressBar[curableResource.length];
		constr.gridx = 0;
		constr.gridwidth = 2;
		constr.gridheight = 1;
		constr.weightx = constr.weighty = 1;
		for (int i = 0; i < curableResource.length; i++) {
			JProgressBar jpb;
			jpbCurableResources[i] = jpb = new JProgressBar(0, 100);
			jpb.setStringPainted(true);
			constr.gridy = i;
			jpPlayerStats.add(jpb, constr);
		}
//		constr.gridheight = 9;
//		constr.weighty = 9;
//		jtaPlayerStats = new JTextArea();
//		jspPlayerAttributes = new JScrollPane(jtaPlayerStats);
//		jspPlayerAttributes.setViewportView(jtaPlayerStats);
//		jspPlayerAttributes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		jpPlayerStats.add(jspPlayerAttributes, constr);
//		jtaPlayerStats.setLineWrap(false); 
		constr.gridy++;
		constr.gridwidth = 1;
		jlMoneyText = new JLabel("Currency: ");
		jpPlayerStats.add(jlMoneyText, constr);
		constr.gridx = 1;
		jlMoneyValue = new JLabel("0");
		jpPlayerStats.add(jlMoneyValue, constr);
		jlPlayerStatText = new JLabel[AttributesTRAn.VALUES.length];
		jlPlayerStatValue = new JLabel[AttributesTRAn.VALUES.length];
		n = AttributesTRAn.VALUES.length;
		startYStats = constr.gridy + 1;
		while (--n >= 0) {
			constr.gridy = startYStats + n;
			constr.gridx = 0;
			jlPlayerStatText[n] = new JLabel(AttributesTRAn.VALUES[n].name());
			jpPlayerStats.add(jlPlayerStatText[n], constr);
			constr.gridx = 1;
			jlPlayerStatValue[n] = new JLabel("0");
			jpPlayerStats.add(jlPlayerStatValue[n], constr);
		}

//		taPlayerStats.setPreferredSize(new Dimension(100, 500));
		/*
		 * TODO add progress bar and textarea, then create a gui-thread to update player
		 * stats
		 */

		fin.setSize(500, 700);
		jpBigContainer.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	void repaintPlayerInfo() {
		int max, v, n;
		Player_E1 p;
		GModality_E1 gmodalitye1;
		String textDisplayed;
//		StringBuilder sb;
		CreatureAttributes ca;
		JProgressBar jpb;
		CurableResourceType curRes;
		gmodalitye1 = (GModality_E1) super.gc.getCurrentGameModality();
		if (gmodalitye1 == null)
			return;
		p = gmodalitye1.getPlayerRPG();

		for (int i = 0; i < curableResource.length; i++) {
			jpb = jpbCurableResources[i];
			curRes = curableResource[i];
			jpb.setMaximum(max = p.getCurableResourceMax(curRes));
			jpb.setValue(v = p.getCurableResourceAmount(curRes));
			textDisplayed = curRes.getName() + ": " + v + " / " + max;
			jpb.setToolTipText(textDisplayed);
			jpb.setString(textDisplayed);
			jpb.repaint();
		}

		ca = p.getAttributes();
//		sb = new StringBuilder(128);
//		for (int i = 0, n = ca.getAttributesCount(); i < n; i++) {
//			sb.append(AttributesTRAr.VALUES[i].name()).append("\t: ").append(ca.getValue(i)).append('\n');
//		}
//		jtaPlayerStats.setText(sb.toString());
		jlMoneyValue
				.setText(Integer.toString(p.getCurrencies().getCurrencyAmount(CurrencySetTRAn.BASE_CURRENCY_INDEX)));
		n = AttributesTRAn.VALUES.length;
		while (--n >= 0) {
			jlPlayerStatValue[n].setText(Integer.toString(ca.getValue(AttributesTRAn.VALUES[n])));
		}
		jpPlayerStats.repaint();
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
			while (gmodalitye1.isAlive()) {
				while (gmodalitye1.isRunningOrSleep()) {
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
		public void stopAndDie() {}

	}

	public static void main(String[] args) {
		GView_E1 view;
		view = new GView_E1(null);
		view.initAndShow();
	}
}