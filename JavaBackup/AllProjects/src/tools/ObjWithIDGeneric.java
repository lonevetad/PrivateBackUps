package tools;

public interface ObjWithIDGeneric<IDType> extends Stringable {
	public IDType getID();
}