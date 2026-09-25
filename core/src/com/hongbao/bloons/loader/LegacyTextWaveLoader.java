package com.hongbao.bloons.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.BloonType;

import java.util.ArrayList;
import java.util.List;

public class LegacyTextWaveLoader implements WaveLoader {

	@Override
	public BloonQueue loadWaveQueue(FileHandle file) {
		String warningMsg = "[DEPRECATION WARNING] Loading legacy text wave definition file: "
				+ file.name() + ". Please migrate to JSON format.";
		if (Gdx.app != null) {
			Gdx.app.log("DEPRECATION", warningMsg);
		}
		System.out.println(warningMsg);

		String fileContents = file.readString();
		String[] lines = fileContents.split("\r?\n");
		long timer = 0;

		List<List<Bloon>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();
		List<WaveMetadata> metadataList = new ArrayList<>();

		List<Bloon> bloons = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();

		int lineNumber = 0;
		int waveIndex = 0;

		for (String line : lines) {
			lineNumber++;
			String trimmed = line.trim();

			if (trimmed.startsWith("//") || trimmed.isEmpty()) {
				continue;
			}

			if ("END".equals(trimmed)) {
				bloonLevels.add(bloons);
				intervalLevels.add(intervals);

				WaveMetadata meta = new WaveMetadata();
				meta.setTitle("Level " + waveIndex);
				if (waveIndex == 1) {
					meta.setMusicTrack("stage");
				} else if (waveIndex == 40) {
					meta.setMusicTrack("boss");
				}
				metadataList.add(meta);

				bloons = new ArrayList<>();
				intervals = new ArrayList<>();
				timer = 0;
				waveIndex++;
			} else if (trimmed.contains(" ")) {
				String[] parts = trimmed.split("\\s+");
				if (parts.length == 3) {
					try {
						int amount = Integer.parseInt(parts[0]);
						long delay = Long.parseLong(parts[1]);
						String bloonTypes = parts[2];

						for (int x = 0; x < amount; x++) {
							String[] types = bloonTypes.split(",");
							for (String type : types) {
								BloonType bloonType = BloonType.fromString(type);
								Bloon bloon = bloonType.createBloon();
								bloons.add(bloon);
								intervals.add(timer);
								timer += delay;
							}
						}
					} catch (Exception e) {
						throw new IllegalArgumentException("Error parsing legacy text wave at line " + lineNumber
								+ " in file '" + file.name() + "': " + line + " -> " + e.getMessage(), e);
					}
				} else {
					throw new IllegalArgumentException("Invalid spawn format at line " + lineNumber
							+ " in file '" + file.name() + "': " + line);
				}
			} else {
				throw new IllegalArgumentException("Unrecognized token at line " + lineNumber
						+ " in file '" + file.name() + "': " + line);
			}
		}

		return new BloonQueue(bloonLevels, intervalLevels, metadataList);
	}

}
