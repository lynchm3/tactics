package com.marklynch.script;

import java.util.concurrent.CopyOnWriteArrayList;

import com.marklynch.Game;
import com.marklynch.script.trigger.ScriptTrigger;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.TextUtils;

import com.marklynch.utils.Color;

public class ScriptEventEndLevel extends ScriptEvent {

	public final static String[] editableAttributes = { "name", "blockUserInput", "scriptTrigger", "text" };

	public CopyOnWriteArrayList<String> text = new CopyOnWriteArrayList<String>();
	public boolean completed = false;

	public ScriptEventEndLevel() {
		name = "ScriptEventEndLevel";
	}

	public ScriptEventEndLevel(boolean blockUserInput, ScriptTrigger scriptTrigger, CopyOnWriteArrayList<String> text) {
		super(blockUserInput, scriptTrigger);
		name = "ScriptEventEndLevel";
		this.text = text;
	}

	@Override
	public boolean checkIfCompleted() {
		return completed;
	}

	@Override
	public void click() {
		Game.level.end();
		completed = true;
	}

	@Override
	public void update(int delta) {

	}

	@Override
	public void draw() {

		QuadUtils.drawQuad(new Color(1.0f, 1.0f, 1.0f, 0.5f), 0f, 0f, Game.windowWidth, Game.windowHeight);

		float posY = Game.windowHeight / 2f;

		float textX1 = 300;
		float textX2 = Game.windowWidth - 300;
		float width = textX2 - textX1;
		if (width <= 100)
			width = 100;

		// TextureUtils.drawTexture(talker.imageTexture, 0, 0, 128, 128);
		TextUtils.printTextWithImages(textX1, posY, width, true, null, Color.WHITE, 1f, text);
	}

	@Override
	public void postLoad() {
		scriptTrigger.postLoad();
	}
}
