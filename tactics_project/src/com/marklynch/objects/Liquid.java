package com.marklynch.objects;

import java.util.ArrayList;

import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.units.Actor;

public class Liquid extends GameObject {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();
	public float volume;
	public Effect[] touchEffects;
	public Effect[] drinkEffects;

	public Liquid() {
		super();
		canBePickedUp = false;

		fitsInInventory = false;

		attackable = false;
	}

	public Liquid makeCopy(Square square, Actor owner, float volume) {
		Liquid liquid = new Liquid();
		instances.add(liquid);
		super.setAttributesForCopy(liquid, square, owner);
		liquid.volume = volume;
		liquid.touchEffects = touchEffects;
		liquid.drinkEffects = drinkEffects;
		return liquid;
	}
}
