package com.marklynch.objects.weapons;

import com.marklynch.level.Square;
import com.marklynch.objects.GameObject;
import com.marklynch.objects.Inventory;

public class WeaponTemplate extends GameObject {
	public final static String[] editableAttributes = { "name", "imageTexture", "damage", "minRange", "maxRange",
			"totalHealth", "remainingHealth", "owner", "inventory", "showInventory", "fitsInInventory",
			"canContainOtherObjects" };

	// attributes
	public float damage = 0;
	public float minRange = 0;
	public float maxRange = 0;

	public WeaponTemplate(String name, float damage, float minRange, float maxRange, String imagePath, float health,
			Square squareGameObjectIsOn, boolean fitsInInventory, boolean canContainOtherObjects, float widthRatio, float heightRatio) {

		super(name, (int) health, imagePath, squareGameObjectIsOn, new Inventory(), false, true, fitsInInventory,
				canContainOtherObjects, widthRatio, heightRatio);

		this.damage = damage;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	@Override
	public GameObject makeCopy(Square square) {
		return new Weapon(new String(name), damage, minRange, maxRange, imageTexturePath, totalHealth, square,
				fitsInInventory, canContainOtherObjects, widthRatio, heightRatio);
	}
}
