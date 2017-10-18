package com.marklynch.level.quest.smallgame;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.ai.utils.AIRoutineUtils;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Group;
import com.marklynch.level.constructs.actionlisteners.ActionListener;
import com.marklynch.level.constructs.adventurelog.AdventureInfo;
import com.marklynch.level.constructs.adventurelog.Objective;
import com.marklynch.level.constructs.beastiary.BestiaryKnowledge;
import com.marklynch.level.constructs.bounds.structure.Structure;
import com.marklynch.level.constructs.bounds.structure.StructurePath;
import com.marklynch.level.constructs.bounds.structure.StructureRoom;
import com.marklynch.level.constructs.bounds.structure.StructureRoom.RoomPart;
import com.marklynch.level.constructs.bounds.structure.StructureSection;
import com.marklynch.level.constructs.inventory.Inventory;
import com.marklynch.level.conversation.Conversation;
import com.marklynch.level.conversation.ConversationPart;
import com.marklynch.level.conversation.ConversationResponse;
import com.marklynch.level.conversation.LeaveConversationListener;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.Chest;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Readable;
import com.marklynch.objects.Templates;
import com.marklynch.objects.Wall;
import com.marklynch.objects.actions.ActionGiveSpecificItem;
import com.marklynch.objects.actions.ActionTalk;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.CarnivoreNeutralWildAnimal;
import com.marklynch.objects.units.Hunter;

public class QuestSmallGame extends Quest {

	// Needs to be a large forest w/ animals near te lodge for the hunters after
	// the quest, if they survive.... it just makes sense. Another village could
	// be a fishing village and another one could be a farming village or
	// another could be a big trading hub and they trade to get food, and a
	// foraging village, and mix and match some..... make it clear in
	// conversations that this is a farming/fishing/hunting/trading village.
	// There could be two farming villages, one of them does only veg and
	// foraging, and theyre all hippy vegans. Another could be deep in the
	// woods, and the houses are made out of giant trees and they forage and
	// hunt.

	// The hunting party leader says out loud to the pack, in a way that u can
	// see it "so, use the water imbued weapons on the the beast. No fire, it
	// feeds off the stuff."

	// Then at the start of the conversation it's him talking to the gorup going
	// "...and that's how we're going to pull off the greatest hunt of all
	// time." Then turning to u.... begin explaining quest.

	// Hunting plan could have a crude drawying of the wolf. I think all signs
	// should have pngs to display instead of text.
	// "Hello stranger. I'm (creating) the most formidable hunting party this
	// town has ever seen, would you be interested in joining us? -yes -no
	// .Living in the craggle cave just south of here is a ferocious beast. She
	// may look nothing more than an obersizef wolf, but dpnt let her fool you.
	// Flames spit forth from his eyes and she can kill a man with nary a howl"
	// -why would u want to kill this beast? -from what u say it soounds like an
	// impossible task -i love a challenge, im in." I love the ridiculous
	// description of the beast, make it more ridiculous, theres definitely
	// dialogue like this in the witcher, look it up. AND THEN make the
	// ridiculous description true :D. Also new condition where u tell them u
	// wont be part of it, and they say last chance, u say ur sure, then the
	// mage runs over to u to tell u to save the beast. Everytging the hunter
	// says should be over the top, pirate weaving tales in a bar stupid. Look
	// up some similar adventure time and pirate show dialogs like this.
	// And the head hunter lost and arm to the beast! :p "-no beast like that
	// exists, you're ridiculous". "Tis the same beast that claimed my arm! Well
	// come with us and see with your own eyes". In the cave theres his arm!!!
	// :'D "rotted arm" With a ring beside it. U could give him the ring if u
	// want and he goes "nono, that's very kind of you, but keep it laddie, it
	// just brings back painful memories for me".
	// On the sign for the hunt plan have even more ridiculous facts about the
	// beast that are still actually facts.

	// "There should be some spare equipment round the back, help yourself"

	// Need to change ownership of the stuff around the back from master hunter
	// to you when you accept the quest. 2 guys around the back to stop u
	// stealing shit beforehand.

	// They're all standing around in a circle with awesome equipment in the
	// middle

	// For stealing show a dialogue if u'll get caught doing it.

