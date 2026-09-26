package com.hongbao.bloons.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesSaveService {

    public static final String PREF_NAME = "BloonsTouhouSave";

    // Key constants for LibGDX Preferences storage
    public static final String KEY_UNLOCKED_LEVEL = "unlockedLevelIndex";
    public static final String KEY_TOTAL_SCORE = "totalScore";
    public static final String KEY_MONEY = "money";
    public static final String KEY_HEALTH = "health";
    public static final String KEY_CURRENT_LEVEL = "currentLevel";
    public static final String KEY_TRIPLE_SPEED = "tripleSpeed";
    public static final String KEY_AUTO_CONTINUE = "autoContinue";
    public static final String KEY_MUSIC_ENABLED = "musicEnabled";
    public static final String KEY_HAS_ACTIVE_SESSION = "hasActiveSession";

    private final Preferences preferences;

    public PreferencesSaveService() {
        this(Gdx.app != null ? Gdx.app.getPreferences(PREF_NAME) : null);
    }

    public PreferencesSaveService(Preferences preferences) {
        this.preferences = preferences;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    /**
     * Saves progress upon wave completion.
     */
    public void saveWaveCompletion(int completedLevelIndex, int totalScore, boolean tripleSpeed, boolean autoContinue, boolean musicEnabled, int money, int health) {
        if (preferences == null) return;

        int currentUnlocked = getUnlockedLevelIndex();
        int nextUnlocked = Math.max(currentUnlocked, completedLevelIndex);
        preferences.putInteger(KEY_UNLOCKED_LEVEL, nextUnlocked);

        int currentHighScore = getTotalScore();
        if (totalScore > currentHighScore) {
            preferences.putInteger(KEY_TOTAL_SCORE, totalScore);
        }

        preferences.putBoolean(KEY_TRIPLE_SPEED, tripleSpeed);
        preferences.putBoolean(KEY_AUTO_CONTINUE, autoContinue);
        preferences.putBoolean(KEY_MUSIC_ENABLED, musicEnabled);

        // Save active session checkpoint
        preferences.putInteger(KEY_CURRENT_LEVEL, completedLevelIndex);
        preferences.putInteger(KEY_MONEY, money);
        preferences.putInteger(KEY_HEALTH, health);
        preferences.putBoolean(KEY_HAS_ACTIVE_SESSION, true);

        flush();
    }

    /**
     * Saves current settings flags.
     */
    public void saveSettings(boolean tripleSpeed, boolean autoContinue, boolean musicEnabled) {
        if (preferences == null) return;
        preferences.putBoolean(KEY_TRIPLE_SPEED, tripleSpeed);
        preferences.putBoolean(KEY_AUTO_CONTINUE, autoContinue);
        preferences.putBoolean(KEY_MUSIC_ENABLED, musicEnabled);
        flush();
    }

    /**
     * Resets active session checkpoint entries in Preferences upon Game Over.
     */
    public void resetActiveSession() {
        if (preferences == null) return;
        preferences.remove(KEY_CURRENT_LEVEL);
        preferences.remove(KEY_MONEY);
        preferences.remove(KEY_HEALTH);
        preferences.putBoolean(KEY_HAS_ACTIVE_SESSION, false);
        flush();
    }

    public int getUnlockedLevelIndex() {
        return preferences != null ? preferences.getInteger(KEY_UNLOCKED_LEVEL, 0) : 0;
    }

    public int getTotalScore() {
        return preferences != null ? preferences.getInteger(KEY_TOTAL_SCORE, 0) : 0;
    }

    public int getMoney(int defaultMoney) {
        return preferences != null ? preferences.getInteger(KEY_MONEY, defaultMoney) : defaultMoney;
    }

    public int getHealth(int defaultHealth) {
        return preferences != null ? preferences.getInteger(KEY_HEALTH, defaultHealth) : defaultHealth;
    }

    public int getCurrentLevel(int defaultLevel) {
        return preferences != null ? preferences.getInteger(KEY_CURRENT_LEVEL, defaultLevel) : defaultLevel;
    }

    public boolean getTripleSpeed(boolean defaultVal) {
        return preferences != null ? preferences.getBoolean(KEY_TRIPLE_SPEED, defaultVal) : defaultVal;
    }

    public boolean getAutoContinue(boolean defaultVal) {
        return preferences != null ? preferences.getBoolean(KEY_AUTO_CONTINUE, defaultVal) : defaultVal;
    }

    public boolean getMusicEnabled(boolean defaultVal) {
        return preferences != null ? preferences.getBoolean(KEY_MUSIC_ENABLED, defaultVal) : defaultVal;
    }

    public boolean hasActiveSession() {
        return preferences != null && preferences.getBoolean(KEY_HAS_ACTIVE_SESSION, false);
    }

    /**
     * Flushes preference storage to disk/native storage.
     */
    public void flush() {
        if (preferences != null) {
            preferences.flush();
        }
    }
}
