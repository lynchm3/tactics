package com.marklynch.tactics.objects;

import java.util.ArrayList;

public class Inventory {

	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

	public GameObject get(int i) {
		return gameObjects.get(i);
	}

	public void add(GameObject gameObject) {
		if (!gameObjects.contains(gameObject))
			gameObjects.add(gameObject);
	}

	public void remove(GameObject gameObject) {
		gameObjects.remove(gameObject);
	}

	public int size() {
		return gameObjects.size();
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(ArrayList<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}

	public boolean contains(GameObject gameObject) {
		return gameObjects.contains(gameObject);
	}

	public boolean canShareSquare() {
		for (GameObject gameObject : gameObjects) {
			if (!gameObject.canShareSquare)
				return false;
		}
		return true;
	}

	public GameObject getGameObjectThatCantShareSquare() {
		for (GameObject gameObject : gameObjects) {
			if (!gameObject.canShareSquare)
				return gameObject;
		}
		return null;
	}

	public Inventory makeCopy() {
		Inventory copy = new Inventory();
		for (GameObject gameObject : gameObjects) {
			copy.add(gameObject.makeCopy(null));
		}
		return copy;
	}

}
