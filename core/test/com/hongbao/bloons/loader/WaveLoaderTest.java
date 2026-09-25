package com.hongbao.bloons.loader;

import com.badlogic.gdx.files.FileHandle;

import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.BloonType;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;

public class WaveLoaderTest {

	@Test
	public void testBloonTypeEnumVariants() {
		String[] typesToTest = {
			"red", "red_camo", "red_regen", "red_camo_regen",
			"blue", "blue_camo", "blue_regen", "blue_camo_regen",
			"green", "green_camo", "green_regen", "green_camo_regen",
			"yellow", "yellow_camo", "yellow_regen", "yellow_camo_regen",
			"pink", "pink_camo", "pink_regen", "pink_camo_regen",
			"black", "black_camo", "black_regen", "black_camo_regen",
			"lead", "lead_camo", "lead_regen", "lead_camo_regen",
			"zebra", "zebra_camo", "zebra_regen", "zebra_camo_regen",
			"rainbow", "rainbow_camo", "rainbow_regen", "rainbow_camo_regen",
			"ceramic", "ceramic_camo", "ceramic_regen", "ceramic_camo_regen",
			"moab", "bfb", "zomg"
		};

		for (String typeStr : typesToTest) {
			BloonType type = BloonType.fromString(typeStr);
			Assert.assertNotNull("BloonType should not be null for " + typeStr, type);
			Bloon bloon = type.createBloon();
			Assert.assertNotNull("Bloon entity should be created for " + typeStr, bloon);
		}
	}

	@Test
	public void testWaveLoaderFactorySelection() {
		FileHandle jsonFile = new FileHandle("test.json");
		FileHandle txtFile = new FileHandle("test.txt");

		WaveLoader jsonLoader = WaveLoaderFactory.getLoader(jsonFile);
		Assert.assertTrue("Should select JsonWaveLoader", jsonLoader instanceof JsonWaveLoader);

		WaveLoader txtLoader = WaveLoaderFactory.getLoader(txtFile);
		Assert.assertTrue("Should select LegacyTextWaveLoader", txtLoader instanceof LegacyTextWaveLoader);
	}

	@Test
	public void testLegacyTextWaveLoaderParsingAndErrorHandling() throws Exception {
		File tempFile = File.createTempFile("legacy_wave", ".txt");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("// Sample legacy text wave file\n");
			writer.write("END\n");
			writer.write("10 20 red,blue\n");
			writer.write("END\n");
		}

		FileHandle handle = new FileHandle(tempFile);
		LegacyTextWaveLoader loader = new LegacyTextWaveLoader();
		BloonQueue queue = loader.loadWaveQueue(handle);

		Assert.assertNotNull(queue);
		Assert.assertNotNull(queue.getMetadataList());
		Assert.assertEquals(2, queue.getMetadataList().size());
		Assert.assertEquals("Level 1", queue.getMetadataList().get(1).getTitle());
		Assert.assertEquals("stage", queue.getMetadataList().get(1).getMusicTrack());

		// Test error reporting on invalid line
		File errFile = File.createTempFile("legacy_err", ".txt");
		errFile.deleteOnExit();
		try (FileWriter writer = new FileWriter(errFile)) {
			writer.write("// Line 1 comment\n");
			writer.write("10 20 invalid_bloon_type_xyz\n");
		}

		FileHandle errHandle = new FileHandle(errFile);
		try {
			loader.loadWaveQueue(errHandle);
			Assert.fail("Expected IllegalArgumentException on invalid bloon type");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Error message should contain line number 2 context",
					e.getMessage().contains("line 2"));
		}
	}

	@Test
	public void testJsonWaveLoaderParsing() throws Exception {
		File tempFile = File.createTempFile("json_wave", ".json");
		tempFile.deleteOnExit();

		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("{\n" +
					"  \"title\": \"Test Map\",\n" +
					"  \"waves\": [\n" +
					"    {\n" +
					"      \"title\": \"Wave 1\",\n" +
					"      \"music\": \"stage\",\n" +
					"      \"reward\": 250,\n" +
					"      \"spawns\": [\n" +
					"        { \"amount\": 5, \"delay\": 10, \"types\": [\"red\", \"blue\"] }\n" +
					"      ]\n" +
					"    }\n" +
					"  ]\n" +
					"}");
		}

		FileHandle handle = new FileHandle(tempFile);
		JsonWaveLoader loader = new JsonWaveLoader();
		BloonQueue queue = loader.loadWaveQueue(handle);

		Assert.assertNotNull(queue);
		Assert.assertEquals(1, queue.getMetadataList().size());
		WaveMetadata meta = queue.getWaveMetadata(0);
		Assert.assertEquals("Wave 1", meta.getTitle());
		Assert.assertEquals("stage", meta.getMusicTrack());
		Assert.assertEquals(250, meta.getRewardMoney());
	}

	@Test
	public void testCanonicalAssets() {
		FileHandle defaultJson = new FileHandle("assets/bloon_queues/default.json");
		if (defaultJson.exists()) {
			WaveLoader loader = WaveLoaderFactory.getLoader(defaultJson);
			BloonQueue queue = loader.loadWaveQueue(defaultJson);
			Assert.assertNotNull(queue);
			Assert.assertTrue("Should have loaded waves from default.json", queue.getMetadataList().size() > 0);
		}

		FileHandle defaultTxt = new FileHandle("assets/bloon_queues/default.txt");
		if (defaultTxt.exists()) {
			WaveLoader loader = WaveLoaderFactory.getLoader(defaultTxt);
			BloonQueue queue = loader.loadWaveQueue(defaultTxt);
			Assert.assertNotNull(queue);
			Assert.assertTrue("Should have loaded waves from default.txt", queue.getMetadataList().size() > 0);
		}
	}

}
