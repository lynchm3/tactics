package com.marklynch.tactics.objects.level.script;

import java.util.Vector;

import com.marklynch.tactics.objects.level.script.ScriptEventSpeech.SpeechPart;
import com.marklynch.tactics.objects.level.script.trigger.ScriptTrigger;

public class ScriptEventInlineSpeech extends ScriptEvent {

	public final static String[] editableAttributes = { "name",
			"blockUserInput", "scriptTrigger", "speechParts" };

	public Vector<SpeechPart> speechParts;
	public transient int speechIndex = 0;
	public transient int timeOnCurrentPart = 0;
	public transient int timePerPart = 10000;

	public ScriptEventInlineSpeech() {
		name = "ScriptEventInlineSpeech";
	}

	public ScriptEventInlineSpeech(boolean blockUserInput,
			Vector<SpeechPart> speechParts, ScriptTrigger scriptTrigger) {
		super(blockUserInput, scriptTrigger);
		this.speechParts = speechParts;
		name = "ScriptEventInlineSpeech";
	}

	@Override
	public boolean checkIfCompleted() {
		if (speechIndex >= speechParts.size())
			return true;
		return false;
	}

	@Override
	public void click() {
	}

	@Override
	public void draw() {
		if (speechIndex < speechParts.size()) {
			speechParts.get(speechIndex).draw();
		}
	}

	@Override
	public void update(int delta) {
		timeOnCurrentPart += delta;
		if (timeOnCurrentPart >= timePerPart) {
			speechIndex++;
			timeOnCurrentPart = 0;
		}

	}

	@Override
	public void postLoad() {
		speechIndex = 0;
		timeOnCurrentPart = 0;
		timePerPart = 10000;
		scriptTrigger.postLoad();
	}
}
