package com.marklynch.objects.inanimateobjects;

import com.marklynch.actions.Action;
import com.marklynch.actions.ActionMove;
import com.marklynch.actions.ActionUsePower;
import com.marklynch.level.constructs.animation.Animation.OnCompletionListener;
import com.marklynch.level.constructs.power.Power;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.utils.CopyOnWriteArrayList;

public class Landmine extends GameObject implements OnCompletionListener {

	public static final CopyOnWriteArrayList<GameObject> instances = new CopyOnWriteArrayList<GameObject>(GameObject.class);
	public int targetWeight = 10;
	public Power power;

	public Landmine() {
		super();
		canBePickedUp = false;
		fitsInInventory = false;
		canShareSquare = true;
		canContainOtherObjects = false;
		persistsWhenCantBeSeen = true;
		attackable = true;
		isFloorObject = true;
		orderingOnGound = 40;
		type = "Trap";
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	@Override
	public void squareContentsChanged() {

		if (squareGameObjectIsOn == null)
			return;

		doTheThing(null);
	}

	public void doTheThing(GameObject g) {
		int weightOnPlate = 0;

		if (squareGameObjectIsOn == null)
			return;

		for (GameObject gameObject : squareGameObjectIsOn.inventory.gameObjects) {
			if (gameObject.isFloorObject == false && gameObject != this) {
				weightOnPlate += gameObject.weight;
			}
		}

		if (weightOnPlate >= targetWeight) {
			explode();
		}

	}

	boolean exploded = false;

	@Override
	public boolean checkIfDestroyed(Object attacker, Action action) {

		if (!exploded && !died && remainingHealth <= 0) {
			exploded = true;
			new ActionUsePower(this, null, this.lastSquare, power.makeCopy(this), true).perform();
		}

		boolean destroyed = super.checkIfDestroyed(attacker, action);

		return destroyed;

	}

	public void explode() {
		hiddenObjectDiscovered();
		this.changeHealthSafetyOff(-this.remainingHealth, this, null);
	}

	@Override
	public Action getDefaultActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true, true);
	}

	@Override
	public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true, true);
	}

	@Override
	public CopyOnWriteArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		CopyOnWriteArrayList<Action> actions = new CopyOnWriteArrayList<Action>(Action.class);
		if (!this.discoveredObject)
			return actions;
		actions.add(new ActionMove(performer, squareGameObjectIsOn, true, true));
		actions.addAll(super.getAllActionsPerformedOnThisInWorld(performer));
		return actions;
	}

	public Landmine makeCopy(Square square, Actor owner, int level, int targetWeight) {
		Landmine landmine = new Landmine();
		setInstances(landmine);
		super.setAttributesForCopy(landmine, square, owner);
		landmine.level = level;
		landmine.targetWeight = targetWeight;
		landmine.power = power;
		return landmine;
	}

	@Override
	public void animationComplete(GameObject gameObject) {
		doTheThing(gameObject);
	}

}
