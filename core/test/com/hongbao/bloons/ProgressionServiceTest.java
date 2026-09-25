package com.hongbao.bloons;

import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ProgressionServiceTest {

    private ProgressionService progressionService;
    private MockPreferences mockPreferences;

    @Before
    public void setUp() {
        mockPreferences = new MockPreferences();
        progressionService = new ProgressionService(mockPreferences);
    }

    @Test
    public void testBasicMapUnlockedByDefault() {
        assertTrue(progressionService.isMapUnlocked(MapType.BASIC_MAP));
        assertFalse(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
        assertFalse(progressionService.isMapUnlocked(MapType.HEATER));
    }

    @Test
    public void testUnlockMap() {
        assertFalse(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
        progressionService.unlockMap(MapType.MAP_WITH_TURN);
        assertTrue(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
    }

    @Test
    public void testSaveProgressUnlocksNextMap() {
        assertFalse(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
        progressionService.saveProgress(MapType.BASIC_MAP, 5, true);
        assertTrue(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
        assertEquals(5, progressionService.getHighestCompletedLevel(MapType.BASIC_MAP));
    }

    @Test
    public void testResetProgress() {
        progressionService.unlockMap(MapType.MAP_WITH_TURN);
        progressionService.saveProgress(MapType.BASIC_MAP, 10, true);
        assertTrue(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));

        progressionService.resetProgress();
        assertTrue(progressionService.isMapUnlocked(MapType.BASIC_MAP));
        assertFalse(progressionService.isMapUnlocked(MapType.MAP_WITH_TURN));
        assertEquals(0, progressionService.getHighestCompletedLevel(MapType.BASIC_MAP));
    }

    private static class MockPreferences implements Preferences {
        private final Map<String, Object> map = new HashMap<>();

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
            Object v = map.get(key);
            return v instanceof Boolean ? (Boolean) v : defValue;
        }

        @Override
        public int getInteger(String key, int defValue) {
            Object v = map.get(key);
            return v instanceof Integer ? (Integer) v : defValue;
        }

        @Override
        public long getLong(String key, long defValue) {
            Object v = map.get(key);
            return v instanceof Long ? (Long) v : defValue;
        }

        @Override
        public float getFloat(String key, float defValue) {
            Object v = map.get(key);
            return v instanceof Float ? (Float) v : defValue;
        }

        @Override
        public String getString(String key, String defValue) {
            Object v = map.get(key);
            return v instanceof String ? (String) v : defValue;
        }

        @Override
        public Map<String, ?> get() {
            return map;
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
        }
    }
}
