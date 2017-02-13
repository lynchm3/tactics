package com.marklynch.objects.units;

import java.util.ArrayList;
import java.util.Vector;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.marklynch.Game;
import com.marklynch.ai.routines.AIRoutine;
import com.marklynch.level.Square;
import com.marklynch.level.constructs.Conversation;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.constructs.Pack;
import com.marklynch.objects.Bed;
import com.marklynch.objects.Carcass;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Inventory;
import com.marklynch.objects.Owner;
import com.marklynch.objects.Quest;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionAttack;
import com.marklynch.objects.actions.ActionTalk;
import com.marklynch.objects.weapons.Weapon;
import com.marklynch.ui.ActivityLog;
import com.marklynch.ui.button.AttackButton;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.WeaponButton;
import com.marklynch.utils.FormattingUtils;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

import mdesl.graphics.Color;

public class Actor extends ActorTemplate implements Owner {

	public final static String[] editableAttributes = { "name", "imageTexture", "faction", "strength", "dexterity",
			"intelligence", "endurance", "totalHealth", "remainingHealth", "travelDistance", "inventory",
			"showInventory", "fitsInInventory", "canContainOtherObjects" };

	public enum Direction {
		UP, RIGHT, DOWN, LEFT
	}

	public transient int distanceMovedThisTurn = 0;
	public transient boolean showWeaponButtons = false;

	// buttons
	public transient ArrayList<Button> buttons;
	public transient AttackButton attackButton = null;
	public transient AttackButton pushButton = null;
	public transient float buttonsAnimateCurrentTime = 0f;
	public final transient float buttonsAnimateMaxTime = 200f;

	// weapon buttons
	public transient ArrayList<Button> weaponButtons;

	// Fight preview on hover
	public transient boolean showHoverFightPreview = false;
	public transient GameObject hoverFightPreviewDefender = null;
	public transient Vector<Fight> hoverFightPreviewFights;

	public transient AIRoutine aiRoutine;

	public String activityDescription = "";

	public transient Bed bed;
	public String bedGUID = null;

	public transient Faction faction;
	public String factionGUID = null;

	public Weapon equippedWeapon = null;
	public String equippedWeaponGUID = null;

	public transient Pack pack;
	private transient ArrayList<Actor> attackers;
	public transient Quest quest;

	public transient Conversation conversation;

	public Actor(String name, String title, int actorLevel, int health, int strength, int dexterity, int intelligence,
			int endurance, String imagePath, Square squareActorIsStandingOn, int travelDistance, Bed bed,
			Inventory inventory, boolean showInventory, boolean fitsInInventory, boolean canContainOtherObjects) {

		super(name, title, actorLevel, health, strength, dexterity, intelligence, endurance, imagePath,
				squareActorIsStandingOn, travelDistance, inventory, showInventory, fitsInInventory,
				canContainOtherObjects);

		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.endurance = endurance;

		this.title = title;
		this.actorLevel = actorLevel;
		this.travelDistance = travelDistance;

		this.bed = bed;
		if (bed != null)
			this.bedGUID = bed.guid;

		buttons = new ArrayList<Button>();
		weaponButtons = new ArrayList<Button>();

		this.attackButton = new AttackButton(0, 0, 50, 50, "attack.png", "attack.png");

		this.pushButton = new AttackButton(0, 0, 50, 50, "push.png", "push.png");

		buttons.add(attackButton);
		buttons.add(pushButton);
		attackButton.enabled = true;

		ArrayList<Weapon> weapons = getWeaponsInInventory();
		//
		// for (Weapon weapon : weapons) {
		// weaponButtons.add(new WeaponButton(0, 0, 50, 50,
		// weapon.imageTexturePath, weapon.imageTexturePath, weapon));
		// }

		if (weapons.size() > 0 && weapons.get(0) != null) {
			equippedWeapon = weapons.get(0);
			equippedWeaponGUID = weapons.get(0).guid;
		}

		hoverFightPreviewFights = new Vector<Fight>();

		attackers = new ArrayList<Actor>();

		conversation = new Conversation(null);
	}

	@Override
	public void postLoad1() {

		super.postLoad1();

		buttons = new ArrayList<Button>();
		weaponButtons = new ArrayList<Button>();
		this.attackButton = new AttackButton(0, 0, 50, 50, "attack.png", "attack.png");

		this.pushButton = new AttackButton(0, 0, 50, 50, "push.png", "push.png");

		buttons.add(attackButton);
		buttons.add(pushButton);
		attackButton.enabled = true;

		ArrayList<Weapon> weapons = getWeaponsInInventory();

		for (Weapon weapon : weapons) {
			weaponButtons.add(new WeaponButton(0, 0, 50, 50, weapon.imageTexturePath, weapon.imageTexturePath, weapon));
		}

		if (weapons.size() > 0 && weapons.get(0) != null) {
			equippedWeaponGUID = weapons.get(0).guid;
		}

		hoverFightPreviewFights = new Vector<Fight>();

		loadImages();
	}

	@Override
	public void postLoad2() {
		super.postLoad2();
		super.postLoad2();

		// faction
		if (factionGUID != null) {
			for (Faction faction : Game.level.factions) {
				if (factionGUID.equals(faction.guid)) {
					this.faction = faction;
					if (!faction.actors.contains(this)) {
						faction.actors.add(this);
					}
				}
			}
		}

		// bed
		if (bedGUID != null) {
			for (GameObject gameObject : Game.level.inanimateObjectsOnGround) {
				if (bedGUID.equals(gameObject.guid)) {
					this.bed = (Bed) gameObject;
				}
			}
		}

		// equippedWeapon
		if (equippedWeaponGUID != null) {
			for (Weapon weapon : this.getWeaponsInInventory()) {
				if (equippedWeaponGUID.equals(weapon.guid)) {
					this.equippedWeapon = weapon;
				}
			}
		}
	}

