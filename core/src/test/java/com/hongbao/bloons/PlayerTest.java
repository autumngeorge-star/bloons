package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    private Application originalApp;

    @Before
    public void setUp() {
        originalApp = Gdx.app;
    }

    @After
    public void tearDown() {
        Gdx.app = originalApp;
    }

    @Test
    public void testInitialScoreAndHighScoreWithoutGdxApp() {
        Gdx.app = null;
        Player player = new Player(1000, 100);
        assertEquals(0, player.getScore());
        assertEquals(0, player.getHighScore());
    }

    @Test
    public void testAddScoreIncrementsScore() {
        Gdx.app = null;
        Player player = new Player(1000, 100);
        player.addScore(50);
        assertEquals(50, player.getScore());
        player.addScore(25);
        assertEquals(75, player.getScore());
    }

    @Test
    public void testHighScorePersistenceAndFlush() {
        final Map<String, Integer> storage = new HashMap<>();
        final int[] flushCount = new int[]{0};

        final Preferences mockPreferences = new Preferences() {
            @Override
            public Preferences putBoolean(String key, boolean val) { return this; }
            @Override
            public Preferences putInteger(String key, int val) {
                storage.put(key, val);
                return this;
            }
            @Override
            public Preferences putLong(String key, long val) { return this; }
            @Override
            public Preferences putFloat(String key, float val) { return this; }
            @Override
            public Preferences putString(String key, String val) { return this; }
            @Override
            public Preferences put(Map<String, ?> vals) { return this; }

            @Override
            public boolean getBoolean(String key) { return false; }
            @Override
            public int getInteger(String key) { return storage.getOrDefault(key, 0); }
            @Override
            public long getLong(String key) { return 0; }
            @Override
            public float getFloat(String key) { return 0; }
            @Override
            public String getString(String key) { return null; }

            @Override
            public boolean getBoolean(String key, boolean defValue) { return defValue; }
            @Override
            public int getInteger(String key, int defValue) { return storage.getOrDefault(key, defValue); }
            @Override
            public long getLong(String key, long defValue) { return defValue; }
            @Override
            public float getFloat(String key, float defValue) { return defValue; }
            @Override
            public String getString(String key, String defValue) { return defValue; }

            @Override
            public Map<String, ?> get() { return storage; }
            @Override
            public boolean contains(String key) { return storage.containsKey(key); }
            @Override
            public void clear() { storage.clear(); }
            @Override
            public void remove(String key) { storage.remove(key); }

            @Override
            public void flush() {
                flushCount[0]++;
            }
        };

        storage.put("highScore", 100);

        Gdx.app = new MockApplication(mockPreferences);

        // Session 1: Player created with initial high score = 100
        Player player1 = new Player();
        assertEquals(0, player1.getScore());
        assertEquals(100, player1.getHighScore());

        // Score increases to 80 (<= 100), flush should not be called
        player1.addScore(80);
        assertEquals(80, player1.getScore());
        assertEquals(100, player1.getHighScore());
        assertEquals(0, flushCount[0]);

        // Score increases to 150 (> 100), high score updates to 150 and flushes
        player1.addScore(70);
        assertEquals(150, player1.getScore());
        assertEquals(150, player1.getHighScore());
        assertEquals(1, flushCount[0]);

        // Session 2: New session loading from stored high score
        Player player2 = new Player();
        assertEquals(0, player2.getScore());
        assertEquals(150, player2.getHighScore());
    }

    private static class MockApplication implements Application {
        private final Preferences preferences;

        public MockApplication(Preferences preferences) {
            this.preferences = preferences;
        }

        @Override
        public Preferences getPreferences(String name) {
            return preferences;
        }

        @Override public com.badlogic.gdx.ApplicationListener getApplicationListener() { return null; }
        @Override public com.badlogic.gdx.Graphics getGraphics() { return null; }
        @Override public com.badlogic.gdx.Audio getAudio() { return null; }
        @Override public com.badlogic.gdx.Input getInput() { return null; }
        @Override public com.badlogic.gdx.Files getFiles() { return null; }
        @Override public com.badlogic.gdx.Net getNet() { return null; }
        @Override public void log(String tag, String message) { }
        @Override public void log(String tag, String message, Throwable exception) { }
        @Override public void error(String tag, String message) { }
        @Override public void error(String tag, String message, Throwable exception) { }
        @Override public void debug(String tag, String message) { }
        @Override public void debug(String tag, String message, Throwable exception) { }
        @Override public void setLogLevel(int logLevel) { }
        @Override public int getLogLevel() { return 0; }
        @Override public void setApplicationLogger(com.badlogic.gdx.ApplicationLogger applicationLogger) { }
        @Override public com.badlogic.gdx.ApplicationLogger getApplicationLogger() { return null; }
        @Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
        @Override public com.badlogic.gdx.utils.Clipboard getClipboard() { return null; }
        @Override public int getVersion() { return 0; }
        @Override public long getJavaHeap() { return 0; }
        @Override public long getNativeHeap() { return 0; }
        @Override public void postRunnable(Runnable runnable) { }
        @Override public void exit() { }
        @Override public void addLifecycleListener(com.badlogic.gdx.LifecycleListener listener) { }
        @Override public void removeLifecycleListener(com.badlogic.gdx.LifecycleListener listener) { }
    }
}
