package com.hongbao.bloons.desktop;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.PreferencesManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class NativePreferencesIntegrationTest {

	private File testDir;

	@Before
	public void setUp() {
		testDir = new File("/tmp/bloons_test_prefs_" + System.currentTimeMillis());
		if (!testDir.exists()) {
			testDir.mkdirs();
		}
	}

	@After
	public void tearDown() {
		if (testDir != null && testDir.exists()) {
			File[] files = testDir.listFiles();
			if (files != null) {
				for (File f : files) {
					f.delete();
				}
			}
			testDir.delete();
		}
	}

	@Test
	public void testNativeLwjglPreferencesPersistenceAcrossRestarts() {
		FileHandle prefFile = new FileHandle(new File(testDir, PreferencesManager.PREF_NAME + ".xml"));
		Preferences nativePrefs1 = new LwjglPreferences(prefFile);

		PreferencesManager pm1 = new PreferencesManager(nativePrefs1);

		// Verify initial default unlocked level
		assertEquals(0, pm1.getUnlockedLevel(0));

		// Session 1: Player clears level 3 -> unlocks level 4
		pm1.saveUnlockedLevel(4);
		pm1.flush();

		assertTrue(prefFile.exists());

		// Session 2: Relaunch app reading preferences from disk
		Preferences nativePrefs2 = new LwjglPreferences(prefFile);
		PreferencesManager pm2 = new PreferencesManager(nativePrefs2);

		assertEquals(4, pm2.getUnlockedLevel(0));
	}
}
