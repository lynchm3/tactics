package com.marklynch.objects.actors;

import com.marklynch.utils.CopyOnWriteArrayList;

import com.marklynch.ai.routines.AIRoutineForCarnivoreNeutralWildAnimal;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.GameObject;

public class CarnivoreNeutralWildAnimal extends WildAnimal {

	public static final CopyOnWriteArrayList<GameObject> instances = new CopyOnWriteArrayList<GameObject>(GameObject.class);

	public CarnivoreNeutralWildAnimal() {
		super();
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	@Override
	public void postLoad1() {
		super.postLoad1();
		aiRoutine = new AIRoutineForCarnivoreNeutralWildAnimal(this);
	}

	@Override
	public void postLoad2() {
		super.postLoad2();
	}

	public CarnivoreNeutralWildAnimal makeCopy(String name, Square square, Faction faction, GameObject bed,
			GameObject[] mustHaves, GameObject[] mightHaves, Area area) {

		CarnivoreNeutralWildAnimal actor = new CarnivoreNeutralWildAnimal();
		setInstances(actor);
		super.setAttributesForCopy(name, actor, square, faction, bed, 0, mustHaves, mightHaves, area);

		return actor;
	}

}
