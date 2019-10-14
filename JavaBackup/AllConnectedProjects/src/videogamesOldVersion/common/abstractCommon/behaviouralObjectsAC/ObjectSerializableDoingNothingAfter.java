package common.abstractCommon.behaviouralObjectsAC;

public interface ObjectSerializableDoingNothingAfter extends ObjectSerializable {
	@Override
	public default String doAfterDeserialization(Object arguments) {
		return null;
	}
}