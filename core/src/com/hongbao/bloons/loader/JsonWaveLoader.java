package com.hongbao.bloons.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.BloonType;

import java.util.ArrayList;
import java.util.List;

public class JsonWaveLoader implements WaveLoader {

	@Override
	public BloonQueue loadWaveQueue(FileHandle file) {
		JsonReader reader = new JsonReader();
		JsonValue root;
		try {
			root = reader.parse(file);
		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to parse JSON wave definition file '" + file.name() + "': " + e.getMessage(), e);
		}

		if (root == null) {
			throw new IllegalArgumentException("JSON wave definition file '" + file.name() + "' is empty or invalid.");
		}

		JsonValue wavesArray = root;
		if (root.isObject()) {
			if (root.has("waves")) {
				wavesArray = root.get("waves");
			} else {
				throw new IllegalArgumentException("JSON wave definition file '" + file.name() + "' missing required 'waves' array.");
			}
		}

		if (!wavesArray.isArray()) {
			throw new IllegalArgumentException("JSON wave definition file '" + file.name() + "' expected 'waves' to be an array.");
		}

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<WaveMetadata> metadataList = new ArrayList<>();

		int waveIndex = 0;
		for (JsonValue waveObj = wavesArray.child(); waveObj != null; waveObj = waveObj.next()) {
			waveIndex++;
			String title = waveObj.getString("title", "Level " + (waveIndex - 1));
			String music = waveObj.getString("music", null);
			if (music == null && waveObj.has("musicTrack")) {
				music = waveObj.getString("musicTrack");
			}
			int reward = waveObj.getInt("reward", 0);
			if (reward == 0 && waveObj.has("rewardMoney")) {
				reward = waveObj.getInt("rewardMoney");
			}

			WaveMetadata metadata = new WaveMetadata(title, music, reward);
			metadataList.add(metadata);

			List<Bloon> bloons = new ArrayList<>();
			List<Long> intervals = new ArrayList<>();
			long timer = 0;

			if (waveObj.has("spawns")) {
				JsonValue spawnsArray = waveObj.get("spawns");
				if (spawnsArray != null && spawnsArray.isArray()) {
					int spawnIndex = 0;
					for (JsonValue spawnObj = spawnsArray.child(); spawnObj != null; spawnObj = spawnObj.next()) {
						spawnIndex++;
						int amount = spawnObj.getInt("amount", spawnObj.getInt("count", 1));
						long delay = spawnObj.getLong("delay", spawnObj.getLong("interval", 0));

						List<String> types = new ArrayList<>();
						if (spawnObj.has("types")) {
							JsonValue typesVal = spawnObj.get("types");
							if (typesVal.isArray()) {
								for (JsonValue typeItem = typesVal.child(); typeItem != null; typeItem = typeItem.next()) {
									types.add(typeItem.asString());
								}
							} else {
								for (String t : typesVal.asString().split(",")) {
									types.add(t);
								}
							}
						} else if (spawnObj.has("type")) {
							for (String t : spawnObj.getString("type").split(",")) {
								types.add(t);
							}
						} else {
							throw new IllegalArgumentException("Spawn entry " + spawnIndex + " in wave " + waveIndex
									+ " missing 'type' or 'types' field in '" + file.name() + "'");
						}

						for (int x = 0; x < amount; x++) {
							for (String typeStr : types) {
								try {
									BloonType bloonType = BloonType.fromString(typeStr);
									Bloon bloon = bloonType.createBloon();
									bloons.add(bloon);
									intervals.add(timer);
									timer += delay;
								} catch (Exception e) {
									throw new IllegalArgumentException("Error in wave " + waveIndex + ", spawn " + spawnIndex
											+ " in '" + file.name() + "': " + e.getMessage(), e);
								}
							}
						}
					}
				}
			}

			bloonLevels.add(bloons);
			intervalLevels.add(intervals);
		}

		return new BloonQueue(bloonLevels, intervalLevels, metadataList);
	}

}
