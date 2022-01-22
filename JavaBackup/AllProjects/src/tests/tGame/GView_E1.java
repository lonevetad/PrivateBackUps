package tests.tGame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import games.generic.controlModel.GController;
import games.generic.controlModel.loaders.LoaderGeneric;
import games.generic.controlModel.loaders.LoaderGeneric.LoadStatusResult;
import games.generic.controlModel.loaders.LoaderManager.LoadingObserver;
import games.generic.controlModel.misc.CreatureAttributes;
import games.generic.controlModel.misc.GThread.GTRunnable;
import games.generic.controlModel.player.PlayerGeneric;
import games.generic.view.GameView;
import games.generic.view.ViewOptions;
import games.generic.view.misc.LoadingProcessView;
import games.theRisingAngel.enums.AttributesTRAn;
import games.theRisingAngel.enums.RechargeableResourcesTRAn;
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
	RechargeableResourcesTRAn[] curableResource = { RechargeableResourcesTRAn.Life, RechargeableResourcesTRAn.Mana,
			RechargeableResourcesTRAn.Shield };

	@Override
	protected ViewOptions newViewOptions() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LoadingProcessView newLoadingProcessView() { // TODO Auto-generated method stub
		return new LoadingProcessView(this) {

			@Override
			public void onRemovingOnView(GameView view) { // TODO Auto-generated method stub
			}

			@Override
			public void onAddingOnView(GameView view) { // TODO Auto-generated method stub
			}

			@Override
			public void notifyLoadingProcessCompleted(LoaderGeneric loader, LoadStatusResult completitionResult) {
				System.out.println("LOADED " + loader.getClass().getName() + " --> " + completitionResult.name());
			}

			@Override
			public void notifyAllLoadingProcessStarted(int loadersAmount) {
				System.out.println("START Loading " + loadersAmount + " loaders");
			}

			@Override
			public void notifyAllLoadingProcessEnded(List<LoaderGeneric> failedLoaders) { // TODO Auto-generated method
																							// stub
				System.out.println("END LOADERS");
				System.out.print("\tfailed:");
				if (failedLoaders == null) {
					System.out.println("NONE :D");
				} else {
					System.out.println(Arrays.toString(failedLoaders.toArray()));
				}
			}
		};
	}

	@Override
	public void initializeNonGUIParts() { // TODO Auto-generated method stub
	}

	@Override
	public void beginsPlayesInteraction(PlayerGeneric thisPlayer, PlayerGeneric otherPlayer) { // TODO Auto-generated
																								// method stub
	}

	@Override
	public List<LoaderGeneric> getAllViewRelatedLoaders() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoadingObserver> getAllLoadersObservers() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void continueViewInitialization() {
//		Container panel;
		int n, startYStats;
		JPanel jpNorth;
		GController c;
		c = super.gameController;

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

		jbStartPause.addActionListener(l -> startSimulation());
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
		jlPlayerStatText = new JLabel[AttributesTRAn.ALL_ATTRIBUTES.length];
		jlPlayerStatValue = new JLabel[AttributesTRAn.ALL_ATTRIBUTES.length];
		n = AttributesTRAn.ALL_ATTRIBUTES.length;
		startYStats = constr.gridy + 1;
		while (--n >= 0) {
			constr.gridy = startYStats + n;
			constr.gridx = 0;
			jlPlayerStatText[n] = new JLabel(AttributesTRAn.ALL_ATTRIBUTES[n].name());
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

	}

	@Override
	public void finishViewSetupAndShow() {
		fin.setSize(500, 700);
		jpBigContainer.setSize(fin.getSize());
		fin.setVisible(true);
//		fin.pack();
	}

	//

	protected void startSimulation() {
		if (gameController == null) {
			System.out.println("GView cannot start because gc is NULL");
			return;
		}
		System.out.println("gc.isAlive()? " + gameController.isAlive());
		if (gameController.isAlive()) {
			if (gameController.isPlaying()) {
				gameController.pauseGame();
				jbStartPause.setText("Resume");
			} else {
				gameController.resumeGame();
				jbStartPause.setText("Pause");
			}
		} else {
			System.out.println("view starting");
			gameController.startGame();
			System.out.println("view adding repainter");
			addRepainterThread();
			System.out.println("STARTED");
			jbStartPause.setText("Pause");
		}
	}

	void repaintPlayerInfo() {
		int max, v, n;
		Player_E1 p;
		GModality_E1 gmodalitye1;
		String textDisplayed;
//		StringBuilder sb;
		CreatureAttributes ca;
		JProgressBar jpb;
		RechargeableResourcesTRAn curRes;
		gmodalitye1 = (GModality_E1) super.gameController.getCurrentGameModality();
		if (gmodalitye1 == null)
			return;
		p = gmodalitye1.getPlayerRPG();
		for (int i = 0; i < curableResource.length; i++) {
			jpb = jpbCurableResources[i];
			curRes = curableResource[i];
			jpb.setMaximum(max = p.getMaxAmount(curRes));
			jpb.setValue(v = p.getAmount(curRes));
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
		jlMoneyValue.setText(Integer.toString(p.getCurrencies().getCurrencyAmount( //
				p.getCurrencies().getCurrencies()[CurrencySetTRAn.BASE_CURRENCY_INDEX])));
		n = AttributesTRAn.ALL_ATTRIBUTES.length;
		while (--n >= 0) {
			jlPlayerStatValue[n].setText(Integer.toString(ca.getValue(AttributesTRAn.ALL_ATTRIBUTES[n])));
		}
		jpPlayerStats.repaint();
		// TODO
	}

	void singleRepaintCycle() {
		repaintPlayerInfo();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void addRepainterThread() {
		GModality_E1 gmodalitye1;
//		GThread t;
		gmodalitye1 = (GModality_E1) super.gameController.getCurrentGameModality();
//		t = new GThread(new PlayerInfoRepainter(gmodalitye1));
//		gmodalitye1.addGameThread(t);
//		t.start();
		System.out.println("view on adding repainter");
		gmodalitye1.addGameThreadSimplyStoppable(this::singleRepaintCycle);
	}

	//

	protected class PlayerInfoRepainter implements GTRunnable {
		final GModality_E1 gmodalitye1;

		public PlayerInfoRepainter(GModality_E1 gmodalitye1) {
			super();
			this.gmodalitye1 = gmodalitye1;
		}

		@Override
		public void run() {
			while (gmodalitye1.isAlive()) {
				while (gmodalitye1.isRunningOrSleep()) {
					singleRepaintCycle();
				}
			}
			System.out.println("END view repainter");
		}

		@Override
		public void stopAndDie() {}

		@Override
		public void restart() {}

	}
}