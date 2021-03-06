package com.marklynch.objects.actors;

import com.marklynch.level.Level;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.constructs.animation.secondary.AnimationBubbles;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.utils.CopyOnWriteArrayList;

public class Fish extends WildAnimal {

	public static final CopyOnWriteArrayList<GameObject> instances = new CopyOnWriteArrayList<GameObject>(GameObject.class);

	public Fish() {
		super();
		canShareSquare = true;
		fitsInInventory = true;
		orderingOnGound = 60;
		type = "Fish";
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	public Fish makeCopy(String name, Square square, Faction faction, GameObject bed, GameObject[] mustHaves,
			GameObject[] mightHaves, Area area) {
		Fish actor = new Fish();
		setInstances(actor);
		super.setAttributesForCopy(name, actor, square, faction, bed, 0, mustHaves, mightHaves, area);

		return actor;
	}

	@Override
	public void update(int delta) {
		super.update(delta);

		if (squareGameObjectIsOn != null && delta % 3 == 0) {
			int x = (int) (squareGameObjectIsOn.xInGridPixels + drawOffsetX);
			int y = (int) (squareGameObjectIsOn.yInGridPixels + drawOffsetY);
			Level.addSecondaryAnimation(new AnimationBubbles(this, x + width, y, 0.1f, null));
		}
	}

}