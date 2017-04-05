package com.marklynch.objects.weapons;

import com.marklynch.Game;
import com.marklynch.level.Square;
import com.marklynch.objects.Arrow;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.units.Actor;
import com.marklynch.utils.TextureUtils;

import mdesl.graphics.Texture;

public class Projectile {

	public String name;
	public Actor shooter;
	public GameObject target;
	float x, y, originX, originY, targetX, targetY, speedX, speedY;
	boolean onTarget;
	String imagePath;
	Texture imageTexture;
	float distanceToCoverX, distanceToCoverY, distanceCoveredX, distanceCoveredY;
	GameObject projectileObject;

	public Projectile(String name, Actor shooter, GameObject target, GameObject projectileObject, float speed,
			boolean onTarget) {
		super();

		if (shooter == Game.level.player) {
			name = "Your " + name;
		} else {
			name = shooter.name + "'s " + name;
		}

		this.name = name;
		this.shooter = shooter;
		this.target = target;
		this.projectileObject = projectileObject;

		this.x = this.originX = shooter.getCenterX();
		this.y = this.originY = shooter.getCenterY();
		this.targetX = target.getCenterX();
		this.targetY = target.getCenterY();

		distanceToCoverX = this.targetX - this.originX;
		distanceToCoverY = this.targetY - this.originY;
		float totalDistanceToCover = Math.abs(distanceToCoverX) + Math.abs(distanceToCoverY);

		this.speedX = (distanceToCoverX / totalDistanceToCover) * speed;
		this.speedY = (distanceToCoverY / totalDistanceToCover) * speed;

		if (distanceToCoverX < 0) {
			projectileObject.backwards = true;
		}

		this.onTarget = onTarget;
	}

	public void update(float delta) {

		float distanceX = speedX * delta;
		float distanceY = speedY * delta;

		distanceCoveredX += distanceX;
		distanceCoveredY += distanceY;

		if (Math.abs(distanceCoveredX) >= Math.abs(distanceToCoverX)
				&& Math.abs(distanceCoveredY) >= Math.abs(distanceToCoverY)) {
			Game.level.projectilesToRemove.add(this);
			shooter.showPow(target);
			if (!(projectileObject instanceof Arrow))
				target.squareGameObjectIsOn.inventory.add(projectileObject);
		} else {
			x += distanceX;
			y += distanceY;
			Square square = Game.level.squares[(int) Math.floor(x / Game.SQUARE_WIDTH)][(int) Math
					.floor(y / Game.SQUARE_HEIGHT)];
			square.inventory.smashWindows(this);

		}

		// if reached target,
		// this.showPow(gameObject);
	}

	public void drawForeground() {
		// float x1 = x;
		// float x2 = x + 10;
		// float y1 = y;
		// float y2 = y + 10;
		// QuadUtils.drawQuad(Color.BLACK, x1, x2, y1, y2);

		// if (this.remainingHealth <= 0)
		// return;

		// if (!Game.fullVisiblity) {
		// if (this.squareGameObjectIsOn.visibleToPlayer == false &&
		// persistsWhenCantBeSeen == false)
		// return;
		//
		// if (!this.squareGameObjectIsOn.seenByPlayer)
		// return;
		// }

		// Draw object
		// if (squareGameObjectIsOn != null) {
		// int actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGrid
		// * (int) Game.SQUARE_WIDTH
		// + drawOffsetX);
		// int actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGrid
		// * (int) Game.SQUARE_HEIGHT
		// + drawOffsetY);

		float alpha = 1.0f;

		// TextureUtils.skipNormals = true;

		// if (!this.squareGameObjectIsOn.visibleToPlayer)
		// alpha = 0.5f;
		TextureUtils.drawTexture(projectileObject.imageTexture, alpha, x, x + projectileObject.width, y,
				y + projectileObject.height, projectileObject.backwards);
		// TextureUtils.skipNormals = false;
		// }

	}

}
