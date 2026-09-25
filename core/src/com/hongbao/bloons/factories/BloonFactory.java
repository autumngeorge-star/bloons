package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.WaveMetadata;
import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.hongbao.bloons.BloonsTouhouDefense.HELLA_BLOONS;


public class BloonFactory {
	
	public static Bloon createRedBloon() {
		return new Bloon(Bloon.Color.RED, 1, false, false);
	}
	
	public static Bloon createRedCamoBloon() {
		return new Bloon(Bloon.Color.RED, 1, true, false);
	}
	
	public static Bloon createRedRegenBloon() {
		return new Bloon(Bloon.Color.RED, 1, false, true);
	}
	
	public static Bloon createRedCamoRegenBloon() {
		return new Bloon(Bloon.Color.RED, 1, true, true);
	}
	
	public static Bloon createBlueBloon() {
		return new Bloon(Bloon.Color.BLUE, 2, false, false);
	}
	
	public static Bloon createBlueCamoBloon() {
		return new Bloon(Bloon.Color.BLUE, 2, true, false);
	}
	
	public static Bloon createBlueRegenBloon() {
		return new Bloon(Bloon.Color.BLUE, 2, false, true);
	}
	
	public static Bloon createBlueCamoRegenBloon() {
		return new Bloon(Bloon.Color.BLUE, 2, true, true);
	}
	
	public static Bloon createGreenBloon() {
		return new Bloon(Bloon.Color.GREEN, 3, false, false);
	}
	
	public static Bloon createGreenCamoBloon() {
		return new Bloon(Bloon.Color.GREEN, 3, true, false);
	}
	
	public static Bloon createGreenRegenBloon() {
		return new Bloon(Bloon.Color.GREEN, 3, false, true);
	}
	
	public static Bloon createGreenCamoRegenBloon() {
		return new Bloon(Bloon.Color.GREEN, 3, true, true);
	}
	
	public static Bloon createYellowBloon() {
		return new Bloon(Bloon.Color.YELLOW, 4, false, false);
	}
	
	public static Bloon createYellowCamoBloon() {
		return new Bloon(Bloon.Color.YELLOW, 4, true, false);
	}
	
	public static Bloon createYellowRegenBloon() {
		return new Bloon(Bloon.Color.YELLOW, 4, false, true);
	}
	
	public static Bloon createYellowCamoRegenBloon() {
		return new Bloon(Bloon.Color.YELLOW, 4, true, true);
	}
	
	public static Bloon createPinkBloon() {
		return new Bloon(Bloon.Color.PINK, 5, false, false);
	}
	
	public static Bloon createPinkCamoBloon() {
		return new Bloon(Bloon.Color.PINK, 5, true, false);
	}
	
	public static Bloon createPinkRegenBloon() {
		return new Bloon(Bloon.Color.PINK, 5, false, true);
	}
	
	public static Bloon createPinkCamoRegenBloon() {
		return new Bloon(Bloon.Color.PINK, 5, true, true);
	}
	
	public static Bloon createBlackBloon() {
		return new Bloon(Bloon.Color.BLACK, 6, false, false);
	}
	
	public static Bloon createBlackCamoBloon() {
		return new Bloon(Bloon.Color.BLACK, 6, true, false);
	}
	
	public static Bloon createBlackRegenBloon() {
		return new Bloon(Bloon.Color.BLACK, 6, false, true);
	}
	
	public static Bloon createBlackCamoRegenBloon() {
		return new Bloon(Bloon.Color.BLACK, 6, true, true);
	}

	public static Bloon createWhiteBloon() {
		return new Bloon(Bloon.Color.WHITE, 6, false, false);
	}

	public static Bloon createWhiteCamoBloon() {
		return new Bloon(Bloon.Color.WHITE, 6, true, false);
	}

	public static Bloon createWhiteRegenBloon() {
		return new Bloon(Bloon.Color.WHITE, 6, false, true);
	}

	public static Bloon createWhiteCamoRegenBloon() {
		return new Bloon(Bloon.Color.WHITE, 6, true, true);
	}
	
	public static Bloon createLeadBloon() {
		return new Bloon(Bloon.Color.LEAD, 7, false, false);
	}
	
	public static Bloon createLeadCamoBloon() {
		return new Bloon(Bloon.Color.LEAD, 7, true, false);
	}
	
	public static Bloon createLeadRegenBloon() {
		return new Bloon(Bloon.Color.LEAD, 7, false, true);
	}
	
	public static Bloon createLeadCamoRegenBloon() {
		return new Bloon(Bloon.Color.LEAD, 7, true, true);
	}
	
	public static Bloon createZebraBloon() {
		return new Bloon(Bloon.Color.ZEBRA, 7, false, false);
	}
	
	public static Bloon createZebraCamoBloon() {
		return new Bloon(Bloon.Color.ZEBRA, 7, true, false);
	}
	
	public static Bloon createZebraRegenBloon() {
		return new Bloon(Bloon.Color.ZEBRA, 7, false, true);
	}
	
