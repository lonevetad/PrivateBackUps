package tests.tGame.tgEvent1;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

import games.generic.controlModel.GameController;
import games.generic.view.GameView;

public class GV_E1 extends GameView {

	public GV_E1(GameController gc) {
		super(gc);
	}

	JFrame fin;
	JButton jbCloseAll;

	@Override
	public void initAndShow() {
//		JPanel jp;
		Container panel;
		GameController c;
		c = super.gc;

		fin = new JFrame("Test Event loop 1");
		panel = fin.getContentPane();
		jbCloseAll = new JButton("Start");
		panel.add(jbCloseAll);

		jbCloseAll.addActionListener(l -> {
			c.closeAll();
		});
	}

}