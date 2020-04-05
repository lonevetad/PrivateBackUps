package appunti.testRoomJoiner;

public class Player {

	public Player(ConnectionTRJ c) {
		this.connection = c;
	}

	public String name;
	ConnectionTRJ connection;

	//

	// TODO GETTER

	public String getName() {
		return name;
	}

	public ConnectionTRJ getConnection() {
		return connection;
	}

	//

	// TODO GETTER

	public Player setName(String name) {
		if (name != null) {
			if ("".equals(name.trim())) throw new IllegalArgumentException("Empty name");
			this.name = name;
		}
		return this;
	}
}