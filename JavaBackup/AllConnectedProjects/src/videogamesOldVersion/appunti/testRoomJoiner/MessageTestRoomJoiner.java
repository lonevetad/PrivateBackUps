package appunti.testRoomJoiner;

import messagges.MessageGeneric;

public class MessageTestRoomJoiner extends MessageGeneric {
	private static final long serialVersionUID = 625108880876L;

	public MessageTestRoomJoiner(Player sender) {
		super();
		this.sender = sender;
	}

	{// instance initializer
		isGeneratedByServer = false;
		sender = null;
	}

	protected boolean isGeneratedByServer;
	protected transient Player sender;

	//

	public boolean isGeneratedByServer() {
		return isGeneratedByServer;
	}

	public Player getSender() {
		return sender;
	}

	//

	public MessageTestRoomJoiner setSender(Player sender) {
		this.sender = sender;
		return this;
	}

	public MessageTestRoomJoiner setGeneratedByServer(boolean isGeneratedByServer) {
		this.isGeneratedByServer = isGeneratedByServer;
		return this;
	}
}