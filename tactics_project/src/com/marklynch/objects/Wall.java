package com.marklynch.objects;

import java.util.ArrayList;
import java.util.Random;

import com.marklynch.Game;
import com.marklynch.level.squares.Square;
//import com.marklynch.objects.actions.ActionPickUp;
import com.marklynch.objects.units.Actor;
import com.marklynch.utils.Color;
import com.marklynch.utils.TextureUtils;

//import mdesl.graphics.Texture;

public class Wall extends GameObject {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>();

	public boolean fullWall = true;
	public boolean fullRightWall;
	public boolean fullLeftWall;
	public boolean fullTopWall;
	public boolean fullBottomWall;
	public boolean connectedTop;
	public boolean connectedTopRight;
	public boolean connectedRight;
	public boolean connectedBottomRight;
	public boolean connectedBottom;
	public boolean connectedBottomLeft;
	public boolean connectedLeft;
	public boolean connectedTopLeft;

	// public static Texture textureFullWall;
	// public static Texture textureFullTopWall;
	// public static Texture textureFullRightWall;
	// public static Texture textureFullBottomWall;
	// public static Texture textureFullLeftWall;
	// public static Texture textureTop;
	// public static Texture textureTopRight;
	// public static Texture textureRight;
	// public static Texture textureBottomRight;
	// public static Texture textureBottom;
	// public static Texture textureBottomLeft;
	// public static Texture textureLeft;
	// public static Texture textureTopLeft;

	public float drawX1, drawX2, drawY1, drawY2;

	public float fullDrawX1, fullDrawX2, fullDrawY1, fullDrawY2;

	public float topLeftDrawX1, topLeftDrawX2, topLeftDrawY1, topLeftDrawY2;
	public float topDrawX1, topDrawX2, topDrawY1, topDrawY2;
	public float topRightDrawX1, topRightDrawX2, topRightDrawY1, topRightDrawY2;
	public float rightDrawX1, rightDrawX2, rightDrawY1, rightDrawY2;
	public float bottomRightDrawX1, bottomRightDrawX2, bottomRightDrawY1, bottomRightDrawY2;
	public float bottomDrawX1, bottomDrawX2, bottomDrawY1, bottomDrawY2;
	public float bottomLeftDrawX1, bottomLeftDrawX2, bottomLeftDrawY1, bottomLeftDrawY2;
	public float leftDrawX1, leftDrawX2, leftDrawY1, leftDrawY2;

	// public float halfWidth = Game.HALF_SQUARE_WIDTH;
	// public float halfHeight = Game.HALF_SQUARE_HEIGHT;
	public float quarterWidth = Game.SQUARE_WIDTH / 4;
	public float quarterHeight = Game.SQUARE_HEIGHT / 4;

