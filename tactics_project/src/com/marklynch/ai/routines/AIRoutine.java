package com.marklynch.ai.routines;

import java.util.Random;

import com.marklynch.Game;
import com.marklynch.actions.Action;
import com.marklynch.actions.ActionBuyItems;
import com.marklynch.actions.ActionClose;
import com.marklynch.actions.ActionDropItems;
import com.marklynch.actions.ActionFishingCompleted;
import com.marklynch.actions.ActionFishingFailed;
import com.marklynch.actions.ActionFishingInProgress;
import com.marklynch.actions.ActionFishingStart;
import com.marklynch.actions.ActionGiveItems;
import com.marklynch.actions.ActionHideInside;
import com.marklynch.actions.ActionLock;
import com.marklynch.actions.ActionReportCrime;
import com.marklynch.actions.ActionShoutForHelp;
import com.marklynch.actions.ActionTakeItems;
import com.marklynch.actions.ActionTalk;
import com.marklynch.actions.ActionThrowItem;
import com.marklynch.actions.ActionWrite;
import com.marklynch.ai.utils.AILine;
import com.marklynch.ai.utils.AIRoutineUtils;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Crime.TYPE;
import com.marklynch.level.constructs.Investigation;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.constructs.bounds.structure.StructureSection;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.constructs.conversation.Conversation;
import com.marklynch.level.constructs.conversation.ConversationPart;
import com.marklynch.level.constructs.conversation.ConversationResponse;
import com.marklynch.level.constructs.conversation.LeaveConversationListener;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.HOBBY;
import com.marklynch.objects.actors.AggressiveWildAnimal;
import com.marklynch.objects.actors.Animal;
import com.marklynch.objects.actors.CarnivoreNeutralWildAnimal;
import com.marklynch.objects.actors.Fish;
import com.marklynch.objects.actors.Guard;
import com.marklynch.objects.actors.HerbivoreWildAnimal;
import com.marklynch.objects.actors.Monster;
import com.marklynch.objects.actors.Pig;
import com.marklynch.objects.actors.TinyNeutralWildAnimal;
import com.marklynch.objects.actors.Trader;
import com.marklynch.objects.inanimateobjects.Carcass;
import com.marklynch.objects.inanimateobjects.Door;
import com.marklynch.objects.inanimateobjects.Food;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.HidingPlace;
import com.marklynch.objects.inanimateobjects.SmallHidingPlace;
import com.marklynch.objects.inanimateobjects.Storage;
import com.marklynch.objects.inanimateobjects.WantedPoster;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.tools.FishingRod;
import com.marklynch.objects.tools.Knife;
import com.marklynch.objects.utils.ThoughtBubbles;
import com.marklynch.ui.ActivityLog;
import com.marklynch.utils.CopyOnWriteArrayList;
import com.marklynch.utils.Color;
import com.marklynch.utils.MapUtil;

public abstract class AIRoutine {

	final static transient String ACTIVITY_DESCRIPTION_SHOPKEEPING = "Shopkeeping";
	final static transient String ACTIVITY_DESCRIPTION_FIGHTING = "Fighting";
	final static transient String ACTIVITY_DESCRIPTION_SEARCHING = "Searching";
	final static transient String ACTIVITY_DESCRIPTION_BEING_A_CHICKEN = "Being a chicken";
	final static transient String ACTIVITY_DESCRIPTION_RUNNING_AWAY = "Running away";
	final static transient String ACTIVITY_DESCRIPTION_SHOUTING_FOR_HELP = "Shouting for help";
	final static transient String ACTIVITY_DESCRIPTION_LOOTING = "Looting!";
	final static transient String ACTIVITY_DESCRIPTION_SKINNING = "Skinning";
	final static transient String ACTIVITY_DESCRIPTION_SELLING_LOOT = "Selling loot";
	final static transient String ACTIVITY_DESCRIPTION_BUYING_EQUIPMENT = "Buying equipment";
	final static transient String ACTIVITY_DESCRIPTION_FEEDING = "Feeding";
	final static transient String ACTIVITY_DESCRIPTION_SLEEPING = "Zzzzzz";
	final static transient String ACTIVITY_DESCRIPTION_HUNTING = "Goin' hunting";
	final static transient String ACTIVITY_DESCRIPTION_GOING_TO_BED = "Bed time";
	final static transient String ACTIVITY_DESCRIPTION_SWIMMING = "Swimming";
	final static transient String ACTIVITY_DESCRIPTION_MINING = "Mine Time";
	final static transient String ACTIVITY_DESCRIPTION_CHOPPING_WOODG = "Chopping Wood";
	final static transient String ACTIVITY_DESCRIPTION_RETREATING = "Retreating";
	final static transient String ACTIVITY_DESCRIPTION_HIDING = "Hiding";
	final static transient String ACTIVITY_DESCRIPTION_RINGING_DINNER_BELL = "Ringing Dinner Bell";
	final static transient String ACTIVITY_DESCRIPTION_PIGGING_OUT = "Pigging out!";
	final static transient String ACTIVITY_DESCRIPTION_BEING_A_PIG = "Being a pig";
	final static transient String ACTIVITY_DESCRIPTION_THIEVING = "Thieving!";
	final static transient String ACTIVITY_WANDERING = "Wandering";
	final static transient String ACTIVITY_DESCRIPTION_UPDATING_SIGN = "Updating Shop Sign";
	final static transient String ACTIVITY_DESCRIPTION_FOLLOWING = "Following";

	public transient static String STOP_THAT = "Stop that!";
	public transient static String TIME_TO_LEAVE = "It's getting late, time for you to go...";
	public transient static String I_SAW_THAT = "I saw that!!";

	public transient Actor actor;
	public transient GameObject target;

	public transient Square targetSquare = null;
	public transient int searchCooldown = 0;
	public transient GameObject searchCooldownActor = null;
	public transient int escapeCooldown = 0;
	public transient GameObject escapeCooldownAttacker = null;
	public transient int wokenUpCountdown = 0;

	public int sleepCounter = 0;
	final static int SLEEP_TIME = 1000;

	public enum STATE {
		HUNTING, MINING, GO_TO_WILD_ANIMAL_AND_ATTACK, GO_TO_WILD_ANIMAL_AND_LOOT, GO_TO_BED_AND_GO_TO_SLEEP, PATROL,
		FREE_TIME, FISHING, SHOPKEEPING, THIEVING, UPDATING_SIGN, SWIMMING, FOLLOWING, CHOPPING_WOOD
	};

	public transient STATE state;

	public static enum AI_TYPE {
		FIGHTER, RUNNER, GUARD, HOSTILE, ANIMAL
	};

	public transient AI_TYPE aiType = AI_TYPE.FIGHTER;

	public transient boolean keepInBounds = false;
	public transient CopyOnWriteArrayList<Area> areaBounds = new CopyOnWriteArrayList<Area>(Area.class);
	public transient CopyOnWriteArrayList<StructureSection> sectionBounds = new CopyOnWriteArrayList<StructureSection>(
			StructureSection.class);
	public transient CopyOnWriteArrayList<StructureRoom> roomBounds = new CopyOnWriteArrayList<StructureRoom>(StructureRoom.class);
	public transient CopyOnWriteArrayList<Square> squareBounds = new CopyOnWriteArrayList<Square>(Square.class);

	public transient HOBBY currentHobby = HOBBY.HUNTING;

	public transient Actor actorToKeepTrackOf = null;
	public transient Square lastLocationSeenActorToKeepTrackOf = null;
	public transient CopyOnWriteArrayList<GameObject> ignoreList = new CopyOnWriteArrayList<GameObject>(GameObject.class);

	public AIRoutine() {
	}

	public AIRoutine(Actor actor) {
		this.actor = actor;
	}

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = new Random().nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public void update() {
	}

