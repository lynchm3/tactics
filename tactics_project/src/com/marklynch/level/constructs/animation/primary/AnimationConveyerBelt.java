package com.marklynch.level.constructs.animation.primary;

import com.marklynch.Game;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.KeyFrame;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.GameObject;

public class AnimationConveyerBelt extends Animation {

	public String name;
	float x, y;
	float angle = 0;
	float rotationSpeed = 0;
	// Square[] targetSquares;

	public AnimationConveyerBelt(GameObject performer, float time, boolean blockAI, double delay,
			OnCompletionListener onCompletionListener, Square... targetSquares) {

		super(performer, onCompletionListener, targetSquares, null, null, null, null, null, false, true, performer,
				targetSquares[targetSquares.length - 1]);
		if (!runAnimation) {
			return;
		}

		this.targetSquares = targetSquares;

		this.x = performer.squareGameObjectIsOn.xInGridPixels;// shooter.getCenterX();
		this.y = performer.squareGameObjectIsOn.yInGridPixels;// shooter.getCenterY();

		float keyFrameTimeMillis = time;

		this.blockAI = blockAI;
		if (!blockAI) {

			keyFrameTimeMillis = Game.MINIMUM_TURN_TIME / targetSquares.length;
		}

		// if (performer instanceof Arrow && distanceToCoverX < 0) {
		// performer.backwards = true;
		// }

		for (int i = 0; i < targetSquares.length; i++) {

			KeyFrame kf0 = new KeyFrame(performer, this);
			kf0.setAllSpeeds(1);
			kf0.offsetX = this.targetSquares[i].xInGridPixels - this.x;
			kf0.offsetY = this.targetSquares[i].yInGridPixels - this.y;
			kf0.keyFrameTimeMillis = keyFrameTimeMillis;
			kf0.normaliseSpeeds = true;
			keyFrames.add(kf0);
		}
	}

	@Override
	public void update(double delta) {
		super.keyFrameUpdate(delta);

	}

	@Override
	public void initiateNextKeyFrame() {
		keyFrames.get(keyFrameIndex).createSpeeds();
	}

	@Override
	public String toString() {
		return "AnimationStraightLine";
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
	public void animationSubclassRunCompletionAlgorightm(boolean wait) {
		postRangedAnimation();
	}

	public void postRangedAnimation() {
		if (performer != null) {
			this.offsetX = 0;
			this.offsetY = 0;
			targetSquares[targetSquares.length - 1].inventory.add(performer);
		}
	}

	@Override
	public void draw3() {
		if (getCompleted())
			return;
	}
}
