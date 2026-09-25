package com.hongbao.bloons;

import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.entities.Bloon;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class PreferencesManagerTest {

	private TestPreferences testPreferences;
	private PreferencesManager preferencesManager;

	@Before
	public void setUp() {
		testPreferences = new TestPreferences();
		preferencesManager = new PreferencesManager(testPreferences);
	}

	@Test
	public void testDefaultUnlockedLevelWhenMissing() {
		assertEquals(0, preferencesManager.getUnlockedLevel(0));
	}

	@Test
	public void testSaveAndRetrieveUnlockedLevel() {
		preferencesManager.saveUnlockedLevel(5);
		assertEquals(5, preferencesManager.getUnlockedLevel(0));
		assertTrue(testPreferences.getFlushCount() > 0);
	}

	@Test
	public void testCorruptPreferenceHandledGracefully() {
		testPreferences.putString(PreferencesManager.KEY_UNLOCKED_LEVEL, "corrupted_string");
		assertEquals(0, preferencesManager.getUnlockedLevel(0));
	}

	@Test
	public void testNegativeUnlockedLevelHandledGracefully() {
		testPreferences.putInteger(PreferencesManager.KEY_UNLOCKED_LEVEL, -10);
		int loadedLevel = preferencesManager.getUnlockedLevel(0);

		List<List<Bloon>> bloonLevels = createMockLevels(3);
		List<List<Long>> intervalLevels = createMockIntervals(3);
		BloonQueue queue = new BloonQueue(bloonLevels, intervalLevels);

		queue.setLevel(loadedLevel);
		assertEquals(0, queue.getLevel());
	}

	@Test
	public void testOverflowUnlockedLevelClampedToMaxQueueIndex() {
		testPreferences.putInteger(PreferencesManager.KEY_UNLOCKED_LEVEL, 9999);
		int loadedLevel = preferencesManager.getUnlockedLevel(0);

		List<List<Bloon>> bloonLevels = createMockLevels(5); // indices 0 to 4
		List<List<Long>> intervalLevels = createMockIntervals(5);
		BloonQueue queue = new BloonQueue(bloonLevels, intervalLevels);

		queue.setLevel(loadedLevel);
		assertEquals(4, queue.getLevel());
	}

	@Test
	public void testQueueSetLevelValidBounds() {
		List<List<Bloon>> bloonLevels = createMockLevels(10);
		List<List<Long>> intervalLevels = createMockIntervals(10);
		BloonQueue queue = new BloonQueue(bloonLevels, intervalLevels);

		queue.setLevel(3);
		assertEquals(3, queue.getLevel());

		queue.setLevel(-1);
		assertEquals(0, queue.getLevel());

		queue.setLevel(20);
		assertEquals(9, queue.getLevel());
	}

	@Test
	public void testSessionRelaunchStatePersistence() {
		// Session 1: Player clears level 2, saving unlocked level 3
		preferencesManager.saveUnlockedLevel(3);
		preferencesManager.flush();

		// Session 2: Game relaunched using saved preferences
		PreferencesManager reloadedManager = new PreferencesManager(testPreferences);
		assertEquals(3, reloadedManager.getUnlockedLevel(0));
	}

	@Test
	public void testBloonManagerLevelCompletionSavesUnlockedLevel() {
		List<List<Bloon>> bloonLevels = createMockLevels(5);
		List<List<Long>> intervalLevels = createMockIntervals(5);
		BloonQueue queue = new BloonQueue(bloonLevels, intervalLevels);

		queue.setLevel(1);
		assertEquals(1, queue.getLevel());

		Set<Bloon> bloons = queue.getBloons();
		assertFalse(bloons.isEmpty());
		assertTrue(queue.isEmpty());

		preferencesManager.saveUnlockedLevel(2);
		preferencesManager.flush();

		assertEquals(2, preferencesManager.getUnlockedLevel(0));
		assertTrue(testPreferences.getFlushCount() > 0);
	}

	private List<List<Bloon>> createMockLevels(int count) {
		List<List<Bloon>> levels = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			List<Bloon> levelBloons = new ArrayList<>();
			if (i > 0) {
				levelBloons.add(new Bloon(Bloon.Color.RED, 1, false, false));
			}
			levels.add(levelBloons);
		}
		return levels;
	}

	private List<List<Long>> createMockIntervals(int count) {
		List<List<Long>> intervals = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			List<Long> levelIntervals = new ArrayList<>();
			if (i > 0) {
				levelIntervals.add(0L);
			}
			intervals.add(levelIntervals);
		}
		return intervals;
	}

	private static class TestPreferences implements Preferences {
		private final Map<String, Object> map = new HashMap<>();
		private int flushCount = 0;

		public int getFlushCount() {
			return flushCount;
		}

		@Override
		public Preferences putBoolean(String key, boolean val) {
			map.put(key, val);
			return this;
		}

		@Override
		public Preferences putInteger(String key, int val) {
			map.put(key, val);
			return this;
		}

		@Override
		public Preferences putLong(String key, long val) {
			map.put(key, val);
			return this;
		}

		@Override
		public Preferences putFloat(String key, float val) {
			map.put(key, val);
			return this;
		}

		@Override
		public Preferences putString(String key, String val) {
			map.put(key, val);
			return this;
		}

		@Override
		public Preferences put(Map<String, ?> vals) {
			map.putAll(vals);
			return this;
		}

		@Override
		public boolean getBoolean(String key) {
			return getBoolean(key, false);
		}

		@Override
		public int getInteger(String key) {
			return getInteger(key, 0);
		}

		@Override
		public long getLong(String key) {
			return getLong(key, 0L);
		}

		@Override
		public float getFloat(String key) {
			return getFloat(key, 0f);
		}

		@Override
		public String getString(String key) {
			return getString(key, "");
		}

		@Override
		public boolean getBoolean(String key, boolean defValue) {
			Object val = map.get(key);
			return val instanceof Boolean ? (Boolean) val : defValue;
		}

		@Override
		public int getInteger(String key, int defValue) {
			Object val = map.get(key);
			if (val instanceof Integer) {
				return (Integer) val;
			}
			if (val instanceof String) {
				throw new ClassCastException("Corrupted preference value");
			}
			return defValue;
		}

		@Override
		public long getLong(String key, long defValue) {
			Object val = map.get(key);
			return val instanceof Long ? (Long) val : defValue;
		}

		@Override
		public float getFloat(String key, float defValue) {
			Object val = map.get(key);
			return val instanceof Float ? (Float) val : defValue;
		}

		@Override
		public String getString(String key, String defValue) {
			Object val = map.get(key);
			return val instanceof String ? (String) val : defValue;
		}

		@Override
		public Map<String, ?> get() {
			return new HashMap<>(map);
		}

		@Override
		public boolean contains(String key) {
			return map.containsKey(key);
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public void remove(String key) {
			map.remove(key);
		}

		@Override
		public void flush() {
			flushCount++;
		}
	}
}
