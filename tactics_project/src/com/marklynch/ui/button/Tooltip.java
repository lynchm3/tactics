package com.marklynch.ui.button;

import java.util.ArrayList;
import java.util.Arrays;

import com.marklynch.Game;
import com.marklynch.utils.Color;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;

public class Tooltip {

	public StringWithColor textWithColor;
	// public int textWidth;
	public static final int wrapWidth = 200;
	LevelButton levelButton;
	float[] dimensions;
	ArrayList<Object> text;

	public Tooltip(LevelButton button, Object... text) {
		this.text = new ArrayList<Object>(Arrays.asList(text));
		System.out.println("this.text.size() a = " + this.text.size());
		this.levelButton = button;
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
		System.out.println("dimensions[0] a = " + dimensions[0]);
		System.out.println("dimensions[1] a = " + dimensions[1]);
	}

	public Tooltip(Object[] text, LevelButton button) {
		this.text = new ArrayList<Object>(Arrays.asList(text));
		System.out.println("this.text.size() b = " + this.text.size());
		this.levelButton = button;
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
	}

	public Tooltip(LevelButton button, ArrayList<Object> text) {
		this.text = text;
		System.out.println("this.text.size() c = " + this.text.size());
		this.levelButton = button;
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
		System.out.println("dimensions[0] a = " + dimensions[0]);
		System.out.println("dimensions[1] a = " + dimensions[1]);
	}

	public void drawStaticUI() {

		// if (1 == 1)
		// return;

		if (levelButton != null) {

			float x1 = 0;
			float x2 = 0;
			float y1 = 0;
			float y2 = 0;

			if (levelButton.realX <= Game.halfWindowWidth && levelButton.realY <= Game.halfWindowHeight) {
				// top left quadrant
				x1 = levelButton.realX + levelButton.width + 10;
				x2 = x1 + dimensions[0];
				y1 = levelButton.realY;
				y2 = y1 + dimensions[1];

			} else if (levelButton.realX <= Game.halfWindowWidth && levelButton.realY > Game.halfWindowHeight) {
				// bottom left quadrant
				x1 = levelButton.realX + levelButton.width + 10;
				x2 = x1 + dimensions[0];
				y2 = levelButton.realY + levelButton.height;
				y1 = y2 - dimensions[1];

			} else if (levelButton.realX > Game.halfWindowWidth && levelButton.realY <= Game.halfWindowHeight) {
				// bottom right
				x2 = levelButton.realX + -10;
				x1 = x2 - dimensions[0];
				y1 = levelButton.realY;
				y2 = y1 + dimensions[1];

			} else if (levelButton.realX > Game.halfWindowWidth && levelButton.realY > Game.halfWindowHeight) {
				// top right
				x2 = levelButton.realX + -10;
				x1 = x2 - dimensions[0];
				y2 = levelButton.realY + levelButton.height;
				y1 = y2 - dimensions[1];

			}

			// textWidth

			QuadUtils.drawQuad(Color.BLACK, x1, y1, x2, y2);

			TextUtils.printTextWithImages(this.text, x1, y1, wrapWidth, true, null);

		}
	}

}
