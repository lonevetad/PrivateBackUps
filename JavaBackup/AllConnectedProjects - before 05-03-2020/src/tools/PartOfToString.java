package tools;

import java.io.Serializable;

public interface PartOfToString extends Serializable {

	public void toString(StringBuilder sb, int tabLevel);

	public default void addTab(StringBuilder sb, int tabLevel) {
		addTab(sb, tabLevel, true);
	}

	public default void addTab(StringBuilder sb, int tabLevel, boolean newLineNeeded) {
		if (sb != null) {
			if (newLineNeeded)
				sb.append('\n');
			sb.ensureCapacity(sb.length() + tabLevel);
			while (tabLevel-- > 0) {
				sb.append('\t');
			}
		}
	}
}