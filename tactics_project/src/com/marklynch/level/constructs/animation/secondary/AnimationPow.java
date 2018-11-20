package com.marklynch.level.constructs.animation.secondary;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.marklynch.Game;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.KeyFrame;
import com.marklynch.objects.GameObject;
import com.marklynch.utils.TextureUtils;

public class AnimationPow extends Animation {

	float startScale, endScale;

	public AnimationPow(GameObject performer, float start, float end, float durationToReachMillis,
			OnCompletionListener onCompletionListener) {

		super(performer, onCompletionListener, null, null, null, null, null, null, false, performer);
		if (!runAnimation)
			return;
		blockAI = false;
		this.durationToReachMillis = durationToReachMillis;

		scaleX = start;
		scaleY = start;

		KeyFrame kf0 = new KeyFrame(performer, this);
		kf0.scaleX = end;
		kf0.scaleY = end;
		kf0.keyFrameTimeMillis = this.durationToReachMillis;
		kf0.normaliseSpeeds = true;
		keyFrames.add(kf0);

	}

	@Override
	public void update(double delta) {
		keyFrameUpdate(delta);
	}

	@Override
	public void draw2() {

		if (performer.squareGameObjectIsOn == null)
			return;

		float powPositionXInPixels = performer.squareGameObjectIsOn.xInGridPixels;
		float powPositionYInPixels = performer.squareGameObjectIsOn.yInGridPixels;
		if (performer.getPrimaryAnimation() != null) {
			powPositionXInPixels += performer.getPrimaryAnimation().offsetX;
			powPositionYInPixels += performer.getPrimaryAnimation().offsetY;
		}

		Matrix4f view = Game.activeBatch.getViewMatrix();

		if (scaleX != 1 || scaleY != 1) {
			Game.flush();
			view.translate(new Vector2f(powPositionXInPixels + Game.HALF_SQUARE_WIDTH,
					powPositionYInPixels + Game.HALF_SQUARE_HEIGHT));
			view.scale(new Vector3f(scaleX, scaleY, 1f));
			view.translate(new Vector2f(-(powPositionXInPixels + Game.HALF_SQUARE_WIDTH),
					-(powPositionYInPixels + Game.HALF_SQUARE_HEIGHT)));
			Game.activeBatch.updateUniforms();
		}

		TextureUtils.drawTexture(GameObject.powTexture, powPositionXInPixels, powPositionYInPixels,
				powPositionXInPixels + Game.SQUARE_WIDTH, powPositionYInPixels + Game.SQUARE_HEIGHT);

		if (scaleX != 1 || scaleY != 1) {
			Game.flush();
			// Matrix4f view = Game.activeBatch.getViewMatrix();
			view.translate(new Vector2f(powPositionXInPixels + Game.HALF_SQUARE_WIDTH,
					powPositionYInPixels + Game.HALF_SQUARE_HEIGHT));
			view.scale(new Vector3f(1f / scaleX, 1f / scaleY, 1f));
			view.translate(new Vector2f(-(powPositionXInPixels + Game.HALF_SQUARE_WIDTH),
					-(powPositionYInPixels + Game.HALF_SQUARE_HEIGHT)));
			Game.activeBatch.updateUniforms();
		}
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

	@Override
	protected void childRunCompletionAlgorightm(boolean wait) {
		// TODO Auto-generated method stub

	}
}
