package com.marklynch.level.constructs.journal;

import java.util.ArrayList;

import com.marklynch.level.Level;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.quest.betweenthewalls.QuestBetweenTheWalls;
import com.marklynch.level.quest.caveoftheblind.QuestCaveOfTheBlind;
import com.marklynch.level.quest.smallgame.QuestSmallGame;
import com.marklynch.level.quest.thepigs.QuestThePigs;
import com.marklynch.level.quest.thesecretroom.QuestTheSecretRoom;

@SuppressWarnings("serial")
public class QuestList extends ArrayList<Quest> {
	public static QuestSmallGame questSmallGame;
	public static QuestCaveOfTheBlind questCaveOfTheBlind;
	public static QuestThePigs questThePigs;
	public static QuestBetweenTheWalls questBetweenTheWalls;
	public static QuestTheSecretRoom questTheSecretRoom;

	public QuestList() {

	}

	public void makeQuests() {

		questCaveOfTheBlind = new QuestCaveOfTheBlind();
		add(questCaveOfTheBlind);

		questSmallGame = new QuestSmallGame();
		add(questSmallGame);

		questThePigs = new QuestThePigs();
		Level.quests.add(questThePigs);

		questBetweenTheWalls = new QuestBetweenTheWalls();
		Level.quests.add(questBetweenTheWalls);

		questTheSecretRoom = new QuestTheSecretRoom();
		Level.quests.add(questTheSecretRoom);
	}

}
