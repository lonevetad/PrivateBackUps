package gui;

import java.awt.Dimension;
// import java.util.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class TestJ_JScrollPane {

	public TestJ_JScrollPane() {
	}

	static int direction = -4;
	static final boolean useScroll = true;
	static final String longString;
	static {
		int rip, len;
		char c;
		StringBuilder sb;
		rip = 8;
		len = 32;
		sb = new StringBuilder(rip * (len + 2));
		for (int i = 0; i < rip; i++) {
			c = (char) ('a' + i);
			for (int j = 0; j < len; j++) {
				sb.append(c);
			}
			sb.append('\n').append('\r');
		}
		longString = sb.toString();
		sb = null;
	}
	// = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
	// + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

	public static void main(String[] args) {
		JFrame test = new JFrame("Test");
		test.setSize(500, 300);
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		test.setLayout(new BoxLayout(test.getContentPane(), BoxLayout.Y_AXIS));
		for (int i = 0; i < 8; i++) {
			JTextArea jta;
			jta = new JTextArea(longString);
			jta.setWrapStyleWord(true);
			if (useScroll) {
				JScrollPane jScrollPane = new JScrollPane(jta);
				jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jScrollPane.setMaximumSize(new Dimension(1000, 100));
				jScrollPane.setPreferredSize(new Dimension(0, 100));
				test.add(jScrollPane);
			} else {
				test.add(jta);
			}
		}
		test.add(Box.createVerticalGlue()); // should use all available space

		final int min = 300, max = 700;
		new Timer(50, e -> {
			int h = test.getHeight();
			SwingUtilities.invokeLater(() -> test.setSize(test.getWidth(), h + direction));
			direction = h == min || h == max ? -direction : direction;
		}).start();

		test.setVisible(true);
	}
}
