package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.Wave;
import com.hongbao.bloons.exceptions.WaveParseException;
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
	
	public static Bloon createBloonOfType(String type, int health) {
		// In the case of bullets that do more than 1 damage, we could (for example) pop a parent bloon so hard that the resulting bloons end up damaged.
		Bloon createdBloon = createBloonOfType(type);
		createdBloon.setHealth(health);
		return createdBloon;
	}
	
	public static Bloon createBloonOfType(String type) {
		BloonType bloonType = BloonType.fromKey(type);
		if (bloonType != null) {
			return bloonType.createBloon();
		}
		throw new WaveParseException("Invalid bloon type '" + type + "'");
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
			return createBloonQueueFromFile("default.txt");
		}
	}
	
	public static BloonQueue createBloonQueueFromFile(String fileName) {
		FileHandle file = Gdx.files.internal("bloon_queues/" + fileName);
		String fileContents = file.readString();
		return createBloonQueueFromText(fileContents, fileName);
	}

	public static BloonQueue createBloonQueueFromText(String fileContents, String fileName) {
		String[] lines = fileContents != null ? fileContents.split("\\r?\\n") : new String[0];

		List<Wave> waves = new ArrayList<>();

		List<Bloon> bloons = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();
		long timer = 0;

		String currentTitle = null;
		String currentMusic = null;
		int currentBonus = 0;

		for (int i = 0; i < lines.length; i++) {
			int lineNumber = i + 1;
			String rawLine = lines[i];
			if (rawLine == null) {
				continue;
			}
			String line = rawLine.trim();

			if (line.isEmpty() || line.startsWith("//")) {
				continue;
			}

			if (line.startsWith("#")) {
				String content = line.substring(1).trim();
				String key;
				String value;
				if (content.contains(":")) {
					String[] parts = content.split(":", 2);
					key = parts[0].trim().toUpperCase();
					value = parts[1].trim();
				} else {
					String[] parts = content.split("\\s+", 2);
					key = parts[0].trim().toUpperCase();
					value = parts.length > 1 ? parts[1].trim() : "";
				}

				if ("TITLE".equals(key)) {
					currentTitle = value;
				} else if ("MUSIC".equals(key)) {
					currentMusic = value;
				} else if ("BONUS".equals(key)) {
					try {
						currentBonus = Integer.parseInt(value);
					} catch (NumberFormatException e) {
						throw new WaveParseException("Invalid bonus amount '" + value + "'", fileName, lineNumber, value);
					}
				} else {
					throw new WaveParseException("Unknown directive '#" + key + "'", fileName, lineNumber, key);
				}
			} else if (line.equals("END")) {
				waves.add(new Wave(bloons, intervals, currentTitle, currentMusic, currentBonus));
				bloons = new ArrayList<>();
				intervals = new ArrayList<>();
				timer = 0;
				currentTitle = null;
				currentMusic = null;
				currentBonus = 0;
			} else {
				String[] parts = line.split("\\s+");
				if (parts.length != 3) {
					throw new WaveParseException("Invalid spawn entry line token count (" + parts.length + ")", fileName, lineNumber, line);
				}

				int amount;
				try {
					amount = Integer.parseInt(parts[0]);
				} catch (NumberFormatException e) {
					throw new WaveParseException("Invalid amount '" + parts[0] + "'", fileName, lineNumber, parts[0]);
				}

				long delay;
				try {
					delay = Long.parseLong(parts[1]);
				} catch (NumberFormatException e) {
					throw new WaveParseException("Invalid delay '" + parts[1] + "'", fileName, lineNumber, parts[1]);
				}

				String bloonTypes = parts[2];
				String[] types = bloonTypes.split(",");
				for (int x = 0; x < amount; x++) {
					for (String type : types) {
						String trimmedType = type.trim();
						if (trimmedType.endsWith("\r")) {
							trimmedType = trimmedType.substring(0, trimmedType.length() - 1).trim();
						}
						try {
							Bloon bloon = createBloonOfType(trimmedType);
							bloons.add(bloon);
							intervals.add(timer);
							timer += delay;
						} catch (WaveParseException e) {
							throw new WaveParseException("Invalid bloon type '" + trimmedType + "'", fileName, lineNumber, trimmedType);
						} catch (Exception e) {
							throw new WaveParseException("Error creating bloon of type '" + trimmedType + "'", fileName, lineNumber, trimmedType);
						}
					}
				}
			}
		}

		if (!bloons.isEmpty() || currentTitle != null || currentMusic != null || currentBonus > 0) {
			waves.add(new Wave(bloons, intervals, currentTitle, currentMusic, currentBonus));
		}

		return new BloonQueue(waves);
	}
	
}