	public static Bloon createZebraCamoRegenBloon() {
		return new Bloon(Bloon.Color.ZEBRA, 7, true, true);
	}
	
	public static Bloon createRainbowBloon() {
		return new Bloon(Bloon.Color.RAINBOW, 8, false, false);
	}
	
	public static Bloon createRainbowCamoBloon() {
		return new Bloon(Bloon.Color.RAINBOW, 8, true, false);
	}
	
	public static Bloon createRainbowRegenBloon() {
		return new Bloon(Bloon.Color.RAINBOW, 8, false, true);
	}
	
	public static Bloon createRainbowCamoRegenBloon() {
		return new Bloon(Bloon.Color.RAINBOW, 8, true, true);
	}
	
	public static Bloon createCeramicBloon() {
		return new Bloon(Bloon.Color.CERAMIC, 18, false, false);
	}
	
	public static Bloon createCeramicCamoBloon() {
		return new Bloon(Bloon.Color.CERAMIC, 18, true, false);
	}
	
	public static Bloon createCeramicRegenBloon() {
		return new Bloon(Bloon.Color.CERAMIC, 18, false, true);
	}
	
	public static Bloon createCeramicCamoRegenBloon() {
		return new Bloon(Bloon.Color.CERAMIC, 18, true, true);
	}
	
	public static Bloon createMOAB() {
		return new Bloon(Bloon.Color.MOAB, 218, false, false);
	}
	
	public static Bloon createBFB() {
		return new Bloon(Bloon.Color.BFB, 918, false, false);
	}
	
	public static Bloon createZOMG() {
		return new Bloon(Bloon.Color.ZOMG, 4918, false, false);
	}

	private static final Map<String, Bloon.Color> BLOON_TYPE_LOOKUP = new HashMap<String, Bloon.Color>() {
		{
			for (Bloon.Color color : Bloon.Color.values()) {
				put(color.getValue().toLowerCase(), color);
			}
		}
	};

	public static Bloon createBloonOfType(String type, int health) {
		Bloon createdBloon = createBloonOfType(type);
		createdBloon.setHealth(health);
		return createdBloon;
	}

	public static Bloon createBloonOfType(String type) {
		if (type == null) {
			throw new IllegalArgumentException("Bloon type cannot be null");
		}
		type = type.trim();
		if (type.endsWith("\r")) {
			type = type.substring(0, type.length() - 1);
		}

		boolean camo = type.contains("_camo");
		boolean regen = type.contains("_regen") || type.contains("_regrowth");

		String baseType = type.toLowerCase()
				.replace("_camo", "")
				.replace("_regen", "")
				.replace("_regrowth", "");

		Bloon.Color color = BLOON_TYPE_LOOKUP.get(baseType);
		if (color == null) {
			throw new IllegalArgumentException("Unrecognized bloon type: '" + type + "' (color: '" + baseType + "')");
		}

		int health = getDefaultHealthForColor(color);
		return new Bloon(color, health, camo, regen);
	}

	private static int getDefaultHealthForColor(Bloon.Color color) {
		switch (color) {
			case RED: return 1;
			case BLUE: return 2;
			case GREEN: return 3;
			case YELLOW: return 4;
			case PINK: return 5;
			case BLACK: return 6;
			case WHITE: return 6;
			case LEAD: return 7;
			case ZEBRA: return 7;
			case RAINBOW: return 8;
			case CERAMIC: return 18;
			case MOAB: return 218;
			case BFB: return 918;
			case ZOMG: return 4918;
			default: throw new IllegalArgumentException("Unknown bloon color: " + color);
		}
	}

	public static Bloon createRandomBloon() {
		Random r = new Random();
		
		int index = r.nextInt(11);
		if (index == 0) {
			return createRedBloon();
		}
		else if (index == 1) {
			return createBlueBloon();
		}
		else if (index == 2) {
			return createGreenBloon();
		}
		else if (index == 3) {
			return createYellowBloon();
		}
		else if (index == 4) {
			return createPinkBloon();
		}
		else if (index == 5) {
			return createBlackBloon();
		}
		else if (index == 6) {
			return createWhiteBloon();
		}
		else if (index == 7) {
			return createLeadBloon();
		}
		else if (index == 8) {
			return createZebraBloon();
		}
		else if (index == 9) {
			return createRainbowBloon();
		}
		else {
			return createCeramicBloon();
		}
	}
	
	public static BloonQueue createBloonQueue() {
		if (HELLA_BLOONS) {
			return createBloonQueueFromFile("hella_bloons.json");
		} else {
			return createBloonQueueFromFile("default.json");
		}
	}
	