	public void createSearchLocationsBasedOnVisibleAttackers() {

		if (actor.sleeping)
			return;

		// Check for enemies last seen locations to search
		if (this.actor.hasAttackers()) {
			for (GameObject attacker : this.actor.getAttackers()) {
				if (this.actor.canSeeGameObject(attacker)) {
					this.actor.addInvestigation(attacker, attacker.squareGameObjectIsOn,
							Investigation.INVESTIGATION_PRIORITY_ATTACKED);
				}
			}
		}

	}

	public void createSearchLocationsBasedOnVisibleCriminals() {

		if (actor.sleeping)
			return;

		// Check for enemies last seen locations to search
		for (Actor criminal : this.actor.knownCriminals) {

			for (Crime crime : actor.mapActorToCrimesWitnessed.get(criminal)) {
				if (!crime.isResolved()) {
					if (this.actor.canSeeGameObject(criminal)) {
						this.actor.addInvestigation(criminal, criminal.squareGameObjectIsOn,
								Investigation.INVESTIGATION_PRIORITY_CRIME_SEEN);
					}
				}
			}
		}

	}

	// public void witnessCrimes() {
	//
	// }
	//
	// public void updateListOfCrimesWitnessed() {
	// int searchBoxX1 = actor.squareGameObjectIsOn.xInGrid - actor.sight;
	// if (searchBoxX1 < 0)
	// searchBoxX1 = 0;
	//
	// int searchBoxX2 = actor.squareGameObjectIsOn.xInGrid + actor.sight;
	// if (searchBoxX2 > Game.level.width - 1)
	// searchBoxX2 = Game.level.width - 1;
	//
	// int searchBoxY1 = actor.squareGameObjectIsOn.yInGrid - actor.sight;
	// if (searchBoxY1 < 0)
	// searchBoxY1 = 0;
	//
	// int searchBoxY2 = actor.squareGameObjectIsOn.yInGrid + actor.sight;
	// if (searchBoxY2 > Game.level.height - 1)
	// searchBoxY2 = Game.level.height - 1;
	//
	// for (int i = searchBoxX1; i <= searchBoxX2; i++) {
	// for (int j = searchBoxY1; j <= searchBoxY2; j++) {
	// Actor actor = (Actor)
	// Game.level.squares[i][j].inventory.getGameObjectOfClass(Actor.class);
	// if (actor != null) {
	// for (Action action : actor.actions) {
	// if (action.legal == false) {
	// if (this.actor.straightLineDistanceTo(actor.squareGameObjectIsOn) <=
	// this.actor.sight
	// && this.actor.visibleFrom(actor.squareGameObjectIsOn)) {
	//
	// }
	// }
	// }
	// }
	// }
	// }
	// }

	public static boolean escapeFromAttackerToSmallHidingPlace(GameObject attacker) {

		// Go to burrow and hide if can
		SmallHidingPlace smallHidingPlace = (SmallHidingPlace) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(20f,
				false, false, false, false, 0, false, true, SmallHidingPlace.class);
		if (smallHidingPlace != null) {

			if (Game.level.activeActor.straightLineDistanceTo(smallHidingPlace.squareGameObjectIsOn) < 2) {
				new ActionHideInside(Game.level.activeActor, smallHidingPlace).perform();
			} else {
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// AIRoutineUtils.moveTowardsTargetToBeOn(smallHidingPlace);
			}
			return true;
		}
		return false;
	}

	public boolean runSleepRoutine() {

		if (state != STATE.GO_TO_BED_AND_GO_TO_SLEEP) {
			if (actor.sleeping && Game.level.shouldLog(actor))
				Game.level.logOnScreen(new ActivityLog(new Object[] { actor, " woke up" }));
			actor.sleeping = false;
			return false;
		}

		if (this.actor.groupOfActors != null && this.actor != this.actor.groupOfActors.getLeader()
				&& this.actor.groupOfActors.getLeader().followersShouldFollow == true) {
			if (actor.sleeping && Game.level.shouldLog(actor))
				Game.level.logOnScreen(new ActivityLog(new Object[] { actor, " woke up" }));
			actor.sleeping = false;
			return false;
		}

		if (actor.sleeping) {
			this.actor.followersShouldFollow = false;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_SLEEPING;
			Game.level.activeActor.thoughtBubbleImageTextureObject = Action.textureSleep;
			return true;
		}

		return false;
	}

	public boolean runFightRoutine(boolean allowedMove) {
		boolean attacked = false;
		boolean moved = false;

		// 1. Fighting
		if (this.actor.hasAttackers()) {

			// sort by best attack option (walk distance, dmg you can do)
			this.actor.getAttackers().sort(AIRoutineUtils.sortAttackers);

			// Go through attackers list
			for (GameObject victimToAttackAttemptPreMove : this.actor.getAttackers()) {

				// If we can see the attacker go for them
				if (this.actor.canSeeGameObject(victimToAttackAttemptPreMove)) {
					target = victimToAttackAttemptPreMove;

					// If it's a hiding place we're planning on attacking,
					// we're searching
					if (target instanceof HidingPlace) {
						this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
					} else {
						this.actor.activityDescription = ACTIVITY_DESCRIPTION_FIGHTING;
					}

					// Try to attack the preference 1 target
					if (target != null) {
						attacked = AIRoutineUtils.attackTarget(target);
						// If you can't attack preference 1 target, move towards
						// them
						if (!attacked && allowedMove) {
							moved = AIRoutineUtils.moveTowards(target);
							if (moved)
								break;
						}
					}

					actor.aiLine = new AILine(AILine.AILineType.AI_LINE_TYPE_ATTACK, actor,
							target.squareGameObjectIsOn);

				}

				if (moved || attacked)
					break;
			}
		}

		// If not targeting a hiding place, hiding places from list
		if (!(target instanceof HidingPlace)) {
			actor.removeHidingPlacesFromAttackersList();
		}

		// Return whether we did anything or not
		if (moved || attacked) {
			createSearchLocationsBasedOnVisibleAttackers();
			this.actor.followersShouldFollow = true;
			this.actor.thoughtBubbleImageTextureObject = target.imageTexture;
			this.actor.thoughtBubbleImageTextureAction = Action.textureAttack;
			return true;
		} else
			return false;
	}

	public boolean runGetHelpRoutine() {
		boolean moved = false;

		actor.removeHidingPlacesFromAttackersList();
		if (this.actor.hasAttackers()) {

			// sort by best attack option (walk distance, dmg you can do)
			this.actor.getAttackers().sort(AIRoutineUtils.sortAttackers);

			// Go through attackers list
			for (GameObject attacker : this.actor.getAttackers()) {

				// If we can see the attacker go for them
				if (this.actor.canSeeGameObject(attacker)) {
					new ActionShoutForHelp(actor, attacker).perform();
					this.actor.activityDescription = ACTIVITY_DESCRIPTION_SHOUTING_FOR_HELP;

					Actor actorNearby = (Actor) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(20, false, false,
							false, false, 0, false, true, Guard.class);

					// if (this.actor.canSeeGameObject(actorNearby)) {
					// } else {
					if (actorNearby != null) {
						AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
						// AIRoutineUtils.moveTowardsSquareToBeAdjacent(actorNearby.squareGameObjectIsOn);
						moved = true;
						this.escapeCooldownAttacker = attacker;

						actor.aiLine = new AILine(AILine.AILineType.AI_LINE_TYPE_ESCAPE, actor,
								actorNearby.squareGameObjectIsOn);
					}
					// }

				}

				if (moved)
					break;

			}
		}

		// Return whether we did anything or not
		if (moved) {
			// shoutForHelpCooldown = 10;
			createSearchLocationsBasedOnVisibleAttackers();
			escapeCooldown = 10;
			this.actor.thoughtBubbleImageTextureObject = Action.textureHelp;
			return true;
		} else {
			return runEscapeRoutine();
		}
	}

