package com.marklynch.objects;

import com.marklynch.level.squares.Square;
import com.marklynch.objects.units.Actor;

public class Junk extends GameObject {

	public Junk() {
		canBePickedUp = true;
		showInventoryInGroundDisplay = false;
		fitsInInventory = true;
		canShareSquare = true;
		canContainOtherObjects = false;
		blocksLineOfSight = false;
		persistsWhenCantBeSeen = false;
		attackable = true;
	}

	@Override
	public Junk makeCopy(Square square, Actor owner) {
		Junk junk = new Junk();
		super.setAttributesForCopy(junk, square, owner);
		return junk;
	}

}
