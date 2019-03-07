package com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom;

import com.marklynch.Game;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.squares.Node;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.Switch;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.utils.SwitchListener;
import com.marklynch.utils.ArrayList;

public class PuzzleRoomWaterDrain extends StructureRoom implements SwitchListener {

	int posX;
	int posY;

	final static int totalWidthInSquares = 6;
	final static int totalHeightInSquares = 9;

	int windowWallY = 5;

	int inputDrainX = 1;
	int inputDrainY = 7;

	int outputDrainX = 1;
	int outputDrainY = 1;

	int electricalX = 1;
	int electricalY = 0;

	int pressurePlateX = 3;
	int pressurePlateY = 1;

	int wellX = 6;
	int wellY = 7;

	public PuzzleRoomWaterDrain(int posX, int posY) {
		super("Cave In Room", posX, posY, false, false, new ArrayList<Actor>(Actor.class), 1, false, new Node[] {},
				new RoomPart[] {
						new RoomPart(posX, posY, posX + totalWidthInSquares - 1, posY + totalHeightInSquares - 1) });

		this.posX = posX;
		this.posY = posY;

		for (int i = 0; i < totalWidthInSquares; i++) {
			Templates.WALL_WINDOW.makeCopy(Game.level.squares[posX + i][posY + windowWallY], null);
			Game.level.squares[posX + i][posY + windowWallY].setFloorImageTexture(Square.STONE_TEXTURE);
		}

		GameObject inputDrain = Templates.INPUT_DRAIN.makeCopy(
				Game.level.squares[posX + inputDrainX][posY + inputDrainY], null,
				Game.level.squares[posX + outputDrainX][posY + outputDrainY]);

		GameObject outputDrain = Templates.DRAIN_FLOOR
				.makeCopy(Game.level.squares[posX + outputDrainX][posY + outputDrainY], null);

		inputDrain.linkedObjects.add(outputDrain);
		outputDrain.linkedObjects.add(inputDrain);

		Templates.ELECTRICAL_WIRING.makeCopy(Game.level.squares[posX + electricalX][posY + electricalY], null);

		Templates.PRESSURE_PLATE.makeCopy(Game.level.squares[posX + pressurePlateX][posY + pressurePlateY], null,
				Switch.SWITCH_TYPE.OPEN_CLOSE, 1, this);

		Templates.WELL.makeCopy(Game.level.squares[posX + wellX][posY + wellY], null);

	}

	@Override
	public void zwitch(Switch zwitch) {
		System.out.println("switch");
	}

}
