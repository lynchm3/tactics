package com.marklynch.objects.actions;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.animation.primary.AnimationSlash;
import com.marklynch.level.constructs.animation.secondary.AnimationTake;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.tools.Shovel;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Player;
import com.marklynch.ui.ActivityLog;

public class ActionDigging extends Action {

	public static final String ACTION_NAME = "Dig";

	Actor performer;
	GameObject target;
	Shovel shovel;

	// Default for hostiles
	public ActionDigging(Actor attacker, GameObject target) {
		super(ACTION_NAME, textureDig);
		super.gameObjectPerformer = this.performer = attacker;
		this.target = target;
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

		if (performer.squareGameObjectIsOn.xInGrid > target.squareGameObjectIsOn.xInGrid) {
			performer.backwards = true;
		} else if (performer.squareGameObjectIsOn.xInGrid < target.squareGameObjectIsOn.xInGrid) {
			performer.backwards = false;
		}

		shovel = (Shovel) performer.inventory.getGameObjectOfClass(Shovel.class);
		if (performer.equipped != shovel)
			performer.equippedBeforePickingUpObject = performer.equipped;
		performer.equipped = shovel;

		performer.setPrimaryAnimation(new AnimationSlash(performer, target) {
			@Override
			public void runCompletionAlgorightm(boolean wait) {
				super.runCompletionAlgorightm(wait);
				postMeleeAnimation();
			}
		});
	}

	public void postMeleeAnimation() {

		performer.distanceMovedThisTurn = performer.travelDistance;
		performer.hasAttackedThisTurn = true;

		target.squareGameObjectIsOn.imageTexture = Square.MUD_TEXTURE;

		if (Game.level.shouldLog(target, performer))
			Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " dug up ", target, " with ", shovel }));

		for (GameObject buriedGamObject : (ArrayList<GameObject>) target.inventory.gameObjects.clone()) {
			if (Game.level.openInventories.size() > 0) {
			} else if (performer.squareGameObjectIsOn.onScreen() && performer.squareGameObjectIsOn.visibleToPlayer) {
				performer.addSecondaryAnimation(new AnimationTake(buriedGamObject, performer, 0, 0, 1f));
			}
			performer.inventory.add(buriedGamObject);
			if (Game.level.shouldLog(target, performer))
				Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " received ", buriedGamObject }));
			if (!legal) {
				Crime crime = new Crime(this, this.performer, this.target.owner, Crime.TYPE.CRIME_THEFT,
						buriedGamObject);
				this.performer.crimesPerformedThisTurn.add(crime);
				this.performer.crimesPerformedInLifetime.add(crime);
				notifyWitnessesOfCrime(crime);
			} else {
				trespassingCheck(this, performer, performer.squareGameObjectIsOn);
			}
		}

		float damage = target.remainingHealth;
		target.changeHealthSafetyOff(-damage, null, null);
		target.checkIfDestroyed(performer, this);

		target.showPow();

		if (performer.faction == Game.level.factions.player) {
			Game.level.undoList.clear();
		}

		if (performer == Game.level.player && Game.level.activeActor == Game.level.player)
			Game.level.endPlayerTurn();
		performer.actionsPerformedThisTurn.add(this);
		if (sound != null)
			sound.play();
		performer.equipped = performer.equippedBeforePickingUpObject;
	}

	@Override
	public boolean check() {

		if (!performer.inventory.contains(Shovel.class)) {
			disabledReason = NEED_A_SHOVEL;
			return false;
		}

		if (target.remainingHealth <= 0) {
			disabledReason = null;
			return false;
		}

		return true;
	}

	@Override
	public boolean checkRange() {

		if (performer.straightLineDistanceTo(target.squareGameObjectIsOn) > 1) {
			return false;
		}

		return true;
	}

	@Override
	public boolean checkLegality() {
		return standardAttackLegalityCheck(performer, target);
	}

	@Override
	public Sound createSound() {
		Shovel shovel = (Shovel) performer.inventory.getGameObjectOfClass(Shovel.class);
		if (shovel != null) {
			float loudness = Math.max(target.soundWhenHit, shovel.soundWhenHitting);
			return new Sound(performer, shovel, target.squareGameObjectIsOn, loudness, legal, this.getClass());
		}
		return null;
	}

	@Override
	public boolean shouldContinue() {

		if (performed && Player.inFight()) {
			return false;
		}

		if (target.remainingHealth <= 0) {
			disabledReason = null;
			return false;
		}

		return true;
	}

}