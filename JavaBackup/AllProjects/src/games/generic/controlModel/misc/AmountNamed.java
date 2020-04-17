package games.generic.controlModel.misc;

import java.io.Serializable;

import tools.ObjectNamedID;

public class AmountNamed implements Serializable {
	private static final long serialVersionUID = -54562455221147L;
	protected int value;
	protected ObjectNamedID type;

	public AmountNamed(ObjectNamedID type, int value) {
		super();
		this.type = type;
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public ObjectNamedID getType() {
		return type;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setType(ObjectNamedID type) {
		this.type = type;
	}
}