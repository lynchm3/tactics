package com.marklynch.level.constructs.animation.primary;

import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.KeyFrame;
import com.marklynch.objects.inanimateobjects.GameObject;

public class AnimationHandsUp extends Animation {

	float startLeftArmAngle = 0f;
	float startRightArmAngle = -0f;
	float targetLeftArmAngle = 3.14f;
	float targetRightArmAngle = -3.14f;

	public AnimationHandsUp(GameObject performer, float durationToReachMillis,
			OnCompletionListener onCompletionListener) {

		super(performer, onCompletionListener, null, null, null, null, null, null, false, true);
		if (!runAnimation)
			return;
		startLeftArmAngle = this.leftShoulderAngle;
		startRightArmAngle = this.rightShoulderAngle;
		blockAI = true;

		KeyFrame kf0 = new KeyFrame(performer, this);
		kf0.setAllSpeeds(0.004f);
		kf0.leftShoulderAngle = targetLeftArmAngle;
		kf0.rightShoulderAngle = targetRightArmAngle;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void drawStaticUI() {
	}

	@Override
	public void draw3() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void animationSubclassRunCompletionAlgorightm(boolean wait) {
		// TODO Auto-generated method stub

	}
}
