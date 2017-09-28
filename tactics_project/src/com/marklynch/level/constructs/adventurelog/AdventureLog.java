package com.marklynch.level.constructs.adventurelog;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.quest.Quest;
import com.marklynch.ui.Draggable;
import com.marklynch.ui.Scrollable;
import com.marklynch.ui.button.Button;
import com.marklynch.ui.button.ClickListener;
import com.marklynch.ui.button.LevelButton;
import com.marklynch.utils.QuadUtils;
import com.marklynch.utils.StringWithColor;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

import mdesl.graphics.Color;

public class AdventureLog implements Draggable, Scrollable {

	public boolean showing = false;

	public static Quest questToDisplayInAdventureLog = null;
	public static Quest activeQuest = null;

	int listX;
	int listY;
	int listBorder;
	int listWidth;

	int contentX;
	int contentY;
	int contentBorder;

	int listItemHeight;

	transient static int bottomBorderHeight;

	public static ArrayList<LevelButton> buttons = new ArrayList<LevelButton>();
	public static ArrayList<LevelButton> buttonsToMakeQuestAcive = new ArrayList<LevelButton>();
	public static ArrayList<LevelButton> buttonsToDisplayQuest = new ArrayList<LevelButton>();
	public static ArrayList<LevelButton> links = new ArrayList<LevelButton>();
	// Close button
	static LevelButton buttonClose;

	public AdventureLog() {
		resize();

		buttonClose = new LevelButton(Game.halfWindowWidth - 25f, bottomBorderHeight, 70f, 30f, "end_turn_button.png",
				"end_turn_button.png", "CLOSE [N]", true, false, Color.BLACK, Color.WHITE);
		buttonClose.setClickListener(new ClickListener() {
			@Override
			public void click() {
				Game.level.openCloseAdventureLog();
			}
		});
		buttons.add(buttonClose);
	}

	public void resize() {
		listX = 0;
		listY = 0;
		listBorder = 16;
		listWidth = 300;

		contentX = listX + listWidth + listBorder * 2;
		contentY = 0;
		contentBorder = 16;

		listItemHeight = 30;

		bottomBorderHeight = 384;

	}

	public void open() {
		// Sort quests by active, keep selectedQuest at top
		// And also sort by completed...
		resize();
		showing = true;

		buttons.clear();
		buttonsToMakeQuestAcive.clear();
		buttonsToDisplayQuest.clear();
		buttons.add(buttonClose);

		int questsDrawnInList = 0;
		for (final Quest quest : Level.quests) {
			if (quest.started) {
				if (questToDisplayInAdventureLog == null) {
					questToDisplayInAdventureLog = quest;
				}
				if (activeQuest == null) {
					activeQuest = quest;
				}

				// buttons to make quest the active one
				final LevelButton buttonToMakeQuestActive = new LevelButton(
						listX + listBorder + listWidth - listItemHeight,
						listY + listBorder + questsDrawnInList * listItemHeight, listItemHeight, listItemHeight,
						"end_turn_button.png", "end_turn_button.png", "", true, true, Color.GRAY, Color.WHITE);
				buttonToMakeQuestActive.setClickListener(new ClickListener() {

					@Override
					public void click() {
						for (LevelButton button : buttonsToMakeQuestAcive) {
							// change colors to off
							button.buttonColor = Color.GRAY;
							button.setTextColor(Color.WHITE);
						}
						buttonToMakeQuestActive.buttonColor = Color.GREEN;
						buttonToMakeQuestActive.setTextColor(Color.BLACK);
						activeQuest = quest;

					}
				});

				if (questToDisplayInAdventureLog == quest) {
					buttonToMakeQuestActive.buttonColor = Color.GREEN;
					buttonToMakeQuestActive.setTextColor(Color.BLACK);
				}

				buttons.add(buttonToMakeQuestActive);
				buttonsToMakeQuestAcive.add(buttonToMakeQuestActive);

				// Button w/ the name of the quest -

				final LevelButton buttonToShowQuestDetails = new LevelButton(listX + listBorder,
						listY + listBorder + questsDrawnInList * listItemHeight, listWidth - listItemHeight,
						listItemHeight, "end_turn_button.png", "end_turn_button.png", quest.name, true, true,
						Color.BLACK, Color.WHITE);
				buttonToShowQuestDetails.setClickListener(new ClickListener() {

					@Override
					public void click() {
						for (LevelButton button : buttonsToDisplayQuest) {
							// change colors to off
							button.buttonColor = Color.BLACK;
							button.setTextColor(Color.WHITE);
						}
						buttonToShowQuestDetails.buttonColor = Color.WHITE;
						buttonToShowQuestDetails.setTextColor(Color.BLACK);
						questToDisplayInAdventureLog = quest;

					}
				});

				if (questToDisplayInAdventureLog == quest) {
					buttonToShowQuestDetails.buttonColor = Color.WHITE;
					buttonToShowQuestDetails.setTextColor(Color.BLACK);
				}

				buttons.add(buttonToShowQuestDetails);
				buttonsToDisplayQuest.add(buttonToShowQuestDetails);
				questsDrawnInList++;

			}
		}

	}

