package games.generic;

/** Generates new ID */
public interface UniqueIDProvider {

	public static final UniqueIDProvider GENERAL_UNIQUE_ID_PROVIDER = newBasicIDProvider();

	public static UniqueIDProvider newBasicIDProvider() {
		return new BaseUniqueIDProvider();
	}

	//

	public Integer getNewID();

//

	public static class BaseUniqueIDProvider implements UniqueIDProvider {
		protected static int idProgressive;

		@Override
		public Integer getNewID() {
			return idProgressive++;
		}
	}
}