	public boolean runEscapeRoutine() {
		boolean canSeeAttacker = false;

		actor.removeHidingPlacesFromAttackersList();

		// 1. Fighting
		if (this.actor.hasAttackers()) {

			// sort by best attack option (walk distance, dmg you can do)
			this.actor.getAttackers().sort(AIRoutineUtils.sortAttackers);

			// Go through attackers list
			for (GameObject attackerToRunFrom : this.actor.getAttackers()) {

				// If we can see the attacker go for them
				if (this.actor.canSeeGameObject(attackerToRunFrom)) {
					canSeeAttacker = true;

					// Try to attack the preference 1 target
					if (attackerToRunFrom != null) {
						if (actor instanceof HerbivoreWildAnimal) {

							if (escapeFromAttackerToSmallHidingPlace(attackerToRunFrom)) {
								// Successfully ran towards burrow
							} else {
								AIRoutineUtils.escapeFromAttacker(attackerToRunFrom);
							}
						} else {
							AIRoutineUtils.escapeFromAttacker(attackerToRunFrom);
						}
					}

					actor.aiLine = new AILine(AILine.AILineType.AI_LINE_TYPE_ESCAPE, actor,
							attackerToRunFrom.squareGameObjectIsOn);
				}

				if (canSeeAttacker)
					break;
			}
		}

		// Return whether we did anything or not
		if (canSeeAttacker) {
			if (actor instanceof Pig) {
				actor.activityDescription = ACTIVITY_DESCRIPTION_BEING_A_CHICKEN;
			} else {
				actor.activityDescription = ACTIVITY_DESCRIPTION_RUNNING_AWAY;
			}
			escapeCooldown = 10;

			createSearchLocationsBasedOnVisibleAttackers();
			this.actor.thoughtBubbleImageTextureObject = Action.textureHelp;
			return true;
		} else {
			return false;
		}
	}

	public boolean runEscapeCooldown(boolean shout) {

		if (escapeCooldown <= 0)
			return false;

		escapeCooldown--;

		// DOORWAYS are my biggest issue here.
		if (Game.level.activeActor instanceof Pig)
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_BEING_A_CHICKEN;
		else if (shout) {
			new ActionShoutForHelp(actor, escapeCooldownAttacker).perform();
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_SHOUTING_FOR_HELP;
		} else {
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_RUNNING_AWAY;
		}

		// Move Away From Last Square;
		boolean moved = false;
		if (actor.squareGameObjectIsOn.xInGrid > actor.lastSquare.xInGrid) {
			Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareToRightOf();
			if (squareInBounds(squareToMoveTo))
				moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
		} else if (actor.squareGameObjectIsOn.xInGrid < actor.lastSquare.xInGrid) {
			Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareToLeftOf();
			if (squareInBounds(squareToMoveTo))
				moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
		} else {
			if (actor.squareGameObjectIsOn.yInGrid > actor.lastSquare.yInGrid) {
				Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareBelow();
				if (squareInBounds(squareToMoveTo))
					moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
			} else if (actor.squareGameObjectIsOn.yInGrid < actor.lastSquare.yInGrid) {
				Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareAbove();
				if (squareInBounds(squareToMoveTo))
					moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
			} else {

			}
		}

		createSearchLocationsBasedOnVisibleAttackers();
		this.actor.thoughtBubbleImageTextureObject = Action.textureHelp;
		return true;
	}

	public boolean runSearchRoutine() {
		// Searching
		if (this.actor.investigationsMap.size() == 0)
			return false;

		// Remove dead objects
		// and remove gameObjects you can see from investigation list
		// and remove out of bounds squares from investigation list
		CopyOnWriteArrayList<GameObject> gameObjectsToStopSearchingFor = new CopyOnWriteArrayList<GameObject>(GameObject.class);
		for (GameObject actorToSearchFor : this.actor.investigationsMap.keySet()) {
			if (actorToSearchFor.remainingHealth <= 0) {
				gameObjectsToStopSearchingFor.add(actorToSearchFor);
			} else if (actorToSearchFor.squareGameObjectIsOn == null) {
				gameObjectsToStopSearchingFor.add(actorToSearchFor);
			} else if (this.actor.canSeeGameObject(actorToSearchFor)) {
				gameObjectsToStopSearchingFor.add(actorToSearchFor);
			} else if (!squareInBounds(actorToSearchFor.squareGameObjectIsOn)) {
				gameObjectsToStopSearchingFor.add(actorToSearchFor);
			}
		}
		for (GameObject gameObjectToRemove : gameObjectsToStopSearchingFor) {
			this.actor.investigationsMap.remove(gameObjectToRemove);
		}

		// Sort list by priority
		MapUtil.sortByValue(this.actor.investigationsMap);

		// this.actor.locationsToSearch.sort(AIRoutineUtils.sortLocationsToSearch);
		CopyOnWriteArrayList<GameObject> toRemove = new CopyOnWriteArrayList<GameObject>(GameObject.class);
		boolean moved = false;

		Square searchSquare = null;

		for (GameObject actorToSearchFor : this.actor.investigationsMap.keySet()) {

			// If you're within 2 squares and can see the target actor, remove
			// from search list
			// if
			// (this.actor.straightLineDistanceTo(actorToSearchFor.squareGameObjectIsOn)
			// <= 2
			// && this.actor.canSeeGameObject(actorToSearchFor)) {
			// searchCooldown = 0;
			// toRemove.add(actorToSearchFor);
			// continue;
			// }

			searchSquare = this.actor.investigationsMap.get(actorToSearchFor).square;
			searchCooldownActor = actorToSearchFor;
			searchCooldown = 10;

			// moce towards search square
			if (this.actor.squareGameObjectIsOn.straightLineDistanceTo(searchSquare) > 1
					&& this.actor.getPathTo(searchSquare) != null) {
				this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
				this.actor.thoughtBubbleImageTextureObject = ThoughtBubbles.QUESTION_MARK;
				moved = AIRoutineUtils.moveTowardsTargetSquare(searchSquare);

				break;

			}

			if (moved)
				break;

			if (this.actor.squareGameObjectIsOn.straightLineDistanceTo(searchSquare) <= 1) {
				toRemove.add(actorToSearchFor);
				break;
			}

			// distance 1 to search square
			for (Square searchSquareAtDistanceOne : searchSquare.getAllSquaresAtDistance(1)) {
				if (this.actor.squareGameObjectIsOn.straightLineDistanceTo(searchSquare) > 1
						&& this.actor.getPathTo(searchSquareAtDistanceOne) != null) {
					this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
					this.actor.thoughtBubbleImageTextureObject = ThoughtBubbles.QUESTION_MARK;
					moved = AIRoutineUtils.moveTowardsTargetSquare(searchSquareAtDistanceOne);

					break;

				}
			}

			if (moved)
				break;

			if (this.actor.squareGameObjectIsOn.straightLineDistanceTo(searchSquare) <= 2) {
				toRemove.add(actorToSearchFor);
				break;
			}

			// distance 2
			for (Square searchSquareAtDistanceTwo : searchSquare.getAllSquaresAtDistance(2)) {
				if (this.actor.getPathTo(searchSquareAtDistanceTwo) != null) {
					this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
					this.actor.thoughtBubbleImageTextureObject = ThoughtBubbles.QUESTION_MARK;
					moved = AIRoutineUtils.moveTowardsTargetSquare(searchSquareAtDistanceTwo);
					break;

				}
			}

			if (moved)
				break;

			toRemove.add(actorToSearchFor);
		}

		for (GameObject actorsToRemoveFromList : toRemove) {
			this.actor.investigationsMap.remove(actorsToRemoveFromList);
		}

		if (moved) {

			for (GameObject attacker : this.actor.getAttackers()) {
				if (this.actor.canSeeGameObject(attacker)) {
					// Change status to fighting if u can see an enemy from
					// new location
					this.actor.thoughtBubbleImageTextureObject = null;
					if (target instanceof HidingPlace) {
						this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
					} else
						this.actor.activityDescription = ACTIVITY_DESCRIPTION_FIGHTING;
					break;
				}
			}

			actor.aiLine = new AILine(AILine.AILineType.AI_LINE_TYPE_SEARCH, actor, searchSquare);
			createSearchLocationsBasedOnVisibleAttackers();
			return true;
		}
		return false;

	}

