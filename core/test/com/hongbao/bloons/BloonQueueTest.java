package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.hongbao.bloons.descriptors.BloonSpawnDescriptor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonQueueTest {

	@BeforeClass
	public static void setUp() {
		Gdx.files = new HeadlessFiles();
	}

	@Test
	public void testBloonSpawnDescriptor() {
		BloonSpawnDescriptor descriptor = new BloonSpawnDescriptor("red_camo_regen", 30);
		assertEquals("red_camo_regen", descriptor.getBloonType());
		assertEquals(30, descriptor.getSpawnTick());
		assertTrue(descriptor.isCamo());
		assertTrue(descriptor.isRegen());
		assertTrue(descriptor.isRegrowth());

		BloonSpawnDescriptor plainDescriptor = new BloonSpawnDescriptor("blue", 60, false, false);
		assertEquals("blue", plainDescriptor.getBloonType());
		assertEquals(60, plainDescriptor.getSpawnTick());
		assertFalse(plainDescriptor.isCamo());
		assertFalse(plainDescriptor.isRegen());
	}

	@Test
	public void testLazyBloonInstantiationFromDescriptor() {
		BloonSpawnDescriptor descriptor = new BloonSpawnDescriptor("yellow_camo", 10);
		Bloon bloon = BloonFactory.createBloonFromDescriptor(descriptor);

		assertNotNull(bloon);
		assertEquals(Bloon.Color.YELLOW, bloon.getColor());
		assertEquals(4, bloon.getHealth());
		assertTrue(bloon.isCamo());
		assertFalse(bloon.isRegen());
	}

	@Test
	public void testQueueFileParsingZeroBloonInstances() {
		BloonQueue queue = BloonFactory.createBloonQueueFromFile("default.txt");
		assertNotNull(queue);

		List<List<BloonSpawnDescriptor>> bloonLevels = queue.getBloonLevels();
		assertTrue(bloonLevels.size() >= 40);

		// Level 0 is empty (pre-game start)
		assertTrue(queue.isEmpty());
		assertEquals(0, queue.getLevel());

		// Ensure level 1 has descriptors loaded
		List<BloonSpawnDescriptor> level1Descriptors = queue.getDescriptorsForLevel(1);
		assertNotNull(level1Descriptors);
		assertFalse(level1Descriptors.isEmpty());
		assertEquals(20, level1Descriptors.size());
		assertEquals("red", level1Descriptors.get(0).getBloonType());
		assertEquals(0, level1Descriptors.get(0).getSpawnTick());
		assertEquals(30, level1Descriptors.get(1).getSpawnTick());
	}

	@Test
	public void testLazyInstantiationOnSchedule() {
		List<List<BloonSpawnDescriptor>> bloonLevels = new ArrayList<>();
		List<BloonSpawnDescriptor> level0 = new ArrayList<>();
		level0.add(new BloonSpawnDescriptor("red", 0));
		level0.add(new BloonSpawnDescriptor("blue", 10));
		bloonLevels.add(level0);

		BloonQueue queue = new BloonQueue(bloonLevels);

		// Clock tick 0: Red bloon should spawn
		Set<Bloon> tick0Bloons = queue.getBloons();
		assertEquals(1, tick0Bloons.size());
		Bloon bloon0 = tick0Bloons.iterator().next();
		assertEquals(Bloon.Color.RED, bloon0.getColor());

		// Clock ticks 1-9: No bloons should spawn
		for (int t = 1; t < 10; t++) {
			Set<Bloon> bloons = queue.getBloons();
			assertTrue("No bloons should spawn at tick " + t, bloons.isEmpty());
		}

		// Clock tick 10: Blue bloon should spawn
		Set<Bloon> tick10Bloons = queue.getBloons();
		assertEquals(1, tick10Bloons.size());
		Bloon bloon10 = tick10Bloons.iterator().next();
		assertEquals(Bloon.Color.BLUE, bloon10.getColor());

		assertTrue(queue.isEmpty());
	}

	@Test
	public void testLevelProgression() {
		BloonQueue queue = BloonFactory.createBloonQueueFromFile("default.txt");

		// Level 0 (empty start level)
		assertTrue(queue.isEmpty());
		assertTrue(queue.hasNextLevel());

		// Move to Level 1
		queue.nextLevel();
		assertEquals(1, queue.getLevel());
		assertFalse(queue.isEmpty());

		// Progress through all levels
		for (int level = 1; level < queue.getBloonLevels().size() - 1; level++) {
			assertTrue(queue.hasNextLevel());
			queue.nextLevel();
		}

		assertFalse(queue.hasNextLevel());
	}

}
