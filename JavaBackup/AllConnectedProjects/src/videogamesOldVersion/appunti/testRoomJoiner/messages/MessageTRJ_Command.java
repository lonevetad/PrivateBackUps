package appunti.testRoomJoiner.messages;

import appunti.testRoomJoiner.MessageTestRoomJoiner;
import appunti.testRoomJoiner.Player;
import appunti.testRoomJoiner.ServerTRJ;

public abstract class MessageTRJ_Command extends MessageTestRoomJoiner implements Runnable {
	private static final long serialVersionUID = -92907170770L;

	public MessageTRJ_Command(Player sender) {
		super(sender);
	}

	ServerTRJ environment;

	public ServerTRJ getEnvironment() {
		return environment;
	}

	public MessageTRJ_Command setEnvironment(ServerTRJ environment) {
		this.environment = environment;
		return this;
	}

}