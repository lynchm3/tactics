package com.marklynch.tactics.objects.level.script;

import java.util.Vector;

import com.marklynch.tactics.objects.level.Level;
import com.marklynch.tactics.objects.unit.Actor;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

public class SpeechPart {

	public Vector<Actor> actors;
	public Vector<Float> positions;
	public Vector<DIRECTION> directions;
	public Actor talker;
	public Object[] text;
	public Level level;

	public enum DIRECTION {
		LEFT, RIGHT
	}

	public SpeechPart(Vector<Actor> actors, Vector<Float> positions,
			Vector<DIRECTION> directions, Actor talker, Object[] text,
			Level level) {
		super();
		this.actors = actors;
		this.positions = positions;
		this.directions = directions;
		this.talker = talker;
		this.text = text;
		this.level = level;
	}

	public void draw() {

		for (int i = 0; i < actors.size(); i++) {
			TextureUtils.drawTextureBackwards(actors.get(i).imageTexture, 1.0f,
					positions.get(i), positions.get(i) + 128, 0, 0 + 128);
		}

		// TextureUtils.drawTexture(talker.imageTexture, 0, 0, 128, 128);
		TextUtils.printTextWithImages(text, 150, 0, level);
	}
}
