package com.marklynch.ui.popups;

import java.util.concurrent.CopyOnWriteArrayList;

import com.marklynch.Game;
import com.marklynch.actions.Action;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.inventory.InventorySquare;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Player;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;

public class PopupMenuSelectAction extends PopupMenu {
	public PopupMenuButton selectSquareButton;
	public CopyOnWriteArrayList<Action> actions;
	public float offsetX;
	public float height = 40f;

	public PopupMenuSelectAction(float offsetX, float width, Level level, Square square, CopyOnWriteArrayList<Action> actions) {

		super(width, level, square);
		this.actions = actions;
		this.offsetX = offsetX;
		updateActionsButtons();
	}

	public void updateActionsButtons() {

		buttons.clear();

		for (int i = 0; i < actions.size(); i++) {
			final int index = i;

			final PopupMenuActionButton actionButton = new PopupMenuActionButton(offsetX, buttons.size() * height - 10,
					width, height, null, null, actions.get(i).actionName, true, true, actions.get(i), this);
			actionButton.enabled = actions.get(index).enabled;

			actionButton.clickListener = new ClickListener() {

				@Override
				public void click() {
					if (actionButton.enabled) {
						for (Button button : buttons) {
							button.down = false;
						}
						highlightedButton.down = true;
						Action action = actions.get(index);
						Game.level.popupMenuObjects.clear();
						Game.level.popupMenuActions.clear();

						if (!(square instanceof InventorySquare) && !action.checkRange()) {
							if (Game.level.settingFollowPlayer && Game.level.player.onScreen()) {
								Game.level.cameraFollow = true;
							}
							Player.playerTargetAction = action;
							// Player.playerTargetSquare = square;
							Player.playerFirstMove = true;
							return;
						} else {
							action.perform();
						}
					}
				}
			};
			buttons.add(actionButton);

		}

		if (buttons.size() > 0) {
			highlightedButton = buttons.get(highlightedButtonIndex);
			highlightedButton.highlight();
		}

	}

}