	public void calculateReachableSquares(Square[][] squares) {

		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {

				Path pathToSquare = paths.get(squares[i][j]);
				if (pathToSquare == null
						|| pathToSquare.travelCost > (this.travelDistance - this.distanceMovedThisTurn)) {
					squares[i][j].reachableBySelectedCharater = false;
					// squares[i][j].distanceToSquare = Integer.MAX_VALUE;

				} else {
					squares[i][j].reachableBySelectedCharater = true;
					// squares[i][j].distanceToSquare = pathToSquare.travelCost;
				}

			}
		}
	}

	public void calculateAttackableSquares(Square[][] squares) {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				squares[i][j].weaponsThatCanAttack.clear();
			}
		}

		for (Weapon weapon : getWeaponsInInventory()) {
			weapon.calculateAttackableSquares(squares);
		}
	}

	public static void highlightSelectedCharactersSquares() {
		Game.level.activeActor.calculatePathToAllSquares(Game.level.squares);
		Game.level.activeActor.calculateReachableSquares(Game.level.squares);
		Game.level.activeActor.calculateAttackableSquares(Game.level.squares);
	}

	public int straightLineDistanceTo(Square square) {

		return Math.abs(square.xInGrid - this.squareGameObjectIsOn.xInGrid)
				+ Math.abs(square.yInGrid - this.squareGameObjectIsOn.yInGrid);

	}

	public boolean hasRange(int weaponDistance) {
		for (Weapon weapon : getWeaponsInInventory()) {
			if (weaponDistance >= weapon.minRange && weaponDistance <= weapon.maxRange) {
				// selectedWeapon = weapon;
				return true;
			}
		}
		return false;
	}

	public void equipBestWeapon(GameObject target) {

		// take countering in to account, this is quite an interesting issue,
		// coz I know the enemy is going to pick the best weapon to counter
		// with....
		// weird...

		int range = this.straightLineDistanceTo(target.squareGameObjectIsOn);
		for (Weapon weapon : getWeaponsInInventory()) {
			if (range >= weapon.minRange && range <= weapon.maxRange) {
				equippedWeapon = weapon;
				equippedWeaponGUID = weapon.guid;
			}
		}
	}

	public void equipBestWeaponForCounter(GameObject target, Weapon targetsWeapon) {

		ArrayList<Weapon> potentialWeaponsToEquip = new ArrayList<Weapon>();

		int range = this.straightLineDistanceTo(target.squareGameObjectIsOn);
		for (Weapon weapon : getWeaponsInInventory()) {
			if (range >= weapon.minRange && range <= weapon.maxRange) {
				potentialWeaponsToEquip.add(weapon);
			}
		}

		if (potentialWeaponsToEquip.size() == 0) {
			equippedWeapon = null;
			equippedWeaponGUID = null;
		} else if (potentialWeaponsToEquip.size() == 1) {
			equippedWeapon = potentialWeaponsToEquip.get(0);
			equippedWeaponGUID = potentialWeaponsToEquip.get(0).guid;
		} else {
			ArrayList<Fight> fights = new ArrayList<Fight>();
			for (Weapon weapon : potentialWeaponsToEquip) {
				Fight fight = new Fight(this, weapon, target, targetsWeapon, range);
				fights.add(fight);
			}
			fights.sort(new Fight.FightComparator());
			equippedWeapon = fights.get(0).attackerWeapon;
			equippedWeaponGUID = fights.get(0).attackerWeapon.guid;
		}
	}

	@Override
	public boolean checkIfDestroyed() {
		if (remainingHealth <= 0) {
			// Remove from draw/update
			this.squareGameObjectIsOn.inventory.remove(this);
			// this.faction.actors.remove(this);

			// add a carcass
			GameObject carcass = new Carcass(this.name + " carcass", 5, "carcass.png", this.squareGameObjectIsOn,
					new Inventory(), false, true, false, true);

			this.giveAllToTarget(null, carcass);
			this.squareGameObjectIsOn.inventory.add(carcass);

			if (!Game.level.inanimateObjectsOnGround.contains(carcass))
				Game.level.inanimateObjectsOnGround.add(carcass);

			// screamAudio.playAsSoundEffect(1.0f, 1.0f, false);
			return true;
		}
		return false;
	}

	@Override
	public void draw1() {

		if (this.remainingHealth <= 0)
			return;

		if (Game.level.activeActor != null && Game.level.activeActor.showHoverFightPreview
				&& Game.level.activeActor.hoverFightPreviewDefender == this) {

		} else {
			// Draw health

			if (Game.level.activeActor != null && Game.level.activeActor.showHoverFightPreview) {

			} else if (remainingHealth != totalHealth) {

				// draw sidebar on square
				float healthPercentage = (remainingHealth) / (totalHealth);
				float weaponAreaWidthInPixels = Game.SQUARE_WIDTH / 20;
				float weaponAreaHeightInPixels = Game.SQUARE_HEIGHT;
				float healthBarHeightInPixels = Game.SQUARE_HEIGHT * healthPercentage;
				float weaponAreaPositionXInPixels = 0;
				float weaponAreaPositionYInPixels = 0;

				if (this.faction == Game.level.factions.get(0)) {
					weaponAreaPositionXInPixels = this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH;
					weaponAreaPositionYInPixels = this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT;
				} else {
					weaponAreaPositionXInPixels = this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH
							+ Game.SQUARE_WIDTH - weaponAreaWidthInPixels;
					weaponAreaPositionYInPixels = this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT;

				}

				// White bit under health bar
				QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f), weaponAreaPositionXInPixels + 1,
						weaponAreaPositionXInPixels + weaponAreaWidthInPixels - 1, weaponAreaPositionYInPixels + 1,
						weaponAreaPositionYInPixels + weaponAreaHeightInPixels - 1);

				// Colored health bar
				QuadUtils.drawQuad(new Color(this.faction.color.r, this.faction.color.g, this.faction.color.b),
						weaponAreaPositionXInPixels + 1, weaponAreaPositionXInPixels + weaponAreaWidthInPixels - 1,
						weaponAreaPositionYInPixels + 1, weaponAreaPositionYInPixels + healthBarHeightInPixels - 1);
			}

			super.draw1();
			// Draw arm

			// OLD LADY IMAGE
			// origin 0,0
			// shoulder 65, 41
			// total 128,128

			// ARM IMAGE
			// origin 0,0
			// shoulder 11, 13
			// hand 70,110
			// total 128, 128

			// AXE IMAGE
			// origin 0,0
			// hand 20,86
			// total 128, 128

			// Need to scale it, rotate it on attack, put weapon in hand
			// Also have dude move over to the enemy :P
			// and short attack animation where he just bumps in the general
			// direction... based on a setting

			int actorPositionXInPixels = this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH;
			int actorPositionYInPixels = this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT;

			// body shoulder coords, arm shoulder coords
			int armPositionXInPixels = actorPositionXInPixels + 65 - 11;
			int armPositionYInPixels = actorPositionYInPixels + 41 - 13;

			int rotateXInPixels = actorPositionXInPixels + 65;
			int rotateYInPixels = actorPositionYInPixels + 41;

			int equippedWeaponPositionXInPixels = armPositionXInPixels + 70 - 20;
			int equippedWeaponPositionYInPixels = armPositionYInPixels + 110 - 86;

			float alpha = 1.0f;
			if (Game.level.activeActor != null && Game.level.activeActor.showHoverFightPreview == true
					&& Game.level.activeActor.hoverFightPreviewDefender != this) {
				alpha = 0.5f;
			}

			if (hasAttackedThisTurn == true && this.faction != null
					&& Game.level.currentFactionMoving == this.faction) {
				alpha = 0.5f;
			}

			Game.activeBatch.flush();
			Matrix4f view = Game.activeBatch.getViewMatrix();
			view.translate(new Vector2f(rotateXInPixels, rotateYInPixels));
			view.rotate((float) Math.toRadians(60), new Vector3f(0f, 0f, 1f));
			// view.scale(new Vector3f(Game.zoom, Game.zoom, 1f));
			view.translate(new Vector2f(-rotateXInPixels, -rotateYInPixels));
			Game.activeBatch.updateUniforms();

			// Draw weapon
			if (equippedWeapon != null) {
				// TextureUtils.skipNormals = true;
				//
				// TextureUtils.drawTexture(equippedWeapon.imageTexture, alpha,
				// equippedWeaponPositionXInPixels,
				// equippedWeaponPositionXInPixels + Game.SQUARE_WIDTH,
				// equippedWeaponPositionYInPixels,
				// equippedWeaponPositionYInPixels + Game.SQUARE_HEIGHT);
				// TextureUtils.skipNormals = false;

				// TextureUtils.drawTexture(equippedWeapon.imageTexture,
				// this.squareGameObjectIsOn.x * (Game.SQUARE_WIDTH)
				// + Game.SQUARE_WIDTH / 2f - 0,
				// this.squareGameObjectIsOn.x * (Game.SQUARE_WIDTH)
				// + Game.SQUARE_WIDTH / 2f + 32,
				// this.squareGameObjectIsOn.y * (Game.SQUARE_HEIGHT)
				// + Game.SQUARE_WIDTH / 2f - 16,
				// this.squareGameObjectIsOn.y * (Game.SQUARE_HEIGHT)
				// + Game.SQUARE_WIDTH / 2f + 16);
			}

			// Draw arm
			// TextureUtils.skipNormals = true;
			// TextureUtils.drawTexture(armTexture, alpha, armPositionXInPixels,
			// armPositionXInPixels + Game.SQUARE_WIDTH,
			// armPositionYInPixels, armPositionYInPixels + Game.SQUARE_HEIGHT);
			// TextureUtils.skipNormals = false;

			Game.activeBatch.flush();
			view.translate(new Vector2f(rotateXInPixels, rotateYInPixels));
			view.rotate((float) Math.toRadians(-60), new Vector3f(0f, 0f, 1f));
			// view.scale(new Vector3f(Game.zoom, Game.zoom, 1f));
			view.translate(new Vector2f(-rotateXInPixels, -rotateYInPixels));
			Game.activeBatch.updateUniforms();

			if (Game.level.activeActor != null && Game.level.activeActor.showHoverFightPreview) {

			} else {
				// draw weapon icons on square
				// float weaponWidthInPixels = Game.SQUARE_WIDTH / 5;
				// float weaponHeightInPixels = Game.SQUARE_HEIGHT / 5;
				// ArrayList<Weapon> weapons = getWeaponsInInventory();
				// for (int i = 0; i < weapons.size(); i++) {
				//
				// Weapon weapon = weapons.get(i);
				//
				// float weaponIconPositionXInPixels = 0;
				// float weaponIconPositionYInPixels = 0;
				//
				// if (this.faction == Game.level.factions.get(0)) {
				// weaponIconPositionXInPixels =
				// this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH;
				// weaponIconPositionYInPixels =
				// this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT
				// + (i * weaponHeightInPixels);
				// } else {
				// weaponIconPositionXInPixels =
				// this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH
				// + Game.SQUARE_WIDTH - weaponWidthInPixels;
				// weaponIconPositionYInPixels =
				// this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT
				// + (i * weaponHeightInPixels);
				//
				// }
				// TextureUtils.drawTexture(weapon.imageTexture,
				// weaponIconPositionXInPixels,
				// weaponIconPositionXInPixels + weaponWidthInPixels,
				// weaponIconPositionYInPixels,
				// weaponIconPositionYInPixels + weaponHeightInPixels);
				// }
			}

			// Draw actor level text
			// String actorLevelString = "L" + this.actorLevel;
			// float actorLevelWidthInPixels =
			// Game.font.getWidth(actorLevelString);
			// float actorLevelPositionXInPixels =
			// (this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH)
			// + Game.SQUARE_WIDTH - actorLevelWidthInPixels - Game.SQUARE_WIDTH
			// / 5;
			// float actorLevelPositionYInPixels =
			// this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT;
			//
			// TextUtils.printTextWithImages(new Object[] { actorLevelString },
			// actorLevelPositionXInPixels,
			// actorLevelPositionYInPixels, Integer.MAX_VALUE, true);

			// draw indicators of whether you can move and/or attack
			// float moveAttackStatusWidthInPixels = Game.font.getWidth("MA");//
			// Game.SQUARE_WIDTH
			// float attackStatusWidthInPixels = Game.font.getWidth("A");//
			// Game.SQUARE_WIDTH

			// float moveAttackStatusPositionXInPixels =
			// (this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH)
			// + Game.SQUARE_WIDTH - moveAttackStatusWidthInPixels -
			// Game.SQUARE_WIDTH / 5;
			// float attackStatusPositionXInPixels =
			// (this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH)
			// + Game.SQUARE_WIDTH - attackStatusWidthInPixels -
			// Game.SQUARE_WIDTH / 5;
			// float moveAttackStatusPositionYInPixels =
			// this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT
			// + Game.SQUARE_HEIGHT - 14;

			// if (hasAttackedThisTurn == false) {
			// if (this.distanceMovedThisTurn < this.travelDistance) {
			//
			// TextUtils.printTextWithImages(new Object[] { "MA" },
			// moveAttackStatusPositionXInPixels,
			// moveAttackStatusPositionYInPixels, Integer.MAX_VALUE, true);
			// } else {
			//
			// TextUtils.printTextWithImages(new Object[] { "A" },
			// attackStatusPositionXInPixels,
			// moveAttackStatusPositionYInPixels, Integer.MAX_VALUE, true);
			// }
			// }
			// GL11.glColor3f(1.0f, 1.0f, 1.0f);
		}

	}

	@Override
	public void draw2() {

		if (this.remainingHealth <= 0)
			return;

		super.draw2();

		// Draw activity text
		if (activityDescription != null && activityDescription.length() > 0) {
			float activityX1 = this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH;
			float activityX2 = this.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH
					+ Game.font.getWidth(activityDescription);
			float activityY1 = this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_WIDTH + 60;
			float activityY2 = this.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_WIDTH + 80;
			QuadUtils.drawQuad(new Color(0.0f, 0.0f, 0.0f, 0.5f), activityX1, activityX2, activityY1, activityY2);
			TextUtils.printTextWithImages(new Object[] { activityDescription }, activityX1, activityY1,
					Integer.MAX_VALUE, false);
		}

	}

	@Override
	public void drawUI() {
		super.drawUI();

		// Hover fight preview
		// GL11.glColor3f(1.0f, 1.0f, 1.0f);
		if (this.showHoverFightPreview) {

			float hoverFightPreviewPositionXInPixels = (hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid
					* (int) Game.SQUARE_WIDTH);
			float hoverFightPreviewPositionYInPixels = hoverFightPreviewDefender.squareGameObjectIsOn.yInGrid
					* (int) Game.SQUARE_HEIGHT;

			// // BG white
			// QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f),
			// hoverFightPreviewPositionXInPixels,
			// hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH,
			// hoverFightPreviewPositionYInPixels,
			// hoverFightPreviewPositionYInPixels + Game.SQUARE_HEIGHT);

			// Target image
			TextureUtils.drawTexture(this.hoverFightPreviewDefender.imageTexture, hoverFightPreviewPositionXInPixels,
					hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH, hoverFightPreviewPositionYInPixels,
					hoverFightPreviewPositionYInPixels + Game.SQUARE_HEIGHT);

			// VS image
			TextureUtils.drawTexture(this.vsTexture, hoverFightPreviewPositionXInPixels,
					hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH, hoverFightPreviewPositionYInPixels,
					hoverFightPreviewPositionYInPixels + Game.SQUARE_HEIGHT);

			float[] previewPositionYs = new float[hoverFightPreviewFights.size()];
			float previewHeight = 28f;
			if (hoverFightPreviewFights.size() == 1) {
				previewPositionYs[0] = 50f + hoverFightPreviewPositionYInPixels;
			} else if (hoverFightPreviewFights.size() == 2) {
				previewPositionYs[0] = 24f + hoverFightPreviewPositionYInPixels;
				previewPositionYs[1] = 76f + hoverFightPreviewPositionYInPixels;
			} else {
				float posY = 11f + hoverFightPreviewPositionYInPixels;
				for (int i = 0; i < previewPositionYs.length; i++) {
					previewPositionYs[i] = posY;
					posY += 11f + 28f;
				}
			}

			for (int i = 0; i < hoverFightPreviewFights.size(); i++) {

				// // BG white under attacker health bar
				// QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f),
				// hoverFightPreviewPositionXInPixels,
				// hoverFightPreviewPositionXInPixels - 10,
				// previewPositionYs[i], previewPositionYs[i] + 28);
				//
				// // BG white under defender health bar
				// QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f),
				// hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH,
				// hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH
				// + 10, previewPositionYs[i],
				// previewPositionYs[i] + 28);

				// Attacker Widths of bars

				float attackerTotalHealthWidth = ((Game.SQUARE_WIDTH + 20) / 2);

				float attackerCurrentHealthWidth = ((Game.SQUARE_WIDTH + 20) / 2)
						* (this.remainingHealth / totalHealth);

				float attackerPotentialHealthLossWidth = ((Game.SQUARE_WIDTH + 20) / 2)
						* (hoverFightPreviewFights.get(i).damageTakenByAttacker / totalHealth);

				float attackerPotentialRemainingHealthWidth = attackerCurrentHealthWidth
						- attackerPotentialHealthLossWidth;

				// Attacker Positions of bars
				float attackerTotalHealthX = this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid
						* (Game.SQUARE_WIDTH) - 10;

				float attackerCurrentHealthX = attackerTotalHealthX;

				float attackerPotentialRemainingHealthX = attackerCurrentHealthX;

				float attackerPotentialHealthLossX = attackerPotentialRemainingHealthX
						+ attackerPotentialRemainingHealthWidth;

				// Attacker draw total health
				QuadUtils.drawQuad(new Color(this.faction.color.r, this.faction.color.g, this.faction.color.b, 0.25f),
						attackerTotalHealthX, attackerTotalHealthX + attackerTotalHealthWidth, previewPositionYs[i],
						previewPositionYs[i] + 28);

				// Attacker draw potential remaining health
				QuadUtils.drawQuad(this.faction.color, attackerPotentialRemainingHealthX,
						attackerPotentialRemainingHealthX + attackerPotentialRemainingHealthWidth, previewPositionYs[i],
						previewPositionYs[i] + 28);

				float attackerHealthLossAlpha = 0f;

				if (hoverFightPreviewFights.get(i).damageTakenByAttacker >= this.remainingHealth) {
					attackerHealthLossAlpha = Game.getTime() % 1000f;
					if (attackerHealthLossAlpha <= 500) {
						attackerHealthLossAlpha = attackerHealthLossAlpha / 500;
					} else {
						attackerHealthLossAlpha = (1000 - attackerHealthLossAlpha) / 500;
					}

				} else {
					attackerHealthLossAlpha = Game.getTime() % 2000f;
					if (attackerHealthLossAlpha <= 1000) {
						attackerHealthLossAlpha = attackerHealthLossAlpha / 1000;
					} else {
						attackerHealthLossAlpha = (2000 - attackerHealthLossAlpha) / 1000;
					}
				}

				// Attacker draw potential health loss
				QuadUtils.drawQuad(
						new Color(this.faction.color.r, this.faction.color.g, this.faction.color.b,
								attackerHealthLossAlpha),
						attackerPotentialHealthLossX, attackerPotentialHealthLossX + attackerPotentialHealthLossWidth,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// Attacker line 1
				QuadUtils.drawQuad(Color.WHITE, attackerPotentialRemainingHealthX,
						attackerPotentialRemainingHealthX + 1, previewPositionYs[i], previewPositionYs[i] + 28);

				// Attacker line 2
				QuadUtils.drawQuad(Color.WHITE,
						attackerPotentialRemainingHealthX + attackerPotentialRemainingHealthWidth,
						attackerPotentialRemainingHealthX + attackerPotentialRemainingHealthWidth + 1,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// Attacker line 3
				QuadUtils.drawQuad(Color.WHITE, attackerPotentialHealthLossX + attackerPotentialHealthLossWidth,
						attackerPotentialHealthLossX + attackerPotentialHealthLossWidth + 1, previewPositionYs[i],
						previewPositionYs[i] + 28);

				// attacker skull symbol
				// if (hoverFightPreviewFights.get(i).damageTakenByAttacker >=
				// this.remainingHealth) {
				// TextureUtils
				// .drawTexture(
				// skullTexture,
				// this.hoverFightPreviewDefender.squareGameObjectIsOn.x
				// * (Game.SQUARE_WIDTH) - 32,
				// this.hoverFightPreviewDefender.squareGameObjectIsOn.x
				// * (Game.SQUARE_WIDTH),
				// previewPositionYs[i] - 2,
				// previewPositionYs[i] + 30);
				// }

				// attacker weapon
				TextureUtils.drawTexture(this.hoverFightPreviewFights.get(i).attackerWeapon.imageTexture,
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 32,
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH),
						previewPositionYs[i] - 2, previewPositionYs[i] + 30);

				// attacker hit chance

				TextUtils.printTextWithImages(
						new Object[] { this.hoverFightPreviewFights.get(i).chanceOfHittingDefender + "%" },
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH),
						previewPositionYs[i], Integer.MAX_VALUE, true);

				// attacker damage
				String attackerDamageString = FormattingUtils
						.formatFloatRemoveUnneccessaryDigits(this.hoverFightPreviewFights.get(i).damageTakenByDefender)
						+ "�" + FormattingUtils.formatFloatRemoveUnneccessaryDigits(
								this.hoverFightPreviewFights.get(i).damageTakenByDefenderMultiplier);

				TextUtils.printTextWithImages(new Object[] { attackerDamageString },
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH),
						previewPositionYs[i] + 14, Integer.MAX_VALUE, true);

				// attacker weapon
				TextureUtils.drawTexture(this.hoverFightPreviewFights.get(i).attackerWeapon.imageTexture,
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 32,
						this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH),
						previewPositionYs[i] - 2, previewPositionYs[i] + 30);

				// attacker advantage/disadvantage
				if (this.hoverFightPreviewFights.get(i).advantage == Fight.Advantage.ATTACKER_ADVANTAGE) {
					TextureUtils.drawTexture(upTexture,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 20,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 4,
							previewPositionYs[i] + 14, previewPositionYs[i] + 30);
				} else if (this.hoverFightPreviewFights.get(i).advantage == Fight.Advantage.DEFENDER_ADVANTAGE) {
					TextureUtils.drawTexture(downTexture,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 20,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 4,
							previewPositionYs[i] + 14, previewPositionYs[i] + 30);
				}

				// Attacker skull symbol
				if (hoverFightPreviewFights
						.get(i).damageTakenByAttacker >= hoverFightPreviewFights.get(i).attacker.remainingHealth) {
					TextureUtils.drawTexture(skullTexture, attackerHealthLossAlpha,
							hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 48,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH) - 16,
							previewPositionYs[i] - 2, previewPositionYs[i] + 30);
				}

				// Defender Widths of bars
				float defenderTotalHealthWidth = ((Game.SQUARE_WIDTH + 20) / 2);

				float defenderCurrentHealthWidth = ((Game.SQUARE_WIDTH + 20) / 2)
						* (this.hoverFightPreviewDefender.remainingHealth / this.hoverFightPreviewDefender.totalHealth);

				float defenderPotentialHealthLossWidth = ((Game.SQUARE_WIDTH + 20) / 2)
						* (hoverFightPreviewFights.get(i).damageTakenByDefender
								/ this.hoverFightPreviewDefender.totalHealth);

				float defenderPotentialRemainingHealthWidth = defenderCurrentHealthWidth
						- defenderPotentialHealthLossWidth;

				float defenderCurrentMissingHealthWidth = ((Game.SQUARE_WIDTH + 20) / 2)
						* ((this.hoverFightPreviewDefender.totalHealth - this.hoverFightPreviewDefender.remainingHealth)
								/ this.hoverFightPreviewDefender.totalHealth);

				// Defender Positions of bars
				float defenderTotalHealthX = this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid
						* Game.SQUARE_WIDTH + Game.SQUARE_WIDTH / 2f;

				float defenderCurrentHealthX = defenderTotalHealthX + defenderCurrentMissingHealthWidth;

				float defenderPotentialRemainingHealthX = defenderCurrentHealthX + defenderPotentialHealthLossWidth;

				float defenderPotentialHealthLossX = defenderTotalHealthX + defenderCurrentMissingHealthWidth;

				// Color color = null;
				// if (this.hoverFightPreviewDefender.faction != null) {
				// color = this.hoverFightPreviewDefender.faction.color;
				// } else {
				Color color = new Color(0.25f, 0.25f, 0.25f);
				// }

				// defender draw total health
				QuadUtils.drawQuad(new Color(color.r, color.g, color.b, 0.25f), defenderTotalHealthX,
						defenderTotalHealthX + defenderTotalHealthWidth, previewPositionYs[i],
						previewPositionYs[i] + 28);

				// defender remaining potential health
				QuadUtils.drawQuad(color, defenderPotentialRemainingHealthX,
						defenderPotentialRemainingHealthX + defenderPotentialRemainingHealthWidth, previewPositionYs[i],
						previewPositionYs[i] + 28);

				float defenderHealthLossAlpha = 0f;

				if (hoverFightPreviewFights
						.get(i).damageTakenByDefender >= hoverFightPreviewFights.get(0).defender.remainingHealth) {
					defenderHealthLossAlpha = Game.getTime() % 1000f;
					if (defenderHealthLossAlpha <= 500) {
						defenderHealthLossAlpha = defenderHealthLossAlpha / 500;
					} else {
						defenderHealthLossAlpha = (1000 - defenderHealthLossAlpha) / 500;
					}

				} else {
					defenderHealthLossAlpha = Game.getTime() % 2000f;
					if (defenderHealthLossAlpha <= 1000) {
						defenderHealthLossAlpha = defenderHealthLossAlpha / 1000;
					} else {
						defenderHealthLossAlpha = (2000 - defenderHealthLossAlpha) / 1000;
					}
				}

				// defender potential health loss
				QuadUtils.drawQuad(new Color(color.r, color.g, color.b, defenderHealthLossAlpha),
						defenderPotentialHealthLossX, defenderPotentialHealthLossX + defenderPotentialHealthLossWidth,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// defender line 1
				QuadUtils.drawQuad(Color.WHITE, defenderPotentialHealthLossX, defenderPotentialHealthLossX + 1,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// defender line 2
				QuadUtils.drawQuad(Color.WHITE,
						defenderPotentialRemainingHealthX + defenderPotentialRemainingHealthWidth,
						defenderPotentialRemainingHealthX + defenderPotentialRemainingHealthWidth + 1,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// defender line 3
				QuadUtils.drawQuad(Color.WHITE, defenderPotentialHealthLossX + defenderPotentialHealthLossWidth,
						defenderPotentialHealthLossX + defenderPotentialHealthLossWidth + 1, previewPositionYs[i],
						previewPositionYs[i] + 28);

				if (this.hoverFightPreviewFights.get(i).defenderWeapon == null) {

				} else {

					// defender hit chance
					TextUtils.printTextWithImages(
							new Object[] { this.hoverFightPreviewFights.get(i).chanceOfHittingAttacker + "%" },
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ (Game.SQUARE_WIDTH)
									- Game.font.getWidth(
											this.hoverFightPreviewFights.get(i).chanceOfHittingAttacker + "%"),
							previewPositionYs[i], Integer.MAX_VALUE, true);

					// defender damage
					String defenderDamageString = FormattingUtils.formatFloatRemoveUnneccessaryDigits(
							this.hoverFightPreviewFights.get(i).damageTakenByAttacker) + "�"
							+ FormattingUtils.formatFloatRemoveUnneccessaryDigits(
									this.hoverFightPreviewFights.get(i).damageTakenByAttackerMultiplier);
					TextUtils.printTextWithImages(new Object[] { defenderDamageString },
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ (Game.SQUARE_WIDTH) - Game.font.getWidth(defenderDamageString),
							previewPositionYs[i] + 14, Integer.MAX_VALUE, true);

					// defender weapon
					if (this.hoverFightPreviewFights.get(i).defenderWeapon != null) {
						TextureUtils.drawTexture(this.hoverFightPreviewFights.get(i).defenderWeapon.imageTexture,
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH),
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH) + 32,
								previewPositionYs[i] - 2, previewPositionYs[i] + 30);
					}

					// attacker advantage/disadvantage
					if (this.hoverFightPreviewFights.get(i).advantage == Fight.Advantage.DEFENDER_ADVANTAGE) {
						TextureUtils.drawTexture(upTexture,
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH) + 12,
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH) + 28,
								previewPositionYs[i] + 14, previewPositionYs[i] + 30);
					} else if (this.hoverFightPreviewFights.get(i).advantage == Fight.Advantage.ATTACKER_ADVANTAGE) {
						TextureUtils.drawTexture(downTexture,
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH) + 12,
								this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
										+ (Game.SQUARE_WIDTH) + 28,
								previewPositionYs[i] + 14, previewPositionYs[i] + 30);
					}
				}

				// Defender skull symbol
				if (hoverFightPreviewFights
						.get(i).damageTakenByDefender >= hoverFightPreviewFights.get(i).defender.remainingHealth) {
					TextureUtils.drawTexture(skullTexture, defenderHealthLossAlpha,
							hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ (Game.SQUARE_WIDTH) + 16,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ (Game.SQUARE_WIDTH) + 48,
							previewPositionYs[i] - 2, previewPositionYs[i] + 30);
				}

				// line down the middle
				float linePositionX = hoverFightPreviewPositionXInPixels + Game.SQUARE_WIDTH / 2f - 0.5f;
				QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 1.0f), linePositionX, linePositionX + 1,
						previewPositionYs[i], previewPositionYs[i] + 28);

				// X symbol because it's out of range
				if (hoverFightPreviewFights.get(i).reachable == false) {
					TextureUtils.drawTexture(this.xTexture,
							hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ Game.SQUARE_WIDTH / 2 - 16,
							this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
									+ Game.SQUARE_WIDTH / 2 + 16,
							previewPositionYs[i] - 2, previewPositionYs[i] + 30);
				}

			}

			// fight symbol
			TextureUtils.drawTexture(fightTexture,
					this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
							+ Game.SQUARE_WIDTH / 2f - 16,
					this.hoverFightPreviewDefender.squareGameObjectIsOn.xInGrid * (Game.SQUARE_WIDTH)
							+ Game.SQUARE_WIDTH / 2f + 16,
					this.hoverFightPreviewDefender.squareGameObjectIsOn.yInGrid * (Game.SQUARE_HEIGHT) - 40,
					this.hoverFightPreviewDefender.squareGameObjectIsOn.yInGrid * (Game.SQUARE_HEIGHT) - 8);

			// TextUtils.printTable(tableContents,
			// hoverFightPreviewPositionXInPixels,
			// hoverFightPreviewPositionYInPixels, 20f, level);

		}

		// actor buttons
		if (Game.level.activeActor == this && this.faction == Game.level.factions.get(0)) {

			// animationX
			buttonsAnimateCurrentTime += Game.delta;
			float animationEndPointX = this.squareGameObjectIsOn.xInGrid * Game.SQUARE_WIDTH + Game.SQUARE_WIDTH;
			float animationStartPointX = animationEndPointX - (buttons.size() * 50);
			float animationDistanceX = animationEndPointX - animationStartPointX;
			if (buttonsAnimateCurrentTime > buttonsAnimateMaxTime) {
				buttonsAnimateCurrentTime = buttonsAnimateMaxTime;
			}
			float animationOffsetX = (buttonsAnimateCurrentTime / buttonsAnimateMaxTime) * animationDistanceX;
			float buttonsX = animationStartPointX + animationOffsetX;
			for (int i = 0; i < buttons.size(); i++) {
				buttons.get(i).x = buttonsX + 50 * i;
				buttons.get(i).y = this.squareGameObjectIsOn.yInGrid * Game.SQUARE_HEIGHT;
				buttons.get(i).drawWithinBounds(animationEndPointX, animationEndPointX + (buttons.size() * 50),
						this.squareGameObjectIsOn.yInGrid * Game.SQUARE_HEIGHT,
						this.squareGameObjectIsOn.yInGrid * Game.SQUARE_HEIGHT + 50);
			}

			if (showWeaponButtons) {
				for (int i = 0; i < weaponButtons.size(); i++) {
					weaponButtons.get(i).x = attackButton.x;
					weaponButtons.get(i).y = attackButton.y + attackButton.height + attackButton.height * i;
					weaponButtons.get(i).draw();
				}
			}

		}
	}

	@Override
	public void drawStaticUI() {

		conversation.drawStaticUI();

	}

	public Vector<Float> calculateIdealDistanceFrom(GameObject target) {

		Vector<Fight> fights = new Vector<Fight>();
		for (Weapon weapon : getWeaponsInInventory()) {
			for (float range = weapon.minRange; range <= weapon.maxRange; range++) {
				Fight fight = new Fight(this, weapon, target, target.bestCounterWeapon(this, weapon, range), range);
				fights.add(fight);
			}
		}

		fights.sort(new Fight.FightComparator());

		Vector<Float> idealDistances = new Vector<Float>();

		for (Fight fight : fights) {
			idealDistances.add(fight.range);
		}

		return idealDistances;
	}

	public void showHoverFightPreview(GameObject defender) {
		this.showHoverFightPreview = true;
		hoverFightPreviewDefender = defender;
		hoverFightPreviewFights.clear();
		for (Weapon weapon : getWeaponsInInventory()) {
			// if (defender.squareGameObjectIsOn.weaponsThatCanAttack
			// .contains(weapon)) {

			for (float range = weapon.minRange; range <= weapon.maxRange; range++) {
				Fight fight = new Fight(this, weapon, defender, defender.bestCounterWeapon(this, weapon, range), range);
				// if (fight.reachable)
				hoverFightPreviewFights.add(fight);
			}
			// }

		}

	}

	public void hideHoverFightPreview() {
		this.showHoverFightPreview = false;
	}

	public void attackClicked() {
		showWeaponButtons = !showWeaponButtons;
		if (showWeaponButtons == false) {
			equippedWeapon = null;
			equippedWeaponGUID = null;
		}
	}

	public Button getButtonFromMousePosition(float alteredMouseX, float alteredMouseY) {
		for (Button button : this.buttons) {
			if (button.calculateIfPointInBoundsOfButton(alteredMouseX, alteredMouseY))
				return button;
		}

		if (showWeaponButtons) {
			for (Button button : this.weaponButtons) {
				if (button.calculateIfPointInBoundsOfButton(alteredMouseX, alteredMouseY))
					return button;
			}
		}
		return null;
	}

	public void unselected() {
		this.equippedWeapon = null;
		equippedWeaponGUID = null;
		buttonsAnimateCurrentTime = 0;
		this.showWeaponButtons = false;
		Game.level.removeWalkingHighlight();
		Game.level.removeWeaponsThatCanAttackHighlight();
		Game.level.activeActor.hideHoverFightPreview();
	}

	public void weaponButtonClicked(Weapon weapon) {
		this.equippedWeapon = weapon;
		equippedWeaponGUID = null;
	}

	@Override
	public Actor makeCopy(Square square) {

		return new Actor(name, title, actorLevel, (int) totalHealth, strength, dexterity, intelligence, endurance,
				imageTexturePath, square, travelDistance, null, inventory, showInventory, fitsInInventory,
				canContainOtherObjects);
	}

	@Override
	public void update(int delta) {

		// Manage attackers list
		ArrayList<Actor> attackersToRemoveFromList = new ArrayList<Actor>();
		for (Actor actor : attackers) {
			if (actor.remainingHealth <= 0) {
				attackersToRemoveFromList.add(actor);
			}
		}

		for (Actor actor : attackersToRemoveFromList) {
			attackers.remove(actor);
		}
		if (this.remainingHealth > 0) {
			Game.level.activeActor.calculatePathToAllSquares(Game.level.squares);
			this.aiRoutine.update();
		}
	}

	public void lootAll(GameObject gameObject) {
		ArrayList<GameObject> gameObjectsToLoot = (ArrayList<GameObject>) gameObject.inventory.getGameObjects().clone();
		for (GameObject gameObjectToLoot : gameObjectsToLoot) {
			Game.level.logOnScreen(
					new ActivityLog(new Object[] { this, " looted ", gameObjectToLoot, " from ", gameObject }));
			gameObject.inventory.remove(gameObjectToLoot);
			this.inventory.add(gameObjectToLoot);
		}
	}

	public void pickup(GameObject target) {
		Game.level.logOnScreen(new ActivityLog(new Object[] { this, " picked up ", target }));
		target.squareGameObjectIsOn.inventory.remove(target);
		this.inventory.add(target);
	}

	public void sellAllToTarget(Class clazz, GameObject gameObject) {
		ArrayList<GameObject> gameObjectsToSell = (ArrayList<GameObject>) this.inventory.getGameObjects().clone();
		for (GameObject gameObjectToSell : gameObjectsToSell) {
			if (clazz == null || clazz.isInstance(gameObjectToSell)) {
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { this, " sold ", gameObjectToSell, " to ", gameObject }));
				this.inventory.remove(gameObjectToSell);
				gameObject.inventory.add(gameObjectToSell);
			}
		}

	}

	public void giveAllToTarget(Class clazz, GameObject gameObject) {
		ArrayList<GameObject> gameObjectsToSell = (ArrayList<GameObject>) this.inventory.getGameObjects().clone();
		for (GameObject gameObjectToSell : gameObjectsToSell) {
			if (clazz == null || clazz.isInstance(gameObjectToSell)) {
				Game.level.logOnScreen(
						new ActivityLog(new Object[] { this, " gave ", gameObjectToSell, " to ", gameObject }));
				this.inventory.remove(gameObjectToSell);
				gameObject.inventory.add(gameObjectToSell);
			}
		}

	}

	public void addAttacker(Actor actor) {
		if (!this.attackers.contains(actor)) {
			this.attackers.add(actor);
		}
	}

	public void manageAttackerReferences(GameObject gameObject) {

		// Manage attackers
		if (!(gameObject instanceof Actor))
			return;

		Actor attacker = (Actor) gameObject;

		if (this.pack != null && attacker.pack != null) {

			for (int i = 0; i < attacker.pack.size(); i++) {
				this.pack.addAttacker(attacker.pack.getMember(i));
			}

			for (int i = 0; i < this.pack.size(); i++) {
				attacker.pack.addAttacker(this.pack.getMember(i));
			}
			attacker.addAttacker(this);
			this.addAttacker(attacker);

		} else if (this.pack != null && attacker.pack == null) {

			for (int i = 0; i < this.pack.size(); i++) {
				attacker.addAttacker(this.pack.getMember(i));
			}

			this.pack.addAttacker(attacker);
			this.addAttacker(attacker);

		} else if (this.pack == null && attacker.pack != null) {

			for (int i = 0; i < attacker.pack.size(); i++) {
				this.addAttacker(attacker.pack.getMember(i));
			}
			attacker.pack.addAttacker(this);
			attacker.addAttacker(this);

		} else if (this.pack == null && attacker.pack == null) {
			attacker.addAttacker(this);
			this.addAttacker(attacker);

		}

	}

	public void manageAttackerReferencesForNearbyAllies(GameObject attacker) {
		for (Actor ally : this.faction.actors) {
			if (ally != this && (this.straightLineDistanceTo(ally.squareGameObjectIsOn) < 10
					|| ally.straightLineDistanceTo(attacker.squareGameObjectIsOn) < 10)) {
				ally.manageAttackerReferences(attacker);
			}
		}
	}

	public void manageAttackerReferencesForNearbyEnemies(GameObject attackerGameObject) {
		if (attackerGameObject instanceof Actor) {
			Actor attacker = (Actor) attackerGameObject;
			for (Actor enemy : attacker.faction.actors) {
				if (enemy != attacker && (this.straightLineDistanceTo(enemy.squareGameObjectIsOn) < 10
						|| enemy.straightLineDistanceTo(attacker.squareGameObjectIsOn) < 10)) {
					enemy.manageAttackerReferences(this);
				}
			}
		}
	}

	@Override
	public Action getDefaultAction(Actor performer) {
		if (performer.attackers.contains(this)) {
			if (Game.level.activeActor != null && Game.level.activeActor.equippedWeapon != null
					&& Game.level.activeActor.equippedWeapon
							.hasRange(Game.level.activeActor.straightLineDistanceTo(this.squareGameObjectIsOn))) {
				return new ActionAttack(performer, this);
			}
		} else {
			return new ActionTalk(performer, this);
		}
		return null;
	}

	public boolean hasAttackers() {
		return attackers.size() > 0;
	}

	public ArrayList<Actor> getAttackers() {
		return attackers;
	}
}