	public void close() {
		showing = false;
	}

	public void drawStaticUI() {

		links.clear();

		// Black cover
		QuadUtils.drawQuad(Color.BLACK, 0, Game.windowWidth, 0, Game.windowHeight);

		// Content
		int questTextsDrawn = 0;
		if (questToDisplayInAdventureLog != null) {
			for (Object pieceOfInfo : questToDisplayInAdventureLog.info) {
				TextUtils.printTextWithImages(contentX + contentBorder,
						contentY + contentBorder + questTextsDrawn * listItemHeight, Integer.MAX_VALUE, true, true,
						links, new Object[] { pieceOfInfo });
				questTextsDrawn++;
			}
		}

		if (buttons.size() == 1) {
			TextUtils.printTextWithImages(0, 0, Integer.MAX_VALUE, true, false, links, new Object[] { "NO QUESTS" });
		} else {
		}

		// Buttons
		for (Button button : buttons) {
			button.draw();
		}
	}

	public void drawActiveQuestObjective() {
		if (activeQuest != null) {
			TextUtils.printTextWithImages(Game.windowWidth - Game.font.getWidth(activeQuest.name) - 150, 20,
					Integer.MAX_VALUE, false, false, null,
					new Object[] { new StringWithColor(activeQuest.name, Color.WHITE) });

			int objectivesPrinted = 0;
			for (Objective currentObjective : activeQuest.currentObjectives) {
				TextUtils.printTextWithImages(Game.windowWidth - Game.font.getWidth(currentObjective.text) - 150,
						40 + 20 * objectivesPrinted, Integer.MAX_VALUE, false, false, null,
						new Object[] { new StringWithColor(currentObjective.text, Color.WHITE) });
				objectivesPrinted++;
			}
		}
	}

	public void drawQuestMarkers() {
		if (activeQuest != null) {
			int markersDrawn = 0;
			for (Objective currentObjective : activeQuest.currentObjectives) {

				if (currentObjective.gameObject != null && currentObjective.gameObject.squareGameObjectIsOn != null) {
					currentObjective.gameObject.squareGameObjectIsOn.drawObjective(markersDrawn);
				} else if (currentObjective.square != null) {
					currentObjective.square.drawObjective(markersDrawn);
				}
				markersDrawn++;

			}
		}
	}

