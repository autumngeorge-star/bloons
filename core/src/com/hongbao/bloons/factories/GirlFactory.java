package com.hongbao.bloons.factories;

import com.hongbao.bloons.entities.Girl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;


public class GirlFactory {

	static {
		registerDefaultTowers();
	}

	public static synchronized void registerDefaultTowers() {
		if (TowerRegistry.getTower("Reimu") == null) {
			registerTower("Reimu", GirlFactory::buildReimu, "img/ui/reimu_box.png");
			registerTower("Yukari", GirlFactory::buildYukari, "img/ui/yukari_box.png");
			registerTower("Marisa", GirlFactory::buildMarisa, "img/ui/marisa_box.png");
			registerTower("Alice", GirlFactory::buildAlice, "img/ui/alice_box.png");
			registerTower("Sakuya", GirlFactory::buildSakuya, "img/ui/sakuya_box.png");
			registerTower("Remilia", GirlFactory::buildRemilia, "img/ui/remilia_box.png");
			registerTower("Youmu", GirlFactory::buildYoumu, "img/ui/youmu_box.png");
			registerTower("Yuyuko", GirlFactory::buildYuyuko, "img/ui/yuyuko_box.png");
		}
	}

	public static void registerTower(TowerDefinition definition) {
		TowerRegistry.registerTower(definition);
	}

	public static void registerTower(String id, Supplier<Girl> supplier, String uiIconPath) {
		TowerRegistry.registerTower(id, supplier, uiIconPath);
	}

	public static List<TowerDefinition> getRegisteredTowers() {
		return TowerRegistry.getRegisteredTowers();
	}

	public static TowerDefinition getTower(String id) {
		return TowerRegistry.getTower(id);
	}

	public static Girl createReimu() {
		TowerDefinition def = getTower("Reimu");
		return def != null ? def.createGirl() : buildReimu();
	}

	public static Girl createYukari() {
		TowerDefinition def = getTower("Yukari");
		return def != null ? def.createGirl() : buildYukari();
	}

	public static Girl createMarisa() {
		TowerDefinition def = getTower("Marisa");
		return def != null ? def.createGirl() : buildMarisa();
	}

	public static Girl createAlice() {
		TowerDefinition def = getTower("Alice");
		return def != null ? def.createGirl() : buildAlice();
	}

	public static Girl createSakuya() {
		TowerDefinition def = getTower("Sakuya");
		return def != null ? def.createGirl() : buildSakuya();
	}

	public static Girl createRemilia() {
		TowerDefinition def = getTower("Remilia");
		return def != null ? def.createGirl() : buildRemilia();
	}

	public static Girl createYoumu() {
		TowerDefinition def = getTower("Youmu");
		return def != null ? def.createGirl() : buildYoumu();
	}

	public static Girl createYuyuko() {
		TowerDefinition def = getTower("Yuyuko");
		return def != null ? def.createGirl() : buildYuyuko();
	}

	private static Girl buildReimu() {
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

	private static Girl buildYukari() {
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

	private static Girl buildMarisa() {
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

	private static Girl buildAlice() {
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

	private static Girl buildSakuya() {
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

	private static Girl buildRemilia() {
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

	private static Girl buildYoumu() {
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

	private static Girl buildYuyuko() {
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
