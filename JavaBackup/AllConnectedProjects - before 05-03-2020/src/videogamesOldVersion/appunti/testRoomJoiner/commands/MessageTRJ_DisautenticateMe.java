package appunti.testRoomJoiner.commands;

import appunti.testRoomJoiner.Player;
import appunti.testRoomJoiner.messages.MessageTRJ_Command;

public class MessageTRJ_DisautenticateMe extends MessageTRJ_Command {
	private static final long serialVersionUID = 4150818056201845L;

	public MessageTRJ_DisautenticateMe(Player sender) {
		super(sender);
	}

	String username;

	public String getUsername() {
		return username;
	}

	public MessageTRJ_DisautenticateMe setUsername(String username) {
		this.username = username;
		return this;
	}

	//

	@Override
	public void run() {
		getEnvironment().disconnect(username);
	}

}
