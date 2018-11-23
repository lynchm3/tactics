package com.marklynch.objects;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.openal.Audio;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.UserInputLevel;
import com.marklynch.level.constructs.Group;
import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.primary.AnimationWait;
import com.marklynch.level.constructs.animation.secondary.AnimationDamageText;
import com.marklynch.level.constructs.animation.secondary.AnimationSecondaryScale;
import com.marklynch.level.constructs.beastiary.BestiaryKnowledge;
import com.marklynch.level.constructs.bounds.structure.StructureRoom;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.effect.EffectBleed;
import com.marklynch.level.constructs.effect.EffectBurning;
import com.marklynch.level.constructs.effect.EffectPoison;
import com.marklynch.level.constructs.effect.EffectWet;
import com.marklynch.level.constructs.enchantment.Enhancement;
import com.marklynch.level.constructs.inventory.Inventory;
import com.marklynch.level.constructs.inventory.InventoryParent;
import com.marklynch.level.constructs.inventory.InventorySquare;
import com.marklynch.level.constructs.journal.Journal;
import com.marklynch.level.constructs.power.PowerTeleportOther;
import com.marklynch.level.conversation.Conversation;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionAttack;
import com.marklynch.objects.actions.ActionBuytemsSelectedInInventory;
import com.marklynch.objects.actions.ActionChangeAppearance;
import com.marklynch.objects.actions.ActionChopping;
import com.marklynch.objects.actions.ActionClose;
import com.marklynch.objects.actions.ActionDie;
import com.marklynch.objects.actions.ActionDigging;
import com.marklynch.objects.actions.ActionDropItemsSelectedInInventory;
import com.marklynch.objects.actions.ActionEatItems;
import com.marklynch.objects.actions.ActionEatItemsSelectedInInventory;
import com.marklynch.objects.actions.ActionEquip;
import com.marklynch.objects.actions.ActionFillContainersInInventory;
import com.marklynch.objects.actions.ActionFillSpecificContainer;
import com.marklynch.objects.actions.ActionFishingStart;
import com.marklynch.objects.actions.ActionFollow;
import com.marklynch.objects.actions.ActionGetIn;
import com.marklynch.objects.actions.ActionGiveItemsSelectedInInventory;
import com.marklynch.objects.actions.ActionHide;
import com.marklynch.objects.actions.ActionIgnite;
import com.marklynch.objects.actions.ActionInitiateTrade;
import com.marklynch.objects.actions.ActionInspect;
import com.marklynch.objects.actions.ActionLift;
import com.marklynch.objects.actions.ActionLock;
import com.marklynch.objects.actions.ActionMining;
import com.marklynch.objects.actions.ActionMove;
import com.marklynch.objects.actions.ActionOpen;
import com.marklynch.objects.actions.ActionOpenInventoryToDropItems;
import com.marklynch.objects.actions.ActionOpenInventoryToGiveItems;
import com.marklynch.objects.actions.ActionOpenInventoryToThrowItems;
import com.marklynch.objects.actions.ActionOpenOtherInventory;
import com.marklynch.objects.actions.ActionPeek;
import com.marklynch.objects.actions.ActionPourContainerInInventory;
import com.marklynch.objects.actions.ActionPourSpecificItem;
import com.marklynch.objects.actions.ActionRead;
import com.marklynch.objects.actions.ActionRemoveMapMarker;
import com.marklynch.objects.actions.ActionRename;
import com.marklynch.objects.actions.ActionRing;
import com.marklynch.objects.actions.ActionSearch;
import com.marklynch.objects.actions.ActionSellItemsSelectedInInventory;
import com.marklynch.objects.actions.ActionSkin;
import com.marklynch.objects.actions.ActionSmash;
import com.marklynch.objects.actions.ActionStarSpecificItem;
import com.marklynch.objects.actions.ActionStopHiding;
import com.marklynch.objects.actions.ActionStopPeeking;
import com.marklynch.objects.actions.ActionTakeItems;
import com.marklynch.objects.actions.ActionTakeItemsSelectedInInventory;
import com.marklynch.objects.actions.ActionThrowItem;
import com.marklynch.objects.actions.ActionTrackMapMarker;
import com.marklynch.objects.actions.ActionUnequip;
import com.marklynch.objects.actions.ActionUnlock;
import com.marklynch.objects.actions.ActionUntrackMapMarker;
import com.marklynch.objects.actions.ActionUse;
import com.marklynch.objects.actions.ActionUsePower;
import com.marklynch.objects.actions.ActionViewInfo;
import com.marklynch.objects.actions.ActionableInInventory;
import com.marklynch.objects.actions.ActionableInWorld;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.tools.Bell;
import com.marklynch.objects.tools.ContainerForLiquids;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Fish;
import com.marklynch.objects.units.NonHuman;
import com.marklynch.objects.weapons.BodyArmor;
import com.marklynch.objects.weapons.Helmet;
import com.marklynch.objects.weapons.LegArmor;
import com.marklynch.objects.weapons.Weapon;
import com.marklynch.ui.ActivityLog;
import com.marklynch.utils.ArrayUtils;
import com.marklynch.utils.Color;
import com.marklynch.utils.LineUtils;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.Texture;
import com.marklynch.utils.TextureUtils;

public class GameObject implements ActionableInWorld, ActionableInInventory, Comparable, InventoryParent, DamageDealer {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();

	public final static String[] editableAttributes = { "name", "imageTexture", "totalHealth", "remainingHealth",
			"owner", "inventory", "showInventory", "canShareSquare", "fitsInInventory", "canContainOtherObjects" };
	public String guid = UUID.randomUUID().toString();

	public HashMap<HIGH_LEVEL_STATS, Stat> highLevelStats = new HashMap<HIGH_LEVEL_STATS, Stat>();
	// public HashMap<DEFENSIVE_STATS, Stat> highLevelStats = new
	// HashMap<HIGH_LEVEL_STATS, Stat>();

	// public enum HIGH_LEVEL_STATS {"PIZZA"}
	// public enum DEFENSIVE_STATS;
	// public enum STATS;

	// Template id
	public int templateId;
	public String name = "";
	public int totalHealth = 0;
	public String imageTexturePath = null;
	public transient Square squareGameObjectIsOn = null;
	public transient Square lastSquare = null;
	public transient InventorySquare inventorySquare = null;
	public Inventory inventory = new Inventory();
	public boolean showInventoryInGroundDisplay = false;;
	public boolean canShareSquare = true;
	public boolean fitsInInventory = true;
	public boolean canContainOtherObjects = false;
	public boolean blocksLineOfSight = false;
	public boolean persistsWhenCantBeSeen = false;
	public boolean attackable = true;
	public boolean moveable = true;
	public boolean canBePickedUp = true;
	public boolean decorative = false;
	public boolean floats = false;
	public boolean isFloorObject = false;

	public int value = 1;
	public int turnAcquired = 1;
	public float widthRatio = 1;
	public float heightRatio = 1;

	public float drawOffsetRatioX = 0;
	public float drawOffsetRatioY = 0;
	public float soundWhenHit = 1;
	public float soundWhenHitting = 1;
	public float soundDampening = 1;
	public Color light;
	public float lightHandleX;
	public float lightHandlY;
	public boolean stackable = false;

	public float weight;

	public transient Texture imageTexture = null;
	public ArrayList<Effect> activeEffectsOnGameObject = new ArrayList<Effect>();
	public ArrayList<Action> actionsPerformedThisTurn = new ArrayList<Action>();

	// attributes
	public int remainingHealth = 1;
	boolean favourite = false;
	public transient boolean hasAttackedThisTurn = false;

	// images
	public static transient Texture powTexture = null;
	public static Texture dustCloudTexture = null;
	public static transient Texture vsTexture = null;
	public static transient Texture fightTexture = null;
	public static transient Texture skullTexture = null;
	public static transient Texture xTexture = null;
	public static transient Texture upTexture = null;
	public static transient Texture downTexture = null;
	public static transient Texture leftTexture = null;
	public static transient Texture rightTexture = null;
	// public static transient Texture armTexture = null;
	public static transient Texture grassNormalTexture = null;
	public static transient Texture skipNormalTexture = null;

	// sounds
	public static transient Audio screamAudio = null;

	// POW
	// public transient boolean showPow = false;

	// Placement in inventory
	public transient Inventory inventoryThatHoldsThisObject;

	// Quest
	public transient Quest quest;

	// Owner
	public transient Actor owner;

	public float height;
	public float width;
	public float halfHeight;
	public float halfWidth;
	// public float drawOffsetX;
	// public float drawOffsetY;

	public float anchorX, anchorY;

	public boolean backwards = false;

	public boolean hiding = false;
	public HidingPlace hidingPlace = null;

	public transient ArrayList<GameObject> attackers = new ArrayList<GameObject>();

	public transient Group group;

	public Object destroyedBy = null;
	public Action destroyedByAction = null;

	protected Animation primaryAnimation;

	public boolean toSell = false;
	public boolean starred = false;
	public boolean flash = false;

	public float minRange = 1;
	public float maxRange = 1;

	public Enhancement enhancement;
	public int level = 1;

	public int thoughtsOnPlayer = 0;

	public static float healthWidthInPixels = Game.SQUARE_WIDTH / 20;

	public boolean diggable = false;

	public boolean flipYAxisInMirror = true;

	public Actor beingFishedBy = null;
	public boolean fightingFishingRod = false;
	// public boolean beingChopped = false;
	// public boolean beingMined = false;
	// public boolean beingDigged = false;
	// public static float healthHeightInPixels = Game.SQUARE_HEIGHT;

	// public ArrayList<DestructionListener> destructionListeners = new
	// ArrayList<DestructionListener>();

	// Fishing stuff
	public float swimmingChangeX = 0;
	public float swimmingChangeY = 0;

	public ArrayList<Arrow> arrowsEmbeddedInThis = new ArrayList<Arrow>();

