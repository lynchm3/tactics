package com.marklynch.level.constructs.effect;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import com.marklynch.Game;
import com.marklynch.objects.GameObject;
import com.marklynch.utils.TextureUtils;

import com.marklynch.utils.Texture;

public abstract class Effect {

	public String logString;
	public String effectName;
	public GameObject source;
	public GameObject target;
	public int totalTurns;
	public int turnsRemaining;
	public Texture imageTexture;

	public abstract void activate();

	public abstract Effect makeCopy(GameObject source, GameObject target);

	public void draw2() {
		// Draw object

		if (!target.squareGameObjectIsOn.visibleToPlayer)
			return;

		if (target.squareGameObjectIsOn != null) {

			int actorPositionXInPixels = (int) (target.squareGameObjectIsOn.xInGridPixels
					+ Game.SQUARE_WIDTH * target.drawOffsetRatioX);
			int actorPositionYInPixels = (int) (target.squareGameObjectIsOn.yInGridPixels
					+ Game.SQUARE_HEIGHT * target.drawOffsetRatioY);
			if (target != null) {
				actorPositionXInPixels += target.primaryAnimation.offsetX;
				actorPositionYInPixels += target.primaryAnimation.offsetY;
			}

			float alpha = 1.0f;

			// TextureUtils.skipNormals = true;

			// if (!target.squareGameObjectIsOn.visibleToPlayer)
			// alpha = 0.5f;
			// if (target.hiding)
			alpha = 0.75f;

			TextureUtils.drawTexture(imageTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
					actorPositionXInPixels + target.width, actorPositionYInPixels + target.height, target.backwards);
			// TextureUtils.skipNormals = false;
		}
	}

	public static void loadEffectImages() {
		getGlobalImage("effect_bleed.png");
		getGlobalImage("effect_burn.png");
		getGlobalImage("effect_poison.png");
		getGlobalImage("effect_wet.png");
	}

	// @Override
	// public void draw2() {
	// // Draw object
	// if (target.squareGameObjectIsOn != null) {
	//
	// int actorPositionXInPixels = (int) (target.squareGameObjectIsOn.xInGrid *
	// (int) Game.SQUARE_WIDTH
	// + target.drawOffsetX);
	// int actorPositionYInPixels = (int) (target.squareGameObjectIsOn.yInGrid *
	// (int) Game.SQUARE_HEIGHT
	// + target.drawOffsetY);
	//
	// float alpha = 1.0f;
	//
	// // TextureUtils.skipNormals = true;
	//
	// if (!target.squareGameObjectIsOn.visibleToPlayer)
	// alpha = 0.5f;
	// if (target.hiding)
	// alpha = 0.5f;
	//
	// TextureUtils.drawTexture(imageTexture, alpha, actorPositionXInPixels,
	// actorPositionXInPixels + target.width,
	// actorPositionYInPixels, actorPositionYInPixels + target.height,
	// target.backwards);
	// // TextureUtils.skipNormals = false;
	// }
	// }
}
