package com.marklynch.actions;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.Level.LevelMode;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.power.Power;
import com.marklynch.level.constructs.power.PowerTeleportOther;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.utils.CopyOnWriteArrayList;

public class ActionUsePower extends Action {
	Power power;
	boolean log;

	// Default for hostiles
	public ActionUsePower(GameObject attacker, GameObject target, Square targetSquare, Power power, boolean log) {
		super("Cast " + power.name, null, attacker, target);
		this.targetSquare = targetSquare;
		this.targetGameObject = target;
		image = power.image;
		this.power = power;
		this.log = log;
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

		Game.level.levelMode = LevelMode.LEVEL_MODE_NORMAL;
		if (log && Game.level.shouldLog(gameObjectPerformer, targetSquare))
			power.log(gameObjectPerformer, targetSquare);

		power.cast(gameObjectPerformer, targetGameObject, targetSquare, this);

		if (sound != null)
			sound.play();

		if (power.hostile) {
			for (Square square : power.getAffectedSquares(targetSquare)) {
				for (GameObject gameObject : (CopyOnWriteArrayList<GameObject>) square.inventory.gameObjects) {
					gameObject.attackedBy(this.gameObjectPerformer, this);// here
				}
			}
		}

		if (!legal) {
			for (Square square : power.getAffectedSquares(targetSquare)) {
				for (GameObject gameObject : square.inventory.getGameObjects()) {
					Actor victim;
					if (gameObject instanceof Actor)
						victim = (Actor) gameObject;
					else
						victim = gameObject.owner;

					Crime.TYPE severity = power.crimeSeverity;
					if (severity == Crime.TYPE.CRIME_ASSAULT && !(gameObject instanceof Actor))
						severity = Crime.TYPE.CRIME_VANDALISM;

					if (victim != gameObjectPerformer && victim != null && performer != null) {
						Crime crime = new Crime(this.performer, victim, severity);
						this.performer.crimesPerformedThisTurn.add(crime);
						this.performer.crimesPerformedInLifetime.add(crime);
						notifyWitnessesOfCrime(crime);
					}
				}
			}

		} else {
			if (performer != null)
				trespassingCheck(performer, gameObjectPerformer.squareGameObjectIsOn);
		}

		if (gameObjectPerformer == Game.level.player && power.endsTurn && !(power instanceof PowerTeleportOther))
			Game.level.endPlayerTurn();
	}

	@Override
	public boolean check() {
		boolean powerCheck = power.check(gameObjectPerformer, targetSquare);
		if (powerCheck) {
			return true;
		} else {
			disabledReason = power.disabledReason;
			return false;
		}
	}

	@Override
	public boolean checkRange() {

		if (performer == Level.player && !targetSquare.visibleToPlayer && !power.hasRange(Integer.MAX_VALUE))
			return false;

		if (!targetSquare.playerCanCastTo && !power.hasRange(Integer.MAX_VALUE))
			return false;

		if (!power.hasRange(gameObjectPerformer.straightLineDistanceTo(targetSquare)))
			return false;

		if (!power.squareInCastLocations(gameObjectPerformer, targetSquare))
			return false;

		return true;
	}

	// @Override
	// public boolean checkLegality() {
	// // Something that belongs to some one else
	// if (target.owner != null && target.owner != Game.level.player)
	// return false;
	// return true;
	// }

	@Override
	public boolean checkLegality() {

		if (!power.potentialyCriminal)
			return true;

		// if (gameObjectSource.attackers.contains(targetSquare))
		// return true;

		for (Square square : power.getAffectedSquares(targetSquare)) {

			for (GameObject gameObject : square.inventory.getGameObjects()) {

				boolean assaultOrVandalism = standardAttackLegalityCheck(gameObjectPerformer, gameObject);

				if (!assaultOrVandalism) {
					if (power.illegalReason != null) {

						this.illegalReason = power.illegalReason;
					}

					return false;
				}

			}

		}
		return true;
	}

	@Override
	public Sound createSound() {

		// Sound

		if (gameObjectPerformer.equipped == null)
			return null;

		float loudness = power.loudness;

		if (gameObjectPerformer.equipped != null)
			return new Sound(gameObjectPerformer, gameObjectPerformer, targetSquare, loudness, legal, this.getClass());
		return null;
	}

}
