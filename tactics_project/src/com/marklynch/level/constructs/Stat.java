package com.marklynch.level.constructs;

import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.annotations.Expose;
import com.marklynch.utils.CopyOnWriteArrayList;

public class Stat {

	// @Expose(serialize = false)
	public static enum HIGH_LEVEL_STATS {
		STRENGTH, DEXTERITY, ENDURANCE, INTELLIGENCE, FRIENDLY_FIRE,
		//
		SLASH_DAMAGE, BLUNT_DAMAGE, PIERCE_DAMAGE, FIRE_DAMAGE, WATER_DAMAGE, ELECTRICAL_DAMAGE, POISON_DAMAGE, BLEED_DAMAGE, HEALING,
		//
		SLASH_RES, BLUNT_RES, PIERCE_RES, FIRE_RES, WATER_RES, ELECTRICAL_RES, POISON_RES, BLEED_RES, HEALING_RES
	};

	@Expose(serialize = false)
	public final static CopyOnWriteArrayList<HIGH_LEVEL_STATS> GENERAL_STATS = new CopyOnWriteArrayList<HIGH_LEVEL_STATS>(
			HIGH_LEVEL_STATS.class);

	@Expose(serialize = false)
	public final static CopyOnWriteArrayList<HIGH_LEVEL_STATS> OFFENSIVE_STATS = new CopyOnWriteArrayList<HIGH_LEVEL_STATS>(
			HIGH_LEVEL_STATS.class);

	@Expose(serialize = false)
	public final static CopyOnWriteArrayList<HIGH_LEVEL_STATS> DEFENSIVE_STATS = new CopyOnWriteArrayList<HIGH_LEVEL_STATS>(
			HIGH_LEVEL_STATS.class);

	@Expose(serialize = false)
	public static ConcurrentHashMap<HIGH_LEVEL_STATS, HIGH_LEVEL_STATS> offensiveStatToDefensiveStatMap;

	// public static enum HIGH_LEVEL_STATS {
	//
	// };

	// public static enum DEFENSIVE_STATS {
	// SLASH_DAMAGE, BLUNT_DAMAGE, PIERCE_DAMAGE, FIRE_DAMAGE, WATER_DAMAGE,
	// ELECTRICAL_DAMAGE, POISON_DAMAGE, BLEED_DAMAGE
	// };

	public int value;
	public HIGH_LEVEL_STATS type;

	// public Stat(int value) {
	// super();
	//
	// this.value = value;
	// }

	public Stat(HIGH_LEVEL_STATS type, int value) {
		super();
		this.type = type;
		this.value = value;
	}

	public static void init() {

		GENERAL_STATS.add(HIGH_LEVEL_STATS.STRENGTH);
		GENERAL_STATS.add(HIGH_LEVEL_STATS.DEXTERITY);
		GENERAL_STATS.add(HIGH_LEVEL_STATS.ENDURANCE);
		GENERAL_STATS.add(HIGH_LEVEL_STATS.INTELLIGENCE);
		GENERAL_STATS.add(HIGH_LEVEL_STATS.FRIENDLY_FIRE);

		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.SLASH_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.BLUNT_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.PIERCE_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.FIRE_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.WATER_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.POISON_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.BLEED_DAMAGE);
		OFFENSIVE_STATS.add(HIGH_LEVEL_STATS.HEALING);

		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.SLASH_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.BLUNT_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.PIERCE_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.FIRE_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.WATER_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.ELECTRICAL_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.POISON_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.BLEED_RES);
		DEFENSIVE_STATS.add(HIGH_LEVEL_STATS.HEALING_RES);

		offensiveStatToDefensiveStatMap = new ConcurrentHashMap<HIGH_LEVEL_STATS, HIGH_LEVEL_STATS>();
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.SLASH_DAMAGE, HIGH_LEVEL_STATS.SLASH_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, HIGH_LEVEL_STATS.BLUNT_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.PIERCE_DAMAGE, HIGH_LEVEL_STATS.PIERCE_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.FIRE_DAMAGE, HIGH_LEVEL_STATS.FIRE_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.WATER_DAMAGE, HIGH_LEVEL_STATS.WATER_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE, HIGH_LEVEL_STATS.ELECTRICAL_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.POISON_DAMAGE, HIGH_LEVEL_STATS.POISON_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.BLEED_DAMAGE, HIGH_LEVEL_STATS.BLEED_RES);
		offensiveStatToDefensiveStatMap.put(HIGH_LEVEL_STATS.HEALING, HIGH_LEVEL_STATS.HEALING_RES);

	}

	public Stat makeCopy() {
		return new Stat(type, value);
	}

	@Override
	public String toString() {
		return "Stat [value=" + value + ", type=" + type + "]";
	}

	// public static String
	// getStringForSavingHIGH_LEVEL_STATS(ConcurrentHashMap<HIGH_LEVEL_STATS, Stat>
	// highLevelStats) {
	// String result = "";
	// for (HIGH_LEVEL_STATS highLevelStat : Stat.HIGH_LEVEL_STATS.values()) {
	// result += highLevelStats.get(highLevelStat).value;
	// if (Stat.HIGH_LEVEL_STATS.values()[Stat.HIGH_LEVEL_STATS.values().length - 1]
	// != highLevelStat) {
	// result += ",";
	// }
	// }
	// return result;
	// }

}
