package com.marklynch.objects.templates;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.objects.actors.Human;
import com.marklynch.objects.armor.BodyArmor;
import com.marklynch.objects.armor.Helmet;
import com.marklynch.objects.armor.LegArmor;
import com.marklynch.objects.inanimateobjects.GameObject;

public class TemplatesArmor {

	// \t\tpublic\sstatic\sfinal\s[a-zA-Z]+\s

	public TemplatesArmor() {

		// ARMOUR
		// Helmets
		Templates.HARD_HAT = new Helmet();
		Templates.HARD_HAT.name = "Hard Hat";
		Templates.HARD_HAT.setImageAndExtrapolateSize("hard_hat.png");
		Templates.HARD_HAT.totalHealth = Templates.HARD_HAT.remainingHealth = 50;
		Templates.HARD_HAT.weight = 7;
		Templates.HARD_HAT.value = 52;
		Templates.HARD_HAT.anchorX = 64;
		Templates.HARD_HAT.anchorY = 25;
		Templates.HARD_HAT.highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(HIGH_LEVEL_STATS.SLASH_RES, 10));
		Templates.HARD_HAT.templateId = GameObject.generateNewTemplateId();

		Templates.PINK_HARD_HAT = new Helmet();
		Templates.PINK_HARD_HAT.name = "Hard Hat";
		Templates.PINK_HARD_HAT.setImageAndExtrapolateSize("pink_hard_hat.png");
		Templates.PINK_HARD_HAT.totalHealth = Templates.PINK_HARD_HAT.remainingHealth = 50;
		Templates.PINK_HARD_HAT.weight = 7;
		Templates.PINK_HARD_HAT.value = 82;
		Templates.PINK_HARD_HAT.anchorX = 64;
		Templates.PINK_HARD_HAT.anchorY = 25;
		Templates.PINK_HARD_HAT.templateId = GameObject.generateNewTemplateId();

		Templates.HUNTING_CAP = new Helmet();
		Templates.HUNTING_CAP.name = "Hunting Cap";
		Templates.HUNTING_CAP.setImageAndExtrapolateSize("hunting_cap.png");
		Templates.HUNTING_CAP.totalHealth = Templates.HUNTING_CAP.remainingHealth = 50;
		Templates.HUNTING_CAP.weight = 5;
		Templates.HUNTING_CAP.value = 21;
		Templates.HUNTING_CAP.anchorX = 64;
		Templates.HUNTING_CAP.anchorY = 25;
		Templates.HUNTING_CAP.templateId = GameObject.generateNewTemplateId();

		Templates.COWBOY_HAT = new Helmet();
		Templates.COWBOY_HAT.name = "Cowboy Hat";
		Templates.COWBOY_HAT.setImageAndExtrapolateSize("cowboy_hat.png");
		Templates.COWBOY_HAT.totalHealth = Templates.COWBOY_HAT.remainingHealth = 15;
		Templates.COWBOY_HAT.weight = 6f;
		Templates.COWBOY_HAT.value = 34;
		Templates.COWBOY_HAT.anchorX = 64;
		Templates.COWBOY_HAT.anchorY = 26;
		Templates.COWBOY_HAT.templateId = GameObject.generateNewTemplateId();

		Templates.HELMET_ANTLERS = new Helmet();
		Templates.HELMET_ANTLERS.name = "Deer Helmet";
		Templates.HELMET_ANTLERS.setImageAndExtrapolateSize("helmet_antlers.png");
		Templates.HELMET_ANTLERS.totalHealth = Templates.HELMET_ANTLERS.remainingHealth = 36;
		Templates.HELMET_ANTLERS.weight = 6f;
		Templates.HELMET_ANTLERS.value = 65;
		Templates.HELMET_ANTLERS.anchorX = 128;
		Templates.HELMET_ANTLERS.anchorY = 98;
		Templates.HELMET_ANTLERS.templateId = GameObject.generateNewTemplateId();