	public boolean runSearchCooldown() {

		if (searchCooldown <= 0)
			return false;

		searchCooldown--;

		// DOORWAYS are my biggest issue here.
		this.actor.activityDescription = ACTIVITY_DESCRIPTION_SEARCHING;
		this.actor.thoughtBubbleImageTextureObject = ThoughtBubbles.QUESTION_MARK;

		// if (room != null) {
		// AIRoutineUtils.moveTowardsTargetSquare(AIRoutineUtils.getRandomSquareInRoom(room));
		// return true;
		// } else {
		if (this.searchCooldownActor != null) {
			if (this.actor.canSeeGameObject(searchCooldownActor)) {
				searchCooldown = 0;
				return false;
			}
		}

		// Move Away From Last Square;
		boolean moved = false;
		if (actor.squareGameObjectIsOn.xInGrid > actor.lastSquare.xInGrid) {
			Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareToRightOf();
			if (squareInBounds(squareToMoveTo))
				moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
		} else if (actor.squareGameObjectIsOn.xInGrid < actor.lastSquare.xInGrid) {
			Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareToLeftOf();
			if (squareInBounds(squareToMoveTo))
				moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
		} else {
			if (actor.squareGameObjectIsOn.yInGrid > actor.lastSquare.yInGrid) {
				Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareBelow();
				if (squareInBounds(squareToMoveTo))
					moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
			} else if (actor.squareGameObjectIsOn.yInGrid < actor.lastSquare.yInGrid) {
				Square squareToMoveTo = actor.squareGameObjectIsOn.getSquareAbove();
				if (squareInBounds(squareToMoveTo))
					moved = AIRoutineUtils.moveTowardsTargetSquare(squareToMoveTo);
			} else {

			}
		}

		if (!moved) {
			Square randomSquareToMoveTo = AIRoutineUtils.getRandomAdjacentSquare(actor.squareGameObjectIsOn);
			if (squareInBounds(randomSquareToMoveTo))
				moved = AIRoutineUtils.moveTowardsTargetSquare(randomSquareToMoveTo);
		}
		// }

		if (moved) {
			createSearchLocationsBasedOnVisibleAttackers();
			return true;
		} else {
			return false;
		}
	}

