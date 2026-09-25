package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlTier;
import com.hongbao.bloons.entities.UpgradeNode;

import java.util.Arrays;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public class GirlFactory {

	public static Girl createReimu() {
		GirlTier baseTier = new GirlTier(86, 20f, 1, 4, 500f, 200f, true);
		
		GirlTier homingTier2 = new GirlTier(60, 25f, 3, 12, 550f, 250f, true);
		UpgradeNode node1A = new UpgradeNode("Divine Spirit", 350, homingTier2, 2);

		GirlTier homingTier1 = new GirlTier(75, 22f, 2, 8, 500f, 220f, true);
		UpgradeNode node1 = new UpgradeNode("Homing Master", 200, homingTier1, Arrays.asList(node1A), 1);

		GirlTier barrierTier2 = new GirlTier(70, 20f, 2, 32, 600f, 250f, true);
		UpgradeNode node2A = new UpgradeNode("Fantasy Seal", 400, barrierTier2, 2);

		GirlTier barrierTier1 = new GirlTier(86, 20f, 1, 16, 550f, 220f, true);
		UpgradeNode node2 = new UpgradeNode("Shrine Maiden Barrier", 250, barrierTier1, Arrays.asList(node2A), 1);

		UpgradeNode root = new UpgradeNode("Base Reimu", 0, baseTier, Arrays.asList(node1, node2), 0);

		return new Girl("Reimu", root, "reimu.png", "red_spell_card.png", 325);
	}

	public static Girl createMarisa() {
		GirlTier baseTier = new GirlTier(56, 35f, 1, 1, 500f, 100f, false);

		GirlTier sparkTier2 = new GirlTier(45, 75f, 4, 5, 600f, 150f, false);
		UpgradeNode node1A = new UpgradeNode("Final Spark", 300, sparkTier2, 2);

		GirlTier sparkTier1 = new GirlTier(56, 50f, 2, 2, 500f, 125f, false);
		UpgradeNode node1 = new UpgradeNode("Master Spark", 140, sparkTier1, Arrays.asList(node1A), 1);

		GirlTier meteorTier2 = new GirlTier(30, 50f, 2, 6, 700f, 200f, true);
		UpgradeNode node2A = new UpgradeNode("Meteor Swarm", 280, meteorTier2, 2);

		GirlTier starlightTier1 = new GirlTier(40, 40f, 1, 3, 600f, 150f, true);
		UpgradeNode node2 = new UpgradeNode("Starlight Missile", 160, starlightTier1, Arrays.asList(node2A), 1);

		UpgradeNode root = new UpgradeNode("Base Marisa", 0, baseTier, Arrays.asList(node1, node2), 0);

		return new Girl("Marisa", root, "marisa.png", "blue_magic_missile.png", 200);
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

	public static Girl createRemilia() {
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

}
