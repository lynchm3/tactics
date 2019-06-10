package com.marklynch.objects.inanimateobjects;

import com.marklynch.Game;
import com.marklynch.actions.Action;
import com.marklynch.actions.ActionMove;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.animation.Animation.OnCompletionListener;
import com.marklynch.level.constructs.animation.primary.AnimationStraightLine;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.Direction;
import com.marklynch.utils.ArrayList;
import com.marklynch.utils.Color;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.Texture;

public class ConveyerBelt extends GameObject implements OnCompletionListener {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>(GameObject.class);
	public Square connectedSquare = null;
	public Direction direction = Direction.LEFT;

	public static Texture textureUp, textureDown, textureLeft, textureRight;

	public ConveyerBelt() {
		super();
		canBePickedUp = false;
		fitsInInventory = false;
		canShareSquare = true;
		canContainOtherObjects = false;
		persistsWhenCantBeSeen = true;
		attackable = false;
		moveable = false;
		isFloorObject = true;
		type = "Conveyer Belt";
	}

	public static void loadStaticImages() {
		textureUp = ResourceUtils.getGlobalImage("conveyer_belt_up.png", true);
		textureDown = ResourceUtils.getGlobalImage("conveyer_belt_down.png", true);
		textureLeft = ResourceUtils.getGlobalImage("conveyer_belt_left.png", true);
		textureRight = ResourceUtils.getGlobalImage("conveyer_belt_right.png", true);
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

//	public void draw1()
//	{
//		
//	}

	public ConveyerBelt makeCopy(Square square, Actor owner, Direction direction) {
		ConveyerBelt conveyorBelt = new ConveyerBelt();
		setInstances(conveyorBelt);
		super.setAttributesForCopy(conveyorBelt, square, owner);
		conveyorBelt.direction = direction;
		if (conveyorBelt.direction == Direction.LEFT) {
			conveyorBelt.connectedSquare = conveyorBelt.squareGameObjectIsOn.getSquareToLeftOf();
			conveyorBelt.imageTexture = textureLeft;
			conveyorBelt.width = 192;
			conveyorBelt.halfWidth = 192 / 2;
			conveyorBelt.widthRatio = 1.5f;
		} else if (conveyorBelt.direction == Direction.RIGHT) {
			conveyorBelt.connectedSquare = conveyorBelt.squareGameObjectIsOn.getSquareToRightOf();
			conveyorBelt.imageTexture = textureRight;
			conveyorBelt.width = 192;
			conveyorBelt.halfWidth = 192 / 2;
			conveyorBelt.widthRatio = 1.5f;
		} else if (conveyorBelt.direction == Direction.UP) {
			conveyorBelt.connectedSquare = conveyorBelt.squareGameObjectIsOn.getSquareAbove();
			conveyorBelt.imageTexture = textureUp;
			conveyorBelt.height = 192;
			conveyorBelt.halfHeight = 192 / 2;
			conveyorBelt.heightRatio = 1.5f;
		} else if (conveyorBelt.direction == Direction.DOWN) {
			conveyorBelt.connectedSquare = conveyorBelt.squareGameObjectIsOn.getSquareBelow();
			conveyorBelt.imageTexture = textureDown;
			conveyorBelt.height = 192;
			conveyorBelt.halfHeight = 192 / 2;
			conveyorBelt.heightRatio = 1.5f;
		}

//		conveyorBelt.widthRatio = 1;
//		conveyorBelt.heightRatio = 1;
//		conveyorBelt.drawOffsetX = -128f;
//		conveyorBelt.drawOffsetY = 0f;

//		conveyorBelt.continueAnimation();

		return conveyorBelt;
	}

	float offsetX = 0;
	float offsetY = 0;

	public boolean draw1() {

		if (!shouldDraw())
			return false;

		if (direction == Direction.LEFT) {
			offsetX -= Game.delta / 128f;
			if (offsetX <= -64)
				offsetX = 0;
		} else if (direction == Direction.RIGHT) {
			offsetX += Game.delta / 128f;
			if (offsetX >= 0)
				offsetX = -64;
		} else if (direction == Direction.UP) {
			offsetY -= Game.delta / 128f;
			if (offsetY <= -64)
				offsetY = 0;
		} else if (direction == Direction.DOWN) {
			offsetY += Game.delta / 128f;
			if (offsetY >= 0)
				offsetY = -64;
		}

//		int offset = Game.delta % maxOffset;

		int actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels + offsetX);
		int actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels + offsetY);
		float alpha = 1.0f;

		if (primaryAnimation != null)
			alpha = primaryAnimation.alpha;
		if (!this.squareGameObjectIsOn.visibleToPlayer)
			alpha = 0.5f;

		float boundsX1 = this.squareGameObjectIsOn.xInGridPixels;
		float boundsY1 = this.squareGameObjectIsOn.yInGridPixels;
		float boundsX2 = this.squareGameObjectIsOn.xInGridPixels + Game.SQUARE_WIDTH;
		float boundsY2 = this.squareGameObjectIsOn.yInGridPixels + Game.SQUARE_HEIGHT;

		float scaleX = 1;
		float scaleY = 1;

		Color color = Level.dayTimeOverlayColor;
		if (this.squareGameObjectIsOn.structureSquareIsIn != null)
			color = StructureRoom.roomColor;
		color = calculateColor(color);
		drawGameObject(actorPositionXInPixels, actorPositionYInPixels, width, height, halfWidth, halfHeight, alpha,
				flash || this == Game.gameObjectMouseIsOver
						|| (Game.gameObjectMouseIsOver != null
								&& Game.gameObjectMouseIsOver.gameObjectsToHighlight.contains(this)),
				scaleX, scaleY, 0f, boundsX1, boundsY1, boundsX2, boundsY2, color, true, imageTexture);
		return true;
	}

	@Override
	public void squareContentsChanged() {

		if (squareGameObjectIsOn == null)
			return;

		for (final GameObject gameObject : (ArrayList<GameObject>) squareGameObjectIsOn.inventory.gameObjects.clone()) {

			if (gameObject == this || gameObject.isFloorObject)
				continue;

			if (gameObject.primaryAnimation != null && gameObject.primaryAnimation.completed == false) {
				gameObject.primaryAnimation.onCompletionListener = this;
			} else {
				doTheThing(gameObject);
			}
		}

	}

	public void doTheThing(final GameObject gameObject) {

		if (squareGameObjectIsOn == null || gameObject == null || connectedSquare == null)
			return;

//		new ActionMove(gameObject, connectedSquare, false, true);

//		new ActionTeleport(ConveyerBelt.this, gameObject, connectedSquare, true, true, false).perform();

//		AnimationStraightLine(GameObject performer, float time, boolean blockAI, double delay,
//				OnCompletionListener onCompletionListener, Square... targetSquares) {

		gameObject.setPrimaryAnimation(new AnimationStraightLine(gameObject, 2f, false, 0f, null, connectedSquare));

	}

	@Override
	public Action getDefaultActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true, true);
	}

	@Override
	public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {
		return new ActionMove(performer, squareGameObjectIsOn, true, true);
	}

	@Override
	public ArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		ArrayList<Action> actions = new ArrayList<Action>(Action.class);
		actions.add(new ActionMove(performer, squareGameObjectIsOn, true, true));
		actions.addAll(super.getAllActionsPerformedOnThisInWorld(performer));
		return actions;
	}

	@Override
	public void animationComplete(GameObject gameObject) {
//		if (gameObject == this)
//			this.continueAnimation();
//		else
		doTheThing(gameObject);
	}

}
