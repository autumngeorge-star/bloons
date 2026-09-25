package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.WaveMetadata;
import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.List;
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
	
	public static Bloon createBloonOfType(String type, int health) {
		// In the case of bullets that do more than 1 damage, we could (for example) pop a parent bloon so hard that the resulting bloons end up damaged.
		Bloon createdBloon = createBloonOfType(type);
		createdBloon.setHealth(health);
		return createdBloon;
	}
	
	public static Bloon createBloonOfType(String type) {
		// todo george at some point add all the variations of bloons too :(
		if (type.endsWith("\r")) {
			type = type.substring(0, type.length() - 1);
		}
		if ("red".equals(type)) {
			return createRedBloon();
		}
		if ("red_camo".equals(type)) {
			return createRedCamoBloon();
		}
		if ("red_regen".equals(type)) {
			return createRedRegenBloon();
		}
		if ("red_camo_regen".equals(type)) {
			return createRedCamoRegenBloon();
		}
		if ("blue".equals(type)) {
			return createBlueBloon();
		}
		if ("green".equals(type)) {
			return createGreenBloon();
		}
		if ("yellow".equals(type)) {
			return createYellowBloon();
		}
		if ("pink".equals(type)) {
			return createPinkBloon();
		}
		if ("black".equals(type)) {
			return createBlackBloon();
		}
		if ("lead".equals(type)) {
			return createLeadBloon();
		}
		if ("zebra".equals(type)) {
			return createZebraBloon();
		}
		if ("rainbow".equals(type)) {
			return createRainbowBloon();
		}
		if ("ceramic".equals(type)) {
			return createCeramicBloon();
		}
		if ("moab".equals(type)) {
			return createMOAB();
		}
		if ("bfb".equals(type)) {
			return createBFB();
		}
		if ("zomg".equals(type)) {
			return createZOMG();
		}
		throw new RuntimeException("Unexpected bloon type: " +type);
	}
	
	public static Bloon createRandomBloon() {
		Random r = new Random();
		
		int index = r.nextInt(10);
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
			return createLeadBloon();
		}
		else if (index == 7) {
			return createZebraBloon();
		}
		else if (index == 8) {
			return createRainbowBloon();
		}
		else {
			return createCeramicBloon();
		}
	}
	
	public static BloonQueue createBloonQueue() {
		if (HELLA_BLOONS) {
			return createBloonQueueFromFile("hella_bloons.txt");
		} else {
			return createBloonQueueFromFile("default.json");
		}
	}
	
	private static FileHandle resolveFileHandle(String fileName) {
		FileHandle file = null;
		if (Gdx.files != null) {
			file = Gdx.files.internal("bloon_queues/" + fileName);
			if (file.exists()) return file;
		}
		file = new FileHandle("assets/bloon_queues/" + fileName);
		if (file.exists()) return file;
		file = new FileHandle("core/assets/bloon_queues/" + fileName);
		if (file.exists()) return file;
		return Gdx.files != null ? Gdx.files.internal("bloon_queues/" + fileName) : new FileHandle("assets/bloon_queues/" + fileName);
	}

	public static BloonQueue createBloonQueueFromFile(String fileName) {
		FileHandle file = resolveFileHandle(fileName);

		if (fileName.endsWith(".json")) {
			if (file.exists()) {
				return parseJsonWaveConfig(file.readString(), fileName);
			} else {
				String txtFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".txt";
				FileHandle txtFile = resolveFileHandle(txtFileName);
				if (txtFile.exists()) {
					if (Gdx.app != null) {
						Gdx.app.log("BloonFactory", "JSON wave configuration file '" + fileName + "' not found. Falling back to legacy file '" + txtFileName + "'");
					}
					return parseLegacyTxtWaveConfig(txtFile.readString(), txtFileName);
				} else {
					throw new RuntimeException("Wave configuration file not found: " + fileName);
				}
			}
		} else if (fileName.endsWith(".txt")) {
			if (file.exists()) {
				return parseLegacyTxtWaveConfig(file.readString(), fileName);
			} else {
				throw new RuntimeException("Wave configuration file not found: " + fileName);
			}
		} else {
			if (file.exists()) {
				String contents = file.readString().trim();
				if (contents.startsWith("{") || contents.startsWith("[")) {
					return parseJsonWaveConfig(contents, fileName);
				} else {
					return parseLegacyTxtWaveConfig(contents, fileName);
				}
			} else {
				throw new RuntimeException("Wave configuration file not found: " + fileName);
			}
		}
	}

	public static BloonQueue parseJsonWaveConfig(FileHandle file) {
		return parseJsonWaveConfig(file.readString(), file.name());
	}

	public static BloonQueue parseJsonWaveConfig(String jsonString, String fileName) {
		JsonValue root;
		try {
			JsonReader reader = new JsonReader();
			root = reader.parse(jsonString);
		} catch (Exception e) {
			String msg = "JSON syntax error in wave configuration file '" + fileName + "': " + e.getMessage();
			if (Gdx.app != null) {
				Gdx.app.error("BloonFactory", msg, e);
			} else {
				System.err.println("BloonFactory error: " + msg);
			}
			throw new RuntimeException(msg, e);
		}

		if (root == null) {
			String msg = "JSON wave configuration root is null in '" + fileName + "'";
			if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
			throw new IllegalArgumentException(msg);
		}

		JsonValue wavesArray = null;
		if (root.isArray()) {
			wavesArray = root;
		} else if (root.isObject()) {
			if (root.has("waves")) {
				wavesArray = root.get("waves");
			} else if (root.has("levels")) {
				wavesArray = root.get("levels");
			} else {
				String msg = "JSON Wave parsing error: Missing required property 'waves' in root configuration of '" + fileName + "'";
				if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
				throw new IllegalArgumentException(msg);
			}
		} else {
			String msg = "JSON Wave parsing error: Invalid root JSON structure in '" + fileName + "'. Expected object or array.";
			if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
			throw new IllegalArgumentException(msg);
		}

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<WaveMetadata> metadataLevels = new ArrayList<>();

		int waveIndex = 0;
		for (JsonValue waveObj = wavesArray.child; waveObj != null; waveObj = waveObj.next, waveIndex++) {
			if (!waveObj.isObject()) {
				String msg = "JSON Wave parsing error: Wave element at index " + waveIndex + " in '" + fileName + "' must be an object.";
				if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
				throw new IllegalArgumentException(msg);
			}

			String title = waveObj.getString("title", "Level " + waveIndex);
			String musicTrack = waveObj.getString("musicTrack", waveObj.getString("musicCue", waveObj.getString("music", null)));

			JsonValue spawnsArray = waveObj.get("spawns");
			if (spawnsArray == null) spawnsArray = waveObj.get("spawnGroups");
			if (spawnsArray == null) spawnsArray = waveObj.get("groups");

			if (spawnsArray == null) {
				String msg = "JSON Wave parsing error: Missing required property 'spawns' in wave at index " + waveIndex + " of '" + fileName + "'";
				if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
				throw new IllegalArgumentException(msg);
			}

			if (!spawnsArray.isArray()) {
				String msg = "JSON Wave parsing error: Property 'spawns' in wave at index " + waveIndex + " of '" + fileName + "' must be an array.";
				if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
				throw new IllegalArgumentException(msg);
			}

			List<Bloon> bloons = new ArrayList<>();
			List<Long> intervals = new ArrayList<>();
			long timer = 0;

			int spawnIndex = 0;
			for (JsonValue spawnObj = spawnsArray.child; spawnObj != null; spawnObj = spawnObj.next, spawnIndex++) {
				if (!spawnObj.isObject()) {
					String msg = "JSON Wave parsing error: Spawn group at index " + spawnIndex + " in wave " + waveIndex + " of '" + fileName + "' must be an object.";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				if (!spawnObj.has("count") && !spawnObj.has("amount")) {
					String msg = "JSON Wave parsing error: Missing required property 'count' in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				if (!spawnObj.has("delay")) {
					String msg = "JSON Wave parsing error: Missing required property 'delay' in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				if (!spawnObj.has("types") && !spawnObj.has("type") && !spawnObj.has("bloonTypes") && !spawnObj.has("bloons")) {
					String msg = "JSON Wave parsing error: Missing required property 'types' in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				int amount = spawnObj.has("count") ? spawnObj.getInt("count") : spawnObj.getInt("amount");
				if (amount <= 0) {
					String msg = "JSON Wave parsing error: Property 'count' must be greater than 0 in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				long delay = spawnObj.getLong("delay");
				if (delay < 0) {
					String msg = "JSON Wave parsing error: Property 'delay' cannot be negative in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				JsonValue typesValue = spawnObj.get("types");
				if (typesValue == null) typesValue = spawnObj.get("type");
				if (typesValue == null) typesValue = spawnObj.get("bloonTypes");
				if (typesValue == null) typesValue = spawnObj.get("bloons");

				List<String> typesList = new ArrayList<>();
				if (typesValue.isArray()) {
					for (JsonValue item = typesValue.child; item != null; item = item.next) {
						typesList.add(item.asString());
					}
				} else if (typesValue.isString()) {
					String[] parts = typesValue.asString().split(",");
					for (String part : parts) {
						typesList.add(part.trim());
					}
				}

				if (typesList.isEmpty()) {
					String msg = "JSON Wave parsing error: Property 'types' cannot be empty in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
					if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
					throw new IllegalArgumentException(msg);
				}

				for (String type : typesList) {
					try {
						createBloonOfType(type);
					} catch (Exception e) {
						String msg = "JSON Wave parsing error: Unknown bloon type '" + type + "' in spawn group at index " + spawnIndex + " of wave " + waveIndex + " in '" + fileName + "'";
						if (Gdx.app != null) Gdx.app.error("BloonFactory", msg);
						throw new IllegalArgumentException(msg, e);
					}
				}

				for (int x = 0; x < amount; x++) {
					for (String type : typesList) {
						Bloon bloon = createBloonOfType(type);
						bloons.add(bloon);
						intervals.add(timer);
						timer += delay;
					}
				}
			}

			bloonLevels.add(bloons);
			intervalLevels.add(intervals);
			metadataLevels.add(new WaveMetadata(title, musicTrack));
		}

		return new BloonQueue(bloonLevels, intervalLevels, metadataLevels);
	}

	public static BloonQueue parseLegacyTxtWaveConfig(String fileContents, String fileName) {
		if (Gdx.app != null) {
			Gdx.app.log("BloonFactory", "DEPRECATION WARNING: Loading legacy text wave configuration file '" + fileName + "'. Please upgrade to JSON format.");
		} else {
			System.out.println("BloonFactory warning: DEPRECATION WARNING: Loading legacy text wave configuration file '" + fileName + "'. Please upgrade to JSON format.");
		}

		String[] lines = fileContents.split("\n");
		long timer = 0;

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<WaveMetadata> metadataLevels = new ArrayList<>();

		List<Bloon> bloons = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();

		int waveIndex = 0;

		for (String line : lines) {
			if (line.startsWith("//")) {
				// do nothing
			} else if (line.contains(" ")) {
				String[] parts = line.split(" ");
				if (parts.length == 3) {
					int amount = Integer.parseInt(parts[0]);
					long delay = Long.parseLong(parts[1]);
					String bloonTypes = parts[2];

					for (int x = 0; x < amount; x++) {
						String[] types = bloonTypes.split(",");
						for (String type : types) {
							Bloon bloon = createBloonOfType(type);
							bloons.add(bloon);
							intervals.add(timer);
							timer += delay;
						}
					}
				} else {
					System.out.println("BloonFactory.createBloonQueue(wtf2) { " + line + " }");
				}
			} else if (line.contains("END")) {
				bloonLevels.add(bloons);
				intervalLevels.add(intervals);

				String title = "Level " + waveIndex;
				String music = null;
				if (waveIndex == 1) {
					music = "stage";
				} else if (waveIndex == 40) {
					music = "final_boss";
				}
				metadataLevels.add(new WaveMetadata(title, music));

				bloons = new ArrayList<>();
				intervals = new ArrayList<>();
				timer = 0;
				waveIndex++;
			} else {
				System.out.println("BloonFactory.createBloonQueue(wtf1) { " + line + " }");
			}
		}

		return new BloonQueue(bloonLevels, intervalLevels, metadataLevels);
	}
	
}
