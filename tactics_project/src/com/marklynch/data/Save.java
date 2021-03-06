package com.marklynch.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.AggressiveWildAnimal;
import com.marklynch.objects.actors.Animal;
import com.marklynch.objects.actors.CarnivoreNeutralWildAnimal;
import com.marklynch.objects.actors.Doctor;
import com.marklynch.objects.actors.Fish;
import com.marklynch.objects.actors.Guard;
import com.marklynch.objects.actors.HerbivoreWildAnimal;
import com.marklynch.objects.actors.Human;
import com.marklynch.objects.actors.Monster;
import com.marklynch.objects.actors.Pig;
import com.marklynch.objects.actors.Player;
import com.marklynch.objects.actors.Thief;
import com.marklynch.objects.actors.TinyNeutralWildAnimal;
import com.marklynch.objects.actors.Trader;
import com.marklynch.objects.actors.WildAnimal;
import com.marklynch.objects.armor.Armor;
import com.marklynch.objects.armor.BodyArmor;
import com.marklynch.objects.armor.Helmet;
import com.marklynch.objects.armor.LegArmor;
import com.marklynch.objects.armor.Weapon;
import com.marklynch.objects.inanimateobjects.Bed;
import com.marklynch.objects.inanimateobjects.Carcass;
import com.marklynch.objects.inanimateobjects.ConveyerBelt;
import com.marklynch.objects.inanimateobjects.Corpse;
import com.marklynch.objects.inanimateobjects.Door;
import com.marklynch.objects.inanimateobjects.Fireplace;
import com.marklynch.objects.inanimateobjects.Food;
import com.marklynch.objects.inanimateobjects.Furnace;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.GameObjectExploder;
import com.marklynch.objects.inanimateobjects.Gold;
import com.marklynch.objects.inanimateobjects.HidingPlace;
import com.marklynch.objects.inanimateobjects.Inspectable;
import com.marklynch.objects.inanimateobjects.Landmine;
import com.marklynch.objects.inanimateobjects.Liquid;
import com.marklynch.objects.inanimateobjects.MapMarker;
import com.marklynch.objects.inanimateobjects.Matches;
import com.marklynch.objects.inanimateobjects.MeatChunk;
import com.marklynch.objects.inanimateobjects.MineCart;
import com.marklynch.objects.inanimateobjects.Mirror;
import com.marklynch.objects.inanimateobjects.Openable;
import com.marklynch.objects.inanimateobjects.Orb;
import com.marklynch.objects.inanimateobjects.Portal;
import com.marklynch.objects.inanimateobjects.PressurePlate;
import com.marklynch.objects.inanimateobjects.PressurePlateRequiringSpecificItem;
import com.marklynch.objects.inanimateobjects.Rail;
import com.marklynch.objects.inanimateobjects.RemoteDoor;
import com.marklynch.objects.inanimateobjects.Roof;
import com.marklynch.objects.inanimateobjects.Searchable;
import com.marklynch.objects.inanimateobjects.Seesaw;
import com.marklynch.objects.inanimateobjects.SmallHidingPlace;
import com.marklynch.objects.inanimateobjects.Spikes;
import com.marklynch.objects.inanimateobjects.Stampable;
import com.marklynch.objects.inanimateobjects.Storage;
import com.marklynch.objects.inanimateobjects.Stump;
import com.marklynch.objects.inanimateobjects.Switch;
import com.marklynch.objects.inanimateobjects.Tree;
import com.marklynch.objects.inanimateobjects.Vein;
import com.marklynch.objects.inanimateobjects.VoidHole;
import com.marklynch.objects.inanimateobjects.Wall;
import com.marklynch.objects.inanimateobjects.WallSupport;
import com.marklynch.objects.inanimateobjects.WantedPoster;
import com.marklynch.objects.inanimateobjects.WaterBody;
import com.marklynch.objects.inanimateobjects.WaterSource;
import com.marklynch.objects.inanimateobjects.Window;
import com.marklynch.objects.tools.Axe;
import com.marklynch.objects.tools.Bell;
import com.marklynch.objects.tools.FishingRod;
import com.marklynch.objects.tools.FlammableLightSource;
import com.marklynch.objects.tools.Jar;
import com.marklynch.objects.tools.Knife;
import com.marklynch.objects.tools.Lantern;
import com.marklynch.objects.tools.Pickaxe;
import com.marklynch.objects.tools.Shovel;
import com.marklynch.objects.tools.Tool;
import com.marklynch.utils.CopyOnWriteArrayList;

