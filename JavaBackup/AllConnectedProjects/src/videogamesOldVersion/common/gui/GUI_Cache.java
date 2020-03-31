package common.gui;

import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import common.abstractCommon.behaviouralObjectsAC.MyComparator;
import common.mainTools.Comparators;

public class GUI_Cache {

	public GUI_Cache() {
	}

	LinkedList<JLabel> jlCacheList;
	LinkedList<JTextField> jtfCacheList;
	LinkedList<JCheckBox> jcheckbCacheList;
	LinkedList<JComboBox<? extends Object>> jcomboboxCacheList;
	LinkedList<ListBoxView</* String */? extends Object>> lbvCacheList;

	@SuppressWarnings("unchecked")
	public <T> T getCached(Class<T> c) {
		if (JLabel.class.isAssignableFrom(c) && (!jlCacheList.isEmpty())) return (T) jlCacheList.removeFirst();
		if (JTextField.class.isAssignableFrom(c) && (!jtfCacheList.isEmpty())) return (T) jtfCacheList.removeFirst();
		if (JCheckBox.class.isAssignableFrom(c) && (!jcheckbCacheList.isEmpty()))
			return (T) jcheckbCacheList.removeFirst();
		if (ListBoxView.class.isAssignableFrom(c) && (!jtfCacheList.isEmpty())) return (T) jtfCacheList.removeFirst();
		return null;
	}

	public JLabel getJLabel() {
		if (jlCacheList == null) jlCacheList = new LinkedList<>();
		return (!jlCacheList.isEmpty()) ? jlCacheList.removeFirst() : new JLabel();
	}

	public JTextField getJTextField() {
		if (jtfCacheList == null) jtfCacheList = new LinkedList<>();
		return (!jtfCacheList.isEmpty()) ? jtfCacheList.removeFirst() : new JTextField();
	}

	public JCheckBox getJCheckBox() {
		if (jcheckbCacheList == null) jcheckbCacheList = new LinkedList<>();
		return (!jcheckbCacheList.isEmpty()) ? jcheckbCacheList.removeFirst() : new JCheckBox();
	}

	public <E extends Object> JComboBox<? extends Object> getComboBox(E[] a) {
		if (jcomboboxCacheList == null) jcomboboxCacheList = new LinkedList<>();
		return (!jcomboboxCacheList.isEmpty()) ? jcomboboxCacheList.removeFirst() : new JComboBox<>(a);
	}

	public ListBoxView<? extends Object> getListBoxView() {
		if (lbvCacheList == null) lbvCacheList = new LinkedList<>();
		return (!lbvCacheList.isEmpty()) ? lbvCacheList.removeFirst()
				: ListBoxView.getDefaultImplementation(Comparators.OBJECT_COMPARATOR);
	}

	@SuppressWarnings("unchecked")
	public <I> ListBoxView<I> getListBoxView(MyComparator<I> comp) {
		Class<?> c;
		ListBoxView<I> lbv;
		if (comp == null) return null;
		if (lbvCacheList.isEmpty()) return ListBoxView.getDefaultImplementation(comp);
		c = comp.getClass();
		for (ListBoxView</* String */? extends Object> l : lbvCacheList) {
			if (l.comparator().getClass().isAssignableFrom(c)) {
				try {
					lbv = (ListBoxView<I>) l;
					return lbv;
				} catch (Exception e) {
				}
			}
		}
		// comp.getClass().get
		return
		// (!lbvCacheList.isEmpty()) ? lbvCacheList.removeFirst():
		ListBoxView.getDefaultImplementation(comp);
	}

	public void store(JLabel jl) {
		if (jlCacheList == null) jlCacheList = new LinkedList<>();
		if (jl != null && (!jlCacheList.contains(jl))) jlCacheList.add(jl);
	}

	public void store(JTextField jtf) {
		if (jtfCacheList == null) jtfCacheList = new LinkedList<>();
		if (jtf != null && (!jtfCacheList.contains(jtf))) jtfCacheList.add(jtf);
	}

	public void store(JCheckBox jcb) {
		if (jcheckbCacheList == null) jcheckbCacheList = new LinkedList<>();
		if (jcb != null && (!jcheckbCacheList.contains(jcb))) jcheckbCacheList.add(jcb);
	}

	public void store(ListBoxView<? extends Object> lbv) {
		if (lbvCacheList == null) lbvCacheList = new LinkedList<>();
		if (lbv != null && (!lbvCacheList.contains(lbv))) lbvCacheList.add(lbv);
	}

	public void store(JComboBox<? extends Object> lbv) {
		if (jcomboboxCacheList == null) jcomboboxCacheList = new LinkedList<>();
		if (lbv != null && (!jcomboboxCacheList.contains(lbv))) jcomboboxCacheList.add(lbv);
	}

	@SuppressWarnings("unchecked")
	public <T extends JComponent> void storeGeneric(T comp) {
		Class<? extends JComponent> c;
		if (comp != null) {
			c = comp.getClass();
			if (JTextField.class.isAssignableFrom(c))
				store((JTextField) comp);
			else if (JLabel.class.isAssignableFrom(c))
				store((JLabel) comp);
			else if (JCheckBox.class.isAssignableFrom(c))
				store((JCheckBox) comp);
			else if (JComboBox.class.isAssignableFrom(c))
				store((JComboBox<?>) comp);
			else if (ListBoxView.class.isAssignableFrom(c)) store((ListBoxView<? extends Object>) comp);
		}
	}
}