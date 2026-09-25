package com.hongbao.bloons;

import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

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
	public void testDefaultValuesFallback() {
		assertEquals(BloonsTouhouDefense.MONEY, preferencesManager.getMoney(BloonsTouhouDefense.MONEY));
		assertEquals(BloonsTouhouDefense.HEALTH, preferencesManager.getHealth(BloonsTouhouDefense.HEALTH));
		assertEquals(1, preferencesManager.getLevel(1));
		assertEquals(1, preferencesManager.getUnlockedLevel(1));
		assertEquals(0, preferencesManager.getHighScore(0));
		assertTrue(preferencesManager.isMusicEnabled(true));
		assertEquals(0.5f, preferencesManager.getMusicVolume(0.5f), 0.001f);
	}

	@Test
	public void testSaveAndReadPreferences() {
		preferencesManager.saveMoney(2500);
		preferencesManager.saveHealth(85);
		preferencesManager.saveLevel(5);
		preferencesManager.saveUnlockedLevel(10);
		preferencesManager.saveHighScore(15000);
		preferencesManager.saveMusicEnabled(false);
		preferencesManager.saveMusicVolume(0.8f);

		assertEquals(2500, preferencesManager.getMoney(BloonsTouhouDefense.MONEY));
		assertEquals(85, preferencesManager.getHealth(BloonsTouhouDefense.HEALTH));
		assertEquals(5, preferencesManager.getLevel(1));
		assertEquals(10, preferencesManager.getUnlockedLevel(1));
		assertEquals(15000, preferencesManager.getHighScore(0));
		assertFalse(preferencesManager.isMusicEnabled(true));
		assertEquals(0.8f, preferencesManager.getMusicVolume(0.5f), 0.001f);
	}

	@Test
	public void testPlayerAutomaticSyncAndFlush() {
		Player player = new Player(1000, 100, preferencesManager);
		int initialFlushCount = testPreferences.getFlushCount();

		player.earnMoney(500);
		assertEquals(1500, preferencesManager.getMoney(BloonsTouhouDefense.MONEY));
		assertTrue(testPreferences.getFlushCount() > initialFlushCount);

		int flushCountBeforeSpend = testPreferences.getFlushCount();
		boolean success = player.spendMoney(200);
		assertTrue(success);
		assertEquals(1300, preferencesManager.getMoney(BloonsTouhouDefense.MONEY));
		assertTrue(testPreferences.getFlushCount() > flushCountBeforeSpend);

		int flushCountBeforeHealth = testPreferences.getFlushCount();
		player.decreaseHealth(15);
		assertEquals(85, preferencesManager.getHealth(BloonsTouhouDefense.HEALTH));
		assertTrue(testPreferences.getFlushCount() > flushCountBeforeHealth);
	}

	@Test
	public void testSessionRelaunchStatePersistence() {
		// Session 1: Player plays and earns money / takes damage
		Player player1 = new Player(BloonsTouhouDefense.MONEY, BloonsTouhouDefense.HEALTH, preferencesManager);
		player1.earnMoney(750);
		player1.decreaseHealth(30);

		int session1EndingMoney = player1.getMoney();
		int session1EndingHealth = player1.getHealth();

		assertEquals(1750, session1EndingMoney);
		assertEquals(70, session1EndingHealth);

		// Session 2: Game is relaunched using stored preferences
		PreferencesManager reloadedPreferencesManager = new PreferencesManager(testPreferences);
		int loadedMoney = reloadedPreferencesManager.getMoney(BloonsTouhouDefense.MONEY);
		int loadedHealth = reloadedPreferencesManager.getHealth(BloonsTouhouDefense.HEALTH);
		Player player2 = new Player(loadedMoney, loadedHealth, reloadedPreferencesManager);

		assertEquals(session1EndingMoney, player2.getMoney());
		assertEquals(session1EndingHealth, player2.getHealth());
	}

	@Test
	public void testLoadPerformanceUnder50ms() {
		preferencesManager.saveMoney(3200);
		preferencesManager.saveHealth(90);
		preferencesManager.saveLevel(12);

		long startTime = System.nanoTime();

		PreferencesManager pm = new PreferencesManager(testPreferences);
		int m = pm.getMoney(BloonsTouhouDefense.MONEY);
		int h = pm.getHealth(BloonsTouhouDefense.HEALTH);
		int l = pm.getLevel(1);
		Player p = new Player(m, h, pm);

		long durationMs = (System.nanoTime() - startTime) / 1_000_000;

		assertEquals(3200, p.getMoney());
		assertEquals(90, p.getHealth());
		assertEquals(12, l);
		assertTrue("Startup state loading took " + durationMs + " ms, which exceeds 50 ms limit", durationMs < 50);
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
			return val instanceof Integer ? (Integer) val : defValue;
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
