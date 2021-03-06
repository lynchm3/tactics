package com.marklynch.level.constructs.conversation;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Arrays;
//import com.marklynch.utils.Color;

import com.marklynch.Game;
import com.marklynch.level.constructs.area.Area;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Human;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.ui.button.Link;
import com.marklynch.utils.Color;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

public class ConversationPart {
	public CopyOnWriteArrayList<Link> links;
	public CopyOnWriteArrayList<Link> linksForJournal;

	protected ConversationResponse[] conversationResponses;
	public Object[] text;
	// int textWidth;
	int halfTextWidth;
	public ConversationResponseDisplay windowSelectConversationResponse;
	public GameObject talker;

	public LeaveConversationListener leaveConversationListener;

	public float height;

	public CopyOnWriteArrayList<Quest> quests;
	private int turn;
	private String turnString;
	private Area area;
	private Square square;
	public CopyOnWriteArrayList<Object> squareAndText;

	public ConversationPart(Object[] text, ConversationResponse[] conversationResponses, GameObject talker,
			Quest... quests) {
		super();
		this.conversationResponses = conversationResponses;
		this.text = text;
		this.talker = talker;
		this.quests = new CopyOnWriteArrayList<Quest>(Arrays.asList(quests));
		// textWidth = Game.font.getWidth(text);
		// halfTextWidth = textWidth / 2;

		// if (conversationResponses.length > 0)
		windowSelectConversationResponse = new ConversationResponseDisplay(100, Game.level, conversationResponses,
				talker, this);

		if (text != null) {
			links = TextUtils.getLinks(text);
			height = TextUtils.getDimensions(text, Integer.MAX_VALUE)[1];

		}

	}

	public void drawStaticUI1() {

		if (talker.groupOfActors != null) {
			for (Actor actor : talker.groupOfActors.getMembers()) {

				if (actor.remainingHealth <= 0)
					continue;

				if (actor == talker)
					continue;

				int offsetX = (int) -(System.identityHashCode(actor) % Game.halfWindowHeight);// -
																								// 128f;//
																								// -64;
				int offsetY = 0;

				if (actor == Game.level.conversation.originalConversationTarget) {

				} else {

					actor.drawActor(0 + offsetX, (int) Game.halfWindowHeight + offsetY, 1f, false, 2f, 2f, 0f,
							Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
							TextureUtils.neutralColor, true, false, actor.backwards, false, false);

					// TextureUtils.drawTexture(actor.imageTexture, 1.0f, 0 + offsetX,
					// (int) Game.halfWindowHeight + offsetY, Game.halfWindowHeight + offsetX,
					// Game.windowHeight + offsetY);
				}

			}

			GameObject originalTarget = Game.level.conversation.originalConversationTarget;
			if (originalTarget != null && talker != originalTarget) {
				int offsetX = (int) -(System.identityHashCode(originalTarget) % Game.halfWindowHeight); // -64;
				int offsetY = 0;

				if (originalTarget instanceof Human) {
					((Actor) Game.level.conversation.originalConversationTarget).drawActor(0 + offsetX,
							(int) Game.halfWindowHeight + offsetY, 1f, false, 2f, 2f, 0f, Integer.MIN_VALUE,
							Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, TextureUtils.neutralColor, true,
							false, ((Actor) Game.level.conversation.originalConversationTarget).backwards, false, false);

				} else {
					TextureUtils.drawTexture(Game.level.conversation.originalConversationTarget.imageTexture,
							0 + offsetX, Game.halfWindowHeight + offsetY, Game.halfWindowHeight + offsetX,
							Game.windowHeight + offsetY, Color.RED);
				}
			}
		}

		// Speaker image
		if (talker instanceof Human) {
			((Actor) talker).drawActor(0, (int) Game.halfWindowHeight, 1f, false, 2f, 2f, 0f, Integer.MIN_VALUE,
					Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, TextureUtils.neutralColor, true, false,
					((Actor) talker).backwards, false, false);
		} else {
			TextureUtils.drawTexture(talker.imageTexture, 1.0f, 0, Game.halfWindowHeight, Game.halfWindowHeight,
					Game.windowHeight);
		}

		// Speker 2 image (player)
		Game.level.player.drawActor((int) (Game.windowWidth - Game.halfWindowHeight), (int) Game.halfWindowHeight, 1f,
				false, 2f, 2f, 0f, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
				TextureUtils.neutralColor, true, false, Game.level.player.backwards, false, false);
		// TextureUtils.drawTexture(Game.level.player.imageTexture, 1.0f,
		// Game.windowWidth, Game.halfWindowHeight,
		// Game.windowWidth - Game.halfWindowHeight, Game.windowHeight);

		windowSelectConversationResponse.draw();

	}

	public void drawStaticUI2() {

		float topMargin = 25;
		float maxWidth = Game.windowWidth;
		float x1 = Game.halfWindowWidth - halfTextWidth;
		float y1 = Game.windowHeight - Conversation.bottomMargin - Conversation.height + topMargin;

		TextUtils.printTextWithImages(x1, y1, maxWidth, true, links, Color.WHITE, 1f, text);

	}

	public void setConversationResponses(ConversationResponse[] conversationResponses) {
		this.conversationResponses = conversationResponses;
		windowSelectConversationResponse = new ConversationResponseDisplay(100, Game.level, conversationResponses,
				talker, this);
	}

	public void resize() {
		windowSelectConversationResponse.resize();

	}

	public void selectDialogueOption(char character) {
		windowSelectConversationResponse.selectDialogueOption(character);

		for (Quest quest : quests)
			quest.addConversationPart(this);

	}

	public void leave() {
		if (leaveConversationListener != null)
			leaveConversationListener.leave();

		for (Quest quest : quests)
			quest.addConversationPart(this);

	}

	public void setTurnAndSquareAndArea(int turn, Square square, Area area) {
		this.turn = turn;
		this.turnString = Game.level.timeString + " (Turn " + turn + ") ";

		this.square = square;
		squareAndText = new CopyOnWriteArrayList<Object>();
		squareAndText.add(square);
		squareAndText.addAll(Arrays.asList(text));

		this.area = area;

		linksForJournal = new CopyOnWriteArrayList<Link>();
		linksForJournal.addAll(TextUtils.getLinks(true, talker, square));
		linksForJournal.addAll(links);

	}

	public Square getSquare() {
		return square;
	}

	public String getTurnString() {
		return turnString;
	}

	public Area getArea() {
		return area;
	}

	public void shown() {

	}

}