public class Save {

	// When you decide to save
	// 1. turn on pause mode (if not already) - Show spinner w/ "Ending Turn"
	// 2. end the turn - Show spinner w/ "Ending Turn"
	// 3. end all animations - Show spinner w/ "Ending Turn"
	// 3. run save

	// Keep this in order they should be loaded.
	public static Class<?>[] classesToSave = new Class[] {

			// Squares
			// Square.class,
			// Faction
			// Faction.class,

			// LVL 4 GameObject subclass in Actor package
			TinyNeutralWildAnimal.class, HerbivoreWildAnimal.class, Fish.class, CarnivoreNeutralWildAnimal.class,
			AggressiveWildAnimal.class,

			// LVL 3 GameObject subclass
			Seesaw.class,
			// SeesawPart.class,

			// LVL 3 GameObject subclass in Tool
			Shovel.class, Pickaxe.class, Knife.class, FlammableLightSource.class, FishingRod.class, Jar.class,
			Bell.class, Axe.class,

			// LVL 3 GameObject subclass in Actor package
			Player.class, WildAnimal.class, Trader.class, Thief.class, Pig.class, Guard.class, Doctor.class,

			// LVL 2 GameObject subclass in Tool
			Lantern.class, Fireplace.class,

			// LVL 2 GameObject subclass
			Door.class, Furnace.class, HidingPlace.class, Landmine.class, Spikes.class, Matches.class,
			PressurePlate.class, PressurePlateRequiringSpecificItem.class, RemoteDoor.class, SmallHidingPlace.class,
			Storage.class, Vein.class, WaterBody.class,

			// LVL 2 GameObject subclass in Weapon package
			BodyArmor.class, Helmet.class, LegArmor.class,

			// LVL 2 GameObject subclass in Tool
			Tool.class,

			// LVL 2 GameObject subclass in Actor package
			Human.class, Monster.class, Animal.class,

			// LVL 1 GameObject subclass
			Bed.class, Carcass.class, Corpse.class, Food.class, GameObjectExploder.class, Gold.class, Inspectable.class,
			GameObject.class, Liquid.class, MapMarker.class, MeatChunk.class, MineCart.class, Mirror.class,
			Openable.class, Orb.class, Portal.class, ConveyerBelt.class, Rail.class, Roof.class, Searchable.class,
			Stampable.class, Stump.class, Switch.class, Tree.class, VoidHole.class, Wall.class, WallSupport.class,
			WantedPoster.class, WaterSource.class, Window.class,

			// LVL 1 GameObject subclass in Weapon package
			Weapon.class, Armor.class,

			// LVL 1 GameObject subclass in Actor package
			Actor.class,

			// GameObject itself THE SUPERCLASS
			GameObject.class,

			// GameObject interfaces
			// Consumable.class, DamageDealer.class, SwitchListener.class,
			// UpdatesWhenSquareContentsChange.class
	};

	public static ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Field>> fieldsForEachClass = new ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Field>>();
	public static Gson saveSerializerGson;
	static CopyOnWriteArrayList<PreparedStatement> preparedStatements = new CopyOnWriteArrayList<PreparedStatement>(PreparedStatement.class);
	static Connection conn;
	static long saveStartTime;
	static long saveEndTime1;
	static long saveEndTime2;
	public static String dbConnString;

