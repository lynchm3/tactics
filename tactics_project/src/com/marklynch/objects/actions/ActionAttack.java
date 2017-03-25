package com.marklynch.objects.actions;

import com.marklynch.Game;
import com.marklynch.level.constructs.Sound;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.units.Actor;
import com.marklynch.objects.weapons.Projectile;
import com.marklynch.ui.ActivityLog;

public class ActionAttack extends Action {

	public static final String ACTION_NAME = "Attack";
	public static final String ACTION_NAME_DISABLED = ACTION_NAME + " (can't reach)";

	Actor attacker;
	GameObject target;

	// Default for hostiles
	public ActionAttack(Actor attacker, GameObject target) {
		super(ACTION_NAME);
		this.attacker = attacker;
		this.target = target;
		if (!check()) {
			enabled = false;
			actionName = ACTION_NAME_DISABLED;
		}
	}

	@Override
	public void perform() {

		if (!enabled)
			return;

		boolean illegal = false;
		if (illegal)
			attacker.performingIllegalAction = true;
		// performer.attack(targetGameObject, false);

		// GameObject targetGameObject;// = target;

		if (target instanceof Actor) {
			attacker.addAttackerForThisAndGroupMembers((Actor) target);
			attacker.addAttackerForNearbyFactionMembersIfVisible((Actor) target);
			((Actor) target).addAttackerForNearbyFactionMembersIfVisible(attacker);
		}
		target.remainingHealth -= attacker.equippedWeapon.getEffectiveSlashDamage();
		attacker.distanceMovedThisTurn = attacker.travelDistance;
		attacker.hasAttackedThisTurn = true;
		String attackTypeString;
		attackTypeString = "attacked ";

		if (attacker.squareGameObjectIsOn.visibleToPlayer)
			Game.level.logOnScreen(new ActivityLog(new Object[] {

					attacker, " " + attackTypeString + " ", target, " with ", attacker.equippedWeapon.imageTexture,
					" for " + attacker.equippedWeapon.getEffectiveSlashDamage() + " damage" }));

		Actor actor = null;
		if (target instanceof Actor)
			actor = (Actor) target;

		if (target.checkIfDestroyed()) {
			if (target instanceof Actor) {
				if (attacker.squareGameObjectIsOn.visibleToPlayer)
					Game.level.logOnScreen(new ActivityLog(new Object[] { attacker, " killed ", target }));
				((Actor) target).faction.checkIfDestroyed();
			} else {
				if (attacker.squareGameObjectIsOn.visibleToPlayer)
					Game.level.logOnScreen(new ActivityLog(new Object[] { attacker, " destroyed a ", target }));
			}

		}

		// shoot projectile
		if (attacker.straightLineDistanceTo(target.squareGameObjectIsOn) > 1) {
			Game.level.projectiles.add(new Projectile("Arrow", attacker, target, 5f, true, "hunter.png"));
		} else {
			attacker.showPow(target);
		}

		// Sound
		float loudness = target.soundWhenHit * attacker.equippedWeapon.soundWhenHitting;
		if (attacker.equippedWeapon != null)
			attacker.sounds.add(new Sound(attacker, attacker.equippedWeapon, attacker.squareGameObjectIsOn, loudness,
					illegal, this.getClass()));

		if (attacker.faction == Game.level.factions.get(0)) {
			Game.level.undoList.clear();
			Game.level.undoButton.enabled = false;
		}

		if (attacker == Game.level.player)
			Game.level.endTurn();
	}

	@Override
	public boolean check() {
		if (!attacker.visibleFrom(target.squareGameObjectIsOn))
			return false;

		if (!attacker.equippedWeapon.hasRange(attacker.straightLineDistanceTo(target.squareGameObjectIsOn)))
			return false;

		return true;
	}

}
