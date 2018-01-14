package com.marklynch.ui;

import com.marklynch.Game;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;

import mdesl.graphics.Color;

public class TextBox {

	public enum TYPE {
		ALL, NUMERIC
	}

	public final TYPE type;

	public float caretX;
	public float caretY;
	public boolean caretOn = true;
	public int caretTimer = 0;
	public int caretPositionIndex = 0;
	public static final int CARET_BLINK_TIME = 500;
	public float drawPositionX, drawPositionY;
	public float height = 20;
	private String text;
	public StringWithColor hintWithColor;
	public float width = 2;

	public TextBoxHolder parent;
	public int maxNumericValue = 0;

	public TextBox(TextBoxHolder parent, String text, String hint, float drawPositionX, float drawPositionY,
			TYPE type) {

		this.parent = parent;
		this.text = text;
		this.hintWithColor = new StringWithColor(hint, Color.GRAY);
		this.drawPositionX = drawPositionX;
		this.drawPositionY = drawPositionY;
		this.type = type;

		caretX = drawPositionX + 5;
		caretY = drawPositionX + 5;
		caretPositionIndex = text.length();

	}

	public void draw() {
		width = Game.windowWidth - drawPositionX;
		// Text box
		if (Game.level.activeTextBox == this)
			QuadUtils.drawQuad(Color.PINK, drawPositionX, drawPositionY, drawPositionX + width + 4,
					drawPositionY + height);
		else
			QuadUtils.drawQuad(Color.BLACK, drawPositionX, drawPositionY, drawPositionX + width + 4,
					drawPositionY + height);

		if (text.length() > 0) {
			// Text string
			TextUtils.printTextWithImages(drawPositionX, drawPositionY, Integer.MAX_VALUE, true, null,
					new Object[] { text });

		} else {
			// Text string
			TextUtils.printTextWithImages(drawPositionX, drawPositionY, Integer.MAX_VALUE, true, null,
					new Object[] { hintWithColor });

		}

		// Caret
		if (Game.level.activeTextBox == this) { // CRASH WHEN TEXT WAS CLEARED
												// BUT CARET WAS NOT
			int caretPosition = Game.font.getWidth(text, 0, caretPositionIndex);
			if (caretOn) {
				QuadUtils.drawQuad(Color.BLACK, drawPositionX + caretPosition, drawPositionY,
						drawPositionX + caretPosition + 2, drawPositionY + height);
			}
		}
	}

	public void keyTyped(char character) {

		if (type == TYPE.NUMERIC && !Character.isDigit(character)) {
			return;
		}

		int oldTextLength = text.length();

		text = text.substring(0, this.caretPositionIndex) + character
				+ text.substring(this.caretPositionIndex, text.length());

		// Max numeric value
		if (type == TYPE.NUMERIC && maxNumericValue != 0) {
			int integerValue = maxNumericValue;
			try {
				integerValue = Integer.parseInt(text);
			} catch (Exception e) {
				text = "" + maxNumericValue;
			}
			if (integerValue > maxNumericValue) {
				text = "" + maxNumericValue;
			}
		}

		int newTextLength = text.length();

		if (newTextLength > oldTextLength)
			caretPositionIndex++;

		parent.textChanged(this);
	}

	public void backSpaceTyped() {
		if (text.length() > 0 && caretPositionIndex > 0) {
			text = text.substring(0, this.caretPositionIndex - 1)
					+ text.substring(this.caretPositionIndex, text.length());
			caretPositionIndex--;
			parent.textChanged(this);
		}
	}

	public void deleteTyped() {
		if (text.length() > 0 && caretPositionIndex < text.length()) {
			text = text.substring(0, this.caretPositionIndex)
					+ text.substring(this.caretPositionIndex + 1, text.length());
			parent.textChanged(this);
		}
	}

	public void updateRealtime(int delta) {
		caretTimer += delta;
		if (caretTimer >= CARET_BLINK_TIME) {
			caretOn = !caretOn;
			caretTimer -= CARET_BLINK_TIME;
		}
	}

	public void moveCaretLeft() {
		this.moveCaretTo(caretPositionIndex - 1);
	}

	public void moveCaretRight() {
		this.moveCaretTo(caretPositionIndex + 1);
	}

	public void moveCaretToEnd() {
		moveCaretTo(text.length());
	}

	public void moveCaretTo(int newPosition) {
		if (newPosition < 0 || newPosition > text.length()) {
			return;
		}
		caretPositionIndex = newPosition;
		caretOn = true;
		caretTimer = 0;
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		if (mouseX > drawPositionX && mouseX < drawPositionX + width && mouseY > drawPositionY
				&& mouseY < drawPositionY + height) {
			return true;
		}
		return false;
	}

	public boolean click(int mouseX, int mouseY) {

		Game.level.activeTextBox = this;

		if (mouseX > drawPositionX && mouseX < drawPositionX + width && mouseY > drawPositionY
				&& mouseY < drawPositionY + height) {

			int clickIndex = 0;
			int relativeX = (int) (mouseX - drawPositionX);
			boolean found = false;
			while (clickIndex < text.length() && !found) {
				if (relativeX < Game.font.getWidth(text, 0, clickIndex)) {
					found = true;
				} else {
					clickIndex++;
				}
			}

			if (clickIndex == 0) {
				this.caretPositionIndex = clickIndex;
			} else {
				int distanceToLower = Math.abs(relativeX - Game.font.getWidth(text, 0, clickIndex - 1));
				int distanceToUpper = Math.abs(relativeX - Game.font.getWidth(text, 0, clickIndex));
				if (distanceToLower <= distanceToUpper)
					this.caretPositionIndex = clickIndex - 1;
				else
					this.caretPositionIndex = clickIndex;
			}
			caretOn = true;
			caretTimer = 0;

		}
		return false;
	}

	public void enterTyped() {
		parent.enterTyped(this);
	}

	public void clearText() {
		if (text.length() > 0) {
			text = "";
			caretPositionIndex = 0;
			parent.textChanged(this);
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		parent.textChanged(this);
	}

}