	public static void drawQuestLines() {
		if (activeQuest != null) {
			int x1 = (int) Game.halfWindowWidth;
			int y1 = (int) Game.halfWindowHeight;

			int x2 = Integer.MAX_VALUE;
			int y2 = Integer.MAX_VALUE;

			int markersDrawn = 0;
			for (Objective currentObjective : activeQuest.currentObjectives) {

				if (currentObjective.gameObject != null && currentObjective.gameObject.squareGameObjectIsOn != null) {

					int squareX = (currentObjective.gameObject.squareGameObjectIsOn.xInGrid * (int) Game.SQUARE_HEIGHT);
					int squareY = (currentObjective.gameObject.squareGameObjectIsOn.yInGrid * (int) Game.SQUARE_HEIGHT);
					x2 = (int) ((Game.windowWidth / 2) + (Game.zoom
							* (squareX - Game.windowWidth / 2 + Game.getDragXWithOffset() + Game.HALF_SQUARE_WIDTH)));
					y2 = (int) ((Game.windowHeight / 2) + (Game.zoom
							* (squareY - Game.windowHeight / 2 + Game.getDragYWithOffset() + Game.HALF_SQUARE_HEIGHT)));

				} else if (currentObjective.square != null) {

					int squareX = (currentObjective.square.xInGrid * (int) Game.SQUARE_HEIGHT);
					int squareY = (currentObjective.square.yInGrid * (int) Game.SQUARE_HEIGHT);
					x2 = (int) ((Game.windowWidth / 2) + (Game.zoom
							* (squareX - Game.windowWidth / 2 + Game.getDragXWithOffset() + Game.HALF_SQUARE_WIDTH)));
					y2 = (int) ((Game.windowHeight / 2) + (Game.zoom
							* (squareY - Game.windowHeight / 2 + Game.getDragYWithOffset() + Game.HALF_SQUARE_HEIGHT)));

				}

				if (x2 != Integer.MAX_VALUE) {
					// LineUtils.drawLine(Color.WHITE, x1, y1, x2, y2, 5);

					// Get intersection of line and edge of screen

					// Right edge
					int x3 = (int) Game.windowWidth;
					int x4 = (int) Game.windowWidth;
					int y3 = 0;
					int y4 = (int) Game.windowHeight;

					int[] intersect = lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4);

					if (intersect != null) {
						TextureUtils.drawTexture(Game.level.gameCursor.cursor, intersect[0] - 10, intersect[1] - 10,
								intersect[0] + 10, intersect[1] + 10);

						markersDrawn++;
						continue;
					}

					// Left edge
					x3 = 0;
					x4 = 0;
					y3 = 0;
					y4 = (int) Game.windowHeight;

					intersect = lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4);

					if (intersect != null) {
						TextureUtils.drawTexture(Game.level.gameCursor.cursor, intersect[0] - 10, intersect[1] - 10,
								intersect[0] + 10, intersect[1] + 10);

						markersDrawn++;
						continue;
					}

					// Top edge
					x3 = 0;
					x4 = (int) Game.windowWidth;
					y3 = 0;
					y4 = 0;

					intersect = lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4);

					if (intersect != null) {
						TextureUtils.drawTexture(Game.level.gameCursor.cursor, intersect[0] - 10, intersect[1] - 10,
								intersect[0] + 10, intersect[1] + 10);

						markersDrawn++;
						continue;
					}

					// Bottom edge
					x3 = 0;
					x4 = (int) Game.windowWidth;
					y3 = (int) Game.windowHeight;
					y4 = (int) Game.windowHeight;

					intersect = lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4);

					if (intersect != null) {
						TextureUtils.drawTexture(Game.level.gameCursor.cursor, intersect[0] - 10, intersect[1] - 10,
								intersect[0] + 10, intersect[1] + 10);

						markersDrawn++;
						continue;
					}

					markersDrawn++;

				}

			}
		}
	}

	public static int[] lineIntersect(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
		double denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
		double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {

			int[] result = new int[2];
			result[0] = (int) (x1 + ua * (x2 - x1));
			result[1] = (int) (y1 + ua * (y2 - y1));
			return result;
		}

		return null;
	}

	@Override
	public void scroll(float dragX, float dragY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drag(float dragX, float dragY) {
		// TODO Auto-generated method stub

	}

}