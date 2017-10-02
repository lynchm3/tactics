package com.marklynch.ui.button;

import com.marklynch.Game;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;

import mdesl.graphics.Color;

public class Tooltip {

	public StringWithColor textWithColor;
	// public int textWidth;
	public static final int wrapWidth = 200;
	LevelButton levelButton;
	float[] dimensions;

	public Tooltip(String text, LevelButton button) {
		textWithColor = new StringWithColor(text, Color.BLACK);
		// textWidth = Game.font.getWidth(text);
		this.levelButton = button;
		dimensions = TextUtils.getDimensions(wrapWidth, this.textWithColor.string);
	}

	public void drawStaticUI() {

		if (levelButton != null) {

			float x1 = 0;
			float x2 = 0;
			float y1 = 0;
			float y2 = 0;

			if (levelButton.realX <= Game.halfWindowWidth && levelButton.realY <= Game.halfWindowHeight) {
				// top left quadrant
				x1 = levelButton.realX + levelButton.width + 10;
				x2 = x1 + dimensions[0];
				y2 = levelButton.realY + levelButton.height;
				y1 = y2 - dimensions[1];
				;

			} else if (levelButton.realX <= Game.halfWindowWidth && levelButton.realY > Game.halfWindowHeight) {
				// bottom left quadrant
				x1 = levelButton.realX + levelButton.width + 10;
				x2 = x1 + dimensions[0];
				y1 = levelButton.realY;
				y2 = y1 + dimensions[1];
				;

			} else if (levelButton.realX > Game.halfWindowWidth && levelButton.realY <= Game.halfWindowHeight) {
				// top right
				x2 = levelButton.realX + -10;
				x1 = x2 - dimensions[0];
				y2 = levelButton.realY + levelButton.height;
				y1 = y2 - dimensions[1];
				;

			} else if (levelButton.realX > Game.halfWindowWidth && levelButton.realY > Game.halfWindowHeight) {
				// bottom right
				x2 = levelButton.realX + -10;
				x1 = x2 - dimensions[0];
				y1 = levelButton.realY;
				y2 = y1 + dimensions[1];

			}

			// textWidth

			QuadUtils.drawQuad(Color.WHITE, x1, x2, y1, y2);

			TextUtils.printTextWithImages(x1, y1, wrapWidth, true, null, this.textWithColor);

		}
	}

}
