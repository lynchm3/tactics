package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.conversation.Conversation;
import com.marklynch.objects.units.Actor;

public class ActionTalk extends Action {

	public static final String ACTION_NAME = "Talk";

	public Actor performer;
	public Actor target;
	public Conversation conversation;

	// Default for hostiles
	public ActionTalk(Actor talker, Actor target) {
		this(talker, target, null);
	}

	// Default for hostiles
	public ActionTalk(Actor talker, Actor target, Conversation conversation) {
		super(ACTION_NAME);
		this.performer = talker;
		this.target = target;
		this.conversation = conversation;
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {
		if (conversation != null) {

		} else if (target == Game.level.player) {
			conversation = performer.getConversation();
		} else {
			conversation = target.getConversation();
		}

		if (conversation != null) {
			conversation.currentConversationPart = conversation.openingConversationPart;
			Game.level.conversation = conversation;

		}

		performer.actionsPerformedThisTurn.add(this);
		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean checkLegality() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Sound createSound() {
		return new Sound(performer, target, target.squareGameObjectIsOn, 1, legal, this.getClass());
	}

}