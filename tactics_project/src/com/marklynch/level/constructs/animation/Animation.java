package com.marklynch.level.constructs.animation;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.animation.primary.AnimationDie;
import com.marklynch.level.constructs.animation.primary.AnimationWait;
import com.marklynch.level.constructs.animation.primary.AnimationWalk;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Player;

public abstract class Animation {
	public boolean blockAI = false;
	// public boolean blockPlayer = false;
	private boolean completed = false;
	public float offsetX = 0;
	public float offsetY = 0;

	public float torsoAngle = 0f;
	public float leftShoulderAngle = 0f;
	public float leftElbowAngle = 0f;
	public float rightShoulderAngle = 0f;
	public float rightElbowAngle = 0f;

	public float scaleX = 1;
	public float scaleY = 1;

	// leg stuff
	public float leftHipAngle = 0;
	public float leftKneeAngle = 0;
	public float rightHipAngle = 0;
	public float rightKneeAngle = 0;

	public float alpha = 1f;

	public float durationSoFar = 0;
	public float durationToReachMillis = 200;
	public int phase = 0;

	public abstract void draw1();

	public abstract void draw2();

	public abstract void draw3();

	public abstract void drawStaticUI();

	protected boolean backwards = true;
	public boolean drawEquipped = true;

	// Arrow
	public boolean drawArrowInOffHand = false;
	public boolean drawArrowInMainHand = false;
	public float arrowHandleY = 0;

	// Bow string
	public boolean drawBowString = false;
	public float bowStringHandleY = 0;

	public int boundsX1 = -128;
	public int boundsY1 = -128;
	public int boundsX2 = 128;
	public int boundsY2 = 128;
	public boolean drawWeapon = true;

	public GameObject performer;

	public boolean runAnimation = false;

	public Animation(GameObject performer, Object... objectsInvolved) {

		runAnimation = Game.level.shouldLog(objectsInvolved, false);
		if (!runAnimation) {
			runCompletionAlgorightm();
			return;
		}

		this.performer = performer;

		if (performer != null && performer.getPrimaryAnimation() != null) {

			this.offsetX = performer.getPrimaryAnimation().offsetX;
			this.offsetY = performer.getPrimaryAnimation().offsetY;

			this.torsoAngle = performer.getPrimaryAnimation().torsoAngle;

			this.leftShoulderAngle = performer.getPrimaryAnimation().leftShoulderAngle;
			this.rightShoulderAngle = performer.getPrimaryAnimation().rightShoulderAngle;
			this.leftElbowAngle = performer.getPrimaryAnimation().leftElbowAngle;
			this.rightElbowAngle = performer.getPrimaryAnimation().rightElbowAngle;

			this.leftHipAngle = performer.getPrimaryAnimation().leftHipAngle;
			this.rightHipAngle = performer.getPrimaryAnimation().rightHipAngle;
			this.leftKneeAngle = performer.getPrimaryAnimation().leftKneeAngle;
			this.rightKneeAngle = performer.getPrimaryAnimation().rightKneeAngle;

			this.alpha = performer.getPrimaryAnimation().alpha;
		}
	}

	public void update(double delta) {
		float alphaChange = (float) (0.002d * delta);
		float targetAlpha = 1f;
		if (performer != null && performer.hiding) {
			targetAlpha = 0.5f;
		}
		alpha = moveTowardsTargetAngleInRadians(alpha, alphaChange, targetAlpha);
	}

	protected void reverseAnimation() {

		torsoAngle = -torsoAngle;

		float temp = rightShoulderAngle;
		rightShoulderAngle = -leftShoulderAngle;
		leftShoulderAngle = -temp;

		temp = rightElbowAngle;
		rightElbowAngle = -leftElbowAngle;
		leftElbowAngle = -temp;

		temp = rightHipAngle;
		rightHipAngle = -leftHipAngle;
		leftHipAngle = -temp;

		temp = rightKneeAngle;
		rightKneeAngle = -leftKneeAngle;
		leftKneeAngle = -temp;

	}

	protected float moveTowardsTargetAngleInRadians(float angleToChange, float angleChange, float targetAngle) {
		if (angleToChange == targetAngle) {

			// } else if (Math.abs(angleToChange) < angleChange) {
			// angleToChange = targetAngle;
		} else if (angleToChange > targetAngle) {

			angleToChange -= angleChange;
		} else if (angleToChange < targetAngle) {
			angleToChange += angleChange;
		}

		if (Math.abs(angleToChange - targetAngle) < angleChange) {
			angleToChange = targetAngle;
		}

		return angleToChange;

	}

	public void runCompletionAlgorightm() {

		Level.blockingAnimations.remove(this);

		Level.animations.remove(this);

		completed = true;

		if (performer == null)
			return;

		if (!(performer instanceof Actor))
			return;

		if (this != performer.getPrimaryAnimation())
			return;

		if (this instanceof AnimationWait || this instanceof AnimationDie)
			return;

		// Make player do wait animation at end of walk.
		if (performer == Level.player && Player.playerPathToMove == null && Player.playerTargetSquare == null
				&& Player.playerTargetAction == null && Player.playerTargetActor == null) {
			performer.setPrimaryAnimation(new AnimationWait(performer));
			return;
		}

		if (this instanceof AnimationWalk)
			return;

		if (performer.remainingHealth > 0) {
			performer.setPrimaryAnimation(new AnimationWait(performer));
		}
		// else
		// performer.setPrimaryAnimation(new AnimationDie(performer));

		// if (!(this instanceof AnimationWalk)) {
		//
		// } else
	}

	public boolean getCompleted() {
		return completed;
	}

}
