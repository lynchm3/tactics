package com.marklynch;

import static com.marklynch.utils.Resources.getGlobalImage;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import com.marklynch.tactics.objects.level.Square;
import com.marklynch.utils.Resources;

public class Dialog {

	public Square reference;
	public float width;
	public float height;

	// background
	public String backgroundImagePath = "";
	public Texture backgroundImageTexture = null;

	// font
	TrueTypeFont font;

	public Dialog(Square reference, float width, float height,
			String backgroundImagePath, String fontPath) {
		super();
		this.reference = reference;
		this.width = width;
		this.height = height;
		this.backgroundImagePath = backgroundImagePath;
		this.backgroundImageTexture = getGlobalImage(backgroundImagePath);
		font = Resources.getGlobalFont(fontPath);
	}

	public void draw() {
		int positionYInPixels = (int) ((reference.y + 1) * Game.zoom
				* Game.SQUARE_HEIGHT - (Game.windowHeight * Game.zoom) / 2
				+ Game.dragY * Game.zoom + Game.windowHeight / 2);

		int positionXInPixels = (int) ((reference.x + 1) * Game.zoom
				* Game.SQUARE_WIDTH - (Game.windowWidth * Game.zoom) / 2
				+ Game.dragX * Game.zoom + Game.windowWidth / 2);

		// background
		// this.backgroundImageTexture.bind();

		// set the color of the quad (R,G,B,A)
		// GL11.glColor3f(0.5f, 0.5f, 1.0f);

		this.backgroundImageTexture.bind();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(positionXInPixels, positionYInPixels);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(positionXInPixels + width, positionYInPixels);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(positionXInPixels + width, positionYInPixels + height);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(positionXInPixels, positionYInPixels + height);
		GL11.glEnd();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		String[] strings = reference.getDetails();

		int i = 0;
		for (String string : strings) {
			font.drawString(positionXInPixels + 10, positionYInPixels + 20 + i,
					string, Color.black);
			i += 20;
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);

	}
}
