package com.marklynch.level.constructs.animation.primary;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;

public class AnimationPushed extends Animation {

	public GameObject performer;
	public Square startSquare;
	public Square endSquare;
	public float startOffsetX = 0;
	public float startOffsetY = 0;

	float quarterDurationToReach;
	float halfDurationToReach;
	float threeQuarterDurationToReach;

	// Animation previousAnimation;

	// for show only, walking actor, primary

	public AnimationPushed(GameObject performer, Square startSquare, Square endSquare, Animation previousAnimation) {
		super();
		this.startSquare = startSquare;
		this.endSquare = endSquare;

		if (previousAnimation != null) {

			torsoAngle = previousAnimation.torsoAngle;

			leftShoulderAngle = previousAnimation.leftShoulderAngle;
			rightShoulderAngle = previousAnimation.rightShoulderAngle;
			leftElbowAngle = previousAnimation.leftElbowAngle;
			rightElbowAngle = previousAnimation.rightElbowAngle;

			leftHipAngle = previousAnimation.leftHipAngle;
			rightHipAngle = previousAnimation.rightHipAngle;
			leftKneeAngle = previousAnimation.leftKneeAngle;
			rightKneeAngle = previousAnimation.rightKneeAngle;

		}

		float distance = (float) Math.hypot(this.startSquare.xInGrid - this.endSquare.xInGrid,
				this.startSquare.yInGrid - this.endSquare.yInGrid);

		durationToReach = distance * 500;

		quarterDurationToReach = durationToReach / 4;
		halfDurationToReach = quarterDurationToReach + quarterDurationToReach;
		threeQuarterDurationToReach = halfDurationToReach + quarterDurationToReach;

		startOffsetX = offsetX = (int) ((this.startSquare.xInGrid - this.endSquare.xInGrid) * Game.SQUARE_WIDTH);
		startOffsetY = offsetY = (int) ((this.startSquare.yInGrid - this.endSquare.yInGrid) * Game.SQUARE_HEIGHT);

		backwards = performer.backwards;
		// -180 to +180 = -3.14 to 3.14

		// targetLimbRadians = (float) Math.atan2(endSquare.yInGrid -
		// startSquare.yInGrid,
		// endSquare.xInGrid - startSquare.xInGrid) + 1.5708f;

		// if (endSquare.yInGrid - startSquare.yInGrid < 0) {
		// targetLimbRadians = -targetLimbRadians;
		// }
		// if (endSquare.xInGrid - startSquare.xInGrid < 0) {
		// targetLimbRadians = -targetLimbRadians;
		// }
		//
		// while (targetLimbRadians > 3.14f) {
		// targetLimbRadians -= 3.14f;
		// }
		//
		// while (targetLimbRadians < -3.14f) {
		// targetLimbRadians += 3.14f;
		// }

		float up = 0f;
		float down = 3.14f;
		float left = -1.57f;
		float right = 1.57f;

		if (endSquare.yInGrid - startSquare.yInGrid < 0) {
			targetLimbRadians = up;
		} else if (endSquare.yInGrid - startSquare.yInGrid > 0) {
			targetLimbRadians = down;

		}
		if (endSquare.xInGrid - startSquare.xInGrid < 0) {
			targetLimbRadians = left;
		} else if (endSquare.xInGrid - startSquare.xInGrid > 0) {
			targetLimbRadians = right;
		}

		// System.out.println("deltaX = " + deltaX);
		// System.out.println("deltaY = " + deltaY);
		System.out.println("targetLimbDegrees = " + targetLimbRadians);

		blockAI = true;

		if (blockAI)
			Level.blockingAnimations.add(this);
	}

	float targetLimbRadians = 0;

	@Override
	public void update(double delta) {

		if (completed)
			return;

		durationSoFar += delta;

		float progress = durationSoFar / durationToReach;

		if (progress >= 1) {
			progress = 1;
		}

		float angleChange = (float) (0.010d * delta);

		torsoAngle = moveTowardsTargetAngleInRadians(torsoAngle, angleChange, targetLimbRadians);

		leftShoulderAngle = moveTowardsTargetAngleInRadians(leftShoulderAngle, angleChange, 0);
		rightShoulderAngle = moveTowardsTargetAngleInRadians(rightShoulderAngle, angleChange, 0);
		leftElbowAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);
		rightElbowAngle = moveTowardsTargetAngleInRadians(rightElbowAngle, angleChange, 0);

		leftHipAngle = moveTowardsTargetAngleInRadians(leftHipAngle, angleChange, 0);
		rightHipAngle = moveTowardsTargetAngleInRadians(rightHipAngle, angleChange, 0);
		leftKneeAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);
		rightKneeAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);

		if (progress >= 1) {
			completed = true;
			if (blockAI)
				Level.blockingAnimations.remove(this);
			offsetX = 0;
			offsetY = 0f;
		} else {
			offsetX = (int) (startOffsetX * (1 - progress));
			offsetY = (int) (startOffsetY * (1 - progress));
		}
	}

	@Override
	public void draw2() {

	}

	@Override
	public void draw1() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawStaticUI() {
	}

}