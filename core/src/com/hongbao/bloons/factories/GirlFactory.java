package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;

import java.util.Arrays;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public class GirlFactory {

	public static Girl createReimu() {
		return new Girl(
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
		);
	}

	public static Girl createYukari() {
		return new Girl("Yukari",
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
		);
	}

	public static Girl createMarisa() {
		return new Girl(
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
		);
	}

	public static Girl createAlice() {
		return new Girl(
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
		);
	}

	public static Girl createSakuya() {
		return new Girl(
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
		);
	}

	public static Girl createRemilia() { // 3000
		return new Girl(
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
		);
	}

	public static Girl createYoumu() {
		return new Girl(
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
		);
	}

	public static Girl createYuyuko() {
		return new Girl(
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
		);
	}

	public static Girl createByName(String name) {
		if (name == null) {
			return null;
		}
		switch (name) {
			case "Reimu":
				return createReimu();
			case "Yukari":
				return createYukari();
			case "Marisa":
				return createMarisa();
			case "Alice":
				return createAlice();
			case "Sakuya":
				return createSakuya();
			case "Remilia":
				return createRemilia();
			case "Youmu":
				return createYoumu();
			case "Yuyuko":
				return createYuyuko();
			default:
				return null;
		}
	}

}
