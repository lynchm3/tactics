package com.marklynch.ui.button;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Arrays;

import org.lwjgl.input.Mouse;

import com.marklynch.Game;
import com.marklynch.utils.Color;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;

public class Tooltip {

	public static TooltipGroup lastTooltipGroupShown = null;

	public StringWithColor textWithColor;
	// public int textWidth;
	public static final int wrapWidth = 200;
	// LevelButton levelButton;
	float[] dimensions;
	CopyOnWriteArrayList<Object> text;
	// float alpha = 0f;
	Color backgroundColor = null;
	public static final Color WHITE = new Color(1f, 1f, 1f, 0f);
	public static final Color RED = new Color(1f, 0f, 0f, 0f);
	Color textColor = new Color(0f, 0f, 0f, 0f);

	public Tooltip(boolean doesNothing, Color color, Object... text) {
		backgroundColor = color;
		this.text = new CopyOnWriteArrayList<Object>(Arrays.asList(text));
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
	}

	public Tooltip(Color color, Object[] text) {
		backgroundColor = color;
		this.text = new CopyOnWriteArrayList<Object>(Arrays.asList(text));
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
	}

	public Tooltip(Color color, CopyOnWriteArrayList<Object> text) {
		backgroundColor = color;
		this.text = text;
		dimensions = TextUtils.getDimensions(this.text, wrapWidth);
	}

	public void drawStaticUI(float y1, float alpha) {

		float mouseX = Mouse.getX();

		float x1 = 0;
		float x2 = 0;
		// float y1 = 0;
		float y2 = y1 + dimensions[1];

		if (mouseX <= Game.halfWindowWidth) {
			// top left quadrant
			x1 = mouseX + 10;
			x2 = x1 + dimensions[0];

		} else if (mouseX > Game.halfWindowWidth) {
			// top right
			x2 = mouseX + -10;
			x1 = x2 - dimensions[0];

		}

		backgroundColor.a = alpha;
		QuadUtils.drawQuad(backgroundColor, x1, y1, x2, y2);// WAT

		textColor.a = alpha;
		TextUtils.printTextWithImages(this.text, x1, y1, wrapWidth, true, textColor, 1f, null);

	}

	public void setTooltipText(Object[] tooltipText) {
		this.text = new CopyOnWriteArrayList<Object>(Arrays.asList(tooltipText));

	}

}
