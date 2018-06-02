package com.marklynch.objects.templates;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.effect.EffectPoison;
import com.marklynch.level.constructs.effect.EffectWet;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Liquid;

public class TemplatesLiquids {

	public TemplatesLiquids() {

		Templates.WATER = new Liquid();
		Templates.WATER.name = "Water";
		Templates.WATER.imageTexturePath = "effect_wet.png";
		Templates.WATER.imageTexture = getGlobalImage(Templates.WATER.imageTexturePath, true);
		Templates.WATER.totalHealth = Templates.WATER.remainingHealth = 10;
		Templates.WATER.widthRatio = 0.5f;
		Templates.WATER.heightRatio = 0.5f;
		Templates.WATER.drawOffsetRatioX = 0f;
		Templates.WATER.drawOffsetRatioY = 0f;
		Templates.WATER.soundWhenHit = 1f;
		Templates.WATER.soundWhenHitting = 1f;
		Templates.WATER.soundDampening = 1f;
		Templates.WATER.stackable = false;
		Templates.WATER.weight = 5f;
		Templates.WATER.value = 11;
		Templates.WATER.anchorX = 0;
		Templates.WATER.anchorY = 0;
		Templates.WATER.templateId = GameObject.generateNewTemplateId();
		Templates.WATER.volume = 1;
		Templates.WATER.touchEffects = new Effect[] { new EffectWet(5) };
		Templates.WATER.drinkEffects = new Effect[] {};
		Templates.WATER.highLevelStats.put(HIGH_LEVEL_STATS.WATER_DAMAGE, new Stat(1));

		Templates.POISON = new Liquid();
		Templates.POISON.name = "Poison";
		Templates.POISON.imageTexturePath = "effect_poison.png";
		Templates.POISON.imageTexture = getGlobalImage(Templates.POISON.imageTexturePath, true);
		Templates.POISON.totalHealth = Templates.POISON.remainingHealth = 10;
		Templates.POISON.widthRatio = 0.5f;
		Templates.POISON.heightRatio = 0.5f;
		Templates.POISON.drawOffsetRatioX = 0f;
		Templates.POISON.drawOffsetRatioY = 0f;
		Templates.POISON.soundWhenHit = 1f;
		Templates.POISON.soundWhenHitting = 1f;
		Templates.POISON.soundDampening = 1f;
		Templates.POISON.stackable = false;
		Templates.POISON.weight = 6f;
		Templates.POISON.value = 16;
		Templates.POISON.anchorX = 0;
		Templates.POISON.anchorY = 0;
		Templates.POISON.templateId = GameObject.generateNewTemplateId();
		Templates.POISON.volume = 1;
		Templates.POISON.touchEffects = new Effect[] { new EffectPoison(5) };
		Templates.POISON.drinkEffects = new Effect[] { new EffectPoison(10) };
		Templates.POISON.highLevelStats.put(HIGH_LEVEL_STATS.POISON_DAMAGE, new Stat(3));
	}

}
