package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.entities.Girl;
import com.hongbao.bloons.entities.GirlConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hongbao.bloons.entities.Girl.NO_UPGRADES_AVAILABLE;

public class GirlFactory {

	private static List<GirlConfig> loadedConfigs = null;

	public static synchronized List<GirlConfig> getLoadedConfigs() {
		if (loadedConfigs == null) {
			loadTowers();
		}
		return loadedConfigs;
	}

	public static synchronized void reloadTowers() {
		loadedConfigs = null;
		getLoadedConfigs();
	}

	public static synchronized void loadTowers() {
		loadedConfigs = new ArrayList<>();
		JsonReader reader = new JsonReader();
		JsonValue root = null;

		try {
			if (Gdx.files != null) {
				if (Gdx.files.internal("towers.json").exists()) {
					root = reader.parse(Gdx.files.internal("towers.json"));
				} else if (Gdx.files.internal("assets/towers.json").exists()) {
					root = reader.parse(Gdx.files.internal("assets/towers.json"));
				} else if (Gdx.files.internal("core/assets/towers.json").exists()) {
					root = reader.parse(Gdx.files.internal("core/assets/towers.json"));
				}
			}
		} catch (Exception e) {
			logError("Failed to parse towers.json via Gdx.files: " + e.getMessage());
		}

		if (root == null) {
			File[] candidateFiles = new File[]{
				new File("core/assets/towers.json"),
				new File("assets/towers.json"),
				new File("towers.json"),
				new File("../core/assets/towers.json")
			};
			for (File file : candidateFiles) {
				if (file.exists()) {
					try (InputStream is = new FileInputStream(file)) {
						root = reader.parse(is);
						break;
					} catch (Exception e) {
						logError("Failed to parse " + file.getPath() + ": " + e.getMessage());
					}
				}
			}
		}

		if (root == null) {
			try (InputStream is = GirlFactory.class.getResourceAsStream("/towers.json")) {
				if (is != null) {
					root = reader.parse(is);
				}
			} catch (Exception e) {
				logError("Failed to parse resource /towers.json: " + e.getMessage());
			}
		}

		if (root != null && root.isArray()) {
			for (JsonValue entry = root.child; entry != null; entry = entry.next) {
				try {
					GirlConfig config = GirlConfig.fromJsonValue(entry);
					loadedConfigs.add(config);
				} catch (Exception e) {
					logError("Error parsing tower entry from JSON: " + e.getMessage());
				}
			}
		} else if (root != null) {
			logError("towers.json root element is not an array.");
		}

		if (loadedConfigs.isEmpty()) {
			logError("towers.json could not be loaded or was empty. Using default fallback configurations.");
			loadedConfigs = getDefaultConfigs();
		}
	}

	public static Girl createGirl(GirlConfig config) {
		if (config == null) {
			return createReimu();
		}
		return new Girl(config);
	}

	public static Girl createGirlByName(String name) {
		for (GirlConfig config : getLoadedConfigs()) {
			if (config.getName() != null && config.getName().equalsIgnoreCase(name)) {
				return createGirl(config);
			}
		}
		return createFallbackGirlByName(name);
	}

	public static Girl createGirlByIndex(int index) {
		List<GirlConfig> configs = getLoadedConfigs();
		if (index >= 0 && index < configs.size()) {
			return createGirl(configs.get(index));
		}
		logError("Invalid tower index requested: " + index);
		return createReimu();
	}

	private static Girl createFallbackGirlByName(String name) {
		if (name == null) return createDefaultReimu();
		switch (name.toLowerCase()) {
			case "reimu": return createDefaultReimu();
			case "yukari": return createDefaultYukari();
			case "marisa": return createDefaultMarisa();
			case "alice": return createDefaultAlice();
			case "sakuya": return createDefaultSakuya();
			case "remilia": return createDefaultRemilia();
			case "youmu": return createDefaultYoumu();
			case "yuyuko": return createDefaultYuyuko();
			default: return createDefaultReimu();
		}
	}

	public static Girl createReimu() {
		return createGirlByName("Reimu");
	}

