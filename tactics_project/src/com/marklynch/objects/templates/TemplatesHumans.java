package com.marklynch.objects.templates;

import com.marklynch.Game;
import com.marklynch.ai.routines.AIRoutineForDoctor;
import com.marklynch.ai.routines.AIRoutineForFisherman;
import com.marklynch.ai.routines.AIRoutineForFollower;
import com.marklynch.ai.routines.AIRoutineForGuard;
import com.marklynch.ai.routines.AIRoutineForHunter;
import com.marklynch.ai.routines.AIRoutineForKidnapper;
import com.marklynch.ai.routines.AIRoutineForLumberjack;
import com.marklynch.ai.routines.AIRoutineForMinecart;
import com.marklynch.ai.routines.AIRoutineForMiner;
import com.marklynch.ai.routines.AIRoutineForMort;
import com.marklynch.ai.routines.AIRoutineForThief;
import com.marklynch.ai.routines.AIRoutineForTrader;
import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.objects.actors.Doctor;
import com.marklynch.objects.actors.Follower;
import com.marklynch.objects.actors.Guard;
import com.marklynch.objects.actors.Human;
import com.marklynch.objects.actors.Kidnapper;
import com.marklynch.objects.actors.Mort;
import com.marklynch.objects.actors.Player;
import com.marklynch.objects.actors.Thief;
import com.marklynch.objects.actors.Trader;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.utils.ResourceUtils;

public class TemplatesHumans {

	// \t\tpublic\sstatic\sfinal\s[a-zA-Z]+\s

