package com.hongbao.bloons.services;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.hongbao.bloons.Player;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class PreferencesSaveServiceTest {

    private static HeadlessApplication app;

    @BeforeClass
    public static void initGdx() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        app = new HeadlessApplication(new ApplicationAdapter() {}, config);
    }

    @AfterClass
    public static void disposeGdx() {
        if (app != null) {
            app.exit();
        }
    }

    @Before
    public void clearPreferences() {
        Preferences prefs = Gdx.app.getPreferences(PreferencesSaveService.PREF_NAME);
        prefs.clear();
        prefs.flush();
    }

    @Test
    public void testPreferenceStorageName() {
        Preferences prefs = Gdx.app.getPreferences("BloonsTouhouSave");
        assertNotNull(prefs);
        PreferencesSaveService service = new PreferencesSaveService();
        assertEquals(prefs, service.getPreferences());
    }

    @Test
    public void testSaveWaveCompletion() {
        PreferencesSaveService service = new PreferencesSaveService();
        service.saveWaveCompletion(15, 5000, true, true, true, 1200, 80);

        assertEquals(15, service.getUnlockedLevelIndex());
        assertEquals(5000, service.getTotalScore());
        assertTrue(service.getTripleSpeed(false));
        assertTrue(service.getAutoContinue(false));
        assertTrue(service.getMusicEnabled(false));
        assertEquals(1200, service.getMoney(0));
        assertEquals(80, service.getHealth(0));
        assertTrue(service.hasActiveSession());
    }

    @Test
    public void testGameOverResetsActiveSession() {
        PreferencesSaveService service = new PreferencesSaveService();
        service.saveWaveCompletion(10, 3000, false, true, true, 800, 50);

        assertTrue(service.hasActiveSession());
        assertEquals(10, service.getUnlockedLevelIndex());

        // Simulate Game Over
        service.resetActiveSession();

        assertFalse(service.hasActiveSession());
        // Unlocked level and score should remain preserved
        assertEquals(10, service.getUnlockedLevelIndex());
        assertEquals(3000, service.getTotalScore());
        // Active session checkpoint entries reset
        assertEquals(1000, service.getMoney(1000));
        assertEquals(100, service.getHealth(100));
    }

    @Test
    public void testSaveWaveCompletionProgressiveUnlockedLevel() {
        PreferencesSaveService service = new PreferencesSaveService();
        service.saveWaveCompletion(5, 1000, false, false, true, 1000, 100);
        assertEquals(5, service.getUnlockedLevelIndex());

        // Completing wave 12 increases unlocked level index to 12
        service.saveWaveCompletion(12, 2500, false, false, true, 1500, 100);
        assertEquals(12, service.getUnlockedLevelIndex());

        // Completing a lower wave level index does not decrease unlocked level index
        service.saveWaveCompletion(3, 3000, false, false, true, 1600, 100);
        assertEquals(12, service.getUnlockedLevelIndex());
    }

    @Test
    public void testSaveSettings() {
        PreferencesSaveService service = new PreferencesSaveService();
        service.saveSettings(true, false, true);

        assertTrue(service.getTripleSpeed(false));
        assertFalse(service.getAutoContinue(true));
        assertTrue(service.getMusicEnabled(false));
    }

    @Test
    public void testPlayerScoreTracking() {
        Player player = new Player(1000, 100);
        assertEquals(0, player.getScore());
        player.earnMoney(250);
        assertEquals(1250, player.getMoney());
        assertEquals(250, player.getScore());
    }
}
