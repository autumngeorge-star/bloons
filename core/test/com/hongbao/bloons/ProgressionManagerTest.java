package com.hongbao.bloons;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProgressionManagerTest {

	private ProgressionManager progressionManager;

	@Before
	public void setUp() {
		ProgressionManager.InMemoryPreferences prefs = new ProgressionManager.InMemoryPreferences();
		progressionManager = new ProgressionManager(prefs);
	}

	@Test
	public void testInitialUnlockState() {
		assertTrue("Basic Map should be unlocked by default", progressionManager.isMapUnlocked(MapType.BASIC_MAP));
		assertFalse("Map with Turn should be locked initially", progressionManager.isMapUnlocked(MapType.MAP_WITH_TURN));
		assertFalse("Heater Map should be locked initially", progressionManager.isMapUnlocked(MapType.HEATER_MAP));
	}

	@Test
	public void testInitialLevelProgress() {
		assertEquals(0, progressionManager.getHighestCompletedLevel(MapType.BASIC_MAP));
		assertEquals(0, progressionManager.getHighestCompletedLevel(MapType.MAP_WITH_TURN));
		assertEquals(0, progressionManager.getHighestCompletedLevel(MapType.HEATER_MAP));
	}

	@Test
	public void testRecordLevelProgress() {
		progressionManager.recordLevelCompletion(MapType.BASIC_MAP, 1, false);
		assertEquals(1, progressionManager.getHighestCompletedLevel(MapType.BASIC_MAP));

		progressionManager.recordLevelCompletion(MapType.BASIC_MAP, 5, false);
		assertEquals(5, progressionManager.getHighestCompletedLevel(MapType.BASIC_MAP));

		// Regression test: lower level should not decrease highest level
		progressionManager.recordLevelCompletion(MapType.BASIC_MAP, 3, false);
		assertEquals(5, progressionManager.getHighestCompletedLevel(MapType.BASIC_MAP));
	}

	@Test
	public void testSequentialMapUnlocking() {
		assertFalse(progressionManager.isMapUnlocked(MapType.MAP_WITH_TURN));

		// Clearing Basic Map unlocks Map with Turn
		progressionManager.recordLevelCompletion(MapType.BASIC_MAP, 40, true);
		assertTrue("Map with Turn should be unlocked after clearing Basic Map", progressionManager.isMapUnlocked(MapType.MAP_WITH_TURN));
		assertFalse("Heater Map should still be locked", progressionManager.isMapUnlocked(MapType.HEATER_MAP));

		// Clearing Map with Turn unlocks Heater Map
		progressionManager.recordLevelCompletion(MapType.MAP_WITH_TURN, 40, true);
		assertTrue("Heater Map should be unlocked after clearing Map with Turn", progressionManager.isMapUnlocked(MapType.HEATER_MAP));
	}

	@Test
	public void testPersistenceAcrossInstances() {
		ProgressionManager.InMemoryPreferences sharedPrefs = new ProgressionManager.InMemoryPreferences();
		ProgressionManager manager1 = new ProgressionManager(sharedPrefs);

		manager1.recordLevelCompletion(MapType.BASIC_MAP, 15, false);
		manager1.unlockMap(MapType.MAP_WITH_TURN);

		// Second manager sharing same backend preferences
		ProgressionManager manager2 = new ProgressionManager(sharedPrefs);
		assertEquals(15, manager2.getHighestCompletedLevel(MapType.BASIC_MAP));
		assertTrue(manager2.isMapUnlocked(MapType.MAP_WITH_TURN));
	}

	@Test
	public void testMapTypeNavigation() {
		assertEquals(MapType.MAP_WITH_TURN, MapType.BASIC_MAP.getNextMap());
		assertEquals(MapType.HEATER_MAP, MapType.MAP_WITH_TURN.getNextMap());
		assertNull(MapType.HEATER_MAP.getNextMap());
	}
}