	protected boolean runCrimeReactionRoutine() {
		for (final Actor criminal : actor.knownCriminals) {
			int accumulatedSeverity = 0;
			final CopyOnWriteArrayList<Crime> unresolvedIllegalMinings = new CopyOnWriteArrayList<Crime>(Crime.class);
			final CopyOnWriteArrayList<Crime> unresolvedThefts = new CopyOnWriteArrayList<Crime>(Crime.class);
			final CopyOnWriteArrayList<Crime> unresolvedCrimes = new CopyOnWriteArrayList<Crime>(Crime.class);
			final CopyOnWriteArrayList<GameObject> stolenItemsOnCriminal = new CopyOnWriteArrayList<GameObject>(GameObject.class);
			final CopyOnWriteArrayList<GameObject> stolenItemsEquippedByCriminal = new CopyOnWriteArrayList<GameObject>(GameObject.class);
			final CopyOnWriteArrayList<GameObject> stolenItemsOnGroundToPickUp = new CopyOnWriteArrayList<GameObject>(GameObject.class);
			boolean newCrime = false;
			// final CopyOnWriteArrayList<GameObject> stolenItemsOnInContainer = new
			// CopyOnWriteArrayList<GameObject>();

			// Mark issues as resolved
			for (Crime crime : actor.mapActorToCrimesWitnessed.get(criminal)) {
				if (crime.isResolved() == false) {

					if (crime.stolenItems.length == 0) {
						crime.resolve();
						continue;
					}
					boolean itemsToBeRetaken = false;
					for (GameObject stolenItem : crime.stolenItems) {
						if (criminal.inventory.contains(stolenItem) || stolenItem.squareGameObjectIsOn != null) {
							itemsToBeRetaken = true;
						}
					}
					if (itemsToBeRetaken) {

					} else {
						crime.resolve();
					}
				}
			}

			// Create list of unresolved crimes, stolenItems
			// Also calculate accumulated severity of crimes
			for (Crime crime : actor.mapActorToCrimesWitnessed.get(criminal)) {
				accumulatedSeverity += crime.type.severity;
				if (crime.isResolved() == false) {
					unresolvedCrimes.add(crime);
					if (crime.type == TYPE.CRIME_ILLEGAL_MINING) {
						unresolvedIllegalMinings.add(crime);
					}
					if (crime.stolenItems.length != 0)
						unresolvedThefts.add(crime);
					for (GameObject stolenItem : crime.stolenItems) {
						if (criminal.inventory.contains(stolenItem)) {
							stolenItemsOnCriminal.add(stolenItem);
						} else if (criminal.equipped == stolenItem) {
							stolenItemsEquippedByCriminal.add(stolenItem);
						} else if (stolenItem.squareGameObjectIsOn != null && stolenItem.fitsInInventory) {
							stolenItemsOnGroundToPickUp.add(stolenItem);
						}
					}

				}
			}

			// "STOP THAT!"
			boolean saidStop = false;
			for (Crime crime : actor.mapActorToCrimesWitnessed.get(criminal)) {
				if (!crime.hasBeenToldToStop) {
					if (criminal == Game.level.player) {
						new ActionTalk(this.actor, criminal, createJusticeStopConversation(crime)).perform();
					} else {
						if (Game.level.shouldLog(this.actor)) {

							if (crime.type == Crime.TYPE.CRIME_TRESPASSING_LEEWAY)
								actor.setMiniDialogue(TIME_TO_LEAVE, criminal);
							else if (crime.type == Crime.TYPE.CRIME_THEFT)
								actor.setMiniDialogue(I_SAW_THAT, criminal);
							else
								actor.setMiniDialogue(STOP_THAT, criminal);
						}

					}
					actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
					// actor.activityDescription = "Dispensing Justice";

					saidStop = true;
					break;
				}

			}
			if (saidStop) {
				for (Crime crime : criminal.crimesPerformedInLifetime) {
					crime.hasBeenToldToStop = true;
				}
			}

			if (accumulatedSeverity >= 10) {
				actor.addAttackerForNearbyFactionMembersIfVisible(criminal);
				actor.addAttackerForThisAndGroupMembers(criminal);
				for (Crime unresolvedCrime : unresolvedCrimes) {
					unresolvedCrime.resolve();
				}
				return true;

				// Illegal mining
			} else if (unresolvedIllegalMinings.size() > 0) {

				if (criminal == Game.level.player) {
					new ActionTalk(this.actor, criminal, createConversationMyOres()).perform();
				} else {
					actor.setMiniDialogue("MY ORES!", criminal);
				}
				new ActionThrowItem(actor, criminal, Templates.ROCK.makeCopy(null, null)).perform();
				for (GameObject stolenItem : stolenItemsOnCriminal) {
					if (criminal.inventory.contains(stolenItem)) {
						new ActionDropItems(criminal, criminal.squareGameObjectIsOn, stolenItem).perform();
						this.actor.thoughtBubbleImageTextureObject = stolenItem.imageTexture;
					}
				}
				for (Crime unresolvedCrime : unresolvedIllegalMinings) {
					unresolvedCrime.resolve();
				}
				// actor.thoughtBubbleImageTexture = ThoughtBubbles.JUSTICE;
				return true;
				// Stolen items in criminal inventory
			} else if (stolenItemsOnCriminal.size() > 0) {
				if (actor.straightLineDistanceTo(criminal.squareGameObjectIsOn) <= 2) {
					if (criminal == Game.level.player) {
						new ActionTalk(this.actor, criminal,
								createJusticeReclaimConversation(this.actor, criminal, stolenItemsOnCriminal))
										.perform();
					} else {
						actor.setMiniDialogue("Give me that!", criminal);
						for (GameObject stolenItemOnCriminal : stolenItemsOnCriminal) {
							new ActionGiveItems(criminal, actor, true, stolenItemOnCriminal).perform();
						}
					}
					actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
					actor.activityDescription = "Confiscating";
					return true;
				}

				if (actor.sight > actor.straightLineDistanceTo(criminal.squareGameObjectIsOn)
						&& actor.canSeeGameObject(criminal)) {

					if (AIRoutineUtils.moveTowards(criminal.squareGameObjectIsOn)) {
						actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
						actor.activityDescription = "Confiscating";
						createSearchLocationsBasedOnVisibleAttackers();
						return true;
					}
				}

				// Stolen items equipped by criminal but not in inventory
				// This is for large objects that dont fit in inventory
			} else if (stolenItemsEquippedByCriminal.size() > 0) {
				if (actor.straightLineDistanceTo(criminal.squareGameObjectIsOn) == 1) {
					new ActionTalk(this.actor, criminal,
							createJusticeDropConversation(criminal, stolenItemsEquippedByCriminal)).perform();
					actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
					return true;
				}

				if (actor.sight > actor.straightLineDistanceTo(criminal.squareGameObjectIsOn)
						&& actor.canSeeGameObject(criminal)) {
					if (AIRoutineUtils.moveTowards(criminal.squareGameObjectIsOn)) {
						actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
						createSearchLocationsBasedOnVisibleAttackers();
						return true;
					}
				}

				// Stolen items on ground
			} else if (stolenItemsOnGroundToPickUp.size() > 0) {
				for (GameObject stolenItemOnGround : stolenItemsOnGroundToPickUp) {
					if (actor.straightLineDistanceTo(stolenItemOnGround.squareGameObjectIsOn) == 1) {
						new ActionTakeItems(this.actor, stolenItemOnGround.squareGameObjectIsOn, stolenItemOnGround)
								.perform();
						actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
						return true;
					}

					if (actor.canSeeSquare(stolenItemOnGround.squareGameObjectIsOn)) {
						if (AIRoutineUtils.moveTowards(stolenItemOnGround.squareGameObjectIsOn)) {
							actor.thoughtBubbleImageTextureObject = ThoughtBubbles.JUSTICE;
							createSearchLocationsBasedOnVisibleAttackers();
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public Conversation createJusticeStopConversation(Crime crime) {
		ConversationResponse done = new ConversationResponse(":/", null);
		ConversationPart conversationPartJustice = null;

		if (crime.type == Crime.TYPE.CRIME_TRESPASSING_LEEWAY) {
			conversationPartJustice = new ConversationPart(new Object[] { TIME_TO_LEAVE },
					new ConversationResponse[] { done }, this.actor);

		} else if (crime.type == Crime.TYPE.CRIME_THEFT) {
			conversationPartJustice = new ConversationPart(new Object[] { I_SAW_THAT },
					new ConversationResponse[] { done }, this.actor);

		} else {
			conversationPartJustice = new ConversationPart(new Object[] { STOP_THAT },
					new ConversationResponse[] { done }, this.actor);

		}

		return new Conversation(conversationPartJustice, actor, true);

	}

	public static Conversation createJusticeReclaimConversation(final Actor accuser, final Actor criminal,
			final CopyOnWriteArrayList<GameObject> stolenItemsOnCriminal) {
		ConversationResponse accept = new ConversationResponse("Comply [Give items]", null) {
			@Override
			public void select() {
				super.select();
				for (GameObject stolenItemOnCriminal : stolenItemsOnCriminal) {
					new ActionGiveItems(criminal, accuser, true, stolenItemOnCriminal).perform();

				}
				for (Crime crime : criminal.crimesPerformedInLifetime) {
					crime.resolve();
				}
			}
		};
		ConversationResponse refuse = new ConversationResponse("Refuse", null) {
			@Override
			public void select() {
				super.select();
				accuser.addAttackerForNearbyFactionMembersIfVisible(criminal);
				accuser.addAttackerForThisAndGroupMembers(criminal);
			}
		};

		Object[] demand = new Object[] {};
		if (stolenItemsOnCriminal.size() == 1) {
			if (stolenItemsOnCriminal.get(0).owner == accuser) {
				demand = new Object[] { "That's my ", stolenItemsOnCriminal.get(0), ", give it back!" };
			} else {
				demand = new Object[] { "Give me that ", stolenItemsOnCriminal.get(0), "!" };
			}
		} else {
			CopyOnWriteArrayList<Object> demandCopyOnWriteArrayList = new CopyOnWriteArrayList<Object>(Object.class);
			for (int i = 0; i < stolenItemsOnCriminal.size(); i++) {
				if (i == 0) {
					// first item
					demandCopyOnWriteArrayList.add("Give me that ");
					demandCopyOnWriteArrayList.add(stolenItemsOnCriminal.get(i));
				} else if (i == stolenItemsOnCriminal.size() - 1) {
					// last item
					demandCopyOnWriteArrayList.add(" and ");
					demandCopyOnWriteArrayList.add(stolenItemsOnCriminal.get(i));
					demandCopyOnWriteArrayList.add("!");
				} else {
					// middle items
					demandCopyOnWriteArrayList.add(", ");
					demandCopyOnWriteArrayList.add(stolenItemsOnCriminal.get(i));
				}
			}

			demand = demandCopyOnWriteArrayList.toArray();
		}

		ConversationPart conversationPartJustice = new ConversationPart(demand,
				new ConversationResponse[] { accept, refuse }, accuser);
		conversationPartJustice.leaveConversationListener = new LeaveConversationListener() {

			@Override
			public void leave() {
				accuser.addAttackerForNearbyFactionMembersIfVisible(criminal);
				accuser.addAttackerForThisAndGroupMembers(criminal);
			}

		};
		for (Crime crime : criminal.crimesPerformedInLifetime) {
			crime.hasBeenToldToStop = true;
		}
		return new Conversation(conversationPartJustice, accuser, true);

	}

	public Conversation createJusticeDropConversation(final Actor criminal,
			final CopyOnWriteArrayList<GameObject> stolenItemsEquippedByCriminal) {
		ConversationResponse accept = new ConversationResponse("Comply", null) {
			@Override
			public void select() {
				super.select();
				for (GameObject stolenItemOnCriminal : stolenItemsEquippedByCriminal) {
					new ActionDropItems(criminal, criminal.squareGameObjectIsOn, stolenItemOnCriminal).perform();
				}
				for (Crime crime : criminal.crimesPerformedInLifetime) {
					crime.resolve();
				}
			}
		};
		ConversationResponse refuse = new ConversationResponse("Refuse", null) {
			@Override
			public void select() {
				super.select();
				actor.addAttackerForNearbyFactionMembersIfVisible(criminal);
				actor.addAttackerForThisAndGroupMembers(criminal);
			}
		};

		Object[] demand = new Object[] {};
		demand = new Object[] { "Drop that ", stolenItemsEquippedByCriminal.get(0), "!" };

		ConversationPart conversationPartJustice = new ConversationPart(demand,
				new ConversationResponse[] { accept, refuse }, this.actor);

		conversationPartJustice.leaveConversationListener = new LeaveConversationListener() {

			@Override
			public void leave() {
				actor.addAttackerForNearbyFactionMembersIfVisible(criminal);
				actor.addAttackerForThisAndGroupMembers(criminal);
			}

		};

		for (Crime crime : criminal.crimesPerformedInLifetime) {
			crime.hasBeenToldToStop = true;
		}

		return new Conversation(conversationPartJustice, actor, true);

	}

	private Conversation createConversationMyOres() {

		ConversationPart conversationPartYouWontGetOut = new ConversationPart(new Object[] { "My Ores!" },
				new ConversationResponse[] {}, actor);

		return new Conversation(conversationPartYouWontGetOut, actor, true);

	}

	public boolean deferToGroupLeader() {

		if (this.actor.groupOfActors != null && this.actor != this.actor.groupOfActors.getLeader()
				&& this.actor.groupOfActors.getLeader().followersShouldFollow == true) {
			if (this.actor.groupOfActors.update(this.actor)) {
				this.actor.thoughtBubbleImageTextureAction = this.actor.groupOfActors.leader.thoughtBubbleImageTextureAction;
				this.actor.thoughtBubbleImageTextureObject = this.actor.groupOfActors.leader.thoughtBubbleImageTextureObject;
				return true;
			}
		}
		return false;
	}

	public boolean deferToQuest() {
		if (this.actor.quest != null) {
			if (this.actor.quest.update(this.actor)) {
				return true;
			}
		}
		return false;
	}

	public boolean lootCarcass() {

		// 1. loot carcasses
		GameObject carcass = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(9f, false, true, true, true, 0, false,
				false, Animal.class);
		if (carcass != null) {
			this.actor.thoughtBubbleImageTextureObject = carcass.imageTexture;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_LOOTING;
			boolean lootedCarcass = AIRoutineUtils.lootTarget(carcass);
			if (!lootedCarcass) {
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// AIRoutineUtils.moveTowardsSquareToBeAdjacent(carcass.squareGameObjectIsOn);
			} else {
			}
			return true;
		}

		return false;
	}

	public boolean skinCarcass() {

		if (!actor.inventory.containsGameObjectOfType(Knife.class))
			return false;

		// 1. loot carcasses
		GameObject carcass = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(9f, false, false, true, true, 0, false,
				true, Carcass.class);

		if (carcass != null) {

			this.actor.thoughtBubbleImageTextureObject = carcass.imageTexture;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_SKINNING;
			boolean lootedCarcass = AIRoutineUtils.skinTarget(carcass);
			if (!lootedCarcass) {
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// AIRoutineUtils.moveTowardsSquareToBeAdjacent(carcass.squareGameObjectIsOn);
			} else {
			}
			return true;
		}

		return false;
	}

	public boolean lootFromGround() {
		// Pick up loot on ground
		GameObject loot = null;
		loot = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(9f, true, false, true, true, 10, false, true,
				GameObject.class);

		if (loot != null) {
			target = loot;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_LOOTING;
			this.actor.thoughtBubbleImageTextureObject = loot.imageTexture;
			this.actor.thoughtBubbleImageTextureAction = Action.textureLeft;

			boolean pickedUpLoot = AIRoutineUtils.pickupTarget(loot);
			if (!pickedUpLoot) {
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// AIRoutineUtils.moveTowardsSquareToBeAdjacent(loot.squareGameObjectIsOn);
			} else {

			}
			return true;
		}
		return false;
	}

	public boolean eatFoodOnGround() {
		GameObject food = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(5f, true, false, false, false, 0, false,
				false, Food.class);
		if (food != null) {
			target = food;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_FEEDING;
			this.actor.thoughtBubbleImageTextureObject = food.imageTexture;
			boolean ateFood = AIRoutineUtils.eatTarget(food);
			if (!ateFood) {
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// AIRoutineUtils.moveTowardsSquareToBeAdjacent(food.squareGameObjectIsOn);
			} else {

			}
			return true;
		}
		return false;
	}

	public boolean eatCarcassOnGround() {
		GameObject corpse = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(5f, true, false, false, false, 0, false,
				false, Animal.class);
		if (corpse != null) {
			target = corpse;
			this.actor.activityDescription = ACTIVITY_DESCRIPTION_FEEDING;
			this.actor.thoughtBubbleImageTextureObject = corpse.imageTexture;
			boolean ateCorpse = AIRoutineUtils.eatTarget(corpse);
			if (!ateCorpse) {
				// Object o = AIRoutineUtils.tempPath;
				AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
				// new ActionMove(Game.level.activeActor,
				// AIRoutineUtils.tempPath.squares.get(0), true).perform();
				// AIRoutineUtils.moveTowardsSquareToBeAdjacent(corpse.squareGameObjectIsOn);
			} else {

			}
			return true;
		}
		return false;
	}

	public void aiRoutineStart() {
		this.actor.followersShouldFollow = false;
		this.actor.aiLine = null;
		this.actor.miniDialogue = null;
		this.actor.activityDescription = null;
		this.actor.thoughtBubbleImageTextureActionColor = Color.WHITE;
		this.actor.thoughtBubbleImageTextureObject = null;
		this.actor.thoughtBubbleImageTextureAction = null;
		if (actor instanceof Animal || actor instanceof Monster) {

		} else {
			createSearchLocationsBasedOnVisibleCriminals();
		}
		createSearchLocationsBasedOnVisibleAttackers();
		if (actor.groupOfActors != null)
			state = actor.groupOfActors.getLeader().aiRoutine.state;
		if (wokenUpCountdown > 0) {
			wokenUpCountdown--;
		}
	}

	// public
	public boolean keepTrackOf(Actor target) {

		// If the actor is in the attackers list they will be handled by
		// fightRoutine/searchRoutine
		if (actor.attackers.contains(target))
			return false;
		// Can mort see the Player in his territory? If so record it. If not,
		// follow.

		if (target != actorToKeepTrackOf) {
			actorToKeepTrackOf = target;
			lastLocationSeenActorToKeepTrackOf = null;
		}

		if (!actor.canSeeGameObject(target)) {

			if (lastLocationSeenActorToKeepTrackOf != null) {
				AIRoutineUtils.moveTowardsTargetSquare(lastLocationSeenActorToKeepTrackOf);

				if (actor.squareGameObjectIsOn == lastLocationSeenActorToKeepTrackOf) {
					this.actor.addInvestigation(target, lastLocationSeenActorToKeepTrackOf,
							Investigation.INVESTIGATION_PRIORITY_KEEP_TRACK);
					lastLocationSeenActorToKeepTrackOf = null;
				}

				if (actor.canSeeGameObject(target)) {
					actor.investigationsMap.remove(target);
					lastLocationSeenActorToKeepTrackOf = target.squareGameObjectIsOn;
				}

				return true;
			}
		} else {
			actor.investigationsMap.remove(target);
			lastLocationSeenActorToKeepTrackOf = target.squareGameObjectIsOn;
		}
		return false;
	}

	public boolean squareInBounds(Square square) {

		// Return true if there are no bounds
		if (keepInBounds == false)
			return true;

		if (square == null)
			return false;

		for (Area area : areaBounds) {
			if (square.areaSquareIsIn == area) {
				return true;
			}
		}

		for (StructureSection section : sectionBounds) {
			if (square.structureSectionSquareIsIn == section) {
				return true;
			}
		}

		for (StructureRoom room : roomBounds) {
			if (square.structureRoomSquareIsIn == room) {
				return true;
			}
		}

		for (Square legitSquare : squareBounds) {
			if (square == legitSquare) {
				return true;
			}
		}
		return false;

	}

	public boolean runDoorRoutine() {
		for (Door door : actor.doors) {

			if (door.locked == false && door.shouldBeLocked() && actor.hasKeyForDoor(door)
					&& actor.squareGameObjectIsOn != door.squareGameObjectIsOn && actor.canSeeGameObject(door)) {
				actor.thoughtBubbleImageTextureObject = door.imageTexture;
				Action action = new ActionLock(this.actor, door);
				if (actor.straightLineDistanceTo(door.squareGameObjectIsOn) > 1) {
					if (AIRoutineUtils.moveTowards(door.squareGameObjectIsOn)) {
						return true;
					}
				} else if (!action.enabled) {
					return false;
				} else {
					action.perform();
					return true;
				}
			} else if (door.isOpen() && door.shouldBeClosed() && actor.squareGameObjectIsOn != door.squareGameObjectIsOn
					&& actor.canSeeGameObject(door)) {
				actor.thoughtBubbleImageTextureObject = door.imageTexture;
				Action action = new ActionClose(this.actor, door);
				if (actor.straightLineDistanceTo(door.squareGameObjectIsOn) > 1) {
					if (AIRoutineUtils.moveTowards(door.squareGameObjectIsOn)) {
						return true;
					}
				} else if (!action.enabled) {
					return false;
				} else {
					action.perform();
					return true;
				}
			}
		}
		return false;

	}

	public boolean sellItems(int minimumItemsToSell) {

		if (actor.inventory.itemsToSellCount < minimumItemsToSell)
			return false;

		Trader target = (Trader) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(Integer.MAX_VALUE, false, false,
				false, false, 0, false, true, Trader.class);

		if (target == null || target.sleeping || target.aiRoutine.state == STATE.GO_TO_BED_AND_GO_TO_SLEEP
				|| target.knownCriminals.contains(actor) || actor.knownCriminals.contains(target)) {
			return false;
		}

		this.actor.activityDescription = ACTIVITY_DESCRIPTION_SELLING_LOOT;
		this.actor.thoughtBubbleImageTextureObject = target.imageTexture;
		this.actor.thoughtBubbleImageTextureAction = Templates.GOLD.imageTexture;

		if (actor.straightLineDistanceTo(target.squareGameObjectIsOn) > 2 || !actor.canSeeGameObject(target)) {
			AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
			return true;
			// return
			// AIRoutineUtils.moveTowardsSquareToBeAdjacent(target.squareGameObjectIsOn);
		} else
			return actor.sellItemsMarkedToSell(target);
	}

	public boolean replenishEquipment() {

		CopyOnWriteArrayList<Integer> equipmentNeeded = new CopyOnWriteArrayList<Integer>(Integer.class);
		for (int requiredTemplateId : actor.requiredEquipmentTemplateIds) {
			if (!actor.inventory.containsGameObjectWithTemplateId(requiredTemplateId)) {
				equipmentNeeded.add(requiredTemplateId);
			}
		}

		if (equipmentNeeded.size() == 0) {
			return false;
		}

		boolean sellItems = sellItems(1);
		if (sellItems)
			return true;

		// find nearest shop keeper w/ pickaxe?
		Trader target = (Trader) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(Integer.MAX_VALUE, false, false,
				false, false, 0, false, true, Trader.class);

		if (target == null || target.sleeping || target.aiRoutine.state == STATE.GO_TO_BED_AND_GO_TO_SLEEP
				|| target.knownCriminals.contains(actor) || actor.knownCriminals.contains(target)) {
			return false;
		}

		this.actor.activityDescription = ACTIVITY_DESCRIPTION_BUYING_EQUIPMENT;
		this.actor.thoughtBubbleImageTextureObject = target.imageTexture;
		this.actor.thoughtBubbleImageTextureAction = Templates.GOLD.imageTexture;

		if (actor.straightLineDistanceTo(target.squareGameObjectIsOn) > 2 || !actor.canSeeGameObject(target)) {
			AIRoutineUtils.moveTowards(AIRoutineUtils.tempPath);
			// AIRoutineUtils.moveTowardsSquareToBeAdjacent(target.squareGameObjectIsOn);
		} else {
			for (GameObject tradersGameObject : (CopyOnWriteArrayList<GameObject>) target.inventory.gameObjects) {
				for (Integer requiredTemplateId : (CopyOnWriteArrayList<Integer>) equipmentNeeded) {
					if (tradersGameObject.toSell == true && tradersGameObject.templateId == requiredTemplateId) {
						new ActionBuyItems(actor, target, tradersGameObject).perform();
						continue;
					}
				}
			}

			equipmentNeeded.clear();
			for (int requiredTemplateId : actor.requiredEquipmentTemplateIds) {
				if (!actor.inventory.containsGameObjectWithTemplateId(requiredTemplateId)) {
					equipmentNeeded.add(requiredTemplateId);
				}
			}

			// Fluff the remaining ones :P but... how do i make instances? eek
			for (Integer requiredTemplateId : equipmentNeeded) {
				GameObject cheatGameObject = null;
				if (requiredTemplateId == Templates.PICKAXE.templateId) {
					cheatGameObject = Templates.PICKAXE.makeCopy(null, null);
				} else if (requiredTemplateId == Templates.HUNTING_BOW.templateId) {
					cheatGameObject = Templates.HUNTING_BOW.makeCopy(null, null);
				} else if (requiredTemplateId == Templates.HUNTING_KNIFE.templateId) {
					cheatGameObject = Templates.HUNTING_KNIFE.makeCopy(null, null);
				} else if (requiredTemplateId == Templates.SWORD.templateId) {
					cheatGameObject = Templates.SWORD.makeCopy(null, null);
				} else if (requiredTemplateId == Templates.FISHING_ROD.templateId) {
					cheatGameObject = Templates.FISHING_ROD.makeCopy(null, null);
				}

				target.inventory.add(cheatGameObject);

				if (actor.getCarriedGoldValue() < cheatGameObject.value) {
					actor.inventory.add(Templates.GOLD.makeCopy(null, null, cheatGameObject.value));
				}

				new ActionBuyItems(actor, target, cheatGameObject).perform();

			}
		}

		return true;
	}

	public boolean updateWantedPosterRoutine() {

		WantedPoster wantedPoster = actor.squareGameObjectIsOn.areaSquareIsIn.wantedPoster;
		if (wantedPoster == null)
			return false;

		if (actor.highestAccumulatedUnresolvedCrimeSeverity == 0)
			return false;

		if (wantedPoster.accumulatedSAeverity >= actor.highestAccumulatedUnresolvedCrimeSeverity)
			return false;

		this.actor.activityDescription = "Updating Wanted Poater";
		this.actor.thoughtBubbleImageTextureObject = wantedPoster.imageTexture;
		this.actor.thoughtBubbleImageTextureAction = Action.textureWrite;

		if (actor.straightLineDistanceTo(wantedPoster.squareGameObjectIsOn) < 2) {

			wantedPoster.updateCrimes(
					(CopyOnWriteArrayList<Crime>) actor.mapActorToCrimesWitnessed
							.get(actor.criminalWithHighestAccumulatedUnresolvedCrimeSeverity),
					actor.criminalWithHighestAccumulatedUnresolvedCrimeSeverity);
			new ActionWrite(actor, wantedPoster, wantedPoster.generateText()).perform();
			return true;
		} else {
			return AIRoutineUtils.moveTowards(wantedPoster.squareGameObjectIsOn);
		}

	}

	public boolean reportToGuardRoutine() {

		if (actor.highestAccumulatedUnresolvedCrimeSeverity == 0)
			return false;

		boolean crimesToReport = false;
		for (Crime crime : actor.crimesWitnessedUnresolved) {
			if (crime.reported == false) {
				crimesToReport = true;
				break;
			}
		}

		if (!crimesToReport)
			return false;

		Guard guardNearby = (Guard) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(100, false, false, false, false,
				0, false, true, Guard.class);

		if (guardNearby == null)
			return false;

		this.actor.thoughtBubbleImageTextureObject = guardNearby.imageTexture;

		if (actor.straightLineDistanceTo(guardNearby.squareGameObjectIsOn) <= 2) {
			new ActionReportCrime(actor, guardNearby).perform();
			return true;
		} else {
			return AIRoutineUtils.moveTowards(guardNearby.squareGameObjectIsOn);
		}

	}

	public boolean dropOffToLostAndFoundRoutine() {

		if (actor.squareGameObjectIsOn.areaSquareIsIn.lostAndFound == null)
			return false;

		if (actor.gameObjectsInInventoryThatBelongToAnother.size() == 0)
			return false;

		Storage lostAndFound = actor.squareGameObjectIsOn.areaSquareIsIn.lostAndFound;

		this.actor.thoughtBubbleImageTextureObject = lostAndFound.storageClosedTexture;
		this.actor.thoughtBubbleImageTextureAction = Action.textureRight;

		if (actor.straightLineDistanceTo(lostAndFound.squareGameObjectIsOn) < 2) {
			new ActionGiveItems(actor, lostAndFound, false, actor.gameObjectsInInventoryThatBelongToAnother).perform();
			return true;
		} else {
			return AIRoutineUtils.moveTowards(lostAndFound.squareGameObjectIsOn);
		}
	}

	// Pick up from lost and found
	public boolean pickUpFromLostAndFoundRoutine() {

		if (actor.squareGameObjectIsOn == null || actor.squareGameObjectIsOn.areaSquareIsIn == null
				|| actor.squareGameObjectIsOn.areaSquareIsIn.lostAndFound == null)
			return false;

		Storage lostAndFound = actor.squareGameObjectIsOn.areaSquareIsIn.lostAndFound;

		if (!lostAndFound.ownersOfContents.contains(actor))
			return false;

		this.actor.thoughtBubbleImageTextureObject = lostAndFound.storageClosedTexture;
		this.actor.thoughtBubbleImageTextureAction = Action.textureLeft;

		if (actor.straightLineDistanceTo(lostAndFound.squareGameObjectIsOn) < 2) {

			CopyOnWriteArrayList<GameObject> gameObjectsToPickUp = new CopyOnWriteArrayList<GameObject>(GameObject.class);
			for (GameObject gameObject : lostAndFound.inventory.gameObjects) {
				if (gameObject.owner == actor) {
					gameObjectsToPickUp.add(gameObject);
				}
			}

			if (gameObjectsToPickUp.size() == 0)
				return false;

			new ActionTakeItems(actor, lostAndFound, gameObjectsToPickUp).perform();
			return true;
		} else {
			return AIRoutineUtils.moveTowards(lostAndFound.squareGameObjectIsOn);
		}
	}

	protected boolean canSeeAnyone() {
		CopyOnWriteArrayList<Square> visibleSquares = actor.getAllSquaresWithinDistance(1, actor.sight);
		for (Square visibleSquare : visibleSquares) {
			Actor actorOnVisibleSquare = visibleSquare.inventory.actor;
			if (actorOnVisibleSquare != null && this.actor.canSeeGameObject(actorOnVisibleSquare)) {
				return true;
			}
		}

		return false;
	}

	public void goToBedAndSleep() {

		if (wokenUpCountdown > 0) {
			actor.thoughtBubbleImageTextureObject = ThoughtBubbles.ANGRY;
			return;
		}

		actor.followersShouldFollow = false;
		actor.activityDescription = ACTIVITY_DESCRIPTION_GOING_TO_BED;
		if (actor.bed != null) {
			if (actor.squareGameObjectIsOn == actor.bed.squareGameObjectIsOn) {

				sleep();
			} else {
				boolean s = AIRoutineUtils.moveTowards(actor.bed);
				actor.thoughtBubbleImageTextureObject = actor.bed.imageTexture;
				actor.thoughtBubbleImageTextureAction = Action.textureSleep;
			}
		} else {
			sleep();

		}

	}

	public boolean runFishingRoutine() {
		actor.thoughtBubbleImageTextureObject = Action.textureFishing;

		if (actor.fishingTarget != null) {

			FishingRod fishingRod = (FishingRod) actor.equipped;
			if (fishingRod.caught) {
				new ActionFishingCompleted(actor, actor.fishingTarget).perform();
				return true;
			} else if (fishingRod.lineDamage >= 1) {
				new ActionFishingFailed(actor, actor.fishingTarget).perform();
				return true;
			}

			Action action = new ActionFishingInProgress(actor, actor.fishingTarget);
			if (action.enabled) {
				action.perform();
				return true;
			}
		}

		actor.followersShouldFollow = false;

		Fish target = (Fish) AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(100, true, false, false, false, 0,
				false, true, Fish.class); // target is null, wtf

		FishingRod fishingRod = null;
		CopyOnWriteArrayList<GameObject> fishingRods = actor.inventory.getGameObjectsOfClass(FishingRod.class);
		for (GameObject f : fishingRods) {
			fishingRod = (FishingRod) f;
		}

		if (target == null || fishingRod == null) {
			return false;
		} else if (actor.straightLineDistanceTo(target.squareGameObjectIsOn) <= fishingRod.fishingRange) {
			if (target.squareGameObjectIsOn.inventory.waterBody != null) {
				new ActionFishingStart(actor, target).perform();
			} else {
				new ActionTakeItems(actor, target).perform();
			}
			return true;
		} else {
			boolean moved = AIRoutineUtils.moveTowards(target);
			return moved;
		}

	}

	public void sleep() {

		if (!actor.sleeping) {
			if (Game.level.shouldLog(actor)) {
				Game.level.logOnScreen(new ActivityLog(new Object[] { actor, " went to sleep" }));
			}
			ignoreList.clear();
			currentHobby = null;
		}

		actor.sleeping = true;
		actor.activityDescription = ACTIVITY_DESCRIPTION_SLEEPING;
		actor.thoughtBubbleImageTextureObject = Action.textureSleep;

	}

	public boolean runHobbyRoutine() {
		if (currentHobby == HOBBY.HUNTING) {
			return runHuntingRoutine();
		} else if (currentHobby == HOBBY.FISHING) {
			return runFishingRoutine();
		}
		return false;
	}

	public boolean runHuntingRoutine() {

		actor.thoughtBubbleImageTextureObject = Templates.HUNTING_BOW.imageTexture;
		this.actor.followersShouldFollow = true;
		this.actor.activityDescription = ACTIVITY_DESCRIPTION_HUNTING;
		// if (target == null)
		GameObject gameObjectToHunt = AIRoutineUtils.getNearestForPurposeOfBeingAdjacent(100, false, false, false, true,
				0, false, true, AggressiveWildAnimal.class, CarnivoreNeutralWildAnimal.class, HerbivoreWildAnimal.class,
				TinyNeutralWildAnimal.class);
		if (gameObjectToHunt == null) {
			AIRoutineUtils.moveTowards(actor.area.centreSquare);
			return true;
		} else {
			if (gameObjectToHunt == null || gameObjectToHunt.squareGameObjectIsOn == null) {
//				target = null;
			} else {
				target = gameObjectToHunt;
				this.actor.activityDescription = ACTIVITY_DESCRIPTION_HUNTING;
				if (target.remainingHealth <= 0) {
				} else {
					boolean attackedAnimal = AIRoutineUtils.attackTarget(target);
					if (!attackedAnimal) {
						AIRoutineUtils.moveTowards(target);
						return true;
					}
				}
			}
		}
		return false;
	}

	public abstract AIRoutine getInstance(Actor actor);

	public HOBBY getRandomHobbyFromActorsHobbies() {
		int rnd = new Random().nextInt(actor.hobbies.length);
		return actor.hobbies[rnd];
	}

}
