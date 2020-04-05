package appunti.testRoomJoiner;

import java.util.List;

import importedUtilities.abstractCommon.behaviouralObjectsAC.MyComparator;
import importedUtilities.mainTools.MyLinkedList;

public class Room {

	private static int idRoomProg = 0;
	public static final MyComparator<Room> ROOM_COMPARATOR = (r1, r2) -> {
		if (r1 == r2) return 0;
		if (r1 == null) return -1;
		if (r2 == null) return 1;
		return Integer.compare(r1.idRoom, r2.idRoom);
	};

	public Room() {
		idRoomInt = idRoomProg++;
		idRoom = idRoomInt;
		connectedPlayer = new MyLinkedList<>();
	}

	private final int idRoomInt;
	protected int maxAmountPlayer;
	private final Integer idRoom;
	protected String robodromeName;
	protected List<Player> connectedPlayer;
	protected Player owner;

	//

	// TODO GETTER

	public int getIdRoomInt() {
		return idRoomInt;
	}

	public Integer getIdRoom() {
		return idRoom;
	}

	public String getRobodromeName() {
		return robodromeName;
	}

	public int getMaxAmountPlayer() {
		return maxAmountPlayer;
	}

	public Player getOwner() {
		return owner;
	}

	public List<Player> getConnectedPlayer() {
		return connectedPlayer;
	}

	//

	// TODO SETTER

	public Room setMaxAmountPlayer(int maxAmountPlayer) {
		this.maxAmountPlayer = maxAmountPlayer;
		return this;
	}

	public Room setOwner(Player owner) {
		this.owner = owner;
		return this;
	}

	public Room setRobodromeName(String robodromeName) {
		this.robodromeName = robodromeName;
		return this;
	}

	//

	//

}