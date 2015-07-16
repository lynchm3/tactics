package com.marklynch.editor;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.marklynch.Game;
import com.marklynch.tactics.objects.level.Faction;
import com.marklynch.tactics.objects.unit.Actor;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.SelectionWindowButton;
import com.marklynch.utils.QuadUtils;

public class SelectionWindow {

	ArrayList<?> objects;
	public ArrayList<SelectionWindowButton> buttons = new ArrayList<SelectionWindowButton>();
	public boolean multi = false;
	public Editor editor;

	public SelectionWindow(final ArrayList<?> objects, boolean multi,
			final Editor editor) {
		this.multi = multi;
		this.objects = objects;
		this.editor = editor;
		for (int i = 0; i < objects.size(); i++) {

			final int index = i;

			float x = i / ((int) Game.windowHeight / 30) * 200;
			float y = i % (Game.windowHeight / 30) * 30;

			SelectionWindowButton selectionWindowButton = new SelectionWindowButton(
					x, y, 190, 30, null, null, "", true, true, objects.get(i));
			selectionWindowButton.clickListener = new ClickListener() {

				@Override
				public void click() {

					// faction
					Actor actor = (Actor) editor.objectToEdit;
					Faction faction = (Faction) objects.get(index);
					actor.faction.actors.remove(actor);
					faction.actors.add(actor);
					actor.faction = faction;
					editor.stopEditingAttribute();

				}
			};
			buttons.add(selectionWindowButton);
		}
	}

	public void draw() {
		// faction
		QuadUtils.drawQuad(Color.black, 0, Game.windowWidth, 0,
				Game.windowHeight);
		for (SelectionWindowButton button : buttons) {
			button.draw();
		}

	}

	public Button getButtonFromMousePosition(float mouseX, float mouseY) {
		for (Button button : buttons) {
			if (button.calculateIfPointInBoundsOfButton(mouseX,
					Game.windowHeight - mouseY))
				return button;
		}
		return null;
	}
}
