package com.marklynch.objects;

import java.util.ArrayList;

import com.marklynch.level.squares.Square;
import com.marklynch.objects.units.Actor;

public class BigGameObject extends GameObject {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();

	public BigGameObject() {
		super();
		canBePickedUp = false;

		fitsInInventory = false;
		canShareSquare = false;

	}

	@Override
	public BigGameObject makeCopy(Square square, Actor owner) {
		BigGameObject bigGameObject = new BigGameObject();
		instances.add(bigGameObject);
		super.setAttributesForCopy(bigGameObject, square, owner);
		return bigGameObject;
	}

}
