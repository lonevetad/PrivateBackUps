package tools;

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
		protected int idProgressive = 0;

		@Override
		public Integer getNewID() {
			return idProgressive++;
		}
	}
}