	public TemplatesHumans() {

		// Player
		Templates.PLAYER = new Player();
		Templates.PLAYER.title = "Fighter";
		Templates.PLAYER.level = 1;
		Templates.PLAYER.totalHealth = Templates.PLAYER.remainingHealth = 1000;
		Templates.PLAYER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.PLAYER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.PLAYER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.PLAYER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.PLAYER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.PLAYER.heightRatio = 1.5f;
		Templates.PLAYER.drawOffsetRatioY = -0.5f;
		Templates.PLAYER.drawOffsetY = Templates.PLAYER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.PLAYER.weight = 90f;
		Templates.PLAYER.canOpenDoors = true;
		Templates.PLAYER.canEquipWeapons = true;
		Templates.PLAYER.templateId = GameObject.generateNewTemplateId();
		Templates.PLAYER.flipYAxisInMirror = false;

		// General People
		Templates.HUNTER = new Human();
		Templates.HUNTER.title = "Hunter";
		Templates.HUNTER.level = 1;
		Templates.HUNTER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.HUNTER.totalHealth = Templates.HUNTER.remainingHealth = 10;
		Templates.HUNTER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.HUNTER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.HUNTER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.HUNTER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.HUNTER.heightRatio = 1.5f;
		Templates.HUNTER.drawOffsetRatioY = -0.5f;
		Templates.HUNTER.drawOffsetY = Templates.HUNTER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.HUNTER.weight = 90f;
		Templates.HUNTER.canOpenDoors = true;
		Templates.HUNTER.canEquipWeapons = true;
		Templates.HUNTER.aiRoutine = new AIRoutineForHunter(Templates.HUNTER);
		Templates.HUNTER.templateId = GameObject.generateNewTemplateId();
		Templates.HUNTER.flipYAxisInMirror = false;
		Templates.HUNTER.bodyArmor = Templates.LEATHERS.makeCopy(null, null);
		Templates.HUNTER.legArmor = Templates.PANTS.makeCopy(null, null);
		Templates.HUNTER.helmet = Templates.HUNTING_CAP.makeCopy(null, null);

		Templates.GUARD = new Guard();
		Templates.GUARD.title = "Guard";
		Templates.GUARD.level = 10;
		Templates.GUARD.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 5));
		Templates.GUARD.totalHealth = Templates.GUARD.remainingHealth = 10;
		Templates.GUARD.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 20));
		Templates.GUARD.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 11));
		Templates.GUARD.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 13));
		Templates.GUARD.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 21));
		Templates.GUARD.heightRatio = 1.5f;
		Templates.GUARD.drawOffsetRatioY = -0.5f;
		Templates.GUARD.drawOffsetY = Templates.GUARD.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.GUARD.weight = 100f;
		Templates.GUARD.canOpenDoors = true;
		Templates.GUARD.canEquipWeapons = true;
		Templates.GUARD.aiRoutine = new AIRoutineForGuard(Templates.GUARD);
		Templates.GUARD.templateId = GameObject.generateNewTemplateId();
		Templates.GUARD.flipYAxisInMirror = false;

		Templates.FISHERMAN = new Human();
		Templates.FISHERMAN.title = "Fisherman";
		Templates.FISHERMAN.level = 10;
		Templates.FISHERMAN.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE,
				new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.FISHERMAN.totalHealth = Templates.FISHERMAN.remainingHealth = 10;
		Templates.FISHERMAN.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 6));
		Templates.FISHERMAN.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 4));
		Templates.FISHERMAN.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE,
				new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 3));
		Templates.FISHERMAN.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 11));
		Templates.FISHERMAN.hairImageTexture = ResourceUtils.getGlobalImage("hair_1.png", false);
		Templates.FISHERMAN.heightRatio = 1.5f;
		Templates.FISHERMAN.drawOffsetRatioY = -0.5f;
		Templates.FISHERMAN.drawOffsetY = Templates.FISHERMAN.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.FISHERMAN.weight = 68f;
		Templates.FISHERMAN.canOpenDoors = true;
		Templates.FISHERMAN.canEquipWeapons = true;
		Templates.FISHERMAN.aiRoutine = new AIRoutineForFisherman(Templates.FISHERMAN);
		Templates.FISHERMAN.templateId = GameObject.generateNewTemplateId();
		Templates.FISHERMAN.flipYAxisInMirror = false;

		// General People
		Templates.MINER = new Human();
		Templates.MINER.title = "Miner";
		Templates.MINER.level = 1;
		Templates.MINER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.MINER.totalHealth = Templates.MINER.remainingHealth = 10;
		Templates.MINER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 13));
		Templates.MINER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 5));
		Templates.MINER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 4));
		Templates.MINER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 12));
		Templates.MINER.heightRatio = 1.5f;
		Templates.MINER.drawOffsetRatioY = -0.5f;
		Templates.MINER.drawOffsetY = Templates.MINER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.MINER.weight = 110f;
		Templates.MINER.canOpenDoors = true;
		Templates.MINER.canEquipWeapons = true;
		Templates.MINER.aiRoutine = new AIRoutineForMiner(Templates.MINER);
		Templates.MINER.templateId = GameObject.generateNewTemplateId();
		Templates.MINER.flipYAxisInMirror = false;

		Templates.LUMBERJACK = new Human();
		Templates.LUMBERJACK.title = "Lumberjack";
		Templates.LUMBERJACK.level = 1;
		Templates.LUMBERJACK.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE,
				new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.LUMBERJACK.totalHealth = Templates.LUMBERJACK.remainingHealth = 10;
		Templates.LUMBERJACK.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 13));
		Templates.LUMBERJACK.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 5));
		Templates.LUMBERJACK.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE,
				new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 4));
		Templates.LUMBERJACK.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 12));
		Templates.LUMBERJACK.heightRatio = 1.5f;
		Templates.LUMBERJACK.drawOffsetRatioY = -0.5f;
		Templates.LUMBERJACK.drawOffsetY = Templates.LUMBERJACK.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.LUMBERJACK.weight = 110f;
		Templates.LUMBERJACK.canOpenDoors = true;
		Templates.LUMBERJACK.canEquipWeapons = true;
		Templates.LUMBERJACK.aiRoutine = new AIRoutineForLumberjack(Templates.LUMBERJACK);
		Templates.LUMBERJACK.templateId = GameObject.generateNewTemplateId();
		Templates.LUMBERJACK.flipYAxisInMirror = false;

		Templates.THIEF = new Thief();
		Templates.THIEF.title = "Thief";
		Templates.THIEF.level = 1;
		Templates.THIEF.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 2));
		Templates.THIEF.totalHealth = Templates.THIEF.remainingHealth = 10;
		Templates.THIEF.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.THIEF.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.THIEF.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.THIEF.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.THIEF.heightRatio = 1.5f;
		Templates.THIEF.drawOffsetRatioY = -0.5f;
		Templates.THIEF.drawOffsetY = Templates.THIEF.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.THIEF.weight = 90f;
		Templates.THIEF.canOpenDoors = true;
		Templates.THIEF.canEquipWeapons = true;
		Templates.THIEF.aiRoutine = new AIRoutineForThief(Templates.THIEF);
		Templates.THIEF.templateId = GameObject.generateNewTemplateId();
		Templates.THIEF.flipYAxisInMirror = false;

		Templates.FARMER = new Human();
		Templates.FARMER.title = "Farmer";
		Templates.FARMER.level = 1;
		Templates.FARMER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.FARMER.totalHealth = Templates.FARMER.remainingHealth = 10;
		Templates.FARMER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.FARMER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.FARMER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.FARMER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.FARMER.heightRatio = 1.5f;
		Templates.FARMER.drawOffsetRatioY = -0.5f;
		Templates.FARMER.drawOffsetY = Templates.FARMER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.FARMER.weight = 90f;
		Templates.FARMER.canOpenDoors = true;
		Templates.FARMER.canEquipWeapons = true;
		Templates.FARMER.aiRoutine = new AIRoutineForHunter(Templates.FARMER);
		Templates.FARMER.templateId = GameObject.generateNewTemplateId();
		Templates.FARMER.flipYAxisInMirror = false;
		Templates.FARMER.bodyArmor = null;
		Templates.FARMER.legArmor = Templates.UNDIES.makeCopy(null, null);

		// Special People
		Templates.MORT = new Mort();
		Templates.MORT.title = "Mort";
		Templates.MORT.level = 1;
		Templates.MORT.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 2));
		Templates.MORT.totalHealth = Templates.MORT.remainingHealth = 10;
		Templates.MORT.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.MORT.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.MORT.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.MORT.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.MORT.heightRatio = 1.5f;
		Templates.MORT.drawOffsetRatioY = -0.5f;
		Templates.MORT.drawOffsetY = Templates.MORT.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.MORT.weight = 90f;
		Templates.MORT.canOpenDoors = true;
		Templates.MORT.canEquipWeapons = true;
		Templates.MORT.aiRoutine = new AIRoutineForMort(Templates.MORT);
		Templates.MORT.templateId = GameObject.generateNewTemplateId();
		Templates.MORT.flipYAxisInMirror = false;

		Templates.KIDNAPPER = new Kidnapper();
		Templates.KIDNAPPER.title = "KIDNAPPER";
		Templates.KIDNAPPER.level = 1;
		Templates.KIDNAPPER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE,
				new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 2));
		Templates.KIDNAPPER.totalHealth = Templates.KIDNAPPER.remainingHealth = 10;
		Templates.KIDNAPPER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.KIDNAPPER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.KIDNAPPER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE,
				new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.KIDNAPPER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.KIDNAPPER.heightRatio = 1.5f;
		Templates.KIDNAPPER.drawOffsetRatioY = -0.5f;
		Templates.KIDNAPPER.drawOffsetY = Templates.KIDNAPPER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.KIDNAPPER.weight = 90f;
		Templates.KIDNAPPER.aiRoutine = new AIRoutineForKidnapper(Templates.KIDNAPPER);
		Templates.KIDNAPPER.templateId = GameObject.generateNewTemplateId();

		Templates.TRADER = new Trader();
		Templates.TRADER.title = "Trader";
		Templates.TRADER.level = 1;
		Templates.TRADER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 2));
		Templates.TRADER.totalHealth = Templates.TRADER.remainingHealth = 10;
		Templates.TRADER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.TRADER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.TRADER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.TRADER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.TRADER.heightRatio = 1.5f;
		Templates.TRADER.drawOffsetRatioY = -0.5f;
		Templates.TRADER.drawOffsetY = Templates.TRADER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.TRADER.weight = 90f;
		Templates.TRADER.canOpenDoors = true;
		Templates.TRADER.canEquipWeapons = true;
		Templates.TRADER.aiRoutine = new AIRoutineForTrader(Templates.TRADER);
		Templates.TRADER.templateId = GameObject.generateNewTemplateId();
		Templates.TRADER.flipYAxisInMirror = false;

		Templates.DOCTOR = new Doctor();
		Templates.DOCTOR.title = "Doctor";
		Templates.DOCTOR.level = 1;
		Templates.DOCTOR.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 4));
		Templates.DOCTOR.totalHealth = Templates.DOCTOR.remainingHealth = 10;
		Templates.DOCTOR.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 7));
		Templates.DOCTOR.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 12));
		Templates.DOCTOR.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 20));
		Templates.DOCTOR.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 7));
		Templates.DOCTOR.heightRatio = 1.5f;
		Templates.DOCTOR.drawOffsetRatioY = -0.5f;
		Templates.DOCTOR.drawOffsetY = Templates.DOCTOR.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.DOCTOR.weight = 80f;
		Templates.DOCTOR.aiRoutine = new AIRoutineForDoctor(Templates.DOCTOR);
		Templates.DOCTOR.templateId = GameObject.generateNewTemplateId();

		Templates.FOLLOWER = new Follower();
		Templates.FOLLOWER.title = "???";
		Templates.FOLLOWER.level = 1;
		Templates.FOLLOWER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE,
				new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 4));
		Templates.FOLLOWER.totalHealth = Templates.FOLLOWER.remainingHealth = 10;
		Templates.FOLLOWER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 7));
		Templates.FOLLOWER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(HIGH_LEVEL_STATS.DEXTERITY, 12));
		Templates.FOLLOWER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE,
				new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 20));
		Templates.FOLLOWER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(HIGH_LEVEL_STATS.ENDURANCE, 7));
		Templates.FOLLOWER.heightRatio = 1.5f;
		Templates.FOLLOWER.drawOffsetRatioY = -0.5f;
		Templates.FOLLOWER.drawOffsetY = Templates.FOLLOWER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.FOLLOWER.weight = 80f;
		Templates.FOLLOWER.aiRoutine = new AIRoutineForFollower(Templates.FOLLOWER);
		Templates.FOLLOWER.templateId = GameObject.generateNewTemplateId();

		Templates.MINECART_RIDER = new Human();
		Templates.MINECART_RIDER.title = "MINECART_RIDER";
		Templates.MINECART_RIDER.level = 1;
		Templates.MINECART_RIDER.highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE,
				new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, 3));
		Templates.MINECART_RIDER.totalHealth = Templates.MINECART_RIDER.remainingHealth = 10;
		Templates.MINECART_RIDER.highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(HIGH_LEVEL_STATS.STRENGTH, 10));
		Templates.MINECART_RIDER.highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY,
				new Stat(HIGH_LEVEL_STATS.DEXTERITY, 10));
		Templates.MINECART_RIDER.highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE,
				new Stat(HIGH_LEVEL_STATS.INTELLIGENCE, 10));
		Templates.MINECART_RIDER.highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE,
				new Stat(HIGH_LEVEL_STATS.ENDURANCE, 10));
		Templates.MINECART_RIDER.heightRatio = 1.5f;
		Templates.MINECART_RIDER.drawOffsetRatioY = -0.5f;
		Templates.MINECART_RIDER.drawOffsetY = Templates.MINECART_RIDER.drawOffsetRatioY * Game.SQUARE_HEIGHT;
		Templates.MINECART_RIDER.weight = 90f;
		Templates.MINECART_RIDER.canOpenDoors = true;
		Templates.MINECART_RIDER.canEquipWeapons = true;
		Templates.MINECART_RIDER.aiRoutine = new AIRoutineForMinecart(Templates.MINECART_RIDER);
		Templates.MINECART_RIDER.templateId = GameObject.generateNewTemplateId();
		Templates.MINECART_RIDER.flipYAxisInMirror = false;
		Templates.MINECART_RIDER.bodyArmor = null;
		Templates.MINECART_RIDER.legArmor = Templates.UNDIES.makeCopy(null, null);
	}

}
