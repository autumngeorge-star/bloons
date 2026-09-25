package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;

public class ProgressionManager {

	public static final String PREFERENCE_NAME = "bloons_progression";
	public static final String UNLOCKED_PREFIX = "map_unlocked_";
	public static final String HIGHEST_LEVEL_PREFIX = "map_highest_level_";
	public static final String CLEARED_PREFIX = "map_cleared_";

	private final Preferences preferences;

	public ProgressionManager() {
		if (Gdx.app != null) {
			this.preferences = Gdx.app.getPreferences(PREFERENCE_NAME);
		} else {
			this.preferences = new InMemoryPreferences();
		}
	}

	public ProgressionManager(Preferences preferences) {
		this.preferences = preferences != null ? preferences : new InMemoryPreferences();
	}

	public boolean isMapUnlocked(MapType mapType) {
		if (mapType == null) {
			return false;
		}
		if (mapType == MapType.BASIC_MAP) {
			return true;
		}

		if (preferences.getBoolean(UNLOCKED_PREFIX + mapType.name(), false)) {
			return true;
		}

		MapType prerequisite = mapType.getPrerequisiteMap();
		if (prerequisite != null) {
			if (preferences.getBoolean(CLEARED_PREFIX + prerequisite.name(), false)) {
				return true;
			}
		}

		return false;
	}

	public void unlockMap(MapType mapType) {
		if (mapType == null) {
			return;
		}
		preferences.putBoolean(UNLOCKED_PREFIX + mapType.name(), true);
		preferences.flush();
	}

	public int getHighestCompletedLevel(MapType mapType) {
		if (mapType == null) {
			return 0;
		}
		return preferences.getInteger(HIGHEST_LEVEL_PREFIX + mapType.name(), 0);
	}

	public void recordLevelCompletion(MapType mapType, int levelCompleted, boolean isMapCleared) {
		if (mapType == null) {
			return;
		}

		int highest = getHighestCompletedLevel(mapType);
		if (levelCompleted > highest) {
			preferences.putInteger(HIGHEST_LEVEL_PREFIX + mapType.name(), levelCompleted);
		}

		if (isMapCleared) {
			preferences.putBoolean(CLEARED_PREFIX + mapType.name(), true);
			MapType nextMap = mapType.getNextMap();
			if (nextMap != null) {
				unlockMap(nextMap);
			}
		}

		preferences.flush();
	}

	public boolean isMapCleared(MapType mapType) {
		if (mapType == null) {
			return false;
		}
		return preferences.getBoolean(CLEARED_PREFIX + mapType.name(), false);
	}

	public void clearProgression() {
		preferences.clear();
		preferences.flush();
	}

	/**
	 * In-memory fallback Preferences for headless or testing environments.
	 */
	public static class InMemoryPreferences implements Preferences {
		private final Map<String, Object> values = new HashMap<>();

		@Override
		public Preferences putBoolean(String key, boolean val) {
			values.put(key, val);
			return this;
		}

		@Override
		public Preferences putInteger(String key, int val) {
			values.put(key, val);
			return this;
		}

		@Override
		public Preferences putLong(String key, long val) {
			values.put(key, val);
			return this;
		}

		@Override
		public Preferences putFloat(String key, float val) {
			values.put(key, val);
			return this;
		}

		@Override
		public Preferences putString(String key, String val) {
			values.put(key, val);
			return this;
		}

		@Override
		public Preferences put(Map<String, ?> vals) {
			values.putAll(vals);
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
			Object val = values.get(key);
			return val instanceof Boolean ? (Boolean) val : defValue;
		}

		@Override
		public int getInteger(String key, int defValue) {
			Object val = values.get(key);
			return val instanceof Integer ? (Integer) val : defValue;
		}

		@Override
		public long getLong(String key, long defValue) {
			Object val = values.get(key);
			return val instanceof Long ? (Long) val : defValue;
		}

		@Override
		public float getFloat(String key, float defValue) {
			Object val = values.get(key);
			return val instanceof Float ? (Float) val : defValue;
		}

		@Override
		public String getString(String key, String defValue) {
			Object val = values.get(key);
			return val instanceof String ? (String) val : defValue;
		}

		@Override
		public Map<String, ?> get() {
			return new HashMap<>(values);
		}

		@Override
		public boolean contains(String key) {
			return values.containsKey(key);
		}

		@Override
		public void clear() {
			values.clear();
		}

		@Override
		public void remove(String key) {
			values.remove(key);
		}

		@Override
		public void flush() {
			// No-op for in-memory
		}
	}
}
