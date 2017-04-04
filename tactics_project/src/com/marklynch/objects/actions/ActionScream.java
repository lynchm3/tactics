package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.units.Actor;
import com.marklynch.ui.ActivityLog;

public class ActionScream extends Action {

	public static final String ACTION_NAME = "Scream";
	public static final String ACTION_NAME_DISABLED = ACTION_NAME + " (can't reach)";

	Actor performer;

	// Default for hostiles
	public ActionScream(Actor attacker) {
		super(ACTION_NAME);
		this.performer = attacker;
		if (!check()) {
			enabled = false;
			actionName = ACTION_NAME_DISABLED;
		}
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {

		if (!enabled)
			return;

		if (performer.squareGameObjectIsOn.visibleToPlayer)
			Game.level.logOnScreen(new ActivityLog(new Object[] { performer, "screamed" }));

		performer.actions.add(this);if (sound != null)sound.play();
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean checkLegality() {
		return true;
	}

	@Override
	public Sound createSound() {
		return new Sound(performer, performer, performer.squareGameObjectIsOn, 20, legal, this.getClass());
	}

}
