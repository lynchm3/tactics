package com.marklynch.level.constructs.power;

import org.lwjgl.util.Point;

import com.marklynch.actions.Action;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.animation.primary.AnimationPush;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.constructs.effect.EffectBleed;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.utils.ResourceUtils;

public class PowerBleed extends Power {

	private static String NAME = "bleed";

	public PowerBleed() {
		this(null);
	}

	public PowerBleed(GameObject source) {
		super(NAME, ResourceUtils.getGlobalImage("effect_bleed.png", false), source,
				new Effect[] { new EffectBleed(source, null, 3) }, 5, null, new Point[] { new Point(0, 0) }, 10, true,
				true, Crime.TYPE.CRIME_ASSAULT);
		selectTarget = true;
	}

	@Override
	public Power makeCopy(GameObject source) {
		return new PowerBleed(source);
	}

	@Override
	public void cast(final GameObject source, GameObject targetGameObject, Square targetSquare, final Action action) {
		source.setPrimaryAnimation(new AnimationPush(source, targetSquare, source.getPrimaryAnimation(), null));
		super.cast(source, targetGameObject, targetSquare, action);

	}
}
