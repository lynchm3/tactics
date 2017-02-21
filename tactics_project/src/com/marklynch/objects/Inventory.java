package com.marklynch.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import com.marklynch.Game;
import com.marklynch.editor.UserInputEditor;
import com.marklynch.objects.units.Actor;

public class Inventory {

	public int widthInSquares = 5;
	public int heightInSquares = 6;
	public transient InventorySquare[][] inventorySquares = new InventorySquare[widthInSquares][heightInSquares];
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>(widthInSquares * heightInSquares);

	public enum INVENTORY_STATE {
		DEFAULT, ADD_OBJECT, MOVEABLE_OBJECT_SELECTED, SETTINGS_CHANGE
	}

	public transient INVENTORY_STATE inventoryState = INVENTORY_STATE.DEFAULT;

	public enum INVENTORY_SORT_BY {
		SORT_ALPHABETICALLY, SORT_BY_NEWEST, SORT_BY_VALUE, SORT_BY_FAVOURITE, SORT_BY_TOTAL_DAMAGE, SORT_BY_SLASH_DAMAGE, SORT_BY_BLUNT_DAMAGE, SORT_BY_PIERCE_DAMAGE, SORT_BY_FIRE_DAMAGE, SORT_BY_WATER_DAMAGE, SORT_BY_POISON_DAMAGE, SORT_BY_ELECTRICAL_DAMAGE, SORT_BY_MAX_RANGE, SORT_BY_MIN_RANGE
	}

	public static transient INVENTORY_SORT_BY inventorySortBy = INVENTORY_SORT_BY.SORT_BY_MAX_RANGE;

	private transient boolean isOpen = false;
	transient float x = 1000;
	transient float y = 100;
	transient float width = widthInSquares * Game.SQUARE_WIDTH;
	transient float height = heightInSquares * Game.SQUARE_HEIGHT;
	transient private InventorySquare inventorySquareMouseIsOver;
	transient private GameObject selectedGameObject;

