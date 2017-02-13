package com.marklynch.level.conversation;

import java.util.ArrayList;
import java.util.Vector;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.Square;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.LevelButton;

public class WindowSelectConversationResponse {

	public float width;
	public Vector<LevelButton> buttons = new Vector<LevelButton>();
	public Level level;
	public Square square;
	public LevelButton selectSquareButton;
	public float drawPositionX, drawPositionY;

	Button highlightedButton;
	int highlightedButtonIndex = 0;

	ArrayList<ConversationResponse> conversationResponses;

	public WindowSelectConversationResponse(float width, Level level,
			ArrayList<ConversationResponse> conversationResponses) {

		this.conversationResponses = conversationResponses;
		updateObjectsButtons();

	}

	public void updateObjectsButtons() {

		buttons.clear();

		float height = 100;
		float bottomMargin = 50;
		float conversationTop = 150;

		float buttonHeight = 30;

		float positionOfTopButton = conversationTop + conversationResponses.size() * buttonHeight;

		for (int i = 0; i < conversationResponses.size(); i++) {
			final int index = i;

			// The line and the highlight are drawn in relation to zoom and
			// position...

			// BUT... I dont want the buttons to zoom :P

			final LevelButton responseButton = new LevelButton(200, positionOfTopButton - i * 30, Game.halfWindowWidth,
					buttonHeight, null, null, "" + conversationResponses.get(i).text, false, false);

			responseButton.clickListener = new ClickListener() {

				@Override
				public void click() {

					ConversationResponse conversationResponse = conversationResponses.get(index);
					ConversationPart nextConversationPart = conversationResponse.nextConversationPart;
					if (nextConversationPart == null) {
						Game.level.conversation = null;
					} else {
						Game.level.conversation.currentConversationPart = nextConversationPart;
					}
				}
			};
			buttons.add(responseButton);

		}

		highlightedButton = buttons.get(highlightedButtonIndex);
		highlightedButton.highlighted = true;

	}

	public void draw() {
		for (LevelButton button : buttons) {
			button.draw();
		}

	}
}
