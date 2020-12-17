package srl;

public interface SRLCodeStatement {
	public default void perform(SRLRegistersCollection registers) { perform(registers, true); }

	public void perform(SRLRegistersCollection registers, boolean isNOTInverse);
}