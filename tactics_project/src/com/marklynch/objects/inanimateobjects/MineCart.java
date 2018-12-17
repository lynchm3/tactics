package com.marklynch.objects.inanimateobjects;

import com.marklynch.actions.ActionUsePower;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.animation.primary.AnimationStraightLine;
import com.marklynch.level.constructs.power.PowerTelekineticPush;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.Direction;
import com.marklynch.objects.utils.UpdatableGameObject;
import com.marklynch.utils.ArrayList;

public class MineCart extends GameObject implements UpdatableGameObject {

	public static final ArrayList<GameObject> instances = new ArrayList<GameObject>(GameObject.class);
	public Direction direction;

	public MineCart() {
		super();
		canBePickedUp = false;
		fitsInInventory = false;
		orderingOnGound = 120;
		moveable = false;
		type = "Mine Cart";
	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	public MineCart makeCopy(Square square, Actor owner, Direction direction) {
		MineCart mineCart = new MineCart();
		setInstances(mineCart);
		super.setAttributesForCopy(mineCart, square, owner);
		mineCart.direction = direction;
		return mineCart;
	}

	@Override
	public void update(int delta) {
		if (squareGameObjectIsOn == null)
			return;

		ArrayList<SquareAndDirection> squaresForAnimation = new ArrayList<SquareAndDirection>(SquareAndDirection.class);

		SquareAndDirection currentSquareAndDirection = new SquareAndDirection();
		currentSquareAndDirection.direction = this.direction;
		currentSquareAndDirection.square = this.squareGameObjectIsOn;
		for (int i = 0; i < 4; i++) {
			currentSquareAndDirection = move(currentSquareAndDirection);
			if (currentSquareAndDirection != null && currentSquareAndDirection.square != null
					&& currentSquareAndDirection.direction != null
					&& currentSquareAndDirection.square != this.squareGameObjectIsOn) {
				squaresForAnimation.add(currentSquareAndDirection);
			} else {
				break;
			}
		}

		if (squaresForAnimation.size() > 0) {
			final Square[] arrayOfSquaresToMoveTo = new Square[squaresForAnimation.size()];
			for (int i = 0; i < squaresForAnimation.size(); i++) {
				arrayOfSquaresToMoveTo[i] = squaresForAnimation.get(i).square;
			}

			this.direction = squaresForAnimation.get(squaresForAnimation.size() - 1).direction;
			for (final GameObject gameObject : (ArrayList<GameObject>) this.squareGameObjectIsOn.inventory.gameObjects
					.clone()) {

				if (gameObject.isFloorObject)
					continue;

				if (gameObject.lastTurnThisWasMovedByMinecart == Level.turn)
					continue;

				gameObject.lastTurnThisWasMovedByMinecart = Level.turn;

				gameObject.setPrimaryAnimation(
						new AnimationStraightLine(gameObject, 2f, false, 0f, null, arrayOfSquaresToMoveTo) {

							@Override
							public void initiateNextKeyFrame() {
								super.initiateNextKeyFrame();

								for (GameObject gameObject : (ArrayList<GameObject>) this.targetSquares[phase].inventory.gameObjects
										.clone()) {
									new ActionUsePower(performer, gameObject, this.targetSquares[phase],
											new PowerTelekineticPush(performer)).perform();
								}
							}
						});
			}
		}
	}

	public class SquareAndDirection {
		public Square square;
		public Direction direction;
	}

	public SquareAndDirection move(SquareAndDirection oldSquareAndDirection) {
		SquareAndDirection newSquareAndDirection = new SquareAndDirection();

		Rail currentRail = (Rail) oldSquareAndDirection.square.inventory.getGameObjectOfClass(Rail.class);

		if (currentRail == null)
			return newSquareAndDirection;

		if (oldSquareAndDirection.direction == Direction.RIGHT) {
			newSquareAndDirection.direction = currentRail.getOppositeDirection(Direction.LEFT);
		} else if (oldSquareAndDirection.direction == Direction.LEFT) {
			newSquareAndDirection.direction = currentRail.getOppositeDirection(Direction.RIGHT);
		} else if (oldSquareAndDirection.direction == Direction.UP) {
			newSquareAndDirection.direction = currentRail.getOppositeDirection(Direction.DOWN);
		} else if (oldSquareAndDirection.direction == Direction.DOWN) {
			newSquareAndDirection.direction = currentRail.getOppositeDirection(Direction.UP);
		}

		if (newSquareAndDirection.direction == null)
			return newSquareAndDirection;

		newSquareAndDirection.square = getSquareToMoveTo(newSquareAndDirection.direction, oldSquareAndDirection.square);

		if (newSquareAndDirection.square == null)
			return null;

		Rail railToMoveTo = (Rail) newSquareAndDirection.square.inventory.getGameObjectOfClass(Rail.class);

		if (railToMoveTo == null) {
			return null;
		} else if (newSquareAndDirection.direction == Direction.RIGHT && railToMoveTo.direction1 != Direction.LEFT
				&& railToMoveTo.direction2 != Direction.LEFT) {
			return null;
		} else if (newSquareAndDirection.direction == Direction.LEFT && railToMoveTo.direction1 != Direction.RIGHT
				&& railToMoveTo.direction2 != Direction.RIGHT) {
			return null;
		} else if (newSquareAndDirection.direction == Direction.UP && railToMoveTo.direction1 != Direction.DOWN
				&& railToMoveTo.direction2 != Direction.DOWN) {
			return null;
		} else if (newSquareAndDirection.direction == Direction.DOWN && railToMoveTo.direction1 != Direction.UP
				&& railToMoveTo.direction2 != Direction.UP) {
			return null;
		}

		return newSquareAndDirection;
	}

	public Square getSquareToMoveTo(Direction direction, Square currentSquare) {

		if (direction == Direction.RIGHT) {
			return currentSquare.getSquareToRightOf();
		} else if (direction == Direction.LEFT) {
			return currentSquare.getSquareToLeftOf();
		} else if (direction == Direction.UP) {
			return currentSquare.getSquareAbove();
		} else if (direction == Direction.DOWN) {
			return currentSquare.getSquareBelow();
		}
		return null;
	}

}
