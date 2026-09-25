package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.descriptors.BloonSpawnDescriptor;
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
	
	public static Bloon createBloonFromDescriptor(BloonSpawnDescriptor descriptor) {
		if (descriptor == null) {
			return null;
		}
		Bloon bloon = createBloonOfType(descriptor.getBloonType());
		if (descriptor.isCamo()) {
			bloon.setCamo(true);
		}
		if (descriptor.isRegen()) {
			bloon.setRegen(true);
		}
		return bloon;
	}

	public static Bloon createBloonOfType(String type, int health) {
		// In the case of bullets that do more than 1 damage, we could (for example) pop a parent bloon so hard that the resulting bloons end up damaged.
		Bloon createdBloon = createBloonOfType(type);
		createdBloon.setHealth(health);
		return createdBloon;
	}
	
	public static Bloon createBloonOfType(String type) {
		if (type == null) {
			throw new RuntimeException("Null bloon type");
		}
		if (type.endsWith("\r")) {
			type = type.substring(0, type.length() - 1);
		}
		boolean camo = type.contains("_camo");
		boolean regen = type.contains("_regen") || type.contains("_regrowth");
		String baseType = type.replace("_camo", "").replace("_regen", "").replace("_regrowth", "");

		if ("red".equals(baseType)) {
			return new Bloon(Bloon.Color.RED, 1, camo, regen);
		}
		if ("blue".equals(baseType)) {
			return new Bloon(Bloon.Color.BLUE, 2, camo, regen);
		}
		if ("green".equals(baseType)) {
			return new Bloon(Bloon.Color.GREEN, 3, camo, regen);
		}
		if ("yellow".equals(baseType)) {
			return new Bloon(Bloon.Color.YELLOW, 4, camo, regen);
		}
		if ("pink".equals(baseType)) {
			return new Bloon(Bloon.Color.PINK, 5, camo, regen);
		}
		if ("black".equals(baseType)) {
			return new Bloon(Bloon.Color.BLACK, 6, camo, regen);
		}
		if ("lead".equals(baseType)) {
			return new Bloon(Bloon.Color.LEAD, 7, camo, regen);
		}
		if ("zebra".equals(baseType)) {
			return new Bloon(Bloon.Color.ZEBRA, 7, camo, regen);
		}
		if ("rainbow".equals(baseType)) {
			return new Bloon(Bloon.Color.RAINBOW, 8, camo, regen);
		}
		if ("ceramic".equals(baseType)) {
			return new Bloon(Bloon.Color.CERAMIC, 18, camo, regen);
		}
		if ("moab".equals(baseType)) {
			return new Bloon(Bloon.Color.MOAB, 218, camo, regen);
		}
		if ("bfb".equals(baseType)) {
			return new Bloon(Bloon.Color.BFB, 918, camo, regen);
		}
		if ("zomg".equals(baseType)) {
			return new Bloon(Bloon.Color.ZOMG, 4918, camo, regen);
		}
		throw new RuntimeException("Unexpected bloon type: " + type);
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
		String[] lines = fileContents.split("\n");
		long timer = 0;

		List<List<BloonSpawnDescriptor>> bloonLevels = new ArrayList<>();
		List<BloonSpawnDescriptor> currentLevelDescriptors = new ArrayList<>();

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
							if (type.endsWith("\r")) {
								type = type.substring(0, type.length() - 1);
							}
							boolean camo = type.contains("_camo");
							boolean regen = type.contains("_regen") || type.contains("_regrowth");
							BloonSpawnDescriptor descriptor = new BloonSpawnDescriptor(type, timer, camo, regen);
							currentLevelDescriptors.add(descriptor);
							timer += delay;
						}
					}
				} else {
					System.out.println("BloonFactory.createBloonQueue(wtf2) { " + line + " }");
				}
			} else if (line.contains("END")) {
				bloonLevels.add(currentLevelDescriptors);
				currentLevelDescriptors = new ArrayList<>();
				timer = 0;
			} else {
				System.out.println("BloonFactory.createBloonQueue(wtf1) { " + line + " }");
			}
		}

		if (!currentLevelDescriptors.isEmpty()) {
			bloonLevels.add(currentLevelDescriptors);
		}
			
		return new BloonQueue(bloonLevels);
	}
	
}
