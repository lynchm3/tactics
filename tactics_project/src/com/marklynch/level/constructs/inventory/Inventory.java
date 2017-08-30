package com.marklynch.level.constructs.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.marklynch.Game;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.Door;
import com.marklynch.objects.Food;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Gold;
import com.marklynch.objects.Junk;
import com.marklynch.objects.WaterSource;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionLootAll;
import com.marklynch.objects.actions.ActionTakeSpecificItem;
import com.marklynch.objects.tools.ContainerForLiquids;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.weapons.Weapon;
import com.marklynch.ui.Draggable;
import com.marklynch.ui.Scrollable;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.LevelButton;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

import mdesl.graphics.Color;
import mdesl.graphics.Texture;

public class Inventory implements Draggable, Scrollable {

	public enum INVENTORY_STATE {
		DEFAULT, ADD_OBJECT, MOVEABLE_OBJECT_SELECTED, SETTINGS_CHANGE
	}

	public transient INVENTORY_STATE inventoryState = INVENTORY_STATE.DEFAULT;

	public enum INVENTORY_SORT_BY {
		SORT_ALPHABETICALLY, SORT_BY_NEWEST, SORT_BY_VALUE, SORT_BY_FAVOURITE, SORT_BY_TOTAL_DAMAGE, SORT_BY_SLASH_DAMAGE, SORT_BY_BLUNT_DAMAGE, SORT_BY_PIERCE_DAMAGE, SORT_BY_FIRE_DAMAGE, SORT_BY_WATER_DAMAGE, SORT_BY_POISON_DAMAGE, SORT_BY_ELECTRICAL_DAMAGE, SORT_BY_MAX_RANGE, SORT_BY_MIN_RANGE
	}

	public static transient INVENTORY_SORT_BY inventorySortBy = INVENTORY_SORT_BY.SORT_BY_NEWEST;

	public enum INVENTORY_FILTER_BY {
		FILTER_BY_ALL, FILTER_BY_WEAPON, FILTER_BY_FOOD, FILTER_BY_CONTAINER_FOR_LIQUIDS
	}

	public static transient INVENTORY_FILTER_BY inventoryFilterBy = INVENTORY_FILTER_BY.FILTER_BY_ALL;
	private static INVENTORY_FILTER_BY lastInventoryFilterBy = null;

	public enum INVENTORY_MODE {
		MODE_NORMAL, MODE_SELECT_ITEM_TO_FILL, MODE_SELECT_ITEM_TO_DROP, MODE_SELECT_ITEM_TO_THROW, MODE_SELECT_ITEM_TO_GIVE, MODE_SELECT_ITEM_TO_POUR, MODE_SELECT_MAP_MARKER, MODE_TRADE, MODE_LOOT
	}

	public static transient INVENTORY_MODE inventoryMode = INVENTORY_MODE.MODE_NORMAL;

	public static boolean sortBackwards = false;
	public int squareGridWidthInSquares = 5;
	public transient ArrayList<InventorySquare> inventorySquares = new ArrayList<InventorySquare>();
	public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<GameObject> filteredGameObjects = new ArrayList<GameObject>();

	private transient boolean isOpen = false;
	public transient float squaresX = 500;
	transient final static float squaresBaseY = 100;
	public final static float inventoryNamesY = 30f;

	transient float squaresY = 100;
	transient float sortButtonX = 400;
	transient float sortButtonWidth = 100;
	transient int actorX = 100;
	transient int actorWidth = 256;
	transient static int bottomBorderHeight = 256;
	transient static int topBorderHeight = 100;
	transient private InventorySquare inventorySquareMouseIsOver;
	transient private GameObject selectedGameObject;

	// Sort buttons
	static LevelButton selectedSortButton;
	public static String selectedSortButtonString;
	public static int selectedSortButtonLength;

	static LevelButton buttonSortAlphabetically;
	public final static String stringSortAlphabetically = "A - Z";
	public final static int lengthSortAlphabetically = Game.font.getWidth(stringSortAlphabetically);
	static LevelButton buttonSortByNewest;
	public final static String stringSortByNewest = "NEW";
	public final static int lengthSortByNewest = Game.font.getWidth(stringSortByNewest);
	static LevelButton buttonSortByFavourite;
	public final static String stringSortByFavourite = "FAV";
	public final static int lengthSortByFavourite = Game.font.getWidth(stringSortByFavourite);
	static LevelButton buttonSortByValue;
	public final static String stringSortByValue = "VALUE";
	public final static int lengthSortByValue = Game.font.getWidth(stringSortByValue);
	static LevelButton buttonSortByTotalDamage;
	public final static String stringSortByTotalDamage = "DMG";
	public final static int lengthSortByTotalDamage = Game.font.getWidth(stringSortByTotalDamage);
	static LevelButton buttonSortBySlashDamage;
	public final static String stringSortBySlashDamage = "SLASH DMG";
	public final static int lengthSortBySlashDamage = Game.font.getWidth(stringSortBySlashDamage);

	// Filter buttons
	static LevelButton buttonFilterByAll;
	static LevelButton buttonFilterByWeapon;
	static LevelButton buttonFilterByFood;

	// Empty text
	public static final String stringEmpty = "Empty";
	public static final int lengthEmpty = Game.font.getWidth(stringEmpty);

	// SHIFT text
	String stringShiftDrop = "[SHIFT] Drop";
	int lengthShiftDrop = Game.font.getWidth(stringShiftDrop);
	String stringShiftPut = "[SHIFT] Put";
	int lengthShiftPut = Game.font.getWidth(stringShiftPut);

	float textShiftX = 0;

	// Color beind inventory squares
	public final static Color inventoryAreaColor = new Color(1f, 1f, 1f, 0.25f);

	// Close button
	static LevelButton buttonClose;

	// Loot all button
	public static LevelButton buttonLootAll;

	public static ArrayList<Button> buttons;
	public static ArrayList<Button> buttonsSort;
	public static ArrayList<Button> buttonsFilter;

	public InventoryParent parent;
	public GroundDisplay groundDisplay;
	public Inventory otherInventory;

	public static float squaresWidth;
	public static float squaresHeight;

