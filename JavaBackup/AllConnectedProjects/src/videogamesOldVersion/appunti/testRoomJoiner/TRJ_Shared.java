package appunti.testRoomJoiner;

import java.io.Serializable;

public class TRJ_Shared {

	public static enum JPIV_R_Fields {
		RobodromeName("Robodrome: ", r -> r.getRobodromeName())//
		, OwnerName("Owner: ", r -> r.getOwner().getName())//
		, PlayersCountLogged("Players: ", r -> Integer.toString(r.getConnectedPlayer().size()))//
		, PlayerMax("/ max: ", r -> Integer.toString(r.getMaxAmountPlayer()));

		public final String pretext;
		public final RoomInformationExtractor roomInformationExtractor;

		JPIV_R_Fields(String t, RoomInformationExtractor rie) {
			this.pretext = t;
			this.roomInformationExtractor = rie;
		}
	}

	public static final JPIV_R_Fields[] valuesJPIV_R_Fields = JPIV_R_Fields.values();

	public interface RoomInformationExtractor extends Serializable {
		public String extractInfo(Room r);
	}
}