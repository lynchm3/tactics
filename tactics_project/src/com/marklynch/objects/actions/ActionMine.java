package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.Junk;
import com.marklynch.objects.Templates;
import com.marklynch.objects.Vein;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.weapons.Pickaxe;
import com.marklynch.ui.ActivityLog;

public class ActionMine extends Action {

	public static final String ACTION_NAME = "Mine";
	public static final String ACTION_NAME_CANT_REACH = ACTION_NAME + " (can't reach)";
	public static final String ACTION_NAME_NEED_PICKAXE = ACTION_NAME + " (need pickaxe)";

	Actor miner;
	Vein vein;

	// Default for hostiles
	public ActionMine(Actor attacker, Vein vein) {
		super(ACTION_NAME);
		this.miner = attacker;
		this.vein = vein;
		if (!check()) {
			enabled = false;
		}
	}

	@Override
	public void perform() {

		if (!enabled)
			return;

		Pickaxe pickaxe = (Pickaxe) miner.inventory.getGameObectOfClass(Pickaxe.class);

		float damage = vein.totalHealth / 4f;
		vein.remainingHealth -= damage;
		miner.distanceMovedThisTurn = miner.travelDistance;
		miner.hasAttackedThisTurn = true;

		Junk ore = Templates.ORE.makeCopy(null);
		miner.inventory.add(ore);

		if (miner.squareGameObjectIsOn.visibleToPlayer)
			Game.level.logOnScreen(new ActivityLog(new Object[] { miner, " mined ", vein, " with ", pickaxe }));

		if (miner.squareGameObjectIsOn.visibleToPlayer)
			Game.level.logOnScreen(new ActivityLog(new Object[] { miner, " received ", ore }));

		if (vein.checkIfDestroyed()) {
			if (miner.squareGameObjectIsOn.visibleToPlayer)
				Game.level.logOnScreen(new ActivityLog(new Object[] { miner, " depleted a ", vein }));
		}

		miner.showPow(vein);

		// Sound
		float loudness = vein.soundWhenHit * pickaxe.soundWhenHitting;
		miner.sounds.add(new Sound(miner, pickaxe, miner.squareGameObjectIsOn, loudness));

		if (miner.faction == Game.level.factions.get(0)) {
			Game.level.undoList.clear();
			Game.level.undoButton.enabled = false;
		}

		if (miner == Game.level.player)
			Game.level.endTurn();
	}

	@Override
	public boolean check() {
		if (!miner.visibleFrom(vein.squareGameObjectIsOn)) {
			actionName = ACTION_NAME_CANT_REACH;
			return false;
		}

		if (miner.straightLineDistanceTo(vein.squareGameObjectIsOn) > 1) {
			actionName = ACTION_NAME_CANT_REACH;
			return false;
		}

		if (!miner.inventory.contains(Pickaxe.class)) {
			actionName = ACTION_NAME_NEED_PICKAXE;
			return false;
		}

		return true;
	}

}