	public Wall() {
		super();

		canBePickedUp = false;

		fitsInInventory = false;
		canShareSquare = false;

		blocksLineOfSight = true;
		persistsWhenCantBeSeen = true;
		type = "Wall";
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	public void initWall(float maxRandomness) {

		if (squareGameObjectIsOn != null) {

			Random random = new Random();
			float randomOffset = random.nextFloat() * maxRandomness;

			halfWidth = Game.HALF_SQUARE_WIDTH;
			halfHeight = Game.HALF_SQUARE_HEIGHT;
			quarterWidth = Game.SQUARE_WIDTH / 4;
			quarterHeight = Game.SQUARE_HEIGHT / 4;

			drawX1 = (int) (squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_WIDTH + drawOffsetRatioX);
			drawX2 = (int) (drawX1 + width);
			drawY1 = (int) (squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT + drawOffsetRatioY);
			drawY2 = (int) (drawY1 + height);

			fullDrawX1 = drawX1;
			fullDrawY1 = drawY1;
			fullDrawX2 = drawX1 + width;
			fullDrawY2 = drawY1 + height;

			topLeftDrawX1 = drawX1;
			topLeftDrawY1 = drawY1;
			topLeftDrawX2 = drawX1 + quarterWidth + randomOffset;
			topLeftDrawY2 = drawY1 + quarterHeight + randomOffset;

			topDrawX1 = drawX1 + quarterWidth - randomOffset;
			topDrawY1 = drawY1;
			topDrawX2 = drawX2 - quarterWidth + randomOffset;
			topDrawY2 = drawY1 + halfHeight + randomOffset;

			topRightDrawX1 = drawX2 - quarterWidth - randomOffset;
			topRightDrawY1 = drawY1;
			topRightDrawX2 = drawX2;
			topRightDrawY2 = drawY1 + quarterHeight + randomOffset;

			rightDrawX1 = drawX1 + halfWidth - randomOffset;
			rightDrawY1 = drawY1 + quarterHeight - randomOffset;
			rightDrawX2 = drawX2;
			rightDrawY2 = drawY2 - quarterHeight + randomOffset;

			bottomRightDrawX1 = drawX2 - quarterWidth - randomOffset;
			bottomRightDrawY1 = drawY2 - quarterHeight - randomOffset;
			bottomRightDrawX2 = drawX2;
			bottomRightDrawY2 = drawY2;

			bottomDrawX1 = drawX1 + quarterWidth - randomOffset;
			bottomDrawY1 = drawY2 - halfHeight - randomOffset;
			bottomDrawX2 = drawX2 - quarterWidth + randomOffset;
			bottomDrawY2 = drawY2;

			bottomLeftDrawX1 = drawX1;
			bottomLeftDrawY1 = drawY2 - quarterHeight - randomOffset;
			bottomLeftDrawX2 = drawX1 + quarterWidth + randomOffset;
			bottomLeftDrawY2 = drawY2;

			leftDrawX1 = drawX1;
			leftDrawY1 = drawY1 + quarterHeight - randomOffset;
			leftDrawX2 = drawX1 + halfWidth + randomOffset;
			leftDrawY2 = drawY2 - quarterHeight + randomOffset;

		}

	}

	@Override
	public void draw1() {

		if (this.remainingHealth <= 0)
			return;

		if (!Game.fullVisiblity) {
			if (!this.squareGameObjectIsOn.seenByPlayer)
				return;

			if (this.squareGameObjectIsOn.visibleToPlayer == false && persistsWhenCantBeSeen == false)
				return;
		}

		// Draw object
		if (squareGameObjectIsOn != null) {

			float alpha = 1.0f;

			if (fullWall) {

				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, fullDrawX1,
						fullDrawY1, fullDrawX2, fullDrawY2, false, false, Color.WHITE);
				return;
			}

			if (connectedTop)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, topDrawX1,
						topDrawY1, topDrawX2, topDrawY2, false, false, Color.WHITE);

			if (connectedTopRight)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2,
						topRightDrawX1, topRightDrawY1, topRightDrawX2, topRightDrawY2, false, false, Color.WHITE);
			//
			if (connectedRight)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, rightDrawX1,
						rightDrawY1, rightDrawX2, rightDrawY2, false, false, Color.WHITE);
			//
			if (connectedBottomRight)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2,
						bottomRightDrawX1, bottomRightDrawY1, bottomRightDrawX2, bottomRightDrawY2, false, false,
						Color.WHITE);
			//
			if (connectedBottom)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, bottomDrawX1,
						bottomDrawY1, bottomDrawX2, bottomDrawY2, false, false, Color.WHITE);
			//
			if (connectedBottomLeft)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2,
						bottomLeftDrawX1, bottomLeftDrawY1, bottomLeftDrawX2, bottomLeftDrawY2, false, false,
						Color.WHITE);
			//
			if (connectedLeft)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, leftDrawX1,
						leftDrawY1, leftDrawX2, leftDrawY2, false, false, Color.WHITE);
			//
			if (connectedTopLeft)
				TextureUtils.drawTextureWithinBounds(imageTexture, alpha, drawX1, drawY1, drawX2, drawY2, topLeftDrawX1,
						topLeftDrawY1, topLeftDrawX2, topLeftDrawY2, false, false, Color.WHITE);
		}
	}

	@Override
	public Wall makeCopy(Square square, Actor owner) {
		Wall wall = new Wall();
		setInstances(wall);
		super.setAttributesForCopy(wall, square, owner);
		// if (wall.squareGameObjectIsOn != null) {
		// wall.drawX1 = (int) (wall.squareGameObjectIsOn.xInGridPixels +
		// wall.drawOffsetRatioX);
		// wall.drawX2 = (int) (wall.drawX1 + wall.width);
		// wall.drawY1 = (int) (wall.squareGameObjectIsOn.yInGridPixels +
		// wall.drawOffsetRatioY);
		// wall.drawY2 = (int) (wall.drawY1 + wall.height);
		// }
		wall.initWall(16f);
		return wall;
	}

	public void checkIfFullWall() {

		fullWall = false;

		fullWall = connectedTop && connectedTopRight && connectedRight && connectedBottomRight && connectedBottom
				&& connectedBottomLeft && connectedLeft && connectedTopLeft;

		if (fullWall == false)
			fullWall = !connectedTop && !connectedTopRight && !connectedRight && !connectedBottomRight
					&& !connectedBottom && !connectedBottomLeft && !connectedLeft && !connectedTopLeft;

		fullLeftWall = connectedTop && connectedBottom && connectedBottomLeft && connectedLeft && connectedTopLeft;

		fullRightWall = connectedTop && connectedTopRight && connectedRight && connectedBottomRight && connectedBottom;

		fullTopWall = connectedTop && connectedTopRight && connectedRight && connectedLeft && connectedTopLeft;

		fullBottomWall = connectedRight && connectedBottomRight && connectedBottom && connectedBottomLeft
				&& connectedLeft;

	}

}