package com.marklynch.ui.button;

import java.util.ArrayList;

import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.Texture;

public abstract class Button {

	public transient Texture enabledTexture;
	public transient Texture disabledTexture;
	public float x, y, width, height;
	public boolean enabled = true;
	public boolean down = false;
	public ClickListener clickListener;
	protected Object text;
	protected boolean highlighted = false;
	public TooltipGroup tooltipGroup;

	public Button(float x, float y, float width, float height, String enabledTexturePath, String disabledTexturePath,
			Object text, String tooltipText) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.enabledTexture = ResourceUtils.getGlobalImage(enabledTexturePath, false);
		this.disabledTexture = ResourceUtils.getGlobalImage(disabledTexturePath, false);
		this.text = text;
		if (tooltipText != null) {
			tooltipGroup = new TooltipGroup();
			tooltipGroup.add(new Tooltip(false, tooltipText));
		}

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

	public void highlight() {
		highlighted = true;

	}

	public void removeHighlight() {
		highlighted = false;

	}

	public void drawTooltip() {

		System.out.println("button.drawTooltip");
		if (tooltipGroup != null)
			tooltipGroup.drawStaticUI();
	}

	public void setTooltipText(Object... tooltipText) {
		tooltipGroup.setTooltipText(tooltipText);
	}

	public void setTooltipText(ArrayList<Object> tooltipText) {
		tooltipGroup.setTooltipText(tooltipText);
	}

}
