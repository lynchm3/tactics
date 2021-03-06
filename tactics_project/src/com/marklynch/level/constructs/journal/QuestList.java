package com.marklynch.level.constructs.journal;

import com.marklynch.level.Level;
import com.marklynch.level.constructs.bounds.structure.Structure;
import com.marklynch.level.constructs.bounds.structure.StructureFeature;
import com.marklynch.level.constructs.bounds.structure.StructurePath;
import com.marklynch.level.constructs.bounds.structure.StructureSection;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomArrows;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomBushesFight;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomCaveInFight;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomChasm;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomConveyerBelt;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomCrumblingWall;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomExtendableBridge;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomFallawayFloor;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomFriendlyCaveIn;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomFuse;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomMaze;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomMineCart;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomMineCart2;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomMineThroughWall;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomMovingBridge;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomSpikeFloor1;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomTeamwork1;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomTeamwork2;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomTeamwork3;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomUndergroundLake;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomWaterDrain;
import com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom.PuzzleRoomWaterShallows;
import com.marklynch.level.quest.Quest;
import com.marklynch.level.quest.betweenthewalls.QuestBetweenTheWalls;
import com.marklynch.level.quest.caveoftheblind.QuestCaveOfTheBlind;
import com.marklynch.level.quest.smallgame.QuestSmallGame;
import com.marklynch.level.quest.thepigs.QuestThePigs;
import com.marklynch.level.quest.thesecretroom.QuestTheSecretRoom;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.Wall;
import com.marklynch.objects.templates.Templates;
import com.marklynch.utils.CopyOnWriteArrayList;
import com.marklynch.utils.ResourceUtils;

@SuppressWarnings("serial")
public class QuestList extends CopyOnWriteArrayList<Quest> {
	public static QuestSmallGame questSmallGame;
	public static QuestCaveOfTheBlind questCaveOfTheBlind;
	public static QuestThePigs questThePigs;
	public static QuestBetweenTheWalls questBetweenTheWalls;
	public static QuestTheSecretRoom questTheSecretRoom;

	public QuestList() {
		super(Quest.class);
	}

	public void makeQuests() {

		// QUEST - the pigs
		questThePigs = new QuestThePigs();
		add(questThePigs);

		// QUEST - the cave of the blind
		questCaveOfTheBlind = new QuestCaveOfTheBlind();
		add(questCaveOfTheBlind);

		// QUEST - small game
		questSmallGame = new QuestSmallGame();
		add(questSmallGame);

		questCaveOfTheBlind.objectiveHunters = new Objective("The Hunters", questSmallGame.hunterBrent, null,
				questSmallGame.hunterBrent.imageTexture);

		// QUEST - between the walls
		questBetweenTheWalls = new QuestBetweenTheWalls(0, 5);
		add(questBetweenTheWalls);

		// QUEST - the secret room
		questTheSecretRoom = new QuestTheSecretRoom();
		add(questTheSecretRoom);

		// PUZZLE ROOMS

		int puzzleRoomsX = 100;
		int puzzleRoomsY = 100;

		CopyOnWriteArrayList<StructurePath> paths = new CopyOnWriteArrayList<StructurePath>(StructurePath.class);
		CopyOnWriteArrayList<StructureSection> structureSections = new CopyOnWriteArrayList<StructureSection>(StructureSection.class);
		CopyOnWriteArrayList<Square> squaresToRemove = new CopyOnWriteArrayList<Square>(Square.class);
		structureSections.add(new StructureSection("Puzzle Structure Section", puzzleRoomsX, puzzleRoomsY,
				puzzleRoomsX + 100, puzzleRoomsY + 150, false, false));

		CopyOnWriteArrayList<StructureRoom> puzzleStructureRooms = new CopyOnWriteArrayList<StructureRoom>(StructureRoom.class);

		// DOORWAY top left entrance
		squaresToRemove.add(Level.squares[puzzleRoomsX][puzzleRoomsY + 10]);
		squaresToRemove.add(Level.squares[puzzleRoomsX][puzzleRoomsY + 11]);

		// ROOM Big bridge room
		puzzleStructureRooms.add(new PuzzleRoomMovingBridge(puzzleRoomsX + 1, puzzleRoomsY + 1));

		// DOORWAY entrance between bridge and minecart
		squaresToRemove.add(Level.squares[puzzleRoomsX + 10][puzzleRoomsY + 21]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 11][puzzleRoomsY + 21]);

