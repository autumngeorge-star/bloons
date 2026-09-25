package com.hongbao.bloons;

import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.events.BloonEventListener;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ScoreManagerTest {

    private InMemoryPreferences preferences;
    private ScoreManager scoreManager;

    @Before
    public void setUp() {
        preferences = new InMemoryPreferences();
        scoreManager = new ScoreManager(preferences);
    }

    @Test
    public void testInitialScoreAndHighScore() {
        assertEquals(0, scoreManager.getScore());
        assertEquals(0, scoreManager.getHighScore());
    }

    @Test
    public void testLoadHighScoreFromPreferences() {
        preferences.putInteger(ScoreManager.KEY_HIGH_SCORE, 500);
        ScoreManager loadedManager = new ScoreManager(preferences);
        assertEquals(0, loadedManager.getScore());
        assertEquals(500, loadedManager.getHighScore());
    }

    @Test
    public void testOnBloonDamagedIncreasesScoreAndHighScore() {
        scoreManager.onBloonDamaged(null, 3);
        assertEquals(30, scoreManager.getScore());
        assertEquals(30, scoreManager.getHighScore());
        assertEquals(30, preferences.getInteger(ScoreManager.KEY_HIGH_SCORE, 0));
        assertTrue(preferences.flushed);
    }

    @Test
    public void testOnBloonPoppedIncreasesScore() {
        Bloon redBloon = BloonFactory.createRedBloon();
        BloonPoppedResult result = new BloonPoppedResult(redBloon, 1);
        scoreManager.onBloonPopped(null, result);
        assertEquals(10, scoreManager.getScore());
        assertEquals(10, scoreManager.getHighScore());
    }

    @Test
    public void testOnLevelClearedBonus() {
        scoreManager.onLevelCleared(3);
        assertEquals(300, scoreManager.getScore());
        assertEquals(300, scoreManager.getHighScore());
    }

    @Test
    public void testResetScoreKeepsHighScore() {
        scoreManager.addScore(200);
        assertEquals(200, scoreManager.getScore());
        assertEquals(200, scoreManager.getHighScore());

        scoreManager.resetScore();
        assertEquals(0, scoreManager.getScore());
        assertEquals(200, scoreManager.getHighScore());
    }

    static class InMemoryPreferences implements Preferences {
        private final Map<String, Object> map = new HashMap<>();
        boolean flushed = false;

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
        public void flush() { flushed = true; }
    }
}
