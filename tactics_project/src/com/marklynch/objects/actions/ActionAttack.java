package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.weapons.Projectile;
import com.marklynch.objects.weapons.Weapon;
import com.marklynch.ui.ActivityLog;

public class ActionAttack extends Action {

	public static final String ACTION_NAME = "Attack";
	public static final String ACTION_NAME_DISABLED = ACTION_NAME + " (can't reach)";

	Actor performer;
	GameObject target;

	// Default for hostiles
	public ActionAttack(Actor attacker, GameObject target) {
		super(ACTION_NAME);
		this.performer = attacker;
		this.target = target;
		if (!check()) {
			enabled = false;
			actionName = ACTION_NAME_DISABLED;
		}
		legal = checkLegality();
		sound = createSound();
	}

	@Override
	public void perform() {

		if (!enabled)
			return;

		Weapon weapon = (Weapon) performer.equipped;

		// performer.attack(targetGameObject, false);

		// GameObject targetGameObject;// = target;

		if (target instanceof Actor) {
			performer.addAttackerForThisAndGroupMembers((Actor) target);
			performer.addAttackerForNearbyFactionMembersIfVisible((Actor) target);
			((Actor) target).addAttackerForNearbyFactionMembersIfVisible(performer);
		}
		target.remainingHealth -= weapon.getEffectiveSlashDamage();
		performer.distanceMovedThisTurn = performer.travelDistance;
		performer.hasAttackedThisTurn = true;
		String attackTypeString;
		attackTypeString = "attacked ";

		if (performer.squareGameObjectIsOn.visibleToPlayer)
			Game.level.logOnScreen(new ActivityLog(new Object[] {

					performer, " " + attackTypeString + " ", target, " with ", performer.equipped.imageTexture,
					" for " + weapon.getEffectiveSlashDamage() + " damage" }));

		Actor actor = null;
		if (target instanceof Actor)
			actor = (Actor) target;

		if (target.checkIfDestroyed()) {
			if (target instanceof Actor) {
				if (performer.squareGameObjectIsOn.visibleToPlayer)
					Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " killed ", target }));
				((Actor) target).faction.checkIfDestroyed();
			} else {
				if (performer.squareGameObjectIsOn.visibleToPlayer)
					Game.level.logOnScreen(new ActivityLog(new Object[] { performer, " destroyed a ", target }));
			}

		}

		// shoot projectile
		if (performer.straightLineDistanceTo(target.squareGameObjectIsOn) > 1) {
			Game.level.projectiles.add(new Projectile("Arrow", performer, target, 5f, true, "hunter.png"));
		} else {
			performer.showPow(target);
		}

		if (performer.faction == Game.level.factions.get(0)) {
			Game.level.undoList.clear();
			Game.level.undoButton.enabled = false;
		}

		if (performer == Game.level.player)
			Game.level.endTurn();

		performer.actions.add(this);
		if (sound != null)
			sound.play();
		if (sound != null)
			sound.play();
	}

	@Override
	public boolean check() {

		if (!performer.visibleFrom(target.squareGameObjectIsOn))
			return false;

		Weapon weapon = null;
		if (performer.equipped instanceof Weapon) {
			weapon = (Weapon) performer.equipped;
		} else {
			return false;
		}

		if (!weapon.hasRange(performer.straightLineDistanceTo(target.squareGameObjectIsOn)))
			return false;

		return true;
	}

	@Override
	public boolean checkLegality() {
		if (target.owner != null && target.owner != Game.level.player)
			return false;
		return true;
	}

	@Override
	public Sound createSound() {

		// Sound
		float loudness = target.soundWhenHit * performer.equipped.soundWhenHitting;
		if (performer.equipped != null)
			return new Sound(performer, performer.equipped, target.squareGameObjectIsOn, loudness, legal,
					this.getClass());
		return null;
	}

}
