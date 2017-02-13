package com.marklynch.editor.settingswindow;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.ai.routines.AIRoutineFreeze;
import com.marklynch.ai.routines.AIRoutineMoveToSquare;
import com.marklynch.ai.routines.AIRoutineStationary;
import com.marklynch.ai.routines.AIRoutineTargetObject;
import com.marklynch.ai.utils.AIRoutineUtils;
import com.marklynch.editor.AttributesDialog;
import com.marklynch.editor.ClassSelectionWindow;
import com.marklynch.editor.Editor;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.SettingsWindowButton;

public class AIsSettingsWindow extends SettingsWindow {

	public AIsSettingsWindow(float width, final Editor editor) {
		super(width, editor);
		updateAIsButtons();
	}

	public void updateAIsButtons() {
		buttons.clear();

		final SettingsWindowButton addAIButton = new SettingsWindowButton(0, 100, 200, 30, "ADD AI", true, true) {

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

		addAIButton.clickListener = new ClickListener() {

			@Override
			public void click() {

				ArrayList<Class> classes = new ArrayList<Class>();
				classes.add(AIRoutineTargetObject.class);
				classes.add(AIRoutineStationary.class);
				classes.add(AIRoutineFreeze.class);
				classes.add(AIRoutineMoveToSquare.class);

				editor.classSelectionWindow = new ClassSelectionWindow(classes, editor, AIRoutineUtils.class,
						"Select an AI routine");
			}
		};
		buttons.add(addAIButton);

		int count = 0;
		for (int i = 0; i < Game.level.ais.size(); i++) {

			final int index = count;

			final SettingsWindowButton aiButton = new SettingsWindowButton(0, 200 + i * 30, 200, 30,
					Game.level.ais.get(i), true, true) {

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

			aiButton.clickListener = new ClickListener() {

				@Override
				public void click() {
					editor.clearSelectedObject();
					editor.depressButtonsSettingsAndDetailsButtons();
					aiButton.down = true;
					editor.attributesWindow = new AttributesDialog(200, 200, 200, Game.level.ais.get(index), editor);

				}
			};
			buttons.add(aiButton);

			count++;

		}

	}

	@Override
	public void update() {
		updateAIsButtons();

	}
}
