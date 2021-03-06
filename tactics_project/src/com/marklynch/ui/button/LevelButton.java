package com.marklynch.ui.button;

import com.marklynch.Game;
import com.marklynch.utils.Color;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;

public class LevelButton extends Button {

	public boolean xFromLeft;
	public boolean yFromTop;
	public Color buttonColor;
	public Color textColor;
	public Object[] textParts;
	public float realX = x;
	public float realY = y;

	public LevelButton(float x, float y, float width, float height, String enabledTexturePath,
			String disabledTexturePath, String text, boolean xFromLeft, boolean yFromTop, Color buttonColor,
			Color textColor, String tooltipText) {
		super(x, y, width, height, enabledTexturePath, disabledTexturePath, text, tooltipText);
		this.xFromLeft = xFromLeft;
		this.yFromTop = yFromTop;
		this.buttonColor = buttonColor;
		this.textColor = textColor;
		this.textParts = new Object[] { new StringWithColor(text, textColor) };

		// if (tooltipText != null)
		// this.tooltip = new Tooltip(false, tooltipText);

		realX = x;
		if (this.xFromLeft == false)
			realX = Game.windowWidth - x;

		realY = y;
		if (this.yFromTop == false)
			realY = Game.windowHeight - y;
	}

	@Override
	public void draw() {

		Color buttonColorToUse = this.buttonColor;
		if (this == Game.buttonMouseIsOver) {
			buttonColorToUse = Color.BLUE;
		}

		realX = x;
		if (this.xFromLeft == false)
			realX = Game.windowWidth - x;

		realY = y;
		if (this.yFromTop == false)
			realY = Game.windowHeight - y;

		if (enabled) {
			if (down) {
				QuadUtils.drawQuad(Color.DARK_GRAY, realX, realY, realX + width, realY + height);
				TextUtils.printTextWithImages(realX, realY, Integer.MAX_VALUE, true, null, Color.WHITE, 1f,
						this.textParts);
			} else {
				QuadUtils.drawQuad(buttonColorToUse, realX, realY, realX + width, realY + height);
				TextUtils.printTextWithImages(realX, realY, Integer.MAX_VALUE, true, null, Color.WHITE, 1f,
						this.textParts);
			}
		} else {

			QuadUtils.drawQuad(Color.LIGHT_GRAY, realX, realY, realX + width, realY + height);
			TextUtils.printTextWithImages(realX, realY, Integer.MAX_VALUE, true, null, Color.WHITE, 1f, this.textParts);
		}

	}

	@Override
	public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {

		float realX = x;
		float realY = y;

		// System.out.println("x = " + x);
		// System.out.println("y = " + y);
		// System.out.println("mouseX = " + mouseX);
		// System.out.println("mouseY = " + mouseY);

		if (this.xFromLeft == false)
			realX = Game.windowWidth - x;
		if (this.yFromTop == false)
			realY = Game.windowHeight - y;

		if (mouseX > realX && mouseX < realX + width && mouseY > realY && mouseY < realY + height) {
			return true;
		}
		return false;
	}

	public void setTextColor(Color color) {
		this.textColor = color;
		this.textParts = new Object[] { new StringWithColor(text.toString(), textColor) };
	}

	public void updatePosition(float x, float y) {

		this.x = x;
		this.y = y;

		realX = x;
		if (this.xFromLeft == false)
			realX = Game.windowWidth - x;

		realY = y;
		if (this.yFromTop == false)
			realY = Game.windowHeight - y;

	}

}
