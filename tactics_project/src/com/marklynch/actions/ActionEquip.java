package com.marklynch.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.armor.BodyArmor;
import com.marklynch.objects.armor.Helmet;
import com.marklynch.objects.armor.LegArmor;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.ui.ActivityLog;
import com.marklynch.utils.ResourceUtils;

public class ActionEquip extends Action {

	public static final String ACTION_NAME = "Equip";

	ActionTakeItems actionTake;

	public ActionEquip(Actor performer, GameObject gameObject) {
		super(ACTION_NAME, textureEquip, performer, gameObject);
		if (!Game.level.player.inventory.contains(gameObject)) {
			actionTake = new ActionTakeItems(performer, gameObject.inventoryThatHoldsThisObject.parent, gameObject);

		}
		if (!Game.level.player.inventory.contains(gameObject) && Game.level.openInventories.size() > 0) {
			image = ResourceUtils.getGlobalImage("leftleft.png", false);
		}
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

		if (actionTake != null)
			actionTake.perform();

		if (Game.level.shouldLog(performer))
			Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " equipped ", targetGameObject }));

		if (targetGameObject instanceof Helmet) {
			performer.helmet = (Helmet) targetGameObject;
		} else if (targetGameObject instanceof BodyArmor) {
			performer.bodyArmor = (BodyArmor) targetGameObject;
		} else if (targetGameObject instanceof LegArmor) {
			performer.legArmor = (LegArmor) targetGameObject;
		} else {
			performer.equip(targetGameObject);
		}

		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {

		if (actionTake != null) {
			disabledReason = actionTake.disabledReason;
			return actionTake.enabled;
		}

		return true;
	}

	@Override
	public boolean checkRange() {

		if (actionTake != null) {
			return actionTake.checkRange();
		}
		return true;
	}

	@Override
	public boolean checkLegality() {
		if (actionTake != null) {
			this.illegalReason = actionTake.illegalReason;
			return actionTake.legal;
		}
		return true;
	}

	@Override
	public Sound createSound() {
		return null;
	}

}