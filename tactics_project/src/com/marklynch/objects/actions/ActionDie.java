package com.marklynch.objects.actions;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.animation.primary.AnimationDie;
import com.marklynch.level.constructs.effect.EffectBurning;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.InanimateObjectToAddOrRemove;
import com.marklynch.objects.Mirror;
import com.marklynch.objects.Stump;
import com.marklynch.objects.Tree;
import com.marklynch.objects.Vein;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.RockGolem;
import com.marklynch.ui.ActivityLog;

public class ActionDie extends Action {

	public static final String ACTION_NAME = "Die";
	public static final String ACTION_NAME_DISABLED = ACTION_NAME + " (can't reach)";
	Square target;

	public ActionDie(GameObject performer, Square target) {
		super(ACTION_NAME, "action_die.png");
		super.gameObjectPerformer = this.gameObjectPerformer = performer;
		this.target = target;
		if (!check()) {
			enabled = false;
			actionName = ACTION_NAME_DISABLED;
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

		logDeath();
		createCorpse();
		if (target.visibleToPlayer && gameObjectPerformer instanceof Actor)
			Game.level.player.addXP(gameObjectPerformer.level, gameObjectPerformer.squareGameObjectIsOn);

		// Remove from draw/update
		if (gameObjectPerformer != Game.level.player) {
			if (gameObjectPerformer instanceof Actor) {
				// if (gameObjectPerformer.squareGameObjectIsOn != null) {
				// Game.level.inanimateObjectsOnGround.remove(gameObjectPerformer);
				// gameObjectPerformer.squareGameObjectIsOn.inventory.remove(gameObjectPerformer);
				// } else if (gameObjectPerformer.inventoryThatHoldsThisObject != null) {
				// gameObjectPerformer.inventoryThatHoldsThisObject.remove(gameObjectPerformer);
				// }
				// Game.level.actorsToRemove.add((Actor) gameObjectPerformer);

			} else {
				Game.level.inanimateObjectsOnGroundToRemove.add(gameObjectPerformer);
			}
		}

		// this.faction.actors.remove(this);
		// gameObjectPerformer.actionsPerformedThisTurn.add(this);
		gameObjectPerformer.clearActions();

		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean checkRange() {
		return true;
	}

	@Override
	public boolean checkLegality() {
		return true;
	}

	@Override
	public Sound createSound() {
		return null;
	}

	public void logDeath() {

		if (gameObjectPerformer instanceof RockGolem) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(new ActivityLog(
						new Object[] { gameObjectPerformer.destroyedBy, " broke ", gameObjectPerformer, this.image }));

		} else if (gameObjectPerformer instanceof Actor && gameObjectPerformer.destroyedBy instanceof EffectBurning) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { gameObjectPerformer, " burned to death ", this.image }));
		} else if (gameObjectPerformer instanceof Vein) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { gameObjectPerformer, " was depleted ", this.image }));
		} else if (gameObjectPerformer.destroyedByAction instanceof ActionSmash) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(new ActivityLog(new Object[] { gameObjectPerformer, " smashed ", this.image }));
		} else if (gameObjectPerformer instanceof Tree
				&& gameObjectPerformer.destroyedByAction instanceof ActionChoppingStart) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { gameObjectPerformer, " was chopped down ", this.image }));
		} else if (gameObjectPerformer.destroyedBy instanceof EffectBurning) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { gameObjectPerformer, " burned down ", this.image }));
		} else if (gameObjectPerformer instanceof Actor) {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(new ActivityLog(
						new Object[] { gameObjectPerformer.destroyedBy, " killed ", gameObjectPerformer, this.image }));

		} else if (gameObjectPerformer.diggable == true) {

		} else {

			if (Game.level.shouldLog(gameObjectPerformer))
				Game.level.logOnScreen(new ActivityLog(new Object[] { gameObjectPerformer.destroyedBy, " destroyed a ",
						gameObjectPerformer, this.image }));

		}
	}

	public void createCorpse() {

		if (gameObjectPerformer instanceof Actor) {

			gameObjectPerformer.setPrimaryAnimation(new AnimationDie(gameObjectPerformer));
			// GameObject body = null;
			// if (gameObjectPerformer instanceof RockGolem) {
			// Templates.ORE.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// Templates.ORE.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// Templates.ROCK.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// Templates.ROCK.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// Templates.ROCK.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// } else if (gameObjectPerformer.destroyedBy instanceof EffectBurning) {
			// // Death by fire
			// Templates.ASH.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// for (GameObject gameObject : (ArrayList<GameObject>)
			// gameObjectPerformer.inventory.gameObjects.clone()) {
			// new ActionDropItems(gameObjectPerformer,
			// gameObjectPerformer.squareGameObjectIsOn, gameObject).perform();
			// }
			// } else if (gameObjectPerformer.destroyedByAction instanceof ActionSquash) {
			// // Deat by squashing
			// body =
			// Templates.BLOODY_PULP.makeCopy(gameObjectPerformer.squareGameObjectIsOn,
			// null);
			// body.name = "Former " + gameObjectPerformer.name;
			// body.weight = gameObjectPerformer.weight;
			// } else if (gameObjectPerformer instanceof AggressiveWildAnimal ||
			// gameObjectPerformer instanceof HerbivoreWildAnimal) {
			// // Dead animal
			// Templates.BLOOD.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// body = Templates.CARCASS.makeCopy(gameObjectPerformer.name + " carcass",
			// gameObjectPerformer.squareGameObjectIsOn, null,
			// gameObjectPerformer.weight);
			// } else {
			// Templates.BLOOD.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
			// body = Templates.CORPSE.makeCopy(gameObjectPerformer.name + " corpse",
			// gameObjectPerformer.squareGameObjectIsOn, null,
			// gameObjectPerformer.weight);
			// }
			// if (body != null) {
			// ArrayList<GameObject> gameObjectsInInventory = (ArrayList<GameObject>)
			// gameObjectPerformer.inventory
			// .getGameObjects().clone();
			// for (GameObject gameObjectInInventory : gameObjectsInInventory) {
			// gameObjectPerformer.inventory.remove(gameObjectInInventory);
			// body.inventory.add(gameObjectInInventory);
			// gameObjectInInventory.owner = null;
			// }
			// }
		} else {
			// GameObjects
			if (gameObjectPerformer.destroyedBy instanceof EffectBurning) {
				// Death by fire
				Game.level.inanimateObjectsToAdd.add(new InanimateObjectToAddOrRemove(
						Templates.ASH.makeCopy(null, null), gameObjectPerformer.squareGameObjectIsOn));
				for (GameObject gameObject : (ArrayList<GameObject>) gameObjectPerformer.inventory.gameObjects
						.clone()) {
					new ActionDropItems(gameObjectPerformer, gameObjectPerformer.squareGameObjectIsOn, gameObject)
							.perform();
				}
			} else if (gameObjectPerformer.templateId == Templates.CRATE.templateId) {
				// Death by fire
				Templates.WOOD_CHIPS.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
				for (GameObject gameObject : (ArrayList<GameObject>) gameObjectPerformer.inventory.gameObjects
						.clone()) {
					new ActionDropItems(gameObjectPerformer, gameObjectPerformer.squareGameObjectIsOn, gameObject)
							.perform();
				}
			} else if (gameObjectPerformer instanceof Tree
					&& gameObjectPerformer.destroyedByAction instanceof ActionChoppingStart) {

				Game.level.inanimateObjectsToAdd.add(new InanimateObjectToAddOrRemove(
						Templates.STUMP.makeCopy(null, null), gameObjectPerformer.squareGameObjectIsOn));
			} else if (gameObjectPerformer instanceof Stump) {
				if (gameObjectPerformer.squareGameObjectIsOn != null) {
					gameObjectPerformer.squareGameObjectIsOn.imageTexture = Square.MUD_TEXTURE;
				}
			} else if (gameObjectPerformer instanceof Mirror) {
				Game.level.inanimateObjectsToAdd.add(new InanimateObjectToAddOrRemove(
						Templates.BROKEN_GLASS.makeCopy(null, null), gameObjectPerformer.squareGameObjectIsOn));
			}

			for (GameObject gameObject : gameObjectPerformer.inventory.gameObjects) {
				Game.level.inanimateObjectsToAdd
						.add(new InanimateObjectToAddOrRemove(gameObject, gameObjectPerformer.squareGameObjectIsOn));
			}

		}
	}
}
