package com.marklynch.level.constructs.animation.primary;

import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;

public class AnimationFlinch extends Animation {

	public Square targetSquare;

	float quarterDurationToReach;
	float halfDurationToReach;
	float threeQuarterDurationToReach;

	public AnimationFlinch(GameObject performer, Square squareBeingAttackedFrom, Animation oldAnimation) {
		super(performer, performer);
		if (!runAnimation)
			return;
		this.targetSquare = squareBeingAttackedFrom;
		durationToReachMillis = 400;

		quarterDurationToReach = durationToReachMillis / 4;
		halfDurationToReach = quarterDurationToReach + quarterDurationToReach;
		threeQuarterDurationToReach = halfDurationToReach + quarterDurationToReach;

		float down = 0.5f;
		float up = -0.5f;
		float right = -0.5f;
		float left = 0.5f;

		if (squareBeingAttackedFrom.yInGrid - performer.squareGameObjectIsOn.yInGrid < 0) {
			targetRadians = up;

		} else if (squareBeingAttackedFrom.yInGrid - performer.squareGameObjectIsOn.yInGrid > 0) {
			targetRadians = down;

		}
		if (squareBeingAttackedFrom.xInGrid - performer.squareGameObjectIsOn.xInGrid < 0) {
			targetRadians = left;
			performer.backwards = true;

		} else if (squareBeingAttackedFrom.xInGrid - performer.squareGameObjectIsOn.xInGrid > 0) {
			targetRadians = right;
			performer.backwards = false;
		}
		backwards = performer.backwards;

		blockAI = true;
	}

	float targetRadians = 0;

	@Override
	public void update(double delta) {

		if (getCompleted()) {
			return;
		}

		super.update(delta);
		durationSoFar += delta;

		float progress = durationSoFar / durationToReachMillis;

		if (progress >= 1) {
			progress = 1;
		}

		float angleChange = (float) (0.02d * delta);

		torsoAngle = moveTowardsTargetAngleInRadians(torsoAngle, angleChange, targetRadians);
		leftElbowAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);
		rightElbowAngle = moveTowardsTargetAngleInRadians(rightElbowAngle, angleChange, 0);
		leftShoulderAngle = moveTowardsTargetAngleInRadians(leftShoulderAngle, angleChange, -targetRadians);
		rightShoulderAngle = moveTowardsTargetAngleInRadians(rightShoulderAngle, angleChange, -targetRadians);
		leftHipAngle = moveTowardsTargetAngleInRadians(leftHipAngle, angleChange, -targetRadians);
		rightHipAngle = moveTowardsTargetAngleInRadians(rightHipAngle, angleChange, -targetRadians);
		leftKneeAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);
		rightKneeAngle = moveTowardsTargetAngleInRadians(leftElbowAngle, angleChange, 0);

		if (progress >= 1) {
			runCompletionAlgorightm(true);
			// if (performer.getPrimaryAnimation() == this)
			// performer.setPrimaryAnimation(new AnimationWait(performer));
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

	@Override
	public void draw3() {
		// TODO Auto-generated method stub

	}

}
