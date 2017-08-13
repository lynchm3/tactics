package com.marklynch.objects.actions;

import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Player;

public class ActionFollow extends Action {

	public static final String ACTION_NAME = "Follow";

	public Player performer;
	public Actor target;

	// Default for hostiles
	public ActionFollow(Player player, Actor target) {
		super(ACTION_NAME, "action_move.png");
		this.performer = player;
		this.target = target;
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {

		Player.playerTargetActor = target;
		performer.actionsPerformedThisTurn.add(this);
		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean checkLegality() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Sound createSound() {
		return null;
	}

}