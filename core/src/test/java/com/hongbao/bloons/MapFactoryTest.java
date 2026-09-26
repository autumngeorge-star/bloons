package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.factories.MapFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFactoryTest {

    private static class MockPreferences implements Preferences {
        private final Map<String, Object> map = new HashMap<>();

        @Override
        public Preferences putBoolean(String key, boolean val) { map.put(key, val); return this; }
        @Override
        public Preferences putInteger(String key, int val) { map.put(key, val); return this; }
        @Override
        public Preferences putLong(String key, long val) { map.put(key, val); return this; }
        @Override
        public Preferences putFloat(String key, float val) { map.put(key, val); return this; }
        @Override
        public Preferences putString(String key, String val) { map.put(key, val); return this; }
        @Override
        public Preferences put(Map<String, ?> vals) { map.putAll(vals); return this; }
        @Override
        public boolean getBoolean(String key) { return (Boolean) map.getOrDefault(key, false); }
        @Override
        public boolean getBoolean(String key, boolean defValue) { return (Boolean) map.getOrDefault(key, defValue); }
        @Override
        public int getInteger(String key) { return (Integer) map.getOrDefault(key, 0); }
        @Override
        public int getInteger(String key, int defValue) { return (Integer) map.getOrDefault(key, defValue); }
        @Override
        public long getLong(String key) { return (Long) map.getOrDefault(key, 0L); }
        @Override
        public long getLong(String key, long defValue) { return (Long) map.getOrDefault(key, defValue); }
        @Override
        public float getFloat(String key) { return (Float) map.getOrDefault(key, 0f); }
        @Override
        public float getFloat(String key, float defValue) { return (Float) map.getOrDefault(key, defValue); }
        @Override
        public String getString(String key) { return (String) map.getOrDefault(key, ""); }
        @Override
        public String getString(String key, String defValue) { return (String) map.getOrDefault(key, defValue); }
        @Override
        public Map<String, ?> get() { return map; }
        @Override
        public boolean contains(String key) { return map.containsKey(key); }
        @Override
        public void clear() { map.clear(); }
        @Override
        public void remove(String key) { map.remove(key); }
        @Override
        public void flush() {}
    }

    private static class MockApplication implements Application {
        private final Preferences prefs = new MockPreferences();

        @Override
        public Preferences getPreferences(String name) { return prefs; }
        @Override public com.badlogic.gdx.utils.Clipboard getClipboard() { return null; }
        @Override public ApplicationListener getApplicationListener() { return null; }
        @Override public Graphics getGraphics() { return null; }
        @Override public Audio getAudio() { return null; }
        @Override public Input getInput() { return null; }
        @Override public Files getFiles() { return null; }
        @Override public Net getNet() { return null; }
        @Override public void log(String tag, String message) {}
        @Override public void log(String tag, String message, Throwable exception) {}
        @Override public void error(String tag, String message) {}
        @Override public void error(String tag, String message, Throwable exception) {}
        @Override public void debug(String tag, String message) {}
        @Override public void debug(String tag, String message, Throwable exception) {}
        @Override public void setLogLevel(int logLevel) {}
        @Override public int getLogLevel() { return 0; }
        @Override public void setApplicationLogger(com.badlogic.gdx.ApplicationLogger applicationLogger) {}
        @Override public com.badlogic.gdx.ApplicationLogger getApplicationLogger() { return null; }
        @Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
        @Override public int getVersion() { return 0; }
        @Override public long getJavaHeap() { return 0; }
        @Override public long getNativeHeap() { return 0; }
        @Override public void postRunnable(Runnable runnable) {}
        @Override public void exit() {}
        @Override public void addLifecycleListener(com.badlogic.gdx.LifecycleListener listener) {}
        @Override public void removeLifecycleListener(com.badlogic.gdx.LifecycleListener listener) {}
    }

    public static void main(String[] args) {
        System.out.println("Running MapFactoryTest...");

        Gdx.app = new MockApplication();

        testRegisteredMapKeysAndDisplayNames();
        testCustomMapRegistration();
        testMapStoragePreferencesPersistence();

        System.out.println("MapFactoryTest PASSED SUCCESSFULLY!");
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError("Expected: " + expected + ", but got: " + actual);
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true, but got false");
        }
    }

    public static void testRegisteredMapKeysAndDisplayNames() {
        List<String> keys = MapFactory.getRegisteredMapKeys();
        assertTrue(keys.contains("basic"));
        assertTrue(keys.contains("map_with_turn"));
        assertTrue(keys.contains("heater"));

        assertEquals("Basic Map", MapFactory.getMapDisplayName("basic"));
        assertEquals("Map With Turn", MapFactory.getMapDisplayName("map_with_turn"));
        assertEquals("Heater Map", MapFactory.getMapDisplayName("heater"));
    }

    public static void testCustomMapRegistration() {
        MapFactory.registerMap("custom_map", "Custom Map Layout", (stage) -> null);

        assertTrue(MapFactory.getRegisteredMapKeys().contains("custom_map"));
        assertEquals("Custom Map Layout", MapFactory.getMapDisplayName("custom_map"));
    }

    public static void testMapStoragePreferencesPersistence() {
        MapStorage.setSelectedMapKey("map_with_turn");
        assertEquals("map_with_turn", MapStorage.getSelectedMapKey());

        MapStorage.updateHighestLevel("basic", 5);
        assertEquals(5, MapStorage.getHighestLevel("basic"));

        // Only higher levels should update the record
        MapStorage.updateHighestLevel("basic", 3);
        assertEquals(5, MapStorage.getHighestLevel("basic"));

        MapStorage.updateHighestLevel("basic", 12);
        assertEquals(12, MapStorage.getHighestLevel("basic"));
    }
}
