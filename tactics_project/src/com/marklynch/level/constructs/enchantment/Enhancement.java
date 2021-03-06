package com.marklynch.level.constructs.enchantment;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.level.constructs.Stats;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.utils.Texture;

public class Enhancement {

	public Stats highLevelStats = new Stats();

	public String enhancementName = "Enhancement";
	public Texture imageTexture;
	public float minRange = 0;
	public float maxRange = 0;

	public Effect[] effect = {};
	public int templateId;

	public enum TYPE {
		WEAPON
	}

	public TYPE type;

	public Enhancement() {

		highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.FRIENDLY_FIRE, new Stat(HIGH_LEVEL_STATS.FRIENDLY_FIRE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.SLASH_DAMAGE, new Stat(HIGH_LEVEL_STATS.SLASH_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.PIERCE_DAMAGE, new Stat(HIGH_LEVEL_STATS.PIERCE_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.FIRE_DAMAGE, new Stat(HIGH_LEVEL_STATS.FIRE_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.WATER_DAMAGE, new Stat(HIGH_LEVEL_STATS.WATER_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE, new Stat(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.POISON_DAMAGE, new Stat(HIGH_LEVEL_STATS.POISON_DAMAGE,0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLEED_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLEED_DAMAGE, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.HEALING, new Stat(HIGH_LEVEL_STATS.HEALING, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(HIGH_LEVEL_STATS.SLASH_RES, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.PIERCE_RES, new Stat(HIGH_LEVEL_STATS.PIERCE_RES,0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_RES, new Stat(HIGH_LEVEL_STATS.BLUNT_RES, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.FIRE_RES, new Stat(HIGH_LEVEL_STATS.FIRE_RES, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.WATER_RES, new Stat(HIGH_LEVEL_STATS.WATER_RES,0));
		highLevelStats.put(HIGH_LEVEL_STATS.ELECTRICAL_RES, new Stat(HIGH_LEVEL_STATS.ELECTRICAL_RES, 0));
		highLevelStats.put(HIGH_LEVEL_STATS.POISON_RES, new Stat(HIGH_LEVEL_STATS.POISON_RES,0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLEED_RES, new Stat(HIGH_LEVEL_STATS.BLEED_RES,0));
		highLevelStats.put(HIGH_LEVEL_STATS.HEALING_RES, new Stat(HIGH_LEVEL_STATS.HEALING_RES, 0));
	}

	public Enhancement makeCopy() {

		Enhancement enhancement = new Enhancement();

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			enhancement.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			enhancement.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			enhancement.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		enhancement.minRange = minRange;
		enhancement.maxRange = maxRange;

		return enhancement;

	}

	public static void loadEnchantmentImages() {
		getGlobalImage("effect_bleed.png", false);
		getGlobalImage("effect_burn.png", false);
		getGlobalImage("effect_poison.png", false);
		getGlobalImage("effect_wet.png", false);
	}
}
