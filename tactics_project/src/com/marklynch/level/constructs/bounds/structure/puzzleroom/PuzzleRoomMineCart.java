package com.marklynch.level.constructs.bounds.structure.puzzleroom;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.bounds.structure.StructureRoom;
import com.marklynch.level.squares.Node;
import com.marklynch.objects.Rail;
import com.marklynch.objects.Switch;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Actor.Direction;

public class PuzzleRoomMineCart extends StructureRoom {
	int posX;
	int posY;
	final static int totalWidthInSquares = 20;
	final static int totalHeightInSquares = 20;

	public PuzzleRoomMineCart(int posX, int posY) {
		super("Crumbling Wall Room", posX, posY, false, new ArrayList<Actor>(), 1, false, new Node[] {},
				new RoomPart[] {
						new RoomPart(posX, posY, posX + totalWidthInSquares - 1, posY + totalHeightInSquares - 1) });

		this.posX = posX;
		this.posY = posY;

		// Top loop

		// top
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 2], null, Direction.DOWN, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 3][posY + 2], null, Direction.LEFT, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 4][posY + 2], null, Direction.LEFT, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 5][posY + 2], null, Direction.LEFT, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 2], null, Direction.LEFT, Direction.DOWN);

		// right
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 3], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 4], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 5], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 6], null, Direction.UP, Direction.DOWN);

		// bottom
		Templates.RAIL.makeCopy(Level.squares[posX + 6][posY + 7], null, Direction.LEFT, Direction.UP);
		Templates.RAIL.makeCopy(Level.squares[posX + 5][posY + 7], null, Direction.LEFT, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 4][posY + 7], null, Direction.LEFT, Direction.RIGHT);
		Templates.RAIL.makeCopy(Level.squares[posX + 3][posY + 7], null, Direction.LEFT, Direction.RIGHT);
		Rail switchable = Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 7], null, Direction.RIGHT,
				Direction.UP);

		// left
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 3], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 4], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 5], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 6], null, Direction.UP, Direction.DOWN);

		Templates.PRESSURE_PLATE.makeCopy(Game.level.squares[posX + 4][posY + 5], null, switchable,
				Switch.SWITCH_TYPE.OPEN_CLOSE, 5);

		// Second loop
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 8], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 9], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 10], null, Direction.UP, Direction.DOWN);
		Templates.RAIL.makeCopy(Level.squares[posX + 2][posY + 11], null, Direction.UP, Direction.DOWN);

		Templates.MINE_CART.makeCopy(Level.squares[posX + 3][posY + 2], null, Direction.RIGHT);
	}

}