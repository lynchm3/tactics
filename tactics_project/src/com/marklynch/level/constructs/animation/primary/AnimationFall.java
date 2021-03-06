package com.marklynch.level.constructs.animation.primary;

import com.marklynch.Game;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.KeyFrame;
import com.marklynch.objects.inanimateobjects.GameObject;

public class AnimationFall extends Animation {

	float startLeftArmAngle = 0f;
	float startRightArmAngle = -0f;
	float targetLeftArmAngle = 3.14f;
	float targetRightArmAngle = -3.14f;

	public AnimationFall(GameObject performer, float start, float end, float durationToReachMillis,
			OnCompletionListener onCompletionListener) {

		super(performer, onCompletionListener, null, null, null, null, null, null, false, true, performer);

		if (!runAnimation)
			return;

		blockAI = true;

		this.durationToReachMillis = durationToReachMillis;
		startLeftArmAngle = this.leftShoulderAngle;
		startRightArmAngle = this.rightShoulderAngle;

		scaleX = start;
		scaleY = start;

		KeyFrame kf0 = new KeyFrame(performer, this);
		kf0.torsoAngle = 0;
		kf0.leftElbowAngle = 0;
		kf0.rightElbowAngle = 0;
		kf0.leftHipAngle = -0;
		kf0.rightHipAngle = -0;
		kf0.leftKneeAngle = 0;
		kf0.rightKneeAngle = 0;

		// the good stuff
		kf0.leftShoulderAngle = this.targetLeftArmAngle;
		kf0.rightShoulderAngle = this.targetRightArmAngle;
		kf0.scaleX = 0;
		kf0.scaleY = 0;
		kf0.offsetX = 100;
		kf0.offsetY = 100;

		kf0.keyFrameTimeMillis = Game.MINIMUM_TURN_TIME;
		kf0.normaliseSpeeds = true;

		keyFrames.add(kf0);

	}

	@Override
	public void update(double delta) {
		super.keyFrameUpdate(delta);
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

	@Override
	public void draw3() {

	}

	@Override
	protected void animationSubclassRunCompletionAlgorightm(boolean wait) {
		// TODO Auto-generated method stub

	}
}
