package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        player = new Player(1000, 100);
    }

    @Test
    public void testInitialScoreAndHighScore() {
        assertEquals(0, player.getScore());
        assertEquals(0, player.getHighScore());
    }

    @Test
    public void testAddScoreUpdatesScoreAndHighScore() {
        player.addScore(150);
        assertEquals(150, player.getScore());
        assertEquals(150, player.getHighScore());

        player.addScore(50);
        assertEquals(200, player.getScore());
        assertEquals(200, player.getHighScore());
    }

    @Test
    public void testResetScoreKeepsHighScore() {
        player.addScore(300);
        assertEquals(300, player.getScore());
        assertEquals(300, player.getHighScore());

        player.resetScore();
        assertEquals(0, player.getScore());
        assertEquals(300, player.getHighScore());

        player.addScore(100);
        assertEquals(100, player.getScore());
        assertEquals(300, player.getHighScore());

        player.addScore(250);
        assertEquals(350, player.getScore());
        assertEquals(350, player.getHighScore());
    }

    @Test
    public void testSetHighScore() {
        player.setHighScore(500);
        assertEquals(500, player.getHighScore());

        player.addScore(200);
        assertEquals(200, player.getScore());
        assertEquals(500, player.getHighScore());

        player.addScore(400);
        assertEquals(600, player.getScore());
        assertEquals(600, player.getHighScore());
    }

    @Test
    public void testPreferencesLoadAndSave() {
        Preferences dummyPrefs = new DummyPreferences();
        Application dummyApp = (Application) Proxy.newProxyInstance(
            Application.class.getClassLoader(),
            new Class<?>[]{ Application.class },
            (proxy, method, args) -> {
                if ("getPreferences".equals(method.getName())) {
                    return dummyPrefs;
                }
                return null;
            }
        );
        Gdx.app = dummyApp;

        dummyPrefs.putInteger(Player.HIGH_SCORE_KEY, 1200);

        player.loadHighScore();
        assertEquals(1200, player.getHighScore());

        player.addScore(1500);
        player.saveHighScore();

        assertEquals(1500, dummyPrefs.getInteger(Player.HIGH_SCORE_KEY, 0));

        Player player2 = new Player(1000, 100);
        player2.loadHighScore();
        assertEquals(1500, player2.getHighScore());

        Gdx.app = null;
    }

    private static class DummyPreferences implements Preferences {
        private final Map<String, Integer> ints = new HashMap<>();

        @Override
        public Preferences putBoolean(String key, boolean val) { return this; }
        @Override
        public Preferences putInteger(String key, int val) { ints.put(key, val); return this; }
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
        public boolean getBoolean(String key, boolean defValue) { return defValue; }
        @Override
        public int getInteger(String key) { return ints.getOrDefault(key, 0); }
        @Override
        public int getInteger(String key, int defValue) { return ints.getOrDefault(key, defValue); }
        @Override
        public long getLong(String key) { return 0; }
        @Override
        public long getLong(String key, long defValue) { return defValue; }
        @Override
        public float getFloat(String key) { return 0; }
        @Override
        public float getFloat(String key, float defValue) { return defValue; }
        @Override
        public String getString(String key) { return ""; }
        @Override
        public String getString(String key, String defValue) { return defValue; }
        @Override
        public Map<String, ?> get() { return ints; }
        @Override
        public boolean contains(String key) { return ints.containsKey(key); }
        @Override
        public void clear() { ints.clear(); }
        @Override
        public void remove(String key) { ints.remove(key); }
        @Override
        public void flush() {}
    }
}
