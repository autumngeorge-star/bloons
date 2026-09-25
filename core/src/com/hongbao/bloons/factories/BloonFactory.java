package com.hongbao.bloons.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonDescriptor;
import com.hongbao.bloons.BloonPool;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.entities.Bloon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hongbao.bloons.BloonsTouhouDefense.HELLA_BLOONS;


public class BloonFactory {

	private static final BloonPool bloonPool = new BloonPool();

	public static BloonPool getPool() {
		return bloonPool;
	}

	public static void freeBloon(Bloon bloon) {
		if (bloon != null) {
			bloonPool.free(bloon);
		}
	}
	
	public static Bloon createRedBloon() {
		return bloonPool.obtain(Bloon.Color.RED, 1, false, false);
	}
	
	public static Bloon createRedCamoBloon() {
		return bloonPool.obtain(Bloon.Color.RED, 1, true, false);
	}
	
	public static Bloon createRedRegenBloon() {
		return bloonPool.obtain(Bloon.Color.RED, 1, false, true);
	}
	
	public static Bloon createRedCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.RED, 1, true, true);
	}
	
	public static Bloon createBlueBloon() {
		return bloonPool.obtain(Bloon.Color.BLUE, 2, false, false);
	}
	
	public static Bloon createBlueCamoBloon() {
		return bloonPool.obtain(Bloon.Color.BLUE, 2, true, false);
	}
	
	public static Bloon createBlueRegenBloon() {
		return bloonPool.obtain(Bloon.Color.BLUE, 2, false, true);
	}
	
	public static Bloon createBlueCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.BLUE, 2, true, true);
	}
	
	public static Bloon createGreenBloon() {
		return bloonPool.obtain(Bloon.Color.GREEN, 3, false, false);
	}
	
	public static Bloon createGreenCamoBloon() {
		return bloonPool.obtain(Bloon.Color.GREEN, 3, true, false);
	}
	
	public static Bloon createGreenRegenBloon() {
		return bloonPool.obtain(Bloon.Color.GREEN, 3, false, true);
	}
	
	public static Bloon createGreenCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.GREEN, 3, true, true);
	}
	
	public static Bloon createYellowBloon() {
		return bloonPool.obtain(Bloon.Color.YELLOW, 4, false, false);
	}
	
	public static Bloon createYellowCamoBloon() {
		return bloonPool.obtain(Bloon.Color.YELLOW, 4, true, false);
	}
	
	public static Bloon createYellowRegenBloon() {
		return bloonPool.obtain(Bloon.Color.YELLOW, 4, false, true);
	}
	
	public static Bloon createYellowCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.YELLOW, 4, true, true);
	}
	
	public static Bloon createPinkBloon() {
		return bloonPool.obtain(Bloon.Color.PINK, 5, false, false);
	}
	
	public static Bloon createPinkCamoBloon() {
		return bloonPool.obtain(Bloon.Color.PINK, 5, true, false);
	}
	
	public static Bloon createPinkRegenBloon() {
		return bloonPool.obtain(Bloon.Color.PINK, 5, false, true);
	}
	
	public static Bloon createPinkCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.PINK, 5, true, true);
	}
	
	public static Bloon createBlackBloon() {
		return bloonPool.obtain(Bloon.Color.BLACK, 6, false, false);
	}
	
	public static Bloon createBlackCamoBloon() {
		return bloonPool.obtain(Bloon.Color.BLACK, 6, true, false);
	}
	
	public static Bloon createBlackRegenBloon() {
		return bloonPool.obtain(Bloon.Color.BLACK, 6, false, true);
	}
	
	public static Bloon createBlackCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.BLACK, 6, true, true);
	}
	
	public static Bloon createLeadBloon() {
		return bloonPool.obtain(Bloon.Color.LEAD, 7, false, false);
	}
	
	public static Bloon createLeadCamoBloon() {
		return bloonPool.obtain(Bloon.Color.LEAD, 7, true, false);
	}
	
	public static Bloon createLeadRegenBloon() {
		return bloonPool.obtain(Bloon.Color.LEAD, 7, false, true);
	}
	
	public static Bloon createLeadCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.LEAD, 7, true, true);
	}
	
	public static Bloon createZebraBloon() {
		return bloonPool.obtain(Bloon.Color.ZEBRA, 7, false, false);
	}
	
	public static Bloon createZebraCamoBloon() {
		return bloonPool.obtain(Bloon.Color.ZEBRA, 7, true, false);
	}
	
	public static Bloon createZebraRegenBloon() {
		return bloonPool.obtain(Bloon.Color.ZEBRA, 7, false, true);
	}
	
	public static Bloon createZebraCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.ZEBRA, 7, true, true);
	}
	
	public static Bloon createRainbowBloon() {
		return bloonPool.obtain(Bloon.Color.RAINBOW, 8, false, false);
	}
	
	public static Bloon createRainbowCamoBloon() {
		return bloonPool.obtain(Bloon.Color.RAINBOW, 8, true, false);
	}
	
	public static Bloon createRainbowRegenBloon() {
		return bloonPool.obtain(Bloon.Color.RAINBOW, 8, false, true);
	}
	
	public static Bloon createRainbowCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.RAINBOW, 8, true, true);
	}
	
	public static Bloon createCeramicBloon() {
		return bloonPool.obtain(Bloon.Color.CERAMIC, 18, false, false);
	}
	
	public static Bloon createCeramicCamoBloon() {
		return bloonPool.obtain(Bloon.Color.CERAMIC, 18, true, false);
	}
	
	public static Bloon createCeramicRegenBloon() {
		return bloonPool.obtain(Bloon.Color.CERAMIC, 18, false, true);
	}
	
	public static Bloon createCeramicCamoRegenBloon() {
		return bloonPool.obtain(Bloon.Color.CERAMIC, 18, true, true);
	}
	
	public static Bloon createMOAB() {
		return bloonPool.obtain(Bloon.Color.MOAB, 218, false, false);
	}
	
	public static Bloon createBFB() {
		return bloonPool.obtain(Bloon.Color.BFB, 918, false, false);
	}
	
	public static Bloon createZOMG() {
		return bloonPool.obtain(Bloon.Color.ZOMG, 4918, false, false);
	}

	public static Bloon obtainBloonOfType(String type, int health) {
		Bloon createdBloon = obtainBloonOfType(type);
		createdBloon.setHealth(health);
		return createdBloon;
	}
	
	public static Bloon createBloonOfType(String type, int health) {
		return obtainBloonOfType(type, health);
	}

	public static Bloon obtainBloonOfType(String type) {
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
		if ("blue_camo".equals(type)) {
			return createBlueCamoBloon();
		}
		if ("blue_regen".equals(type)) {
			return createBlueRegenBloon();
		}
		if ("blue_camo_regen".equals(type)) {
			return createBlueCamoRegenBloon();
		}
		if ("green".equals(type)) {
			return createGreenBloon();
		}
		if ("green_camo".equals(type)) {
			return createGreenCamoBloon();
		}
		if ("green_regen".equals(type)) {
			return createGreenRegenBloon();
		}
		if ("green_camo_regen".equals(type)) {
			return createGreenCamoRegenBloon();
		}
		if ("yellow".equals(type)) {
			return createYellowBloon();
		}
		if ("yellow_camo".equals(type)) {
			return createYellowCamoBloon();
		}
		if ("yellow_regen".equals(type)) {
			return createYellowRegenBloon();
		}
		if ("yellow_camo_regen".equals(type)) {
			return createYellowCamoRegenBloon();
		}
		if ("pink".equals(type)) {
			return createPinkBloon();
		}
		if ("pink_camo".equals(type)) {
			return createPinkCamoBloon();
		}
		if ("pink_regen".equals(type)) {
			return createPinkRegenBloon();
		}
		if ("pink_camo_regen".equals(type)) {
			return createPinkCamoRegenBloon();
		}
		if ("black".equals(type)) {
			return createBlackBloon();
		}
		if ("black_camo".equals(type)) {
			return createBlackCamoBloon();
		}
		if ("black_regen".equals(type)) {
			return createBlackRegenBloon();
		}
		if ("black_camo_regen".equals(type)) {
			return createBlackCamoRegenBloon();
		}
		if ("lead".equals(type)) {
			return createLeadBloon();
		}
		if ("lead_camo".equals(type)) {
			return createLeadCamoBloon();
		}
		if ("lead_regen".equals(type)) {
			return createLeadRegenBloon();
		}
		if ("lead_camo_regen".equals(type)) {
			return createLeadCamoRegenBloon();
		}
		if ("zebra".equals(type)) {
			return createZebraBloon();
		}
		if ("zebra_camo".equals(type)) {
			return createZebraCamoBloon();
		}
		if ("zebra_regen".equals(type)) {
			return createZebraRegenBloon();
		}
		if ("zebra_camo_regen".equals(type)) {
			return createZebraCamoRegenBloon();
		}
		if ("rainbow".equals(type)) {
			return createRainbowBloon();
		}
		if ("rainbow_camo".equals(type)) {
			return createRainbowCamoBloon();
		}
		if ("rainbow_regen".equals(type)) {
			return createRainbowRegenBloon();
		}
		if ("rainbow_camo_regen".equals(type)) {
			return createRainbowCamoRegenBloon();
		}
		if ("ceramic".equals(type)) {
			return createCeramicBloon();
		}
		if ("ceramic_camo".equals(type)) {
			return createCeramicCamoBloon();
		}
		if ("ceramic_regen".equals(type)) {
			return createCeramicRegenBloon();
		}
		if ("ceramic_camo_regen".equals(type)) {
			return createCeramicCamoRegenBloon();
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
		throw new RuntimeException("Unexpected bloon type: " + type);
	}
	
	public static Bloon createBloonOfType(String type) {
		return obtainBloonOfType(type);
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

		List<List<BloonDescriptor>> bloonLevels = new ArrayList<>();
		List<List<Long>> intervalLevels = new ArrayList<>();

		List<BloonDescriptor> bloons = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();
		
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
							bloons.add(new BloonDescriptor(type));
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
				bloons = new ArrayList<>();
				intervals = new ArrayList<>();
				timer = 0;
			} else {
				System.out.println("BloonFactory.createBloonQueue(wtf1) { " + line + " }");
			}
		}
			
		return new BloonQueue(bloonLevels, intervalLevels);
	}
	
}