		// Body Armor
		Templates.JUMPER = new BodyArmor();
		Templates.JUMPER.name = "Jumper";
		Templates.JUMPER.imageTexture = getGlobalImage("jumper.png", true);
		Templates.JUMPER.armUpperTexture = getGlobalImage("jumper_arm_upper.png", true);
		Templates.JUMPER.armLowerTexture = getGlobalImage("jumper_arm_lower.png", true);
		Templates.JUMPER.totalHealth = Templates.JUMPER.remainingHealth = 20;
		Templates.JUMPER.widthRatio = 1f;
		Templates.JUMPER.heightRatio = 1.5f;
		Templates.JUMPER.soundWhenHit = 1f;
		Templates.JUMPER.soundWhenHitting = 1f;
		Templates.JUMPER.soundDampening = 1f;
		Templates.JUMPER.stackable = false;
		Templates.JUMPER.weight = 14f;
		Templates.JUMPER.value = 30;
		Templates.JUMPER.highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(HIGH_LEVEL_STATS.SLASH_RES, 1));
		Templates.JUMPER.templateId = GameObject.generateNewTemplateId();
		Human.bodyArmors.add(Templates.JUMPER.makeCopy(null, null));

		Templates.APRON = new BodyArmor();
		Templates.APRON.name = "Apron";
		Templates.APRON.imageTexture = getGlobalImage("apron.png", true);
		Templates.APRON.armUpperTexture = getGlobalImage("apron_arm_upper.png", true);
		Templates.APRON.armLowerTexture = getGlobalImage("apron_arm_lower.png", true);
		Templates.APRON.totalHealth = Templates.APRON.remainingHealth = 20;
		Templates.APRON.widthRatio = 1f;
		Templates.APRON.heightRatio = 1.5f;
		Templates.APRON.weight = 17f;
		Templates.APRON.value = 38;
		Templates.APRON.templateId = GameObject.generateNewTemplateId();
		Human.bodyArmors.add(Templates.APRON.makeCopy(null, null));

		Templates.ROBE = new BodyArmor();
		Templates.ROBE.name = "Robe";
		Templates.ROBE.imageTexture = getGlobalImage("robe.png", true);
		Templates.ROBE.armUpperTexture = getGlobalImage("robe_arm_upper.png", true);
		Templates.ROBE.armLowerTexture = getGlobalImage("robe_arm_lower.png", true);
		Templates.ROBE.backTexture = getGlobalImage("robe_back.png", true);
		Templates.ROBE.totalHealth = Templates.ROBE.remainingHealth = 26;
		Templates.ROBE.widthRatio = 1f;
		Templates.ROBE.heightRatio = 1.5f;
		Templates.ROBE.weight = 21f;
		Templates.ROBE.value = 46;
		Templates.ROBE.templateId = GameObject.generateNewTemplateId();

		Templates.LEATHERS = new BodyArmor();
		Templates.LEATHERS.name = "Leathers";
		Templates.LEATHERS.imageTexture = getGlobalImage("leathers.png", true);
		Templates.LEATHERS.armUpperTexture = getGlobalImage("leathers_arm_upper.png", true);
		Templates.LEATHERS.armLowerTexture = getGlobalImage("leathers_arm_lower.png", true);
		Templates.LEATHERS.totalHealth = Templates.LEATHERS.remainingHealth = 54;
		Templates.LEATHERS.widthRatio = 1f;
		Templates.LEATHERS.heightRatio = 1.5f;
		Templates.LEATHERS.weight = 46f;
		Templates.LEATHERS.value = 104;
		Templates.LEATHERS.templateId = GameObject.generateNewTemplateId();

		Templates.CHAINMAIL = new BodyArmor();
		Templates.CHAINMAIL.name = "Chainmail";
		Templates.CHAINMAIL.imageTexture = getGlobalImage("chainmail.png", true);
		Templates.CHAINMAIL.armUpperTexture = getGlobalImage("chainmail_arm_upper.png", true);
		Templates.CHAINMAIL.armLowerTexture = getGlobalImage("chainmail_arm_lower.png", true);
		Templates.CHAINMAIL.backTexture = getGlobalImage("chainmail_back.png", true);
		Templates.CHAINMAIL.totalHealth = Templates.CHAINMAIL.remainingHealth = 77;
		Templates.CHAINMAIL.widthRatio = 1f;
		Templates.CHAINMAIL.heightRatio = 1.5f;
		Templates.CHAINMAIL.weight = 75f;
		Templates.CHAINMAIL.value = 201;
		Templates.CHAINMAIL.templateId = GameObject.generateNewTemplateId();

		// Leg Armor

		Templates.PANTS = new LegArmor();
		Templates.PANTS.name = "Pants";
		Templates.PANTS.imageTexture = getGlobalImage("pants.png", true);
		Templates.PANTS.legLowerTexture = getGlobalImage("pants_leg_lower.png", true);
		Templates.PANTS.legUpperTexture = getGlobalImage("pants_leg_upper.png", true);
		Templates.PANTS.totalHealth = Templates.PANTS.remainingHealth = 20;
		Templates.PANTS.heightRatio = 1.5f;
		Templates.PANTS.weight = 12f;
		Templates.PANTS.value = 24;
		Templates.PANTS.highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(HIGH_LEVEL_STATS.SLASH_RES, 2));
		Templates.PANTS.templateId = GameObject.generateNewTemplateId();

		Templates.DUNGAREES = new LegArmor();
		Templates.DUNGAREES.name = "Dungarees";
		Templates.DUNGAREES.imageTexture = getGlobalImage("dungarees.png", true);
		Templates.DUNGAREES.legLowerTexture = getGlobalImage("dungarees_leg_lower.png", true);
		Templates.DUNGAREES.legUpperTexture = getGlobalImage("dungarees_leg_upper.png", true);
		Templates.DUNGAREES.frontTexture = getGlobalImage("dungarees_front.png", true);
		Templates.DUNGAREES.totalHealth = Templates.DUNGAREES.remainingHealth = 27;
		Templates.DUNGAREES.heightRatio = 1.5f;
		Templates.DUNGAREES.weight = 14f;
		Templates.DUNGAREES.value = 29;
		Templates.DUNGAREES.highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(HIGH_LEVEL_STATS.SLASH_RES, 10));
		Templates.DUNGAREES.templateId = GameObject.generateNewTemplateId();

		Templates.UNDIES = new LegArmor();
		Templates.UNDIES.name = "Undies";
		Templates.UNDIES.imageTexture = getGlobalImage("undies.png", true);
		Templates.UNDIES.totalHealth = Templates.UNDIES.remainingHealth = 27;
		Templates.UNDIES.heightRatio = 1.5f;
		Templates.UNDIES.weight = 14f;
		Templates.UNDIES.value = 29;
		Templates.UNDIES.templateId = GameObject.generateNewTemplateId();
	}

}