	public static void save() {

		if (saveSerializerGson == null)
			saveSerializerGson = SaveSerializationCreator.createSaveSerializerGson();

		saveStartTime = System.currentTimeMillis();
		dbConnString = "jdbc:sqlite:test" + System.currentTimeMillis() + ".db";

		try {

			// Create fields list for each class
			for (Class<?> classToSave : Save.classesToSave) {
				fieldsForEachClass.put(classToSave, Save.getFields(classToSave));
			}

			conn = DriverManager.getConnection(dbConnString);

			createPreparedStatementForSquareInserts();
			createPreparedStatementForFactionInserts();

			// Create table for each class
			for (Class<?> classToSave : classesToSave) {
				createTable(classToSave);
			}

			// Create the insert statements for each class
			for (Class<?> classToSave : classesToSave) {

				PreparedStatement p = createPreparedStatementForInserts(classToSave);
				if (p != null)
					preparedStatements.add(p);
			}

			new DiskWritingThread().start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		saveEndTime1 = System.currentTimeMillis();
		// System.out.println("Non-disk save time = " + (saveEndTime1 - saveStartTime));

	}

	public static void createPreparedStatementForSquareInserts() {

		fieldsForEachClass.put(Square.class, Save.getFields(Square.class));
		createTable(Square.class);
		String insertQueryTemplate = "INSERT INTO Square VALUES (?,?,?,?)";
		// id
		// inventory
		// floorImageTexture
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(insertQueryTemplate);
			for (Square square : Level.squaresToSave) {
				preparedStatement.setLong(1, square.id);
				preparedStatement.setString(2, saveSerializerGson.toJson(square.inventory));
				preparedStatement.setBoolean(3, square.seenByPlayer);
				preparedStatement.setString(4, square.getFloorImageTexture().path);
				preparedStatement.addBatch();
			}
			preparedStatements.add(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createPreparedStatementForFactionInserts() {

		fieldsForEachClass.put(Faction.class, Save.getFields(Faction.class));
		createTable(Faction.class);
		String insertQueryTemplate = "INSERT INTO Faction VALUES (?,?,?,?,?)";

		// public long id;
		// public String name;
		// public ConcurrentHashMap<Faction, Integer> relationships = new ConcurrentHashMap<Faction,
		// Integer>();
		// public CopyOnWriteArrayList<Actor> actors = new CopyOnWriteArrayList<Actor>();
		// public Texture imageTexture = null;

		try {
			PreparedStatement preparedStatement = conn.prepareStatement(insertQueryTemplate);
			for (Faction faction : Level.factions) {
				preparedStatement.setLong(1, faction.id);
				preparedStatement.setString(2, faction.name);
				preparedStatement.setString(3, saveSerializerGson.toJson(faction.relationships));
				preparedStatement.setString(4, saveSerializerGson.toJson(faction.actors));
				preparedStatement.setString(5, faction.imageTexture.path);
				preparedStatement.addBatch();
			}
			preparedStatements.add(preparedStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static class DiskWritingThread extends Thread {
		@Override
		public void run() {

			try {

				// insert for each class
				conn.setAutoCommit(false);
				for (PreparedStatement preparedStatement : preparedStatements) {
					preparedStatement.executeBatch();
				}
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			saveEndTime2 = System.currentTimeMillis();
			// System.out.println("Disk save time = " + (saveEndTime2 - saveEndTime1));
			// System.out.println("Total save time = " + (saveEndTime2 - saveStartTime));
		}
	}

	public static PreparedStatement createPreparedStatementForInserts(Class clazz) {
		CopyOnWriteArrayList<Field> fields = null;
		String insertQueryTemplate = null;
		Object object1 = null;
		Field field1 = null;

		// clazz is FlammableLightSource

		try {

			fields = fieldsForEachClass.get(clazz);

			if (fields.isEmpty())
				return null;

			// Make create table query and insert query template
			insertQueryTemplate = "INSERT INTO " + clazz.getSimpleName() + " VALUES (";
			for (Field field : fields) {
				insertQueryTemplate += "?";
				if (fields.get(fields.size() - 1) != field) {
					insertQueryTemplate += ",";
				}
			}
			insertQueryTemplate += ");";

			// Actually do the big ol' insert

			PreparedStatement preparedStatement = conn.prepareStatement(insertQueryTemplate);

			for (Object object : (CopyOnWriteArrayList<?>) clazz.getField("instances").get(null)) {// GameObject.instances

				object1 = object;
				int count = 1;
				for (Field field : fields) {
					field1 = field;

					Object value = field.get(object); // THIS is the crashing line

					if (value instanceof Boolean) {
						preparedStatement.setBoolean(count, (Boolean) value);
					} else if (value instanceof Long) {
						preparedStatement.setLong(count, (Long) value);
					} else if (value instanceof Integer) {
						preparedStatement.setInt(count, (Integer) value);
					} else if (value instanceof String) {
						preparedStatement.setString(count, (String) value);
					} else if (value instanceof Float) {
						preparedStatement.setFloat(count, (Float) value);
					} else if (value instanceof Double) {
						preparedStatement.setDouble(count, (Double) value);
					} else {
						preparedStatement.setString(count, saveSerializerGson.toJson(value));
					}

					count++;
				}

				preparedStatement.addBatch();
			}

			return preparedStatement;

		} catch (Exception e) {
			System.err.println("=======================");
			System.err.println("saveGameObjects() error");
			System.err.println("clazz = " + clazz);
			System.err.println("fields = " + fields);
			System.err.println("field = " + field1);
			System.err.println("object = " + object1);
			System.err.println("insertQueryTemplate = " + insertQueryTemplate);
			e.printStackTrace();
			System.err.println("=======================");
		}

		return null;

	}

	// public static String getGameObjectArrayStringForInsertion(GameObject[] array)
	// {
	//
	// String result = "[";
	// for (GameObject gameObject : array) {
	// result += gameObject.id;
	// if (array[array.length - 1] != gameObject) {
	// result += ",";
	// }
	// }
	// result += "]";
	// return result;
	// }
	//
	// public static String getSquareArrayStringForInsertion(Square[] array) {
	//
	// String result = "[";
	// for (Square square : array) {
	// result += square.id;
	// if (array[array.length - 1] != square) {
	// result += ",";
	// }
	// }
	// result += "]";
	// return result;
	// }
	//
	// public static String
	// getSwitchListenerArrayStringForInsertion(SwitchListener[] array) {
	//
	// String result = "[";
	// for (SwitchListener switchListener : array) {
	// result += switchListener.getId();
	// if (array[array.length - 1] != switchListener) {
	// result += ",";
	// }
	// }
	// result += "]";
	// return result;
	// }

	static void createTable(Class<?> clazz) {

		CopyOnWriteArrayList<Field> fields = null;
		Statement statement = null;
		String createTableQuery = null;
		try {

			fields = fieldsForEachClass.get(clazz);
			if (fields.isEmpty())
				return;

			if (clazz == Square.class) {
				System.out.println("fields 2 = " + fields);
			}

			statement = conn.createStatement();

			// Make create table query and insert query template
			createTableQuery = "CREATE TABLE " + clazz.getSimpleName() + " (";
			for (Field field : fields) {
				createTableQuery += field.getName();
				if (fields.get(fields.size() - 1) != field) {
					createTableQuery += ",";
				}
			}
			createTableQuery += ");";
			if (clazz == Square.class) {
				System.out.println("fcreateTableQuery = " + createTableQuery);
			}

			statement.executeUpdate(createTableQuery);
		} catch (Exception e) {
			System.err.println("=======================");
			System.err.println("saveGameObjects() error");
			System.err.println("clazz = " + clazz);
			System.err.println("fields = " + fields);
			e.printStackTrace();
			System.err.println("=======================");

		}

	}

	static CopyOnWriteArrayList<Field> getFields(Class<?> clazz) {

		try {
			CopyOnWriteArrayList<Field> fields = new CopyOnWriteArrayList<Field>(Field.class);
			fields.addAll(Arrays.asList(clazz.getFields()));

			CopyOnWriteArrayList<Field> declaredFields = new CopyOnWriteArrayList<Field>(Field.class);
			declaredFields.addAll(Arrays.asList(clazz.getDeclaredFields()));

			// Remove transient and static fields, don't want to save them
			for (Field field : (CopyOnWriteArrayList<Field>) fields) {
				if (
				//
				Modifier.isTransient(field.getModifiers())
						//
						|| Modifier.isStatic(field.getModifiers())
						//
						|| (!declaredFields.contains(field) && !field.getName().equals("id")))
				//
				{
					fields.remove(field);
				}
			}

			if (clazz == Square.class) {
				System.out.println("fields = " + fields);
				System.out.println("declaredFields = " + declaredFields);
			}

			return fields;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}