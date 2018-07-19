package com.marklynch.level.constructs.power;

import org.lwjgl.util.Point;

import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.primary.AnimationPush;
import com.marklynch.level.constructs.animation.secondary.AnimationThrown;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.effect.EffectBurning;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.Arrow;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.actions.Action;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.units.Actor;
import com.marklynch.utils.ResourceUtils;

public class PowerInferno extends Power {

	private static String NAME = "Inferno";

	public PowerInferno(GameObject source) {
		super(NAME, ResourceUtils.getGlobalImage("action_burn.png", false), source,
				new Effect[] { new EffectBurning(source, null, 3) }, 5,
				null,
				new Point[] { new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0) }, 10, true, true, Crime.TYPE.CRIME_ASSAULT);
		selectTarget = true;
	}

	@Override
	public Power makeCopy(GameObject source) {
		return new PowerInferno(source);
	}

	@Override
	public void cast(final Actor source, GameObject targetGameObject, Square targetSquare, final Action action) {
		source.setPrimaryAnimation(new AnimationPush(source, targetSquare, source.getPrimaryAnimation()));
		final Arrow fireBall = Templates.FIRE_BALL.makeCopy(null, null);
		Animation animationThrown = new AnimationThrown("Fire Ball", source, action, targetGameObject, targetSquare,
				fireBall, source, 1f, 0f, true) {
			@Override
			public void runCompletionAlgorightm(boolean wait) {
				super.runCompletionAlgorightm(wait);
				PowerInferno.super.cast(source, targetGameObject, targetSquare, action);
			}
		};
		source.addSecondaryAnimation(animationThrown);

	}
}
