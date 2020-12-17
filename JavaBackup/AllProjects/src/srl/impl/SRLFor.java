package srl.impl;

import java.util.Objects;

import srl.SRLCodeStatement;
import srl.SRLRegistersCollection;
import srl.SRLRegistersCollection.Register;

public class SRLFor implements SRLCodeStatement {

	public SRLFor(String registerName, SRLBody body) {
		super();
		this.registerName = registerName;
		this.body = body;
	}

	protected final String registerName;
	protected final SRLBody body;

	public String getRegisterName() { return registerName; }

	public SRLBody getBody() { return body; }

	@Override
	public void perform(SRLRegistersCollection registers, boolean isNOTInverse) {
		long rep;
		Register r;
		r = registers.getRegister(registerName);
		Objects.requireNonNull(r);
		if ((rep = r.value) < 0) {
			isNOTInverse = !isNOTInverse; // by definition
			rep = -rep;
		}
		if (rep != 0) {
			while (--rep >= 0) {
				body.perform(registers, isNOTInverse);
			}
		}
	}
}