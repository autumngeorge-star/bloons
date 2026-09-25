package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class BloonQueueTest {

	@Test
	public void testBloonSpawnSpec() {
		BloonSpawnSpec spec1 = new BloonSpawnSpec("red");
		BloonSpawnSpec spec2 = new BloonSpawnSpec("red ");
		BloonSpawnSpec spec3 = new BloonSpawnSpec("blue");

		assertEquals("red", spec1.getType());
		assertEquals("red", spec2.getType());
		assertEquals(spec1, spec2);
		assertNotEquals(spec1, spec3);
		assertEquals(spec1.hashCode(), spec2.hashCode());
		assertTrue(spec1.toString().contains("red"));
	}

	@Test
	public void testLazyBloonInstantiationAndQueueTiming() {
		// Create level 0 specs: 2 red bloons at tick 0 and tick 2
		List<BloonSpawnSpec> level0Specs = Arrays.asList(
				new BloonSpawnSpec("red"),
				new BloonSpawnSpec("blue")
		);
		List<Long> level0Intervals = Arrays.asList(0L, 2L);

		// Create level 1 specs: 1 yellow bloon at tick 1
		List<BloonSpawnSpec> level1Specs = Arrays.asList(
				new BloonSpawnSpec("yellow")
		);
		List<Long> level1Intervals = Arrays.asList(1L);

		List<List<BloonSpawnSpec>> spawnSpecs = Arrays.asList(level0Specs, level1Specs);
		List<List<Long>> intervals = Arrays.asList(level0Intervals, level1Intervals);

		BloonQueue queue = new BloonQueue(spawnSpecs, intervals);

		// Verify initial queue state
		assertEquals(0, queue.getLevel());
		assertTrue(queue.hasNextLevel());
		assertFalse(queue.isEmpty());

		// Tick 0: Should spawn first red bloon
		Set<Bloon> tick0Bloons = queue.getBloons();
		assertEquals(1, tick0Bloons.size());
		Bloon bloon1 = tick0Bloons.iterator().next();
		assertEquals(Bloon.Color.RED, bloon1.getColor());
		assertFalse(queue.isEmpty());

		// Tick 1: Should spawn nothing
		Set<Bloon> tick1Bloons = queue.getBloons();
		assertTrue(tick1Bloons.isEmpty());
		assertFalse(queue.isEmpty());

		// Tick 2: Should spawn blue bloon
		Set<Bloon> tick2Bloons = queue.getBloons();
		assertEquals(1, tick2Bloons.size());
		Bloon bloon2 = tick2Bloons.iterator().next();
		assertEquals(Bloon.Color.BLUE, bloon2.getColor());
		assertTrue(queue.isEmpty());

		// Next level
		queue.nextLevel();
		assertEquals(1, queue.getLevel());
		assertFalse(queue.hasNextLevel());
		assertFalse(queue.isEmpty());

		// Level 1, Tick 0: Should spawn nothing
		Set<Bloon> l1Tick0Bloons = queue.getBloons();
		assertTrue(l1Tick0Bloons.isEmpty());

		// Level 1, Tick 1: Should spawn yellow bloon
		Set<Bloon> l1Tick1Bloons = queue.getBloons();
		assertEquals(1, l1Tick1Bloons.size());
		Bloon bloon3 = l1Tick1Bloons.iterator().next();
		assertEquals(Bloon.Color.YELLOW, bloon3.getColor());
		assertTrue(queue.isEmpty());
	}
}
