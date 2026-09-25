package com.hongbao.bloons;

import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LevelProgressionManagerTest {

	private static class InMemoryPreferences implements Preferences {
		private final Map<String, Object> values = new HashMap<>();
		public boolean flushed = false;

		@Override
		public Preferences putBoolean(String key, boolean val) { values.put(key, val); return this; }
		@Override
		public Preferences putInteger(String key, int val) { values.put(key, val); return this; }
		@Override
		public Preferences putLong(String key, long val) { values.put(key, val); return this; }
		@Override
		public Preferences putFloat(String key, float val) { values.put(key, val); return this; }
		@Override
		public Preferences putString(String key, String val) { values.put(key, val); return this; }
		@Override
		public Preferences put(Map<String, ?> vals) { values.putAll(vals); return this; }

		@Override
		public boolean getBoolean(String key) { return getBoolean(key, false); }
		@Override
		public boolean getBoolean(String key, boolean defValue) {
			Object val = values.get(key);
			return val instanceof Boolean ? (Boolean) val : defValue;
		}

		@Override
		public int getInteger(String key) { return getInteger(key, 0); }
		@Override
		public int getInteger(String key, int defValue) {
			Object val = values.get(key);
			return val instanceof Integer ? (Integer) val : defValue;
		}

		@Override
		public long getLong(String key) { return getLong(key, 0L); }
		@Override
		public long getLong(String key, long defValue) {
			Object val = values.get(key);
			return val instanceof Long ? (Long) val : defValue;
		}

		@Override
		public float getFloat(String key) { return getFloat(key, 0f); }
		@Override
		public float getFloat(String key, float defValue) {
			Object val = values.get(key);
			return val instanceof Float ? (Float) val : defValue;
		}

		@Override
		public String getString(String key) { return getString(key, ""); }
		@Override
		public String getString(String key, String defValue) {
			Object val = values.get(key);
			return val instanceof String ? (String) val : defValue;
		}

		@Override
		public Map<String, ?> get() { return values; }
		@Override
		public boolean contains(String key) { return values.containsKey(key); }
		@Override
		public void clear() { values.clear(); }
		@Override
		public void remove(String key) { values.remove(key); }
		@Override
		public void flush() { flushed = true; }
	}

	private InMemoryPreferences prefs;
	private LevelProgressionManager progressionManager;
	private static final int TOTAL_LEVELS = 41;

	@Before
	public void setUp() {
		prefs = new InMemoryPreferences();
		progressionManager = new LevelProgressionManager(TOTAL_LEVELS, prefs);
	}

	@Test
	public void testDefaultMaxUnlockedLevelOnNewGame() {
		assertEquals(0, progressionManager.getMaxUnlockedLevel());
		assertEquals(0, progressionManager.getCurrentLevel());
	}

	@Test
	public void testCorruptedOrInvalidDataFallback() {
		prefs.putInteger(LevelProgressionManager.KEY_MAX_UNLOCKED_LEVEL, -5);
		LevelProgressionManager manager1 = new LevelProgressionManager(TOTAL_LEVELS, prefs);
		assertEquals(0, manager1.getMaxUnlockedLevel());

		prefs.putInteger(LevelProgressionManager.KEY_MAX_UNLOCKED_LEVEL, 100);
		LevelProgressionManager manager2 = new LevelProgressionManager(TOTAL_LEVELS, prefs);
		assertEquals(0, manager2.getMaxUnlockedLevel());
	}

	@Test
	public void testSelectLevelWithinBounds() {
		prefs.putInteger(LevelProgressionManager.KEY_MAX_UNLOCKED_LEVEL, 3);
		LevelProgressionManager manager = new LevelProgressionManager(TOTAL_LEVELS, prefs);

		assertTrue(manager.selectLevel(0));
		assertEquals(0, manager.getCurrentLevel());

		assertTrue(manager.selectLevel(2));
		assertEquals(2, manager.getCurrentLevel());

		assertTrue(manager.selectLevel(3));
		assertEquals(3, manager.getCurrentLevel());

		assertFalse(manager.selectLevel(4));
		assertEquals(3, manager.getCurrentLevel());

		assertFalse(manager.selectLevel(-1));
		assertEquals(3, manager.getCurrentLevel());
	}

	@Test
	public void testIncrementAndDecrementLevel() {
		prefs.putInteger(LevelProgressionManager.KEY_MAX_UNLOCKED_LEVEL, 2);
		LevelProgressionManager manager = new LevelProgressionManager(TOTAL_LEVELS, prefs);

		assertEquals(0, manager.getCurrentLevel());
		assertTrue(manager.incrementLevel());
		assertEquals(1, manager.getCurrentLevel());
		assertTrue(manager.incrementLevel());
		assertEquals(2, manager.getCurrentLevel());

		// Cannot increment past max unlocked level (2)
		assertFalse(manager.incrementLevel());
		assertEquals(2, manager.getCurrentLevel());

		assertTrue(manager.decrementLevel());
		assertEquals(1, manager.getCurrentLevel());
		assertTrue(manager.decrementLevel());
		assertEquals(0, manager.getCurrentLevel());

		// Cannot decrement below 0
		assertFalse(manager.decrementLevel());
		assertEquals(0, manager.getCurrentLevel());
	}

	@Test
	public void testStageCompletionPersistsAndFlushes() {
		assertEquals(0, progressionManager.getMaxUnlockedLevel());
		assertFalse(prefs.flushed);

		progressionManager.markStageCleared(0);

		assertEquals(1, progressionManager.getMaxUnlockedLevel());
		assertTrue(prefs.flushed);
		assertEquals(1, prefs.getInteger(LevelProgressionManager.KEY_MAX_UNLOCKED_LEVEL));
	}

	@Test
	public void testReplayingEarlierStageDoesNotLowerMaxUnlockedLevel() {
		progressionManager.markStageCleared(0); // unlocks 1
		progressionManager.markStageCleared(1); // unlocks 2
		progressionManager.markStageCleared(2); // unlocks 3

		assertEquals(3, progressionManager.getMaxUnlockedLevel());

		prefs.flushed = false;
		progressionManager.markStageCleared(1); // replaying stage 1

		assertEquals(3, progressionManager.getMaxUnlockedLevel());
		assertFalse(prefs.flushed);
	}
}
