package com.marklynch.tactics.objects.level.script;

import java.util.Vector;

public class ScriptEventInlineSpeech extends ScriptEvent {

	public Vector<InlineSpeechPart> speechParts;
	public int speechIndex = 0;
	public int timeOnCurrentPart = 0;
	public int timePerPart = 10000;

	public ScriptEventInlineSpeech(boolean blockUserInput,
			Vector<InlineSpeechPart> speechParts, ScriptTrigger scriptTrigger) {
		super(blockUserInput, scriptTrigger);
		this.speechParts = speechParts;
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

}