	public static Girl createYukari() {
		return createGirlByName("Yukari");
	}

	public static Girl createMarisa() {
		return createGirlByName("Marisa");
	}

	public static Girl createAlice() {
		return createGirlByName("Alice");
	}

	public static Girl createSakuya() {
		return createGirlByName("Sakuya");
	}

	public static Girl createRemilia() {
		return createGirlByName("Remilia");
	}

	public static Girl createYoumu() {
		return createGirlByName("Youmu");
	}

	public static Girl createYuyuko() {
		return createGirlByName("Yuyuko");
	}

	private static List<GirlConfig> getDefaultConfigs() {
		List<GirlConfig> defaults = new ArrayList<>();
		defaults.add(new GirlConfig("Reimu", Arrays.asList(86, 86, 75), Arrays.asList(20f, 20f, 20f), Arrays.asList(1, 1, 1), Arrays.asList(4, 8, 13), Arrays.asList(500f, 500f, 500f), Arrays.asList(200f, 220f, 250f), Arrays.asList(true, true, true), "reimu.png", "red_spell_card.png", "reimu_box.png", 325, Arrays.asList(200, 280, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Yukari", Arrays.asList(4, 4, 4), Arrays.asList(100f, 100f, 100f), Arrays.asList(1, 1, 2), Arrays.asList(1, 2, 4), Arrays.asList(600f, 700f, 1000f), Arrays.asList(300f, 400f, 500f), Arrays.asList(false, false, false), "yukari.png", "purple_energy.png", "yukari_box.png", 2500, Arrays.asList(2500, 4500, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Marisa", Arrays.asList(56, 56, 56), Arrays.asList(35f, 50f, 75f), Arrays.asList(1, 1, 1), Arrays.asList(1, 2, 4), Arrays.asList(500f, 500f, 500f), Arrays.asList(100f, 125f, 150f), Arrays.asList(false, false, false), "marisa.png", "blue_magic_missile.png", "marisa_box.png", 200, Arrays.asList(140, 220, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Alice", Arrays.asList(53, 53, 53), Arrays.asList(50f, 50f, 50f), Arrays.asList(1, 1, 2), Arrays.asList(2, 2, 2), Arrays.asList(600f, 600f, 600f), Arrays.asList(200f, 250f, 300f), Arrays.asList(false, true, true), "alice.png", "magic_spike.png", "alice_box.png", 450, Arrays.asList(150, 600, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Sakuya", Arrays.asList(30, 20, 20), Arrays.asList(20f, 20f, 20f), Arrays.asList(1, 1, 1), Arrays.asList(2, 2, 4), Arrays.asList(500f, 500f, 500f), Arrays.asList(150f, 200f, 250f), Arrays.asList(false, false, false), "sakuya.png", "blue_knives.png", "sakuya_box.png", 500, Arrays.asList(300, 350, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Remilia", Arrays.asList(4, 2, 2), Arrays.asList(100f, 100f, 100f), Arrays.asList(1, 1, 1), Arrays.asList(1, 2, 4), Arrays.asList(600f, 700f, 1000f), Arrays.asList(300f, 400f, 500f), Arrays.asList(false, false, false), "remilia.png", "bat.png", "remilia_box.png", 2500, Arrays.asList(4000, 5000, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Youmu", Arrays.asList(90, 90, 90), Arrays.asList(20f, 60f, 60f), Arrays.asList(1, 1, 2), Arrays.asList(18, 28, 28), Arrays.asList(200f, 300f, 400f), Arrays.asList(200f, 300f, 400f), Arrays.asList(false, false, false), "youmu.png", "sword_slash.png", "youmu_box.png", 600, Arrays.asList(400, 800, NO_UPGRADES_AVAILABLE)));
		defaults.add(new GirlConfig("Yuyuko", Arrays.asList(40, 30, 20), Arrays.asList(10f, 11f, 12f), Arrays.asList(20, 20, 20), Arrays.asList(1, 1, 1), Arrays.asList(500f, 600f, 700f), Arrays.asList(200f, 250f, 300f), Arrays.asList(true, true, true), "yuyuko.png", "pink_butterfly.png", "yuyuko_box.png", 2000, Arrays.asList(500, 1500, NO_UPGRADES_AVAILABLE)));
		return defaults;
	}

	private static Girl createDefaultReimu() {
		return new Girl("Reimu", Arrays.asList(86, 86, 75), Arrays.asList(20f, 20f, 20f), Arrays.asList(1, 1, 1), Arrays.asList(4, 8, 13), Arrays.asList(500f, 500f, 500f), Arrays.asList(200f, 220f, 250f), Arrays.asList(true, true, true), "reimu.png", "red_spell_card.png", 325, Arrays.asList(200, 280, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultYukari() {
		return new Girl("Yukari", Arrays.asList(4, 4, 4), Arrays.asList(100f, 100f, 100f), Arrays.asList(1, 1, 2), Arrays.asList(1, 2, 4), Arrays.asList(600f, 700f, 1000f), Arrays.asList(300f, 400f, 500f), Arrays.asList(false, false, false), "yukari.png", "purple_energy.png", 2500, Arrays.asList(2500, 4500, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultMarisa() {
		return new Girl("Marisa", Arrays.asList(56, 56, 56), Arrays.asList(35f, 50f, 75f), Arrays.asList(1, 1, 1), Arrays.asList(1, 2, 4), Arrays.asList(500f, 500f, 500f), Arrays.asList(100f, 125f, 150f), Arrays.asList(false, false, false), "marisa.png", "blue_magic_missile.png", 200, Arrays.asList(140, 220, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultAlice() {
		return new Girl("Alice", Arrays.asList(53, 53, 53), Arrays.asList(50f, 50f, 50f), Arrays.asList(1, 1, 2), Arrays.asList(2, 2, 2), Arrays.asList(600f, 600f, 600f), Arrays.asList(200f, 250f, 300f), Arrays.asList(false, true, true), "alice.png", "magic_spike.png", 450, Arrays.asList(150, 600, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultSakuya() {
		return new Girl("Sakuya", Arrays.asList(30, 20, 20), Arrays.asList(20f, 20f, 20f), Arrays.asList(1, 1, 1), Arrays.asList(2, 2, 4), Arrays.asList(500f, 500f, 500f), Arrays.asList(150f, 200f, 250f), Arrays.asList(false, false, false), "sakuya.png", "blue_knives.png", 500, Arrays.asList(300, 350, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultRemilia() {
		return new Girl("Remilia", Arrays.asList(4, 2, 2), Arrays.asList(100f, 100f, 100f), Arrays.asList(1, 1, 1), Arrays.asList(1, 2, 4), Arrays.asList(600f, 700f, 1000f), Arrays.asList(300f, 400f, 500f), Arrays.asList(false, false, false), "remilia.png", "bat.png", 2500, Arrays.asList(4000, 5000, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultYoumu() {
		return new Girl("Youmu", Arrays.asList(90, 90, 90), Arrays.asList(20f, 60f, 60f), Arrays.asList(1, 1, 2), Arrays.asList(18, 28, 28), Arrays.asList(200f, 300f, 400f), Arrays.asList(200f, 300f, 400f), Arrays.asList(false, false, false), "youmu.png", "sword_slash.png", 600, Arrays.asList(400, 800, NO_UPGRADES_AVAILABLE));
	}

	private static Girl createDefaultYuyuko() {
		return new Girl("Yuyuko", Arrays.asList(40, 30, 20), Arrays.asList(10f, 11f, 12f), Arrays.asList(20, 20, 20), Arrays.asList(1, 1, 1), Arrays.asList(500f, 600f, 700f), Arrays.asList(200f, 250f, 300f), Arrays.asList(true, true, true), "yuyuko.png", "pink_butterfly.png", 2000, Arrays.asList(500, 1500, NO_UPGRADES_AVAILABLE));
	}

	private static void logError(String message) {
		if (Gdx.app != null) {
			Gdx.app.error("GirlFactory", message);
		} else {
			System.err.println("[GirlFactory ERROR] " + message);
		}
	}

}
