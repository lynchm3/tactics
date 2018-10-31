package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.conversation.Conversation;
import com.marklynch.level.conversation.ConversationPart;
import com.marklynch.level.conversation.ConversationResponse;
import com.marklynch.objects.Readable;
import com.marklynch.objects.units.Actor;
import com.marklynch.ui.ActivityLog;

public class ActionWrite extends Action {

	public static final String ACTION_NAME = "Write";

	Readable sign;
	Object[] text;

	public ActionWrite(Actor performer, Readable sign, Object[] text) {
		super(ACTION_NAME, textureWrite, performer, sign, null);
		this.sign = sign;
		this.text = text;
		if (!check()) {
			enabled = false;
		}
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {
		super.perform();

		if (!enabled)
			return;

		if (!checkRange())
			return;

		ConversationPart conversationPart = new ConversationPart(text, new ConversationResponse[] {}, null);
		Conversation conversation = new Conversation(conversationPart, sign, true);
		sign.setConversation(conversation);
		if (Game.level.shouldLog(performer))
			Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " updated ", sign }));
		performer.actionsPerformedThisTurn.add(this);
		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean checkRange() {
		if (performer.straightLineDistanceTo(sign.squareGameObjectIsOn) < 2) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkLegality() {
		return true;
	}

	@Override
	public Sound createSound() {
		return null;
	}

}