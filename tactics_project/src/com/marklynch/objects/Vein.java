package com.marklynch.objects;

import java.util.ArrayList;

import com.marklynch.level.squares.Square;
import com.marklynch.objects.units.Actor;

public class Vein extends Wall {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();

	public boolean infinite = false;

	public Junk oreTemplate;

	public double dropChance;

	public Vein() {
		super();
		type = "Vein";
	}

	// public boolean fullWall;
	// public boolean fullRightWall;
	// public boolean fullLeftWall;
	// public boolean fullTopWall;
	// public boolean fullBottomWall;
	// public boolean horizontalWall;
	// public boolean verticalWall;
	// public boolean connectedTop;
	// public boolean connectedTopRight;
	// public boolean connectedRight;
	// public boolean connectedBottomRight;
	// public boolean connectedBottom;
	// public boolean connectedBottomLeft;
	// public boolean connectedLeft;
	// public boolean connectedTopLeft;
	//
	// public static Texture textureFullWall;
	// public static Texture textureFullTopWall;
	// public static Texture textureFullRightWall;
	// public static Texture textureFullBottomWall;
	// public static Texture textureFullLeftWall;
	// public static Texture textureHorizontalWall;
	// public static Texture textureVerticalWall;
	// public static Texture textureTop;
	// public static Texture textureTopRight;
	// public static Texture textureRight;
	// public static Texture textureBottomRight;
	// public static Texture textureBottom;
	// public static Texture textureBottomLeft;
	// public static Texture textureLeft;
	// public static Texture textureTopLeft;
	//
	// public float drawX1, drawX2, drawY1, drawY2;
	//
	// public float topLeftDrawX1, topLeftDrawX2, topLeftDrawY1, topLeftDrawY2;
	// public float topDrawX1, topDrawX2, topDrawY1, topDrawY2;
	// public float topRightDrawX1, topRightDrawX2, topRightDrawY1,
	// topRightDrawY2;
	// public float rightDrawX1, rightDrawX2, rightDrawY1, rightDrawY2;
	// public float bottomRightDrawX1, bottomRightDrawX2, bottomRightDrawY1,
	// bottomRightDrawY2;
	// public float bottomDrawX1, bottomDrawX2, bottomDrawY1, bottomDrawY2;
	// public float bottomLeftDrawX1, bottomLeftDrawX2, bottomLeftDrawY1,
	// bottomLeftDrawY2;
	// public float leftDrawX1, leftDrawX2, leftDrawY1, leftDrawY2;
	//
	// public float halfWidth = Game.HALF_SQUARE_WIDTH;
	// public float halfHeight = Game.HALF_SQUARE_HEIGHT;
	// public float quarterWidth = Game.SQUARE_WIDTH / 4;
	// public float quarterHeight = Game.SQUARE_HEIGHT / 4;
	//
	// public Vein() {
	// super();
	// if (squareGameObjectIsOn != null) {
	// drawX1 = (int) (squareGameObjectIsOn.xInGridPixels + drawOffsetRatioX);
	// drawX2 = (int) (drawX1 + width);
	// drawY1 = (int) (squareGameObjectIsOn.yInGridPixels + drawOffsetRatioY);
	// drawY2 = (int) (drawY1 + height);
	//
	// }
	//
	// canBePickedUp = false;
	//
	// fitsInInventory = false;
	// canShareSquare = false;
	//
	// blocksLineOfSight = true;
	// persistsWhenCantBeSeen = true;
	//
	// }
	//
	// public static void loadStaticImages() {
	// textureFullWall = getGlobalImage("vein.png", true);
	// textureFullTopWall = getGlobalImage("vein_full_top.png", false);
	// textureFullRightWall = getGlobalImage("vein_full_right.png", false);
	// textureFullBottomWall = getGlobalImage("vein_full_bottom.png", false);
	// textureFullLeftWall = getGlobalImage("vein_full_left.png", false);
	// textureTop = getGlobalImage("vein_top.png", false);
	// textureTopRight = getGlobalImage("vein_top_right.png", false);
	// textureRight = getGlobalImage("vein_right.png", false);
	// textureBottomRight = getGlobalImage("vein_bottom_right.png", false);
	// textureBottom = getGlobalImage("vein_bottom.png", false);
	// textureBottomLeft = getGlobalImage("vein_bottom_left.png", false);
	// textureLeft = getGlobalImage("vein_left.png", false);
	// textureTopLeft = getGlobalImage("vein_top_left.png", false);
	// }
	//
	// @Override
	// public void draw1() {
	//
	// if (this.remainingHealth <= 0)
	// return;
	//
	// TextureUtils.drawTexture(textureFullWall, 1f, drawX1, drawY1, drawX2,
	// drawY2);
	//
	// if (!Game.fullVisiblity) {
	// if (!this.squareGameObjectIsOn.seenByPlayer)
	// return;
	//
	// if (this.squareGameObjectIsOn.visibleToPlayer == false &&
	// persistsWhenCantBeSeen == false)
	// return;
	// }
	//
	// // Draw object
	// if (squareGameObjectIsOn != null) {
	//
	// float alpha = 1.0f;
	//
	// // 8
	// if (fullWall) {
	// TextureUtils.drawTexture(textureFullWall, alpha, drawX1, drawY1, drawX2,
	// drawY2);
	//
	// }
	//
	// // 5
	// else if (fullLeftWall) {
	// TextureUtils.drawTexture(textureFullLeftWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	//
	// else if (fullRightWall) {
	// TextureUtils.drawTexture(textureFullRightWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	//
	// else if (fullTopWall) {
	// TextureUtils.drawTexture(textureFullTopWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	//
	// else if (fullBottomWall) {
	// TextureUtils.drawTexture(textureFullBottomWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	//
	// // 2
	// else if (horizontalWall) {
	// TextureUtils.drawTexture(textureHorizontalWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	//
	// else if (verticalWall) {
	// TextureUtils.drawTexture(textureVerticalWall, alpha, drawX1, drawY1,
	// drawX2, drawY2);
	//
	// }
	// Game.flush();
	//
	// // if (connectedTop)
	// // TextureUtils.drawTexture(textureTop, alpha, topDrawX1, topDrawX2,
	// // topDrawY1, topDrawY2);
	// // if (connectedTopRight)
	// // TextureUtils.drawTexture(textureTopRight, alpha, topRightDrawX1,
	// // topRightDrawX2, topRightDrawY1,
	// // topRightDrawY2);
	// // if (connectedRight)
	// // TextureUtils.drawTexture(textureRight, alpha, rightDrawX1,
	// // rightDrawX2, rightDrawY1, rightDrawY2);
	// // if (connectedBottomRight)
	// // TextureUtils.drawTexture(textureBottomRight, alpha,
	// // bottomRightDrawX1, bottomRightDrawX2,
	// // bottomRightDrawY1, bottomRightDrawY2);
	// // if (connectedBottom)
	// // TextureUtils.drawTexture(textureBottom, alpha, bottomDrawX1,
	// // bottomDrawX2, bottomDrawY1, bottomDrawY2);
	// // if (connectedBottomLeft)
	// // TextureUtils.drawTexture(textureBottomLeft, alpha,
	// // bottomLeftDrawX1, bottomLeftDrawX2, bottomLeftDrawY1,
	// // bottomLeftDrawY2);
	// // if (connectedLeft)
	// // TextureUtils.drawTexture(textureLeft, alpha, leftDrawX1,
	// // leftDrawX2, leftDrawY1, leftDrawY2);
	// // if (connectedTopLeft)
	// // TextureUtils.drawTexture(textureTopLeft, alpha, topLeftDrawX1,
	// // topLeftDrawX2, topLeftDrawY1,
	// // topLeftDrawY2);
	// }
	// }
	//
	// // @Override
	// // public Action getDefaultActionPerformedOnThisInWorld(Actor performer)
	// {
	// // return new ActionMine(performer, this);
	// // }
	//
	// @Override
	// public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {
	// return new ActionMine(performer, this);
	// }
	//
	// @Override
	// public Vein makeCopy(Square square, Actor owner) {
	// Vein vein = new Vein();
	// super.setAttributesForCopy(vein, square, owner);
	// if (squareGameObjectIsOn != null) {
	// vein.drawX1 = (int) (vein.squareGameObjectIsOn.xInGridPixels +
	// vein.drawOffsetRatioX);
	// vein.drawX2 = (int) (vein.drawX1 + vein.width);
	// vein.drawY1 = (int) (vein.squareGameObjectIsOn.yInGridPixels +
	// vein.drawOffsetRatioY);
	// vein.drawY2 = (int) (vein.drawY1 + vein.height);
	// }
	// return vein;
	// }
	//
	// @Override
	// public void checkIfFullWall() {
	//
	// fullWall = true;
	// if (fullWall)
	// return;
	//
	// // 8
	// if (fullWall = connectedTop && connectedTopRight && connectedRight &&
	// connectedBottomRight && connectedBottom
	// && connectedBottomLeft && connectedLeft && connectedTopLeft)
	// return;
	//
	// // 5
	// if (fullLeftWall = connectedTop && connectedBottom && connectedBottomLeft
	// && connectedLeft && connectedTopLeft)
	// return;
	//
	// if (fullRightWall = connectedTop && connectedTopRight && connectedRight
	// && connectedBottomRight
	// && connectedBottom)
	// return;
	//
	// if (fullTopWall = connectedTop && connectedTopRight && connectedRight &&
	// connectedLeft && connectedTopLeft)
	// return;
	//
	// if (fullBottomWall = connectedRight && connectedBottomRight &&
	// connectedBottom && connectedBottomLeft
	// && connectedLeft)
	// return;
	//
	// // 2
	// if (horizontalWall = connectedRight && connectedLeft)
	// return;
	//
	// if (verticalWall = connectedTop && connectedBottom)
	// return;
	//
	// }

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	// , boolean infinite, Junk oreTemplate, double dropChance) {
	public Vein makeCopy(Square square, Actor owner, boolean infinite, Junk oreTemplate, double dropChance) {
		Vein vein = new Vein();
		setInstances(vein);
		vein.infinite = infinite;
		vein.oreTemplate = oreTemplate;
		vein.dropChance = dropChance;
		super.setAttributesForCopy(vein, square, owner);
		if (vein.squareGameObjectIsOn != null) {
			vein.drawX1 = (int) (vein.squareGameObjectIsOn.xInGridPixels + vein.drawOffsetRatioX);
			vein.drawX2 = (int) (vein.drawX1 + vein.width);
			vein.drawY1 = (int) (vein.squareGameObjectIsOn.yInGridPixels + vein.drawOffsetRatioY);
			vein.drawY2 = (int) (vein.drawY1 + vein.height);
		}
		return vein;
	}

}
