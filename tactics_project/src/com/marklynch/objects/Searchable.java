package com.marklynch.objects;

import java.util.ArrayList;

import com.marklynch.level.Square;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionSearch;
import com.marklynch.objects.units.Actor;

import mdesl.graphics.Color;

public abstract class Searchable extends GameObject {

	public Searchable(String name, int health, String imagePath, Square squareGameObjectIsOn, Inventory inventory,
			boolean showInventory, boolean canShareSquare, boolean fitsInInventory, boolean canContainOtherObjects,
			boolean blocksLineOfSight, boolean persistsWhenCantBeSeen, float widthRatio, float heightRatio,
			float soundHandleX, float soundHandleY, float soundWhenHit, float soundWhenHitting, float soundDampening,
			Color light, float lightHandleX, float lightHandlY, boolean stackable, float fireResistance,
			float iceResistance, float electricResistance, float poisonResistance, Actor owner) {
		super(name, health, imagePath, squareGameObjectIsOn, inventory, showInventory, canShareSquare, fitsInInventory,
				canContainOtherObjects, blocksLineOfSight, persistsWhenCantBeSeen, widthRatio, heightRatio,
				soundHandleX, soundHandleY, soundWhenHit, soundWhenHitting, soundDampening, light, lightHandleX,
				lightHandlY, stackable, fireResistance, iceResistance, electricResistance, poisonResistance, owner);

	}

	@Override
	public ArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		ArrayList<Action> actions = new ArrayList<Action>();

		if (this.remainingHealth < 0)
			return actions;

		actions.add(new ActionSearch(performer, this));

		return actions;

	}

	public abstract boolean search();

	// @Override
	// public Searchable makeCopy(Square square, Actor owner) {
	// return new Searchable(new String(name), (int) totalHealth,
	// imageTexturePath, square, inventory.makeCopy(),
	// showInventory, canShareSquare, fitsInInventory, canContainOtherObjects,
	// blocksLineOfSight,
	// persistsWhenCantBeSeen, widthRatio, heightRatio, soundHandleX,
	// soundHandleY, soundWhenHit,
	// soundWhenHitting, soundDampening, light, lightHandleX, lightHandlY,
	// stackable, fireResistance,
	// iceResistance, electricResistance, poisonResistance, owner);
	// }

}
