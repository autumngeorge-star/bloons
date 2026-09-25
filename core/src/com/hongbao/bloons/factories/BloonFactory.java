package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.Wave;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.exceptions.WaveSchemaValidationException;

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
			return createBloonQueueFromJson("hella_bloons.json");
		} else {
			return createBloonQueueFromJson("default.json");
		}
	}
	
	public static BloonQueue createBloonQueueFromJson(String fileName) {
		FileHandle file = Gdx.files.internal("bloon_queues/" + fileName);
		if (!file.exists()) {
			file = Gdx.files.internal("assets/bloon_queues/" + fileName);
		}
		if (!file.exists()) {
			file = Gdx.files.internal(fileName);
		}
		if (!file.exists()) {
			file = Gdx.files.absolute(fileName);
		}
		if (!file.exists()) {
			throw new WaveSchemaValidationException("Wave definition file not found: bloon_queues/" + fileName);
		}

		JsonReader reader = new JsonReader();
		JsonValue root;
		try {
			root = reader.parse(file);
		} catch (Exception e) {
			throw new WaveSchemaValidationException("Failed to parse wave JSON file '" + fileName + "': " + e.getMessage(), e);
		}

		if (root == null || !root.isObject()) {
			throw new WaveSchemaValidationException("Root element in '" + fileName + "' must be a JSON object");
		}
		if (!root.has("name") || root.get("name").isNull()) {
			throw new WaveSchemaValidationException("Missing required property 'name' in JSON root of file '" + fileName + "'");
		}
		if (!root.has("waves") || root.get("waves").isNull()) {
			throw new WaveSchemaValidationException("Missing required property 'waves' in JSON root of file '" + fileName + "'");
		}
		JsonValue wavesArray = root.get("waves");
		if (!wavesArray.isArray()) {
			throw new WaveSchemaValidationException("Property 'waves' must be an array in file '" + fileName + "'");
		}

		List<Wave> waveList = new ArrayList<>();

		for (int i = 0; i < wavesArray.size; i++) {
			JsonValue waveVal = wavesArray.get(i);
			if (waveVal == null || !waveVal.isObject()) {
				throw new WaveSchemaValidationException("Wave element at index " + i + " must be an object in file '" + fileName + "'");
			}
			if (!waveVal.has("level") || waveVal.get("level").isNull()) {
				throw new WaveSchemaValidationException("Missing required property 'level' in wave at index " + i + " in file '" + fileName + "'");
			}
			int level = waveVal.getInt("level");

			if (!waveVal.has("title") || waveVal.get("title").isNull()) {
				throw new WaveSchemaValidationException("Missing required property 'title' in wave level " + level + " in file '" + fileName + "'");
			}
			String title = waveVal.getString("title");

			if (!waveVal.has("musicTrack") || waveVal.get("musicTrack").isNull()) {
				throw new WaveSchemaValidationException("Missing required property 'musicTrack' in wave level " + level + " in file '" + fileName + "'");
			}
			String musicTrack = waveVal.getString("musicTrack");

			if (!waveVal.has("bloons") || waveVal.get("bloons").isNull()) {
				throw new WaveSchemaValidationException("Missing required property 'bloons' in wave level " + level + " in file '" + fileName + "'");
			}
			JsonValue bloonsArray = waveVal.get("bloons");
			if (!bloonsArray.isArray()) {
				throw new WaveSchemaValidationException("Property 'bloons' in wave level " + level + " must be an array in file '" + fileName + "'");
			}

			List<Bloon> bloons = new ArrayList<>();
			List<Long> intervals = new ArrayList<>();
			long timer = 0;

			for (int j = 0; j < bloonsArray.size; j++) {
				JsonValue groupVal = bloonsArray.get(j);
				if (groupVal == null || !groupVal.isObject()) {
					throw new WaveSchemaValidationException("Bloon group element at index " + j + " in wave level " + level + " must be an object in file '" + fileName + "'");
				}

				if (!groupVal.has("amount") || groupVal.get("amount").isNull()) {
					throw new WaveSchemaValidationException("Missing required property 'amount' in bloon group " + j + " of wave level " + level + " in file '" + fileName + "'");
				}
				int amount = groupVal.getInt("amount");

				if (!groupVal.has("delay") || groupVal.get("delay").isNull()) {
					throw new WaveSchemaValidationException("Missing required property 'delay' in bloon group " + j + " of wave level " + level + " in file '" + fileName + "'");
				}
				long delay = groupVal.getLong("delay");

				if (!groupVal.has("types") && !groupVal.has("type")) {
					throw new WaveSchemaValidationException("Missing required property 'types' in bloon group " + j + " of wave level " + level + " in file '" + fileName + "'");
				}

				List<String> typesList = new ArrayList<>();
				JsonValue typesVal = groupVal.has("types") ? groupVal.get("types") : groupVal.get("type");
				if (typesVal.isArray()) {
					for (int k = 0; k < typesVal.size; k++) {
						typesList.add(typesVal.getString(k));
					}
				} else if (typesVal.isString()) {
					String[] split = typesVal.asString().split(",");
					for (String s : split) {
						typesList.add(s.trim());
					}
				} else {
					throw new WaveSchemaValidationException("Property 'types' in bloon group " + j + " of wave level " + level + " must be a string or string array in file '" + fileName + "'");
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

			waveList.add(new Wave(level, title, musicTrack, bloons, intervals));
		}

		return new BloonQueue(waveList);
	}
	
}
