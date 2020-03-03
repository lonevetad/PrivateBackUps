package appunti.testRoomJoiner.messages;

import appunti.testRoomJoiner.Player;

public class MessageTRJ_SillyPrint extends MessageTRJ_Command {
	private static final long serialVersionUID = 84508625005L;

	public MessageTRJ_SillyPrint(Player sender) {
		super(sender);
	}

	@Override
	public void run() {
		getEnvironment().getLog().log("\n\r Silly print: " + getText());
	}

}