package com.marklynch.actions;

import java.util.ArrayList;

import com.marklynch.Game;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.constructs.Sound;
import com.marklynch.level.constructs.effect.Effect;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.inanimateobjects.Carcass;
import com.marklynch.objects.inanimateobjects.Corpse;
import com.marklynch.objects.inanimateobjects.Food;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.objects.inanimateobjects.Liquid;
import com.marklynch.objects.inanimateobjects.WaterBody;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.tools.Jar;
import com.marklynch.objects.utils.Consumable;
import com.marklynch.ui.ActivityLog;

public class ActionEatItems extends VariableQtyAction {

	public static final String ACTION_NAME = "Eat";
	public static final String ACTION_NAME_DRINK = "Drink";

	GameObject[] targets;

	public ActionEatItems(Actor performer, ArrayList<GameObject> objects) {
		this(performer, objects.toArray(new GameObject[objects.size()]), false);
	}

	public ActionEatItems(Actor performer, GameObject... objects) {
		this(performer, objects, false);
	}

	public ActionEatItems(Actor performer, GameObject[] objects, boolean doesNothing) {
		super(ACTION_NAME, textureEat, performer, null);
		if (objects != null && objects.length > 0 && (objects[0] instanceof Liquid
				|| objects[0] instanceof Jar || objects[0] instanceof WaterBody)) {
			this.actionName = ACTION_NAME_DRINK;
			this.image = textureDrink;
		}
		this.targets = objects;
		if (!check()) {
			enabled = false;
		}
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {
		super.perform();

		if (!enabled)
			return;

		if (!checkRange())
			return;

		int amountToEat = Math.min(targets.length, qty);

		if (amountToEat == 0)
			return;

		for (int i = 0; i < amountToEat; i++) {
			GameObject object = targets[i];

			if (object instanceof Food || object instanceof Corpse || object instanceof Carcass
					|| object instanceof Liquid || object instanceof Jar) {
				// Inventory inventoryThatHoldsThisObject = object.inventoryThatHoldsThisObject;
				if (object.inventoryThatHoldsThisObject.parent instanceof Square) {
					if (object instanceof Jar) {
						Templates.JAR.makeCopy(gameObjectPerformer.squareGameObjectIsOn, null);
					}
				} else {
					if (object instanceof Jar) {
						object.inventoryThatHoldsThisObject.add(Templates.JAR.makeCopy(null, null));
					}
					object.inventoryThatHoldsThisObject.remove(object);
				}
			} else if (object instanceof WaterBody) {

			} else if (object.fitsInInventory) {
				performer.inventory.add(object);
			}

			if (Game.level.shouldLog(performer)) {
				String amountText = "";
				if (amountToEat > 1) {
					amountText = "x" + amountToEat;
				}

				String actionWord = " ate ";
				if (actionName == ACTION_NAME_DRINK) {
					actionWord = " drank ";
				}
				Game.level.logOnScreen(new ActivityLog(new Object[] { performer, actionWord, targets[0], amountText }));
			}

			// System.out.println("object = " + object);

			if (object instanceof Consumable) {
				Consumable consumable = (Consumable) object;
				if (consumable.getConsumeEffects() != null) {
					for (Effect effect : consumable.getConsumeEffects()) {
						performer.addEffect(effect.makeCopy(performer, performer));
					}
				}
			}

			if (object.owner == null)
				object.owner = performer;
			if (sound != null)
				sound.play();

			if (!legal) {
				Crime crime = new Crime(this.performer, object.owner, Crime.TYPE.CRIME_THEFT, object);
				this.performer.crimesPerformedThisTurn.add(crime);
				this.performer.crimesPerformedInLifetime.add(crime);
				notifyWitnessesOfCrime(crime);
			}
		}
	}

	@Override
	public boolean check() {
		return true;
	}

	@Override
	public boolean checkRange() {
		if (performer.straightLineDistanceTo(targets[0].squareGameObjectIsOn) < 2) {
			return true;
		}
		if (performer.inventory == targets[0].inventoryThatHoldsThisObject)
			return true;
		return false;
	}

	@Override
	public boolean checkLegality() {
		return standardAttackLegalityCheck(performer, targets[0]);
	}

	@Override
	public Sound createSound() {
		return null;
	}

}