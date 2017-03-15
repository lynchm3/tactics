package com.marklynch.objects;

import com.marklynch.objects.units.Actor;
import com.marklynch.objects.units.Blind;
import com.marklynch.objects.units.Hunter;
import com.marklynch.objects.weapons.Weapon;

public class Templates {

	// People
	public static final Actor OLD_LADY = new Actor("You", "Fighter", 10, 100, 0, 0, 0, 0, "red1.png", null, 1, 10, null,
			new Inventory(), true, false, true, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f,
			0f, 0f, null, 80f, 80f, 10f);
	public static final Actor HUNTER = new Hunter("Hunter", "Hunter", 1, 10, 0, 0, 0, 0, "hunter.png", null, 1, 10,
			null, new Inventory(), true, false, true, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false,
			0f, 0f, 0f, 0f, null, 88, 54, 10f);
	public static final Actor BLIND = new Blind("Blind", "Blind", 1, 10, 0, 0, 0, 0, "blind.png", null, 1, 1, null,
			new Inventory(), true, false, true, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f,
			0f, 0f, null, 88, 54, 20f);

	// Tools
	public static final Weapon BROOM = new Weapon("Broom", 1, 1, 1, "broom.png", 100, null, true, false, false, false,
			1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 59, 63);
	public static final Weapon PICKAXE = new Weapon("Pickaxe", 3, 1, 1, "pickaxe.png", 100, null, true, false, false,
			false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon HOE = new Weapon("Hoe", 2, 1, 1, "hoe.png", 100, null, true, false, false, false, 1f, 1f,
			0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon SICKLE = new Weapon("Sickle", 3, 1, 1, "sickle.png", 100, null, true, false, false,
			false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon HAMMER = new Weapon("Hammer", 4, 1, 1, "hammer.png", 100, null, true, false, false,
			false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon BASKET = new Weapon("Basket", 1, 1, 1, "basket.png", 100, null, true, false, false,
			false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon BUCKET = new Weapon("Bucket", 1, 1, 1, "bucket.png", 100, null, true, false, false,
			false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon WHIP = new Weapon("Whip", 3, 1, 1, "whip.png", 100, null, true, false, false, false, 1f,
			1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);
	public static final Weapon SERRATED_SPOON = new Weapon("Serrated Spoon", 1, 1, 1, "serrated_spoon.png", 100, null,
			true, false, false, false, 0.5f, 0.5f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 16, 24);

	// Blades
	public static final Weapon KATANA = new Weapon("Katana", 10, 1, 1, "katana.png", 100, null, true, false, false,
			false, 1f, 0.5f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 22, 15);

	// Axes
	// https://en.wikipedia.org/wiki/Axe#Types_of_axes
	public static final Weapon HATCHET = new Weapon("Hatchet", 3, 1, 1, "a3r1.png", 100, null, true, false, false,
			false, 0.5f, 0.5f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 24, 40);

	// Bows
	// https://en.wikipedia.org/wiki/Bow_and_arrow#Types_of_bow
	public static final Weapon HUNTING_BOW = new Weapon("Hunting Bow", 1, 1, 4, "a2r2.png", 100, null, true, false,
			false, false, 0.5f, 0.5f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f, 40, 42);

	// Furniture
	public static final Bed BED = new Bed("Bed", 5, "bed.png", "bed_Covers.png", null, new Inventory(), false, true,
			false, false, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f);
	public static final Sign SIGN = new Sign("Sign", 5, "sign.png", null, new Inventory(), true, false, false, true,
			false, true, new Object[] { "" }, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f);
	public static final GameObjectTemplate SHOP_COUNTER = new GameObjectTemplate("Shop Counter", 5, "shop_counter.png",
			null, new Inventory(), false, true, false, true, false, false, 1f, 1f, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f,
			false, 0f, 0f, 0f, 0f);
	public static final Sign ROCK_WITH_ETCHING = new Sign("Rock with Etching", 1000, "rock_with_etching.png", null,
			new Inventory(), true, false, false, true, false, true, new Object[] { "" }, 1, 1, 0.5f, 0.5f, 20f, 1f,
			null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f);

	// LARGE CONTAINER
	public static final GameObjectTemplate DUMPSTER = new GameObjectTemplate("dumpster", 5, "skip_with_shadow.png",
			null, new Inventory(), true, false, false, true, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f,
			false, 0f, 0f, 0f, 0f);

	// JUNK
	public static final Junk furTemplate = new Junk("Fur", 5, "fur.png", null, new Inventory(), false, true, true,
			false, false, false, 1, 1, 0.5f, 0.5f, 1f, 1f, null, 0.5f, 0.5f, false, 0f, 0f, 0f, 0f);

	// NATURE
	// BURROW
	// TREE
	//

}
