package games.generic.controlModel.loaders;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import dataStructures.MapTreeAVL;
import games.generic.controlModel.GController;
import games.generic.controlModel.GMap;
import games.generic.controlModel.abilities.AbilityGeneric;
import games.generic.controlModel.events.GEvent;
import games.generic.controlModel.items.InventoryItem;
import games.generic.controlModel.misc.uidp.UIDPCollector;
import games.generic.controlModel.misc.uidp.UIDPCollector.UIDProviderLoadedListener;
import games.generic.controlModel.objects.OrbitingInteractiveObject;
import games.generic.view.dataProviders.DrawableObjProviderEventsObserver;
import tools.Comparators;
import tools.ObjectWithID;
import tools.UniqueIDProvider;
import tools.UniqueIDProvider.BaseUniqueIDProvider;

/**
 * Class that loads every {@link UIDPState} in order to load the state of the
 * {@link LoaderGeneric} that can be loaded (using the
 * {@link UIDProviderLoadedListener} list defined in
 * {@link LoaderUniqueIDProvidersState#enrichAllKnownUIDPLoadedListenerList(List)}.
 * <br>
 * This class may simple initialize new ones (NOT recommended if any
 * {@link ObjectWithID} is saved and restored from somewhere, like a file, a
 * database, etc, because its ID may be generated again)
 * <p>
 * TODO : all usage/reference of
 * {@link UIDPCollector#registerForProvider(Class, UIDProviderLoadedListener)}
 * should be refactored from a call in the static initalizer of some classes
 * (like {@link AbilityGeneric}, {@link GEvent}, {@link GMap}, etc) to the
 * definition of a static but non-final {@link UniqueIDProvider}, initialized to
 * null, and a {@link UIDProviderLoadedListener} static final instance
 * 
 * <p>
 * NOTE: this class is a {@link LoaderGeneric} but it HAS to be invoked as the
 * FIRST loader EVER
 * 
 * @author ottin
 *
 */
public abstract class LoaderUniqueIDProvidersState extends LoaderGeneric {

	//

	public LoaderUniqueIDProvidersState() {
//		this.ID = UIDP_LOADER.getNewID();
		this.classesUsingUIDPLoadableBackmap = MapTreeAVL.newMap(MapTreeAVL.Optimizations.MinMaxIndexIteration,
				Comparators.STRING_COMPARATOR);
		this.classesUsingUIDPLoadable = Collections.unmodifiableSet(this.classesUsingUIDPLoadableBackmap.entrySet());
	};

	protected final MapTreeAVL<String, UIDProviderLoadedListener> classesUsingUIDPLoadableBackmap;
	protected final Set<Map.Entry<String, UIDProviderLoadedListener>> classesUsingUIDPLoadable;

	/**
	 * Returns a set of ALL classes (their name, to be precise, depending on
	 * {@link UIDPCollector#CLASS_TO_NAME}) that requires to use a
	 * {@link UniqueIDProvider} for them (and their subclasses) AND the
	 * {@link UniqueIDProvider} state (a {@link UIDPState} has to be saved and
	 * restored between game runs.
	 * 
	 * It can also be used by the {@link ModGa}
	 * 
	 * @return
	 */
	public Set<Map.Entry<String, UIDProviderLoadedListener>> getClassesUsingUIDPLoadable() {
		return classesUsingUIDPLoadable;
	}

	//

	/**
	 * Read all {@link UIDPState} stored somewhere
	 * 
	 * @return
	 */
	protected abstract Iterable<UIDPState> readSavedUIDPStates();