	// O M F G the enviromentalist wants to enslave super wolf. O M F G
	// O M F G. And he like tried to get the wolves power before but he was
	// rejected.
	// And now that the wolf is weakened after the fight he puts him in an evil
	// restriction
	// And u can choose to fight the environmentalist or not. If u fight him he
	// has a skill "entrap" or "restriction" or something, and he has the skill
	// book for that power on his body. And he has a note on his body,
	// instructions on how to entrap a wolf spirit god thing. SO u can try
	// entrap the wolf or let it go. Entrap it to get ++fire resistance and if u
	// let the wolf go he'll give u something deadly from the cave.
	// And if u kill the wolf he attacks u.. and hes powerful and it'll take a
	// bunch of ur new hunter pals to save ur ass. Or something.

	// SO powerwise - hunters == wolfpack > envirnmentalist.

	// special case if u kill enviromentalist early on :P

	// Could lead in to larger overarching story with spirit animals

	// U can rob the hunters blind when they leave on the hunt, u can go back
	// and steal all their shit :P I like this.

	// quest flag on the weapons round back should be enough to stop AIs
	// stealing shit + guards nearby + put ownership = hunters on it until ur
	// told to go get them

	// Make the enviromentalist seem dopey until the end. What do u call an
	// enviromentalist magic user? Sage? Forest Sage? Woodland sage? Sage is
	// good, makes him sound benevolent and omniscient.

	// Some sort of reaction from the wolves if u see them before accepting the
	// hunt.Walk over and have a look at u? Nothing agressive. Maybe
	// unintentionally intimidating?

	// Quest text
	final String OBJECTIVE_FOLLOW_THE_HUNTERS_TO_SUPERWOLF = "Follow the hunters to Superwolf";

	// Activity Strings
	final String ACTIVITY_PLANNING_A_HUNT = "Planning a hunt";
	final String ACTIVITY_DESCRIPTION_HUNTING = "Goin' hunting";
	final String ACTIVITY_SPYING = "Spying";
	final String ACTIVITY_SAVING_THE_WORLD = "Saving the world";
	final String ACTIVITY_WAITING_FOR_YOU = "Waiting for you";
	final String ACTIVITY_DESCRIPTION_GOING_HOME = "Going home";

	// End
	boolean huntersReleasedFromQuest;

	// Actors
	Group hunterPack;
	Actor environmentalistBill;
	Group wolfPack;
	Actor superWolf;
	Actor cub;

	// GameObjects
	ArrayList<GameObject> weaponsBehindLodge;

	// Squares
	Square squareBehindLodge;
	Square huntPlanningArea;
	Actor hunterBrent;

	// Conversations
	public static Conversation conversationHuntersJoinTheHunt;
	public static Conversation conversationEnviromentalistImNotSpying;
	public static Conversation conversationEnviromentalistSaveTheWolf;
	public static Conversation conversationHuntersReadyToGo;
	public static Conversation conversationHuntersOnlyHuntersGetLoot;

	public static Actor hunter;

	// public String objectiveTheWolves;// = "The Wolves";
	// public String objectiveTheWeapons = "Weapons behind lodge";
	// public String objectiveTheHunters = "Hunters";
	public Objective objectiveWolves;
	public Objective objectiveWeaponsBehindLodge;
	public Objective objectiveHunters;
	public Objective objectiveEnvironmentalist;

	// Info strings
	AdventureInfo infoSeenHunters = new AdventureInfo("I've spotted some hunters planning a hunt");
	AdventureInfo infoSeenWolves = new AdventureInfo("I've spotted a pack of wolves.");
	AdventureInfo infoAgreedToJoinHunters = new AdventureInfo(
			"I've agreed to join a group of hunters in town on a hunt for The Super Wolf, they told me there's some weapons around the back of their Lodge");
	AdventureInfo infoSetOffWithHunters = new AdventureInfo(
			"I've set off with the hunters towards the creature's lair");
	AdventureInfo infoEnviromentalistWasSpying = new AdventureInfo(
			"I met a strange figure spying on the hunters of Town Lodge");
	AdventureInfo infoSaveTheWolf1 = new AdventureInfo(
			"Behind the hunting lodge, where the weapons were meant to be, stood a strange figure. He spoke 3 words - \"Save the wolf\"");
	AdventureInfo infoSaveTheWolf2 = new AdventureInfo(
			"Behind the hunting lodge, where the weapons were meant to be, stood the strange figure from before. He spoke 3 words - \"Save the wolf\"");

	AdventureInfo infoRetrievedWeapons = new AdventureInfo("I've retrieved the weapons from behind the hunter's lodge");
	AdventureInfo infoReadHuntPlan1 = new AdventureInfo("In the staging area for a hunt I found the plan for the hunt");
	AdventureInfo infoReadHuntPlan2 = new AdventureInfo(
			"In the staging area for the hunt I found the plan for the hunt");

