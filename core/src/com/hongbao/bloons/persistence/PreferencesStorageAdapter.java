package com.hongbao.bloons.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Lightweight storage adapter implementation backed by LibGDX Preferences.
 */
public class PreferencesStorageAdapter implements PersistenceService {

    public static final String DEFAULT_PREF_NAME = "bloons_touhou_defense_save";
    private static final String KEY_MONEY = "money";
    private static final String KEY_HEALTH = "health";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_HAS_DATA = "has_data";

    private final String prefName;
    private Preferences preferences;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PreferencesStorageAdapter() {
        this(DEFAULT_PREF_NAME);
    }

    public PreferencesStorageAdapter(String prefName) {
        this.prefName = prefName;
    }

    public PreferencesStorageAdapter(Preferences preferences) {
        this.prefName = DEFAULT_PREF_NAME;
        this.preferences = preferences;
    }

    private Preferences getPrefs() {
        if (preferences != null) {
            return preferences;
        }
        if (Gdx.app == null) {
            throw new PersistenceException("LibGDX Gdx.app context is not initialized");
        }
        try {
            preferences = Gdx.app.getPreferences(prefName);
            return preferences;
        } catch (Exception e) {
            throw new PersistenceException("Failed to obtain LibGDX preferences: " + prefName, e);
        }
    }

    @Override
    public PlayerData loadData() {
        try {
            Preferences prefs = getPrefs();
            if (!prefs.contains(KEY_HAS_DATA) && !prefs.contains(KEY_MONEY)) {
                return new PlayerData();
            }
            int money = prefs.getInteger(KEY_MONEY, 1000);
            int health = prefs.getInteger(KEY_HEALTH, 100);
            int level = prefs.getInteger(KEY_LEVEL, 0);
            return new PlayerData(money, health, level);
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Error loading data from Preferences", e);
        }
    }

    @Override
    public void saveData(PlayerData data) {
        if (data == null) {
            throw new PersistenceException("Cannot save null PlayerData");
        }
        try {
            Preferences prefs = getPrefs();
            prefs.putInteger(KEY_MONEY, data.getMoney());
            prefs.putInteger(KEY_HEALTH, data.getHealth());
            prefs.putInteger(KEY_LEVEL, data.getLevel());
            prefs.putBoolean(KEY_HAS_DATA, true);
            prefs.flush();
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Error saving data to Preferences", e);
        }
    }

    @Override
    public void saveDataAsync(PlayerData data) {
        executorService.submit(() -> {
            try {
                saveData(data);
            } catch (Exception e) {
                // Background exception swallowed/logged to prevent crash
                if (Gdx.app != null) {
                    Gdx.app.error("PreferencesStorageAdapter", "Async save failed", e);
                }
            }
        });
    }

    @Override
    public void resetData() {
        try {
            Preferences prefs = getPrefs();
            prefs.clear();
            prefs.flush();
        } catch (PersistenceException pe) {
            throw pe;
        } catch (Exception e) {
            throw new PersistenceException("Error resetting Preferences data", e);
        }
    }

    @Override
    public PersistenceService getActiveAdapter() {
        return this;
    }
}
