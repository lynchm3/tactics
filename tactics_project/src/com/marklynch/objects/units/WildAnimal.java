package com.marklynch.objects.units;

import com.marklynch.ai.routines.AIRoutineForWildAnimal;
import com.marklynch.level.Square;
import com.marklynch.objects.Bed;
import com.marklynch.objects.Inventory;

public class WildAnimal extends Actor {

	public WildAnimal(String name, String title, int actorLevel, int health, int strength, int dexterity,
			int intelligence, int endurance, String imagePath, Square squareActorIsStandingOn, int travelDistance,
			Bed bed, Inventory inventory, boolean showInventory, boolean fitsInInventory,
			boolean canContainOtherObjects, float widthRatio, float heightRatio) {
		super(name, title, actorLevel, health, strength, dexterity, intelligence, endurance, imagePath,
				squareActorIsStandingOn, travelDistance, bed, inventory, showInventory, fitsInInventory,
				canContainOtherObjects, widthRatio, heightRatio);
		aiRoutine = new AIRoutineForWildAnimal();
	}

	@Override
	public void postLoad1() {
		super.postLoad1();
		aiRoutine = new AIRoutineForWildAnimal();
	}

	@Override
	public void postLoad2() {
		super.postLoad2();
	}

	@Override
	public WildAnimal makeCopy(Square square) {

		WildAnimal actor = new WildAnimal(name, title, actorLevel, (int) totalHealth, strength, dexterity, intelligence,
				endurance, imageTexturePath, square, travelDistance, null, inventory, showInventory, fitsInInventory,
				canContainOtherObjects, widthRatio, heightRatio);
		actor.faction = this.faction;
		actor.factionGUID = this.faction.guid;
		return actor;
	}

}