	AdventureInfo infoTalkedToWolves = new AdventureInfo(
			"A wolf, talked to me. He told me \"They come\". He showed me hunters in the town nearby planning a hunt.");

	AdventureInfo infoAttackedHunters = new AdventureInfo("I attacked the hunters");
	AdventureInfo infoAttackedWolves = new AdventureInfo("I attacked the wolves");
	AdventureInfo infoHuntersEngagedWolves = new AdventureInfo("The hunters have engaged the wolves");
	AdventureInfo infoHuntersDead = new AdventureInfo("All the hunters are dead");
	AdventureInfo infoWolvesDead = new AdventureInfo("All the wolves are dead");

	// Resolutions
	AdventureInfo infoToldToFuckOffByHunters = new AdventureInfo(
			"I didn't help the hunters and they're giving me nothing");
	AdventureInfo infoRewardedByHunters = new AdventureInfo("The hunters rewarded me for helping them");
	AdventureInfo infoIgnoredByWolves = new AdventureInfo("I didn't help the wolves and they are ignoring me");
	AdventureInfo infoThankedByWolves = new AdventureInfo("The wolves thanked me for helping them");
	AdventureInfo infoAllDead = new AdventureInfo("The hunters and wolves are all dead");

	// Flags

	// boolean playerAttackedWolves;
	// boolean huntersDead;
	// boolean wolvesDead;

