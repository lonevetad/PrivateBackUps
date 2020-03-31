package common.abstractCommon;

import java.io.Serializable;

public interface StatisticField extends Serializable {

	/**
	 * This interface designed to be implemented by a {@link java.lang.Enum}'s subclass, so this
	 * method is be already defined as {@link java.lang.Enum#ordinal()}.
	 */
	public int ordinal();

	/**
	 * Get the ID associated with this field. Could be a index.<br>
	 * Calls {@link #ordinal()}.
	 */
	public default int getIdStat() {
		return ordinal();
	}

	/** Like {@link #getIdStat()}, but should cache the {@link Integer} instance. */
	public default Integer getIdStatAsInteger() {
		return getIdStat();
	}

	/** Like {@link #ordinal()}. */
	public String name();

	public boolean isBounded();
}
