package com.marklynch.level;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.marklynch.Game;
import com.marklynch.actions.Action;
import com.marklynch.actions.ActionTeleport;
import com.marklynch.actions.ActionUsePower;
import com.marklynch.ai.utils.AIPath;
import com.marklynch.data.Load;
import com.marklynch.data.Save;
import com.marklynch.level.Level.LevelMode;
import com.marklynch.level.constructs.animation.primary.AnimationWalk;
import com.marklynch.level.constructs.inventory.Inventory;
import com.marklynch.level.constructs.inventory.InventorySquare;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Player;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.MapLevelGameObject;
import com.marklynch.ui.Draggable;
import com.marklynch.ui.PinWindow;
import com.marklynch.ui.Scrollable;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.popups.FullScreenTextBox;
import com.marklynch.ui.popups.Notification;
import com.marklynch.ui.popups.PopupMenu;
import com.marklynch.ui.popups.PopupMenuSelectAction;
import com.marklynch.ui.popups.PopupMenuSelectObject;
import com.marklynch.ui.quickbar.QuickBarSquare;

public class UserInputLevel {

	public static float mouseDownX = -1;
	public static float mouseDownY = -1;
	public static float mouseLastX = -1;
	public static float mouseLastY = -1;
	public static boolean dragging = false;
	public static boolean draggingMap = false;
	public static boolean mouseButtonStateLeft = false;
	public static boolean mouseButtonStateRight = false;

	public static boolean capsLock = false;
	public static boolean keyStateUp = false;
	public static boolean keyStateDown = false;
	public static boolean keyStateLeft = false;
	public static boolean keyStateRight = false;
	public static boolean keyStateTab = false;
	public static boolean keyStateReturn = false;
	public static boolean keyStateBack = false;
	public static boolean keyStateDelete = false;
	public static boolean keyStateLeftShift = false;
	public static boolean keyStateRightShift = false;
	public static boolean keyStateEscape = false;
	public static boolean keyStateSpace = false;
	public static boolean keyStateA = false;
	public static boolean keyStateB = false;
	public static boolean keyStateC = false;
	public static boolean keyStateD = false;
	public static boolean keyStateE = false;
	public static boolean keyStateF = false;
	public static boolean keyStateG = false;
	public static boolean keyStateH = false;
	public static boolean keyStateI = false;
	public static boolean keyStateJ = false;
	public static boolean keyStateK = false;
	public static boolean keyStateL = false;
	public static boolean keyStateM = false;
	public static boolean keyStateN = false;
	public static boolean keyStateO = false;
	public static boolean keyStateP = false;
	public static boolean keyStateQ = false;
	public static boolean keyStateR = false;
	public static boolean keyStateS = false;
	public static boolean keyStateT = false;
	public static boolean keyStateU = false;
	public static boolean keyStateV = false;
	public static boolean keyStateW = false;
	public static boolean keyStateX = false;
	public static boolean keyStateY = false;
	public static boolean keyStateZ = false;
	public static boolean keyState1 = false;
	public static boolean keyState2 = false;
	public static boolean keyState3 = false;
	public static boolean keyState4 = false;
	public static boolean keyState5 = false;
	public static boolean keyState6 = false;
	public static boolean keyState7 = false;
	public static boolean keyState8 = false;
	public static boolean keyState9 = false;
	public static boolean keyState0 = false;
	public static boolean keyStateMinus = false;
	public static boolean keyStateUnderscore = false;
	public static boolean keyStateEquals = false;
	public static boolean keyStatePlus = false;
	public static boolean keyStatePeriod = false;
	public static boolean keyStateQuestionMark = false;
	public static boolean keyStateForwardSlash = false;
	public static boolean keyStateF5 = false;
	public static boolean keyStateF9 = false;

	public static AIPath path;
	static boolean controllingMenu = false;

	public static Draggable draggableMouseIsOver = null;
	public static Scrollable scrollableMouseIsOver = null;

	public static float mouseXTransformed = 0;
	public static float mouseYTransformed = 0;

	public static CopyOnWriteArrayList<Square> nodeHighlights = new CopyOnWriteArrayList<Square>();

