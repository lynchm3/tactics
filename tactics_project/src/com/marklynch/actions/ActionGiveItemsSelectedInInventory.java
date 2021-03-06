package com.marklynch.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.inventory.InventorySquare;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.GameObject;

public class ActionGiveItemsSelectedInInventory extends Action {

	public static final String ACTION_NAME = "Give";
	// GameObject receiver;
	GameObject objectToGive;
	boolean logAsTake;
	InventorySquare inventorySquare;

	public ActionGiveItemsSelectedInInventory(GameObject performer, GameObject receiver, boolean logAsTake,
			GameObject objectToGive) {
		super(ACTION_NAME, textureGive, performer, receiver);
		if (!(receiver instanceof Actor) || receiver.remainingHealth <= 0) {
			this.actionName = "Put";
			this.image = texturePut;
		}
		this.logAsTake = logAsTake;
		this.inventorySquare = objectToGive.inventorySquare;
		this.objectToGive = objectToGive;

		if (!check()) {
			enabled = false;
		} else {
			actionName = ACTION_NAME + " " + objectToGive.name;
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

		if (inventorySquare.stack.size() <= 5) {
			new ActionGiveItems(gameObjectPerformer, targetGameObject, logAsTake, objectToGive).perform();
		} else {
			String qtyString = "Enter qty to give (have: " + inventorySquare.stack.size() + ")";
			if (!(targetGameObject instanceof Actor)) {
				qtyString = "Enter qty to put (have: " + +inventorySquare.stack.size() + ")";
			}
			Game.level.player.inventory.showQTYDialog(
					new ActionGiveItems(gameObjectPerformer, targetGameObject, logAsTake, objectToGive.inventorySquare.stack),
					inventorySquare.stack.size(), qtyString, 0);
		}
	}

	@Override
	public boolean check() {

		if (targetGameObject == null)
			return false;

		return true;
	}

	@Override
	public boolean checkRange() {

		if (gameObjectPerformer instanceof Actor
				&& !((Actor) gameObjectPerformer).canSeeSquare(targetGameObject.squareGameObjectIsOn)) {
			return false;
		}

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
