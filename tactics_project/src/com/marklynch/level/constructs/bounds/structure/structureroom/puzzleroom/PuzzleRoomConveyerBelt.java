package com.marklynch.level.constructs.bounds.structure.structureroom.puzzleroom;

import com.marklynch.level.Level;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.actors.Actor.Direction;
import com.marklynch.objects.templates.Templates;
import com.marklynch.utils.ArrayList;

public class PuzzleRoomConveyerBelt extends StructureRoom {

	int posX;
	int posY;
	final static int totalWidthInSquares = 14;
	final static int totalHeightInSquares = 2;

	ArrayList<Square> extendedBridgeSquares = new ArrayList<Square>(Square.class);

	Square voidSquare;

	public PuzzleRoomConveyerBelt(int posX, int posY) {
		super("Bridge Room", posX, posY, false, false, new ArrayList<Actor>(Actor.class), 1, false, new RoomPart[] {
				new RoomPart(posX, posY, posX + totalWidthInSquares - 1, posY + totalHeightInSquares - 1) });

		this.posX = posX;
		this.posY = posY;

		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 1][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 2][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 3][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 4][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 5][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 6][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 7][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 8][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 9][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 10][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 11][posY], null, Direction.RIGHT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 12][posY], null, Direction.RIGHT);

		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 1][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 2][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 3][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 4][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 5][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 6][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 7][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 8][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 9][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 10][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 11][posY + 1], null, Direction.LEFT);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 12][posY + 1], null, Direction.LEFT);

		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 13][posY], null, Direction.UP);
		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 13][posY + 1], null, Direction.UP);

//		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 14][posY], null, Direction.DOWN);
//		Templates.CONVEYER_BELT.makeCopy(Level.squares[posX + 14][posY + 1], null, Direction.DOWN);
	}

	@Override
	public Long getId() {
		return id;
	}

}
