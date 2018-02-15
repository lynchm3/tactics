package com.marklynch.objects.tools;

import java.util.ArrayList;

import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.units.Actor;

public class Pickaxe extends Tool {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();

	public Pickaxe() {
		super();
	}

	@Override
	public Pickaxe makeCopy(Square square, Actor owner) {
		Pickaxe weapon = new Pickaxe();
		instances.add(weapon);
		setAttributesForCopy(weapon, square, owner);
		return weapon;
	}
}