	public static void userInput(int delta2) {
		if (Game.ticksSinceDisplayInactive < 10)
			return;

		// Getting what square pixel the mouse is on
		float mouseXinPixels = Mouse.getX();
		float mouseYinPixels = Mouse.getY();

		// Setting the draggable
		setScrollableMouseIsOver();

		// Transformed mouse coords
		mouseXTransformed = (((Game.windowWidth / 2) - Game.getDragXWithOffset() - (Game.windowWidth / 2) / Game.zoom)
				+ (mouseXinPixels) / Game.zoom);
		mouseYTransformed = ((Game.windowHeight / 2 - Game.getDragYWithOffset() - (Game.windowHeight / 2) / Game.zoom)
				+ (((Game.windowHeight - mouseYinPixels)) / Game.zoom));

		// Getting what square coordinates the mouse is on (as in squares on the
		// grid)
		float mouseXInSquares = (int) (mouseXTransformed / Game.SQUARE_WIDTH);
		float mouseYInSquares = (int) (mouseYTransformed / Game.SQUARE_HEIGHT);

		// MOUSE WHEEL
		int mouseWheelDelta = Mouse.getDWheel();

		if (UserInputLevel.scrollableMouseIsOver != null) {
			if (mouseWheelDelta > 0)
				scrollableMouseIsOver.scroll(0, -100);
			else if (mouseWheelDelta < 0)
				scrollableMouseIsOver.scroll(0, 100);

			// if (mouseWheelDelta != 0) {
			// Game.level.activityLogger.drag(-mouseWheelDelta);
			// }
		} else {
			// Calculate zoom
			if (mouseWheelDelta > 0)
				Game.zoomLevelIndex--;
			else if (mouseWheelDelta < 0)
				Game.zoomLevelIndex++;

			// halfWindowWidth = Display.getWidth() / 2;
			// halfWindowHeight

			// Game.zoom += 0.001 * mouseWheelDelta;
			if (Game.zoomLevelIndex < 0)
				Game.zoomLevelIndex = 0;
			else if (Game.zoomLevelIndex >= Game.zoomLevels.length)
				Game.zoomLevelIndex = Game.zoomLevels.length - 1;

			// Change in zoom
			if (Game.lastZoomLevelIndex != Game.zoomLevelIndex) {
				float oldZoom = Game.zoom;
				Game.zoom = Game.zoomLevels[Game.zoomLevelIndex];
				Game.dragX += ((Mouse.getX() - Game.halfWindowWidth) / Game.zoom)
						- ((Mouse.getX() - Game.halfWindowWidth) / oldZoom);
				Game.dragY -= ((Mouse.getY() - Game.halfWindowHeight) / Game.zoom)
						- ((Mouse.getY() - Game.halfWindowHeight) / oldZoom);
				Game.lastZoomLevelIndex = Game.zoomLevelIndex;
			}

		}

		// DRAG
		if (Mouse.isButtonDown(0)) {

			mouseButtonStateLeft = true;

			if (mouseDownX == -1) {
				mouseDownX = Mouse.getX();
				mouseDownY = Mouse.getY();
			}

			if (draggingMap == false && draggableMouseIsOver == null) {
				if (Mouse.getX() - mouseDownX > 5 || Mouse.getX() - mouseDownX < -5 || Mouse.getY() - mouseDownY > 5
						|| Mouse.getY() - mouseDownY < -5) {
					setDraggableMouseIsOver();
					if (draggableMouseIsOver == null) {
						draggingMap = true;
					}
					dragging = true;
				}

			}

			if (draggableMouseIsOver != null) {
				if (draggableMouseIsOver instanceof PinWindow) {
					((PinWindow) draggableMouseIsOver).bringToFront();
				}
				draggableMouseIsOver.drag(Mouse.getX() - mouseLastX, Mouse.getY() - mouseLastY);
			} else if (draggingMap == true) {
				if (Game.level.cameraFollow) {
					Game.dragX = Game.getDragXWithOffset();
					Game.dragY = Game.getDragYWithOffset();
				}
				Game.level.cameraFollow = false;
				Game.level.centerToSquare = false;

				Game.dragX += (Mouse.getX() - mouseLastX) / Game.zoom;
				Game.dragY -= (Mouse.getY() - mouseLastY) / Game.zoom;
			}
		}

		// Check if a script is hogging the screen and intercepting clicks
		boolean scriptInterceptsClick = false;

		// Get the square that we're hovering over
		Game.squareMouseIsOver = null;
		Game.gameObjectMouseIsOver = null;
		if (Level.journal.showing) {
		} else if (Level.characterScreen.showing) {
		} else if (Level.skillTree.showing) {
		} else if (Level.availablePowerScreen.showing) {
		} else if (Level.mainMenu.showing) {
		} else if (Level.characterCreation.showing) {
		} else if (Level.gameOver.showing) {
		} else if (Game.level.openInventories.size() > 0) {
			InventorySquare inventorySquareMouseIsOver = Game.level.openInventories.get(0)
					.getInventorySquareMouseIsOver(mouseXinPixels, mouseYinPixels);
			Game.squareMouseIsOver = inventorySquareMouseIsOver;
			Game.level.openInventories.get(0).setSquareMouseHoveringOver(inventorySquareMouseIsOver);
		} else if (Game.level.fullScreenTextBox != null) {

		} else if (Game.level.conversation != null) {

		} else if (draggableMouseIsOver != null) {

		} else {

			if (Game.zoom == Game.zoomLevels[Game.MAP_MODE_ZOOM_LEVEL_INDEX]) {
//				System.out.println("MapLevelGameObject.instances = " + MapLevelGameObject.instances);
				for (GameObject gameObject : MapLevelGameObject.instances) {
					if (gameObject.squareGameObjectIsOn != null && gameObject.squareGameObjectIsOn.seenByPlayer) {
						if (((MapLevelGameObject) gameObject).checkIfPointOnGameObject()) {
							Game.gameObjectMouseIsOver = gameObject;
						}
					}
				}
			} else if ((int) mouseXInSquares > -1 && (int) mouseXInSquares < Game.level.squares.length
					&& (int) mouseYInSquares > -1 && (int) mouseYInSquares < Game.level.squares[0].length) {
				Game.squareMouseIsOver = Game.level.squares[(int) mouseXInSquares][(int) mouseYInSquares];
				if (Game.squareMouseIsOver != null) {
					Game.gameObjectMouseIsOver = Game.squareMouseIsOver.getGameObjectMouseIsOver();
				}
			}
		}

		// highlight sqrs for node ur hovering over

		for (Square square : nodeHighlights) {
			square.highlight = false;
		}
		nodeHighlights.clear();
		if (Game.squareMouseIsOver != null && Game.squareMouseIsOver.node != null) {
			for (Square square : Game.squareMouseIsOver.node.getSquares()) {
				square.highlight = true;
				nodeHighlights.add(square);
			}
		}

		// highlight path
		if (Game.highlightPath) {
			// remove path highlight
			if (Player.playerPathToMouse != null) {
				for (Square square : Player.playerPathToMouse.squares) {
					square.drawPathDot = false;
					square.drawEndPathDot = false;
				}
			}

			Player.playerPathToMouse = null;

			// Create path to draw
			if (Game.gameObjectMouseIsOver != null && Game.gameObjectMouseIsOver.squareGameObjectIsOn != null
					&& !(Game.gameObjectMouseIsOver.squareGameObjectIsOn instanceof InventorySquare)) {
				Player.playerPathToMouse = Level.player.getPathTo(Game.gameObjectMouseIsOver.squareGameObjectIsOn);
			} else if (Game.squareMouseIsOver != null && !(Game.squareMouseIsOver instanceof InventorySquare)) {
				Player.playerPathToMouse = Level.player.getPathTo(Game.squareMouseIsOver);
			}

			// Add path highlight
			if (Player.playerPathToMouse != null) {
				if (Player.playerPathToMouse.squares.size() > 0) {
					if (Player.playerPathToMouse != null) {
						for (Square square : Level.player.playerPathToMouse.squares) {
							square.drawPathDot = true;
						}
					}
					Player.playerPathToMouse.squares
							.get(Player.playerPathToMouse.squares.size() - 1).drawEndPathDot = true;
				}
			}
		}

		// Clear path highlights
		for (int i = 0; i < Game.level.width; i++) {
			for (int j = 0; j < Game.level.height; j++) {
				Game.level.squares[i][j].inPath = false;
			}
		}

		// Getting button that the mouse is over, if any
		Game.oldButtonHoveringOver = Game.buttonMouseIsOver;
		Game.buttonMouseIsOver = null;
		Game.pinWindowHoveringOver = null;
		Game.textBoxHoveringOver = null;
		if (draggingMap == false) {
			Game.buttonMouseIsOver = Game.level.getButtonFromMousePosition(Mouse.getX(), Mouse.getY(),
					mouseXTransformed, mouseYTransformed);
			Game.pinWindowHoveringOver = Game.level.getWindowFromMousePosition(Mouse.getX(), Mouse.getY(),
					mouseXTransformed, mouseYTransformed);
		}

		if (draggingMap == false) {
			if (Game.level.openInventories.size() > 0 && Game.level.openInventories.get(0)
					.isMouseOverTextBox(Mouse.getX(), (int) (Game.windowHeight - Mouse.getY()))) {
				Game.textBoxHoveringOver = Game.level.openInventories.get(0).textBoxSearch;
			} else if (Game.level.fullScreenTextBox != null && Game.level.fullScreenTextBox.isMouseOver(Mouse.getX(),
					(int) (Game.windowHeight - Mouse.getY()))) {
				Game.textBoxHoveringOver = Game.level.fullScreenTextBox.textBox;
			}
		}

		if (mouseLastX != Mouse.getX() || mouseLastY != Mouse.getY()) {
			if (Game.oldButtonHoveringOver != null) {
				Game.oldButtonHoveringOver.removeHighlight();

			}

			for (PopupMenu popupMenu : Game.level.popupMenuObjects) {
				popupMenu.clearHighlights();
			}

			for (PopupMenu popupMenu : Game.level.popupMenuActions) {
				popupMenu.clearHighlights();
			}

			if (Game.buttonMouseIsOver != null) {
				for (PopupMenu popUp : Game.level.popupMenuActions) {
					if (popUp.buttons.contains(Game.buttonMouseIsOver)) {
						popUp.highlightedButton = Game.buttonMouseIsOver;
						popUp.highlightedButtonIndex = popUp.buttons.indexOf(Game.buttonMouseIsOver);
					}
				}
				for (PopupMenu popUp : Game.level.popupMenuObjects) {
					if (popUp.buttons.contains(Game.buttonMouseIsOver)) {
						popUp.highlightedButton = Game.buttonMouseIsOver;
						popUp.highlightedButtonIndex = popUp.buttons.indexOf(Game.buttonMouseIsOver);
					}
				}
				Game.buttonMouseIsOver.highlight();
			}
		}

		// Path highlights
		// if (scriptInterceptsClick == false && Game.buttonHoveringOver == null
		// && Game.level.player != null
		// && Game.squareMouseIsOver != null &&
		// Game.squareMouseIsOver.reachableBySelectedCharater
		// && Game.level.player.faction == Game.level.factions.player
		// && Game.level.currentFactionMoving == Game.level.factions.player) {
		//// path = Game.level.player.paths.get(Game.squareMouseIsOver);
		// for (Square square : path.squares) {
		// square.inPath = true;
		// }
		// }

		// Lifted the mouse to perform click
		if (mouseButtonStateLeft == true && !Mouse.isButtonDown(0) && draggingMap == false && !dragging) {

			// Left Click
			// Game.level.popupMenuObjects.clear();
			// Game.level.popupMenuActions.clear();
			if (Game.pinWindowHoveringOver != null)
				Game.pinWindowHoveringOver.bringToFront();

			if (scriptInterceptsClick) {
				// Continue script
			} else if (Game.buttonMouseIsOver != null) {
				// Click button
				Game.buttonMouseIsOver.click();
			} else if (Level.journal.showing) {

			} else if (Level.characterScreen.showing) {

			} else if (Level.skillTree.showing) {

			} else if (Level.availablePowerScreen.showing) {

			} else if (Game.level.conversation != null && Game.level.openInventories.size() == 0) {

			} else if (Game.textBoxHoveringOver != null) {
				Game.textBoxHoveringOver.click(Mouse.getX(), (int) (Game.windowHeight - Mouse.getY()));
			} else if (Game.level.fullScreenTextBox != null) {

			} else if (Game.pinWindowHoveringOver != null) {

				// } else if (Game.squareMouseIsOver != null &&
				// Player.playerTargetSquare != null) {
				// Game.level.pausePlayer();
			} else if (Game.squareMouseIsOver != null) {

				if (Game.level.popupMenuObjects.size() > 0 || Game.level.popupMenuActions.size() > 0) {
					Level.closeAllPopups();
				} else {
					interactWith(Game.squareMouseIsOver, -1,
							Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
							Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
							Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
				}
			}
			dragging = false;
		}

		if (mouseButtonStateRight == true && !Mouse.isButtonDown(1) && draggingMap == false)

		{

			// Right Click
			if (Game.pinWindowHoveringOver != null)
				Game.pinWindowHoveringOver.bringToFront();

			if (Level.journal.showing) {
			} else if (Level.characterScreen.showing) {
			} else if (Level.skillTree.showing) {
			} else if (Level.availablePowerScreen.showing) {
			} else if (Game.level.conversation != null && Game.level.openInventories.size() == 0) {

			} else if (Game.level.fullScreenTextBox != null) {

			} else if (Game.pinWindowHoveringOver != null) {

			} else if (Game.squareMouseIsOver != null && Player.playerTargetAction != null) {
				Game.level.pausePlayer();
			} else if (Game.level.popupMenuObjects.isEmpty() && Game.squareMouseIsOver != null) {
				interactWith(Game.squareMouseIsOver, -1, true, false, false);
				// Game.level.popups.add(new PopupSelectObject(100, Game.level,
				// Game.squareMouseIsOver));
			} else {
				Level.closeAllPopups();
			}
		}

		if (!Mouse.isButtonDown(0)) {
			mouseButtonStateLeft = false;
			mouseDownX = -1;
			mouseDownY = -1;
		}

		if (Mouse.isButtonDown(1)) {
			mouseButtonStateRight = true;
		} else if (!Mouse.isButtonDown(1)) {
			mouseButtonStateRight = false;
		}

		if (!Mouse.isButtonDown(0)) {
			dragging = false;
			draggingMap = false;
			if (draggableMouseIsOver != null) {
				draggableMouseIsOver.dragDropped();
				UserInputLevel.draggableMouseIsOver = null;
				draggingMap = false;
			}
		}

		checkButtons();

		mouseLastX = Mouse.getX();
		mouseLastY = Mouse.getY();
		// if (interactedThisTurn)
		// Game.level.endTurn();

		// interactedThisTurn = false;
	}

	private static void setScrollableMouseIsOver() {

		scrollableMouseIsOver = null;

		if (Level.fullScreenTextBox != null)
			return;

		if (Level.dialog != null)
			return;

		if (Level.mainMenu.showing) {
			scrollableMouseIsOver = Level.mainMenu;
			return;
		}

		if (Level.characterCreation.showing) {
			scrollableMouseIsOver = Level.characterCreation;
			return;
		}

		if (Level.gameOver.showing) {
			scrollableMouseIsOver = Level.gameOver;
			return;
		}

		if (Level.journal.showing) {
			scrollableMouseIsOver = Level.journal;
			return;
		}

		if (Level.characterScreen.showing) {
			scrollableMouseIsOver = Level.characterScreen;
			return;
		}

		if (Level.skillTree.showing) {
			scrollableMouseIsOver = Level.skillTree;
			return;
		}

		if (Level.availablePowerScreen.showing) {
			scrollableMouseIsOver = Level.availablePowerScreen;
			return;
		}

		boolean inventoriesOpen = Game.level.openInventories.size() > 0;
		if (inventoriesOpen) {
			scrollableMouseIsOver = (Scrollable) Game.level.openInventories.get(0).getDraggable(Mouse.getX(),
					(int) Game.windowHeight - Mouse.getY());
			return;
		}

		// Pop up menu for actions
		if (!Game.level.popupMenuActions.isEmpty()) {
			for (int i = Game.level.popupMenuActions.size() - 1; i >= 0; i--) {
				for (Button button : Game.level.popupMenuActions.get(i).buttons) {
					if (button.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {

						scrollableMouseIsOver = Game.level.popupMenuActions.get(i);
						return;
					}
				}

			}
		}

		// Pop up menu for objects
		if (!Game.level.popupMenuObjects.isEmpty()) {
			for (int i = Game.level.popupMenuObjects.size() - 1; i >= 0; i--) {
				for (Button button : Game.level.popupMenuObjects.get(i).buttons) {
					if (button.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
						scrollableMouseIsOver = Game.level.popupMenuObjects.get(i);
						return;
					}
				}

			}
		}

		for (QuickBarSquare quickBarSquare : Game.level.quickBar.quickBarSquares) {
			if (quickBarSquare.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
				scrollableMouseIsOver = quickBarSquare;
				return;
			}
		}

		if (Game.level.activityLogger.isMouseOver(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
			scrollableMouseIsOver = Game.level.activityLogger;
			return;
		}

	}

	private static void setDraggableMouseIsOver() {

		if (draggingMap)
			return;

		if (draggableMouseIsOver != null)
			return;

		if (Level.fullScreenTextBox != null)
			return;

		if (Level.dialog != null)
			return;

		if (Level.mainMenu.showing) {
			draggableMouseIsOver = Level.mainMenu;
			return;
		}

		if (Level.characterCreation.showing) {
			draggableMouseIsOver = Level.characterCreation;
			return;
		}

		if (Level.gameOver.showing) {
			draggableMouseIsOver = Level.gameOver;
			return;
		}

		if (Level.journal.showing) {
			draggableMouseIsOver = Level.journal;
			return;
		}

		if (Level.characterScreen.showing) {
			draggableMouseIsOver = Level.characterScreen;
			return;
		}

		for (QuickBarSquare quickBarSquare : Game.level.quickBar.quickBarSquares) {
			if (quickBarSquare.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
				draggableMouseIsOver = quickBarSquare;
				return;
			}
		}

		boolean inventoriesOpen = Game.level.openInventories.size() > 0;
		if (inventoriesOpen) {
			draggableMouseIsOver = Game.level.openInventories.get(0).getDraggable(Mouse.getX(),
					(int) Game.windowHeight - Mouse.getY());
			return;
		}

		if (Level.availablePowerScreen.showing) {
			draggableMouseIsOver = Level.availablePowerScreen.getDraggable(Mouse.getX(),
					(int) Game.windowHeight - Mouse.getY());
			return;
		}

		if (Level.skillTree.showing) {
			draggableMouseIsOver = Level.skillTree.getDraggable();
			return;
		}

		for (int i = Game.level.pinWindows.size() - 1; i >= 0; i--) {
			if (Game.level.pinWindows.get(i).isMouseOver(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
				draggableMouseIsOver = Game.level.pinWindows.get(i);
				return;
			}
		}

		// Pop up menu for actions
		if (!Game.level.popupMenuActions.isEmpty()) {
			for (int i = Game.level.popupMenuActions.size() - 1; i >= 0; i--) {
				for (Button button : Game.level.popupMenuActions.get(i).buttons) {
					if (button.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {

						draggableMouseIsOver = Game.level.popupMenuActions.get(i);
						return;
					}
				}

			}
		}

		// Pop up menu for objects
		if (!Game.level.popupMenuObjects.isEmpty()) {
			for (int i = Game.level.popupMenuObjects.size() - 1; i >= 0; i--) {
				for (Button button : Game.level.popupMenuObjects.get(i).buttons) {
					if (button.calculateIfPointInBoundsOfButton(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
						draggableMouseIsOver = Game.level.popupMenuObjects.get(i);
						return;
					}
				}

			}
		}

		if (Game.level.activityLogger.isMouseOver(Mouse.getX(), (int) Game.windowHeight - Mouse.getY())) {
			draggableMouseIsOver = Game.level.activityLogger;
			return;
		}

	}

	// static boolean interactedThisTurn = false;

	public static void interactWith(Square square, int key, boolean openMenu, boolean secondary, boolean attack) {

		if (openMenu) {
			Game.level.levelMode = LevelMode.LEVEL_MODE_NORMAL;
		}

		if (Game.level.levelMode == LevelMode.LEVEL_MODE_FISHING) {

			return;
		}

		if (Game.level.levelMode == LevelMode.LEVEL_SELECT_TELEPORT_SQUARE) {
			new ActionTeleport(Game.level.player, Level.teleportee, square, true, true, false).perform();
			Game.level.levelMode = LevelMode.LEVEL_MODE_NORMAL;
			return;
		} else if (Game.level.levelMode == LevelMode.LEVEL_MODE_CAST) {
			new ActionUsePower(Game.level.player, Game.gameObjectMouseIsOver, Game.squareMouseIsOver,
					Game.level.selectedPower, true).perform();
			// Game.level.selectedPower.cast(Game.level.player, square);
			// Game.level.levelMode = LevelMode.LEVEL_MODE_NORMAL;
			return;
		}

		Action action = null;

		if (!openMenu) {
			if (attack) {
				action = square.getAttackActionForTheSquareOrObject(Game.level.player, key != -1);
			} else if (secondary) {
				action = square.getSecondaryActionForTheSquareOrObject(Game.level.player, key != -1);
			} else {
				action = square.getDefaultActionForTheSquareOrObject(Game.level.player, key != -1);
			}
		}

		if (action != null && !action.enabled) {
			if (action.disabledReason != null) {
				Game.level.addNotification(new Notification(new Object[] { action.disabledReason },
						Notification.NotificationType.ACTION_DISABLED, null));

				if (Game.level.openInventories.size() > 0)
					Game.level.openCloseInventory();

			}
			return;
		}

		if (action != null && (square instanceof InventorySquare)) {
			action.perform();
		} else if (action != null && !(square instanceof InventorySquare)) {
			if (Game.level.settingFollowPlayer && Game.level.player.onScreen()) {
				Game.level.cameraFollow = true;
			}
			Level.pausePlayer();
			Player.playerTargetAction = action;
			if (!(Level.player.getPrimaryAnimation() instanceof AnimationWalk))
				Player.playerFirstMove = true;
			return;
		} else {

			// openMenu here

			Level.closeAllPopups();
			if (square instanceof InventorySquare && ((InventorySquare) square).stack.get(0) != null) {
				PopupMenuSelectAction popupSelectAction = new PopupMenuSelectAction(0, 200, Game.level, square,
						((InventorySquare) square).getAllActionsForTheSquareOrObject(Game.level.player));
				if (popupSelectAction.buttons.size() > 0)
					Game.level.popupMenuActions.add(popupSelectAction);

				// Game.level.popups.add(e);
			} else if (Game.gameObjectMouseIsOver != null && Game.gameObjectMouseIsOver.squareGameObjectIsOn != null) {
				PopupMenuSelectObject popupSelectObject = new PopupMenuSelectObject(100, Game.level,
						Game.gameObjectMouseIsOver.squareGameObjectIsOn, true, true, false);
				if (popupSelectObject.buttons.size() > 0)
					Game.level.popupMenuObjects.add(popupSelectObject);

			} else if (!(square instanceof InventorySquare)) {
				PopupMenuSelectObject popupSelectObject = new PopupMenuSelectObject(100, Game.level, square, true, true,
						false);
				if (popupSelectObject.buttons.size() > 0)
					Game.level.popupMenuObjects.add(popupSelectObject);
			}

		}

	}

	public static void waitPressed(boolean allowMenuControl, boolean held) {

		// if (Player.playerTargetSquare != null) {
		// Game.level.pausePlayer();
		// return;
		// }

		if (Game.level.fullScreenTextBox != null) {
			return;
		} else if (Level.journal.showing) {
			return;
		} else if (Level.characterScreen.showing) {
			return;
		} else if (Level.skillTree.showing) {
			return;
		} else if (Level.availablePowerScreen.showing) {
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			if (Inventory.buttons.contains(Inventory.buttonLootAll)) {
				Inventory.buttonLootAll.click();
			} else if (Inventory.buttons.contains(Inventory.buttonQuickSell)) {
				Inventory.buttonQuickSell.click();
			}
			controllingMenu = true;
			return;
		} else if (Game.level.conversation != null) {
			return;
		}

		if (!held)
			controllingMenu = false;

		if (Game.level.popupMenuActions.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuActions.get(0).clickHighlightedButton();
			}
		} else if (Game.level.popupMenuObjects.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuObjects.get(0).clickHighlightedButton();
			}
		} else if (!controllingMenu) {

			// if (Level.player.getPrimaryAnimation() != null &&
			// !Level.player.getPrimaryAnimation().getCompleted())
			// return;

			interactWith(Level.player.squareGameObjectIsOn, Keyboard.KEY_SPACE,
					Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
					Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
					Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
		}
	}

	public static void upPressed(boolean allowMenuControl, boolean held) {

		// if (Player.playerTargetSquare != null) {
		// Game.level.pausePlayer();
		// return;
		// }

		if (Game.level.fullScreenTextBox != null) {
			return;
		} else if (Level.journal.showing) {
			return;
		} else if (Level.characterScreen.showing) {
			return;
		} else if (Level.skillTree.showing) {
			return;
		} else if (Level.availablePowerScreen.showing) {
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			return;
		} else if (Game.level.conversation != null) {
			return;
		}

		if (!held)
			controllingMenu = false;

		if (Game.level.popupMenuActions.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuActions.get(0).moveHighLightUp();
			}
		} else if (Game.level.popupMenuObjects.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuObjects.get(0).moveHighLightUp();
			}
		} else if (!controllingMenu) {
			int y = Game.level.player.squareGameObjectIsOn.yInGrid - 1;
			if (y >= 0) {
				interactWith(Game.level.squares[Game.level.player.squareGameObjectIsOn.xInGrid][y], Keyboard.KEY_UP,
						Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
						Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
						Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));

			}
		}
	}

	public static void downPressed(boolean allowMenuControl, boolean held) {

		// if (Player.playerTargetSquare != null) {
		// Game.level.pausePlayer();
		// return;
		// }

		if (Game.level.fullScreenTextBox != null) {
			return;
		} else if (Level.journal.showing) {
			return;
		} else if (Level.skillTree.showing) {
			return;
		} else if (Level.availablePowerScreen.showing) {
			return;
		} else if (Level.characterScreen.showing) {
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			return;
		} else if (Game.level.conversation != null) {
			return;
		}

		if (!held)
			controllingMenu = false;
		if (Game.level.popupMenuActions.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuActions.get(0).moveHighLightDown();
			}
		} else if (Game.level.popupMenuObjects.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuObjects.get(0).moveHighLightDown();
			}
		} else if (!controllingMenu) {
			int y = Game.level.player.squareGameObjectIsOn.yInGrid + 1;
			if (y < Game.level.squares[0].length) {
				interactWith(Game.level.squares[Game.level.player.squareGameObjectIsOn.xInGrid][y], Keyboard.KEY_DOWN,
						Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
						Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
						Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
			}
		}

	}

	public static void leftPressed(boolean allowMenuControl, boolean held) {
		// if (Player.playerTargetSquare != null) {
		// Game.level.pausePlayer();
		// return;
		// }

		if (Level.activeTextBox != null) {
			Level.activeTextBox.moveCaretLeft();
			return;
		} else if (Level.journal.showing) {
			return;
		} else if (Level.characterScreen.showing) {
			return;
		} else if (Level.skillTree.showing) {
			return;
		} else if (Level.availablePowerScreen.showing) {
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			return;
		} else if (Game.level.conversation != null) {
			return;
		}

		if (!held)
			controllingMenu = false;

		if (Game.level.popupMenuActions.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuActions.clear();
				for (Button button : Game.level.popupMenuObjects.get(0).buttons) {
					button.down = false;
				}
			}
		} else if (Game.level.popupMenuObjects.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuObjects.clear();
			}
		}

