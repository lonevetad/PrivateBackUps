package appunti.testRoomJoiner.messages;

import appunti.testRoomJoiner.MessageTestRoomJoiner;
import appunti.testRoomJoiner.Player;
import appunti.testRoomJoiner.ServerTRJ;
import appunti.testRoomJoiner.ServerTRJ.ConnectionStatus;

public class MessageTRJ_ConnectionStatus extends MessageTestRoomJoiner {
	private static final long serialVersionUID = 526087070833220L;

	public MessageTRJ_ConnectionStatus(Player sender) {
		super(sender);
		connectionStatus = ConnectionStatus.UNDEFINED.id;
	}

	public MessageTRJ_ConnectionStatus(Player sender, ConnectionStatus cs) {
		this(sender);
		this.setConnectionStatus(cs);
	}

	int connectionStatus;

	//

	public void setConnectionStatus(ConnectionStatus cs) {
		connectionStatus = (cs == null ? ConnectionStatus.UNDEFINED : cs).id;
	}

	public int getConnectionStatusID() {
		return connectionStatus;
	}

	public ConnectionStatus getConnectionStatus() {
		return ServerTRJ.valuesConnectionStatus[connectionStatus];
	}
}
