package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.utils.Clipboard;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreTest {

	private MockPreferences mockPreferences;

	private static class MockPreferences implements Preferences {
		private final Map<String, Object> storage = new HashMap<>();

		@Override public Preferences putBoolean(String key, boolean val) { storage.put(key, val); return this; }
		@Override public Preferences putInteger(String key, int val) { storage.put(key, val); return this; }
		@Override public Preferences putLong(String key, long val) { storage.put(key, val); return this; }
		@Override public Preferences putFloat(String key, float val) { storage.put(key, val); return this; }
		@Override public Preferences putString(String key, String val) { storage.put(key, val); return this; }
		@Override public Preferences put(Map<String, ?> vals) { storage.putAll(vals); return this; }

		@Override public boolean getBoolean(String key) { return (Boolean) storage.getOrDefault(key, false); }
		@Override public int getInteger(String key) { return (Integer) storage.getOrDefault(key, 0); }
		@Override public long getLong(String key) { return (Long) storage.getOrDefault(key, 0L); }
		@Override public float getFloat(String key) { return (Float) storage.getOrDefault(key, 0f); }
		@Override public String getString(String key) { return (String) storage.getOrDefault(key, ""); }

		@Override public boolean getBoolean(String key, boolean defValue) { return (Boolean) storage.getOrDefault(key, defValue); }
		@Override public int getInteger(String key, int defValue) { return (Integer) storage.getOrDefault(key, defValue); }
		@Override public long getLong(String key, long defValue) { return (Long) storage.getOrDefault(key, defValue); }
		@Override public float getFloat(String key, float defValue) { return (Float) storage.getOrDefault(key, defValue); }
		@Override public String getString(String key, String defValue) { return (String) storage.getOrDefault(key, defValue); }

		@Override public Map<String, ?> get() { return storage; }
		@Override public boolean contains(String key) { return storage.containsKey(key); }
		@Override public void clear() { storage.clear(); }
		@Override public void remove(String key) { storage.remove(key); }
		@Override public void flush() { }
	}

	private static class MockApplication implements Application {
		private final MockPreferences preferences;

		MockApplication(MockPreferences preferences) {
			this.preferences = preferences;
		}

		@Override public Preferences getPreferences(String name) { return preferences; }
		@Override public ApplicationListener getApplicationListener() { return null; }
		@Override public Graphics getGraphics() { return null; }
		@Override public Audio getAudio() { return null; }
		@Override public Input getInput() { return null; }
		@Override public Files getFiles() { return null; }
		@Override public Net getNet() { return null; }
		@Override public Clipboard getClipboard() { return null; }
		@Override public com.badlogic.gdx.ApplicationLogger getApplicationLogger() { return null; }
		@Override public void setApplicationLogger(com.badlogic.gdx.ApplicationLogger applicationLogger) {}
		@Override public void log(String tag, String message) {}
		@Override public void log(String tag, String message, Throwable exception) {}
		@Override public void error(String tag, String message) {}
		@Override public void error(String tag, String message, Throwable exception) {}
		@Override public void debug(String tag, String message) {}
		@Override public void debug(String tag, String message, Throwable exception) {}
		@Override public void setLogLevel(int logLevel) {}
		@Override public int getLogLevel() { return 0; }
		@Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
		@Override public int getVersion() { return 0; }
		@Override public long getJavaHeap() { return 0; }
		@Override public long getNativeHeap() { return 0; }
		@Override public void addLifecycleListener(LifecycleListener listener) {}
		@Override public void removeLifecycleListener(LifecycleListener listener) {}
		@Override public void postRunnable(Runnable runnable) {}
		@Override public void exit() {}
	}

	private static void assertEquals(String msg, int expected, int actual) {
		if (expected != actual) {
			throw new AssertionError(msg + " - Expected: " + expected + ", Actual: " + actual);
		}
	}

	private static void assertEquals(int expected, int actual) {
		assertEquals("", expected, actual);
	}

	public void setUp() {
		mockPreferences = new MockPreferences();
		Gdx.app = new MockApplication(mockPreferences);
	}

	public void testInitialScoreAndHighScore() {
		Player player = new Player();
		assertEquals("Initial score should be 0", 0, player.getScore());
		assertEquals("Initial high score should be 0 when no stored prefs", 0, player.getHighScore());
	}

	public void testScoreAccumulation() {
		Player player = new Player();
		player.addScore(10);
		assertEquals(10, player.getScore());
		assertEquals(10, player.getHighScore());

		player.addScore(25);
		assertEquals(35, player.getScore());
		assertEquals(35, player.getHighScore());
	}

	public void testHighScorePersistenceAcrossAppRestart() {
		Player session1 = new Player();
		session1.addScore(150);
		assertEquals(150, session1.getScore());
		assertEquals(150, session1.getHighScore());
		assertEquals(150, mockPreferences.getInteger("highScore"));

		// Simulate app restart by creating a new Player instance with stored preferences intact
		Player session2 = new Player();
		assertEquals("Current score in new session should start at 0", 0, session2.getScore());
		assertEquals("High score should persist from previous session", 150, session2.getHighScore());

		// Score less than high score should not overwrite high score
		session2.addScore(50);
		assertEquals(50, session2.getScore());
		assertEquals(150, session2.getHighScore());

		// Score exceeding previous high score should update high score
		session2.addScore(200); // 50 + 200 = 250
		assertEquals(250, session2.getScore());
		assertEquals(250, session2.getHighScore());
		assertEquals(250, mockPreferences.getInteger("highScore"));
	}

	public void testResetScore() {
		Player player = new Player();
		player.addScore(100);
		assertEquals(100, player.getScore());
		assertEquals(100, player.getHighScore());

		player.resetScore();
		assertEquals("Score should reset to 0", 0, player.getScore());
		assertEquals("High score should remain unchanged on score reset", 100, player.getHighScore());
	}

	public void testPreventNegativeScore() {
		Player player = new Player();
		player.addScore(-50);
		assertEquals("Adding negative points should be ignored/prevented", 0, player.getScore());

		player.addScore(20);
		player.addScore(0);
		assertEquals(20, player.getScore());
	}

	public static void main(String[] args) {
		PlayerScoreTest test = new PlayerScoreTest();
		test.setUp();
		test.testInitialScoreAndHighScore();

		test.setUp();
		test.testScoreAccumulation();

		test.setUp();
		test.testHighScorePersistenceAcrossAppRestart();

		test.setUp();
		test.testResetScore();

		test.setUp();
		test.testPreventNegativeScore();

		System.out.println("ALL PLAYER SCORE TESTS PASSED SUCCESSFULLY!");
	}
}