	public static BloonQueue createBloonQueueFromFile(String fileName) {
		FileHandle file = Gdx.files.internal("bloon_queues/" + fileName);
		if (!file.exists()) {
			Gdx.app.error("BloonFactory", "Bloon queue file not found: bloon_queues/" + fileName);
			return new BloonQueue(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		}

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<WaveMetadata> waveMetadatas = new ArrayList<>();

		try {
			JsonReader reader = new JsonReader();
			JsonValue root = reader.parse(file);

			if (root == null) {
				Gdx.app.error("BloonFactory", "Failed to parse JSON root in file: " + fileName);
				return new BloonQueue(bloonLevels, intervalLevels, waveMetadatas);
			}

			String fileTitle = getOptString(root, "title", "Default Wave Set");
			String fileMusic = getOptString(root, "music", "music/demystify_feast.mp3");
			int fileCashBonus = getOptInt(root, "cashBonus", 100);

			JsonValue wavesArray = root.get("waves");
			if (wavesArray == null || !wavesArray.isArray()) {
				Gdx.app.error("BloonFactory", "Missing or invalid 'waves' array field in file: " + fileName);
				return new BloonQueue(bloonLevels, intervalLevels, waveMetadatas);
			}

			int waveIdx = 0;
			for (JsonValue waveVal = wavesArray.child; waveVal != null; waveVal = waveVal.next, waveIdx++) {
				String title = getOptString(waveVal, "title", waveIdx == 0 ? "Initial Preparation" : "Level " + waveIdx);
				String music = getOptString(waveVal, "music", waveIdx == 0 ? "" : fileMusic);
				int cashBonus = getOptInt(waveVal, "cashBonus", waveIdx == 0 ? 0 : fileCashBonus);

				WaveMetadata waveMeta = new WaveMetadata(title, music, cashBonus);
				waveMetadatas.add(waveMeta);

				List<Bloon> bloons = new ArrayList<>();
				List<Long> intervals = new ArrayList<>();
				long timer = 0;

				JsonValue spawnsArray = waveVal.get("spawns");
				if (spawnsArray != null && spawnsArray.isArray()) {
					int spawnIdx = 0;
					for (JsonValue spawnVal = spawnsArray.child; spawnVal != null; spawnVal = spawnVal.next, spawnIdx++) {
						try {
							if (!spawnVal.has("count") || !spawnVal.get("count").isNumber()) {
								Gdx.app.error("BloonFactory", "Invalid or missing 'count' field at wave " + waveIdx + ", spawn " + spawnIdx + " in file " + fileName);
								continue;
							}
							if (!spawnVal.has("delay") || !spawnVal.get("delay").isNumber()) {
								Gdx.app.error("BloonFactory", "Invalid or missing 'delay' field at wave " + waveIdx + ", spawn " + spawnIdx + " in file " + fileName);
								continue;
							}

							int amount = spawnVal.getInt("count");
							long delay = spawnVal.getLong("delay");

							List<String> types = parseTypesField(spawnVal, fileName, waveIdx, spawnIdx);

							for (int x = 0; x < amount; x++) {
								for (String type : types) {
									Bloon bloon = createBloonOfType(type);
									bloons.add(bloon);
									intervals.add(timer);
									timer += delay;
								}
							}
						} catch (Exception e) {
							Gdx.app.error("BloonFactory", "Error processing spawn entry at wave " + waveIdx + ", spawn " + spawnIdx + " in file " + fileName + ": " + e.getMessage());
						}
					}
				}

				bloonLevels.add(bloons);
				intervalLevels.add(intervals);
			}
		} catch (Exception e) {
			Gdx.app.error("BloonFactory", "Error parsing wave definition file: " + fileName + ", exception: " + e.getMessage());
		}

		return new BloonQueue(bloonLevels, intervalLevels, waveMetadatas);
	}

	private static List<String> parseTypesField(JsonValue spawnVal, String fileName, int waveIdx, int spawnIdx) {
		List<String> types = new ArrayList<>();
		JsonValue typesVal = spawnVal.get("types");
		if (typesVal == null) {
			typesVal = spawnVal.get("type");
		}

		if (typesVal == null) {
			Gdx.app.error("BloonFactory", "Missing 'types' field at wave " + waveIdx + ", spawn " + spawnIdx + " in file " + fileName);
			return types;
		}

		if (typesVal.isArray()) {
			for (JsonValue t = typesVal.child; t != null; t = t.next) {
				types.add(t.asString());
			}
		} else if (typesVal.isString()) {
			String[] split = typesVal.asString().split(",");
			for (String s : split) {
				types.add(s.trim());
			}
		} else {
			Gdx.app.error("BloonFactory", "Invalid type for 'types' field at wave " + waveIdx + ", spawn " + spawnIdx + " in file " + fileName);
		}
		return types;
	}

	private static String getOptString(JsonValue json, String name, String defaultVal) {
		if (json != null && json.has(name) && json.get(name).isString()) {
			return json.getString(name);
		}
		return defaultVal;
	}

	private static int getOptInt(JsonValue json, String name, int defaultVal) {
		if (json != null && json.has(name) && json.get(name).isNumber()) {
			return json.getInt(name);
		}
		return defaultVal;
	}
	
}
