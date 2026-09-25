package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.hongbao.bloons.descriptors.BloonSpawnDescriptor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonFactoryTest {

	@BeforeClass
	public static void setUp() {
		Gdx.files = new HeadlessFiles();
	}

	@Test
	public void testCreateBloonOfTypeAllTypes() {
		String[] types = {
				"red", "blue", "green", "yellow", "pink",
				"black", "lead", "zebra", "rainbow", "ceramic",
				"moab", "bfb", "zomg"
		};

		for (String type : types) {
			Bloon bloon = BloonFactory.createBloonOfType(type);
			assertNotNull(bloon);
			assertFalse(bloon.isCamo());
			assertFalse(bloon.isRegen());

			Bloon camoBloon = BloonFactory.createBloonOfType(type + "_camo");
			assertNotNull(camoBloon);
			assertTrue(camoBloon.isCamo());

			Bloon regenBloon = BloonFactory.createBloonOfType(type + "_regen");
			assertNotNull(regenBloon);
			assertTrue(regenBloon.isRegen());

			Bloon camoRegenBloon = BloonFactory.createBloonOfType(type + "_camo_regen");
			assertNotNull(camoRegenBloon);
			assertTrue(camoRegenBloon.isCamo());
			assertTrue(camoRegenBloon.isRegen());
		}
	}

	@Test
	public void testHellaBloonsQueueParsing() {
		BloonQueue queue = BloonFactory.createBloonQueueFromFile("hella_bloons.txt");
		assertNotNull(queue);

		List<List<BloonSpawnDescriptor>> levels = queue.getBloonLevels();
		assertFalse(levels.isEmpty());

		// Ensure all descriptors in all levels have valid properties
		int totalDescriptors = 0;
		for (List<BloonSpawnDescriptor> level : levels) {
			totalDescriptors += level.size();
			for (BloonSpawnDescriptor desc : level) {
				assertNotNull(desc.getBloonType());
				assertTrue(desc.getSpawnTick() >= 0);
			}
		}

		assertTrue(totalDescriptors > 0);
	}

}
