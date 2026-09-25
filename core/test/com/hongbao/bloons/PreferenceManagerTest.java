package com.hongbao.bloons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class PreferenceManagerTest {

	private static HeadlessApplication application;

	@BeforeClass
	public static void setUpClass() {
		HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
		application = new HeadlessApplication(new ApplicationAdapter() {}, config);
	}

	@AfterClass
	public static void tearDownClass() {
		if (application != null) {
			application.exit();
		}
	}

	@Before
	public void setUp() {
		Preferences prefs = Gdx.app.getPreferences(PreferenceManager.PREF_NAME);
		prefs.clear();
		prefs.flush();
	}

	@Test
	public void testDefaultValues() {
		PreferenceManager pm = new PreferenceManager();
		assertEquals(PreferenceManager.DEFAULT_HIGH_SCORE, pm.getHighScore());
		assertEquals(PreferenceManager.DEFAULT_MUSIC_ENABLED, pm.isMusicEnabled());
		assertEquals(PreferenceManager.DEFAULT_VOLUME, pm.getVolume(), 0.001f);
		assertEquals(PreferenceManager.DEFAULT_AUTO_CONTINUE, pm.isAutoContinue());
		assertEquals(PreferenceManager.DEFAULT_TRIPLE_SPEED, pm.isTripleSpeed());
		Set<String> unlocked = pm.getUnlockedMaps();
		assertTrue(unlocked.contains("heater.png"));
		assertTrue(unlocked.contains("basic_map.png"));
		assertTrue(unlocked.contains("map_with_turn.png"));
	}

	@Test
	public void testUpdateAndRetrieveHighScore() {
		PreferenceManager pm = new PreferenceManager();
		pm.setHighScore(500);
		assertEquals(500, pm.getHighScore());

		pm.updateHighScore(300); // lower, should not update
		assertEquals(500, pm.getHighScore());

		pm.updateHighScore(1200); // higher, should update
		assertEquals(1200, pm.getHighScore());
	}

	@Test
	public void testSettingsPersistence() {
		PreferenceManager pm = new PreferenceManager();
		pm.setMusicEnabled(false);
		pm.setVolume(0.8f);
		pm.setAutoContinue(true);
		pm.setTripleSpeed(true);
		pm.flush();

		// Create new manager instance to verify reads from preferences
		PreferenceManager pm2 = new PreferenceManager();
		assertFalse(pm2.isMusicEnabled());
		assertEquals(0.8f, pm2.getVolume(), 0.001f);
		assertTrue(pm2.isAutoContinue());
		assertTrue(pm2.isTripleSpeed());
	}

	@Test
	public void testUnlockMap() {
		PreferenceManager pm = new PreferenceManager();
		pm.unlockMap("custom_map.png");
		assertTrue(pm.isMapUnlocked("custom_map.png"));

		PreferenceManager pm2 = new PreferenceManager();
		assertTrue(pm2.isMapUnlocked("custom_map.png"));
	}

	@Test
	public void testPlayerHighScoreIntegration() {
		PreferenceManager pm = new PreferenceManager();
		Player player = new Player(100, 100);
		player.setPreferenceManager(pm);

		player.earnMoney(500);
		assertEquals(500, player.getHighScore());
		assertEquals(500, pm.getHighScore());

		// New session with saved high score
		Player newPlayer = new Player(100, 100);
		newPlayer.setPreferenceManager(pm);
		assertEquals(500, newPlayer.getHighScore());
	}

	@Test
	public void testMusicPlayerSettingsIntegration() {
		PreferenceManager pm = new PreferenceManager();
		MusicPlayer mp = new MusicPlayer(pm);

		mp.setVolume(0.75f);
		mp.setMusicEnabled(false);

		assertEquals(0.75f, pm.getVolume(), 0.001f);
		assertFalse(pm.isMusicEnabled());

		MusicPlayer mp2 = new MusicPlayer(pm);
		assertEquals(0.75f, mp2.getVolume(), 0.001f);
		assertFalse(mp2.isMusicEnabled());
	}

	@Test
	public void testCorruptedPreferencesFallback() {
		MockPreferences mockPrefs = new MockPreferences();
		mockPrefs.throwExceptionOnRead = true;

		PreferenceManager pm = new PreferenceManager(mockPrefs);
		assertEquals(PreferenceManager.DEFAULT_HIGH_SCORE, pm.getHighScore());
		assertEquals(PreferenceManager.DEFAULT_MUSIC_ENABLED, pm.isMusicEnabled());
		assertEquals(PreferenceManager.DEFAULT_VOLUME, pm.getVolume(), 0.001f);
		assertEquals(PreferenceManager.DEFAULT_AUTO_CONTINUE, pm.isAutoContinue());
		assertEquals(PreferenceManager.DEFAULT_TRIPLE_SPEED, pm.isTripleSpeed());
		assertTrue(pm.getUnlockedMaps().contains("heater.png"));
	}

	private static class MockPreferences implements Preferences {
		boolean throwExceptionOnRead = false;
		Map<String, Object> map = new HashMap<>();

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
		public boolean getBoolean(String key) { return getBoolean(key, false); }
		@Override
		public int getInteger(String key) { return getInteger(key, 0); }
		@Override
		public long getLong(String key) { return getLong(key, 0L); }
		@Override
		public float getFloat(String key) { return getFloat(key, 0f); }
		@Override
		public String getString(String key) { return getString(key, ""); }

		@Override
		public boolean getBoolean(String key, boolean defValue) {
			if (throwExceptionOnRead) throw new ClassCastException("Corrupted data");
			return map.containsKey(key) ? (Boolean) map.get(key) : defValue;
		}

		@Override
		public int getInteger(String key, int defValue) {
			if (throwExceptionOnRead) throw new ClassCastException("Corrupted data");
			return map.containsKey(key) ? (Integer) map.get(key) : defValue;
		}

		@Override
		public long getLong(String key, long defValue) {
			if (throwExceptionOnRead) throw new ClassCastException("Corrupted data");
			return map.containsKey(key) ? (Long) map.get(key) : defValue;
		}

		@Override
		public float getFloat(String key, float defValue) {
			if (throwExceptionOnRead) throw new ClassCastException("Corrupted data");
			return map.containsKey(key) ? (Float) map.get(key) : defValue;
		}

		@Override
		public String getString(String key, String defValue) {
			if (throwExceptionOnRead) throw new ClassCastException("Corrupted data");
			return map.containsKey(key) ? (String) map.get(key) : defValue;
		}

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
}
