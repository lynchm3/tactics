package com.marklynch.tactics.objects.weapons;

import static com.marklynch.utils.ResourceUtils.getGlobalImage;

import java.util.Vector;

import mdesl.graphics.Texture;

import com.marklynch.tactics.objects.level.Square;
import com.marklynch.tactics.objects.unit.Actor.Direction;

public class Weapon {

	// attributes
	public float damage = 0;
	public float minRange = 0;
	public float maxRange = 0;

	// image
	public String imagePath = "";
	public transient Texture imageTexture = null;
	public String name;

	public Weapon(String name, float damage, float minRange, float maxRange,
			String imagePath) {
		super();
		this.name = name;
		this.damage = damage;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.imagePath = imagePath;
		loadImages();
	}

	public void loadImages() {
		this.imageTexture = getGlobalImage(imagePath);
	}

	public void calculateAttackableSquares(Square[][] squares) {
		for (int i = 0; i < squares.length; i++) {
			for (int j = 0; j < squares.length; j++) {
				if (squares[i][j].reachableBySelectedCharater) {
					for (float range = minRange; range <= maxRange; range++) {
						Vector<Square> squaresInThisPath = new Vector<Square>();
						squaresInThisPath.add(squares[i][j]);
						calculateAttackableSquares(squares, range,
								squares[i][j], Direction.UP, squaresInThisPath);
						calculateAttackableSquares(squares, range,
								squares[i][j], Direction.RIGHT,
								squaresInThisPath);
						calculateAttackableSquares(squares, range,
								squares[i][j], Direction.DOWN,
								squaresInThisPath);
						calculateAttackableSquares(squares, range,
								squares[i][j], Direction.LEFT,
								squaresInThisPath);
					}
				}
			}
		}
	}

	public void calculateAttackableSquares(Square[][] squares,
			float remainingRange, Square parentSquare, Direction direction,
			Vector<Square> squaresInThisPath) {
		Square currentSquare = null;

		if (direction == Direction.UP) {
			if (parentSquare.y - 1 >= 0) {
				currentSquare = squares[parentSquare.x][parentSquare.y - 1];
			}
		} else if (direction == Direction.RIGHT) {
			if (parentSquare.x + 1 < squares.length) {
				currentSquare = squares[parentSquare.x + 1][parentSquare.y];
			}
		} else if (direction == Direction.DOWN) {

			if (parentSquare.y + 1 < squares[0].length) {
				currentSquare = squares[parentSquare.x][parentSquare.y + 1];
			}
		} else if (direction == Direction.LEFT) {
			if (parentSquare.x - 1 >= 0) {
				currentSquare = squares[parentSquare.x - 1][parentSquare.y];
			}
		}

		if (currentSquare != null && !squaresInThisPath.contains(currentSquare)) {
			squaresInThisPath.add(currentSquare);
			if (!currentSquare.weaponsThatCanAttack.contains(this))
				currentSquare.weaponsThatCanAttack.add(this);
			remainingRange -= 1;
			if (remainingRange > 0) {
				calculateAttackableSquares(squares, remainingRange,
						currentSquare, Direction.UP, squaresInThisPath);
				calculateAttackableSquares(squares, remainingRange,
						currentSquare, Direction.RIGHT, squaresInThisPath);
				calculateAttackableSquares(squares, remainingRange,
						currentSquare, Direction.DOWN, squaresInThisPath);
				calculateAttackableSquares(squares, remainingRange,
						currentSquare, Direction.LEFT, squaresInThisPath);
			}
			squaresInThisPath.remove(currentSquare);
		}

	}

	public boolean hasRange(int weaponDistanceTo) {
		if (weaponDistanceTo >= minRange && weaponDistanceTo <= maxRange) {
			return true;
		}
		return false;
	}

	public Weapon makeCopy() {
		return new Weapon(new String(name), damage, minRange, maxRange,
				new String(imagePath));
	}
}
