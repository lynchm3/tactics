package com.marklynch.objects;

import java.util.ArrayList;

import com.marklynch.level.constructs.animation.Animation.OnCompletionListener;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionMove;
import com.marklynch.objects.actions.ActionTeleport;
import com.marklynch.objects.units.Actor;

public class Portal extends GameObject implements UpdatesWhenSquareContentsChange, OnCompletionListener {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();
	public Square connectedSquare = null;

	public Portal() {
		super();
		canBePickedUp = false;
		fitsInInventory = false;
		canShareSquare = true;
		canContainOtherObjects = false;
		persistsWhenCantBeSeen = true;
		attackable = false;
		isFloorObject = true;
	}

	// @Override
	// public void draw1() {
	// }
	//
	// @Override
	// public void draw2() {
	// }
	//
	// @Override
	// public void draw3() {
	// }

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	public Portal makeCopy(Square square, Actor owner, Square connectedSquare) {
		Portal portal = new Portal();
		setInstances(portal);
		super.setAttributesForCopy(portal, square, owner);
		portal.connectedSquare = connectedSquare;
		return portal;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
	}

	@Override
	public void squareContentsChanged() {

		if (squareGameObjectIsOn == null)
			return;

		for (final GameObject gameObject : (ArrayList<GameObject>) squareGameObjectIsOn.inventory.gameObjects.clone()) {

			if (gameObject == this || gameObject.isFloorObject)
				continue;

			if (gameObject.primaryAnimation != null && gameObject.primaryAnimation.completed == false) {
				gameObject.primaryAnimation.onCompletionListener = this;
			} else {
				doTheThing(gameObject);
			}
		}
		System.out.println("updateVoid 1 - " + squareGameObjectIsOn.inventory);

	}

	public void doTheThing(final GameObject gameObject) {

		if (squareGameObjectIsOn == null || gameObject == null || connectedSquare == null)
			return;
		new ActionTeleport(Portal.this, gameObject, connectedSquare, true).perform();

	}

	@Override
	public Action getDefaultActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true);
	}

	@Override
	public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true);
	}

	@Override
	public ArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		ArrayList<Action> actions = new ArrayList<Action>();
		actions.add(new ActionMove(performer, squareGameObjectIsOn, true));
		actions.addAll(super.getAllActionsPerformedOnThisInWorld(performer));
		return actions;
	}

	@Override
	public void animationComplete(GameObject gameObject) {
		System.out.println("VoidHole.animationComplete");
		doTheThing(gameObject);
	}

}
