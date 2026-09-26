package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.entities.Bloon;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BloonPersistenceTest {

	private static HeadlessApplication application;

	@BeforeClass
	public static void setUpClass() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		application = new HeadlessApplication(new ApplicationAdapter() {}, config);
	}

	@AfterClass
	public static void tearDownClass() {
		if (application != null) {
			application.exit();
		}
	}

	@Before
	public void setUp() {
		Preferences prefs = Gdx.app.getPreferences("bloons_preferences");
		prefs.clear();
		prefs.flush();
	}

	@Test
	public void testBloonQueueSetLevel() {
		List<List<Bloon>> bloons = new ArrayList<>();
		List<List<Long>> intervals = new ArrayList<>();

		// Level 0 (empty)
		bloons.add(Collections.emptyList());
		intervals.add(Collections.emptyList());

		// Level 1
		bloons.add(Arrays.asList(new Bloon(Bloon.Color.RED, 1, false, false)));
		intervals.add(Arrays.asList(0L));

		// Level 2
		bloons.add(Arrays.asList(new Bloon(Bloon.Color.BLUE, 2, false, false)));
		intervals.add(Arrays.asList(0L));

		BloonQueue queue = new BloonQueue(bloons, intervals);
		assertEquals(0, queue.getLevel());

		queue.setLevel(2);
		assertEquals(2, queue.getLevel());
		assertEquals(2, queue.getMaxLevel());

		queue.setLevel(1);
		assertEquals(1, queue.getLevel());
	}

	@Test
	public void testPreferencesPersistenceAndSelector() {
		Preferences prefs = Gdx.app.getPreferences("bloons_preferences");
		prefs.putInteger("highestUnlockedLevel", 3);
		prefs.flush();

		assertEquals(3, prefs.getInteger("highestUnlockedLevel", 1));

		// Clear preferences back to 1 for clean start test
		prefs.clear();
		prefs.flush();
		assertEquals(1, prefs.getInteger("highestUnlockedLevel", 1));
	}

	@Test
	public void testBloonManagerLevelNavigation() {
		Preferences prefs = Gdx.app.getPreferences("bloons_preferences");
		prefs.putInteger("highestUnlockedLevel", 5);
		prefs.flush();

		BloonManager bm = new BloonManager(null, null);
		assertEquals(5, bm.getHighestUnlockedLevel());
		assertEquals(5, bm.getSelectedLevel());

		// Test decrement
		bm.decrementSelectedLevel();
		assertEquals(4, bm.getSelectedLevel());

		// Decrement all the way to 1
		bm.setSelectedLevel(1);
		assertEquals(1, bm.getSelectedLevel());

		// Decrement below 1 should be clamped to 1
		bm.decrementSelectedLevel();
		assertEquals(1, bm.getSelectedLevel());

		// Increment beyond highestUnlockedLevel (5) should be clamped
		bm.setSelectedLevel(5);
		bm.incrementSelectedLevel();
		assertEquals(5, bm.getSelectedLevel());

		// Attempt setting above highestUnlockedLevel
		bm.setSelectedLevel(10);
		assertEquals(5, bm.getSelectedLevel());
	}
}
