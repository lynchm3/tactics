package com.marklynch.tactics.objects.unit.ai;

import com.marklynch.Game;
import com.marklynch.tactics.objects.GameObject;
import com.marklynch.tactics.objects.unit.Actor;

public class AItargetObject extends AI {

	GameObject object;

	// For loading and saving
	String objectGUID;

	public AItargetObject(GameObject object, Actor actor) {
		super(actor);
		this.object = object;
		objectGUID = object.guid;
	}

	@Override
	public boolean move() {
		if (object != null)
			return this.moveTowardsTargetToAttack(object);
		else
			return false;
	}

	@Override
	public boolean attack() {

		if (object != null)
			return this.attackTarget(object);
		else
			return false;
	}

	@Override
	public void postLoad() {
		object = Game.level.findObjectFromGUID(objectGUID);
	}
}
