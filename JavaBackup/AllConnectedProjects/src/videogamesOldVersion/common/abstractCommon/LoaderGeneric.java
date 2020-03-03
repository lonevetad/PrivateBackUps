package common.abstractCommon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

import javax.swing.ImageIcon;

import common.EnumGameObjectTileImageCollection;
import common.FullReloadEnvironment;
import common.abstractCommon.AbstractMapGame.AbstractMementoMapGame;
import common.abstractCommon.referenceHolderAC.ImageAnimationHolder;
import common.abstractCommon.referenceHolderAC.LoggerMessagesHolder;
import common.mainTools.AnimatedImage;
import common.mainTools.FileUtilities;
import common.mainTools.LoggerMessages;
import common.mainTools.mOLM.abstractClassesMOLM.AbstractMatrixObjectLocationManager;
import common.mainTools.mOLM.abstractClassesMOLM.DoSomethingWithNode;
import common.mainTools.mOLM.abstractClassesMOLM.ObjectWithID;
import common.utilities.CastingClass;

public abstract class LoaderGeneric implements LoggerMessagesHolder {
	private static final long serialVersionUID = -8541015888090L;
	public static final char sc = File.separatorChar;
	public static final String superPath = "resources" + sc;

	public static enum LoadWriteType implements Serializable {
		BinaryRaw, Memento, TextFile;

		public static LoadWriteType getOrDefault(LoadWriteType lt) {
			return lt != null ? lt : BinaryRaw;
		}
	}

	protected LoggerMessages log;

	//

	@Override
	public LoggerMessages getLog() {
		return log;
	}

	@Override
	public LoaderGeneric setLog(LoggerMessages log) {
		this.log = log;
		return this;
	}

	//

	public abstract String getPath();

	public abstract String getPathImage();

	public abstract String getPathMap();

	public abstract String getPathSaves();

	public abstract String getPathSound();

	public abstract String getMapExtension();

	public abstract String getSavesExtension();

	public String saveMap(AbstractMapGame map) {
		return saveMap(map, LoadWriteType.BinaryRaw);
	}

	public String saveMap(AbstractMapGame map, LoadWriteType loadType) {
		String error, path;
		AbstractMapGame.AbstractMementoMapGame memento;

		error = null;
		loadType = LoadWriteType.getOrDefault(loadType);
		if (map != null) {
			path = getPathMap() + map.getMapName() + getMapExtension();
			switch (loadType) {
			case BinaryRaw:
				error = FileUtilities.writeObject(map, path);
				break;
			case Memento:
				memento = map.createMemento();
				if (memento != null) {
					error = FileUtilities.writeObject(memento, path);
				} else
					error = "ERROR: on " + this.getClass().getSimpleName() + ".saveMap, memento is null";
				break;
			case TextFile:
				// throw new UnsupportedOperationException()
				error = "ERROR: on " + this.getClass().getSimpleName()
						+ ".saveMap, Unable to write a map into a txt file";
				break;
			default:
				// throw new IllegalArgumentException()
				error = "ERROR: on " + this.getClass().getSimpleName() + ".saveMap, Unknown load-write type: "
						+ loadType;
			}
		} else
			error = "ERROR: on " + this.getClass().getSimpleName() + ".saveMap, map is null";
		return error;
	}

	public String saveSaveData(AbstractSaveData save) {
		return saveSaveData(save, LoadWriteType.BinaryRaw);
	}

	public String saveSaveData(AbstractSaveData save, LoadWriteType loadType) {
		String error, path;
		// AbstractSaveData memento;
		error = null;
		loadType = LoadWriteType.getOrDefault(loadType);
		if (save != null) {
			path = getPathSaves() + save.getSaveName() + getSavesExtension();
			switch (loadType) {
			case BinaryRaw:
				error = FileUtilities.writeObject(save, path);
				break;
			case Memento:
				// throw new UnsupportedOperationException();
				error = "ERROR: on " + this.getClass().getSimpleName()
						+ ".saveMap, Unable to write a save into a Memento binary file";
			case TextFile:
				// throw new UnsupportedOperationException()
				error = "ERROR: on " + this.getClass().getSimpleName()
						+ ".saveMap, Unable to write a save into a txt file";
				break;
			default:
				// throw new IllegalArgumentException()
				error = "ERROR: on " + this.getClass().getSimpleName() + ".saveSaveData, Unknown load-write type: "
						+ loadType;
			}
		} else
			error = "ERROR: on " + this.getClass().getSimpleName() + ".saveSaveData, map is null";
		return error;
	}

	/** Override designed */
	public void finishReloadingOWIDAfterDeserialization(ObjectWithID owid, FullReloadEnvironment fre) {
		/**
		 * //<br>
		 * ObjectTiled ot;//<br>
		 * TileMap tmCached;//<br>
		 * EnumTileImageCollection listTileMapEnum;//<br>
		 * if (owid instanceof ObjectTiled) {//<br>
		 * ot = (ObjectTiled) owid;//<br>
		 * listTileMapEnum = fre.getListTileMapEnum();//<br>
		 * tmCached = listTileMapEnum.getTileMapInstance(fre.getMain(),
		 * ot.getImageFilename());//<br>
		 * if (tmCached != null) {//<br>
		 * ot.shareImagesFrom(tmCached);//<br>
		 * }//<br>
		 * }//<br>
		 */
	}

	/***/
	public AbstractMapGame loadMap(String mapName, MainController main,
			EnumGameObjectTileImageCollection listTileMapEnum) {
		return loadMap(mapName, main, listTileMapEnum, LoadWriteType.BinaryRaw);
	}

