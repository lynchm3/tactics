package com.marklynch.level.popup;

import com.marklynch.ui.button.Button;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.TextUtils;

import mdesl.graphics.Color;

public class PopupMenuButton extends Button {

	boolean xFromLeft;
	boolean yFromTop;
	public Object object;
	public PopupMenu popup;

	public PopupMenuButton(float x, float y, float width, float height, String enabledTexturePath,
			String disabledTexturePath, boolean xFromLeft, boolean yFromTop, Object object, PopupMenu popup, String text) {
		super(x, y, width, height, enabledTexturePath, disabledTexturePath, text);
		this.xFromLeft = xFromLeft;
		this.yFromTop = yFromTop;
		this.object = object;
		this.popup = popup;
	}

	@Override
	public void draw() {

		float realX = x + popup.drawPositionX;
		float realY = y + popup.drawPositionY;

		if (this.xFromLeft == false)
			realX = popup.drawPositionX - x;

		if (this.yFromTop == false)
			realY = popup.drawPositionY - y;

		if (enabled) {
			if (down) {
				QuadUtils.drawQuad(Color.GREEN, realX, realX + width, realY, realY + height);
				TextUtils.printTextWithImages(new Object[] { object }, realX, realY, Integer.MAX_VALUE, true);
			} else {
				if (highlighted) {
					QuadUtils.drawQuad(Color.BLACK, realX, realX + width, realY, realY + height);
					TextUtils.printTextWithImages(new Object[] { object }, realX, realY, Integer.MAX_VALUE, true);
				} else {
					QuadUtils.drawQuad(Color.DARK_GRAY, realX, realX + width, realY, realY + height);
					TextUtils.printTextWithImages(new Object[] { object }, realX, realY, Integer.MAX_VALUE, true);
				}
			}
		} else {

			QuadUtils.drawQuad(Color.LIGHT_GRAY, realX, realX + width, realY, realY + height);
			TextUtils.printTextWithImages(new Object[] { object }, realX, realY, Integer.MAX_VALUE, true);
		}

	}

	@Override
	public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {

		float realX = x + popup.drawPositionX;
		float realY = y + popup.drawPositionY;

		if (this.xFromLeft == false)
			realX = popup.drawPositionX - x;

		if (this.yFromTop == false)
			realY = popup.drawPositionY - y;

		if (mouseX > realX && mouseX < realX + width && mouseY > realY && mouseY < realY + height) {
			return true;
		}
		return false;
	}

	@Override
	public void drawWithinBounds(float boundsX1, float boundsX2, float boundsY1, float boundsY2) {
	}

}