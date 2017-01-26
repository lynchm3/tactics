package com.marklynch.tactics.objects.weapons;

import com.marklynch.tactics.objects.GameObject;
import com.marklynch.tactics.objects.level.Square;

public class WeaponTemplate extends GameObject {
	public final static String[] editableAttributes = { "name", "imageTexture", "damage", "minRange", "maxRange",
			"totalHealth", "remainingHealth", "owner", "inventory", "showInventory" };

	// attributes
	public float damage = 0;
	public float minRange = 0;
	public float maxRange = 0;

	public WeaponTemplate(String name, float damage, float minRange, float maxRange, String imagePath, float health,
			Square squareGameObjectIsOn) {

		super(name, (int) health, imagePath, squareGameObjectIsOn, null, false, true);

		this.damage = damage;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public Weapon makeWeapon() {
		return new Weapon(new String(name), damage, minRange, maxRange, imageTexturePath, owner, totalHealth,
				squareGameObjectIsOn);
	}
}
