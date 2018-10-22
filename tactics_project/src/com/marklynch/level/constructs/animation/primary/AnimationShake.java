package com.marklynch.level.constructs.animation.primary;

import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.objects.GameObject;

public class AnimationShake extends Animation {

	public float baseOffsetX = 0;
	public float baseOffsetY = 0;

	// for show only, walking actor, primary

	public AnimationShake(GameObject performer) {
		super(performer, performer);
		if (!runAnimation)
			return;
		// durationToReach = 200;
		blockAI = false;

	}

	@Override
	public void update(double delta) {
		offsetX = (float) (Math.random() * 16) - 8f;
		offsetY = (float) (Math.random() * 16) - 8f;
		// keyFrameUpdate(delta);
	}

	// @Override
	// public void update(double delta) {
	//
	// if (getCompleted())
	// return;
	// super.update(delta);
	//
	// offsetX = (float) (Math.random() * 16) - 8f;
	// offsetY = (float) (Math.random() * 16) - 8f;
	//
	// }

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
		// TODO Auto-generated method stub

	}

}
