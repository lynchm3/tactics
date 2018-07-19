package com.marklynch.level.constructs.power;

import org.lwjgl.util.Point;

import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Stat;
import com.marklynch.level.constructs.Stat.HIGH_LEVEL_STATS;
import com.marklynch.level.constructs.animation.primary.AnimationStraightLine;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.units.Actor;
import com.marklynch.utils.ResourceUtils;

public class PowerDash extends Power {

	private static String NAME = "Dash";

	public PowerDash(GameObject source) {
		super(NAME, ResourceUtils.getGlobalImage("right.png", false), source, new Effect[] {}, 3,
				Power.castLocationsOnly2, new Point[] { new Point(0, 0) }, 10, true, true, Crime.TYPE.CRIME_ASSAULT);
		selectTarget = true;
	}

	@Override
	public Power makeCopy(GameObject source) {
		return new PowerDash(source);
	}

	@Override
	public void cast(final Actor source, GameObject targetGameObject, final Square targetSquare, final Action action) {

		source.setPrimaryAnimation(new AnimationStraightLine(source, 2f, true, new Square[] { targetSquare }) {
			@Override
			public void runCompletionAlgorightm(boolean wait) {
				super.runCompletionAlgorightm(wait);
				postRangedAnimation(source, targetSquare);
				// postRangedAnimation(arrow);
			}
		});
	}

	public void postAnimation(GameObject pushedGameObject, Action action, GameObject obstacle) {

		pushedGameObject.changeHealth(source, action, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, pushedGameObject.weight));
		if (obstacle != null) {
			obstacle.changeHealth(source, action, new Stat(HIGH_LEVEL_STATS.BLUNT_DAMAGE, pushedGameObject.weight));
		}
	}

}
