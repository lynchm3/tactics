package com.marklynch.objects.inanimateobjects;

import java.util.concurrent.ConcurrentHashMap;

import com.marklynch.Game;
import com.marklynch.level.constructs.Crime;
import com.marklynch.level.squares.Square;
import com.marklynch.objects.actors.Actor;
import com.marklynch.objects.templates.Templates;
import com.marklynch.objects.utils.UpdatableGameObject;
import com.marklynch.utils.Color;
import com.marklynch.utils.CopyOnWriteArrayList;
import com.marklynch.utils.TextUtils;
import com.marklynch.utils.TextureUtils;

public class WantedPoster extends GameObject implements UpdatableGameObject {

	public static final CopyOnWriteArrayList<GameObject> instances = new CopyOnWriteArrayList<GameObject>(
			GameObject.class);

	public CopyOnWriteArrayList<Crime> crimes;
	public Actor criminal;
	public int reward;
	public int accumulatedSAeverity;

	public WantedPoster() {
		super();
		// type = "Wanted Poster";

	}

	@Override
	public void setInstances(GameObject gameObject) {
		instances.add(gameObject);
		super.setInstances(gameObject);
	}

	public WantedPoster makeCopy(Square square, String name, java.util.concurrent.CopyOnWriteArrayList<Crime> arrayList,
			Actor owner) {
		WantedPoster wantedPoster = new WantedPoster();
		setInstances(wantedPoster);
		super.setAttributesForCopy(wantedPoster, square, owner);
		wantedPoster.crimes = new CopyOnWriteArrayList<Crime>(Crime.class);
		wantedPoster.crimes.addAll(arrayList);
		conversation = createConversation(wantedPoster.generateText());
		return wantedPoster;
	}

	public void updateCrimes(java.util.concurrent.CopyOnWriteArrayList<Crime> crimes, Actor criminal) {
		this.crimes = new CopyOnWriteArrayList<Crime>(Crime.class);
		this.crimes.addAll(crimes);
		this.criminal = criminal;
	}

	@Override
	public boolean draw1() {

		boolean shouldDraw = super.draw1();
		if (!shouldDraw)
			return false;

		// Draw crim on the wanted poster
		if (criminal == null)
			return true;

		// Draw object
		if (squareGameObjectIsOn != null) {

			int actorPositionXInPixels = (int) (this.squareGameObjectIsOn.xInGridPixels + drawOffsetX
					+ Game.QUARTER_SQUARE_WIDTH);
			int actorPositionYInPixels = (int) (this.squareGameObjectIsOn.yInGridPixels + drawOffsetY
					+ Game.QUARTER_SQUARE_HEIGHT);
			float alpha = 1.0f;
			if (primaryAnimation != null)
				alpha = primaryAnimation.alpha;
			if (!this.squareGameObjectIsOn.visibleToPlayer)
				alpha = 0.5f;

			TextureUtils.drawTexture(criminal.imageTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
					actorPositionXInPixels + halfWidth, actorPositionYInPixels + halfWidth, backwards);

			if (flash) {
				TextureUtils.drawTexture(imageTexture, alpha, actorPositionXInPixels, actorPositionYInPixels,
						actorPositionXInPixels + halfWidth, actorPositionYInPixels + halfWidth, 0, 0, 0, 0, backwards,
						false, Color.BLACK, false);
			}

			Game.flush();
		}
		return true;
	}

	@Override
	public void update() {
		for (Crime crime : crimes) {
			crime.notifyWitnessesOfCrime();
		}
	}

	public Object[] generateText() {

		reward = 0;
		accumulatedSAeverity = 0;

		if (crimes.size() == 0) {

			criminal = null;
			Object[] conversationText = { "For official use only" };
			return conversationText;

		}
		CopyOnWriteArrayList<Crime.TYPE> uniqueCrimes = new CopyOnWriteArrayList<Crime.TYPE>(Crime.TYPE.class);
		ConcurrentHashMap<Crime.TYPE, Integer> crimeTypeCounts = new ConcurrentHashMap<Crime.TYPE, Integer>();
		for (Crime crime : crimes) {
			if (!uniqueCrimes.contains(crime.type)) {
				uniqueCrimes.add(crime.type);
			}
			if (crimeTypeCounts.containsKey(crime.type)) {
				crimeTypeCounts.put(crime.type, crimeTypeCounts.get(crime.type) + 1);
			} else {
				crimeTypeCounts.put(crime.type, 1);
			}
			reward += crime.type.severity * 100;
			accumulatedSAeverity += crime.type.severity;
			crime.addCrimeListener(this);
		}

		String crimesString = "For ";
		for (Crime.TYPE crimeType : uniqueCrimes) {
			crimesString += crimeTypeCounts.get(crimeType) + " counts of " + crimeType.name;
			if (crimeType == uniqueCrimes.get(uniqueCrimes.size() - 1)) {

			} else if (crimeType == uniqueCrimes.get(uniqueCrimes.size() - 2)) {
				crimesString += " and ";
			} else {
				crimesString += ", ";
			}
		}

		Object[] conversationText = { "WANTED!", TextUtils.NewLine.NEW_LINE, crimesString, TextUtils.NewLine.NEW_LINE,
				Templates.GOLD.imageTexture, "Reward " + reward };

		return conversationText;

	}

	public void notifyWitnesses() {

	}

	@Override
	public void crimeUpdate(Crime crime) {
		if (crime.isResolved()) {
			crimes.remove(crime);
		}
		conversation = createConversation(generateText());
	}
}
