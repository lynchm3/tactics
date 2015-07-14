package com.marklynch.utils;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import com.marklynch.tactics.objects.GameObject;
import com.marklynch.tactics.objects.level.Faction;
import com.marklynch.tactics.objects.level.Level;
import com.marklynch.tactics.objects.unit.Actor;

public class TextUtils {

	public static void printTextWithImages(Object[] contents, float posX,
			float posY, Level level) {
		printTextWithImages(contents, posX, posY, level, Float.MAX_VALUE);
	}

	public static void printTextWithImages(Object[] contents, float posX,
			float posY, Level level, float maxWidth) {

		float offsetX = 0;
		float offsetY = 0;

		for (Object content : contents) {
			if (content instanceof String || content instanceof StringWithColor) {
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				String string = null;
				Color color = Color.white;
				if (content instanceof String) {
					string = (String) content;
				} else if (content instanceof StringWithColor) {
					StringWithColor stringWithColor = (StringWithColor) content;
					string = stringWithColor.string;
					color = stringWithColor.color;
				}

				String[] stringParts = string
						.split("(?<=[\\p{Punct}\\p{Space}|\\p{Space}\\p{Punct}|\\p{Punct}|\\p{Space}])");

				for (String stringPart : stringParts) {

					float width = level.font20.getWidth(stringPart);
					if (offsetX + width > maxWidth && offsetX != 0) {
						offsetY += 20;
						offsetX = 0;
					}

					level.font20.drawString(posX + offsetX, posY + offsetY,
							stringPart, color);

					offsetX += width;

				}
			} else if (content instanceof Texture) {

				float width = 20;
				if (offsetX + width > maxWidth) {
					offsetY += 20;
					offsetX = 0;
				}
				TextureUtils.drawTexture((Texture) content, posX + offsetX,
						posX + offsetX + 20, posY + offsetY, posY + offsetY
								+ 20);
				offsetX += width;

			} else if (content instanceof GameObject) {

				GameObject gameObject = (GameObject) content;

				float textWidth = level.font20.getWidth(gameObject.name);
				float textureWidth = 20;

				float width = textWidth + textureWidth;
				if (offsetX + width > maxWidth) {
					offsetY += 20;
					offsetX = 0;
				}

				// Name
				if (gameObject instanceof Actor) {
					Actor actor = (Actor) gameObject;

					level.font20.drawString(posX + offsetX, posY + offsetY,
							gameObject.name, actor.faction.color);
				} else {
					level.font20.drawString(posX + offsetX, posY + offsetY,
							gameObject.name, Color.gray);
				}
				offsetX += textWidth;

				// Image
				float x = posX + offsetX;
				TextureUtils.drawTexture(gameObject.imageTexture, x, x + 20,
						posY + offsetY, posY + offsetY + 20);
				offsetX += textureWidth;

			} else if (content instanceof Faction) {
				Faction faction = (Faction) content;

				float textWidth = level.font20.getWidth(faction.name);
				float textureWidth = 20;

				float width = textWidth + textureWidth;
				if (offsetX + width > maxWidth) {
					offsetY += 20;
					offsetX = 0;
				}

				// Name

				level.font20.drawString(posX + offsetX, posY + offsetY,
						faction.name, faction.color);
				offsetX += textWidth;

				// Image

				float x = posX + offsetX;
				TextureUtils.drawTexture(faction.imageTexture, x, x + 20, posY
						+ offsetY, posY + offsetY + 20);
				offsetX += textureWidth;

			}
		}

	}
	// fit font to width, this could be awkward coz they're loaded w/ font sizes

	// public static void printTable(Object[][] tableContents, float posX,
	// float posY, float rowHeight, Level level) {
	//
	// QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f), posX, posX
	// + Game.SQUARE_WIDTH, posY, posY + Game.SQUARE_HEIGHT);
	//
	// int rowCount = tableContents.length;
	// int columnCount = tableContents[0].length;
	// float[] columnOffsets = new float[columnCount];
	//
	// for (int i = 1; i < columnCount; i++) {
	// // Init column width to be at least
	// // row height
	// }
	//
	// for (int i = 1; i < columnCount; i++) {
	// columnOffsets[i] = rowHeight + columnOffsets[i - 1] + 5;
	// for (int j = 0; j < rowCount; j++) {
	// if (tableContents[j][i] instanceof String) {
	// int stringWidth = level.font12
	// .getWidth((String) tableContents[j][i]);
	// if (columnOffsets[i] < stringWidth + columnOffsets[i - 1]
	// + 5) {
	// columnOffsets[i] = stringWidth + columnOffsets[i - 1]
	// + 5;
	// }
	// }
	// }
	// }
	//
	// for (int i = 0; i < rowCount; i++) {
	// for (int j = 0; j < columnCount; j++) {
	//
	// float x = columnOffsets[j] + posX;
	// float y = i * rowHeight + posY;
	// if (tableContents[i][j] instanceof String) {
	// level.font20.drawString(x, y, (String) tableContents[i][j],
	// Color.white);
	// } else if (tableContents[i][j] instanceof Texture) {
	//
	// TextureUtils.drawTexture((Texture) tableContents[i][j], x,
	// x + rowHeight, y, y + rowHeight);
	// }
	// }
	// }
	// }
}
