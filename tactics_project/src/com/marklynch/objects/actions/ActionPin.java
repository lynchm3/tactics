package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.units.Actor;
import com.marklynch.ui.PinWindow;

public class ActionPin extends Action {

	public static final String ACTION_NAME = "Info";

	public Actor performer;
	public Object target;

	// Default for hostiles
	public ActionPin(Actor reader, Object target) {
		super(ACTION_NAME, textureSearch);
		super.gameObjectPerformer = this.performer = reader;
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

		Game.level.pinWindows.add(new PinWindow(target));

		performer.actionsPerformedThisTurn.add(this);
	}

	@Override
	public boolean check() {
		for (PinWindow pinWindow : Game.level.pinWindows) {
			if (pinWindow.object == target) {
				return false;
			}
		}
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

}
