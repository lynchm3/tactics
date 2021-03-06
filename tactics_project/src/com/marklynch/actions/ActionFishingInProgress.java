package com.marklynch.actions;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.Level.LevelMode;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Player;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.tools.FishingRod;
import com.marklynch.objects.tools.Shovel;

public class ActionFishingInProgress extends Action {

	public static final String ACTION_NAME = "Fishing";

	// Default for hostiles
	public ActionFishingInProgress(Actor attacker, GameObject target) {
		super(ACTION_NAME, textureFishing, attacker, target);
		this.targetGameObject = target;
		if (!check()) {
			enabled = false;
		}
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {
		super.perform();

		if (!enabled)
			return;

		if (!checkRange())
			return;

		performer.fishingTarget = targetGameObject;
		targetGameObject.beingFishedBy = performer;

		if (performer == Level.player) {
			Level.levelMode = LevelMode.LEVEL_MODE_FISHING;
			Player.playerTargetAction = new ActionFishingInProgress(performer, targetGameObject);
			Player.playerFirstMove = true;
			// }
		} else {
		}

		performer.distanceMovedThisTurn = performer.travelDistance;
		performer.hasAttackedThisTurn = true;

		if (!legal) {
			Crime crime = new Crime(this.performer, this.targetGameObject.owner, Crime.TYPE.CRIME_THEFT, targetGameObject);
			this.performer.crimesPerformedThisTurn.add(crime);
			this.performer.crimesPerformedInLifetime.add(crime);
			notifyWitnessesOfCrime(crime);
		} else {
			trespassingCheck(performer, performer.squareGameObjectIsOn);
		}

		if (performer.faction == Game.level.factions.player) {
			Game.level.undoList.clear();
		}

		if (performer == Game.level.player && Game.level.activeActor == Game.level.player)
			Game.level.endPlayerTurn();
		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {

		if (!performer.inventory.containsGameObjectOfType(FishingRod.class)) {
			disabledReason = NEED_A_FISHING_ROD;
			return false;
		}

		if (targetGameObject.remainingHealth <= 0)
			return false;

		return true;
	}

	@Override
	public boolean checkRange() {
		return true;

		// CopyOnWriteArrayList<GameObject> fishingRods =
		// performer.inventory.getGameObjectsOfClass(FishingRod.class);
		//
		// for (GameObject fishingRod : fishingRods) {
		// if (performer.straightLineDistanceTo(target.squareGameObjectIsOn) <=
		// fishingRod.maxRange) {
		// return true;
		// }
		// }
		// actionName = ACTION_NAME_CANT_REACH;
		// return false;
	}

	@Override
	public boolean checkLegality() {
		return standardAttackLegalityCheck(performer, targetGameObject);
	}

	@Override
	public Sound createSound() {
		Shovel shovel = (Shovel) performer.inventory.getGameObjectOfClass(Shovel.class);
		if (shovel != null) {
			float loudness = Math.max(targetGameObject.soundWhenHit, shovel.soundWhenHitting);
			return new Sound(performer, shovel, targetGameObject.squareGameObjectIsOn, loudness, legal, this.getClass());
		}
		return null;
	}

}