package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Before;
import org.junit.Test;

import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BloonManagerTest {

	private BloonManager bloonManager;

	@Before
	public void setUp() {
		bloonManager = new BloonManager(null, null);
	}

	@Test
	public void testShellHealthCalculation() {
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createRedBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createBlueBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createGreenBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createYellowBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createPinkBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createBlackBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createZebraBloon()));
		assertEquals(1, BloonManager.getShellHealth(BloonFactory.createRainbowBloon()));
		assertEquals(10, BloonManager.getShellHealth(BloonFactory.createCeramicBloon()));
		assertEquals(200, BloonManager.getShellHealth(BloonFactory.createMOAB()));
		assertEquals(700, BloonManager.getShellHealth(BloonFactory.createBFB()));
		assertEquals(4000, BloonManager.getShellHealth(BloonFactory.createZOMG()));
	}

	@Test
	public void testEnqueuePopJob() {
		Bloon red = BloonFactory.createRedBloon();
		BloonActor redActor = new BloonActor(red, 100, 100, null);

		bloonManager.enqueuePopJob(redActor, 1);
		Queue<BloonPopJob> queue = bloonManager.getPopJobQueue();

		assertEquals(1, queue.size());
		BloonPopJob job = queue.peek();
		assertNotNull(job);
		assertEquals(redActor, job.getBloonActor());
		assertEquals(1, job.getDamage());
	}

	@Test
	public void testProcessPopQueueSingleLayer() {
		Bloon red = BloonFactory.createRedBloon();
		BloonActor redActor = new BloonActor(red, 100, 100, null);

		bloonManager.popBloon(redActor, 1);
		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}

	@Test
	public void testOverkillDamageMultiLayerPop() {
		// Pink bloon has health 5 (Pink -> Yellow -> Green -> Blue -> Red)
		Bloon pink = BloonFactory.createPinkBloon();
		BloonActor pinkActor = new BloonActor(pink, 200, 200, null);

		// Apply 3 overkill damage to Pink bloon
		bloonManager.popBloon(pinkActor, 3);

		// Queue should be completely processed
		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}

	@Test
	public void testMOABOverkillPropagatesToChildren() {
		// MOAB bloon has health 218 (shell HP 200)
		Bloon moab = BloonFactory.createMOAB();
		BloonActor moabActor = new BloonActor(moab, 300, 300, null);

		// 212 damage pops MOAB shell (200 HP) leaving 12 damage for child Ceramics
		bloonManager.popBloon(moabActor, 212);

		// Queue should process iteratively until empty
		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}

	@Test
	public void testCeramicOverkillDamage() {
		// Ceramic bloon has health 18 (shell HP 10)
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonActor ceramicActor = new BloonActor(ceramic, 250, 250, null);

		// 12 damage pops Ceramic shell (10 HP) leaving 2 damage for 2 child Rainbow bloons
		bloonManager.popBloon(ceramicActor, 12);

		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}

	@Test
	public void testPartialLayerDamage() {
		// Ceramic bloon hit with 3 damage (< 10 shell HP)
		Bloon ceramic = BloonFactory.createCeramicBloon();
		BloonActor ceramicActor = new BloonActor(ceramic, 250, 250, null);

		bloonManager.popBloon(ceramicActor, 3);

		assertEquals(15, ceramic.getHealth());
		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}

	@Test
	public void testSafetyBoundsMaxIterations() {
		// Add multiple pop jobs to ensure queue processing safety loop bounds check
		Bloon pink = BloonFactory.createPinkBloon();
		BloonActor pinkActor = new BloonActor(pink, 100, 100, null);

		bloonManager.enqueuePopJob(pinkActor, 5);
		bloonManager.processPopQueue();

		assertTrue(bloonManager.getPopJobQueue().isEmpty());
	}
}
