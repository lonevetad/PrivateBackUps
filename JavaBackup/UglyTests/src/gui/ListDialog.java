package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/** @see http://stackoverflow.com/questions/5759131 */
public class ListDialog {

	private static final int N = 12, MAX = N - 4;
	private JDialog dlg = new JDialog();
	private DefaultListModel<String> model = new DefaultListModel<>();
	private JList<String> list = new JList<>(model);
	private JScrollPane sp = new JScrollPane(list);
	private int count;

	public ListDialog() {
		JPanel panel = new JPanel();
		panel.add(new JButton(new AbstractAction("Add") {
			private static final long serialVersionUID = -30519541L;

			@Override
			public void actionPerformed(ActionEvent e) {
				append();
				if (count <= N) {
					list.setVisibleRowCount(count);
					dlg.pack();
				}
			}
		}));
		for (int i = 0; i < MAX; i++) {
			this.append();
		}
		list.setVisibleRowCount(MAX);
		dlg.add(sp, BorderLayout.CENTER);
		dlg.add(panel, BorderLayout.SOUTH);
		dlg.pack();
		dlg.setLocationRelativeTo(null);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.setVisible(true);
	}

	private void append() {
		model.addElement("String " + String.valueOf(++count));
		list.ensureIndexIsVisible(count - 1);
	}

	public static void main(String[] a_args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ListDialog pd = new ListDialog();
				System.out.println(pd);
			}
		});
	}
}