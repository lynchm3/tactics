package com.marklynch.editor.settingswindow;

import com.marklynch.editor.Editor;

public class RelationsSettingsWindow extends SettingsWindow {

	public RelationsSettingsWindow(float width, final Editor editor) {
		super(width, editor);
		updateRelationsButtons();
	}

	public void updateRelationsButtons() {
		buttons.clear();

		// final CopyOnWriteArrayList<FactionRelationship> factionRelationships = new
		// CopyOnWriteArrayList<FactionRelationship>();
		// for (Faction faction : Game.level.factions) {
		// for (FactionRelationship factionRelationship :
		// faction.relationships.values()) {
		// factionRelationships.add(factionRelationship);
		// }
		// }
		//
		// int count = 0;
		// for (int i = 0; i < factionRelationships.size(); i++) {
		//
		// final int index = count;
		//
		// final SettingsWindowButton scriptButton = new SettingsWindowButton(0, 200 + i
		// * 30, 200, 30,
		// factionRelationships.get(i), true, true) {
		//
		// @Override
		// public void keyTyped(char character) {
		// }
		//
		// @Override
		// public void enterTyped() {
		// }
		//
		// @Override
		// public void backTyped() {
		// }
		//
		// @Override
		// public void depress() {
		// }
		//
		// };
		//
		// scriptButton.clickListener = new ClickListener() {
		//
		// @Override
		// public void click() {
		// editor.clearSelectedObject();
		// editor.depressButtonsSettingsAndDetailsButtons();
		// scriptButton.down = true;
		// editor.attributesWindow = new AttributesDialog(200, 200, 200,
		// factionRelationships.get(index),
		// editor);
		//
		// }
		// };
		// buttons.add(scriptButton);
		//
		// count++;
		//
		// }

	}

	@Override
	public void update() {
		updateRelationsButtons();

	}
}
