package com.marklynch.level.constructs.animation.secondary;

import com.marklynch.Game;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.Food;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Tree;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.units.Actor;

public class AnimationDrop extends Animation {

	public Square targetSquare;
	float originX, originY, targetX, targetY;
	public float startOffsetX = 0;
	public float startOffsetY = 0;

	// Transfer object from person to sqr, for show only, used in drop, primary

	public AnimationDrop(String name, GameObject shooter, Action action, Square targetSquare,
			GameObject projectileObject, float speed) {

		super(null);

		if (shooter == Game.level.player) {
			name = "Your " + name;
		} else {
			name = shooter.name + "'s " + name;
		}

		if (shooter instanceof Actor) {
			Actor shooterActor = (Actor) shooter;
			originX = (int) (shooter.squareGameObjectIsOn.xInGridPixels + shooter.drawOffsetRatioX * Game.SQUARE_WIDTH
					+ shooterActor.rightArmHingeX - projectileObject.anchorX);
			originY = (int) (shooter.squareGameObjectIsOn.yInGridPixels + shooter.drawOffsetRatioY * Game.SQUARE_HEIGHT
					+ shooterActor.handY - projectileObject.anchorY);
		} else if (shooter instanceof Tree && projectileObject instanceof Food) {
			Food fruit = (Food) projectileObject;
			originX = (int) (shooter.squareGameObjectIsOn.xInGridPixels + fruit.drawOffsetRatioX * Game.SQUARE_WIDTH);
			originY = (int) (shooter.squareGameObjectIsOn.yInGridPixels + fruit.drawOffsetYInTree * Game.SQUARE_HEIGHT);
		} else {
			originX = (int) (shooter.squareGameObjectIsOn.xInGridPixels
					+ (Game.SQUARE_WIDTH - projectileObject.width) / 2);
			originY = (int) (shooter.squareGameObjectIsOn.yInGridPixels
					+ (Game.SQUARE_HEIGHT - projectileObject.height) / 2);
		}

		targetX = (int) (targetSquare.xInGridPixels + Game.SQUARE_WIDTH * projectileObject.drawOffsetRatioX);
		targetY = (int) (targetSquare.yInGridPixels + Game.SQUARE_HEIGHT * projectileObject.drawOffsetRatioY);

		startOffsetX = offsetX = originX - targetX;
		startOffsetY = offsetY = originY - targetY;

		blockAI = false;
	}

	@Override
	public void update(double delta) {

		if (getCompleted())
			return;

		durationSoFar += delta;
		double progress = durationSoFar / durationToReach;
		if (progress >= 1) {
			runCompletionAlgorightm();
			offsetX = 0;
			offsetY = 0;
		} else {
			offsetX = (int) (startOffsetX * (1 - progress));
			offsetY = (int) (startOffsetY * (1 - progress));
		}

	}

	@Override
	public void draw2() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw1() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawStaticUI() {
	}

	// @Override
	// public void update(double delta) {
	//
	// if (completed)
	// return;
	//
	// float distanceX = (float) (speedX * delta);
	// float distanceY = (float) (speedY * delta);
	//
	// angle += rotationSpeed * delta;
	//
	// distanceCoveredX += distanceX;
	// distanceCoveredY += distanceY;
	//
	// if (Math.abs(distanceCoveredX) >= Math.abs(distanceToCoverX)
	// && Math.abs(distanceCoveredY) >= Math.abs(distanceToCoverY)) {
	// completed();
	// } else {
	// x += distanceX;
	// y += distanceY;
	// }
	// }

	// public void completed() {
	// completed = true;
	//
	// // receiver.inventory.add(object);
	// if (targetSquare.inventory.contains(Searchable.class)) {
	// Searchable searchable = (Searchable)
	// targetSquare.inventory.getGameObjectOfClass(Searchable.class);
	// searchable.inventory.add(projectileObject);
	// } else {
	// Game.level.inanimateObjectsToAdd.add(new
	// InanimateObjectToAddOrRemove(projectileObject, targetSquare));
	// }
	//
	// if (Game.level.player.inventory.groundDisplay != null)
	// Game.level.player.inventory.groundDisplay.refreshGameObjects();
	// }

	// @Override
	// public void draw() {
	// float alpha = 1.0f;
	//
	// float radians = (float) Math.toRadians(angle);
	// Game.flush();
	// Matrix4f view = Game.activeBatch.getViewMatrix();
	// view.translate(new Vector2f(x, y));
	// view.rotate(radians, new Vector3f(0f, 0f, 1f));
	// Game.activeBatch.updateUniforms();
	//
	// TextureUtils.drawTexture(projectileObject.imageTexture, alpha, 0, 0, 0 +
	// projectileObject.width,
	// 0 + projectileObject.height);
	//
	// Game.flush();
	// view.rotate(-radians, new Vector3f(0f, 0f, 1f));
	// view.translate(new Vector2f(-x, -y));
	// Game.activeBatch.updateUniforms();
	// }
}