	/**
	 * NOTE FOR OVERRIDING:
	 * 
	 * <pre>
	<code>
	protected void enrichAllKnownUIDPLoadedListenerList(List&#60;Map.Entry&#60;Class&#60;&#63;&#62;, UIDProviderLoadedListener&#62;&#62; list) {
		
		// MANDATORY
		super.enrichAllKnownUIDPLoadedListenerList(list);
	
		// ADD HERE THE NEW ONES , like :
		list.add(Map.entry(MyFancyFooClass.class, MyFancyFooClass.UIDP_LOADED_LISTENER_MFFC));
		list.add(Map.entry(MyFancyBarInterface.class, MyFancyBarInterface.UIDP_LOADED_LISTENER_MFBI));
	}
	</code>
	
	where
	
	<code>
	public class MyFancyFooClass {
		private static UniqueIDProvider UIDP_MFFC = null;
		
		public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_MFFC =
			uidp -> { if(uidp != null) { UIDP_MFFC = uidp; } };
		
		// since the provider is not final, it's not advised to make it public:
		// this function makes it accessible without exposing the non-final pointer
		public static UniqueIDProvider getUniqueIDProvider_MyFancyFooClass(){
			return UIDP_MFFC;
		}
		
		// other stuffs
	}
	</code>
	
	and
	
	<code>
	public interface MyFancyBarInterface {
		// suggested implementation: using UIDPLoadableFromCollector
		 
		public static final UniqueIDProvider UIDP_MFBI = new UIDPLoadableFromCollector&#60;&#62;(MyFancyBarInterface.class);
		
		&#64;SuppressWarnings("unchecked")
		public static final UIDProviderLoadedListener UIDP_LOADED_LISTENER_MFBI = ((UIDPLoadableFromCollector&#60;MyFancyBarInterface&#62;) UIDP_MyFancyBarInterface)
			.getUidpLoaderListener();
		
		public static UniqueIDProvider getUniqueIDProvider_MyFancyBarInterface(){
			return UIDP_MFBI;
		}
		
		// other stuffs
	}
	</code>
	 * </pre>
	 * <p>
	 * Add new {@link UIDProviderLoadedListener} taken somewhere, probably as a
	 * static field of some class
	 * 
	 * @param list the list to be enriched using {@link List#add(Object)}.
	 */
	public void enrichAllKnownUIDPLoadedListenerList(List<Map.Entry<Class<?>, UIDProviderLoadedListener>> list) {
		list.addAll(Arrays.asList(
				//
				Map.entry(AbilityGeneric.class, AbilityGeneric.UIDP_LOADED_LISTENER_ABILITY),
				Map.entry(GEvent.class, GEvent.UIDP_LOADED_LISTENER_EVENT),
				Map.entry(GMap.class, GMap.UIDP_LOADED_LISTENER_GMAP),
				Map.entry(InventoryItem.class, InventoryItem.UIDP_LOADED_LISTENER_INVENTORY),
				// special cases / subclasses
				Map.entry(OrbitingInteractiveObject.class,
						OrbitingInteractiveObject.UIDP_LOADED_LISTENER_ORBITING_INTERACTIVE_OBJECT),
				// GUI
				Map.entry(DrawableObjProviderEventsObserver.class,
						DrawableObjProviderEventsObserver.UIDP_LOADED_LISTENER_DOPEO)

		));
	}

	/***
	 * NOTE FOR OVERRIDING: <code>
	protected List<UIDProviderLoadedListener> getAllKnownUIDPLoadedListener() {
		List<UIDProviderLoadedListener> list;
		list = super.getAllKnownUIDPLoadedListener();
		// ADD HERE THE NEW ONES
		return list;
	}
	 * 
	 * 
	 * </code>
	 * 
	 * @return
	 */
	protected final List<Map.Entry<Class<?>, UIDProviderLoadedListener>> getAllKnownUIDPLoadedListener() {
		List<Map.Entry<Class<?>, UIDProviderLoadedListener>> list;
		list = new LinkedList<>();
		this.enrichAllKnownUIDPLoadedListenerList(list);
		return list;
	}

	//

	public <E> void registerClassAsUsingUIDP(Class<E> clazz, UIDProviderLoadedListener loadListener) {
		this.classesUsingUIDPLoadableBackmap.put(UIDPState.CLASS_TO_NAME.apply(clazz), loadListener);
	}

	@Override
	public LoadStatusResult loadInto(GController gc) {

		this.getAllKnownUIDPLoadedListener().forEach(e -> this.registerClassAsUsingUIDP(e.getKey(), e.getValue()));

		try {
			for (UIDPState state : this.readSavedUIDPStates()) {
				try {
					UIDPCollector.loadProvider(state.classIdentifier, state);
					this.classesUsingUIDPLoadableBackmap//
							.get(state.classIdentifier)//
							.accept(UIDPCollector.getProvider(state.classIdentifier));
				} catch (Exception e2) {
					gc.getLogger().logException(e2);
				}
			}
		} catch (Exception e) {
			gc.getLogger().logException(e);
			return LoadStatusResult.MinorFail;
		}

		return LoadStatusResult.Success;
	}

	//

	public static class UIDPState implements Serializable {
		public static final Function<Class<?>, String> CLASS_TO_NAME = Class::getName;
		private static final long serialVersionUID = -401587584588922221L;

		public UIDPState(Class<?> clazz, long state) { this(CLASS_TO_NAME.apply(clazz), state); }

		public UIDPState(String classIdentifier, long state) {
			super();
			this.state = state;
			this.classIdentifier = classIdentifier;
		}

		public final long state;
		/**
		 * Some Classes may use a private {@link UniqueIDProvider}. This variable is
		 * used to identify that class.
		 */
		public final String classIdentifier;
	}

	//

	public static class UIDPLoadable extends BaseUniqueIDProvider {

		public UIDPState getState() { return this.getState(getClass()); }

		public void loadState(UIDPState state) { this.idProgressive = state.state; }

		public UIDPState getState(Class<?> clazz) { return new UIDPState(clazz, idProgressive); }

	}
}