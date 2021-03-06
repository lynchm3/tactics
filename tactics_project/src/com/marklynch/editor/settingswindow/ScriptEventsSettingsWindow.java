package com.marklynch.editor.settingswindow;

import java.util.concurrent.CopyOnWriteArrayList;

import com.marklynch.editor.ClassSelectionWindow;
import com.marklynch.editor.Editor;
import com.marklynch.script.ScriptEvent;
import com.marklynch.script.ScriptEventEndLevel;
import com.marklynch.script.ScriptEventGroup;
import com.marklynch.script.ScriptEventInlineSpeech;
import com.marklynch.script.ScriptEventSetAI;
import com.marklynch.script.ScriptEventSpeech;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.SettingsWindowButton;

public class ScriptEventsSettingsWindow extends SettingsWindow {

	public ScriptEventsSettingsWindow(float width, final Editor editor) {
		super(width, editor);
		updateScriptsButtons();
	}

	public void updateScriptsButtons() {
		buttons.clear();

		final SettingsWindowButton addScriptButton = new SettingsWindowButton(0, 100, 200, 30, "ADD SCRIPT EVENT", true,
				true) {

			@Override
			public void keyTyped(char character) {
			}

			@Override
			public void enterTyped() {
			}

			@Override
			public void backTyped() {
			}

			@Override
			public void depress() {
			}

		};

		addScriptButton.clickListener = new ClickListener() {

			@Override
			public void click() {

				CopyOnWriteArrayList<Class> classes = new CopyOnWriteArrayList<Class>();
				classes.add(ScriptEventEndLevel.class);
				classes.add(ScriptEventGroup.class);
				classes.add(ScriptEventInlineSpeech.class);
				classes.add(ScriptEventSetAI.class);
				classes.add(ScriptEventSpeech.class);

				editor.classSelectionWindow = new ClassSelectionWindow(classes, editor, ScriptEvent.class,
						"Select a Script Event Type");
			}
		};
		buttons.add(addScriptButton);

	}

	@Override
	public void update() {
		updateScriptsButtons();

	}
}
