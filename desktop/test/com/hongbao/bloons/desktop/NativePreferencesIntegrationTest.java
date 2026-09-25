package com.hongbao.bloons.desktop;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import com.hongbao.bloons.BloonsTouhouDefense;
import com.hongbao.bloons.Player;
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

		// Verify initial defaults
		assertEquals(BloonsTouhouDefense.MONEY, pm1.getMoney(BloonsTouhouDefense.MONEY));
		assertEquals(BloonsTouhouDefense.HEALTH, pm1.getHealth(BloonsTouhouDefense.HEALTH));

		// Session 1: Create Player and perform game actions
		Player player1 = new Player(pm1.getMoney(BloonsTouhouDefense.MONEY), pm1.getHealth(BloonsTouhouDefense.HEALTH), pm1);
		player1.earnMoney(850);
		player1.decreaseHealth(25);

		pm1.saveLevel(4);
		pm1.saveUnlockedLevel(6);
		pm1.saveHighScore(12000);
		pm1.saveMusicEnabled(false);
		pm1.saveMusicVolume(0.7f);
		pm1.flush();

		assertTrue(prefFile.exists());

		// Session 2: Shutdown Session 1 and open a new session reading from disk
		Preferences nativePrefs2 = new LwjglPreferences(prefFile);
		PreferencesManager pm2 = new PreferencesManager(nativePrefs2);

		int loadedMoney = pm2.getMoney(BloonsTouhouDefense.MONEY);
		int loadedHealth = pm2.getHealth(BloonsTouhouDefense.HEALTH);
		Player player2 = new Player(loadedMoney, loadedHealth, pm2);

		assertEquals(1850, player2.getMoney());
		assertEquals(75, player2.getHealth());
		assertEquals(4, pm2.getLevel(1));
		assertEquals(6, pm2.getUnlockedLevel(1));
		assertEquals(12000, pm2.getHighScore(0));
		assertFalse(pm2.isMusicEnabled(true));
		assertEquals(0.7f, pm2.getMusicVolume(0.5f), 0.001f);
	}
}
