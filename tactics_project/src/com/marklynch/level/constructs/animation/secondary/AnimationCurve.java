package com.marklynch.level.constructs.animation.secondary;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.utils.TextureUtils;
import com.marklynch.utils.Utils.Point;

public class AnimationCurve extends Animation {

	public String name;
	float x, y, speed;
	float angleInRadians = 0;
	float distanceToCoverX, distanceToCoverY, distanceCoveredX, distanceCoveredY;
	GameObject projectileObject;
	float rotationSpeed = 0;
	float hypotanusLength;
	Point focalPoint;
	float startAngle, targetAngle;
	// float currentAngle = 0;

	// SOH... Sine: sin(θ) = Opposite / Hypotenuse
	// ...CAH... Cosine: cos(θ) = Adjacent / Hypotenuse
	// ...TOA Tangent: tan(θ) = Opposite / Adjacent

	public AnimationCurve(GameObject projectileObject, float speed, Point focalPoint, float angleChange) {

		super(null, projectileObject);
		if (!runAnimation)
			return;

		this.projectileObject = projectileObject;
		this.speed = speed;
		this.focalPoint = focalPoint;
		this.startAngle = this.angleInRadians = getAngle(focalPoint,
				new Point(projectileObject.squareGameObjectIsOn.xInGridPixels + Game.HALF_SQUARE_WIDTH,
						projectileObject.squareGameObjectIsOn.yInGridPixels + Game.HALF_SQUARE_HEIGHT));
		this.targetAngle = angleInRadians + angleChange;
		this.x = projectileObject.squareGameObjectIsOn.xInGridPixels
				+ (Game.SQUARE_WIDTH * projectileObject.drawOffsetRatioX);// shooter.getCenterX();
		this.y = projectileObject.squareGameObjectIsOn.yInGridPixels
				+ (Game.SQUARE_WIDTH * projectileObject.drawOffsetRatioY);// shooter.getCenterY();
		this.hypotanusLength = (float) Math.hypot(Math.abs((x + projectileObject.halfWidth) - focalPoint.x),
				Math.abs((y + projectileObject.halfHeight) - focalPoint.y));
		System.out.println("this.hypotanusLength = " + this.hypotanusLength);

		blockAI = true;

		projectileObject.squareGameObjectIsOn.inventory.remove(projectileObject);

	}

	public float getAngle(Point center, Point target) {
		float angle = (float) Math.atan2(target.y - center.y, target.x - center.x);

		return angle;
	}

	@Override
	public void update(double delta) {

		if (getCompleted())
			return;

		if (targetAngle > angleInRadians)
			angleInRadians += (float) (speed * delta);
		else
			angleInRadians -= (float) (speed * delta);

		System.out.println("angle = " + angleInRadians);

		if (Math.abs(targetAngle - angleInRadians) < 0.05f) {
			runCompletionAlgorightm();
		} else {
			x = (float) (focalPoint.x + this.hypotanusLength * Math.cos(angleInRadians)) - Game.HALF_SQUARE_WIDTH
					+ (Game.SQUARE_WIDTH * projectileObject.drawOffsetRatioX);
			y = (float) (focalPoint.y + this.hypotanusLength * Math.sin(angleInRadians)) - Game.HALF_SQUARE_HEIGHT
					+ (Game.SQUARE_WIDTH * projectileObject.drawOffsetRatioY);

			System.out.println("x = " + x);
			System.out.println("y = " + y);
			// runCompletionAlgorightm();

			// SOH... Sine: sin(θ) = Opposite / Hypotenuse
			// ...CAH... Cosine: cos(θ) = Adjacent / Hypotenuse

			// runCompletionAlgorightm();

			// Hypotenuse*
			// cos(θ)
			// =
			// Adjacent

			// Hypotenuse
			// *
			// sin(θ)
			// =
			// Opposite
		}
	}

	@Override
	public String toString() {
		return "AnimationThrown";
	}

	@Override
	public void draw2() {
	}

	@Override
	public void draw1() {
	}

	@Override
	public void drawStaticUI() {
	}

	public static void postRangedAnimation(GameObject projectileObject, Square... targetSquares) {
		postRangedAnimation(projectileObject, targetSquares, false);
	}

	public static void postRangedAnimation(GameObject projectileObject, Square[] targetSquares, boolean doesNothing) {

		targetSquares[targetSquares.length - 1].inventory.add(projectileObject);

		if (Level.player.inventory.groundDisplay != null)
			Level.player.inventory.groundDisplay.refreshGameObjects();
	}

	@Override
	public void draw3() {

		if (getCompleted())
			return;

		// Game.flush();
		// Matrix4f view = Game.activeBatch.getViewMatrix();
		// view.translate(new Vector2f(x + 64, y + 64));
		// view.rotate(angleInRadians, new Vector3f(0f, 0f, 1f));
		// view.translate(new Vector2f(-(x + 64), -(y + 64)));
		// Game.activeBatch.updateUniforms();

		TextureUtils.drawTexture(projectileObject.imageTexture, 1.0f, x, y, x + projectileObject.width,
				y + projectileObject.height, projectileObject.backwards);

		// Game.flush();
		// view.translate(new Vector2f(x + 64, y + 64));
		// view.rotate(-angleInRadians, new Vector3f(0f, 0f, 1f));
		// view.translate(new Vector2f(-(x + 64), -(y + 64)));
		// Game.activeBatch.updateUniforms();

	}
}