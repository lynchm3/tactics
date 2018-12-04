package com.marklynch.data;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.marklynch.ai.routines.AIRoutine;
import com.marklynch.ai.routines.AIRoutine.STATE;
import com.marklynch.ai.routines.AIRoutineForBlind;
import com.marklynch.ai.routines.AIRoutineForGuard;
import com.marklynch.ai.routines.AIRoutineForHerbivoreWildAnimal;
import com.marklynch.ai.routines.AIRoutineForMort;
import com.marklynch.ai.routines.AIRoutineForThief;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.constructs.GroupOfActors;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.inventory.Inventory;
import com.marklynch.level.constructs.inventory.SquareInventory;
import com.marklynch.level.constructs.power.Power;
import com.marklynch.level.squares.Node;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.HOBBY;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.MeatChunk;
import com.marklynch.objects.inanimateobjects.Seesaw;
import com.marklynch.objects.utils.SwitchListener;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.Texture;

public class LoadDeserializerCreatore {

	public static Gson createLoadDeserializerGson() {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Texture.class, deserializerForTexture);
		gsonBuilder.registerTypeAdapter(Faction.class, deserializerForIdable);
		gsonBuilder.registerTypeAdapter(GroupOfActors.class, deserializerForIdable);
		gsonBuilder.registerTypeAdapter(SwitchListener.class, deserializerForIdable);
		gsonBuilder.registerTypeAdapter(Square.class, deserializerForIdable);
		gsonBuilder.registerTypeAdapter(Node.class, deserializerForIdable);
		gsonBuilder.registerTypeAdapter(Area.class, deserializerForIdable);
		// gsonBuilder.registerTypeAdapter(Crime.class, deserializerForCrime);
		// gsonBuilder.registerTypeAdapter(Investigation.class,
		// deserializerForInvestigation);
		// gsonBuilder.registerTypeAdapter(Sound.class, deserializerForSound);
		// gsonBuilder.registerTypeAdapter(AILine.class, deserializerForAILine);
		// gsonBuilder.registerTypeAdapter(Enhancement.class,
		// deserializerForEnhancement);
		gsonBuilder.registerTypeAdapter(Inventory.class, deserializerForInventory);
		gsonBuilder.registerTypeAdapter(SquareInventory.class, deserializerForInventory);
		// gsonBuilder.registerTypeAdapter(SWITCH_TYPE.class,
		// deserializerForSWITCH_TYPE);
		// gsonBuilder.registerTypeAdapter(Color.class, deserializerForColor);
		// gsonBuilder.registerTypeAdapter(StructureSection.class,
		// deserializerForIdable);
		// gsonBuilder.registerTypeAdapter(Direction.class, deserializerForDirection);
		// gsonBuilder.registerTypeAdapter(Stat.class, deserializerForStat);