	public GameObject fishingTarget;
	public GameObject equipped = null;

	public boolean bigShadow = false;

	public int orderingOnGound = 100; // lower value is background, drawn first.

	public String type = "Object";

	public int lastTurnThisWasMovedByMinecart = -1;

	public GameObject() {

		highLevelStats.put(HIGH_LEVEL_STATS.STRENGTH, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.DEXTERITY, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.ENDURANCE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.INTELLIGENCE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.FRIENDLY_FIRE, new Stat(0));

		highLevelStats.put(HIGH_LEVEL_STATS.SLASH_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.PIERCE_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.FIRE_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.WATER_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.POISON_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLEED_DAMAGE, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.HEALING, new Stat(0));

		highLevelStats.put(HIGH_LEVEL_STATS.SLASH_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLUNT_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.PIERCE_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.FIRE_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.WATER_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.ELECTRICAL_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.POISON_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.BLEED_RES, new Stat(0));
		highLevelStats.put(HIGH_LEVEL_STATS.HEALING_RES, new Stat(0));
	}

	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
	}

	public void init() {

		if (squareGameObjectIsOn != null) {
			this.squareGameObjectIsOn.inventory.add(this);
		}

		loadImages();

		width = Game.SQUARE_WIDTH * widthRatio;
		height = Game.SQUARE_HEIGHT * heightRatio;
		halfWidth = width / 2;
		halfHeight = height / 2;
		randomisePosition();

		inventory = new Inventory();
		inventory.parent = this;
	}

	public void randomisePosition() {
		if (widthRatio < 1f && heightRatio < 1f) {
			float drawOffsetXMax = 1 - width / Game.SQUARE_WIDTH;
			float drawOffsetYMax = 1 - height / Game.SQUARE_WIDTH;
			if (drawOffsetYMax < 0) {
				drawOffsetYMax = 0;
			}
			this.drawOffsetRatioX = (float) (Math.random() * drawOffsetXMax);
			this.drawOffsetRatioY = (/* Math.random() * */ drawOffsetYMax);
		}
	}

	public void postLoad1() {
		inventory.postLoad1();
		// loadImages();
		// paths = new HashMap<Square, Path>();
		if (squareGameObjectIsOn != null) {
			this.squareGameObjectIsOn.inventory.add(this);
		}

		loadImages();
	}

	public void postLoad2() {
		inventory.postLoad2();
	}

	public void loadImages() {
		// this.imageTexture = getGlobalImage(imageTexturePath, true);

	}

	public static void loadStaticImages() {

		powTexture = getGlobalImage("pow.png", false);
		dustCloudTexture = getGlobalImage("dust_cloud.png", false);
		vsTexture = getGlobalImage("vs.png", false);
		fightTexture = getGlobalImage("fight.png", false);
		skullTexture = getGlobalImage("skull.png", false);
		xTexture = getGlobalImage("x.png", false);
		upTexture = getGlobalImage("up.png", false);
		downTexture = getGlobalImage("down.png", false);
		leftTexture = getGlobalImage("left.png", false);
		rightTexture = getGlobalImage("right.png", false);
		// armTexture = getGlobalImage("arm.png", false);
		grassNormalTexture = getGlobalImage("grass_NRM.png", false);
		skipNormalTexture = getGlobalImage("skip_with_shadow_NRM.png", false);
		screamAudio = ResourceUtils.getGlobalSound("scream.wav");
	}

	protected Color flashColor = new Color(255f, 255f, 255f, 0.5f);
	protected Color underWaterColor = new Color(0.1f, 0.1f, 0.1f, 1f);

	protected boolean shouldDraw() {
		// if (this.remainingHealth <= 0)
		// return false;

		if (squareGameObjectIsOn == null)
			return false;

		if (hiding && this != Game.level.player)
			return false;

		if (!Game.fullVisiblity && this != Game.level.player) {

			if (this.squareGameObjectIsOn.visibleToPlayer == false && persistsWhenCantBeSeen == false)
				return false;

			if (!this.squareGameObjectIsOn.seenByPlayer)
				return false;
		}
		return true;
	}

	public boolean draw1() {

		if (!shouldDraw())
			return false;

		if (primaryAnimation != null && primaryAnimation.getCompleted() == false)
			primaryAnimation.draw1();

		int actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels
				+ Game.SQUARE_WIDTH * drawOffsetRatioX);
		int actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels
				+ Game.SQUARE_HEIGHT * drawOffsetRatioY);
		float alpha = 1.0f;

		// TextureUtils.skipNormals = true;

		if (primaryAnimation != null)
			alpha = primaryAnimation.alpha;
		if (!this.squareGameObjectIsOn.visibleToPlayer && this != Game.level.player)
			alpha = 0.5f;

		float boundsX1 = actorPositionXInPixels;
		float boundsY1 = actorPositionYInPixels;
		float boundsX2 = actorPositionXInPixels + width;
		float boundsY2 = actorPositionYInPixels + height;

		if (primaryAnimation != null) {
			actorPositionXInPixels += primaryAnimation.offsetX;
			actorPositionYInPixels += primaryAnimation.offsetY;
			boundsX1 += primaryAnimation.offsetX + primaryAnimation.boundsX1;
			boundsY1 += primaryAnimation.offsetY + primaryAnimation.boundsY1;
			boundsX2 += primaryAnimation.offsetX + primaryAnimation.boundsX2;
			boundsY2 += primaryAnimation.offsetY + primaryAnimation.boundsY2;
		}

		float scaleX = 1;
		float scaleY = 1;
		if (primaryAnimation != null) {
			scaleX = primaryAnimation.scaleX;
			scaleY = primaryAnimation.scaleY;
		}

		// shadow
		if (!isFloorObject && Level.shadowDarkness > 0 && bigShadow
				&& this.squareGameObjectIsOn.structureSquareIsIn == null) {
			drawGameObject((actorPositionXInPixels), (actorPositionYInPixels), Level.shadowDarkness, false, scaleX,
					Level.shadowLength * scaleY, Level.shadowAngle, boundsX1, boundsY1, boundsX2, boundsY2, Color.BLACK,
					false);
		} else if (Level.shadowDarkness > 0 && this.squareGameObjectIsOn.structureSquareIsIn == null) {
			// drawGameObject((actorPositionXInPixels), (actorPositionYInPixels),
			// Level.shadowDarkness, false, 1f, 1f,
			// Level.shadowAngle, boundsX1, boundsY1, boundsX2, boundsY2, Color.BLACK,
			// false);

			// Level.shadowDarkness
			drawGameObject((int) (actorPositionXInPixels + Level.smallShadowOffSetX),
					(int) (actorPositionYInPixels + Level.smallShadowOffSetY), Level.shadowDarkness, false, scaleX,
					Level.shadowLength * scaleY, 0f, boundsX1 + Level.smallShadowOffSetX,
					boundsY1 + Level.smallShadowOffSetY, boundsX2 + Level.smallShadowOffSetX,
					boundsY2 + Level.smallShadowOffSetY, Color.BLACK, false);
		}

		Color color = Level.dayTimeOverlayColor;
		if (this.squareGameObjectIsOn.structureSquareIsIn != null)
			color = StructureRoom.roomColor;
		drawGameObject(actorPositionXInPixels, actorPositionYInPixels, alpha,
				flash || this == Game.gameObjectMouseIsOver, scaleX, scaleY, 0f, boundsX1, boundsY1, boundsX2, boundsY2,
				color, true);
		return true;
	}

	public void drawGameObject(int x, int y, float alpha, boolean highlight, float scaleX, float scaleY,
			float rotationRad, float boundsX1, float boundsY1, float boundsX2, float boundsY2, Color color,
			boolean drawHealthBar) {

		// Draw object
		if (squareGameObjectIsOn != null) {

			Matrix4f view = Game.activeBatch.getViewMatrix();

			if (scaleX != 1 || scaleY != 1) {
				Game.flush();
				view.translate(new Vector2f(x + halfWidth, y + halfHeight));
				view.scale(new Vector3f(scaleX, scaleY, 1f));
				view.translate(new Vector2f(-(x + halfWidth), -(y + halfHeight)));
				Game.activeBatch.updateUniforms();
			}

			Game.flush();
			view.translate(new Vector2f(x + halfWidth, y + height));
			view.rotate(rotationRad, new Vector3f(0f, 0f, 1f));
			view.translate(new Vector2f(-(x + halfWidth), -(y + height)));
			Game.activeBatch.updateUniforms();

			float torsoAngle = 0;
			if (primaryAnimation != null) {
				torsoAngle = primaryAnimation.torsoAngle;
			}

			Game.flush();
			view.translate(new Vector2f(x + halfWidth, y + halfHeight));
			view.rotate(torsoAngle, new Vector3f(0f, 0f, 1f));
			view.translate(new Vector2f(-(x + halfWidth), -(y + halfHeight)));
			Game.activeBatch.updateUniforms();

			for (Arrow arrow : arrowsEmbeddedInThis) {

				float arrowWidth = arrow.width;
				// arrowWidth = -arrowWidth;

				// QuadUtils.drawQuad(Color.RED,
				// this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH *
				// arrow.drawOffsetRatioX,
				// this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT
				// * arrow.drawOffsetRatioY,
				// this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH *
				// arrow.drawOffsetRatioX + 10,
				// this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT
				// * arrow.drawOffsetRatioY + 10);

				if (arrow.backwards) {
					TextureUtils.drawTexture(arrow.textureEmbeddedPoint, alpha,

							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX
									+ arrowWidth,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY
									+ arrow.height,
							color);
				} else {
					TextureUtils.drawTexture(arrow.textureEmbeddedPoint, alpha,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX
									- arrowWidth,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY
									+ arrow.height,
							color);

				}
			}

			TextureUtils.drawTextureWithinBounds(imageTexture, alpha, x, y, x + width, y + height, boundsX1, boundsY1,
					boundsX2, boundsY2, backwards, false, color);

			if (highlight) {
				TextureUtils.drawTexture(imageTexture, 0.5f, x, y, x + width, y + height, 0, 0, 0, 0, backwards, false,
						flashColor, false);
			} else if (squareGameObjectIsOn.inventory.waterBody != null && !(this instanceof Fish)
					&& !(this instanceof WaterBody)) {

				TextureUtils.drawTexture(imageTexture, 0.5f, x, y, x + width, y + height, 0, 0, 0, 0, backwards, false,
						underWaterColor, false);
				TextureUtils.drawTexture(Templates.WATER_BODY.imageTexture, alpha, x, y, x + width, y + height,
						backwards);
			}

			for (Arrow arrow : arrowsEmbeddedInThis) {

				float arrowWidth = arrow.width;
				// arrowWidth = -arrowWidth;

				// QuadUtils.drawQuad(Color.RED,
				// this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH *
				// arrow.drawOffsetRatioX,
				// this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT
				// * arrow.drawOffsetRatioY,
				// this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH *
				// arrow.drawOffsetRatioX + 10,
				// this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT
				// * arrow.drawOffsetRatioY + 10);

				if (arrow.backwards) {
					TextureUtils.drawTexture(arrow.textureEmbedded, alpha,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX
									+ arrowWidth,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY
									+ arrow.height,
							color);

				} else {
					TextureUtils.drawTexture(arrow.textureEmbedded, alpha,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX
									- arrowWidth,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY,
							this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * arrow.drawOffsetRatioX,
							this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * arrow.drawOffsetRatioY
									+ arrow.height,
							color);

				}
			}

			if (drawHealthBar && remainingHealth != totalHealth) {
				// draw sidebar on square
				float healthPercentage = ((float) remainingHealth) / ((float) totalHealth);
				float healthBarHeightInPixels = height * healthPercentage;
				float healthXInPixels = this.squareGameObjectIsOn.xInGridPixels;
				float healthYInPixels = this.squareGameObjectIsOn.yInGridPixels;
				if (primaryAnimation != null) {
					healthXInPixels += primaryAnimation.offsetX;
					healthYInPixels += primaryAnimation.offsetY;
				}

				Color healthColor = Color.YELLOW;
				if (thoughtsOnPlayer > 50) {
					healthColor = Color.GREEN;
				} else if (thoughtsOnPlayer < -50) {
					healthColor = Color.RED;
				}

				// White bit under health bar
				QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f), x + 1, y + 1, x + healthWidthInPixels - 1,
						y + height - 1);

				// Colored health bar
				QuadUtils.drawQuad(healthColor, x + 1, y + 1, x + healthWidthInPixels - 1,
						y + healthBarHeightInPixels - 1);
			}

			Game.flush();
			view.translate(new Vector2f(x + halfWidth, y + halfHeight));
			view.rotate(-torsoAngle, new Vector3f(0f, 0f, 1f));
			view.translate(new Vector2f(-(x + halfWidth), -(y + halfHeight)));
			Game.flush();

			Game.flush();
			view.translate(new Vector2f(x + halfWidth, y + height));
			view.rotate(-(rotationRad), new Vector3f(0f, 0f, 1f));
			view.translate(new Vector2f(-(x + halfWidth), -(y + height)));
			Game.activeBatch.updateUniforms();

			if (scaleX != 1 || scaleY != 1) {
				Game.flush();
				// Matrix4f view = Game.activeBatch.getViewMatrix();
				view.translate(new Vector2f(x + halfWidth, y + halfHeight));
				view.scale(new Vector3f(1f / scaleX, 1f / scaleY, 1f));
				view.translate(new Vector2f(-(x + halfWidth), -(y + halfHeight)));
				Game.activeBatch.updateUniforms();
			}
		}

	}

	private void drawHighlight() {

	}

	public void draw2() {
		for (Effect effect : activeEffectsOnGameObject) {
			effect.draw2();
		}

		if (primaryAnimation != null && primaryAnimation.getCompleted() == false)
			primaryAnimation.draw2();
	}

	public void draw3() {

		// if (this.remainingHealth <= 0)
		// return;
		if (squareGameObjectIsOn == null)
			return;
		if (hiding)
			return;

		if (!Game.fullVisiblity) {

			if (this.squareGameObjectIsOn.visibleToPlayer == false && persistsWhenCantBeSeen == false)
				return;

			if (!this.squareGameObjectIsOn.seenByPlayer)
				return;
		}

		// if (primaryAnimation != null && primaryAnimation.completed == false)
		// primaryAnimation.draw1();

		if (primaryAnimation != null && primaryAnimation.getCompleted() == false)
			primaryAnimation.draw3();

		// water stuff... i dunno

		if (squareGameObjectIsOn.inventory.waterBody == null)
			return;
		if (!floats)
			return;
		// Draw object
		if (squareGameObjectIsOn != null) {
			int actorPositionXInPixels = 0;
			int actorPositionYInPixels = 0;
			if (primaryAnimation != null) {
				actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels
						+ Game.SQUARE_WIDTH * drawOffsetRatioX + primaryAnimation.offsetX);
				actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels
						+ Game.SQUARE_HEIGHT * drawOffsetRatioY + primaryAnimation.offsetY);
			} else {
				actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels
						+ Game.SQUARE_WIDTH * drawOffsetRatioX);
				actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels
						+ Game.SQUARE_HEIGHT * drawOffsetRatioY);

			}

			float alpha = 1.0f;
			if (primaryAnimation != null)
				alpha = primaryAnimation.alpha;

			float boundsX1 = actorPositionXInPixels;
			float boundsY1 = actorPositionYInPixels;
			float boundsX2 = (boundsX1 + width);
			float boundsY2 = (boundsY1 + halfHeight);

			// GL11.glTexParameteri(target, pname, param);
			TextureUtils.drawTextureWithinBounds(imageTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
					actorPositionXInPixels + width, actorPositionYInPixels + height, boundsX1, boundsY1, boundsX2,
					boundsY2, backwards, false, TextureUtils.neutralColor);

			Color color1 = new Color(1, 144, 255);
			Color color2 = new Color(9, 114, 255);
			Color color3 = new Color(18, 111, 236);

			LineUtils.drawLine(color1, boundsX1, boundsY2, boundsX2, boundsY2, 1);
			LineUtils.drawLine(color2, boundsX1, boundsY2 + 2, boundsX2, boundsY2 + 1, 1);
			LineUtils.drawLine(color3, boundsX1, boundsY2 + 1, boundsX2, boundsY2 + 2, 1);

			// TextureUtils.drawTextureWithinBounds(gameObject.imageTexture,
			// alpha, actorPositionXInPixels,
			// actorPositionYInPixels, actorPositionXInPixels +
			// gameObject.width,
			// actorPositionYInPixels + gameObject.height, boundsX1, boundsY1,
			// boundsX2, boundsY2, false,
			// gameObject.flipYAxisInMirror);

			// if (flash || this == Game.gameObjectMouseIsOver) {
			// TextureUtils.drawTexture(imageTexture, 0.5f, actorPositionXInPixels,
			// actorPositionYInPixels,
			// actorPositionXInPixels + width, actorPositionYInPixels + height, 0, 0, 0, 0,
			// backwards, false,
			// flashColor, false);
			// }
		}

	}

	public void drawUI() {
	}

	public void drawStaticUI() {

		if (primaryAnimation != null && primaryAnimation.getCompleted() == false)
			primaryAnimation.drawStaticUI();
	}

	protected boolean died = false;

	public boolean checkIfDestroyed(Object attacker, Action action) {

		if (!died && remainingHealth <= 0) {

			died = true;
			destroyedBy = attacker;
			destroyedByAction = action;
			this.canShareSquare = true;
			this.blocksLineOfSight = false;

			soundDampening = 1;
			this.activeEffectsOnGameObject.clear();

			// Unequip destroyed item
			if (inventoryThatHoldsThisObject != null && inventoryThatHoldsThisObject.parent instanceof Actor) {

				// GameObject holder = (GameObject)
				// inventorySquareGameObjectIsOn.inventoryThisBelongsTo.parent;

				Actor actor = (Actor) inventoryThatHoldsThisObject.parent;
				if (actor.equipped == this) {
					if (actor.inventory.contains(actor.equippedBeforePickingUpObject)) {
						actor.equip(actor.equippedBeforePickingUpObject);
					} else if (actor.inventory.containsDuplicateOf(this)) {
						actor.equip(actor.inventory.getDuplicateOf(this));
					} else {
						actor.equip(null);
					}
					actor.equippedBeforePickingUpObject = null;
				}
				if (actor.helmet == this)
					actor.helmet = null;
				if (actor.bodyArmor == this)
					actor.bodyArmor = null;
				if (actor.legArmor == this)
					actor.legArmor = null;

			}

			// if (primaryAnimation != null) {
			// Level.blockingAnimations.remove(primaryAnimation);
			// }
			//
			// for (Animation animation : secondaryAnimations) {
			// Level.blockingAnimations.remove(animation);
			// }

			// for (DestructionListener destructionListener :
			// destructionListeners) {
			// destructionListener.onDestroy();
			// }

			new ActionDie(this, squareGameObjectIsOn).perform();

			return true;
		}
		return false;
	}

	public ArrayList<Square> getAllSquaresAtDistance(int distance) {
		ArrayList<Square> squares = new ArrayList<Square>();
		if (distance == 0) {
			squares.add(this.squareGameObjectIsOn);
			return squares;
		}

		boolean xGoingUp = true;
		boolean yGoingUp = true;
		for (int i = 0, x = -distance, y = 0; i < distance * 4; i++) {
			if (ArrayUtils.inBounds(Game.level.squares, this.squareGameObjectIsOn.xInGrid + x,
					this.squareGameObjectIsOn.yInGrid + y)) {
				squares.add(Game.level.squares[this.squareGameObjectIsOn.xInGrid + x][this.squareGameObjectIsOn.yInGrid
						+ y]);
			}

			if (xGoingUp) {
				if (x == distance) {
					xGoingUp = false;
					x--;
				} else {
					x++;
				}
			} else {
				if (x == -distance) {
					xGoingUp = true;
					x++;
				} else {
					x--;
				}
			}

			if (yGoingUp) {
				if (y == distance) {
					yGoingUp = false;
					y--;
				} else {
					y++;
				}
			} else {
				if (y == -distance) {
					yGoingUp = true;
					y++;
				} else {
					y--;
				}
			}

		}
		return squares;
	}

	public ArrayList<Square> getAllSquaresWithinDistance(int minDistance, int maxDistance) {
		return getAllSquaresWithinDistance(minDistance, maxDistance, this.squareGameObjectIsOn);
	}

	public static ArrayList<Square> getAllSquaresWithinDistance(int minDistance, int maxDistance, Square squareFrom) {
		ArrayList<Square> squares = new ArrayList<Square>();

		for (int distance = minDistance; distance <= maxDistance; distance++) {

			if (distance == 0)

			{
				squares.add(squareFrom);
				continue;
			}

			boolean xGoingUp = true;
			boolean yGoingUp = true;
			for (int i = 0, x = -distance, y = 0; i < distance * 4; i++) {
				if (ArrayUtils.inBounds(Game.level.squares, squareFrom.xInGrid + x, squareFrom.yInGrid + y)) {
					squares.add(Game.level.squares[squareFrom.xInGrid + x][squareFrom.yInGrid + y]);
				}

				if (xGoingUp) {
					if (x == distance) {
						xGoingUp = false;
						x--;
					} else {
						x++;
					}
				} else {
					if (x == -distance) {
						xGoingUp = true;
						x++;
					} else {
						x--;
					}
				}

				if (yGoingUp) {
					if (y == distance) {
						yGoingUp = false;
						y--;
					} else {
						y++;
					}
				} else {
					if (y == -distance) {
						yGoingUp = true;
						y++;
					} else {
						y--;
					}
				}

			}
		}

		return squares;
	}

	public ArrayList<Point> getAllCoordinatesAtDistanceFromSquare(int distance) {
		return getAllCoordinatesAtDistanceFromSquare(distance, this.squareGameObjectIsOn);
	}

	public ArrayList<Point> getAllCoordinatesAtDistanceFromSquare(int distance, Square square) {
		ArrayList<Point> coordinates = new ArrayList<Point>();
		if (distance == 0) {
			coordinates.add(new Point(square.xInGrid, square.yInGrid));
			return coordinates;
		}

		boolean xGoingUp = true;
		boolean yGoingUp = true;
		for (int i = 0, x = -distance, y = 0; i < distance * 4; i++) {
			coordinates.add(new Point(square.xInGrid + x, square.yInGrid + y));

			if (xGoingUp) {
				if (x == distance) {
					xGoingUp = false;
					x--;
				} else {
					x++;
				}
			} else {
				if (x == -distance) {
					xGoingUp = true;
					x++;
				} else {
					x--;
				}
			}

			if (yGoingUp) {
				if (y == distance) {
					yGoingUp = false;
					y--;
				} else {
					y++;
				}
			} else {
				if (y == -distance) {
					yGoingUp = true;
					y++;
				} else {
					y--;
				}
			}

		}
		return coordinates;
	}

	public void showPow() {
		showPow(powTexture, 50, 200);
	}

	public void showPow(Texture texture, float scaleDuration, float stayDuration) {
		Level.addSecondaryAnimation(
				new AnimationSecondaryScale(this, 0f, 1f, scaleDuration, stayDuration, texture, null));
	}

	public Weapon bestCounterWeapon(GameObject attacker, Weapon attackerWeapon, float range) {

		if (inventory == null)
			return null;

		for (GameObject gameObject : inventory.getGameObjects()) {
			if (gameObject instanceof Weapon) {
				Weapon weapon = (Weapon) gameObject;
				if (range >= weapon.getEffectiveMinRange() && range <= weapon.getEffectiveMaxRange()) {
					return weapon;
				}
			}
		}
		return null;
	}

	public GameObject makeCopy(Square square, Actor owner) {
		GameObject gameObject = new GameObject();
		setInstances(gameObject);
		setAttributesForCopy(gameObject, square, owner);
		return gameObject;
	}

	public ArrayList<Weapon> getWeaponsInInventory() {
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		for (GameObject gameObject : inventory.getGameObjects()) {
			if (gameObject instanceof Weapon) {
				weapons.add((Weapon) gameObject);
			}
		}
		return weapons;
	}

	public void update(int delta) {

		if (!(this instanceof Actor))
			clearActions();

		if (this.remainingHealth > 0) {
			activateEffects();
		}

		for (GameObject gameObjectInInventory : this.inventory.getGameObjects()) {
			if (!(gameObjectInInventory instanceof Actor))
				gameObjectInInventory.update(delta);
		}
	}

	// public void updateRealtime(int delta) {
	//
	//
	// }

	public float getCenterX() {
		return squareGameObjectIsOn.xInGridPixels + Game.HALF_SQUARE_WIDTH;
	}

	public float getCenterY() {
		return squareGameObjectIsOn.yInGridPixels + Game.HALF_SQUARE_HEIGHT;
	}

	@Override
	public Action getDefaultActionPerformedOnThisInWorld(Actor performer) {
		if (this instanceof Discoverable) {
			Discoverable discoverable = (Discoverable) this;
			if (!discoverable.discovered)
				return null;
		}

		if (isFloorObject) {
			return new ActionMove(performer, this.squareGameObjectIsOn, true);
		}

		// Water Source
		if (!(this instanceof WaterBody) && this.squareGameObjectIsOn != null
				&& this.squareGameObjectIsOn.inventory.waterBody != null) {
			return new ActionFishingStart(performer, this);
		}

		if (diggable) {
			Action action = new ActionDigging(performer, this);
			return action;
		}

		if (this instanceof Vein) {
			Action action = new ActionMining(performer, (Vein) this);
			return action;
		}

		if (this instanceof Stump || this instanceof Tree) {
			return new ActionChopping(performer, this);
		}

		if (this instanceof Switch) {
			Switch zwitch = (Switch) this;
			return new ActionUse(performer, zwitch, zwitch.actionName, zwitch.actionVerb, zwitch.requirementsToMeet);
		}

		if (this instanceof MineCart) {
			return new ActionGetIn(performer, this);
		}

		if (this instanceof Door) {
			if (((Door) this).open) {
				return new ActionMove(performer, this.squareGameObjectIsOn, true);
			} else if (!(this instanceof RemoteDoor)) {
				return new ActionOpen(performer, ((Door) this));
			}
		}

		if (/* (this instanceof Openable) && */this.canContainOtherObjects && !(this instanceof Actor)) {
			return new ActionOpenOtherInventory(performer, this);
		}

		if (this.fitsInInventory && !decorative) {
			return new ActionTakeItems(performer, this.squareGameObjectIsOn, this);
		}

		return null;
	}

	@Override
	public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {

		if (this instanceof Discoverable) {
			Discoverable discoverable = (Discoverable) this;
			if (!discoverable.discovered)
				return null;
		}

		if (isFloorObject) {
			return null;
		}

		if (diggable) {
			return new ActionDigging(performer, this);
		}

		if (this instanceof Vein) {
			Action action = new ActionMining(performer, (Vein) this);
			return action;
		}

		if (this instanceof Stump || this instanceof Tree || this.templateId == Templates.TREE_CONTAINER.templateId
				|| this.templateId == Templates.TREE_READABLE.templateId) {
			return new ActionChopping(performer, this);
		}

		// Pressure Plate
		if (this instanceof PressurePlate || this instanceof PressurePlateRequiringSpecificItem) {
			return new ActionOpenInventoryToDropItems(performer, this.squareGameObjectIsOn);
		}

		if (this instanceof Door && !(this instanceof RemoteDoor)) {
			if (((Door) this).open) {
				return new ActionClose(performer, ((Door) this));
			}
		}

		// if ((this instanceof Openable) && this.canContainOtherObjects &&
		// !(this instanceof Actor)) {
		// return new ActionOpenOtherInventory(performer, this);
		// }

		if (this.fitsInInventory && !decorative) {
			return new ActionTakeItems(performer, this.squareGameObjectIsOn, this);
		}

		return null;
	}

	@Override
	public ArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		ArrayList<Action> actions = new ArrayList<Action>();

		// if (this.remainingHealth <= 0)
		// return actions;

		if (isFloorObject) {
			return actions;
		}

		if (this instanceof Discoverable) {
			Discoverable discoverable = (Discoverable) this;
			if (!discoverable.discovered)
				return actions;
		}

		// Inspectable
		if (this instanceof Inspectable) {
			actions.add(new ActionInspect(performer, this));
			return actions;
		}

		// Map Marker
		if (this instanceof MapMarker) {
			MapMarker mapMarker = (MapMarker) this;

			actions.add(new ActionInspect(performer, mapMarker));
			if (Journal.markersToTrack.contains(mapMarker)) {
				actions.add(new ActionUntrackMapMarker(mapMarker));
			} else {
				actions.add(new ActionTrackMapMarker(mapMarker));
			}
			actions.add(new ActionRename(mapMarker));
			actions.add(new ActionChangeAppearance(mapMarker));
			actions.add(new ActionRemoveMapMarker(mapMarker));
			return actions;
		}

		// Water Source
		if (this instanceof WaterSource) {
			actions.add(new ActionFillContainersInInventory(performer, (WaterSource) this));
		}

		// Water Body
		if (!(this instanceof WaterBody) && this.squareGameObjectIsOn != null
				&& this.squareGameObjectIsOn.inventory.waterBody != null) {
			actions.add(new ActionFishingStart(performer, this));
		}

		// Skinnable
		if (this instanceof Carcass) {
			actions.add(new ActionSkin(performer, this));
		}

		// Readable
		if (this instanceof Readable) {
			actions.add(new ActionRead(performer, (Readable) this));
		}

		// Hiding place
		if (this instanceof HidingPlace) {
			HidingPlace hidingPlace = (HidingPlace) this;
			if (performer.hiding && performer.squareGameObjectIsOn == this.squareGameObjectIsOn) {
				actions.add(new ActionStopHiding(performer, hidingPlace));
			} else {
				actions.add(new ActionHide(performer, hidingPlace));
			}
		}

		// Searchable
		if (this instanceof Searchable) {
			actions.add(new ActionSearch(performer, (Searchable) this));
		}

		if (diggable) {
			actions.add(new ActionDigging(performer, this));
		}

		// Tree and stump
		if (this instanceof Stump || this instanceof Tree || this.templateId == Templates.TREE_CONTAINER.templateId
				|| this.templateId == Templates.TREE_READABLE.templateId) {
			actions.add(new ActionChopping(performer, this));
		}

		// Food / Drink
		if (this instanceof Food || this instanceof Liquid || this instanceof ContainerForLiquids
				|| this instanceof WaterBody) {
			actions.add(new ActionEatItems(performer, this));
		}

		// Switch
		if (this instanceof Switch) {
			Switch zwitch = (Switch) this;
			actions.add(
					new ActionUse(performer, zwitch, zwitch.actionName, zwitch.actionVerb, zwitch.requirementsToMeet));
		}

		// MineCart
		if (this instanceof MineCart) {
			actions.add(new ActionGetIn(performer, this));
		}

		// Vein
		if (this instanceof Vein) {
			actions.add(new ActionMining(performer, (Vein) this));
		}

		// Window
		if (this instanceof Window) {
			actions.add(new ActionSmash(performer, this));
		}

		// Loot
		if (/* (this instanceof Openable) && */ this.canContainOtherObjects && !(this instanceof Actor)) {
			actions.add(new ActionOpenOtherInventory(performer, this));
		}

		// Openable, Chests, Doors
		if (this instanceof Openable && !(this instanceof RemoteDoor)) {
			Openable openable = (Openable) this;

			if (!openable.open && openable.isOpenable) {
				actions.add(new ActionOpen(performer, openable));
			}

			if (openable.open && openable.isOpenable)
				actions.add(new ActionClose(performer, openable));

			if (openable.locked && openable.lockable)
				actions.add(new ActionUnlock(performer, openable));

			if (!openable.locked && openable.lockable)
				actions.add(new ActionLock(performer, openable));

			if (this instanceof Door) {
				if (!openable.open) {
					if (Game.level.player.peekingThrough == this)
						actions.add(new ActionStopPeeking(performer));
					else
						actions.add(new ActionPeek(performer, this));
				}
			}
		}

		// public boolean showInventory;
		// public boolean canShareSquare;
		if (!decorative && fitsInInventory) {
			actions.add(new ActionTakeItems(performer, this.squareGameObjectIsOn, this));
		}

		if (!decorative && canBePickedUp && !fitsInInventory) {
			actions.add(new ActionLift(performer, this));
		}

		if (!decorative && canBePickedUp && fitsInInventory) {
			actions.add(new ActionEquip(performer, this));
		}

		// Here put view loot

		// if (Game.level.activeActor != null &&
		// Game.level.activeActor.equippedWeapon != null
		// && Game.level.activeActor.equippedWeapon
		// .hasRange(Game.level.activeActor.straightLineDistanceTo(this.squareGameObjectIsOn)))
		// {
		if (!decorative && this != Game.level.player && attackable)
			actions.add(new ActionAttack(performer, this));

		if (!decorative && this != Game.level.player && attackable && !(this instanceof Wall)
				&& !(this instanceof Door))
			actions.add(new ActionUsePower(Level.player, this, this.squareGameObjectIsOn,
					new PowerTeleportOther(Level.player)));
		// actions.add(new ActionSelectTeleportTarget(performer, this));
		// }
		if (!decorative && this != Game.level.player && this instanceof Actor)
			actions.add(new ActionFollow(Game.level.player, (Actor) this));

		// if (!decorative && performer.equipped != null &&
		// this.canContainOtherObjects) {
		// actions.add(new ActionGiveSpecificItem(performer, this,
		// performer.equipped, false));
		// }

		// Give from inventory
		if (!decorative && this.canContainOtherObjects) {
			actions.add(new ActionOpenInventoryToGiveItems(performer, this));
		}

		// Trade
		if (!decorative && this.canContainOtherObjects && this instanceof Actor && !(this instanceof NonHuman)) {
			actions.add(new ActionInitiateTrade(performer, (Actor) this));
		}

		if (!decorative && this.squareGameObjectIsOn != Game.level.player.squareGameObjectIsOn
				&& performer.equipped != null) {
			actions.add(new ActionThrowItem(performer, this, performer.equipped));
		}

		// Throw from inventory
		if (!decorative && this.squareGameObjectIsOn != Game.level.player.squareGameObjectIsOn)
			actions.add(new ActionOpenInventoryToThrowItems(performer, this, null));

		// Pour from inventory
		if (!decorative)
			actions.add(new ActionPourContainerInInventory(performer, this, null));

		if (!decorative)
			actions.add(new ActionIgnite(performer, this));

		// if (!decorative)
		// actions.add(new ActionCastBurn(performer, this));
		// if (!decorative)
		// actions.add(new ActionCastDouse(performer, this));
		// if (!decorative)
		// actions.add(new ActionCastPoison(performer, this));

		if (!(this instanceof MapMarker))
			actions.add(new ActionViewInfo(performer, this));

		return actions;

	}

	public Conversation getConversation() {
		return null;
	}

	public int straightLineDistanceBetween(Square sourceSquare, Square targetSquare) {

		if (sourceSquare == null)
			return Integer.MAX_VALUE;

		if (targetSquare == null)
			return Integer.MAX_VALUE;

		return Math.abs(sourceSquare.xInGrid - targetSquare.xInGrid)
				+ Math.abs(sourceSquare.yInGrid - targetSquare.yInGrid);
	}

	public int straightLineDistanceTo(Square square) {
		Game.straightLineDistanceTo++;

		return straightLineDistanceBetween(this.squareGameObjectIsOn, square);
	}

	@Override
	public int compareTo(Object otherObject) {

		if (otherObject instanceof GameObject) {
			return compareGameObjectToGameObject((GameObject) otherObject);
		}

		// if (Inventory.inventorySortBy ==
		// Inventory.INVENTORY_SORT_BY.SORT_BY_SLASH_DAMAGE) {
		// if (!(this instanceof Weapon) && !(otherObject instanceof Weapon)) {
		// return 0;
		// } else if (!(this instanceof Weapon)) {
		// return +1;
		// } else if (!(otherObject instanceof Weapon)) {
		// return -1;
		// } else {
		// return Math.round((((Weapon) otherObject).slashDamage - ((Weapon)
		// this).slashDamage));
		// }
		// }
		return 0;
	}

	public int compareGameObjectToGameObject(GameObject otherGameObject) {

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_ALPHABETICALLY) {
			return this.name.compareToIgnoreCase(otherGameObject.name);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_NEWEST) {
			return otherGameObject.turnAcquired - this.turnAcquired;
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_FAVOURITE) {
			return Boolean.compare(this.favourite, otherGameObject.favourite);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_VALUE) {
			return Float.compare(otherGameObject.value, this.value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_TOTAL_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_SLASH_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_BLUNT_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_PIERCE_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_FIRE_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_WATER_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_POISON_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_ELECTRICAL_DAMAGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_MAX_RANGE
				|| Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_MIN_RANGE) {

			if (!(this instanceof Weapon) && !(otherGameObject instanceof Weapon)) {
				return 0;
			} else if (!(this instanceof Weapon)) {
				return +1;
			} else if (!(otherGameObject instanceof Weapon)) {
				return -1;
			} else {
				return ((Weapon) this).compareWeaponToWeapon((Weapon) otherGameObject);
			}

		}

		return 0;

	}

	@Override
	public Action getDefaultActionPerformedOnThisInInventory(Actor performer) {

		if (this.inventoryThatHoldsThisObject == null) {
			return null;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_FILL) {
			if (this instanceof ContainerForLiquids)
				return new ActionFillSpecificContainer(performer, Inventory.waterSource, (ContainerForLiquids) this);
			else
				return null;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_DROP) {
			return new ActionDropItemsSelectedInInventory(performer, (Square) Inventory.target, this);
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_GIVE) {
			return new ActionGiveItemsSelectedInInventory(performer, (GameObject) Inventory.target, false, this);
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_THROW) {
			return new ActionThrowItem(performer, Inventory.target, this);
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_POUR) {
			return new ActionPourSpecificItem(performer, Inventory.target, (ContainerForLiquids) this);
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_LOOT) {
			if (this.inventoryThatHoldsThisObject != performer.inventory)
				return new ActionTakeItemsSelectedInInventory(performer, Inventory.target, this);
			else if (Inventory.target instanceof GameObject)
				return new ActionGiveItemsSelectedInInventory(performer, (GameObject) Inventory.target, false, this);
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_TRADE) {
			if (this.inventoryThatHoldsThisObject == performer.inventory)
				return new ActionSellItemsSelectedInInventory(performer, (Actor) Inventory.target, this);
			else
				return new ActionBuytemsSelectedInInventory(performer, (Actor) Inventory.target, this);
		}

		if (performer.equipped == this || performer.helmet == this || performer.bodyArmor == this
				|| performer.legArmor == this)
			return new ActionUnequip(performer, this);
		else
			return new ActionEquip(performer, this);

	}

	public Action getSecondaryActionPerformedOnThisInInventory(Actor performer) {

		if (this.inventoryThatHoldsThisObject == null) {
			return null;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_NORMAL) {

			// Food / drink
			if (this instanceof Food || this instanceof Liquid || this instanceof ContainerForLiquids
					|| this instanceof WaterBody) {
				return new ActionEatItemsSelectedInInventory(performer, this);
			}

			if (this.inventoryThatHoldsThisObject == performer.inventory) {
				return new ActionDropItemsSelectedInInventory(performer, performer.squareGameObjectIsOn, this);
			} else {
				return new ActionEquip(Game.level.player, this);
			}

		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_LOOT) {
			if (performer.equipped == this || performer.helmet == this || performer.bodyArmor == this
					|| performer.legArmor == this)
				return new ActionUnequip(performer, this);
			else
				return new ActionEquip(performer, this);

		}

		return null;
	}

	@Override
	public ArrayList<Action> getAllActionsPerformedOnThisInInventory(Actor performer) {

		ArrayList<Action> actions = new ArrayList<Action>();

		if (this.inventoryThatHoldsThisObject == null) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_FILL) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_DROP) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_GIVE) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_THROW) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_SELECT_ITEM_TO_POUR) {
			return actions;
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_LOOT) {
			if (this.inventoryThatHoldsThisObject != performer.inventory)
				actions.add(new ActionTakeItemsSelectedInInventory(performer, Inventory.target, this));
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_TRADE) {
			if (this.inventoryThatHoldsThisObject == performer.inventory)
				actions.add(new ActionSellItemsSelectedInInventory(performer, (Actor) Inventory.target, this));
			else
				actions.add(new ActionBuytemsSelectedInInventory(performer, (Actor) Inventory.target, this));
		}

		if (performer.equipped == this || performer.helmet == this || performer.bodyArmor == this
				|| performer.legArmor == this)
			actions.add(new ActionUnequip(performer, this));
		else
			actions.add(new ActionEquip(performer, this));

		// Food/drink
		if (this instanceof Food || this instanceof Liquid || this instanceof ContainerForLiquids
				|| this instanceof WaterBody) {
			actions.add(new ActionEatItemsSelectedInInventory(performer, this));
		}

		if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_LOOT) {
			actions.add(new ActionGiveItemsSelectedInInventory(performer, (GameObject) Inventory.target, false, this));
		}

		actions.add(new ActionDropItemsSelectedInInventory(performer, performer.squareGameObjectIsOn, this));

		if (this.inventoryThatHoldsThisObject == Game.level.player.inventory && !(this instanceof Gold)) {
			actions.add(new ActionStarSpecificItem(this));
		}

		// actions.add(new ActionThrow(performer, this, performer.equipped));
		// actions.add(new ActionCastBurn(performer, this));
		// actions.add(new ActionCastDouse(performer, this));
		// actions.add(new ActionCastPoison(performer, this));

		return actions;
	}

	public ArrayList<Action> getAllActionsPerformedOnThisInOtherInventory(Actor performer) {
		ArrayList<Action> actions = new ArrayList<Action>();
		if (!(this.inventoryThatHoldsThisObject.parent instanceof Actor)) {
			actions.add(new ActionTakeItemsSelectedInInventory(performer, Inventory.target, this));
			actions.add(new ActionEquip(performer, this));
		} else if (Inventory.inventoryMode == Inventory.INVENTORY_MODE.MODE_TRADE) {
			actions.add(new ActionBuytemsSelectedInInventory(performer, (Actor) Inventory.target, this));
		}

		return actions;
	}

	public boolean visibleFrom(Square square) {
		return visibilityBetween(squareGameObjectIsOn, square);
	}

	public boolean visibilityBetween(Square sourceSquare, Square targetSquare) {
		return checkVisibilityBetweenTwoPoints(targetSquare.xInGrid + 0.5d, targetSquare.yInGrid + 0.5d,
				sourceSquare.xInGrid + 0.5d, sourceSquare.yInGrid + 0.5d);
	}

	// SUPERCOVER algorithm
	public boolean checkVisibilityBetweenTwoPoints(double x0, double y0, double x1, double y1) {
		double vx = x1 - x0;
		double vy = y1 - y0;
		double dx = Math.sqrt(1 + Math.pow((vy / vx), 2));
		double dy = Math.sqrt(1 + Math.pow((vx / vy), 2));
		double ix = Math.floor(x0);
		double iy = Math.floor(y0);
		double sx, ex;

		if (vx < 0) {
			sx = -1;
			ex = (x0 - ix) * dx;
		} else {
			sx = 1;
			ex = (ix + 1 - x0) * dx;
		}

		double sy, ey;
		if (vy < 0) {
			sy = -1;
			ey = (y0 - iy) * dy;
		} else {
			sy = 1;
			ey = (iy + 1 - y0) * dy;
		}

		boolean done = false;
		double len = Math.sqrt(vx * vx + vy * vy);

		while (done == false) {
			if (Math.min(ex, ey) <= len) {
				double rx = ix;
				double ry = iy;
				if (ex < ey) {
					ex = ex + dx;
					ix = ix + sx;
				} else {
					ey = ey + dy;
					iy = iy + sy;
				}

				if (Game.level.squares[(int) rx][(int) ry] != squareGameObjectIsOn) {
					if (Game.level.squares[(int) rx][(int) ry].inventory.blocksLineOfSight()) {

						if ((int) x0 == (int) rx && (int) y0 == (int) ry) {
							return true;
						} else {
							return false;

						}
					}
				}
				// done = markSquareAsVisibleToActiveCharacter((int) rx, (int)
				// ry);
			} else if (!done) {
				done = true;
				return true;
				// markSquareAsVisibleToActiveCharacter((int) ix, (int) iy);
			}
		}
		return true;
	}

	public void clearActions() {
		actionsPerformedThisTurn.clear();
	}

	public void attackedBy(Object attacker, Action action) {
		if (attackable) {
			checkIfDestroyed(attacker, action);
		}
	}

	public void addEffect(Effect effectToAdd) {

		if (remainingHealth <= 0 || !attackable)
			return;

		boolean fullyResisted = true;
		for (HIGH_LEVEL_STATS offensiveStat : Stat.OFFENSIVE_STATS) {

			if (effectToAdd.highLevelStats.get(offensiveStat).value == 0)
				continue;

			if (this.highLevelStats.get(Stat.offensiveStatToDefensiveStatMap.get(offensiveStat)).value == 100)
				continue;

			fullyResisted = false;
			break;

		}

		if (fullyResisted)
			return;

		if (effectToAdd instanceof EffectBleed && !(this instanceof Actor))
			return;

		if (effectToAdd instanceof EffectWet)
			this.removeBurningEffect();

		Effect effectToRemove = null;
		for (Effect existingEffect : this.activeEffectsOnGameObject) {
			if (existingEffect.getClass() == effectToAdd.getClass()) {
				if (effectToAdd.turnsRemaining >= existingEffect.turnsRemaining) {
					effectToRemove = existingEffect;
				} else {
					return;
				}
			}
		}

		this.activeEffectsOnGameObject.remove(effectToRemove);
		this.activeEffectsOnGameObject.add(effectToAdd);
		if (Game.level.shouldLog(this))
			Game.level.logOnScreen(new ActivityLog(new Object[] { this, effectToAdd.logString, effectToAdd.source }));
	}

	public void activateEffects() {

		if (activeEffectsOnGameObject.size() == 0)
			return;

		// boolean wet = isWet();

		ArrayList<Effect> effectsToRemove = new ArrayList<Effect>();
		for (Effect effect : (ArrayList<Effect>) this.activeEffectsOnGameObject.clone()) {

			effect.activate();
			if (effect.turnsRemaining == 0)
				effectsToRemove.add(effect);
		}

		for (Effect effect : effectsToRemove) {
			this.removeEffect(effect);
		}
	}

	public void looted() {

	}

	public void thrown(Actor shooter) {

	}

	public void landed(Actor shooter, Action action) {

	}

	protected void addAttacker(GameObject potentialAttacker) {

		if (potentialAttacker != null && potentialAttacker.remainingHealth > 0
				&& !this.attackers.contains(potentialAttacker) && potentialAttacker != this) {
			this.attackers.add(potentialAttacker);
			// potentialAttacker.addAttackerForThisAndGroupMembers(this);
		}
	}

	public void addAttackerForThisAndGroupMembers(GameObject attacker) {

		if (this == attacker || attacker == null || attacker.remainingHealth <= 0)
			return;

		attacker.addAttacker(this);

		this.addAttacker(attacker);

		if (this.group != null) {
			if (!this.group.getAttackers().contains(attacker)) {
				this.group.addAttacker(attacker);
			}
			for (Actor groupMember : this.group.getMembers()) {
				if (!groupMember.attackers.contains(attacker)) {
					groupMember.addAttacker(attacker);
				}
				if (!attacker.attackers.contains(groupMember)) {
					attacker.addAttacker(groupMember);
				}
			}
		}

		if (attacker.group != null) {
			if (!attacker.group.getAttackers().contains(this)) {
				attacker.group.addAttacker(this);
			}
			for (Actor groupMember : attacker.group.getMembers()) {
				if (!groupMember.attackers.contains(this)) {
					groupMember.addAttacker(this);
				}
				if (!this.attackers.contains(groupMember)) {
					this.addAttacker(groupMember);
				}
			}
		}

	}

	public ArrayList<Effect> getActiveEffectsOnGameObject() {
		return activeEffectsOnGameObject;
	}

	@Override
	public String toString() {
		return name + " @ " + squareGameObjectIsOn;
	}

	// (int) (mouseXTransformed / Game.SQUARE_WIDTH); gives u sqr[x][]

	public Conversation createConversation(String text) {
		return Conversation.createConversation(text, this);
	}

	public Conversation createConversation(Object[] text) {
		return Conversation.createConversation(text, this);
	}

	public static int currentTemplateIdCount = 0;

	public static int generateNewTemplateId() {
		currentTemplateIdCount++;

		Level.bestiaryKnowledgeCollection.put(currentTemplateIdCount, new BestiaryKnowledge(currentTemplateIdCount));

		return currentTemplateIdCount;
	}

	public void setAttributesForCopy(GameObject gameObject, Square square, Actor owner) {
		gameObject.owner = owner;
		gameObject.squareGameObjectIsOn = square;
		gameObject.anchorX = anchorX;
		gameObject.anchorY = anchorY;
		gameObject.name = name;
		gameObject.value = value;
		gameObject.floats = floats;

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			gameObject.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			gameObject.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		for (HIGH_LEVEL_STATS statKey : this.highLevelStats.keySet()) {
			gameObject.highLevelStats.put(statKey, this.highLevelStats.get(statKey).makeCopy());
		}

		gameObject.totalHealth = gameObject.remainingHealth = totalHealth;
		gameObject.imageTexturePath = imageTexturePath;
		gameObject.imageTexture = imageTexture;
		gameObject.widthRatio = widthRatio;
		gameObject.heightRatio = heightRatio;
		gameObject.drawOffsetRatioX = drawOffsetRatioX;
		gameObject.drawOffsetRatioY = drawOffsetRatioY;
		gameObject.soundWhenHit = soundWhenHit;
		gameObject.soundWhenHitting = soundWhenHitting;
		gameObject.soundDampening = soundDampening;
		gameObject.weight = weight;

		gameObject.minRange = minRange;
		gameObject.maxRange = maxRange;

		gameObject.templateId = templateId;

		gameObject.diggable = diggable;
		gameObject.flipYAxisInMirror = flipYAxisInMirror;

		gameObject.bigShadow = bigShadow;
		gameObject.type = type;

		gameObject.orderingOnGound = orderingOnGound;

		gameObject.canBePickedUp = canBePickedUp;
		gameObject.fitsInInventory = fitsInInventory;
		if (gameObject.fitsInInventory) {
			gameObject.backwards = Game.random.nextBoolean();
		}
		gameObject.persistsWhenCantBeSeen = persistsWhenCantBeSeen;
		gameObject.attackable = attackable;
		gameObject.isFloorObject = isFloorObject;
		gameObject.moveable = moveable;

		gameObject.init();
	}

	@Override
	public void inventoryChanged() {
	}

	public void removeEffect(Effect effect) {
		this.activeEffectsOnGameObject.remove(effect);
	}

	public boolean isWet() {
		for (Effect effect : activeEffectsOnGameObject) {
			if (effect instanceof EffectWet) {
				return true;
			}
		}
		return false;
	}

	public void removeWetEffect() {
		Effect effectWet = null;
		for (Effect effect : activeEffectsOnGameObject) {
			if (effect instanceof EffectWet) {
				effectWet = effect;
			}
		}
		if (effectWet != null)
			removeEffect(effectWet);
	}

	public void removeBurningEffect() {
		Effect effectBurning = null;
		for (Effect effect : activeEffectsOnGameObject) {
			if (effect instanceof EffectBurning) {
				effectBurning = effect;
			}
		}
		if (effectBurning != null)
			removeEffect(effectBurning);
	}

	public void removePosionEffect() {
		Effect effectBurning = null;
		for (Effect effect : activeEffectsOnGameObject) {
			if (effect instanceof EffectPoison) {
				effectBurning = effect;
			}
		}
		if (effectBurning != null)
			removeEffect(effectBurning);
	}

	// public static interface DestructionListener {
	// public void onDestroy();
	// }

	// public void addDestructionListener(DestructionListener
	// destructionListener) {
	// if (!destructionListeners.contains(destructionListener))
	// destructionListeners.add(destructionListener);
	// }

	public boolean hasRange(int weaponDistanceTo) {
		if (getEffectiveMinRange() == 1 && weaponDistanceTo == 0)
			return true;

		if (weaponDistanceTo >= getEffectiveMinRange() && weaponDistanceTo <= getEffectiveMaxRange()) {
			return true;
		}
		return false;
	}

	public int compareWeaponToWeapon(Weapon otherGameObject) {

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_TOTAL_DAMAGE) {
			return Math.round(otherGameObject.getTotalDamage() - this.getTotalDamage());
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_SLASH_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.SLASH_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.SLASH_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_BLUNT_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.BLUNT_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.BLUNT_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_PIERCE_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.PIERCE_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.PIERCE_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_FIRE_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.FIRE_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.FIRE_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_WATER_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.WATER_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.WATER_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_POISON_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.POISON_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.POISON_DAMAGE).value);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_ELECTRICAL_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE).value);

		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_BLEEDING_DAMAGE) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.BLEED_DAMAGE).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.BLEED_DAMAGE).value);

		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_HEALING) {
			return Math.round(otherGameObject.highLevelStats.get(HIGH_LEVEL_STATS.HEALING).value
					- this.highLevelStats.get(HIGH_LEVEL_STATS.HEALING).value);

		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_MAX_RANGE) {
			return Math.round(otherGameObject.maxRange - this.maxRange);
		}

		if (Inventory.inventorySortBy == Inventory.INVENTORY_SORT_BY.SORT_BY_MIN_RANGE) {
			return Math.round(otherGameObject.minRange - this.minRange);
		}

		return 0;

	}

	public ArrayList<Action> getAllActionsForEquippedItem(Actor performer) {

		ArrayList<Action> actions = new ArrayList<Action>();

		if (isFloorObject) {
			return actions;
		}

		if (this instanceof Bell) {
			actions.add(new ActionRing(performer, this));
		}

		// Skinnable
		if (this instanceof Carcass) {
			actions.add(new ActionSkin(performer, this));
		}

		// Readable
		if (this instanceof Readable) {
			actions.add(new ActionRead(performer, (Readable) this));
		}

		// Searchable
		if (this instanceof Searchable) {
			actions.add(new ActionSearch(performer, (Searchable) this));
		}

		// Food / Drink
		if (this instanceof Food || this instanceof Liquid || this instanceof ContainerForLiquids
				|| this instanceof WaterBody) {
			actions.add(new ActionEatItems(performer, this));
		}

		// Switch
		if (this instanceof Switch) {
			Switch zwitch = (Switch) this;
			actions.add(
					new ActionUse(performer, zwitch, zwitch.actionName, zwitch.actionVerb, zwitch.requirementsToMeet));
		}

		// Loot
		if (/* (this instanceof Openable) && */ this.canContainOtherObjects && !(this instanceof Actor)) {
			actions.add(new ActionOpenOtherInventory(performer, this));
		}

		// Openable, Chests, Doors
		if (this instanceof Openable && !(this instanceof RemoteDoor)) {
			Openable openable = (Openable) this;

			if (!openable.open && openable.isOpenable) {
				actions.add(new ActionOpen(performer, openable));
			}

			if (openable.open && openable.isOpenable)
				actions.add(new ActionClose(performer, openable));

			if (openable.locked && openable.lockable)
				actions.add(new ActionUnlock(performer, openable));

			if (!openable.locked && openable.lockable)
				actions.add(new ActionLock(performer, openable));

			if (this instanceof Door) {
				if (!openable.open) {
					if (Game.level.player.peekingThrough == this)
						actions.add(new ActionStopPeeking(performer));
					else
						actions.add(new ActionPeek(performer, this));
				}
			}
		}

		actions.add(new ActionUnequip(performer, this));

		if (this instanceof Helmet || this instanceof BodyArmor || this instanceof LegArmor)
			actions.add(new ActionEquip(performer, this));

		actions.add(new ActionDropItemsSelectedInInventory(performer, performer.squareGameObjectIsOn, this));

		if (this.inventoryThatHoldsThisObject == Level.player.inventory && !(this instanceof Gold)) {
			actions.add(new ActionStarSpecificItem(this));
		}

		// if (!decorative && this != Game.level.player && attackable)
		// actions.add(new ActionAttack(performer, this));
		//
		// if (!decorative && this != Game.level.player && attackable && !(this
		// instanceof Wall)
		// && !(this instanceof Door))
		// actions.add(new ActionUsePower(Level.player, this, this.squareGameObjectIsOn,
		// new PowerTeleportOther(Level.player)));
		//
		// if (!decorative && this.squareGameObjectIsOn !=
		// Game.level.player.squareGameObjectIsOn
		// && performer.equipped != null) {
		// actions.add(new ActionThrowItem(performer, this, performer.equipped));
		// }
		//
		// // Throw from inventory
		// if (!decorative && this.squareGameObjectIsOn !=
		// Game.level.player.squareGameObjectIsOn)
		// actions.add(new ActionOpenInventoryToThrowItems(performer, this, null));
		//
		// // Pour from inventory
		// if (!decorative)
		// actions.add(new ActionPourContainerInInventory(performer, this, null));
		//
		// if (!decorative)
		// actions.add(new ActionIgnite(performer, this));

		return actions;
	}

	@Override
	public float getEffectiveHighLevelStat(HIGH_LEVEL_STATS statType) {
		float result = highLevelStats.get(statType).value;
		if (enhancement != null && enhancement.highLevelStats.get(statType).value != 0)
			result += enhancement.highLevelStats.get(statType).value;
		return result;
	}

	@Override
	public ArrayList<Object> getEffectiveHighLevelStatTooltip(HIGH_LEVEL_STATS statType) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(name + " " + highLevelStats.get(statType).value);
		if (enhancement != null && enhancement.highLevelStats.get(statType).value != 0) {
			result.add(TextUtils.NewLine.NEW_LINE);
			result.add("" + enhancement.enhancementName + " " + enhancement.highLevelStats.get(statType).value);
		}
		return result;
	}

	public float getEffectiveMinRange() {
		return minRange;
	}

	public float getEffectiveMaxRange() {
		return maxRange;
	}

	protected float getTotalDamage() {
		return highLevelStats.get(HIGH_LEVEL_STATS.SLASH_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.BLUNT_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.PIERCE_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.FIRE_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.WATER_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.ELECTRICAL_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.POISON_DAMAGE).value
				+ highLevelStats.get(HIGH_LEVEL_STATS.BLEED_DAMAGE).value;
	}

	public void changeHealth(float change, Object attacker, Action action) {

		if (attackable == false)
			return;

		remainingHealth += change;
		if (remainingHealth > totalHealth)
			remainingHealth = totalHealth;
		if (remainingHealth < 0)
			remainingHealth = 0;

		if (change < 0)
			attackedBy(attacker, action);

	}

	public void changeHealthSafetyOff(float change, Object attacker, Action action) {

		remainingHealth += change;
		if (remainingHealth > totalHealth)
			remainingHealth = totalHealth;
		if (remainingHealth < 0)
			remainingHealth = 0;

		if (change < 0)
			attackedBy(attacker, action);

	}

	public float changeHealth(Object attacker, Action action, DamageDealer damageDealer) {

		if (attackable == false)
			return 0;

		int offsetY = 0;
		boolean thisIsAnAttack = false;
		float totalDamage = 0;
		GameObject gameObjectAttacker = null;
		if (attacker instanceof GameObject)
			gameObjectAttacker = (GameObject) attacker;

		for (HIGH_LEVEL_STATS statType : Stat.OFFENSIVE_STATS) {
			if (damageDealer.getEffectiveHighLevelStat(statType) != 0) {

				float resistance = (this.highLevelStats.get(Stat.offensiveStatToDefensiveStatMap.get(statType)).value
						* 0.01f);
				float resistedDamage = damageDealer.getEffectiveHighLevelStat(statType) * resistance;
				float friendlyFireReduction = 0;
				if (gameObjectAttacker != null && gameObjectAttacker.group != null
						&& gameObjectAttacker.group.contains(this)) {
					friendlyFireReduction = gameObjectAttacker.getEffectiveHighLevelStat(HIGH_LEVEL_STATS.FRIENDLY_FIRE)
							* 0.01f;
				}
				float dmg = damageDealer.getEffectiveHighLevelStat(statType) - resistedDamage - friendlyFireReduction;
				doDamageAnimation(dmg, offsetY, statType, this.highLevelStats.get(statType).value);
				remainingHealth -= dmg;
				totalDamage += dmg;

				if (dmg >= 0)
					thisIsAnAttack = true;

				// Update bestiary
				if (Game.level.shouldLog(this))
					Level.bestiaryKnowledgeCollection.get(this.templateId).putHighLevel(statType, true);

				if (gameObjectAttacker != null)
					if (Game.level.shouldLog(gameObjectAttacker))
						Level.bestiaryKnowledgeCollection.get(gameObjectAttacker.templateId).putHighLevel(statType,
								true);

				offsetY += 48;
			}
		}

		if (remainingHealth > totalHealth)
			remainingHealth = totalHealth;
		if (remainingHealth < 0)
			remainingHealth = 0;

		if (attacker != null && thisIsAnAttack == true) {
			if (this instanceof Actor) {
				Actor actor = (Actor) this;
				actor.attackedBy(attacker, action);
			} else {
				attackedBy(attacker, action);
			}
		}

		return totalDamage;
	}

	public float changeHealth(Object attacker, Action action, Stat damage) {

		if (attackable == false)
			return 0;

		int offsetY = 0;
		boolean thisIsAnAttack = false;
		float totalDamage = 0;
		GameObject gameObjectAttacker = null;
		if (attacker instanceof GameObject)
			gameObjectAttacker = (GameObject) attacker;

		float resistance = (this.highLevelStats.get(Stat.offensiveStatToDefensiveStatMap.get(damage.type)).value
				* 0.01f);
		float resistedDamage = damage.value * resistance;
		float friendlyFireReduction = 0;
		if (gameObjectAttacker != null && gameObjectAttacker.group != null && gameObjectAttacker.group.contains(this)) {
			friendlyFireReduction = gameObjectAttacker.getEffectiveHighLevelStat(HIGH_LEVEL_STATS.FRIENDLY_FIRE)
					* 0.01f;
		}
		float dmg = damage.value - resistedDamage - friendlyFireReduction;
		doDamageAnimation(dmg, offsetY, damage.type, this.highLevelStats.get(damage.type).value);
		remainingHealth -= dmg;
		totalDamage += dmg;

		if (dmg >= 0)
			thisIsAnAttack = true;

		// Update bestiary
		if (Game.level.shouldLog(this))
			Level.bestiaryKnowledgeCollection.get(this.templateId).putHighLevel(damage.type, true);

		if (gameObjectAttacker != null)
			if (Game.level.shouldLog(gameObjectAttacker))
				Level.bestiaryKnowledgeCollection.get(gameObjectAttacker.templateId).putHighLevel(damage.type, true);

		offsetY += 48;

		if (remainingHealth > totalHealth)
			remainingHealth = totalHealth;
		if (remainingHealth < 0)
			remainingHealth = 0;

		if (attacker != null && thisIsAnAttack == true) {
			if (this instanceof Actor) {
				Actor actor = (Actor) this;
				actor.attackedBy(attacker, action);
			} else {
				attackedBy(attacker, action);
			}
		}

		return totalDamage;
	}

	// public enum DAMAGE_TYPE {
	// SLASH, BLUNT, PIERCE, FIRE, WATER, ELECTRIC, POISON, BLEEDING, HEALING
	// };

	public void setPrimaryAnimation(Animation animation) {

		if (animation == null || animation.runAnimation == false)
			return;

		if (this.primaryAnimation != null && !this.primaryAnimation.completed
				&& !(this.primaryAnimation instanceof AnimationWait)) {
			this.primaryAnimation.runCompletionAlorightm(false);
		}

		Level.blockingAnimations.remove(this.primaryAnimation);
		Level.animations.remove(this.primaryAnimation);

		this.primaryAnimation = animation;

		if (animation != null && animation.blockAI) {
			Level.blockingAnimations.add(animation);
		}
		Level.animations.add(animation);
	}

	public Animation getPrimaryAnimation() {
		return this.primaryAnimation;
	}

	public void doDamageAnimation(float healing, float offsetY, HIGH_LEVEL_STATS statType, float res) {

		if (squareGameObjectIsOn == null)
			return;

		Color color = Color.WHITE;

		// Heal
		if (res > 100)
			color = color.GREEN;

		// bad hit
		else if (res > 50)
			color = color.DARK_GRAY;
		else if (res > 0)
			color = color.LIGHT_GRAY;

		// good hit
		else if (res < -50)

			color = color.RED;
		else if (res < 0)
			color = color.ORANGE;

		// TYPES
		// CRIT large red, HIGH DMG red, NORMAL DMG white, resisted DMG grey,
		// HEAL green

		int x = (int) (squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH * drawOffsetRatioX);
		int y = (int) (squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT * drawOffsetRatioY);

		Level.addSecondaryAnimation(
				new AnimationDamageText((int) healing, this, x + 32, y - 64 + offsetY, 0.1f, statType, color, null));
	}

	public void setImageAndExtrapolateSize(String imagPath) {
		this.imageTexturePath = imagPath;
		this.imageTexture = getGlobalImage(imagPath, true);
		this.widthRatio = imageTexture.getWidth() / Game.SQUARE_WIDTH;
		this.heightRatio = imageTexture.getHeight() / Game.SQUARE_HEIGHT;

	}

	public void drawAction(Action action, boolean onMouse) {

		if (onMouse) {
			TextureUtils.drawTexture(action.image, 1f, UserInputLevel.mouseLastX + 16,
					Game.windowHeight - UserInputLevel.mouseLastY + 16,
					UserInputLevel.mouseLastX + Game.QUARTER_SQUARE_WIDTH + 16,
					Game.windowHeight - UserInputLevel.mouseLastY + Game.QUARTER_SQUARE_HEIGHT + 16);
		} else {

			int actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels
					+ Game.SQUARE_WIDTH * drawOffsetRatioX);
			int actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels
					+ Game.SQUARE_HEIGHT * drawOffsetRatioY);
			if (primaryAnimation != null) {
				actorPositionXInPixels += primaryAnimation.offsetX;
				actorPositionYInPixels += primaryAnimation.offsetY;
			}
			TextureUtils.drawTexture(action.image, actorPositionXInPixels + Game.QUARTER_SQUARE_WIDTH,
					actorPositionYInPixels + Game.QUARTER_SQUARE_WIDTH,
					actorPositionXInPixels + Game.SQUARE_WIDTH - Game.QUARTER_SQUARE_WIDTH,
					actorPositionYInPixels + Game.SQUARE_HEIGHT - Game.QUARTER_SQUARE_WIDTH);
		}

	}

	public ArrayList<GameObject> getSurroundingGameObjects() {
		ArrayList<GameObject> surroundingGameObjects = new ArrayList<GameObject>();
		Square squareToLeft = this.squareGameObjectIsOn.getSquareToLeftOf();
		if (squareToLeft != null) {
			surroundingGameObjects.addAll(squareToLeft.inventory.gameObjects);
		}

		Square squareToRight = this.squareGameObjectIsOn.getSquareToRightOf();
		if (squareToRight != null) {
			surroundingGameObjects.addAll(squareToRight.inventory.gameObjects);
		}
		Square squareAbove = this.squareGameObjectIsOn.getSquareAbove();
		if (squareAbove != null) {
			surroundingGameObjects.addAll(squareAbove.inventory.gameObjects);
		}
		Square squareBelow = this.squareGameObjectIsOn.getSquareBelow();
		if (squareBelow != null) {
			surroundingGameObjects.addAll(squareBelow.inventory.gameObjects);
		}

		return surroundingGameObjects;

	}
}