	public Inventory() {
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {
				inventorySquares[j][i] = new InventorySquare(j, i, "dialogbg.png", this);
			}
		}
	}

	public void postLoad1() {
		inventorySquares = new InventorySquare[widthInSquares][heightInSquares];
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {
				inventorySquares[j][i] = new InventorySquare(i, j, "dialogbg.png", this);
				inventorySquares[j][i].inventoryThisBelongsTo = this;
				inventorySquares[j][i].loadImages();
			}
		}

		// Tell objects they're in this inventory
		for (GameObject gameObject : gameObjects) {
			gameObject.inventoryThatHoldsThisObject = this;
			gameObject.postLoad1();
		}

		int index = 0;

		// Put objects in inventory
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {

				if (index >= gameObjects.size())
					return;

				if (inventorySquares[j][i].gameObject == null) {
					inventorySquares[j][i].gameObject = gameObjects.get(index);
					gameObjects.get(index).inventorySquareGameObjectIsOn = inventorySquares[j][i];
					index++;
				}
			}
		}
	}

	public void postLoad2() {
		// Tell objects they're in this inventory
		for (GameObject gameObject : gameObjects) {
			gameObject.inventoryThatHoldsThisObject = this;
			gameObject.postLoad2();
		}

	}

	public void loadImages() {
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {
				inventorySquares[j][i].loadImages();
			}
		}
	}

	public GameObject get(int i) {
		return gameObjects.get(i);
	}

	public void add(GameObject gameObject) {
		if (!gameObjects.contains(gameObject)) {

			// Remove references with square
			if (gameObject.squareGameObjectIsOn != null)
				gameObject.squareGameObjectIsOn.inventory.remove(gameObject);
			gameObject.squareGameObjectIsOn = null;

			// Remove from ground squares index
			if (Game.level.inanimateObjectsOnGround.contains(gameObject))
				Game.level.inanimateObjectsOnGround.remove(gameObject);

			// Remove from another gameObjects inventory
			if (gameObject.inventoryThatHoldsThisObject != null)
				gameObject.inventoryThatHoldsThisObject.remove(gameObject);

			// Add to this inventory's list of game objects
			gameObjects.add(gameObject);
			gameObject.inventoryThatHoldsThisObject = this;

			// Add to the inventory UI
			for (int i = 0; i < inventorySquares[0].length; i++) {
				for (int j = 0; j < inventorySquares.length; j++) {
					if (inventorySquares[j][i].gameObject == null) {
						inventorySquares[j][i].gameObject = gameObject;
						gameObject.inventorySquareGameObjectIsOn = inventorySquares[j][i];
						return;
					}
				}
			}

			gameObject.pickUpdateDateTime = new Date();
		}
	}

	public void remove(GameObject gameObject) {
		if (gameObjects.contains(gameObject)) {
			gameObjects.remove(gameObject);
			for (int i = 0; i < inventorySquares[0].length; i++) {
				for (int j = 0; j < inventorySquares.length; j++) {
					if (inventorySquares[j][i].gameObject == gameObject) {
						inventorySquares[j][i].gameObject.inventorySquareGameObjectIsOn = null;
						inventorySquares[j][i].gameObject = null;
						return;
					}
				}
			}
		}
	}

	public int size() {
		return gameObjects.size();
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(ArrayList<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
		int index = 0;
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {

				if (index >= gameObjects.size())
					return;

				// if (inventorySquares[j][i].gameObject == null) {
				inventorySquares[j][i].gameObject = gameObjects.get(index);
				gameObjects.get(index).inventorySquareGameObjectIsOn = inventorySquares[j][i];
				index++;
				// }
			}
		}
	}

	public boolean contains(GameObject gameObject) {
		return gameObjects.contains(gameObject);
	}

	public boolean contains(Class clazz) {
		for (GameObject gameObject : gameObjects) {
			if (clazz.isInstance(gameObject)) {
				return true;
			}
		}
		return false;
	}

	public boolean canShareSquare() {
		for (GameObject gameObject : gameObjects) {
			if (gameObject != null && !gameObject.canShareSquare)
				return false;
		}
		return true;
	}

	public boolean isPassable(Actor forActor) {
		for (GameObject gameObject : gameObjects) {

			if (forActor.group != null && forActor.group.getLeader() == gameObject)
				return false;

			if (gameObject != Game.level.player && gameObject instanceof Actor)
				return true;

			if (gameObject != null && !gameObject.canShareSquare)
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

	public boolean hasGameObjectsThatCanContainOtherObjects() {
		// TODO Auto-generated method stubArrayList<GameObject>
		for (GameObject gameObject : gameObjects) {
			if (gameObject.canContainOtherObjects)
				return true;
		}
		return false;
	}

	public GameObject getGameObectOfClass(Class clazz) {
		for (GameObject gameObject : gameObjects) {
			if (clazz.isInstance(gameObject)) {
				return gameObject;
			}
		}
		return null;
	}

	public ArrayList<GameObject> getGameObjectsThatCanContainOtherObjects() {
		ArrayList<GameObject> gameObjectsThatCanContainOtherObjects = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject.canContainOtherObjects)
				gameObjectsThatCanContainOtherObjects.add(gameObject);
		}
		return gameObjectsThatCanContainOtherObjects;
	}

	public Inventory makeCopy() {
		Inventory copy = new Inventory();
		for (GameObject gameObject : gameObjects) {
			copy.add(gameObject.makeCopy(null));
		}
		return copy;
	}

	public void drawBackground() {

	}

	public void drawForeground() {

	}

	@SuppressWarnings("unchecked")
	public void drawStaticUI() {

		System.out.println("SORTING");
		Collections.sort(gameObjects);
		this.setGameObjects(this.gameObjects);

		// if (isOpen) {
		int gameObjectIndex = 0;
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {

				inventorySquares[j][i].drawStaticUI();
				// gameObjectIndex = i * inventorySquares[i].length + j;
				// System.out.println("gameObjects.size() = " +
				// gameObjects.size());
				// System.out.println("gameObjectIndex = " + gameObjectIndex);
				// if (gameObjects.size() > gameObjectIndex) {
				//
				// System.out.println("gameObjects.get(gameObjectIndex) = " +
				// gameObjects.get(gameObjectIndex));
				// }
				// if (gameObjects.size() > gameObjectIndex &&
				// gameObjects.get(gameObjectIndex) != null) {
				//
				// TextureUtils.drawTexture(gameObjects.get(gameObjectIndex).imageTexture,
				// inventorySquares[j][i].xInPixels,
				// inventorySquares[j][i].xInPixels + Game.SQUARE_WIDTH,
				// inventorySquares[j][i].yInPixels,
				// inventorySquares[j][i].yInPixels + Game.SQUARE_HEIGHT);
				//
				// }

			}
		}

		if (this.inventorySquareMouseIsOver != null) {
			this.inventorySquareMouseIsOver.drawCursor();
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void open() {
		this.isOpen = true;
		Game.level.openInventories.add(this);
	}

	public void close() {
		this.isOpen = false;
		Game.level.openInventories.remove(this);
	}

	public boolean calculateIfPointInBoundsOfInventory(float mouseX, float mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			return true;
		}
		return false;
	}

	public void userInput() {

		this.inventorySquareMouseIsOver = null;
		for (int i = 0; i < inventorySquares[0].length; i++) {
			for (int j = 0; j < inventorySquares.length; j++) {
				if (inventorySquares[j][i].calculateIfPointInBoundsOfSquare(UserInputEditor.mouseXinPixels,
						Game.windowHeight - UserInputEditor.mouseYinPixels)) {
					this.inventorySquareMouseIsOver = inventorySquares[j][i];
				}
			}
		}
	}

	public void click() {
		if (this.inventorySquareMouseIsOver != null) {
			this.inventorySquareClicked(inventorySquareMouseIsOver);
		}
	}

	private void inventorySquareClicked(InventorySquare inventorySquare) {
		if (inventorySquare.gameObject == null) {
			// Nothing on the square
			if (inventoryState == INVENTORY_STATE.DEFAULT || inventoryState == INVENTORY_STATE.SETTINGS_CHANGE) {
				// selectSquare(square);
			} else if (inventoryState == INVENTORY_STATE.ADD_OBJECT) {
				// attemptToAddNewObjectToSquare(square);
			} else if (inventoryState == INVENTORY_STATE.MOVEABLE_OBJECT_SELECTED) {
				// swapGameObjects(this.selectedGameObject, gameObjectOnSquare);
				moveGameObject(this.selectedGameObject, inventorySquare);
			}
		} else {
			// There's an object on the square
			if (inventoryState == INVENTORY_STATE.DEFAULT || inventoryState == INVENTORY_STATE.SETTINGS_CHANGE) {
				selectGameObject(inventorySquare.gameObject);
			} else if (inventoryState == INVENTORY_STATE.MOVEABLE_OBJECT_SELECTED) {
				swapGameObjects(this.selectedGameObject, inventorySquare.gameObject);
			}
		}
	}

	private void selectGameObject(GameObject gameObject) {
		selectedGameObject = gameObject;
		inventoryState = INVENTORY_STATE.MOVEABLE_OBJECT_SELECTED;
	}

	public void swapGameObjects(GameObject gameObject1, GameObject gameObject2) {
		InventorySquare square1 = gameObject1.inventorySquareGameObjectIsOn;
		InventorySquare square2 = gameObject2.inventorySquareGameObjectIsOn;

		square1.gameObject = gameObject2;
		square2.gameObject = gameObject1;

		gameObject1.inventorySquareGameObjectIsOn = square2;
		gameObject2.inventorySquareGameObjectIsOn = square1;

	}

	public void moveGameObject(GameObject gameObject1, InventorySquare square2) {
		InventorySquare square1 = gameObject1.inventorySquareGameObjectIsOn;

		if (square1 != null)
			square1.gameObject = null;

		square2.gameObject = gameObject1;

		gameObject1.inventorySquareGameObjectIsOn = square2;
	}

}
