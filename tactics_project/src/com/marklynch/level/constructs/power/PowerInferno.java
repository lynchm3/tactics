package com.marklynch.level.constructs.power;

import org.lwjgl.util.Point;

import com.marklynch.actions.Action;
import com.marklynch.level.Level;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.animation.Animation;
import com.marklynch.level.constructs.animation.Animation.OnCompletionListener;
import com.marklynch.level.constructs.animation.primary.AnimationPush;
import com.marklynch.level.constructs.animation.secondary.AnimationThrown;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.effect.EffectBurn;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.Arrow;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.templates.Templates;
import com.marklynch.utils.ResourceUtils;

public class PowerInferno extends Power {

	private static String NAME = "Inferno";

	public PowerInferno() {
		this(null);
	}

	public PowerInferno(GameObject source) {
		super(NAME, ResourceUtils.getGlobalImage("action_burn.png", false), source,
				new Effect[] { new EffectBurn(source, null, 3) }, 5, null,
				new Point[] { new Point(0, 0), new Point(0, 1), new Point(0, -1), new Point(-1, 0), new Point(1, 0) },
				10, true, true, Crime.TYPE.CRIME_ASSAULT);
		selectTarget = true;
	}

	@Override
	public Power makeCopy(GameObject source) {
		return new PowerInferno(source);
	}

	@Override
	public void cast(final GameObject source, final GameObject targetGameObject, final Square targetSquare,
			final Action action) {

		if (source instanceof Actor) {
			source.setPrimaryAnimation(new AnimationPush(source, targetSquare, source.getPrimaryAnimation(), null));
			final Arrow fireBall = Templates.FIRE_BALL.makeCopy(null, null);
			Animation animationThrown = new AnimationThrown("Fire Ball", (Actor) source, action, targetGameObject,
					targetSquare, fireBall, source, 1f, 0f, true, new OnCompletionListener() {
						@Override
						public void animationComplete(GameObject gameObject) {
							PowerInferno.super.cast(source, targetGameObject, targetSquare, action);
						}
					});
			Level.addSecondaryAnimation(animationThrown);
		} else {
			PowerInferno.super.cast(source, targetGameObject, targetSquare, action);
		}
	}
}