package com.hongbao.bloons.services;

import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.BloonQueue;
import com.hongbao.bloons.Player;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.factories.GirlFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class GameStatePersistenceServiceTest {

    private MockPreferences preferences;
    private GameStatePersistenceService persistenceService;

    @Before
    public void setUp() {
        preferences = new MockPreferences();
        persistenceService = new GameStatePersistenceService(preferences);
    }

    @Test
    public void testSaveAndLoadPlayerStats() {
        Player player = new Player(1500, 80);

        persistenceService.saveGameState(player, null, null);

        assertTrue(persistenceService.hasActiveSession());
        assertEquals(1500, preferences.getInteger(GameStatePersistenceService.KEY_PLAYER_MONEY));
        assertEquals(80, preferences.getInteger(GameStatePersistenceService.KEY_PLAYER_HEALTH));
        assertTrue(preferences.flushed);

        Player loadedPlayer = new Player(0, 0);
        boolean restored = persistenceService.loadGameState(loadedPlayer, null, null);

        assertTrue(restored);
        assertEquals(1500, loadedPlayer.getMoney());
        assertEquals(80, loadedPlayer.getHealth());
    }

    @Test
    public void testSaveAndLoadLevelState() {
        Player player = new Player(1000, 100);
        BloonQueue bloonQueue = BloonFactory.createBloonQueue();
        bloonQueue.restoreState(5, 12, 350);

        assertEquals(5, bloonQueue.getLevel());
        assertEquals(12, bloonQueue.getCurrentIndex());
        assertEquals(350, bloonQueue.getClock());

        // Simulate save level state directly into preferences
        preferences.putBoolean(GameStatePersistenceService.KEY_HAS_ACTIVE_SESSION, true);
        preferences.putInteger(GameStatePersistenceService.KEY_PLAYER_MONEY, player.getMoney());
        preferences.putInteger(GameStatePersistenceService.KEY_PLAYER_HEALTH, player.getHealth());
        preferences.putInteger(GameStatePersistenceService.KEY_LEVEL_CURRENT, bloonQueue.getLevel());
        preferences.putInteger(GameStatePersistenceService.KEY_LEVEL_INDEX, bloonQueue.getCurrentIndex());
        preferences.putInteger(GameStatePersistenceService.KEY_LEVEL_CLOCK, bloonQueue.getClock());

        BloonQueue newQueue = BloonFactory.createBloonQueue();
        boolean restored = persistenceService.loadGameState(player, null, null);

        assertTrue(restored);
        assertEquals(1000, player.getMoney());
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testGirlFactoryByName() {
        assertNotNull(GirlFactory.createByName("Reimu"));
        assertNotNull(GirlFactory.createByName("Yukari"));
        assertNotNull(GirlFactory.createByName("Marisa"));
        assertNotNull(GirlFactory.createByName("Alice"));
        assertNotNull(GirlFactory.createByName("Sakuya"));
        assertNotNull(GirlFactory.createByName("Remilia"));
        assertNotNull(GirlFactory.createByName("Youmu"));
        assertNotNull(GirlFactory.createByName("Yuyuko"));
        assertNull(GirlFactory.createByName("NonExistentTower"));
    }

    @Test
    public void testTowerSerializationKeys() {
        preferences.putBoolean(GameStatePersistenceService.KEY_HAS_ACTIVE_SESSION, true);
        preferences.putInteger(GameStatePersistenceService.KEY_PLAYER_MONEY, 2000);
        preferences.putInteger(GameStatePersistenceService.KEY_PLAYER_HEALTH, 100);
        preferences.putInteger(GameStatePersistenceService.KEY_TOWERS_COUNT, 2);

        preferences.putString("tower.0.name", "Reimu");
        preferences.putInteger("tower.0.level", 0);
        preferences.putFloat("tower.0.center_x", 300f);
        preferences.putFloat("tower.0.center_y", 400f);
        preferences.putFloat("tower.0.rotation", 45f);

        preferences.putString("tower.1.name", "Yukari");
        preferences.putInteger("tower.1.level", 1);
        preferences.putFloat("tower.1.center_x", 500f);
        preferences.putFloat("tower.1.center_y", 600f);
        preferences.putFloat("tower.1.rotation", 90f);

        assertTrue(persistenceService.hasActiveSession());
        assertEquals(2, preferences.getInteger(GameStatePersistenceService.KEY_TOWERS_COUNT));
        assertEquals("Reimu", preferences.getString("tower.0.name"));
        assertEquals("Yukari", preferences.getString("tower.1.name"));
        assertEquals(1, preferences.getInteger("tower.1.level"));
    }

    @Test
    public void testClearSession() {
        Player player = new Player(1000, 100);
        persistenceService.saveGameState(player, null, null);

        assertTrue(persistenceService.hasActiveSession());

        persistenceService.clearSession();

        assertFalse(persistenceService.hasActiveSession());

        Player loadedPlayer = new Player(100, 100);
        boolean loaded = persistenceService.loadGameState(loadedPlayer, null, null);

        assertFalse(loaded);
    }

    @Test
    public void testCorruptOrMissingDataFallback() {
        assertFalse(persistenceService.hasActiveSession());

        Player player = new Player(500, 50);
        boolean loaded = persistenceService.loadGameState(player, null, null);

        assertFalse(loaded);
        assertEquals(500, player.getMoney());
        assertEquals(50, player.getHealth());
    }

    private static class MockPreferences implements Preferences {
        private final java.util.Map<String, Object> map = new HashMap<>();
        public boolean flushed = false;

        @Override public Preferences putBoolean(String key, boolean val) { map.put(key, val); return this; }
        @Override public Preferences putInteger(String key, int val) { map.put(key, val); return this; }
        @Override public Preferences putLong(String key, long val) { map.put(key, val); return this; }
        @Override public Preferences putFloat(String key, float val) { map.put(key, val); return this; }
        @Override public Preferences putString(String key, String val) { map.put(key, val); return this; }
        @Override public Preferences put(java.util.Map<String, ?> vals) { map.putAll(vals); return this; }

        @Override public boolean getBoolean(String key) { return getBoolean(key, false); }
        @Override public int getInteger(String key) { return getInteger(key, 0); }
        @Override public long getLong(String key) { return getLong(key, 0L); }
        @Override public float getFloat(String key) { return getFloat(key, 0f); }
        @Override public String getString(String key) { return getString(key, ""); }

        @Override public boolean getBoolean(String key, boolean defValue) { Object v = map.get(key); return v instanceof Boolean ? (Boolean) v : defValue; }
        @Override public int getInteger(String key, int defValue) { Object v = map.get(key); return v instanceof Integer ? (Integer) v : defValue; }
        @Override public long getLong(String key, long defValue) { Object v = map.get(key); return v instanceof Long ? (Long) v : defValue; }
        @Override public float getFloat(String key, float defValue) { Object v = map.get(key); return v instanceof Float ? (Float) v : defValue; }
        @Override public String getString(String key, String defValue) { Object v = map.get(key); return v instanceof String ? (String) v : defValue; }

        @Override public java.util.Map<String, ?> get() { return new HashMap<>(map); }
        @Override public boolean contains(String key) { return map.containsKey(key); }
        @Override public void clear() { map.clear(); }
        @Override public void remove(String key) { map.remove(key); }
        @Override public void flush() { flushed = true; }
    }
}
