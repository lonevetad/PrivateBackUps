package srl.impl;

import java.util.LinkedList;
import java.util.List;

import srl.SRLCodeStatement;
import srl.SRLRegistersCollection;

/** Just a scope, useful for {@link SRLFor}.S */
public class SRLBody implements SRLCodeStatement {
	private static final long serialVersionUID = 55027084952165L;

	public SRLBody() { this.body = new LinkedList<>(); }

	protected List<SRLCodeStatement> body;

	public List<SRLCodeStatement> getBody() { return body; }

	public void addStatement(SRLCodeStatement s) { body.add(s); }

	@Override
	public void runCode(SRLRegistersCollection registers, boolean isNOTInverse) {
		if (this.body.isEmpty()) { System.out.println("SRLBody empty"); }
		this.body.forEach(o -> o.runCode(registers, isNOTInverse));
	}

	@Override
	public SRLBody clone() {
		SRLBody b;
		b = new SRLBody();
		this.body.forEach(stat -> b.addStatement(stat.clone()));
		return b;
	}
}