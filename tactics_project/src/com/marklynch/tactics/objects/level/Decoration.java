package com.marklynch.tactics.objects.level;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;
import mdesl.graphics.Texture;

import com.marklynch.utils.TextureUtils;

public class Decoration {

	public String name;
	public String imagePath;
	public transient Texture imageTexture = null;
	public float x, y, width, height;
	public boolean background;

	public Decoration(String name, float x, float y, float width, float height,
			boolean background, String imagePath) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.background = background;
		this.imagePath = imagePath;
		loadImages();
	}

	public void loadImages() {
		this.imageTexture = getGlobalImage(imagePath);
	}

	public void draw() {
		// Draw object
		if (background)
			TextureUtils.drawTexture(imageTexture, x, x + width, y, y + height);
	}

	public void draw2() {
		// Draw object
		if (!background)
			TextureUtils.drawTexture(imageTexture, x, x + width, y, y + height);
	}
}
