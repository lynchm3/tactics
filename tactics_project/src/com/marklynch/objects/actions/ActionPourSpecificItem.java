package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.tools.ContainerForLiquids;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.AggressiveWildAnimal;
import com.marklynch.objects.units.Monster;
import com.marklynch.ui.ActivityLog;

public class ActionPourSpecificItem extends Action {

	public static final String ACTION_NAME = "Pour";

	Actor performer;
	Square targetSquare;
	GameObject targetGameObject;
	ContainerForLiquids containerForLiquids;

	// Default for hostiles
	public ActionPourSpecificItem(Actor performer, Object target, ContainerForLiquids container) {
		super(ACTION_NAME, texturePour);
		super.gameObjectPerformer = this.performer = performer;
		if (target instanceof Square) {
			targetSquare = (Square) target;
			targetGameObject = targetSquare.inventory.gameObjectThatCantShareSquare;
		} else if (target instanceof GameObject) {
			targetGameObject = (GameObject) target;
			targetSquare = targetGameObject.squareGameObjectIsOn;
		}
		this.containerForLiquids = container;
		if (!check()) {
			enabled = false;
		} else {
			actionName = ACTION_NAME + " " + container.name;
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

		if (Game.level.shouldLog(targetGameObject, performer)) {
			if (targetGameObject != null) {
				Game.level.logOnScreen(new ActivityLog(
						new Object[] { performer, " poured ", containerForLiquids, " on ", targetGameObject }));
			} else {
				Game.level
						.logOnScreen(new ActivityLog(new Object[] { performer, " poured out ", containerForLiquids }));

			}
		}

		for (GameObject gameObject : this.targetSquare.inventory.getGameObjects()) {
			// new ActionDouse(shooter, gameObject).perform();
			for (Effect effect : containerForLiquids.liquid.touchEffects) {
				gameObject.addEffect(effect.makeCopy(performer, gameObject));
			}
		}

		GameObject newJar = Templates.JAR.makeCopy(null, containerForLiquids.owner);
		performer.inventory.add(newJar);
		if (performer.equipped == containerForLiquids)
			performer.equipped = newJar;
		performer.inventory.remove(containerForLiquids);

		if (Game.level.openInventories.size() > 0)
			Game.level.openInventories.get(0).close();

		performer.distanceMovedThisTurn = performer.travelDistance;

		if (performer.faction == Game.level.factions.player) {
			Game.level.undoList.clear();
		}

		if (performer == Game.level.player)
			Game.level.endPlayerTurn();

		performer.actionsPerformedThisTurn.add(this);
		if (sound != null)
			sound.play();

		if (!legal) {

			Actor victim = null;

			if (targetGameObject instanceof Actor)
				victim = (Actor) targetGameObject;
			else if (targetGameObject != null)
				victim = targetGameObject.owner;
			if (victim != null) {
				Crime crime = new Crime(this, this.performer, victim, Crime.TYPE.CRIME_DOUSE);
				this.performer.crimesPerformedThisTurn.add(crime);
				this.performer.crimesPerformedInLifetime.add(crime);
				notifyWitnessesOfCrime(crime);
			}
		} else {
			trespassingCheck(this, performer, performer.squareGameObjectIsOn);
		}
	}

	@Override
	public boolean check() {

		if (targetSquare == null && targetGameObject == null)
			return false;

		if (containerForLiquids.liquid == null) {
			disabledReason = CONTAINER_IS_EMPTY;
			return false;
		}

		return true;
	}

	@Override
	public boolean checkRange() {

		if (performer.straightLineDistanceTo(targetSquare) > 1) {
			return false;
		}

		if (!performer.canSeeSquare(targetSquare)) {
			return false;
		}

		return true;
	}

	@Override
	public boolean checkLegality() {
		// Empty square, it's fine
		if (targetGameObject == null)
			return true;

		// Something that belongs to some one else
		if (targetGameObject.owner != null && targetGameObject.owner != performer) {
			illegalReason = VANDALISM;
			return false;
		}

		// Is human
		if (targetGameObject instanceof Actor)
			if (!(targetGameObject instanceof Monster) && !(targetGameObject instanceof AggressiveWildAnimal)) {
				illegalReason = ASSAULT;
				return false;
			}

		return true;
	}

	@Override
	public Sound createSound() {

		// Sound
		float loudness = 3;
		return new Sound(performer, containerForLiquids, targetSquare, loudness, legal, this.getClass());
	}

}
