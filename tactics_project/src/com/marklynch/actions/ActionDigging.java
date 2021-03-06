package com.marklynch.actions;

import java.util.concurrent.CopyOnWriteArrayList;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.animation.Animation.OnCompletionListener;
import com.marklynch.level.constructs.animation.primary.AnimationSlash;
import com.marklynch.level.constructs.animation.secondary.AnimationFanOut;
import com.marklynch.level.constructs.animation.secondary.AnimationTake;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.Direction;
import com.marklynch.objects.actors.Player;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.tools.Shovel;
import com.marklynch.ui.ActivityLog;
import com.marklynch.ui.popups.Toast;

public class ActionDigging extends Action {

	public static final String ACTION_NAME = "Dig";
	Shovel shovel;

	// Default for hostiles
	public ActionDigging(Actor attacker, Object target) {
		super(ACTION_NAME, textureDig, attacker, target);
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

		if (performer.squareGameObjectIsOn.xInGrid > targetSquare.xInGrid) {
			performer.backwards = true;
		} else if (performer.squareGameObjectIsOn.xInGrid < targetSquare.xInGrid) {
			performer.backwards = false;
		}

		shovel = (Shovel) performer.inventory.getGameObjectOfClass(Shovel.class);
		if (performer.equipped != shovel)
			performer.equippedBeforePickingUpObject = performer.equipped;
		performer.equipped = shovel;

		performer.setPrimaryAnimation(new AnimationSlash(performer, targetGameObject, new OnCompletionListener() {
			@Override
			public void animationComplete(GameObject gameObject) {
				if (targetGameObject != null && targetGameObject.diggable) {
					postMeleeAnimation();
				} else {
					Game.level
							.logOnScreen(new ActivityLog(new Object[] { performer, " dug up nothing with ", shovel }));
				}
			}
		}));

		// Animation for kicking up stones
		Direction direction = Direction.RIGHT;
		if (performer.squareGameObjectIsOn.xInGrid < targetSquare.xInGrid) {
			direction = Direction.RIGHT;
		} else if (performer.squareGameObjectIsOn.xInGrid > targetSquare.xInGrid) {
			direction = Direction.LEFT;
		} else if (performer.squareGameObjectIsOn.yInGrid < targetSquare.yInGrid) {
			direction = Direction.UP;
		} else if (performer.squareGameObjectIsOn.yInGrid > targetSquare.yInGrid) {
			direction = Direction.DOWN;
		}

		float x = (targetSquare.xInGridPixels);// + Game.HALF_SQUARE_WIDTH);
		float y = (targetSquare.yInGridPixels);// - Game.HALF_SQUARE_HEIGHT);
		Level.addSecondaryAnimation(
				new AnimationFanOut(performer, direction, x, y, 0.5f, Templates.ROCK.imageTexture, null));

//	AnimationFanOut(GameObject targetGameObject, Direction direction, float originX, float originY, float speed,
//				Texture texture, OnCompletionListener onCompletionListener) {

	}

	public void postMeleeAnimation() {

		performer.distanceMovedThisTurn = performer.travelDistance;
		performer.hasAttackedThisTurn = true;

		targetGameObject.squareGameObjectIsOn.setFloorImageTexture(Square.MUD_TEXTURE);

		if (!targetGameObject.discoveredObject && targetGameObject.level <= performer.level) {
			new ActionDiscover(performer, targetGameObject).perform();
		}

		if (Game.level.shouldLog(targetGameObject, performer))
			Game.level.logOnScreen(
					new ActivityLog(new Object[] { performer, " dug up ", targetGameObject, " with ", shovel }));

		for (GameObject buriedGamObject : (CopyOnWriteArrayList<GameObject>) targetGameObject.inventory.gameObjects) {
			if (Game.level.openInventories.size() > 0) {
			} else if (performer.squareGameObjectIsOn.onScreen() && performer.squareGameObjectIsOn.visibleToPlayer) {
				Level.addSecondaryAnimation(new AnimationTake(buriedGamObject, performer,
						performer.getHandXY().x - buriedGamObject.anchorX,
						performer.getHandXY().y - buriedGamObject.anchorY, 1f, null));
			}
			performer.inventory.add(buriedGamObject);

			if (performer == Level.player)
				Level.addToast(new Toast(new Object[] { this.image, " ", buriedGamObject }));
			if (Game.level.shouldLog(targetGameObject, performer))
				Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " received ", buriedGamObject }));
			if (!legal) {
				Crime crime = new Crime(this.performer, this.targetGameObject.owner, Crime.TYPE.CRIME_THEFT,
						buriedGamObject);
				this.performer.crimesPerformedThisTurn.add(crime);
				this.performer.crimesPerformedInLifetime.add(crime);
				notifyWitnessesOfCrime(crime);
			} else {
				trespassingCheck(performer, performer.squareGameObjectIsOn);
			}
		}

		float damage = targetGameObject.remainingHealth;
		targetGameObject.changeHealthSafetyOff(-damage, null, null);
		targetGameObject.checkIfDestroyed(performer, this);

		targetGameObject.showPow();

		if (performer.faction == Game.level.factions.player) {
			Game.level.undoList.clear();
		}

		if (performer == Game.level.player && Game.level.activeActor == Game.level.player)
			Game.level.endPlayerTurn();
		if (sound != null)
			sound.play();

		if (performer == Level.player) {
			if (!(Player.playerTargetAction instanceof ActionDigging) || !shouldContinue())
				performer.equipped = performer.equippedBeforePickingUpObject;
		}
	}

	@Override
	public boolean check() {

		if (!performer.inventory.containsGameObjectOfType(Shovel.class)) {
			disabledReason = NEED_A_SHOVEL;
			return false;
		}

		if (targetGameObject != null && targetGameObject.remainingHealth <= 0) {
			disabledReason = null;
			return false;
		}

		return true;
	}

	@Override
	public boolean checkRange() {

		if (performer.straightLineDistanceTo(targetSquare) > 1)
			return false;

		return true;
	}

	@Override
	public boolean checkLegality() {
		if (targetGameObject == null)
			return true;

		return standardAttackLegalityCheck(performer, targetGameObject);
	}

	@Override
	public Sound createSound() {
		if (targetGameObject == null)
			return null;

		Shovel shovel = (Shovel) performer.inventory.getGameObjectOfClass(Shovel.class);
		if (shovel != null) {
			float loudness = Math.max(targetGameObject.soundWhenHit, shovel.soundWhenHitting);
			return new Sound(performer, shovel, targetGameObject.squareGameObjectIsOn, loudness, legal,
					this.getClass());
		}
		return null;
	}

	@Override
	public boolean shouldContinue() {
		if (targetGameObject == null) {
			if (performed)
				return false;
			else
				return true;
		}

		if (performed && Player.inFight()) {
			return false;
		}

		if (targetGameObject.remainingHealth <= 0) {
			disabledReason = null;
			return false;
		}

		return true;
	}

}
