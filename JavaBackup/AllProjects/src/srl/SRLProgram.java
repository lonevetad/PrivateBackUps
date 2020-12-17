package srl;

import srl.impl.SRLBody;
import srl.parsers.SRLParser;

/** Interpreter of a SRL program. Built via {@link SRLParser}. */
public class SRLProgram implements Runnable {

	public SRLProgram(SRLBody prog) { this.prog = prog; }

	protected final SRLBody prog;
	protected SRLRegistersCollection registers;

	public SRLBody getProg() { return prog; }

	public SRLRegistersCollection getRegisters() { return registers; }

	public void setRegisters(SRLRegistersCollection registers) { this.registers = registers; }

	public void addStatement(SRLCodeStatement s) { prog.addStatement(s); }

	/** See {@link SRLRegistersCollection#resetRegisters()}. */
	public void resetRegisters() {
		this.registers.resetRegisters();
	}

	//

	@Override
	public void run() { this.execute(); }

	public void execute() { prog.perform(registers); }
}