		// ROOM Minecart room 1
		puzzleStructureRooms.add(new PuzzleRoomMineCart(puzzleRoomsX + 1, puzzleRoomsY + 22));

		// Doorway minecart room 1 to fuse room
		squaresToRemove.add(Level.squares[puzzleRoomsX + 14][puzzleRoomsY + 23]);

		// Fuse room 1
		puzzleStructureRooms.add(new PuzzleRoomFuse(puzzleRoomsX + 15, puzzleRoomsY + 22));

		// DOORWAY minecart 1 to minecart 2
		squaresToRemove.add(Level.squares[puzzleRoomsX + 14][puzzleRoomsY + 41]);

		// ROOM minecart room 2
		puzzleStructureRooms.add(new PuzzleRoomMineCart2(puzzleRoomsX + 15, puzzleRoomsY + 30));

		// DOORWAY minecart 2 to fallaway floor
		squaresToRemove.add(Level.squares[puzzleRoomsX + 35][puzzleRoomsY + 41]);

		// ROOM Fallaway floor
		puzzleStructureRooms.add(new PuzzleRoomFallawayFloor(puzzleRoomsX + 36, puzzleRoomsY + 30));

		// DOORWAY fallaway floor to maze
		squaresToRemove.add(Level.squares[puzzleRoomsX + 38][puzzleRoomsY + 60]);

		// ROOM maze
		// puzzleStructureRooms.add(
		PuzzleRoomMaze puzzleRoomMaze = new PuzzleRoomMaze(puzzleRoomsX + 24, puzzleRoomsY + 61);
		puzzleStructureRooms.add(puzzleRoomMaze);
		paths.addAll(puzzleRoomMaze.structurePaths);
		// );

		// DOORWAY maze to dig pointer
		squaresToRemove.add(Level.squares[puzzleRoomsX + 53][puzzleRoomsY + 91]);

		// ROOM Dig pointer
		puzzleStructureRooms.add(new PuzzleRoomArrows(puzzleRoomsX + 6, puzzleRoomsY + 92));

		// DOORWAY fallaway floor to chambers 1
		squaresToRemove.add(Level.squares[puzzleRoomsX + 66][puzzleRoomsY + 30]);

		// ROOM Teamwork chambers 1
		puzzleStructureRooms.add(new PuzzleRoomTeamwork1(puzzleRoomsX + 67, puzzleRoomsY + 30));

		// DOORWAY fallaway floor to team chambers 2
		squaresToRemove.add(Level.squares[puzzleRoomsX + 66][puzzleRoomsY + 41]);

		// ROOM Teamwork chambers 2
		puzzleStructureRooms.add(new PuzzleRoomTeamwork2(puzzleRoomsX + 67, puzzleRoomsY + 41));
		squaresToRemove.add(Level.squares[puzzleRoomsX + 14][puzzleRoomsY + 41]);

		// DOORWAY fallaway floor to team chambers 3
		squaresToRemove.add(Level.squares[puzzleRoomsX + 66][puzzleRoomsY + 52]);

		// ROOM Teamwork chambers 3
		puzzleStructureRooms.add(new PuzzleRoomTeamwork3(puzzleRoomsX + 67, puzzleRoomsY + 52));

		// ROOM Mine through walls
		puzzleStructureRooms.add(new PuzzleRoomMineThroughWall(puzzleRoomsX + 67, puzzleRoomsY + 62));