		else if (!controllingMenu) {
			int x = Game.level.player.squareGameObjectIsOn.xInGrid - 1;
			if (x >= 0) {
				interactWith(Game.level.squares[x][Game.level.player.squareGameObjectIsOn.yInGrid], Keyboard.KEY_LEFT,
						Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
						Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
						Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
			}
		}

	}

	public static void rightPressed(boolean allowMenuControl, boolean held) {

		if (Level.activeTextBox != null) {
			Level.activeTextBox.moveCaretRight();
			return;
		} else if (Level.journal.showing) {
			return;
		} else if (Level.characterScreen.showing) {
			return;
		} else if (Level.skillTree.showing) {
			return;
		} else if (Level.availablePowerScreen.showing) {
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			return;
		} else if (Game.level.conversation != null) {
			return;
		}

		if (!held)
			controllingMenu = false;

		if (Game.level.popupMenuActions.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuActions.get(0).clickHighlightedButton();
			}
		} else if (Game.level.popupMenuObjects.size() != 0) {
			if (allowMenuControl) {
				controllingMenu = true;
				Game.level.popupMenuObjects.get(0).clickHighlightedButton();
			}
		} else if (!controllingMenu) {

			int x = Game.level.player.squareGameObjectIsOn.xInGrid + 1;
			if (x < Game.level.squares.length) {
				interactWith(Game.level.squares[x][Game.level.player.squareGameObjectIsOn.yInGrid], Keyboard.KEY_RIGHT,
						Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL),
						Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT),
						Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
			}
		}

	}

	public static void enterTyped() {
		if (Level.activeTextBox != null) {
			Level.activeTextBox.enterTyped();
			return;
		}

		if (Game.level.openInventories.size() > 0) {
			Game.level.player.inventory.buttonSearch.click();
			return;
		}

		if (Level.journal.showing) {
			return;
		}

		if (Level.characterScreen.showing) {
			return;
		}

		if (Level.skillTree.showing) {
			return;
		}

		if (Level.availablePowerScreen.showing) {
			return;
		}

		if (Game.level.popupMenuActions.size() != 0) {
			controllingMenu = true;
			Game.level.popupMenuActions.get(0).clickHighlightedButton();
		} else if (Game.level.popupMenuObjects.size() != 0) {
			controllingMenu = true;
			Game.level.popupMenuObjects.get(0).clickHighlightedButton();
		} else {
			FullScreenTextBox fullScreenTextBox = new FullScreenTextBox(null, FullScreenTextBox.SQUARE_SEARCH_X,
					FullScreenTextBox.TYPE.SQUARE_SEARCH_X);
			Level.openFullScreenTextBox(fullScreenTextBox);
			Level.activeTextBox.maxNumericValue = Game.level.squares.length - 1;
		}
	}

	public static void backSpaceTyped() {

		if (Game.level.activeTextBox != null) {
			Game.level.activeTextBox.backSpaceTyped();
			return;
		}

		if (Game.level.openInventories.size() > 0) {
			Game.level.player.inventory.backSpaceTyped();
			return;
		}

		Level.closeAllPopups();
	}

	public static void deleteTyped() {

		if (Game.level.activeTextBox != null) {
			Game.level.activeTextBox.deleteTyped();
			return;
		}
	}

	public static void escapeTyped() {

		Game.level.levelMode = Level.LevelMode.LEVEL_MODE_NORMAL;

		if (Level.mainMenu.showing) {
			Game.level.openCloseMainMenu();
			return;
		} else if (Level.characterCreation.showing) {
			Game.level.openCloseCharacterCreation();
			return;
		} else if (Game.level.popupMenuObjects.size() != 0 || Game.level.popupMenuActions.size() != 0) {
			Level.closeAllPopups();
		} else if (Level.activeTextBox != null) {
			Level.activeTextBox = null;
		} else if (Level.fullScreenTextBox != null) {
			Level.closeFullScreenTextBox();
		} else if (Level.journal.showing) {
			Game.level.openCloseJournal();
		} else if (Level.characterScreen.showing) {
			Game.level.openCloseCharacterScreen();
		} else if (Level.skillTree.showing) {
			Game.level.openCloseSkillTree();
		} else if (Level.availablePowerScreen.showing) {
			Game.level.openClosePowersScreen();
		} else if (Level.gameOver.showing) {
			return;
		} else if (Game.level.openInventories.size() != 0) {
			Game.level.player.inventory.escapeTyped();
			return;
		} else if (Game.level.conversation != null
				&& Game.level.conversation.currentConversationPart.windowSelectConversationResponse.buttonLeave != null) {
			Game.level.conversation.currentConversationPart.windowSelectConversationResponse.buttonLeave.click();
			return;
		} else if (Game.level.popupMenuObjects.size() != 0 || Game.level.popupMenuActions.size() != 0) {
			Level.closeAllPopups();
			return;
		} else {
			Game.level.openCloseMainMenu();
			return;
		}

		return;
	}

	public static void tabTyped() {
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB) == true) {
			Level.closeAllPopups();
			Game.level.popupMenuHighlightObjects.clear();
			for (int j = Level.gridY1Bounds; j < Level.gridY2Bounds; j++) {
				for (int i = Level.gridX1Bounds; i < Level.gridX2Bounds; i++) {
					if (Game.level.squares[i][j].visibleToPlayer) {
						PopupMenuSelectObject popupSelectObject = new PopupMenuSelectObject(100, Game.level,
								Game.level.squares[i][j], false, false, true);
						if (popupSelectObject.buttons.size() > 0)
							Game.level.popupMenuHighlightObjects.add(popupSelectObject);
					}
				}
			}
		}
	}

	public static long lastCopy = 0;

	public static void keyTyped(char character) {

		if (Level.activeTextBox != null) {
			Level.activeTextBox.keyTyped(character);
			return;
		}

		if (character == '1') {
			Level.quickBar.quickBarSquares[0].click();
		} else if (character == '2') {
			Level.quickBar.quickBarSquares[1].click();
		} else if (character == '3') {
			Level.quickBar.quickBarSquares[2].click();
		} else if (character == '4') {
			Level.quickBar.quickBarSquares[3].click();
		} else if (character == '5') {
			Level.quickBar.quickBarSquares[4].click();
		} else if (character == '6') {
			Level.quickBar.quickBarSquares[5].click();
		} else if (character == '7') {
			Level.quickBar.quickBarSquares[6].click();
		} else if (character == '8') {
			Level.quickBar.quickBarSquares[7].click();
		} else if (character == '9') {
			Level.quickBar.quickBarSquares[8].click();
		} else if (character == '0') {
			Level.quickBar.quickBarSquares[9].click();
		} else if (character == '.') {
			Game.level.pauseButton.click();
		} else if (character == '/') {
			Game.level.showWindowPixelCoords = !Game.level.showWindowPixelCoords;
		} else if (character == 'j' || character == 'J') {
			Game.level.openCloseJournal();
			return;
		} else if (character == 'c' || character == 'C') {
			Game.level.openCloseCharacterScreen();
			return;
		} else if (character == 't' || character == 'T') {
			Game.level.openCloseSkillTree();
			return;
		} else if (character == 'p' || character == 'P') {
			Game.level.openClosePowersScreen();
			return;
		} else if (Level.mainMenu.showing) {
			return;
		} else if (Level.characterCreation.showing) {
			return;
		} else if (Level.gameOver.showing) {
			return;
		} else if (character == 'i' || character == 'I') {
			Game.level.openCloseInventory();
			return;
		} else if (character == ' ') {
			waitPressed(true, false);
		} else if (character == 'c' || character == 'C') {

			if (Game.squareMouseIsOver != null) {

				long now = System.currentTimeMillis();
				String stringToCopy = "";
				if (now - lastCopy < 10000) {
					try {
						stringToCopy += (String) Toolkit.getDefaultToolkit().getSystemClipboard()
								.getData(DataFlavor.stringFlavor);
					} catch (HeadlessException e) {
						e.printStackTrace();
					} catch (UnsupportedFlavorException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				stringToCopy += ("Templates.BIG_TREE.makeCopy(Game.level.squares[" + Game.squareMouseIsOver.xInGrid
						+ "][" + Game.squareMouseIsOver.yInGrid + "],null);\n");

				StringSelection stringSelection = new StringSelection(stringToCopy);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);

				lastCopy = now;
			}
		} else if (character == 'm' || character == 'M') {
			Game.level.mapZoomInOut();
		} else if (character == 'q' || character == 'Q') {
			Game.level.centerOnPlayer();
		} else if (character == 'l' || character == 'L') {
			Game.level.showHideLogButton.click();
		} else if (character == 'w' || character == 'W') {
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				Level.ctrlActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
				Level.altActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				Level.shiftActionHasBeenPressed = true;
			}
			upPressed(true, false);
		} else if (character == 'a' || character == 'A') {
			if (Level.journal.showing) {

			} else if (Level.characterScreen.showing) {

			} else if (Level.skillTree.showing) {

			} else if (Level.availablePowerScreen.showing) {

			} else if (Game.level.openInventories.size() > 0) {
			} else if (Game.level.conversation != null
					&& Game.level.conversation.currentConversationPart.windowSelectConversationResponse.buttonTrade != null) {
				Game.level.conversation.currentConversationPart.windowSelectConversationResponse.buttonTrade.click();
			} else {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
					Level.ctrlActionHasBeenPressed = true;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
					Level.altActionHasBeenPressed = true;
				} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					Level.shiftActionHasBeenPressed = true;
				}
				leftPressed(true, false);
			}
		} else if (character == 's' || character == 'S')

		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				Level.ctrlActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
				Level.altActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				Level.shiftActionHasBeenPressed = true;
			}
			downPressed(true, false);
		} else if (character == 'd' || character == 'D') {
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
				Level.ctrlActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU)) {
				Level.altActionHasBeenPressed = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
				Level.shiftActionHasBeenPressed = true;
			}
			rightPressed(true, false);
		} else if (character == '1' || character == '2' || character == '3' || character == '4' || character == '5'
				|| character == '6' || character == '7' || character == '8' || character == '9' || character == '0') {
			if (Game.level.conversation != null) {
				Game.level.conversation.selectDialogueOption(character);
			}
		} else if (character == '+' || character == '=') {
			Game.zoomLevelIndex--;
		} else if (character == '_' || character == '-') {
			Game.zoomLevelIndex++;
		}

	}

	private static void checkButtons() {

		if (keyStateTab == false && Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			tabTyped();
			keyStateTab = true;
		} else if (keyStateTab == true && !Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			keyStateTab = false;
			Game.level.popupMenuHighlightObjects.clear();
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_TAB)) {
			keyStateTab = false;
		}

		if (keyStateUp == false && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			upPressed(true, false);
			keyStateUp = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			keyStateUp = false;
		}

		if (keyStateDown == false && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {

			downPressed(true, false);
			keyStateDown = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			keyStateDown = false;
		}

		if (keyStateLeft == false && Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			leftPressed(true, false);
			keyStateLeft = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			keyStateLeft = false;
		}

		if (keyStateRight == false && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			rightPressed(true, false);
			keyStateRight = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			keyStateRight = false;
		}

		if (keyStateReturn == false && Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			enterTyped();
			keyStateReturn = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			keyStateReturn = false;
		}

		if (keyStateBack == false && Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
			backSpaceTyped();
			keyStateBack = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
			keyStateBack = false;
		}

		if (keyStateDelete == false && Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
			deleteTyped();
			keyStateDelete = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
			keyStateDelete = false;
		}

		if (keyStateLeftShift == false && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			keyStateLeftShift = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			keyStateLeftShift = false;
		}

		if (keyStateRightShift == false && Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			keyStateRightShift = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			keyStateRightShift = false;
		}

		if (keyStateEscape == false && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			keyStateEscape = true;
			escapeTyped();
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			keyStateEscape = false;
		}

		if (keyStateSpace == false && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			keyTyped(' ');
			keyStateSpace = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			keyStateSpace = false;
		}

		if (keyStateA == false && Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('a');
				} else {
					keyTyped('A');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('A');
				} else {
					keyTyped('a');
				}
			}
			keyStateA = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_A)) {
			keyStateA = false;
		}

		if (keyStateB == false && Keyboard.isKeyDown(Keyboard.KEY_B)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('b');
				} else {
					keyTyped('B');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('B');
				} else {
					keyTyped('b');
				}
			}
			keyStateB = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_B)) {
			keyStateB = false;
		}

		if (keyStateC == false && Keyboard.isKeyDown(Keyboard.KEY_C)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('c');
				} else {
					keyTyped('C');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('C');
				} else {
					keyTyped('c');
				}
			}
			keyStateC = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_C)) {
			keyStateC = false;
		}

		if (keyStateD == false && Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('d');
				} else {
					keyTyped('D');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('D');
				} else {
					keyTyped('d');
				}
			}
			keyStateD = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_D)) {
			keyStateD = false;
		}

		if (keyStateE == false && Keyboard.isKeyDown(Keyboard.KEY_E)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('e');
				} else {
					keyTyped('E');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('E');
				} else {
					keyTyped('e');
				}
			}
			keyStateE = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_E)) {
			keyStateE = false;
		}

		if (keyStateF == false && Keyboard.isKeyDown(Keyboard.KEY_F)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('f');
				} else {
					keyTyped('F');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('F');
				} else {
					keyTyped('f');
				}
			}
			keyStateF = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_F)) {
			keyStateF = false;
		}

		if (keyStateG == false && Keyboard.isKeyDown(Keyboard.KEY_G)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('g');
				} else {
					keyTyped('G');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('G');
				} else {
					keyTyped('g');
				}
			}
			keyStateG = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_G)) {
			keyStateG = false;
		}

		if (keyStateH == false && Keyboard.isKeyDown(Keyboard.KEY_H)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('h');
				} else {
					keyTyped('H');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('H');
				} else {
					keyTyped('h');
				}
			}
			keyStateH = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_H)) {
			keyStateH = false;
		}

		if (keyStateI == false && Keyboard.isKeyDown(Keyboard.KEY_I)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('i');
				} else {
					keyTyped('I');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('I');
				} else {
					keyTyped('i');
				}
			}
			keyStateI = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_I)) {
			keyStateI = false;
		}

		if (keyStateJ == false && Keyboard.isKeyDown(Keyboard.KEY_J)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('j');
				} else {
					keyTyped('J');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('J');
				} else {
					keyTyped('j');
				}
			}
			keyStateJ = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_J)) {
			keyStateJ = false;
		}

		if (keyStateK == false && Keyboard.isKeyDown(Keyboard.KEY_K)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('k');
				} else {
					keyTyped('K');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('K');
				} else {
					keyTyped('k');
				}
			}
			keyStateK = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_K)) {
			keyStateK = false;
		}

		if (keyStateL == false && Keyboard.isKeyDown(Keyboard.KEY_L)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('l');
				} else {
					keyTyped('L');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('L');
				} else {
					keyTyped('l');
				}
			}
			keyStateL = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_L)) {
			keyStateL = false;
		}

		if (keyStateM == false && Keyboard.isKeyDown(Keyboard.KEY_M)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('m');
				} else {
					keyTyped('M');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('M');
				} else {
					keyTyped('m');
				}
			}
			keyStateM = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_M)) {
			keyStateM = false;
		}

		if (keyStateN == false && Keyboard.isKeyDown(Keyboard.KEY_N)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('n');
				} else {
					keyTyped('N');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('N');
				} else {
					keyTyped('n');
				}
			}
			keyStateN = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_N)) {
			keyStateN = false;
		}

		if (keyStateO == false && Keyboard.isKeyDown(Keyboard.KEY_O)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('o');
				} else {
					keyTyped('O');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('O');
				} else {
					keyTyped('o');
				}
			}
			keyStateO = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_O)) {
			keyStateO = false;
		}

		if (keyStateP == false && Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('p');
				} else {
					keyTyped('P');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('P');
				} else {
					keyTyped('p');
				}
			}
			keyStateP = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_P)) {
			keyStateP = false;
		}

		if (keyStateQ == false && Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('q');
				} else {
					keyTyped('Q');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('Q');
				} else {
					keyTyped('q');
				}
			}
			keyStateQ = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			keyStateQ = false;
		}

		if (keyStateR == false && Keyboard.isKeyDown(Keyboard.KEY_R)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('r');
				} else {
					keyTyped('R');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('R');
				} else {
					keyTyped('r');
				}
			}
			keyStateR = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_R)) {
			keyStateR = false;
		}

		if (keyStateS == false && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('s');
				} else {
					keyTyped('S');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('S');
				} else {
					keyTyped('s');
				}
			}
			keyStateS = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_S)) {
			keyStateS = false;
		}

		if (keyStateT == false && Keyboard.isKeyDown(Keyboard.KEY_T)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('t');
				} else {
					keyTyped('T');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('T');
				} else {
					keyTyped('t');
				}
			}
			keyStateT = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_T)) {
			keyStateT = false;
		}

		if (keyStateU == false && Keyboard.isKeyDown(Keyboard.KEY_U)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('u');
				} else {
					keyTyped('U');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('U');
				} else {
					keyTyped('u');
				}
			}
			keyStateU = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_U)) {
			keyStateU = false;
		}

		if (keyStateV == false && Keyboard.isKeyDown(Keyboard.KEY_V)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('v');
				} else {
					keyTyped('V');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('V');
				} else {
					keyTyped('v');
				}
			}
			keyStateV = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_V)) {
			keyStateV = false;
		}

		if (keyStateW == false && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('w');
				} else {
					keyTyped('W');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('W');
				} else {
					keyTyped('w');
				}
			}
			keyStateW = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_W)) {
			keyStateW = false;
		}

		if (keyStateX == false && Keyboard.isKeyDown(Keyboard.KEY_X)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('x');
				} else {
					keyTyped('X');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('X');
				} else {
					keyTyped('x');
				}
			}
			keyStateX = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_X)) {
			keyStateX = false;
		}

		if (keyStateY == false && Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('y');
				} else {
					keyTyped('Y');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('Y');
				} else {
					keyTyped('y');
				}
			}
			keyStateY = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			keyStateY = false;
		}

		if (keyStateZ == false && Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			if (capsLock) {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('z');
				} else {
					keyTyped('Z');
				}
			} else {
				if (keyStateLeftShift || keyStateRightShift) {
					keyTyped('Z');
				} else {
					keyTyped('z');
				}
			}
			keyStateZ = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			keyStateZ = false;
		}

		if (keyState1 == false && Keyboard.isKeyDown(Keyboard.KEY_1)) {
			keyTyped('1');
			keyState1 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_1)) {
			keyState1 = false;
		}

		if (keyState2 == false && Keyboard.isKeyDown(Keyboard.KEY_2)) {
			keyTyped('2');
			keyState2 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_2)) {
			keyState2 = false;
		}

		if (keyState3 == false && Keyboard.isKeyDown(Keyboard.KEY_3)) {
			keyTyped('3');
			keyState3 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_3)) {
			keyState3 = false;
		}

		if (keyState4 == false && Keyboard.isKeyDown(Keyboard.KEY_4)) {
			keyTyped('4');
			keyState4 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_4)) {
			keyState4 = false;
		}

		if (keyState5 == false && Keyboard.isKeyDown(Keyboard.KEY_5)) {
			keyTyped('5');
			keyState5 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_5)) {
			keyState5 = false;
		}

		if (keyState6 == false && Keyboard.isKeyDown(Keyboard.KEY_6)) {
			keyTyped('6');
			keyState6 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_6)) {
			keyState6 = false;
		}

		if (keyState7 == false && Keyboard.isKeyDown(Keyboard.KEY_7)) {
			keyTyped('7');
			keyState7 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_7)) {
			keyState7 = false;
		}

		if (keyState8 == false && Keyboard.isKeyDown(Keyboard.KEY_8)) {
			keyTyped('8');
			keyState8 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_8)) {
			keyState8 = false;
		}

		if (keyState9 == false && Keyboard.isKeyDown(Keyboard.KEY_9)) {
			keyTyped('9');
			keyState9 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_9)) {
			keyState9 = false;
		}

		if (keyState0 == false && Keyboard.isKeyDown(Keyboard.KEY_0)) {
			keyTyped('0');
			keyState0 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_0)) {
			keyState0 = false;
		}

		if (keyStateMinus == false && Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
			keyTyped('-');
			keyStateMinus = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
			keyStateMinus = false;
		}

		if (keyStateUnderscore == false && Keyboard.isKeyDown(Keyboard.KEY_UNDERLINE)) {
			keyTyped('_');
			keyStateUnderscore = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_UNDERLINE)) {
			keyStateUnderscore = false;
		}

		if (keyStateEquals == false && Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
			keyTyped('=');
			keyStateEquals = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {
			keyStateEquals = false;
		}

		if (keyStatePlus == false && Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			keyTyped('+');
			keyStatePlus = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			keyStatePlus = false;
		}

		if (keyStatePeriod == false && Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) {
			keyTyped('.');
			keyStatePeriod = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) {
			keyStatePeriod = false;
		}

		if (keyStatePeriod == false && Keyboard.isKeyDown(Keyboard.KEY_SLASH)) {
			keyTyped('/');
			keyStateForwardSlash = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_SLASH)) {
			keyStateForwardSlash = false;
		}

		if (keyStateF5 == false && Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			Save.save();
			keyStateF5 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_F5)) {
			keyStateF5 = false;
		}

		if (keyStateF9 == false && Keyboard.isKeyDown(Keyboard.KEY_F9)) {
			Load.load();
			keyStateF9 = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_F9)) {
			keyStateF9 = false;
		}
	}
}
