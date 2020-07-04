package stuffs.logic;

import java.util.Comparator;

public class AtomLogicProposition {
	protected static int idProg = 0;
	public static final Comparator<AtomLogicProposition> COMPARATOR = (c1, c2) -> {
		if (c1 == c2)
			return 0;
		if (c1 == null)
			return -1;
		if (null == c2)
			return +1;
		if (c1.ID.equals(c2.ID)) {
			return (c1.isPositive == c2.isPositive) ? 0 : //
			c1.isPositive ? 1 : -1;
		}
		return c1.ID > c2.ID ? 1 : -1;
	};

	public static AtomLogicProposition fromString(String s) {
		boolean ip;
		char c;
		int i, len, pow, id, minIndex;
//		ip = false;
		if (s == null || (len = s.length()) == 0 || ((ip = s.charAt(0) == '~') && len == 1))
			return null;
		id = 0;
		minIndex = ip ? 0 : 1;
		pow = 1;
		while (--len >= minIndex) {
			c = s.charAt(len);
			if (!(('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')))
				return null;
			i = c - 'a';
			id += pow * i;
			pow *= 26;
		}
		return new AtomLogicProposition(ip, id, s);
	}

	//

	public AtomLogicProposition() { this(true); }

	public AtomLogicProposition(boolean isPositive) { this(isPositive, idProg++); }

	public AtomLogicProposition(boolean isPositive, int i) {
		this.isPositive = isPositive;
		setIDAndName(i);
	}

	protected AtomLogicProposition(boolean p, Integer i, String n) {
		this.isPositive = p;
		this.ID = i;
		this.name = n;
	}

	public final boolean isPositive;
	protected Integer ID;
	protected String name;

	public AtomLogicProposition negate() {
		return new AtomLogicProposition(!this.isPositive, ID, //
				(this.isPositive) ? ('~' + this.name) : this.name.substring(1)//
		);
	}

	public boolean isPositive() { return isPositive; }

	public Integer getID() { return ID; }

	public String getName() { return name; }

	public AtomLogicProposition setIDAndName(int i) {
		int remainder;
		StringBuilder sb;
		if ((idProg >= 0 && i >= idProg) || (idProg < 0 && i <= idProg)) { idProg = i + 1; }
		this.ID = i;
		sb = new StringBuilder(4);
		if (i == 0)
			sb.append('a');
		while (i != 0) {
			remainder = i % 26;
			i /= 26;
			sb.append((char) ('a' + remainder));
		}
		if (!isPositive)
			sb.append('~');
		name = sb.reverse().toString();
		return this;
	}

	@Override
	public String toString() { return name; }

	public static void main(String[] args) {
		int i, len;
		boolean[] ap = { true, false, true, false, true, true, false, true, false };
		int[] aid = { 3, 5, 25, 26, 27, (26 * 3 + 2), (26 * 26 * 2 + 26 * 8 + 7), (26 * 26 * 26), 0 };
		len = ap.length;
		i = -1;
		while (++i < len) {
			System.out.println(new AtomLogicProposition(ap[i], aid[i]));
		}
		System.out.println("default-generated id:");
		System.out.println(new AtomLogicProposition(false));
		System.out.println(new AtomLogicProposition(false));
	}
}