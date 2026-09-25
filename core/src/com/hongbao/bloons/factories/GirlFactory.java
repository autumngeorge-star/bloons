package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlTier;

import java.util.Arrays;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public class GirlFactory {

	public static Girl createReimu() {
		return new Girl(
				"Reimu",
				Arrays.asList(
						new GirlTier(86, 20f, 1, 4, 500f, 200f, true, 200),
						new GirlTier(86, 20f, 1, 8, 500f, 220f, true, 280),
						new GirlTier(75, 20f, 1, 13, 500f, 250f, true, NO_UPGRADES_AVAILABLE)
				),
				"reimu.png",
				"red_spell_card.png",
				325
		);
	}

	public static Girl createYukari() {
		return new Girl(
				"Yukari",
				Arrays.asList(
						new GirlTier(4, 100f, 1, 1, 600f, 300f, false, 2500),
						new GirlTier(4, 100f, 1, 2, 700f, 400f, false, 4500),
						new GirlTier(4, 100f, 2, 4, 1000f, 500f, false, NO_UPGRADES_AVAILABLE)
				),
				"yukari.png",
				"purple_energy.png",
				2500
		);
	}

	public static Girl createMarisa() {
		return new Girl(
				"Marisa",
				Arrays.asList(
						new GirlTier(56, 35f, 1, 1, 500f, 100f, false, 140),
						new GirlTier(56, 50f, 1, 2, 500f, 125f, false, 220),
						new GirlTier(56, 75f, 1, 4, 500f, 150f, false, NO_UPGRADES_AVAILABLE)
				),
				"marisa.png",
				"blue_magic_missile.png",
				200
		);
	}

	public static Girl createAlice() {
		return new Girl(
				"Alice",
				Arrays.asList(
						new GirlTier(53, 50f, 1, 2, 600f, 200f, false, 150),
						new GirlTier(53, 50f, 1, 2, 600f, 250f, true, 600),
						new GirlTier(53, 50f, 2, 2, 600f, 300f, true, NO_UPGRADES_AVAILABLE)
				),
				"alice.png",
				"magic_spike.png",
				450
		);
	}

	public static Girl createSakuya() {
		return new Girl(
				"Sakuya",
				Arrays.asList(
						new GirlTier(30, 20f, 1, 2, 500f, 150f, false, 300),
						new GirlTier(20, 20f, 1, 2, 500f, 200f, false, 350),
						new GirlTier(20, 20f, 1, 4, 500f, 250f, false, NO_UPGRADES_AVAILABLE)
				),
				"sakuya.png",
				"blue_knives.png",
				500
		);
	}

	public static Girl createRemilia() {
		return new Girl(
				"Remilia",
				Arrays.asList(
						new GirlTier(4, 100f, 1, 1, 600f, 300f, false, 4000),
						new GirlTier(2, 100f, 1, 2, 700f, 400f, false, 5000),
						new GirlTier(2, 100f, 1, 4, 1000f, 500f, false, NO_UPGRADES_AVAILABLE)
				),
				"remilia.png",
				"bat.png",
				2500
		);
	}

	public static Girl createYoumu() {
		return new Girl(
				"Youmu",
				Arrays.asList(
						new GirlTier(90, 20f, 1, 18, 200f, 200f, false, 400),
						new GirlTier(90, 60f, 1, 28, 300f, 300f, false, 800),
						new GirlTier(90, 60f, 2, 28, 400f, 400f, false, NO_UPGRADES_AVAILABLE)
				),
				"youmu.png",
				"sword_slash.png",
				600
		);
	}

	public static Girl createYuyuko() {
		return new Girl(
				"Yuyuko",
				Arrays.asList(
						new GirlTier(40, 10f, 20, 1, 500f, 200f, true, 500),
						new GirlTier(30, 11f, 20, 1, 600f, 250f, true, 1500),
						new GirlTier(20, 12f, 20, 1, 700f, 300f, true, NO_UPGRADES_AVAILABLE)
				),
				"yuyuko.png",
				"pink_butterfly.png",
				2000
		);
	}

}