		// DOORWAY mine through wall to chasm
		squaresToRemove.add(Level.squares[puzzleRoomsX + 69][puzzleRoomsY + 72]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 70][puzzleRoomsY + 72]);

		// ROOM Chasm
		puzzleStructureRooms.add(new PuzzleRoomChasm(puzzleRoomsX + 60, puzzleRoomsY + 73));

		// DOORWAY chasm to underground lake
		squaresToRemove.add(Level.squares[puzzleRoomsX + 80][puzzleRoomsY + 78]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 80][puzzleRoomsY + 79]);

		// ROOM Shallows
		puzzleStructureRooms.add(new PuzzleRoomWaterShallows(puzzleRoomsX + 81, puzzleRoomsY + 75));

		// DOORWAY chasm to shallows
		squaresToRemove.add(Level.squares[puzzleRoomsX + 69][puzzleRoomsY + 93]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 70][puzzleRoomsY + 93]);

		// ROOM underground lake
		puzzleStructureRooms.add(new PuzzleRoomUndergroundLake(puzzleRoomsX + 60, puzzleRoomsY + 94));

		// Doorway between underground lake and friendly cave in room
		squaresToRemove.add(Level.squares[puzzleRoomsX + 71][puzzleRoomsY + 114]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 60][puzzleRoomsY + 114]);

		// ROOM friendly cave in room
		puzzleStructureRooms.add(new PuzzleRoomFriendlyCaveIn(puzzleRoomsX + 60, puzzleRoomsY + 115));

		// Doorway between minecart1 and cave in
		squaresToRemove.add(Level.squares[puzzleRoomsX + 5][puzzleRoomsY + 42]);

		// ROOM fight cave in
		puzzleStructureRooms.add(new PuzzleRoomCaveInFight(puzzleRoomsX + 1, puzzleRoomsY + 43));

		// Doorway between cave in and bush sneak room
		squaresToRemove.add(Level.squares[puzzleRoomsX + 5][puzzleRoomsY + 63]);

		// ROOM bush sneak fight
		puzzleStructureRooms.add(new PuzzleRoomBushesFight(puzzleRoomsX + 1, puzzleRoomsY + 64));

		// Doorway bush sneak room and spike room 1
		squaresToRemove.add(Level.squares[puzzleRoomsX + 2][puzzleRoomsY + 84]);

		// Spike room 1
		puzzleStructureRooms.add(new PuzzleRoomSpikeFloor1(puzzleRoomsX + 1, puzzleRoomsY + 85));

		// Doorway spike room 1 to drain room
		squaresToRemove.add(Level.squares[puzzleRoomsX + 16][puzzleRoomsY + 84]);

		// Drain room 1
		puzzleStructureRooms.add(new PuzzleRoomWaterDrain(puzzleRoomsX + 15, puzzleRoomsY + 75));

		// Doorway drain room top left
		squaresToRemove.add(Level.squares[puzzleRoomsX + 14][puzzleRoomsY + 76]);

		// ROOM Crumbling wall
		puzzleStructureRooms.add(new PuzzleRoomCrumblingWall(puzzleRoomsX + 22, puzzleRoomsY + 10));

		// Crumbling wall to extendable bridge
		squaresToRemove.add(Level.squares[puzzleRoomsX + 27][puzzleRoomsY + 11]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 27][puzzleRoomsY + 12]);

		// ROOM extendable bridge
		puzzleStructureRooms.add(new PuzzleRoomExtendableBridge(puzzleRoomsX + 28, puzzleRoomsY + 11));

		// Extendable bridge to conveyer belt room
		squaresToRemove.add(Level.squares[puzzleRoomsX + 48][puzzleRoomsY + 11]);
		squaresToRemove.add(Level.squares[puzzleRoomsX + 48][puzzleRoomsY + 12]);

		// Conveyer belt room
		puzzleStructureRooms.add(new PuzzleRoomConveyerBelt(puzzleRoomsX + 49, puzzleRoomsY + 11));

		CopyOnWriteArrayList<StructureFeature> features = new CopyOnWriteArrayList<StructureFeature>(StructureFeature.class);
		features.addAll(puzzleRoomMaze.features);

		CopyOnWriteArrayList<Square> entrances = new CopyOnWriteArrayList<Square>(Square.class);
		entrances.add(Level.squares[puzzleRoomsX][puzzleRoomsY + 10]);
		// 2nd top left entrance
		// squaresToRemove.add(Level.squares[x][y + 31]);
		// squaresToRemove.add(Level.squares[x][y + 32]);
		CopyOnWriteArrayList<Wall> extraWalls = new CopyOnWriteArrayList<Wall>(Wall.class);
		extraWalls.add(Templates.FALSE_WALL.makeCopy(Level.squares[puzzleRoomsX + 1 + 1][puzzleRoomsY + 22 + 0], null));
		Wall readableWall = Templates.READABLE_WALL.makeCopy(Level.squares[puzzleRoomsX + 27][puzzleRoomsY + 10], null);
		readableWall.conversation = readableWall.createConversation("Watup, I'm a wall");
		extraWalls.add(readableWall);

		Structure puzzleStructure = new Structure("Puzzle Structure", structureSections, puzzleStructureRooms, paths,
				features, entrances, ResourceUtils.getGlobalImage("icon_cave.png", false), puzzleRoomsX, puzzleRoomsY,
				puzzleRoomsX + 100, puzzleRoomsY + 100, true, null, squaresToRemove, extraWalls, Templates.WALL_CAVE,
				Square.STONE_TEXTURE, 10);

	}

	// public void update() {
	// for (Quest quest : this) {
	//
	// }
	// }

}
