package com.marklynch.objects;

import java.util.ArrayList;
import java.util.Comparator;

import com.marklynch.Game;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actions.ActionSmash;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.weapons.Projectile;

public class SquareInventory extends Inventory implements Comparator<GameObject> {

	public transient Square square;

	@Override
	public void postLoad1() {
		inventorySquares = new InventorySquare[5][5];
		for (int i = 0; i < inventorySquares.length; i++) {
			for (int j = 0; j < inventorySquares[i].length; j++) {
				inventorySquares[i][j] = new InventorySquare(i, j, null, this);
				inventorySquares[i][j].inventoryThisBelongsTo = this;
				inventorySquares[i][j].loadImages();
			}
		}

		int index = 0;

		// Tell objects they're in this inventory
		for (GameObject gameObject : gameObjects) {
			gameObject.squareGameObjectIsOn = square;
			if (!(gameObject instanceof Actor))
				Game.level.inanimateObjectsOnGround.add(gameObject);
		}

		for (GameObject gameObject : gameObjects) {
			gameObject.postLoad1();
		}

		// Put objects in inventory
		for (int i = 0; i < inventorySquares.length; i++) {
			for (int j = 0; j < inventorySquares[i].length; j++) {

				if (index >= gameObjects.size())
					return;

				if (inventorySquares[i][j].gameObject == null) {
					inventorySquares[i][j].gameObject = gameObjects.get(index);
					// gameObjects.get(index).inventorySquareGameObjectIsOn =
					// inventorySquares[i][j];
					index++;
				}
			}
		}
	}

	@Override
	public void postLoad2() {

		super.postLoad2();
		for (GameObject gameObject : gameObjects) {
			gameObject.postLoad2();
		}
	}

	@Override
	public void add(GameObject gameObject) {
		if (!gameObjects.contains(gameObject)) {

			// Remove references with square
			if (gameObject.squareGameObjectIsOn != null)
				gameObject.squareGameObjectIsOn.inventory.remove(gameObject);
			gameObject.squareGameObjectIsOn = null;

			// Remove from ground squares index
			// Game.level.inanimateObjectsOnGround.remove(gameObject);

			// Remove from another gameObjects inventory
			if (gameObject.inventoryThatHoldsThisObject != null)
				gameObject.inventoryThatHoldsThisObject.remove(gameObject);

			// add to this inventory
			gameObjects.add(gameObject);
			gameObject.inventoryThatHoldsThisObject = this;
			gameObject.squareGameObjectIsOn = square;

			// Add the general level tracking of inanimate objects
			if (!Game.level.inanimateObjectsOnGround.contains(gameObject) && !(gameObject instanceof Actor))
				Game.level.inanimateObjectsOnGround.add(gameObject);

			this.gameObjects.sort(this);
			// this.sort(eee, false);
			gameObject.randomisePosition();
			// for (int i = 0; i < inventorySquares.length; i++) {
			// for (int j = 0; j < inventorySquares[i].length; j++) {
			// if (inventorySquares[i][j].gameObject == null) {
			//
			// // Inventory Square
			// inventorySquares[i][j].gameObject = gameObject;
			// return;
			// }
			// }
			// }
		}
	}

	@Override
	public int remove(GameObject gameObject) {
		if (gameObjects.contains(gameObject)) {
			gameObjects.remove(gameObject);
			// gameObject.inventorySquareGameObjectIsOn = null;
			// for (int i = 0; i < inventorySquares.length; i++) {
			// for (int j = 0; j < inventorySquares[i].length; j++) {
			// if (inventorySquares[i][j].gameObject == gameObject) {
			// inventorySquares[i][j].gameObject = null;
			// return;
			// }
			// }
			// }
		}
		return -1;
	}

	public ArrayList<GameObject> getGameObjectsThatFitInInventory() {
		ArrayList<GameObject> gameObjectsThatFitInInventory = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject.fitsInInventory)
				gameObjectsThatFitInInventory.add(gameObject);
		}
		return gameObjectsThatFitInInventory;
	}

	public boolean hasGameObjectsThatFitInInventory() {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.fitsInInventory)
				return true;
		}
		return false;
	}

	public boolean blocksLineOfSight() {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.blocksLineOfSight)
				return true;
		}
		return false;
	}

	public void smashWindows(Projectile projectile) {
		for (GameObject gameObject : gameObjects) {
			if (gameObject.remainingHealth > 0 && gameObject instanceof Window) {
				new ActionSmash(projectile.shooter, gameObject).perform();

			}
		}

	}

	public float getSoundDampening() {
		float soundDampening = 1;

		for (GameObject gameObject : gameObjects) {
			if (gameObject.remainingHealth > 0 && gameObject.soundDampening > soundDampening) {
				soundDampening = gameObject.soundDampening;
			}
		}
		return soundDampening;
	}

	@Override
	public int compare(GameObject a, GameObject b) {
		// TODO Auto-generated method stub
		return (int) (a.height - b.height);
	}

	// public GameObject getAc() {
	// for (GameObject gameObject : gameObjects) {
	// if (gameObject instanceof Actor) {
	// return gameObject;
	// }
	// }
	// return null;
	// }
}
