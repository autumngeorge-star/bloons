package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BloonPoolTest {

	@Test
	public void testBloonResetAndReuse() {
		BloonPool pool = new BloonPool(16, 100);
		Assert.assertEquals(0, pool.getFree());

		// Obtain bloon and initialize
		Bloon b1 = pool.obtain(Bloon.Color.RED, 1, true, false);
		b1.setDistanceTravelled(50);

		Assert.assertEquals(Bloon.Color.RED, b1.getColor());
		Assert.assertEquals(1, b1.getHealth());
		Assert.assertTrue(b1.isCamo());
		Assert.assertFalse(b1.isRegen());
		Assert.assertEquals(50, b1.getDistanceTravelled());

		// Free bloon back to pool
		pool.free(b1);
		Assert.assertEquals(1, pool.getFree());

		// Verify reset cleared state
		Assert.assertNull(b1.getColor());
		Assert.assertEquals(0, b1.getHealth());
		Assert.assertFalse(b1.isCamo());
		Assert.assertFalse(b1.isRegen());
		Assert.assertEquals(0, b1.getDistanceTravelled());
		Assert.assertNull(b1.getImageFileName());

		// Obtain recycled bloon
		Bloon b2 = pool.obtain(Bloon.Color.BLUE, 2, false, true);
		Assert.assertSame("Should reuse same instance", b1, b2);
		Assert.assertEquals(Bloon.Color.BLUE, b2.getColor());
		Assert.assertEquals(2, b2.getHealth());
		Assert.assertFalse(b2.isCamo());
		Assert.assertTrue(b2.isRegen());
		Assert.assertEquals(0, b2.getDistanceTravelled());
	}

	@Test
	public void testBloonPoolBoundedMax() {
		int maxPoolSize = 8;
		BloonPool pool = new BloonPool(2, maxPoolSize);

		List<Bloon> bloons = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			bloons.add(pool.obtain());
		}

		for (Bloon b : bloons) {
			pool.free(b);
		}

		Assert.assertTrue("Free count should be bounded to max pool size", pool.getFree() <= maxPoolSize);
	}

	@Test
	public void testBloonQueueZeroStartupAllocation() {
		List<List<BloonDescriptor>> levelDescriptors = new ArrayList<>();
		List<BloonDescriptor> wave = new ArrayList<>();
		wave.add(new BloonDescriptor("red"));
		wave.add(new BloonDescriptor("blue"));
		wave.add(new BloonDescriptor("green"));
		levelDescriptors.add(wave);

		List<List<Long>> levelIntervals = new ArrayList<>();
		List<Long> intervals = new ArrayList<>();
		intervals.add(0L);
		intervals.add(0L);
		intervals.add(5L);
		levelIntervals.add(intervals);

		// Initialize BloonQueue
		BloonQueue queue = new BloonQueue(levelDescriptors, levelIntervals);

		// Zero Bloon entities are held directly in descriptors
		Assert.assertFalse(queue.isEmpty());
		Assert.assertEquals("red", queue.getBloonDescriptors().get(0).get(0).getType());

		// Fetch at interval 0
		Set<Bloon> spawned = queue.getBloons();
		Assert.assertEquals(2, spawned.size());

		// Return spawned bloons back to pool
		for (Bloon b : spawned) {
			BloonFactory.freeBloon(b);
		}
	}

	@Test
	public void testChildBloonGenerationFromPool() {
		Bloon blueBloon = BloonFactory.obtainBloonOfType("blue");
		blueBloon.setDistanceTravelled(100);

		BloonPoppedResult result = new BloonPoppedResult(blueBloon, 1);
		Set<Bloon> children = result.getBloonsGenerated();

		Assert.assertFalse(children.isEmpty());
		for (Bloon child : children) {
			Assert.assertEquals(Bloon.Color.RED, child.getColor());
			Assert.assertEquals(100, child.getDistanceTravelled());
			BloonFactory.freeBloon(child);
		}

		BloonFactory.freeBloon(blueBloon);
	}

	@Test
	public void testRecyclingAcrossDifferentTypesAndWaves() {
		BloonPool pool = new BloonPool(10, 50);

		// First cycle: MOAB
		Bloon moab = pool.obtain(Bloon.Color.MOAB, 218, false, false, 300);
		Assert.assertEquals(Bloon.Color.MOAB, moab.getColor());
		Assert.assertEquals("img/bloons/moab_bloon.png", moab.getImageFileName());
		Assert.assertEquals(218, moab.getHealth());

		pool.free(moab);

		// Second cycle: Red Camo Regen
		Bloon red = pool.obtain(Bloon.Color.RED, 1, true, true, 0);
		Assert.assertSame("Should reuse same instance", moab, red);
		Assert.assertEquals(Bloon.Color.RED, red.getColor());
		Assert.assertEquals("img/bloons/red_camo_regrowth_bloon.png", red.getImageFileName());
		Assert.assertEquals(1, red.getHealth());
		Assert.assertTrue(red.isCamo());
		Assert.assertTrue(red.isRegen());
		Assert.assertEquals(0, red.getDistanceTravelled());

		pool.free(red);

		// Third cycle: Ceramic
		Bloon ceramic = pool.obtain(Bloon.Color.CERAMIC, 18, false, false, 500);
		Assert.assertSame("Should reuse same instance", moab, ceramic);
		Assert.assertEquals(Bloon.Color.CERAMIC, ceramic.getColor());
		Assert.assertEquals("img/bloons/ceramic_bloon.png", ceramic.getImageFileName());
		Assert.assertEquals(18, ceramic.getHealth());
		Assert.assertFalse(ceramic.isCamo());
		Assert.assertFalse(ceramic.isRegen());
		Assert.assertEquals(500, ceramic.getDistanceTravelled());

		pool.free(ceramic);
	}
}
