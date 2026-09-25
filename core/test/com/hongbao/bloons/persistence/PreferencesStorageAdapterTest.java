package com.hongbao.bloons.persistence;

import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PreferencesStorageAdapterTest {

    private MockPreferences mockPreferences;
    private PreferencesStorageAdapter adapter;

    @Before
    public void setUp() {
        mockPreferences = new MockPreferences();
        adapter = new PreferencesStorageAdapter(mockPreferences);
    }

    @Test
    public void testLoadDefaultDataWhenEmpty() {
        PlayerData data = adapter.loadData();
        assertNotNull(data);
        assertEquals(1000, data.getMoney());
        assertEquals(100, data.getHealth());
        assertEquals(0, data.getLevel());
    }

    @Test
    public void testSaveAndLoadData() {
        PlayerData dataToSave = new PlayerData(1500, 80, 5);
        adapter.saveData(dataToSave);

        PlayerData loadedData = adapter.loadData();
        assertNotNull(loadedData);
        assertEquals(1500, loadedData.getMoney());
        assertEquals(80, loadedData.getHealth());
        assertEquals(5, loadedData.getLevel());
    }

    @Test
    public void testResetData() {
        PlayerData dataToSave = new PlayerData(2000, 50, 10);
        adapter.saveData(dataToSave);
        adapter.resetData();

        PlayerData loadedData = adapter.loadData();
        assertEquals(1000, loadedData.getMoney());
        assertEquals(100, loadedData.getHealth());
        assertEquals(0, loadedData.getLevel());
    }

    @Test(expected = PersistenceException.class)
    public void testSaveNullDataThrowsException() {
        adapter.saveData(null);
    }

    private static class MockPreferences implements Preferences {
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
            return (boolean) values.getOrDefault(key, false);
        }

        @Override
        public int getInteger(String key) {
            return (int) values.getOrDefault(key, 0);
        }

        @Override
        public long getLong(String key) {
            return (long) values.getOrDefault(key, 0L);
        }

        @Override
        public float getFloat(String key) {
            return (float) values.getOrDefault(key, 0f);
        }

        @Override
        public String getString(String key) {
            return (String) values.get(key);
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            return values.containsKey(key) ? (boolean) values.get(key) : defValue;
        }

        @Override
        public int getInteger(String key, int defValue) {
            return values.containsKey(key) ? (int) values.get(key) : defValue;
        }

        @Override
        public long getLong(String key, long defValue) {
            return values.containsKey(key) ? (long) values.get(key) : defValue;
        }

        @Override
        public float getFloat(String key, float defValue) {
            return values.containsKey(key) ? (float) values.get(key) : defValue;
        }

        @Override
        public String getString(String key, String defValue) {
            return values.containsKey(key) ? (String) values.get(key) : defValue;
        }

        @Override
        public Map<String, ?> get() {
            return values;
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
            // No-op for mock
        }
    }
}
