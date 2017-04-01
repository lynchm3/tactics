package com.marklynch.ui.button;

import com.marklynch.utils.ResourceUtils;

import mdesl.graphics.Texture;

public abstract class Button {

	public transient Texture enabledTexture;
	public transient Texture disabledTexture;
	public float x, y, width, height;
	public boolean enabled = true;
	public boolean down = false;
	public ClickListener clickListener;
	public Object text;
	protected boolean highlighted = false;

	public Button(float x, float y, float width, float height, String enabledTexturePath, String disabledTexturePath,
			Object text) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.enabledTexture = ResourceUtils.getGlobalImage(enabledTexturePath);
		this.disabledTexture = ResourceUtils.getGlobalImage(disabledTexturePath);
		this.text = text;
	}

	public void setClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public boolean calculateIfPointInBoundsOfButton(float mouseX, float mouseY) {
		if (mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) {
			return true;
		}
		return false;
	}

	public void click() {
		if (clickListener != null && enabled)
			clickListener.click();
	}

	public abstract void draw();

	public abstract void drawWithinBounds(float boundsX1, float boundsX2, float boundsY1, float boundsY2);

	public void highlight() {
		System.out.println("Button.highlight()");
		highlighted = true;

	}

	public void removeHighlight() {
		System.out.println("Button.removeHighlight()");
		highlighted = false;

	}

}
