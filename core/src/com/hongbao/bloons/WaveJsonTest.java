package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;

import java.io.File;
import java.io.FileWriter;

public class WaveJsonTest {

	public static void main(String[] args) throws Exception {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		new HeadlessApplication(new ApplicationAdapter() {
			@Override
			public void create() {
				try {
					runTests();
					System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
					Gdx.app.exit();
				} catch (Throwable t) {
					t.printStackTrace();
					System.exit(1);
				}
			}
		}, config);
	}

	private static void runTests() throws Exception {
		testBloonTypeResolver();
		testWhiteBloonCreation();
		testDefaultWaveJsonParsing();
		testHellaBloonsJsonParsing();
		testInvalidFieldsHandling();
	}

	private static void testBloonTypeResolver() {
		System.out.println("Testing bloon type resolver...");
		Bloon red = BloonFactory.createBloonOfType("red");
		if (red.getColor() != Bloon.Color.RED || red.isCamo() || red.isRegen()) {
			throw new RuntimeException("Red bloon check failed");
		}

		Bloon redCamoRegen = BloonFactory.createBloonOfType("red_camo_regen");
		if (redCamoRegen.getColor() != Bloon.Color.RED || !redCamoRegen.isCamo() || !redCamoRegen.isRegen()) {
			throw new RuntimeException("Red camo regen bloon check failed");
		}

		Bloon ceramic = BloonFactory.createBloonOfType("ceramic");
		if (ceramic.getColor() != Bloon.Color.CERAMIC) {
			throw new RuntimeException("Ceramic bloon check failed");
		}

		Bloon zomg = BloonFactory.createBloonOfType("zomg");
		if (zomg.getColor() != Bloon.Color.ZOMG) {
			throw new RuntimeException("ZOMG bloon check failed");
		}

		boolean caught = false;
		try {
			BloonFactory.createBloonOfType("unknown_bloon_type_xyz");
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		if (!caught) {
			throw new RuntimeException("Expected IllegalArgumentException for unknown bloon type");
		}
		System.out.println("Bloon type resolver passed.");
	}

	private static void testWhiteBloonCreation() {
		System.out.println("Testing white bloon creation...");
		Bloon white = BloonFactory.createBloonOfType("white");
		if (white.getColor() != Bloon.Color.WHITE || white.getHealth() != 6 || white.getSpeed() != 7 || white.isCamo() || white.isRegen()) {
			throw new RuntimeException("White bloon check failed");
		}

		Bloon whiteCamoRegen = BloonFactory.createBloonOfType("white_camo_regen");
		if (whiteCamoRegen.getColor() != Bloon.Color.WHITE || !whiteCamoRegen.isCamo() || !whiteCamoRegen.isRegen()) {
			throw new RuntimeException("White camo regen bloon check failed");
		}

		Bloon directWhite = BloonFactory.createWhiteBloon();
		if (directWhite.getColor() != Bloon.Color.WHITE) {
			throw new RuntimeException("Direct white bloon check failed");
		}

		System.out.println("White bloon creation passed.");
	}

	private static void testDefaultWaveJsonParsing() {
		System.out.println("Testing default.json wave parsing...");
		BloonQueue queue = BloonFactory.createBloonQueueFromFile("default.json");
		if (queue == null) {
			throw new RuntimeException("Queue was null");
		}
		if (queue.getLevel() != 0) {
			throw new RuntimeException("Initial level should be 0");
		}

		WaveMetadata prepMeta = queue.getCurrentWaveMetadata();
		if (prepMeta == null || !"Preparation".equals(prepMeta.getTitle())) {
			throw new RuntimeException("Preparation metadata mismatch");
		}

		queue.nextLevel(); // level 1
		if (queue.getLevel() != 1) {
			throw new RuntimeException("Level should be 1 after nextLevel()");
		}
		WaveMetadata level1Meta = queue.getCurrentWaveMetadata();
		if (level1Meta == null || !"music/demystify_feast.mp3".equals(level1Meta.getMusic()) || level1Meta.getCashBonus() != 100) {
			throw new RuntimeException("Level 1 metadata mismatch: " + level1Meta);
		}

		System.out.println("default.json wave parsing passed.");
	}

	private static void testHellaBloonsJsonParsing() {
		System.out.println("Testing hella_bloons.json wave parsing...");
		BloonQueue queue = BloonFactory.createBloonQueueFromFile("hella_bloons.json");
		if (queue == null) {
			throw new RuntimeException("Hella queue was null");
		}
		queue.nextLevel();
		if (queue.getLevel() != 1) {
			throw new RuntimeException("Hella queue level should be 1");
		}
		WaveMetadata meta = queue.getCurrentWaveMetadata();
		if (meta == null || meta.getCashBonus() != 100) {
			throw new RuntimeException("Hella metadata mismatch");
		}
		System.out.println("hella_bloons.json wave parsing passed.");
	}

	private static void testInvalidFieldsHandling() throws Exception {
		System.out.println("Testing malformed/invalid wave JSON handling...");
		File invalidFile = new File("bloon_queues/invalid_test.json");
		try (FileWriter writer = new FileWriter(invalidFile)) {
			writer.write("{\n" +
					"  \"title\": \"Bad Queue\",\n" +
					"  \"waves\": [\n" +
					"    {\n" +
					"      \"spawns\": [\n" +
					"        { \"count\": \"not_a_number\", \"delay\": 10, \"types\": [\"red\"] },\n" +
					"        { \"count\": 5, \"delay\": 20, \"types\": [\"invalid_color_type\"] },\n" +
					"        { \"count\": 2, \"delay\": 10, \"types\": [\"blue\"] }\n" +
					"      ]\n" +
					"    }\n" +
					"  ]\n" +
					"}");
		}

		try {
			BloonQueue queue = BloonFactory.createBloonQueueFromFile("invalid_test.json");
			if (queue == null) {
				throw new RuntimeException("Invalid test queue was null");
			}
			if (queue.getBloons() == null) {
				throw new RuntimeException("getBloons returned null");
			}
		} finally {
			if (invalidFile.exists()) {
				invalidFile.delete();
			}
		}
		System.out.println("Invalid fields handling passed.");
	}
}
