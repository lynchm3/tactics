package com.marklynch.tactics.objects.level;

import java.util.Vector;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import com.marklynch.Dialog;
import com.marklynch.Game;
import com.marklynch.GameCursor;
import com.marklynch.tactics.objects.GameObject;
import com.marklynch.tactics.objects.unit.Actor;
import com.marklynch.tactics.objects.weapons.Weapon;
import com.marklynch.ui.Button;
import com.marklynch.utils.Resources;

public class Level {

	public int width;
	public int height;
	public GameCursor gameCursor;
	public Vector<Actor> actors;
	public Actor selectedActor;
	public Vector<GameObject> gameObjects;
	public Vector<Dialog> dialogs;
	public Square[][] squares;
	public Button endTurnButton;
	public int turn = 1;
	public TrueTypeFont font;

	// java representation of a grid??
	// 2d array?

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		squares = new Square[width][height];
		initGrid();
		initObjects();
		dialogs = new Vector<Dialog>();
		endTurnButton = new Button(Game.windowWidth - 210,
				Game.windowHeight - 110, 200, 100, "end_turn_button.png", this);
		font = Resources.getGlobalFont("KeepCalm-Medium.ttf");
	}

	private void initGrid() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				squares[i][j] = new Square(i, j, "grass.png", 1, 0);
			}
		}
	}

	private void initObjects() {

		// Actors
		actors = new Vector<Actor>();

		Weapon weapon1ForActor1 = new Weapon(3, 1, "avatar.png");
		Weapon weapon2ForActor1 = new Weapon(2, 2, "avatar.png");
		Vector<Weapon> weaponsForActor1 = new Vector<Weapon>();
		weaponsForActor1.add(weapon1ForActor1);
		weaponsForActor1.add(weapon2ForActor1);

		actors.add(new Actor("John Lennon", "Fighter", 1, 0, 0, 0, 0,
				"avatar.png", squares[0][0], weaponsForActor1));
		actors.add(new Actor("Paul McCartney", "Maniac", 2, 0, 0, 0, 0,
				"avatar.png", squares[2][7], new Vector<Weapon>()));
		actors.add(new Actor("George Harrison", "Thief", 3, 0, 0, 0, 0,
				"avatar.png", squares[5][3], new Vector<Weapon>()));

		// Game Objects
		gameObjects = new Vector<GameObject>();
		gameObjects.add(new GameObject(0, 0, 0, 0, "skip.png", squares[3][3],
				weaponsForActor1));

		// Cursor
		gameCursor = new GameCursor("highlight.png", "highlight2.png");
	}

	public void removeWalkingHighlight() {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				squares[i][j].reachableBySelectedCharater = false;
			}
		}
	}

	public void removeWeaponsThatCanAttackHighlight() {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				squares[i][j].weaponsThatCanAttack.clear();
			}
		}
	}

	public void draw() {

		// zoom
		GL11.glPushMatrix();

		GL11.glTranslatef(Game.windowWidth / 2, Game.windowHeight / 2, 0);
		GL11.glScalef(Game.zoom, Game.zoom, 0);
		GL11.glTranslatef(Game.dragX, Game.dragY, 0);
		GL11.glTranslatef(-Game.windowWidth / 2, -Game.windowHeight / 2, 0);

		// Squares
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// is it better to bind once and draw all the same ones?
				squares[i][j].draw(this);
			}
		}

		// Cursor
		// if (selectedActor != null) {
		// gameCursor.imageTexture.bind();
		// int cursorPositionXInPixels = gameCursor.square.x
		// * (int) Game.SQUARE_WIDTH;
		// int cursorPositionYInPixels = gameCursor.square.y
		// * (int) Game.SQUARE_HEIGHT;
		// GL11.glPushMatrix();
		// GL11.glBegin(GL11.GL_QUADS);
		// GL11.glTexCoord2f(0, 0);
		// GL11.glVertex2f(cursorPositionXInPixels, cursorPositionYInPixels);
		// GL11.glTexCoord2f(1, 0);
		// GL11.glVertex2f(cursorPositionXInPixels + Game.SQUARE_WIDTH,
		// cursorPositionYInPixels);
		// GL11.glTexCoord2f(1, 1);
		// GL11.glVertex2f(cursorPositionXInPixels + Game.SQUARE_WIDTH,
		// cursorPositionYInPixels + Game.SQUARE_HEIGHT);
		// GL11.glTexCoord2f(0, 1);
		// GL11.glVertex2f(cursorPositionXInPixels, cursorPositionYInPixels
		// + Game.SQUARE_HEIGHT);
		// GL11.glEnd();
		// GL11.glPopMatrix();
		// }

		// Objects

		for (GameObject gameObject : gameObjects) {
			gameObject.draw();
		}

		// Actors

		for (Actor actor : actors) {
			actor.draw();
		}
		// zoom end
		GL11.glPopMatrix();

		// Dialogs
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// is it better to bind once and draw all the same ones?
				squares[i][j].drawDialogs();
			}
		}

		// End Turn Button
		endTurnButton.draw();

		// Turn text
		font.drawString(Game.windowWidth - 100, 20, "Turn " + turn, Color.black);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

	public void clearDialogs() {
		// Level
		this.dialogs.clear();
		// Squares
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				squares[i][j].dialogs.clear();
			}
		}
	}

	public Button getButtonFromMousePosition() {

		if (Mouse.getX() > endTurnButton.x
				&& Mouse.getX() < endTurnButton.x + endTurnButton.width
				&& Game.windowHeight - Mouse.getY() > endTurnButton.y
				&& Game.windowHeight - Mouse.getY() < endTurnButton.y
						+ endTurnButton.height) {

			return endTurnButton;
		}
		return null;
	}

	public void endTurn() {
		this.turn++;
	}
}
