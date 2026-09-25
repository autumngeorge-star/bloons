package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class BloonQueueTest {

	@Test
	public void testSingleListSpawnEntryEncapsulation() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon red1 = new Bloon(Bloon.Color.RED, 1, false, false);
		Bloon red2 = new Bloon(Bloon.Color.RED, 1, false, false);
		Bloon blue1 = new Bloon(Bloon.Color.BLUE, 2, false, false);

		level1.add(new SpawnEntry(red1, 0));
		level1.add(new SpawnEntry(red2, 10));
		level1.add(new SpawnEntry(blue1, 20));
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);

		assertEquals(1, queue.getSpawnLevels().size());
		assertEquals(3, queue.getSpawnLevels().get(0).size());
		assertEquals(0, queue.getSpawnLevels().get(0).get(0).getTargetTick());
		assertSame(red1, queue.getSpawnLevels().get(0).get(0).getBloon());
		assertEquals(10, queue.getSpawnLevels().get(0).get(1).getTargetTick());
		assertSame(red2, queue.getSpawnLevels().get(0).get(1).getBloon());
	}

	@Test
	public void testFixedRateTickAccumulatorAt60FPS() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();

		Bloon red = new Bloon(Bloon.Color.RED, 1, false, false);
		Bloon blue = new Bloon(Bloon.Color.BLUE, 2, false, false);

		level1.add(new SpawnEntry(red, 0));
		level1.add(new SpawnEntry(blue, 3));
		spawnLevels.add(level1);

		BloonQueue queue = new BloonQueue(spawnLevels);

		float delta = 1.0f / 60.0f; // 1 frame at 60 FPS = 1 tick step

		// Frame 1 (tick 0)
		Set<Bloon> bloonsFrame1 = queue.getBloons(delta);
		assertEquals(1, bloonsFrame1.size());
		assertTrue(bloonsFrame1.contains(red));
		assertEquals(1, queue.getClock());

		// Frame 2 (tick 1)
		Set<Bloon> bloonsFrame2 = queue.getBloons(delta);
		assertTrue(bloonsFrame2.isEmpty());
		assertEquals(2, queue.getClock());

		// Frame 3 (tick 2)
		Set<Bloon> bloonsFrame3 = queue.getBloons(delta);
		assertTrue(bloonsFrame3.isEmpty());
		assertEquals(3, queue.getClock());

		// Frame 4 (tick 3)
		Set<Bloon> bloonsFrame4 = queue.getBloons(delta);
		assertEquals(1, bloonsFrame4.size());
		assertTrue(bloonsFrame4.contains(blue));
		assertEquals(4, queue.getClock());
		assertTrue(queue.isEmpty());
	}

	@Test
	public void testFrameRateIndependence60vs30vs120FPS() {
		// Helper to build a standard queue
		java.util.function.Supplier<BloonQueue> queueSupplier = () -> {
			List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
			List<SpawnEntry> level = new ArrayList<>();
			level.add(new SpawnEntry(new Bloon(Bloon.Color.RED, 1, false, false), 0));
			level.add(new SpawnEntry(new Bloon(Bloon.Color.BLUE, 2, false, false), 20));
			level.add(new SpawnEntry(new Bloon(Bloon.Color.GREEN, 3, false, false), 40));
			spawnLevels.add(level);
			return new BloonQueue(spawnLevels);
		};

		// 60 FPS simulation: 60 updates with delta = 1/60s (1 second real time)
		BloonQueue queue60 = queueSupplier.get();
		int spawned60 = 0;
		for (int i = 0; i < 60; i++) {
			spawned60 += queue60.getBloons(1.0f / 60.0f).size();
		}

		// 30 FPS simulation: 30 updates with delta = 1/30s (1 second real time)
		BloonQueue queue30 = queueSupplier.get();
		int spawned30 = 0;
		for (int i = 0; i < 30; i++) {
			spawned30 += queue30.getBloons(1.0f / 30.0f).size();
		}

		// 120 FPS simulation: 120 updates with delta = 1/120s (1 second real time)
		BloonQueue queue120 = queueSupplier.get();
		int spawned120 = 0;
		for (int i = 0; i < 120; i++) {
			spawned120 += queue120.getBloons(1.0f / 120.0f).size();
		}

		// All simulations ran for 1.0 second real time, so clock should advance by 60 ticks in all
		assertEquals(60, queue60.getClock());
		assertEquals(60, queue30.getClock());
		assertEquals(60, queue120.getClock());

		// All simulations spawned the same bloons (3 bloons total at tick 0, 30, and 60)
		assertEquals(3, spawned60);
		assertEquals(3, spawned30);
		assertEquals(3, spawned120);
	}

	@Test
	public void testSafetyCapUnderExtremeLagSpike() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			level.add(new SpawnEntry(new Bloon(Bloon.Color.RED, 1, false, false), i));
		}
		spawnLevels.add(level);

		BloonQueue queue = new BloonQueue(spawnLevels);

		// Massive lag spike: delta = 5.0 seconds (300 ticks worth)
		Set<Bloon> bloons = queue.getBloons(5.0f);

		// Sub-stepping must cap at MAX_SUB_STEPS (100 ticks)
		assertEquals(BloonQueue.MAX_SUB_STEPS, queue.getClock());
		assertEquals(BloonQueue.MAX_SUB_STEPS, bloons.size());
		assertEquals(0f, queue.getTickAccumulator(), 0.0001f);
	}

	@Test
	public void testLevelTransitionsAndEmptyState() {
		List<List<SpawnEntry>> spawnLevels = new ArrayList<>();
		List<SpawnEntry> level1 = new ArrayList<>();
		level1.add(new SpawnEntry(new Bloon(Bloon.Color.RED, 1, false, false), 0));
		spawnLevels.add(level1);

		List<SpawnEntry> level2 = new ArrayList<>();
		level2.add(new SpawnEntry(new Bloon(Bloon.Color.BLUE, 2, false, false), 0));
		spawnLevels.add(level2);

		BloonQueue queue = new BloonQueue(spawnLevels);

		assertFalse(queue.isEmpty());
		assertTrue(queue.hasNextLevel());
		assertEquals(0, queue.getLevel());

		// Clear level 1
		queue.getBloons(1.0f / 60.0f);
		assertTrue(queue.isEmpty());
		assertTrue(queue.hasNextLevel());

		// Go to level 2
		queue.nextLevel();
		assertEquals(1, queue.getLevel());
		assertFalse(queue.isEmpty());
		assertFalse(queue.hasNextLevel());

		// Clear level 2
		queue.getBloons(1.0f / 60.0f);
		assertTrue(queue.isEmpty());
	}
}
