package com.hongbao.bloons.entities;

import com.badlogic.gdx.Input;

import java.util.Arrays;
import java.util.function.Supplier;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public enum TowerType {

	REIMU("Reimu", "img/ui/reimu_box.png", 325, Input.Keys.NUM_1, () -> new Girl(
			"Reimu",
			Arrays.asList(86, 86, 75),
			Arrays.asList(20f, 20f, 20f),
			Arrays.asList(1, 1, 1),
			Arrays.asList(4, 8, 13),
			Arrays.asList(500f, 500f, 500f),
			Arrays.asList(200f, 220f, 250f),
			Arrays.asList(true, true, true),
			"reimu.png",
			"red_spell_card.png",
			325,
			Arrays.asList(200, 280, NO_UPGRADES_AVAILABLE)
	)),

	YUKARI("Yukari", "img/ui/yukari_box.png", 2500, Input.Keys.NUM_2, () -> new Girl(
			"Yukari",
			Arrays.asList(4, 4, 4),
			Arrays.asList(100f, 100f, 100f),
			Arrays.asList(1, 1, 2),
			Arrays.asList(1, 2, 4),
			Arrays.asList(600f, 700f, 1000f),
			Arrays.asList(300f, 400f, 500f),
			Arrays.asList(false, false, false),
			"yukari.png",
			"purple_energy.png",
			2500,
			Arrays.asList(2500, 4500, NO_UPGRADES_AVAILABLE)
	)),

	MARISA("Marisa", "img/ui/marisa_box.png", 200, Input.Keys.NUM_3, () -> new Girl(
			"Marisa",
			Arrays.asList(56, 56, 56),
			Arrays.asList(35f, 50f, 75f),
			Arrays.asList(1, 1, 1),
			Arrays.asList(1, 2, 4),
			Arrays.asList(500f, 500f, 500f),
			Arrays.asList(100f, 125f, 150f),
			Arrays.asList(false, false, false),
			"marisa.png",
			"blue_magic_missile.png",
			200,
			Arrays.asList(140, 220, NO_UPGRADES_AVAILABLE)
	)),

	ALICE("Alice", "img/ui/alice_box.png", 450, Input.Keys.NUM_4, () -> new Girl(
			"Alice",
			Arrays.asList(53, 53, 53),
			Arrays.asList(50f, 50f, 50f),
			Arrays.asList(1, 1, 2),
			Arrays.asList(2, 2, 2),
			Arrays.asList(600f, 600f, 600f),
			Arrays.asList(200f, 250f, 300f),
			Arrays.asList(false, true, true),
			"alice.png",
			"magic_spike.png",
			450,
			Arrays.asList(150, 600, NO_UPGRADES_AVAILABLE)
	)),

	SAKUYA("Sakuya", "img/ui/sakuya_box.png", 500, Input.Keys.NUM_5, () -> new Girl(
			"Sakuya",
			Arrays.asList(30, 20, 20),
			Arrays.asList(20f, 20f, 20f),
			Arrays.asList(1, 1, 1),
			Arrays.asList(2, 2, 4),
			Arrays.asList(500f, 500f, 500f),
			Arrays.asList(150f, 200f, 250f),
			Arrays.asList(false, false, false),
			"sakuya.png",
			"blue_knives.png",
			500,
			Arrays.asList(300, 350, NO_UPGRADES_AVAILABLE)
	)),

	REMILIA("Remilia", "img/ui/remilia_box.png", 2500, Input.Keys.NUM_6, () -> new Girl(
			"Remilia",
			Arrays.asList(4, 2, 2),
			Arrays.asList(100f, 100f, 100f),
			Arrays.asList(1, 1, 1),
			Arrays.asList(1, 2, 4),
			Arrays.asList(600f, 700f, 1000f),
			Arrays.asList(300f, 400f, 500f),
			Arrays.asList(false, false, false),
			"remilia.png",
			"bat.png",
			2500,
			Arrays.asList(4000, 5000, NO_UPGRADES_AVAILABLE)
	)),

	YOUMU("Youmu", "img/ui/youmu_box.png", 600, Input.Keys.NUM_7, () -> new Girl(
			"Youmu",
			Arrays.asList(90, 90, 90),
			Arrays.asList(20f, 60f, 60f),
			Arrays.asList(1, 1, 2),
			Arrays.asList(18, 28, 28),
			Arrays.asList(200f, 300f, 400f),
			Arrays.asList(200f, 300f, 400f),
			Arrays.asList(false, false, false),
			"youmu.png",
			"sword_slash.png",
			600,
			Arrays.asList(400, 800, NO_UPGRADES_AVAILABLE)
	)),

	YUYUKO("Yuyuko", "img/ui/yuyuko_box.png", 2000, Input.Keys.NUM_8, () -> new Girl(
			"Yuyuko",
			Arrays.asList(40, 30, 20),
			Arrays.asList(10f, 11f, 12f),
			Arrays.asList(20, 20, 20),
			Arrays.asList(1, 1, 1),
			Arrays.asList(500f, 600f, 700f),
			Arrays.asList(200f, 250f, 300f),
			Arrays.asList(true, true, true),
			"yuyuko.png",
			"pink_butterfly.png",
			2000,
			Arrays.asList(500, 1500, NO_UPGRADES_AVAILABLE)
	));

	private final String displayName;
	private final String boxImagePath;
	private final int cost;
	private final int hotkey;
	private final Supplier<Girl> creator;

	TowerType(String displayName, String boxImagePath, int cost, int hotkey, Supplier<Girl> creator) {
		this.displayName = displayName;
		this.boxImagePath = boxImagePath;
		this.cost = cost;
		this.hotkey = hotkey;
		this.creator = creator;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getBoxImagePath() {
		return boxImagePath;
	}

	public int getCost() {
		return cost;
	}

	public int getBaseCost() {
		return cost;
	}

	public int getHotkey() {
		return hotkey;
	}

	public Supplier<Girl> getCreator() {
		return creator;
	}

	public Girl create() {
		return creator.get();
	}

	public Girl createGirl() {
		return creator.get();
	}
}