	public QuestSmallGame() {
		super();

		name = "SMALL GAME";
		// addObjective("No objective");

		squareBehindLodge = Game.level.squares[111][16];
		huntPlanningArea = Game.level.squares[105][8];

		// BRENT

		// Add lead hunter
		GameObject brentsBed = Templates.BED.makeCopy(Game.level.squares[110][10]);
		hunterBrent = Templates.HUNTER.makeCopy(Game.level.squares[105][8],
				Game.level.factions.townsPeople, brentsBed, 203, new GameObject[] {
						Templates.HUNTING_BOW.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		hunter = hunterBrent;

		// Hunting lodge
		ArrayList<GameObject> lodgeFeatures = new ArrayList<GameObject>();
		lodgeFeatures
				.add(Templates.DOOR.makeCopy("Door", Game.level.squares[105][12], false, false, false, hunterBrent));
		ArrayList<StructureRoom> lodgeRooms = new ArrayList<StructureRoom>();
		lodgeRooms.add(new StructureRoom("Hunting Lodge", 107, 9, false, new ArrayList<Actor>(),
				new RoomPart(106, 10, 110, 14)));
		ArrayList<StructureSection> lodgeSections = new ArrayList<StructureSection>();
		lodgeSections.add(new StructureSection("Hunting Lodge", 105, 9, 111, 15, false));
		Structure lodge = new Structure("Hunting Lodge", lodgeSections, lodgeRooms, new ArrayList<StructurePath>(),
				lodgeFeatures, new ArrayList<Square>(), "building.png", 896, 896 + 640, 896, 896 + 640, true,
				hunterBrent, new ArrayList<Square>(), new ArrayList<Wall>(), Templates.WALL, Square.STONE_TEXTURE, 2);
		Game.level.structures.add(lodge);

		// Add hunters
		GameObject brontsBed = Templates.BED.makeCopy(Game.level.squares[108][10]);
		Actor hunterBront1 = Templates.HUNTER.makeCopy(Game.level.squares[103][7],
				Game.level.factions.townsPeople, brontsBed, 124, new GameObject[] {
						Templates.HUNTING_BOW.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		GameObject bront2sBed = Templates.BED.makeCopy(Game.level.squares[106][10]);
		Actor hunterBront2 = Templates.HUNTER.makeCopy(Game.level.squares[103][8],
				Game.level.factions.townsPeople, bront2sBed, 73, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		GameObject bront3sBed = Templates.BED.makeCopy(Game.level.squares[110][12]);
		Actor hunterBront3 = hunterBront2.makeCopy(Game.level.squares[103][9],
				Game.level.factions.townsPeople, bront3sBed, 30, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		GameObject bront4sBed = Templates.BED.makeCopy(Game.level.squares[110][14]);
		Actor hunterBront4 = hunterBront2.makeCopy(Game.level.squares[102][7],
				Game.level.factions.townsPeople, bront4sBed, 83, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		GameObject bront5sBed = Templates.BED.makeCopy(Game.level.squares[108][14]);
		Actor hunterBront5 = hunterBront2.makeCopy(Game.level.squares[102][8],
				Game.level.factions.townsPeople, bront5sBed, 23, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);
		GameObject bront6sBed = Templates.BED.makeCopy(Game.level.squares[106][14]);
		Actor hunterBront6 = hunterBront2.makeCopy(Game.level.squares[102][9],
				Game.level.factions.townsPeople, bront6sBed, 43, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);

		Actor thief = Templates.THIEF.makeCopy(
				Game.level.squares[12][13], Game.level.factions.outsiders, null, 64, new GameObject[] {
						Templates.HATCHET.makeCopy(null, null), Templates.HUNTING_KNIFE.makeCopy(null, null) },
				new GameObject[] {}, null);

		// Some ground hatchets
		Templates.HATCHET.makeCopy(Game.level.squares[3][6], Game.level.player);
		Templates.HATCHET.makeCopy(Game.level.squares[5][6], thief);
		Templates.BLOOD.makeCopy(Game.level.squares[5][6], Game.level.player);
		Templates.HATCHET.makeCopy(Game.level.squares[1][6], Game.level.player);

		hunterPack = new Group("Hunting Party", hunterBrent, hunterBront1, hunterBront2, hunterBront3, hunterBront4,
				hunterBront5, hunterBront6);

		this.hunterPack.quest = this;
		for (GameObject hunter : hunterPack.getMembers()) {
			hunter.quest = this;
		}

		final Readable huntingPlan = Templates.SIGN.makeCopy(Game.level.squares[106][8], "Hunt Action Plan",
				new Object[] { "Super Wolf - Weaknesses: Water Strengths: Fire will heal the beast" }, hunterBrent);

		Chest chest = Templates.CHEST.makeCopy("Chest", Game.level.squares[103][1], false, null);
		chest.inventory.add(Templates.CLEAVER.makeCopy(null, null));
		chest.inventory.add(Templates.HUNTING_KNIFE.makeCopy(null, thief));

		environmentalistBill = new Hunter("Environmentalist Bill", "Environmentalist", 1, 10, 0, 0, 0, 0,
				"environmentalist.png", Game.level.squares[105][16], 1, 10, null, new Inventory(), 1, 1, 0f, 0f, 1f, 1f,
				1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 0f, 110f, null, Game.level.factions.outsiders, 0, 0, 0, 0,
				0, 0, 0, 0, 10, new GameObject[] {}, new GameObject[] {}, GameObject.generateNewTemplateId());
		environmentalistBill.inventory.add(Templates.HATCHET.makeCopy(null, environmentalistBill));
		environmentalistBill.quest = this;

		superWolf = new CarnivoreNeutralWildAnimal("Wolf Queen", "Wild animal", 1, 10, 0, 0, 0, 0, "fire_wolf.png",
				Game.level.squares[128][12], 1, 10, null, new Inventory(), 1, 1, 0f, 0f, 1f, 1f, 1f, null, 0.5f, 0.5f,
				false, 0f, 0f, 0f, 0f, 0f, 150f, null, Game.level.factions.wolves, 0, 0, 0, 0, 0, 0, 0, 0,
				new GameObject[] {}, new GameObject[] {}, GameObject.generateNewTemplateId());

		Actor wolf2 = new CarnivoreNeutralWildAnimal("Wolf", "Wild animal", 1, 10, 0, 0, 0, 0, "wolf_green.png",
				Game.level.squares[127][13], 1, 10, null, new Inventory(), 1, 1, 0f, 0f, 1f, 1f, 1f, null, 0.5f, 0.5f,
				false, 0f, 0f, 0f, 0f, 0f, 60f, null, Game.level.factions.wolves, 0, 0, 0, 0, 0, 0, 0, 0,
				new GameObject[] {}, new GameObject[] {}, GameObject.generateNewTemplateId());

		Actor wolf3 = new CarnivoreNeutralWildAnimal("Wolf", "Wild animal", 1, 10, 0, 0, 0, 0, "wolf_pink.png",
				Game.level.squares[127][11], 1, 10, null, new Inventory(), 1, 1, 0f, 0f, 1f, 1f, 1f, null, 0.5f, 0.5f,
				false, 0f, 0f, 0f, 0f, 0f, 60f, null, Game.level.factions.wolves, 0, 0, 0, 0, 0, 0, 0, 0,
				new GameObject[] {}, new GameObject[] {}, GameObject.generateNewTemplateId());

		// [207][16]

		wolfPack = new Group("Wolf pack", superWolf, wolf2, wolf3);

		this.wolfPack.quest = this;
		for (GameObject wolf : wolfPack.getMembers()) {
			wolf.quest = this;
			wolf.inventory.add(Templates.CLEAVER.makeCopy(null, null));
		}

		weaponsBehindLodge = new ArrayList<GameObject>();
		weaponsBehindLodge.add(Templates.HATCHET.makeCopy(squareBehindLodge, hunterBrent));
		weaponsBehindLodge.add(Templates.HUNTING_BOW.makeCopy(squareBehindLodge, hunterBrent));

		for (GameObject weaponBehindLodge : weaponsBehindLodge) {
			weaponBehindLodge.quest = this;
		}
		// END OF FROM EDITOR

		AreaTownForest.createForest();

		objectiveWolves = new Objective("The Wolves", superWolf, null);
		objectiveWeaponsBehindLodge = new Objective("Hunting Weapons", weaponsBehindLodge.get(0), null);
		objectiveHunters = new Objective("The Hunters", hunterBrent, null);
		objectiveEnvironmentalist = new Objective("Environmentalist", environmentalistBill, null);

		setUpConversationReady();
		setUpConversationImNotSpying();
		setUpConversationSaveTheWolf();
		setUpConversationHunterOpening();
		setUpConversationYouDidntHelp();

		huntingPlan.setOnReadListener(new ActionListener() {
			@Override
			public void run() {
				if (!haveInfo(infoReadHuntPlan1) && !haveInfo(infoReadHuntPlan2)) {
					if (!started) {
						addInfo(infoReadHuntPlan1);
						addObjective(objectiveHunters);
					} else {
						addInfo(infoReadHuntPlan2);
					}
					turnUpdated = Level.turn;
					addInfo(new AdventureInfo(huntingPlan));
					addInfo(new AdventureInfo(superWolf));

					BestiaryKnowledge bestiaryKnowledge = Level.bestiaryKnowledgeCollection.get(superWolf.templateId);
					bestiaryKnowledge.name = true;
					bestiaryKnowledge.image = true;
					bestiaryKnowledge.fireResistance = true;
					bestiaryKnowledge.waterResistance = true;

				}
			}
		});
	}

	@Override
	public void update() {
		if (resolved)
			return;

		// See hunters for first time
		if (!haveInfo(infoSeenHunters)) {
			if (hunterBrent.squareGameObjectIsOn.visibleToPlayer) {
				addInfo(infoSeenHunters);
				addObjective(objectiveHunters);
			}
		}

		// See wolves for first time
		if (!haveInfo(infoSeenWolves)) {
			if (superWolf.squareGameObjectIsOn.visibleToPlayer) {
				addInfo(infoSeenWolves);
				addObjective(objectiveWolves);
			}
		}

		// You have the pick up weapons objective and pick them up
		if (currentObjectives.contains(objectiveWeaponsBehindLodge)) {
			for (GameObject weapon : weaponsBehindLodge) {
				if (Game.level.player.inventory.contains(weapon)) {
					currentObjectives.remove(this.objectiveWeaponsBehindLodge);
					if (!haveInfo(infoSaveTheWolf1) && !haveInfo(infoSaveTheWolf2)) {
						addInfo(infoRetrievedWeapons);
					}
				}
			}
		}

		// The wolves are dead
		if (!haveInfo(infoWolvesDead)) {
			if (wolfPack.size() == 0) {
				addInfo(infoWolvesDead);
			}
		}

		// Player has attacked the wolves
		if (!haveInfo(infoAttackedWolves) && wolfPack.getAttackers().contains(Game.level.player)) {
			addInfo(infoSeenWolves);
			addInfo(infoAttackedWolves);

		}

		// The hunters are dead
		if (!haveInfo(infoHuntersDead)) {
			if (hunterPack.size() == 0) {
				addInfo(infoHuntersDead);
			}
		}

		// Player has attacked the hunters after accepting quest
		if (!haveInfo(infoAttackedHunters) && hunterPack.getAttackers().contains(Game.level.player)) {
			addInfo(infoSeenHunters);
			addInfo(infoAttackedHunters);
		}

		// Hunters and wolves have fought
		if (!haveInfo(infoHuntersEngagedWolves) && hunterPack.hasAttackers()) {
			for (int j = 0; j < wolfPack.size(); j++) {
				if (hunterPack.getAttackers().contains(wolfPack.getMember(j))) {
					addInfo(infoSeenWolves);
					addInfo(infoHuntersEngagedWolves);
					break;
				}
			}
		}

	}

	@Override
	public boolean update(Actor actor) {
		if (resolved)
			return false;
		if (hunterPack.contains(actor)) {
			return updateHunter(actor);
		} else if (actor == environmentalistBill) {
			return updateEnvironmentalist(actor);
		} else if (wolfPack.contains(actor)) {
			return true;
		}
		return false;
	}

	private boolean updateHunter(Actor actor) {

		if (huntersReleasedFromQuest) {
			return false;
		}

		if (!haveInfo(infoSetOffWithHunters)) {
			actor.activityDescription = ACTIVITY_PLANNING_A_HUNT;
			if (actor == hunterPack.getLeader()) {
				goToHuntPlanningArea(actor);
			}
		} else if (haveInfo(infoSetOffWithHunters) && !haveInfo(infoWolvesDead)) {

			if (actor == hunterPack.getLeader()) {

				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) > 5
						&& actor.straightLineDistanceTo(superWolf.squareGameObjectIsOn) < Game.level.player
								.straightLineDistanceTo(superWolf.squareGameObjectIsOn)) {
					// Wait for the player
					actor.activityDescription = ACTIVITY_WAITING_FOR_YOU;
				} else {
					actor.activityDescription = ACTIVITY_DESCRIPTION_HUNTING;
					if (superWolf.remainingHealth > 0) {
						if (!AIRoutineUtils.attackTarget(superWolf)) {
							AIRoutineUtils.moveTowardsTargetToAttack(superWolf);
						}
					}
				}
			}
		} else if (haveInfo(infoWolvesDead) && haveInfo(infoAttackedWolves) && !haveInfo(infoAgreedToJoinHunters)) {
			// Wolves were killed by player before accepting the mission
			goToHuntPlanningArea(actor);
		} else if (haveInfo(infoWolvesDead) && haveInfo(infoAttackedWolves) && !haveInfo(infoSetOffWithHunters)) {
			// Wolves were killed by player after accepting the mission, but
			// before he told the hunters he's ready to go
			goToHuntPlanningArea(actor);
		} else if (haveInfo(infoWolvesDead) && haveInfo(infoAttackedWolves)) {
			// Wolves were killed after departing for the hunt, and the player
			// helped kill them
			// Talk to them... for some reason
			if (actor == hunterPack.getLeader()) {
				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) < 2) {
					new ActionTalk(actor, Game.level.player).perform();
				} else {
					AIRoutineUtils.moveTowardsSquareToBeAdjacent(Game.level.player.squareGameObjectIsOn);
				}
			}
		} else if (haveInfo(infoWolvesDead) && !haveInfo(infoAttackedWolves)) {
			// Wolves were killed, but player didnt help
			if (actor == hunterPack.getLeader()) {
				if (actor.squareGameObjectIsOn != huntPlanningArea) {
					actor.activityDescription = ACTIVITY_DESCRIPTION_GOING_HOME;
					AIRoutineUtils.moveTowardsTargetSquare(huntPlanningArea);
				} else {
					huntersReleasedFromQuest = true;
				}
			}
		}
		return true;

	}

	public void goToHuntPlanningArea(Actor actor) {
		AIRoutineUtils.moveTowardsTargetSquare(huntPlanningArea);
	}

	private boolean updateEnvironmentalist(Actor actor) {
		if (haveInfo(infoSetOffWithHunters)) {
			// Hunters are on the move, head to wolf
			if (environmentalistBill.canSeeGameObject(superWolf)) {
			} else {
				AIRoutineUtils.moveTowardsTargetSquare(superWolf.squareGameObjectIsOn);
			}
			return true;
		} else if (!haveInfo(infoAgreedToJoinHunters)) {
			// Starting point, spying
			actor.activityDescription = ACTIVITY_SPYING;
		} else if (haveInfo(infoAgreedToJoinHunters) && (!haveInfo(infoSaveTheWolf1) && !haveInfo(infoSaveTheWolf2))) {
			// Go get the weapons

			actor.activityDescription = ACTIVITY_SAVING_THE_WORLD;

			if (environmentalistBill.squareGameObjectIsOn != squareBehindLodge) {
				// Move to weapons behind the lodge
				AIRoutineUtils.moveTowardsTargetSquare(squareBehindLodge);
			} else {
				// Pick up weapons behind the lodge
				for (GameObject weaponBehindLodge : weaponsBehindLodge) {
					if (weaponBehindLodge.squareGameObjectIsOn == squareBehindLodge) {
						AIRoutineUtils.pickupTarget(weaponBehindLodge);

						// MAYBE put loot in a chest in the lodge

					}
				}
				// If the player is near, tell them not to kill the wolf and
				// give them the weapons
				if (actor.straightLineDistanceTo(Game.level.player.squareGameObjectIsOn) < 2) {
					new ActionTalk(actor, Game.level.player).perform();
				}
			}
		}
		return true;
	}

	@Override
	public Conversation getConversation(Actor actor) {
		if (resolved)
			return null;
		if (hunterPack.contains(actor)) {
			return getConversationForHunter(actor);
		} else if (actor == environmentalistBill) {
			return getConversationForEnvironmentalist(actor);
		} else if (wolfPack.contains(actor)) {
			return getConversationForWolf(actor);
		}
		return null;
	}

	public Conversation getConversationForHunter(Actor actor) {
		// Talking to a hunter
		if (!haveInfo(infoAgreedToJoinHunters)) {
			return conversationHuntersJoinTheHunt;
		} else if (!haveInfo(infoSetOffWithHunters)) {
			return conversationHuntersReadyToGo;
		} else if (haveInfo(infoWolvesDead) && !haveInfo(infoAttackedWolves)) {
			return conversationHuntersOnlyHuntersGetLoot;
		}
		return null;
	}

	public Conversation getConversationForEnvironmentalist(Actor actor) {
		// Talking to environmentalist
		if (!haveInfo(infoAgreedToJoinHunters)) {
			return conversationEnviromentalistImNotSpying;
		} else if (!haveInfo(infoSaveTheWolf1) && !haveInfo(infoSaveTheWolf2)) {
			return conversationEnviromentalistSaveTheWolf;
		}
		return environmentalistBill.createConversation("...");
	}

	public Conversation getConversationForWolf(Actor actor) {
		// Talking to environmentalist
		if (haveInfo(infoAttackedWolves)) {
			return null;
		} else if (haveInfo(infoHuntersDead) && !haveInfo(infoAttackedHunters)) {
			return superWolf.createConversation("I have survived, no thanks to you");
		} else if (haveInfo(infoHuntersDead) && haveInfo(infoAttackedHunters)) {
			return superWolf.createConversation("Thank you");
		} else if (haveInfo(infoHuntersEngagedWolves)) {
			return null;
		} else if (haveInfo(infoSetOffWithHunters)) {
			return superWolf.createConversation("They come");
		} else if (!haveInfo(infoSetOffWithHunters)) {
			Conversation conversation = superWolf.createConversation("They plot");
			conversation.openingConversationPart.leaveConversationListener = new LeaveConversationListener() {
				@Override
				public void leave() {
					addInfo(infoSeenWolves);
					addInfo(infoTalkedToWolves);
					addObjective(objectiveWolves);
					addObjective(objectiveHunters);
				}
			};
			return conversation;
		}
		return null;
	}

	public void setUpConversationHunterOpening() {

		ConversationPart conversationPartTheresEquipment = new ConversationPart(
				new Object[] {
						"There's should be some spare equipment 'round back, help yourself! Joe runs a shop to the North if you think you need anything else. Let us know when you're ready." },
				new ConversationResponse[] {}, hunterPack.getLeader(), this);

		ConversationPart conversationPartSuitYourself = new ConversationPart(new Object[] { "Suit yourself." },
				new ConversationResponse[] {}, hunterPack.getLeader(), this);

		ConversationResponse conversationResponseNoThanks = new ConversationResponse("No thanks",
				conversationPartSuitYourself) {
			@Override
			public void select() {
				super.select();
				// ADD QUEST TO QUEST LOG IF NO IN HARDCORE MODE
				// THIS ALSO COMES WITH A TOAST / POPUP SAYING "QUEST STARTED -
				// PACK HUNTERS"

				addInfo(infoSeenHunters);
				addObjective(objectiveHunters);

			}
		};
		ConversationResponse conversationResponseYesPlease = new ConversationResponse("Yes please",
				conversationPartTheresEquipment) {
			@Override
			public void select() {
				super.select();
				// ADD QUEST TO QUEST LOG IF NO IN HARDCORE MODE
				// THIS ALSO COMES WITH A TOAST / POPUP SAYING "QUEST STARTED -
				// PACK HUNTERS"

				addInfo(infoSeenHunters);
				addInfo(infoAgreedToJoinHunters);

				addObjective(objectiveHunters);
				// Add marker for weapons only if theyre on the square
				for (GameObject weapon : weaponsBehindLodge) {
					if (squareBehindLodge.inventory.contains(weapon)) {
						addObjective(objectiveWeaponsBehindLodge);
						weapon.owner = null;
					}
				}

			}
		};

		ConversationPart conversationPartWantToComeHunting = new ConversationPart(
				new Object[] { "We're just about to head out on the hunt, an extra man wouldn't go amiss." },
				new ConversationResponse[] { conversationResponseYesPlease, conversationResponseNoThanks },
				hunterPack.getLeader(), this);

		conversationHuntersJoinTheHunt = new Conversation(conversationPartWantToComeHunting);

	}

	private void setUpConversationImNotSpying() {

		// Environmentalist could have emoticon over his head showing his
		// feelings
		// Anime style
		// try it out
		ConversationPart conversationPartImNotSpying = new ConversationPart(new Object[] {
				"What? NO! I'm not spying! You're spying!", superWolf, hunterBrent, environmentalistBill },
				new ConversationResponse[] {}, environmentalistBill, this);

		conversationPartImNotSpying.leaveConversationListener = new LeaveConversationListener() {

			@Override
			public void leave() {
				addInfo(infoEnviromentalistWasSpying);
				addObjective(objectiveEnvironmentalist);
				addObjective(objectiveHunters);

			}

		};

		conversationEnviromentalistImNotSpying = new Conversation(conversationPartImNotSpying);
	}

	private void setUpConversationSaveTheWolf() {

		ConversationPart conversationPartSaveTheWolf = new ConversationPart(new Object[] { "Save the wolf!" },
				new ConversationResponse[] {}, environmentalistBill, this);

		conversationPartSaveTheWolf.leaveConversationListener = new LeaveConversationListener() {

			@Override
			public void leave() {
				addObjective(objectiveEnvironmentalist);
				for (GameObject gameObject : weaponsBehindLodge) {
					if (environmentalistBill.inventory.contains(gameObject)) {
						new ActionGiveSpecificItem(environmentalistBill, Game.level.player, gameObject, false)
								.perform();

					}
				}
				if (haveInfo(infoEnviromentalistWasSpying)) {
					addInfo(infoSaveTheWolf2);
				} else {
					addInfo(infoSaveTheWolf1);
				}
			}

		};
		conversationEnviromentalistSaveTheWolf = new Conversation(conversationPartSaveTheWolf);

	}

	public void setUpConversationReady() {

		ConversationPart conversationAlrightLetsGo = new ConversationPart(
				new Object[] {
						"Alright! The cave is to the east, past the forest, at the entrance to a now defunct mining operation." },
				new ConversationResponse[] {}, hunterPack.getLeader(), this);

		ConversationPart conversationPartWellHurryOn = new ConversationPart(new Object[] { "Well hurry on!" },
				new ConversationResponse[] {}, hunterPack.getLeader(), this);

		ConversationResponse conversationResponseNotYet = new ConversationResponse("Not yet",
				conversationPartWellHurryOn);
		ConversationResponse conversationResponseReady = new ConversationResponse("Ready!", conversationAlrightLetsGo) {
			@Override
			public void select() {
				super.select();
				// Update quest log
				// Set enviromentalist to come watch
				// Hunters on the way
				addObjective(objectiveWolves);
				if (Game.level.quests.questCaveOfTheBlind.started == false) {
					Game.level.quests.questCaveOfTheBlind
							.addObjective(Game.level.quests.questCaveOfTheBlind.objectiveCave);
					Game.level.quests.questCaveOfTheBlind.addObjective(objectiveHunters);
					Game.level.quests.questCaveOfTheBlind
							.addInfo(Game.level.quests.questCaveOfTheBlind.infoHeardFromHunters);
				}
				addInfo(infoSetOffWithHunters);
			}
		};

		ConversationPart conversationPartReadyToGo = new ConversationPart(new Object[] { "Ready to go, pal?" },
				new ConversationResponse[] { conversationResponseReady, conversationResponseNotYet },
				hunterPack.getLeader(), this);

		conversationHuntersReadyToGo = new Conversation(conversationPartReadyToGo);

	}

	private void setUpConversationYouDidntHelp() {
		// Really like the "Now fuck off!" bit.
		ConversationPart conversationPartOnlyHuntersGetLoot = new ConversationPart(
				new Object[] { "Only hunters get loot. Now fuck off!" }, new ConversationResponse[] {},
				hunterPack.getLeader(), this);
		conversationPartOnlyHuntersGetLoot.leaveConversationListener = new LeaveConversationListener() {

			@Override
			public void leave() {
				addInfo(infoToldToFuckOffByHunters);
				resolve();
			}
		};

		conversationHuntersOnlyHuntersGetLoot = new Conversation(conversationPartOnlyHuntersGetLoot);

	}

}
