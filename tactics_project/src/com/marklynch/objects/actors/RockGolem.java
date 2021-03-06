package com.marklynch.objects.actors;

import com.marklynch.Game;
import com.marklynch.actions.Action;
import com.marklynch.actions.ActionAttack;
import com.marklynch.actions.ActionLift;
import com.marklynch.ai.routines.AIRoutineForRockGolem;
import com.marklynch.level.constructs.Faction;
import com.marklynch.level.constructs.bounds.structure.structureroom.StructureRoom;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.inanimateobjects.GameObject;
import com.marklynch.ui.ActivityLog;
import com.marklynch.utils.CopyOnWriteArrayList;
import com.marklynch.utils.ResourceUtils;
import com.marklynch.utils.Texture;
import com.marklynch.utils.TextureUtils;

public class RockGolem extends Monster {

	public static final CopyOnWriteArrayList<GameObject> instances = new CopyOnWriteArrayList<GameObject>(GameObject.class);

	public StructureRoom roomLivingIn;
	public boolean awake = false;
	public Texture sleepingTexture;
	public Texture awakeTexture;

	public RockGolem() {
		super();
		aiRoutine = new AIRoutineForRockGolem(this);
		sleepingTexture = ResourceUtils.getGlobalImage("rock_golem_sleeping.png", false);
		awakeTexture = ResourceUtils.getGlobalImage("rock_golem.png", false);

		anchorX = 16;
		anchorY = 64;

		this.blocksLineOfSight = true;
		this.name = "Suspicious Boulder";
		imageTexture = sleepingTexture;

	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	@Override
	public boolean draw1() {
		// if (this.squareGameObjectIsOn.visibleToPlayer == false &&
		// persistsWhenCantBeSeen == false)
		// return;
		//
		// if (!this.squareGameObjectIsOn.seenByPlayer)
		// return;
		if (awake) {
			return super.draw1();
		}

		if (!shouldDraw())
			return false;

		// Draw object
		float actorPositionXInPixels = this.squareGameObjectIsOn.xInGridPixels;
		float actorPositionYInPixels = this.squareGameObjectIsOn.yInGridPixels;

		float alpha = 1.0f;

		// TextureUtils.skipNormals = true;

		if (primaryAnimation != null)
			alpha = primaryAnimation.alpha;
		if (!this.squareGameObjectIsOn.visibleToPlayer)
			alpha = 0.5f;
		TextureUtils.drawTexture(sleepingTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
				actorPositionXInPixels + 64, actorPositionYInPixels + 64, backwards);
		// TextureUtils.skipNormals = false;
		return true;

	}

	@Override
	public void postLoad1() {
		super.postLoad1();
		aiRoutine = new AIRoutineForRockGolem(this);
	}

	@Override
	public void postLoad2() {
		super.postLoad2();
	}

	@Override
	public void attackedBy(Object attacker, Action action) {
		if (!awake)
			wakeUp();
		super.attackedBy(attacker, action);
	}

	@Override
	public Action getSecondaryActionPerformedOnThisInWorld(Actor performer) {
		if (!awake) {
			return null;
		} else {
			ActionAttack actionAttack = new ActionAttack(performer, this);
			return actionAttack;
		}
	}

	@Override
	public CopyOnWriteArrayList<Action> getAllActionsPerformedOnThisInWorld(Actor performer) {
		CopyOnWriteArrayList<Action> actions = new CopyOnWriteArrayList<Action>(Action.class);
		// Pick up
		if (!awake)
			actions.add(new ActionLift(performer, this));

		actions.addAll(super.getAllActionsPerformedOnThisInWorld(performer));

		// Attack
		// actions.add(new ActionAttack(performer, this));
		// actions.add(new ActionThrow(performer, this, performer.equipped));
		return actions;
	}

	@Override
	public void landed(Actor shooter, Action action) {
		if (!awake)
			wakeUp();
		changeHealth(-10, shooter, action);
	}

	public void wakeUp() {

		this.awake = true;
		this.blocksLineOfSight = false;
		this.canBePickedUp = false;
		this.name = "Rock Golem";
		imageTexture = awakeTexture;
		if (Game.level.shouldLog(this))
			Game.level.logOnScreen(new ActivityLog(new Object[] { this, " woke up" }));
	}

	public void sleep() {

		this.awake = false;
		this.blocksLineOfSight = true;
		this.canBePickedUp = true;
		this.name = "Suspicious Boulder";
		imageTexture = sleepingTexture;
		if (Game.level.shouldLog(this))
			Game.level.logOnScreen(new ActivityLog(new Object[] { this, " went to sleep" }));

	}

	public RockGolem makeCopy(String name, Square square, Faction faction, StructureRoom roomLivingIn,
			GameObject[] mustHaves, GameObject[] mightHaves) {

		RockGolem actor = new RockGolem();
		setInstances(actor);
		super.setAttributesForCopy(name, actor, square, faction, bed, 0, mustHaves, mightHaves, area);

		this.roomLivingIn = roomLivingIn;

		return actor;
	}

}
