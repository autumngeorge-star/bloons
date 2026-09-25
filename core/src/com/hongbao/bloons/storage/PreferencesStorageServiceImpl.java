package com.hongbao.bloons.storage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesStorageServiceImpl implements GameStorageService {

    public static final String DEFAULT_PREFS_NAME = "com.hongbao.bloons.preferences";
    public static final String HIGH_SCORE_KEY = "high_score";

    private final String prefsName;
    private Preferences preferences;

    public PreferencesStorageServiceImpl() {
        this(DEFAULT_PREFS_NAME);
    }

    public PreferencesStorageServiceImpl(String prefsName) {
        this.prefsName = prefsName;
        if (Gdx.app != null) {
            this.preferences = Gdx.app.getPreferences(prefsName);
        }
    }

    public PreferencesStorageServiceImpl(Preferences preferences) {
        this.prefsName = DEFAULT_PREFS_NAME;
        this.preferences = preferences;
    }

    private Preferences getPrefs() {
        if (preferences == null && Gdx.app != null) {
            preferences = Gdx.app.getPreferences(prefsName);
        }
        return preferences;
    }

    @Override
    public int loadHighScore() {
        return loadScore(HIGH_SCORE_KEY, 0);
    }

    @Override
    public void saveHighScore(int highScore) {
        saveScore(HIGH_SCORE_KEY, highScore);
    }

    @Override
    public int loadScore(String key, int defaultValue) {
        Preferences prefs = getPrefs();
        if (prefs != null) {
            return prefs.getInteger(key, defaultValue);
        }
        return defaultValue;
    }

    @Override
    public void saveScore(String key, int value) {
        Preferences prefs = getPrefs();
        if (prefs != null) {
            prefs.putInteger(key, value);
            prefs.flush();
        }
    }

    @Override
    public void clear() {
        Preferences prefs = getPrefs();
        if (prefs != null) {
            prefs.clear();
            prefs.flush();
        }
    }
}
