package com.marklynch.actions;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.Openable;
import com.marklynch.ui.ActivityLog;

public class ActionOpen extends Action {

	public static final String ACTION_NAME = "Open";

	Openable openable;

	public ActionOpen(GameObject opener, Openable openable) {
		super(ACTION_NAME, textureOpen, opener, openable);
		this.openable = openable;
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

		if (openable.isLocked())
			new ActionUnlock(gameObjectPerformer, openable).perform();

		openable.open();

		if (Game.level.shouldLog(openable, gameObjectPerformer))
			Game.level.logOnScreen(new ActivityLog(new Object[] { gameObjectPerformer, " opened ", openable }));

		openable.showPow();

		if (gameObjectPerformer instanceof Actor) {
			Actor actor = (Actor) gameObjectPerformer;
			if (actor.faction == Game.level.factions.player) {
				Game.level.undoList.clear();
			}

			trespassingCheck(actor, actor.squareGameObjectIsOn);

			if (Game.level.player.peekSquare != null) {
				new ActionStopPeeking(Game.level.player).perform();
			}

		}
		if (sound != null)
			sound.play();

		if (performer == Level.player)
			Level.player.calculateVisibleAndCastableSquares(Level.player.squareGameObjectIsOn);
	}

	@Override
	public boolean check() {
		if (gameObjectPerformer instanceof Actor) {
			Actor actor = (Actor) gameObjectPerformer;

			if (openable.isLocked() && !actor.hasKeyForDoor(openable)) {
				disabledReason = NEED_A_KEY;
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkRange() {
		if (gameObjectPerformer instanceof Actor) {
			Actor actor = (Actor) gameObjectPerformer;
			if (!actor.canSeeGameObject(openable)) {
				return false;
			}

			if (gameObjectPerformer.straightLineDistanceTo(openable.squareGameObjectIsOn) != 1) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkLegality() {
		if (openable.isLocked()) {
			Action unlock = new ActionUnlock(gameObjectPerformer, openable);
			if (!unlock.legal) {
				illegalReason = TRESPASSING;
				return false;
			}
		}
		return true;
	}

	@Override
	public Sound createSound() {
		return new Sound(gameObjectPerformer, openable, openable.squareGameObjectIsOn, 1, legal, this.getClass());
	}

}