		// Add deserializers for all GamObjects, Effects and aiRoutines
		ArrayList<Class<?>> gameObjectClasses = new ArrayList<Class<?>>();
		gameObjectClasses.addAll(PackageUtils.getClasses("com.marklynch.objects.actors"));
		gameObjectClasses.addAll(PackageUtils.getClasses("com.marklynch.objects.inanimateobjects"));
		gameObjectClasses.addAll(PackageUtils.getClasses("com.marklynch.objects.tools"));
		gameObjectClasses.addAll(PackageUtils.getClasses("com.marklynch.objects.armor"));
		for (Class<?> clazz : gameObjectClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForIdable);
		}
		gsonBuilder.registerTypeAdapter(Seesaw.SeesawPart.class, deserializerForIdable);

		// Effects
		ArrayList<Class<?>> effectClasses = PackageUtils.getClasses("com.marklynch.level.constructs.effect");
		for (Class<?> clazz : effectClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForEffect);
		}

		// AI Routines
		ArrayList<Class<?>> aiRoutineClasses = PackageUtils.getClasses("com.marklynch.ai.routines");
		for (Class<?> clazz : aiRoutineClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForAIRoutine);
		}

		// // Power
		ArrayList<Class<?>> powerClasses = PackageUtils.getClasses("com.marklynch.level.constructs.power");
		for (Class<?> clazz : powerClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForPower);
		}

		// Quests
		ArrayList<Class<?>> questClasses = PackageUtils.getClasses("com.marklynch.level.quest");
		for (Class<?> clazz : questClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForIdable);
		}

		// Structure Room
		ArrayList<Class<?>> structureRoomClasses = PackageUtils
				.getClasses("com.marklynch.level.constructs.bounds.structure.structureroom");
		for (Class<?> clazz : structureRoomClasses) {
			gsonBuilder.registerTypeAdapter(clazz, deserializerForIdable);
		}

		Gson gson = gsonBuilder.create();
		return gson;

	}

	// change serialization for specific types
	public static JsonDeserializer<Object> deserializerForIdable = new JsonDeserializer<Object>() {
		@Override
		public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return Level.ids.get(json.getAsLong());
		}
	};

	// Texture
	public static JsonDeserializer<Texture> deserializerForTexture = new JsonDeserializer<Texture>() {
		@Override
		public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return ResourceUtils.getGlobalImage(json.getAsString(), true);
		}
	};

	// Effect
	public static JsonDeserializer<Effect> deserializerForEffect = new JsonDeserializer<Effect>() {
		@Override
		public Effect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			Effect effect = null;

			JsonObject jsonObject = json.getAsJsonObject();
			String classString = jsonObject.get("class").getAsString();
			Class<?> clazz;
			try {
				clazz = Class.forName(classString);
				effect = (Effect) clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			effect.effectName = jsonObject.get("effectName").getAsString();

			JsonElement sourceJson = jsonObject.get("source");
			if (sourceJson != null)
				effect.source = (GameObject) Level.ids.get(sourceJson.getAsLong());

			JsonElement targetJson = jsonObject.get("target");
			if (targetJson != null)
				effect.target = (GameObject) Level.ids.get(targetJson.getAsLong());

			effect.totalTurns = jsonObject.get("totalTurns").getAsInt();
			effect.turnsRemaining = jsonObject.get("turnsRemaining").getAsInt();

			JsonElement imageTextureJson = jsonObject.get("imageTexture");
			if (imageTextureJson != null)
				effect.imageTexture = ResourceUtils.getGlobalImage(imageTextureJson.getAsString(), true);

			return effect;
		}
	};

	// AIRoutine
	public static JsonDeserializer<AIRoutine> deserializerForAIRoutine = new JsonDeserializer<AIRoutine>() {
		@Override
		public AIRoutine deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			AIRoutine aiRoutine = null;

			JsonObject jsonObject = json.getAsJsonObject();
			String classString = jsonObject.get("class").getAsString();
			Class<?> clazz;
			try {
				clazz = Class.forName(classString);
				aiRoutine = (AIRoutine) clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			JsonElement actorJson = jsonObject.get("actor");
			if (actorJson != null)
				aiRoutine.actor = (Actor) Level.ids.get(actorJson.getAsLong());

			JsonElement targetJson = jsonObject.get("target");
			if (targetJson != null)
				aiRoutine.target = (GameObject) Level.ids.get(targetJson.getAsLong());

			JsonElement targetSquareJson = jsonObject.get("targetSquare");
			if (targetSquareJson != null)
				aiRoutine.targetSquare = (Square) Level.ids.get(targetSquareJson.getAsLong());

			aiRoutine.searchCooldown = jsonObject.get("searchCooldown").getAsInt();

			JsonElement searchCooldownActorJson = jsonObject.get("searchCooldownActor");
			if (searchCooldownActorJson != null)
				aiRoutine.actor = (Actor) Level.ids.get(searchCooldownActorJson.getAsLong());

			aiRoutine.escapeCooldown = jsonObject.get("escapeCooldown").getAsInt();

			JsonElement escapeCooldownAttackerJson = jsonObject.get("escapeCooldownAttacker");
			if (escapeCooldownAttackerJson != null)
				aiRoutine.actor = (Actor) Level.ids.get(escapeCooldownAttackerJson.getAsLong());

			aiRoutine.wokenUpCountdown = jsonObject.get("wokenUpCountdown").getAsInt();

			JsonElement stateJson = jsonObject.get("state");
			aiRoutine.state = Load.loadDeserializerGson.fromJson(stateJson, STATE.class);

			aiRoutine.keepInBounds = jsonObject.get("keepInBounds").getAsBoolean();

			JsonElement areaBoundsJson = jsonObject.get("areaBounds");
			if (areaBoundsJson != null)
				aiRoutine.areaBounds = Load.loadDeserializerGson.fromJson(areaBoundsJson,
						aiRoutine.areaBounds.getClass());

			JsonElement sectionBoundsJson = jsonObject.get("sectionBounds");
			if (sectionBoundsJson != null)
				aiRoutine.sectionBounds = Load.loadDeserializerGson.fromJson(sectionBoundsJson,
						aiRoutine.sectionBounds.getClass());

			JsonElement roomBoundsJson = jsonObject.get("roomBounds");
			if (roomBoundsJson != null)
				aiRoutine.roomBounds = Load.loadDeserializerGson.fromJson(roomBoundsJson,
						aiRoutine.roomBounds.getClass());

			JsonElement squareBoundsJson = jsonObject.get("squareBounds");
			if (squareBoundsJson != null)
				aiRoutine.squareBounds = Load.loadDeserializerGson.fromJson(squareBoundsJson,
						aiRoutine.squareBounds.getClass());

			JsonElement currentHobbyJson = jsonObject.get("currentHobby");
			aiRoutine.currentHobby = Load.loadDeserializerGson.fromJson(currentHobbyJson, HOBBY.class);

			JsonElement actorToKeepTrackOfJson = jsonObject.get("actorToKeepTrackOf");
			if (actorToKeepTrackOfJson != null)
				aiRoutine.actorToKeepTrackOf = (Actor) Level.ids.get(actorToKeepTrackOfJson.getAsLong());

			aiRoutine.lastLocationSeenActorToKeepTrackOf = Load.loadDeserializerGson.fromJson(
					jsonObject.get("lastLocationSeenActorToKeepTrackOf"),
					aiRoutine.lastLocationSeenActorToKeepTrackOf.getClass());

			aiRoutine.sleepCounter = jsonObject.get("sleepCounter").getAsInt();

			aiRoutine.ignoreList = Load.loadDeserializerGson.fromJson(jsonObject.get("ignoreList"),
					aiRoutine.ignoreList.getClass());

			if (aiRoutine instanceof AIRoutineForBlind) {

				AIRoutineForBlind aiRoutineForBlind = (AIRoutineForBlind) aiRoutine;
				JsonElement meatChunkJson = jsonObject.get("meatChunk");
				if (meatChunkJson != null)
					aiRoutineForBlind.meatChunk = (MeatChunk) Level.ids.get(meatChunkJson.getAsLong());

				JsonElement originalMeatChunkSquareJson = jsonObject.get("originalMeatChunkSquare");
				if (originalMeatChunkSquareJson != null)
					aiRoutineForBlind.originalMeatChunkSquare = (Square) Level.ids
							.get(originalMeatChunkSquareJson.getAsLong());

				aiRoutineForBlind.hangry = jsonObject.get("hangry").getAsBoolean();
				aiRoutineForBlind.timeSinceEating = jsonObject.get("timeSinceEating").getAsInt();
				aiRoutineForBlind.failedToGetPathToBellCount = jsonObject.get("failedToGetPathToBellCount").getAsInt();
				aiRoutineForBlind.failedToGetPathToFoodCount = jsonObject.get("failedToGetPathToFoodCount").getAsInt();

				JsonElement bellSoundJson = jsonObject.get("bellSound");
				aiRoutineForBlind.bellSound = Load.loadDeserializerGson.fromJson(bellSoundJson,
						aiRoutineForBlind.bellSound.getClass());

			} else if (aiRoutine instanceof AIRoutineForGuard) {
				((AIRoutineForGuard) aiRoutine).patrolIndex = jsonObject.get("patrolIndex").getAsInt();
			} else if (aiRoutine instanceof AIRoutineForHerbivoreWildAnimal) {
				((AIRoutineForHerbivoreWildAnimal) aiRoutine).hidingCount = jsonObject.get("hidingCount").getAsInt();
			} else if (aiRoutine instanceof AIRoutineForMort) {
				AIRoutineForMort aiRoutineForMort = (AIRoutineForMort) aiRoutine;
				aiRoutineForMort.rangBellAsLastResort = jsonObject.get("rangBellAsLastResort").getAsBoolean();
				aiRoutineForMort.retreatedToRoom = jsonObject.get("retreatedToRoom").getAsBoolean();
				aiRoutineForMort.feedingDemoState = Load.loadDeserializerGson.fromJson(jsonObject.get("ignoreList"),
						aiRoutineForMort.feedingDemoState.getClass());
			} else if (aiRoutine instanceof AIRoutineForThief) {
				((AIRoutineForThief) aiRoutine).theftCooldown = jsonObject.get("theftCooldown").getAsInt();
			}

			return aiRoutine;
		}
	};

	// Power
	public static JsonDeserializer<Power> deserializerForPower = new JsonDeserializer<Power>() {
		@Override
		public Power deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			Power power = null;
			String classString = json.getAsString();
			Class<?> clazz;

			try {
				clazz = Class.forName(classString);
				power = (Power) clazz.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return power;
		}
	};

	// Inventory
	public static JsonDeserializer<Inventory> deserializerForInventory = new JsonDeserializer<Inventory>() {
		@Override
		public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			Inventory inventory;
			if (typeOfT == SquareInventory.class) {
				inventory = new SquareInventory();
			} else {
				inventory = new Inventory();
			}
			ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

			JsonArray jsonArray = json.getAsJsonArray();
			for (int i = 0; i < jsonArray.size(); i++) {
				Long id = jsonArray.get(i).getAsLong();
				gameObjects.add((GameObject) Level.ids.get(id));
			}

			inventory.gameObjects = gameObjects;

			return inventory;
		}
	};
}