	public static WaterSource waterSource;
	public static Square square;
	public static Object target;

	public static Texture textureUp;
	public static Texture textureDown;
	public static Texture textureCorner;
	public static Texture textureFilter;
	public static Texture textureBag;

	public Inventory(GameObject... gameObjects) {
		for (GameObject gameObject : gameObjects) {
			add(gameObject);
		}

	}

	public void open() {
		buttons = new ArrayList<Button>();
		buttonsSort = new ArrayList<Button>();
		buttonsFilter = new ArrayList<Button>();

		buttonSortAlphabetically = new LevelButton(sortButtonX, 100f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortAlphabetically, true, true, Color.BLACK, Color.WHITE);
		buttonSortAlphabetically.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_ALPHABETICALLY, true, true);
			}
		});
		buttonsSort.add(buttonSortAlphabetically);

		buttonSortByNewest = new LevelButton(sortButtonX, 150f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortByNewest, true, true, Color.BLACK, Color.WHITE);
		buttonSortByNewest.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_BY_NEWEST, true, true);
			}
		});
		buttonsSort.add(buttonSortByNewest);

		buttonSortByFavourite = new LevelButton(sortButtonX, 200f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortByFavourite, true, true, Color.BLACK, Color.WHITE);
		buttonSortByFavourite.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_BY_FAVOURITE, true, true);
			}
		});
		buttonsSort.add(buttonSortByFavourite);

		buttonSortByValue = new LevelButton(sortButtonX, 250f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortByValue, true, true, Color.BLACK, Color.WHITE);
		buttonSortByValue.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_BY_VALUE, true, true);
			}
		});
		buttonsSort.add(buttonSortByValue);

		buttonSortByTotalDamage = new LevelButton(sortButtonX, 300f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortByTotalDamage, true, true, Color.BLACK, Color.WHITE);
		buttonSortByTotalDamage.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_BY_TOTAL_DAMAGE, true, true);
			}
		});
		buttonsSort.add(buttonSortByTotalDamage);

		buttonSortBySlashDamage = new LevelButton(sortButtonX, 350f, sortButtonWidth, 30f, "end_turn_button.png",
				"end_turn_button.png", stringSortBySlashDamage, true, true, Color.BLACK, Color.WHITE);
		buttonSortBySlashDamage.setClickListener(new ClickListener() {
			@Override
			public void click() {
				sort(INVENTORY_SORT_BY.SORT_BY_SLASH_DAMAGE, true, true);
			}
		});
		buttonsSort.add(buttonSortBySlashDamage);

		buttonFilterByAll = new LevelButton(sortButtonX + 100f, squaresY - 30, 100f, 30f, "end_turn_button.png",
				"end_turn_button.png", "ALL", true, true, Color.BLACK, Color.WHITE);
		buttonFilterByAll.setClickListener(new ClickListener() {
			@Override
			public void click() {
				filter(INVENTORY_FILTER_BY.FILTER_BY_ALL, false);
			}
		});
		buttonsFilter.add(buttonFilterByAll);

		buttonFilterByWeapon = new LevelButton(sortButtonX + 200f, squaresY - 30, 100f, 30f, "end_turn_button.png",
				"end_turn_button.png", "WEAPONS", true, true, Color.BLACK, Color.WHITE);
		buttonFilterByWeapon.setClickListener(new ClickListener() {
			@Override
			public void click() {
				filter(INVENTORY_FILTER_BY.FILTER_BY_WEAPON, false);
			}
		});
		buttonsFilter.add(buttonFilterByWeapon);

		buttonFilterByFood = new LevelButton(sortButtonX + 300f, squaresY - 30, 100f, 30f, "end_turn_button.png",
				"end_turn_button.png", "FOOD", true, true, Color.BLACK, Color.WHITE);
		buttonFilterByFood.setClickListener(new ClickListener() {
			@Override
			public void click() {
				filter(INVENTORY_FILTER_BY.FILTER_BY_FOOD, false);
			}
		});
		buttonsFilter.add(buttonFilterByFood);

		buttonLootAll = new LevelButton(900f, bottomBorderHeight, 100f, 30f, "end_turn_button.png",
				"end_turn_button.png", "LOOT ALL [A]", true, false, Color.BLACK, Color.WHITE);
		buttonLootAll.setClickListener(new ClickListener() {
			@Override
			public void click() {
				if (groundDisplay == null) {
					new ActionLootAll(Game.level.player, (GameObject) target).perform();
				} else {
					ArrayList<Action> actionsToPerform = new ArrayList<Action>();
					for (Square square : groundDisplay.squares) {
						for (GameObject gameObject : square.inventory.gameObjects) {
							if (gameObject.fitsInInventory) {
								Action action = new ActionTakeSpecificItem(Game.level.player, square, gameObject);
								if (action.legal) {
									actionsToPerform.add(action);
								}
							}
						}
					}
					for (Action action : actionsToPerform) {
						action.perform();
					}
					groundDisplay.refreshGameObjects();
				}
				Game.level.openCloseInventory();
			}
		});

		if (inventoryMode == INVENTORY_MODE.MODE_NORMAL || inventoryMode == INVENTORY_MODE.MODE_LOOT) {
			buttons.add(buttonLootAll);
		}

		buttonClose = new LevelButton(Game.halfWindowWidth - 25f, bottomBorderHeight, 70f, 30f, "end_turn_button.png",
				"end_turn_button.png", "CLOSE [I]", true, false, Color.BLACK, Color.WHITE);
		buttonClose.setClickListener(new ClickListener() {
			@Override
			public void click() {
				Game.level.openCloseInventory();
			}
		});
		buttons.add(buttonClose);

		if (inventoryMode == INVENTORY_MODE.MODE_NORMAL || inventoryMode == INVENTORY_MODE.MODE_LOOT
				|| inventoryMode == INVENTORY_MODE.MODE_TRADE
				|| inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_DROP
				|| inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_GIVE
				|| inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_THROW) {
			buttons.addAll(buttonsFilter);
			buttons.addAll(buttonsSort);
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_FILL
				|| inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_POUR) {
			buttons.addAll(buttonsSort);
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_MAP_MARKER) {

		}

		this.isOpen = true;
		if (!Game.level.openInventories.contains(this))
			Game.level.openInventories.add(this);

		this.groundDisplay = null;
		if (inventoryMode == INVENTORY_MODE.MODE_NORMAL || inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_DROP)
			this.groundDisplay = new GroundDisplay(900, 100);

		if (inventoryMode == INVENTORY_MODE.MODE_TRADE || inventoryMode == INVENTORY_MODE.MODE_LOOT) {
			otherInventory.isOpen = true;
		}
	}

	public void close() {
		this.isOpen = false;
		if (Inventory.lastInventoryFilterBy != null) {
			Inventory.inventoryFilterBy = lastInventoryFilterBy;
		}

		if (Game.level.openInventories.contains(this))
			Game.level.openInventories.remove(this);
		this.inventorySquares = new ArrayList<InventorySquare>();
		this.groundDisplay = null;
		if (this.otherInventory != null) {
			otherInventory.isOpen = false;
			this.otherInventory.inventorySquares = new ArrayList<InventorySquare>();
			this.otherInventory = null;
		}
	}

	public void sort(INVENTORY_SORT_BY inventorySortBy, boolean filterFirst, boolean fromSortButtonPress) {

		if (otherInventory != null) {
			otherInventory.sort(inventorySortBy, filterFirst, false);
		}

		// Button selectedSortButton = null;

		if (inventorySortBy == INVENTORY_SORT_BY.SORT_ALPHABETICALLY) {
			selectedSortButton = buttonSortAlphabetically;
			selectedSortButtonString = stringSortAlphabetically;
			selectedSortButtonLength = lengthSortAlphabetically;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_NEWEST) {
			selectedSortButton = buttonSortByNewest;
			selectedSortButtonString = stringSortByNewest;
			selectedSortButtonLength = lengthSortByNewest;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_VALUE) {
			selectedSortButton = buttonSortByValue;
			selectedSortButtonString = stringSortByValue;
			selectedSortButtonLength = lengthSortByValue;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_FAVOURITE) {
			selectedSortButton = buttonSortByFavourite;
			selectedSortButtonString = stringSortByFavourite;
			selectedSortButtonLength = lengthSortByFavourite;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_TOTAL_DAMAGE) {
			selectedSortButton = buttonSortByTotalDamage;
			selectedSortButtonString = stringSortByTotalDamage;
			selectedSortButtonLength = lengthSortByTotalDamage;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_SLASH_DAMAGE) {
			selectedSortButton = buttonSortBySlashDamage;
			selectedSortButtonString = stringSortBySlashDamage;
			selectedSortButtonLength = lengthSortBySlashDamage;
		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_BLUNT_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_PIERCE_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_FIRE_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_WATER_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_POISON_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_ELECTRICAL_DAMAGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_MAX_RANGE) {

		} else if (inventorySortBy == INVENTORY_SORT_BY.SORT_BY_MIN_RANGE) {

		}

		if (fromSortButtonPress) {
			if (selectedSortButton.down == true) {
				sortBackwards = !sortBackwards;
			} else {
				sortBackwards = false;
			}
		}

		for (Button button : buttonsSort) {
			button.down = false;
			button.x = this.sortButtonX;
			button.width = this.sortButtonWidth;
		}

		selectedSortButton.down = true;
		selectedSortButton.x -= 20;
		selectedSortButton.width += 20;

		Inventory.inventorySortBy = inventorySortBy;

		if (filterFirst) {
			filter(this.inventoryFilterBy, false);
			return;
		}

		// sorts filtered game objects
		Collections.sort(filteredGameObjects);
		if (sortBackwards)
			Collections.reverse(filteredGameObjects);

		matchGameObjectsToSquares();
	}

	public void filter(INVENTORY_FILTER_BY inventoryFilterBy, boolean temporary) {

		if (inventoryFilterBy != Inventory.inventoryFilterBy) {
			if (temporary) {
				Inventory.lastInventoryFilterBy = Inventory.inventoryFilterBy;
				Inventory.inventoryFilterBy = inventoryFilterBy;
			} else {
				Inventory.inventoryFilterBy = inventoryFilterBy;
				Inventory.lastInventoryFilterBy = null;
			}
		}

		for (Button button : buttonsFilter)
			button.down = false;
		if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_ALL) {
		} else if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_WEAPON) {
		}
		filteredGameObjects.clear();
		if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_ALL) {
			buttonFilterByAll.down = true;
			filteredGameObjects.addAll(gameObjects);
		} else if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_WEAPON) {
			buttonFilterByWeapon.down = true;
			for (GameObject gameObject : gameObjects) {
				if (gameObject instanceof Weapon) {
					filteredGameObjects.add(gameObject);
				}
			}
		} else if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_FOOD) {
			buttonFilterByFood.down = true;
			for (GameObject gameObject : gameObjects) {
				if (gameObject instanceof Food) {
					filteredGameObjects.add(gameObject);
				}
			}
		} else if (inventoryFilterBy == INVENTORY_FILTER_BY.FILTER_BY_CONTAINER_FOR_LIQUIDS) {
			buttonFilterByFood.down = true;
			for (GameObject gameObject : gameObjects) {
				if (gameObject instanceof ContainerForLiquids) {
					filteredGameObjects.add(gameObject);
				}
			}
		}

		if (otherInventory != null) {
			otherInventory.filter(inventoryFilterBy, temporary);
		}

		sort(Inventory.inventorySortBy, false, false);
	}

	public void postLoad1() {

		new ArrayList<InventorySquare>();

		// Tell objects they're in this inventory
		for (GameObject gameObject : gameObjects) {
			gameObject.inventoryThatHoldsThisObject = this;
			gameObject.postLoad1();
		}

		int index = 0;

		// Put objects in inventory
		// for (int i = 0; i < inventorySquares[0].length; i++) {
		// for (int j = 0; j < inventorySquares.length; j++) {
		//
		// if (index >= gameObjects.size())
		// return;
		//
		// // if (inventorySquares[j][i].gameObject == null) {
		// // inventorySquares[j][i].gameObject = gameObjects.get(index);
		// // gameObjects.get(index).inventorySquareGameObjectIsOn =
		// // inventorySquares[j][i];
		// // index++;
		// // }
		// }
		// }
	}

	public void postLoad2() {
		// Tell objects they're in this inventory
		for (GameObject gameObject : gameObjects) {
			gameObject.inventoryThatHoldsThisObject = this;
			gameObject.postLoad2();
		}

	}

	public static void loadStaticImages() {
		textureUp = ResourceUtils.getGlobalImage("inventory_up.png");
		textureDown = ResourceUtils.getGlobalImage("inventory_down.png");
		textureCorner = ResourceUtils.getGlobalImage("inventory_corner.png");
		textureFilter = ResourceUtils.getGlobalImage("filter.png");
		textureBag = ResourceUtils.getGlobalImage("bag.png");
	}

	public GameObject get(int i) {
		return gameObjects.get(i);
	}

	public void add(GameObject gameObject) {
		add(gameObject, -1);
	}

	public void add(GameObject gameObject, int index) {
		if (!gameObjects.contains(gameObject)) {

			gameObject.toSell = false;

			// Remove references with square
			if (gameObject.squareGameObjectIsOn != null) {
				gameObject.squareGameObjectIsOn.inventory.remove(gameObject);
				gameObject.squareGameObjectIsOn.inventory.matchGameObjectsToSquares();
			}
			gameObject.squareGameObjectIsOn = null;

			// Remove from ground squares index
			if (Game.level.inanimateObjectsOnGround.contains(gameObject))
				Game.level.inanimateObjectsOnGround.remove(gameObject);

			// Remove from another gameObjects inventory
			if (gameObject.inventoryThatHoldsThisObject != null) {
				Inventory oldInventory = gameObject.inventoryThatHoldsThisObject;
				oldInventory.remove(gameObject);
				oldInventory.matchGameObjectsToSquares();
			}

			// Add to this inventory's list of game objects
			gameObjects.add(gameObject);
			gameObject.inventoryThatHoldsThisObject = this;

			// this.sort(inventorySortBy);

			// pick up date for sorting by newest
			gameObject.pickUpdateDateTime = new Date();

			if (parent != null)
				parent.inventoryChanged();

			if (index != -1) {
				filteredGameObjects.remove(index);
				filteredGameObjects.add(index, gameObject);
			} else {
				filteredGameObjects.add(gameObject);
			}
			matchGameObjectsToSquares();
			if (groundDisplay != null)
				groundDisplay.refreshGameObjects();

		}
	}

	public int remove(GameObject gameObject) {
		int index = -1;
		if (gameObjects.contains(gameObject)) {

			if (this.parent instanceof Actor) {
				Actor actor = (Actor) parent;
				if (actor.equipped == gameObject)
					actor.equipped = null;
			}

			gameObjects.remove(gameObject);

			gameObject.inventoryThatHoldsThisObject = null;
			if (parent != null)
				parent.inventoryChanged();
			// this.sort(inventorySortBy);
			if (filteredGameObjects.contains(gameObject)) {
				index = filteredGameObjects.indexOf(gameObject);
				filteredGameObjects.remove(gameObject);
				// filteredGameObjects.set(filteredGameObjects.indexOf(gameObject),
				// null);
			}
			this.matchGameObjectsToSquares();
			if (groundDisplay != null)
				groundDisplay.refreshGameObjects();
		}
		return index;
	}

	// public void replace(GameObject out, GameObject in) {
	// int index = this.remove(out);
	// this.add(in, index);
	//
	// }

	public int size() {
		return gameObjects.size();
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public HashMap<String, Integer> itemTypeCount = new HashMap<String, Integer>();

	public void matchGameObjectsToSquares() {

		/*
		 * 
		 * So... merging items What gets merged? Junk Food
		 * 
		 * 
		 * fuck... how the fuck do i even match them...
		 * 
		 * By name i gues... So... if(Food || Junk){show #} ughck
		 * 
		 * 
		 * 
		 */

		// System.out.println("matchGameObjectsToSquares");

		if (!isOpen)
			return;

		itemTypeCount.clear();

		inventorySquares.clear();

		ArrayList<String> alreadyAdded = new ArrayList<String>();

		for (GameObject gameObject : this.filteredGameObjects) {

			if (this.parent == Game.level.player && gameObject instanceof Gold)
				continue;

			if (inventoryMode == INVENTORY_MODE.MODE_TRADE && gameObject instanceof Gold)
				continue;

			if (gameObject.value == 0 && gameObject instanceof Gold)
				continue;

			if (alreadyAdded.contains(gameObject.name)) {
				itemTypeCount.put(gameObject.name, itemTypeCount.get(gameObject.name) + 1);

			} else {

				InventorySquare inventorySquare = new InventorySquare(0, 0, null, this);
				inventorySquare.gameObject = gameObject;
				inventorySquares.add(inventorySquare);
				alreadyAdded.add(gameObject.name);
				itemTypeCount.put(gameObject.name, 1);
			}
		}

		// Code to fill up space with empty sqrs
		// int squareAreaHeightInSquares = (int) ((Game.windowHeight -
		// bottomBorderHeight - topBorderHeight)
		// / Game.INVENTORY_SQUARE_HEIGHT);
		// int squaresRequiredToFillSpace = this.squareGridWidthInSquares *
		// squareAreaHeightInSquares;
		// while (inventorySquares.size() < squaresRequiredToFillSpace) {
		// InventorySquare inventorySquare = new InventorySquare(0, 0, null,
		// this);
		// inventorySquares.add(inventorySquare);
		// }

		// System.out.println(x);
		if (inventoryMode != INVENTORY_MODE.MODE_SELECT_MAP_MARKER && this.parent != Game.level.player) {
			Game.level.player.inventory.matchGameObjectsToSquares();
			return;
		}

		resize1();
	}

	public void resize1() {
		if (!isOpen)
			return;

		float pixelsToLeftOfSquares = this.sortButtonX + sortButtonWidth;
		float pixelsToRightOfSquares = actorWidth;
		float pixelsBetweenSquares = Game.INVENTORY_SQUARE_WIDTH;
		float availablePixelsForSquares = Game.windowWidth
				- (pixelsToLeftOfSquares + pixelsBetweenSquares + pixelsToRightOfSquares);
		this.squareGridWidthInSquares = (int) ((availablePixelsForSquares / 2f) / Game.INVENTORY_SQUARE_WIDTH);
		if (this.squareGridWidthInSquares < 1)
			this.squareGridWidthInSquares = 1;
		squaresWidth = squareGridWidthInSquares * Game.INVENTORY_SQUARE_WIDTH;
		this.squaresX = pixelsToLeftOfSquares;
		squaresHeight = Game.windowHeight - Inventory.topBorderHeight - Inventory.bottomBorderHeight;

		if (otherInventory != null) {
			otherInventory.squareGridWidthInSquares = this.squareGridWidthInSquares;
			otherInventory.squaresX = pixelsToLeftOfSquares + (squareGridWidthInSquares * Game.INVENTORY_SQUARE_WIDTH)
					+ pixelsBetweenSquares;
			otherInventory.actorX = (int) (otherInventory.squaresX + squaresWidth);
		}
		if (groundDisplay != null) {
			groundDisplay.squareGridWidthInSquares = this.squareGridWidthInSquares;
			groundDisplay.squaresX = (int) (pixelsToLeftOfSquares
					+ (squareGridWidthInSquares * Game.INVENTORY_SQUARE_WIDTH) + pixelsBetweenSquares);
		}

		fixScroll();
		resize2();
		buttonClose.x = squaresX;
		textShiftX = squaresX + buttonClose.width;

		if (this.groundDisplay != null) {
			this.groundDisplay.fixScroll();
			this.groundDisplay.resize2();
			buttonLootAll.x = groundDisplay.squaresX;
		}
		if (this.otherInventory != null) {
			this.otherInventory.fixScroll();
			this.otherInventory.resize2();
			buttonLootAll.x = this.otherInventory.squaresX;
		}
	}

	public void resize2() {
		if (!isOpen)
			return;
		int xIndex = 0;
		int yIndex = 0;
		for (InventorySquare inventorySquare : inventorySquares) {
			inventorySquare.xInGrid = xIndex;
			inventorySquare.yInGrid = yIndex;
			inventorySquare.xInPixels = Math
					.round(this.squaresX + inventorySquare.xInGrid * Game.INVENTORY_SQUARE_WIDTH);
			inventorySquare.yInPixels = Math
					.round(this.squaresY + inventorySquare.yInGrid * Game.INVENTORY_SQUARE_HEIGHT);
			xIndex++;
			if (xIndex == this.squareGridWidthInSquares) {
				xIndex = 0;
				yIndex++;
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

	public boolean canBeMovedTo() {
		if (canShareSquare()) {
			return true;
		} else {
			if (contains(Actor.class))
				return true;
		}
		return false;
	}

	public boolean isPassable(Actor forActor) {
		for (GameObject gameObject : gameObjects) {

			// Can't go through group leader
			if (forActor.group != null && forActor.group.getLeader() == gameObject)
				return false;

			// Can't go through player
			if (gameObject == Game.level.player)
				return false;

			// Can't got through impassable objects
			if (!gameObject.canShareSquare && !(gameObject instanceof Door) && !(gameObject instanceof Actor))
				return false;

			// Can't go through locked doors u dont have the key for
			if (gameObject instanceof Door) {
				Door door = (Door) gameObject;
				if (door.locked && !forActor.hasKeyForDoor(door)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isPassable(GameObject forGameObject) {

		if (forGameObject instanceof Actor)
			return isPassable((Actor) forGameObject);

		return this.canShareSquare();
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

	public GameObject getGameObjectOfClass(Class clazz) {
		for (GameObject gameObject : gameObjects) {
			if (clazz.isInstance(gameObject)) {
				return gameObject;
			}
		}
		return null;
	}

	public ArrayList<GameObject> getGameObjectsOfClass(Class clazz) {
		ArrayList<GameObject> gameObjectsToReturn = new ArrayList<GameObject>();
		for (GameObject gameObject : this.gameObjects) {
			if (clazz.isInstance(gameObject)) {
				gameObjectsToReturn.add(gameObject);
			}
		}
		return gameObjectsToReturn;
	}

	public ArrayList<GameObject> getGameObjectsThatCanContainOtherObjects() {
		ArrayList<GameObject> gameObjectsThatCanContainOtherObjects = new ArrayList<GameObject>();
		for (GameObject gameObject : gameObjects) {
			if (gameObject.canContainOtherObjects)
				gameObjectsThatCanContainOtherObjects.add(gameObject);
		}
		return gameObjectsThatCanContainOtherObjects;
	}

	public void drawBackground() {

	}

	public void drawForeground() {

	}

	public final static Color backgroundColor = new Color(0f, 0f, 0f, 1f);

	@SuppressWarnings("unchecked")
	public void drawStaticUI() {

		// Black cover
		QuadUtils.drawQuad(backgroundColor, 0, Game.windowWidth, 0, Game.windowHeight);

		drawBorder();

		// Draw bag textureBag
		float bagTextureX = this.squaresX + Inventory.squaresWidth / 2 - textureBag.getWidth();
		float bagTextureY = this.squaresBaseY + Inventory.squaresHeight / 2 - textureBag.getHeight();
		TextureUtils.drawTexture(textureBag, 0.5f, bagTextureX, bagTextureY, bagTextureX + textureBag.getWidth() * 2,
				bagTextureY + textureBag.getHeight() * 2);

		// sqrs
		drawSquares();

		// Ground display sqrs
		if (groundDisplay != null) {
			groundDisplay.drawStaticUI();
			if (groundDisplay.gameObjects.size() > 0) {
				groundDisplay.drawSquares();
			} else {
				float emptyStringX = groundDisplay.squaresX + this.squaresWidth / 2 - GroundDisplay.lengthEmpty / 2;
				float emptyStringY = groundDisplay.squaresY + this.squaresHeight / 2 - 10;
				QuadUtils.drawQuad(Color.BLACK, emptyStringX - 8, emptyStringX + GroundDisplay.lengthEmpty + 8,
						emptyStringY - 8, emptyStringY + 28);
				TextUtils.printTextWithImages(emptyStringX, emptyStringY, Integer.MAX_VALUE, false,
						new Object[] { GroundDisplay.stringEmpty });
			}
			if (groundDisplay.gameObjects.size() == 0)
				this.buttonLootAll.enabled = false;
			else
				this.buttonLootAll.enabled = true;
		}

		// Other Gameobject / actor inventory squares
		if (otherInventory != null) {
			otherInventory.drawBorder();
			GameObject otherGameObject = (GameObject) target;
			float otherTextureX = otherInventory.squaresX + otherInventory.squaresWidth / 2 - (otherGameObject.width);
			float otherTextureY = otherInventory.squaresBaseY + otherInventory.squaresHeight / 2
					- (otherGameObject.height);
			if (otherGameObject instanceof Actor) {
				int actorPositionXInPixels = otherInventory.actorX;
				int actorPositionYInPixels = (int) (squaresBaseY + squaresHeight / 2
						- (otherGameObject.imageTexture.getHeight()));
				TextureUtils.drawTexture(otherGameObject.imageTexture, 1f, actorPositionXInPixels,
						actorPositionYInPixels, actorPositionXInPixels + otherGameObject.width * 2,
						actorPositionYInPixels + otherGameObject.height * 2, true);
				TextureUtils.drawTexture(textureBag, 0.5f, otherTextureX, otherTextureY,
						otherTextureX + otherGameObject.width * 2, otherTextureY + otherGameObject.height * 2, true);
			} else {
				TextureUtils.drawTexture(otherGameObject.imageTexture, 0.5f, otherTextureX, otherTextureY,
						otherTextureX + otherGameObject.width * 2, otherTextureY + otherGameObject.height * 2);

			}
			if (otherInventory.size() > 0) {
				otherInventory.drawSquares();
			} else {
				float emptyStringX = otherInventory.squaresX + otherInventory.squaresWidth / 2 - lengthEmpty / 2;
				float emptyStringY = otherInventory.squaresY + otherInventory.squaresHeight / 2 - 10;
				QuadUtils.drawQuad(Color.BLACK, emptyStringX - 8, emptyStringX + lengthEmpty + 8, emptyStringY - 8,
						emptyStringY + 28);
				TextUtils.printTextWithImages(emptyStringX, emptyStringY, Integer.MAX_VALUE, false,
						new Object[] { stringEmpty });
			}
			if (otherInventory.size() == 0)
				this.buttonLootAll.enabled = false;
			else
				this.buttonLootAll.enabled = true;
		}

		// cursor and action over squares
		if (this.inventorySquareMouseIsOver != null && Game.buttonHoveringOver == null) {
			this.inventorySquareMouseIsOver.drawCursor();
			this.inventorySquareMouseIsOver.drawAction(false);

		}

		// Top border black mask
		QuadUtils.drawQuad(backgroundColor, 0, Game.windowWidth, 0, topBorderHeight);

		// Bottom border black mask
		QuadUtils.drawQuad(backgroundColor, 0, Game.windowWidth, Game.windowHeight - bottomBorderHeight,
				Game.windowHeight);

		// [SHIFT]
		if (groundDisplay != null) {
			TextUtils.printTextWithImages(textShiftX, Game.windowHeight - bottomBorderHeight, Integer.MAX_VALUE, false,
					new Object[] { stringShiftDrop });
		}

		if (otherInventory != null && inventoryMode != INVENTORY_MODE.MODE_TRADE) {
			TextUtils.printTextWithImages(textShiftX, Game.windowHeight - bottomBorderHeight, Integer.MAX_VALUE, false,
					new Object[] { stringShiftPut });
		}

		// text
		if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_FILL) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select a Container to Fill", Color.WHITE) });
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_POUR) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select a Container to Pour Out", Color.WHITE) });
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_MAP_MARKER) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select a Map Marker", Color.WHITE) });
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_DROP) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select an Item to Drop", Color.WHITE) });
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_GIVE) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select an Item to Give", Color.WHITE) });
		} else if (inventoryMode == INVENTORY_MODE.MODE_SELECT_ITEM_TO_THROW) {
			TextUtils.printTextWithImages(100f, 8f, 300f, true,
					new Object[] { new StringWithColor("Please Select an Item to Throw", Color.WHITE) });
		}

		TextUtils.printTextWithImages(this.squaresX, inventoryNamesY, 300f, true, "Bag");

		if (groundDisplay != null) {
			groundDisplay.drawText();
		}

		if (otherInventory != null) {
			otherInventory.drawOtherInventoryText();
		}

		// Actor
		int actorPositionXInPixels = this.actorX;
		int actorPositionYInPixels = (int) (squaresBaseY + squaresHeight / 2
				- (Game.level.player.imageTexture.getHeight()));
		float alpha = 1.0f;
		TextureUtils.drawTexture(Game.level.player.imageTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
				actorPositionXInPixels + actorWidth, actorPositionYInPixels + Game.level.player.height * 2);

		GameObject gameObjectMouseIsOver = null;

		GameObject gameObjectToDrawInPlayersHand = null;
		GameObject gameObjectToDrawOnPlayersHead = Game.level.player.helmet;
		GameObject gameObjectToDrawOnPlayersBody = Game.level.player.bodyArmor;
		GameObject gameObjectToDrawOnPlayersLegs = Game.level.player.legArmor;

		// if (Game.squareMouseIsOver != null && Game.squareMouseIsOver
		// instanceof InventorySquare) {
		// // Preview weapon
		// gameObjectMouseIsOver = ((InventorySquare)
		// Game.squareMouseIsOver).gameObject;
		//
		// if (gameObjectMouseIsOver instanceof Helmet) {
		// gameObjectToDrawOnPlayersHead = gameObjectMouseIsOver;
		// } else if (gameObjectMouseIsOver instanceof BodyArmor) {
		// gameObjectToDrawOnPlayersBody = gameObjectMouseIsOver;
		// } else if (gameObjectMouseIsOver instanceof LegArmor) {
		// gameObjectToDrawOnPlayersLegs = gameObjectMouseIsOver;
		// } else {
		// gameObjectToDrawInPlayersHand = gameObjectMouseIsOver;
		// }
		//
		// }

		if (gameObjectToDrawInPlayersHand == null) {
			gameObjectToDrawInPlayersHand = Game.level.player.equipped;
		}

		// Object to draw player holding
		if (gameObjectToDrawInPlayersHand != null) {
			int weaponPositionXInPixels = (int) (actorPositionXInPixels
					+ (Game.level.player.handAnchorX - gameObjectToDrawInPlayersHand.anchorX) * 2);
			int weaponPositionYInPixels = (int) (actorPositionYInPixels
					+ (Game.level.player.handAnchorY - gameObjectToDrawInPlayersHand.anchorY) * 2);
			TextureUtils.drawTexture(gameObjectToDrawInPlayersHand.imageTexture, alpha, weaponPositionXInPixels,
					weaponPositionYInPixels, weaponPositionXInPixels + gameObjectToDrawInPlayersHand.width * 2,
					weaponPositionYInPixels + gameObjectToDrawInPlayersHand.height * 2);
		}

		// Helmet
		if (gameObjectToDrawOnPlayersHead != null) {
			int helmetPositionXInPixels = (int) (actorPositionXInPixels
					+ (Game.level.player.headAnchorX - gameObjectToDrawOnPlayersHead.anchorX) * 2);
			int helmetPositionYInPixels = (int) (actorPositionYInPixels
					+ (Game.level.player.headAnchorY - gameObjectToDrawOnPlayersHead.anchorY) * 2);
			TextureUtils.drawTexture(gameObjectToDrawOnPlayersHead.imageTexture, alpha, helmetPositionXInPixels,
					helmetPositionYInPixels, helmetPositionXInPixels + gameObjectToDrawOnPlayersHead.width * 2,
					helmetPositionYInPixels + gameObjectToDrawOnPlayersHead.height * 2);
		} else if (Game.level.player.hairImageTexture != null) {
			int helmetPositionXInPixels = actorPositionXInPixels + 0 * 2;
			int helmetPositionYInPixels = actorPositionYInPixels + -8 * 2;
			TextureUtils.drawTexture(Game.level.player.hairImageTexture, alpha, helmetPositionXInPixels,
					helmetPositionYInPixels,
					helmetPositionXInPixels + Game.level.player.hairImageTexture.getWidth() * 4,
					helmetPositionYInPixels + Game.level.player.hairImageTexture.getHeight() * 4);
		}

		// Body Armor
		if (gameObjectToDrawOnPlayersBody != null) {
			int bodyArmorPositionXInPixels = (int) (actorPositionXInPixels
					+ (Game.level.player.bodyAnchorX - gameObjectToDrawOnPlayersBody.anchorX) * 2);
			int bodyArmorPositionYInPixels = (int) (actorPositionYInPixels
					+ (Game.level.player.bodyAnchorY - gameObjectToDrawOnPlayersBody.anchorY) * 2);
			TextureUtils.drawTexture(gameObjectToDrawOnPlayersBody.imageTexture, alpha, bodyArmorPositionXInPixels,
					bodyArmorPositionYInPixels, bodyArmorPositionXInPixels + actorWidth,
					bodyArmorPositionYInPixels + gameObjectToDrawOnPlayersBody.height * 2);
		}

		// Leg Armor
		if (gameObjectToDrawOnPlayersLegs != null) {
			int legArmorPositionXInPixels = (int) (actorPositionXInPixels
					+ (Game.level.player.legsAnchorX - gameObjectToDrawOnPlayersLegs.anchorX) * 2);
			int legArmorPositionYInPixels = (int) (actorPositionYInPixels
					+ (Game.level.player.legsAnchorY - gameObjectToDrawOnPlayersLegs.anchorY) * 2);
			TextureUtils.drawTexture(gameObjectToDrawOnPlayersLegs.imageTexture, alpha, legArmorPositionXInPixels,
					legArmorPositionYInPixels, legArmorPositionXInPixels + actorWidth,
					legArmorPositionYInPixels + gameObjectToDrawOnPlayersLegs.height * 2);
		}

		// Weapon comparison
		if (this.inventorySquareMouseIsOver != null && this.inventorySquareMouseIsOver.gameObject instanceof Weapon) {

			int comparisonPositionXInPixels = 1150;
			int comparisonPositionYInPixels = 250;
		}

		// Top left corner
		// TextureUtils.drawTexture(textureCorner, squaresX - 100, squaresY -
		// 100, squaresX, squaresY);

		// LineUtils.drawLine(Color.WHITE, squaresX - 200, squaresY - 200,
		// squaresX, squaresY, 10);

		// buttons
		for (Button button : buttons) {
			button.draw();
		}

		// Up / down icon on active sort button
		if (buttons.contains(selectedSortButton)) {
			if (sortBackwards)
				TextureUtils.drawTexture(textureUp, selectedSortButton.x + selectedSortButtonLength + 5,
						selectedSortButton.y + 5, selectedSortButton.x + selectedSortButtonLength + 15,
						selectedSortButton.y + 15);
			else
				TextureUtils.drawTexture(textureDown, selectedSortButton.x + selectedSortButtonLength + 5,
						selectedSortButton.y + 5, selectedSortButton.x + selectedSortButtonLength + 15,
						selectedSortButton.y + 15);
		}

		// Up / down icon on active sort button
		for (Button filterButton : buttonsFilter) {
			if (filterButton.down && buttons.contains(filterButton)) {
				TextureUtils.drawTexture(textureFilter, filterButton.x - 5, filterButton.y - 5, filterButton.x + 5,
						filterButton.y + 5);
			}
		}

		// Gold for this actor
		if (this.parent instanceof Actor) {
			Actor actor = (Actor) this.parent;
			String goldText = "Gold: " + actor.getCarriedGoldValue();
			float goldWidth = Game.font.getWidth(goldText);
			float goldPositionX = squaresX + squaresWidth - goldWidth;
			TextUtils.printTextWithImages(goldPositionX, Game.windowHeight - bottomBorderHeight, Integer.MAX_VALUE,
					false, new Object[] { goldText });
		}

		// Gold for other actor
		if (otherInventory != null && otherInventory.parent instanceof Actor) {
			Actor actor = (Actor) otherInventory.parent;
			String goldText = "Gold: " + actor.getCarriedGoldValue();
			float goldWidth = Game.font.getWidth(goldText);
			float goldPositionX = otherInventory.squaresX + squaresWidth - goldWidth;
			TextUtils.printTextWithImages(goldPositionX, Game.windowHeight - bottomBorderHeight, Integer.MAX_VALUE,
					false, new Object[] { goldText });

		}

	}

	public void drawOtherInventoryText() {
		TextUtils.printTextWithImages(this.squaresX, inventoryNamesY, 300f, true,
				new Object[] { ((GameObject) this.parent).name });
	}

	public void drawBorder() {
		QuadUtils.drawQuad(inventoryAreaColor, squaresX, squaresX + squaresWidth, this.squaresY,
				this.squaresY + squaresHeight);
	}

	public void drawSquares() {
		for (InventorySquare inventorySquare : inventorySquares) {
			inventorySquare.drawStaticUI();
		}
	}

	public boolean isOpen() {
		return isOpen;
	}

	public InventorySquare getInventorySquareMouseIsOver(float mouseXInPixels, float mouseYInPixels) {

		if (mouseYInPixels <= (bottomBorderHeight))
			return null;

		// Inventory sqr
		float offsetX = squaresX;
		float offsetY = squaresY;
		float scroll = 0;

		int mouseXInSquares = (int) (((mouseXInPixels - offsetX) / Game.INVENTORY_SQUARE_WIDTH));
		int mouseYInSquares = (int) ((Game.windowHeight - mouseYInPixels - offsetY - scroll)
				/ Game.INVENTORY_SQUARE_HEIGHT);

		for (InventorySquare inventorySquare : inventorySquares) {
			if (inventorySquare.xInGrid == mouseXInSquares && inventorySquare.yInGrid == mouseYInSquares)
				return inventorySquare;
		}

		// Ground display sqr
		if (groundDisplay != null) {
			GroundDisplaySquare groundDisplaySquareMouseIsOver = groundDisplay
					.getGroundDisplaySquareMouseIsOver(mouseXInPixels, mouseYInPixels);
			if (groundDisplaySquareMouseIsOver != null) {
				return groundDisplaySquareMouseIsOver;
			}
		}

		if (otherInventory != null) {
			InventorySquare inventorySquareMouseIsOver = otherInventory.getInventorySquareMouseIsOver(mouseXInPixels,
					mouseYInPixels);
			if (inventorySquareMouseIsOver != null) {
				return inventorySquareMouseIsOver;
			}
		}

		return null;
	}

	public void setSquareMouseHoveringOver(InventorySquare squareMouseIsOver) {
		this.inventorySquareMouseIsOver = squareMouseIsOver;

	}

	public void setMode(INVENTORY_MODE mode) {
		inventoryMode = mode;
	}

	@Override
	public void scroll(float dragX, float dragY) {
		drag(dragX, dragY);
	}

	@Override
	public void drag(float dragX, float dragY) {
		this.squaresY -= dragY;
		fixScroll();
		resize2();
	}

	private void fixScroll() {
		// TODO Auto-generated method stub

		int totalSquaresHeight = (int) ((filteredGameObjects.size() / squareGridWidthInSquares)
				* Game.INVENTORY_SQUARE_HEIGHT);
		if (totalSquaresHeight < Game.windowHeight - bottomBorderHeight - topBorderHeight) {
			this.squaresY = this.squaresBaseY;
		} else if (this.squaresY < -(totalSquaresHeight - (Game.windowHeight - bottomBorderHeight))) {
			this.squaresY = -(totalSquaresHeight - (Game.windowHeight - bottomBorderHeight));
		} else if (this.squaresY > this.squaresBaseY) {
			this.squaresY = this.squaresBaseY;
		}

	}

	public Draggable getDraggable(int mouseX, int mouseY) {

		if (mouseX > squaresX && mouseX < squaresX + squaresWidth && mouseY > squaresBaseY
				&& mouseY < squaresBaseY + squaresHeight) {
			return this;
		}

		if (this.otherInventory != null) {
			if (mouseX > otherInventory.squaresX && mouseX < otherInventory.squaresX + squaresWidth
					&& mouseY > squaresBaseY && mouseY < squaresBaseY + squaresHeight) {
				return otherInventory;
			}
		}

		if (this.groundDisplay != null) {
			if (mouseX > groundDisplay.squaresX && mouseX < groundDisplay.squaresX + squaresWidth
					&& mouseY > squaresBaseY && mouseY < squaresBaseY + squaresHeight) {
				return groundDisplay;
			}
		}

		return null;

		// if (this.inventorySquareMouseIsOver == null)
		// return null;
		//
		// if (this.inventorySquares.contains(this.inventorySquareMouseIsOver))
		// {
		// return this;
		// }
		//
		// if (this.otherInventory != null) {
		// if
		// (this.otherInventory.inventorySquares.contains(this.inventorySquareMouseIsOver))
		// {
		// return otherInventory;
		// }
		// }
		//
		// if (this.groundDisplay != null) {
		// if
		// (this.groundDisplay.groundDisplaySquares.contains(this.inventorySquareMouseIsOver))
		// {
		// return groundDisplay;
		// }
		// }
		//
		// return null;
	}

	public void markItemsToSell() {
		ArrayList<String> weaponAlreadyAdded = new ArrayList<String>();
		for (GameObject gameObject : gameObjects) {

			// Junk
			if (gameObject instanceof Junk) {
				gameObject.toSell = true;
				continue;
			}

			// Duplicate weapons
			if (gameObject instanceof Weapon) {
				if (weaponAlreadyAdded.contains(gameObject.name)) {
					gameObject.toSell = true;
				} else {
					weaponAlreadyAdded.add(gameObject.name);
				}
			}
		}
	}

	public boolean hasItemsToSell() {
		for (GameObject gameObject : gameObjects)
			if (gameObject.toSell == true)
				return true;

		return false;
	}
}