	public AbstractMapGame loadMap(String mapName, MainController main,
			EnumGameObjectTileImageCollection listTileMapEnum, LoadWriteType loadType) {
		AbstractMapGame map;
		AbstractMapGame.AbstractMementoMapGame memento;
		String error;
		Object o;
		FullReloadEnvironment fre;

		if (main == null || listTileMapEnum == null) {
			System.err.println("ERROR: on loadMap, main is null? " + (main == null) + ", tile list null? "
					+ (listTileMapEnum == null));
			return null;
		}
		loadType = LoadWriteType.getOrDefault(loadType);
		map = null;
		error = null;
		if (mapName != null) {
			o = FileUtilities.readObject(getPathMap() + mapName + getMapExtension());

			// memento = map.createMemento();
			if (o != null) {
				fre = new FullReloadEnvironment(main, listTileMapEnum);
				switch (loadType) {
				case BinaryRaw:
					if (o instanceof AbstractMapGame) {
						AbstractMatrixObjectLocationManager molms[];
						DoSomethingWithNode instanceOWIDFinisher;
						map = (AbstractMapGame) o;
						molms = map.getMolms();
						if (molms != null && molms.length > 0) {
							instanceOWIDFinisher = (m, owid, x, y) -> {
								if (owid != null) finishReloadingOWIDAfterDeserialization(owid, fre);
								return null;
							};
							for (AbstractMatrixObjectLocationManager molm : molms) {
								molm.forEach(instanceOWIDFinisher);
							}
						}
					} else
						error = "ERROR: on " + this.getClass().getSimpleName()
								+ ".loadMap, object read is not a AbstractMapGameGeneric instance: "
								+ o.getClass().getName();
					break;
				case Memento:
					if (o instanceof AbstractMementoMapGame) {
						memento = (AbstractMementoMapGame) o;
						try {

							o = memento.reinstanceFromMe(fre);
							if (o != null && o instanceof AbstractMapGame) {
								map = (AbstractMapGame) o;
							} else
								error = "ERROR: on loadMap, memento creates a strange instance: "
										+ (o == null ? "null" : o.getClass().getName());
						} catch (Exception e) {
							e.printStackTrace();
							error = ("ERROR: on loadMap, exception raised:\n" + e);
						}
						// error =
						// FileUtilities.writeObject(memento, getPathMap() +
						// map.getMapName() + getMapExtension());
					} else
						error = "ERROR: on " + this.getClass().getSimpleName()
								+ ".loadMap, object read is of wrong class: " + o.getClass().getName();
					break;
				case TextFile:
					throw new UnsupportedOperationException("Unable to read a map from a txt file");
				default:
					throw new IllegalArgumentException("Unknown load-write type: " + loadType);
				}
			} else
				error = "ERROR: on " + this.getClass().getSimpleName() + ".saveMap, object read is null";
		} else
			error = "ERROR: on " + this.getClass().getSimpleName() + ".saveMap, map is null";

		// if (map != null) {
		/*
		 * if (map.getMolms() != null && map.getMolms().length > 0) { for
		 * (AbstractMatrixObjectLocationManager molm : map.getMolms()) {
		 * molm.resetDefaultInstances(); } }
		 */
		// map.doAfterDeserialization(main.mainSetter);
		// }

		if (error != null) main.log(error);
		return map;
	}

	/** Done: 06/12/2017 */
	public ImageAnimationHolder loadImage(String filename) throws IOException {
		ImageAnimationHolder iah;
		Object o;
		AnimatedImage ai;

		iah = null;
		// before all, animation
		ai = AnimatedImage.newInstanceReadingImagesFromFolder(getPathImage(), filename, getClass());
		if (ai != null) {
			iah = ImageAnimationHolder.newDefaultImplementation();
			iah.setAnimatedImage(ai);
		} else {
			// try with raw image
			o = FileUtilities.getBufferedImageScanningFoldersAndSubFolders(filename, getPathImage(), getClass());
			if (o != null) {
				// something found
				iah = ImageAnimationHolder.newDefaultImplementation();
				if (o instanceof BufferedImage) {
					iah.setImageOriginal((BufferedImage) o);
				} else if (o instanceof Image) {
					iah.setImageOriginal(CastingClass.castImage_ARGB_ToBufferedImage((Image) o));
				} else if (o instanceof ImageIcon) {
					iah.setImageOriginal(CastingClass.castImage_ARGB_ToBufferedImage(((ImageIcon) o).getImage()));
				} else
					throw new IOException("Unrecognize image type: an image was found at:\n\t--" + getPathImage()
							+ "-- with filename:\n\t--" + filename
							+ "-- but the reading process has returned a strange instance of class: "
							+ o.getClass().getName());
			}
		}
		if (iah != null) iah.setImageName(filename);
		return iah;
	}

	//

	// TODO STATIC

	/**
	 * Returns null in case of error, the file describing the given folder
	 * otherwise
	 */
	public static File checkAndCreateFolder(String path) {
		File f;
		f = null;
		if (path != null && (path = path.trim()).length() > 0) {
			f = new File(path);
			if (!(f.exists() && f.isDirectory())) {
				return f.mkdirs() ? f : null;
			}
			// else f=null
		}
		return f;
	}

	public static Exception deleteFile(String path) {
		File f;
		Exception e;
		f = null;
		e = null;
		if (path != null && (path = path.trim()).length() > 0) {
			f = new File(path);
			try {
				Files.delete(f.toPath());
			} catch (IOException e1) {
				// e1.printStackTrace();
				e = e1;
			}
		}
		return e;
	}
}