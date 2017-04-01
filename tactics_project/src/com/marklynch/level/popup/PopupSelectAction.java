package com.marklynch.level.popup;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.Square;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.actions.ActionableInWorld;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;

public class PopupSelectAction extends Popup {
	public PopupButton selectSquareButton;
	public ActionableInWorld object;

	public PopupSelectAction(float width, Level level, Square square, ActionableInWorld object) {

		super(width, level, square);
		this.object = object;
		updateActionsButtons();
	}

	public void updateActionsButtons() {

		buttons.clear();

		final ArrayList<Action> actions = object.getAllActionsPerformedOnThisInWorld(Game.level.player);

		for (int i = 0; i < actions.size(); i++) {
			final int index = i;

			final PopupActionButton actionButton = new PopupActionButton(200, buttons.size() * 30, 200, 30, null, null,
					actions.get(i).actionName, true, true, actions.get(i), this);
			actionButton.enabled = actions.get(index).enabled;

			actionButton.clickListener = new ClickListener() {

				@Override
				public void click() {
					if (actionButton.enabled) {
						for (Button button : buttons) {
							button.down = false;
						}
						highlightedButton.down = true;
						actions.get(index).perform();
						for (Popup popup : Game.level.popups) {
							for (Button button : popup.buttons) {
								button.removeHighlight();
							}
						}
						Game.level.popups.clear();
					}
				}
			};
			buttons.add(actionButton);

		}

		highlightedButton = buttons.get(highlightedButtonIndex);
		highlightedButton.highlight();

	}

}
