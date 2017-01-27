package com.marklynch.tactics.objects.weapons;

import com.marklynch.tactics.objects.GameObject;
import com.marklynch.tactics.objects.GameObjectTemplate;
import com.marklynch.tactics.objects.level.Square;

public class WeaponTemplate extends GameObjectTemplate {
	public final static String[] editableAttributes = { "name", "imageTexture", "damage", "minRange", "maxRange",
			"totalHealth", "remainingHealth", "owner", "inventory", "showInventory", "fitsInInventory",
			"canContainOtherObjects" };

	// attributes
	public float damage = 0;
	public float minRange = 0;
	public float maxRange = 0;

	public WeaponTemplate(String name, float damage, float minRange, float maxRange, String imagePath, float health,
			Square squareGameObjectIsOn, boolean fitsInInventory, boolean canContainOtherObjects) {

		super(name, (int) health, imagePath, squareGameObjectIsOn, null, false, true, fitsInInventory,
				canContainOtherObjects);

		this.damage = damage;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	@Override
	public GameObject makeCopy(Square square) {
		return new Weapon(new String(name), damage, minRange, maxRange, imageTexturePath, owner, totalHealth, square,
				fitsInInventory, canContainOtherObjects);
	}
}
