package com.marklynch.ui.button;

import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import com.marklynch.Game;

public class TooltipGroup extends CopyOnWriteArrayList<Tooltip> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float[] toolTipGroupDimensions = new float[2];
	float alpha = 0f;

	public TooltipGroup() {
	}

	@Override
	public boolean add(Tooltip tooltip) {
		boolean result = super.add(tooltip);
		toolTipGroupDimensions[0] = Math.max(toolTipGroupDimensions[0], tooltip.dimensions[0]);
		toolTipGroupDimensions[1] = 0;

		for (Tooltip tooltipsInList : this) {
			toolTipGroupDimensions[1] += tooltipsInList.dimensions[1];
		}
		return result;
	}

	public void drawStaticUI() {

		if (Tooltip.lastTooltipGroupShown != this) {
			alpha = 0f;
		} else if (alpha < 1f) {
			// fade in
			alpha += 0.05f;
			if (alpha > 1) {
				alpha = 1;
			}
		}

		// !!! HERE !!!
		alpha = 1;

		float mouseY = Game.windowHeight - Mouse.getY();

		float y1 = 0;
		float y2 = 0;

		// System.out.println("toolTipGroupDimensions[1] = " +
		// toolTipGroupDimensions[1]);

		if (mouseY <= Game.halfWindowHeight) {
			y1 = mouseY;
			y2 = y1 + toolTipGroupDimensions[1];
		} else if (mouseY > Game.halfWindowHeight) {
			y2 = mouseY;
			y1 = y2 - toolTipGroupDimensions[1];
			// System.out.println("toolTipGroupDimensions[1]" + toolTipGroupDimensions[1]);
		}

		float offsetY = 0;
		for (Tooltip tooltip : this) {
			tooltip.drawStaticUI(y1 + offsetY, alpha);
			offsetY += tooltip.dimensions[1];
		}
	}

	public void setTooltipText(Object[] tooltipText) {
		if (tooltipText != null && this.size() > 0) {
			this.remove(0);
			this.add(new Tooltip(false, Tooltip.WHITE, tooltipText));
		}

	}

	public void setTooltipText(CopyOnWriteArrayList<Object> tooltipText) {
		if (tooltipText != null && this.size() > 0) {
			this.remove(0);
			this.add(new Tooltip(false, Tooltip.WHITE, tooltipText));
		}

	}

}