package tests.tGame.tgEvent1;

import java.util.Set;
import java.util.function.Consumer;

import games.generic.ObjectWithID;
import games.generic.controlModel.GModality;
import games.generic.controlModel.GModel;
import games.generic.controlModel.GObjectsHolder;
import games.generic.controlModel.gameObj.TimedObject;
import games.generic.controlModel.subImpl.GameModelTimeBased;

public class TestGModel {

	public static void main(String[] args) {
		GModel gm;
		GameModelTimeBased gmt;
		Consumer<ObjectWithID> printer;
		printer = o -> System.out.println(o.getID());
		gm = new GModel() {
			@Override
			public void onCreate() {
				System.out.println("CIAOOOO");
			}
		};
		System.out.println("helooo " + gm.addObjHolder("TIME", new GOH()));
		System.out.println("FINE");
		gmt = new GameModelTimeBased() {

			@Override
			public void onCreate() {
				System.out.println("REAL timed model");
			}
		};
		System.out.println("helooo 2.0 " + gmt.addObjHolder("MEMORYLESS", new GOH()));
		gmt.addTimedObject(new TO(777));
		gmt.addTimedObject(new TO(12));
		gmt.add(() -> Integer.valueOf(44));
		gmt.add(() -> Integer.valueOf(33));
		gmt.add(() -> Integer.valueOf(-55));
		System.out.println("fine 2, stampa di tutto");
		gmt.forEach(printer);
		System.out.println("ora stampa solo i timed by " + GameModelTimeBased.NAME_TIMED_OBJECT_HOLDER);
		gmt.getObjHolder(GameModelTimeBased.NAME_TIMED_OBJECT_HOLDER).forEach(printer);
		System.out.println("ora stampa solo i timed by MEMORYLESS");
		gmt.getObjHolder("MEMORYLESS").forEach(printer);
		System.out.println("\n\n in the end, print just GModel own owids");
		gmt.getObjects().forEach(printer);
	}

	static class TO implements TimedObject {
		Integer id;

		public TO(Integer id) {
			this.id = id;
		}

		@Override
		public Integer getID() {
			return id;
		}

		@Override
		public void act(GModality modality, long milliseconds) {
			System.out.println("time " + milliseconds);
		}
	}

	static class GOH implements GObjectsHolder {
		@Override
		public boolean add(ObjectWithID o) {
			System.out.println("ehehe adding " + o.getID());
			return false;
		}

		@Override
		public boolean remove(ObjectWithID o) {
			return false;
		}

		@Override
		public boolean contains(ObjectWithID o) {
			return false;
		}

		@Override
		public void forEach(Consumer<ObjectWithID> action) {
		}

		@Override
		public ObjectWithID get(Integer id) {
			return null;
		}

		@Override
		public Set<ObjectWithID> getObjects() {
			return null;
		}

		@Override
		public boolean removeAll() {
			return false;
		}
	}
}