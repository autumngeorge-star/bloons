package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProgressionService {

    private static final String PREF_NAME = "bloons_touhou_defense_progression";
    private static final String UNLOCKED_PREFIX = "unlocked_map_";
    private static final String HIGHEST_LEVEL_PREFIX = "highest_level_";

    private final Preferences preferences;

    public ProgressionService() {
        this(Gdx.app != null ? Gdx.app.getPreferences(PREF_NAME) : null);
    }

    public ProgressionService(Preferences preferences) {
        if (preferences == null && Gdx.app != null) {
            preferences = Gdx.app.getPreferences(PREF_NAME);
        }
        this.preferences = preferences;

        if (this.preferences != null) {
            // Basic Map (index 0) is unlocked by default
            if (!this.preferences.contains(UNLOCKED_PREFIX + 0)) {
                this.preferences.putBoolean(UNLOCKED_PREFIX + 0, true);
                this.preferences.flush();
            }
        }
    }

    public boolean isMapUnlocked(MapType mapType) {
        if (mapType == null) {
            return false;
        }
        if (mapType.getIndex() == 0) {
            return true;
        }
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(UNLOCKED_PREFIX + mapType.getIndex(), false);
    }

    public boolean isMapUnlocked(int index) {
        if (index == 0) {
            return true;
        }
        if (preferences == null) {
            return false;
        }
        return preferences.getBoolean(UNLOCKED_PREFIX + index, false);
    }

    public void unlockMap(MapType mapType) {
        if (mapType != null && preferences != null) {
            preferences.putBoolean(UNLOCKED_PREFIX + mapType.getIndex(), true);
            preferences.flush();
        }
    }

    public void unlockMap(int index) {
        if (preferences != null) {
            preferences.putBoolean(UNLOCKED_PREFIX + index, true);
            preferences.flush();
        }
    }

    public int getHighestCompletedLevel(MapType mapType) {
        if (mapType == null || preferences == null) {
            return 0;
        }
        return preferences.getInteger(HIGHEST_LEVEL_PREFIX + mapType.getIndex(), 0);
    }

    public void saveProgress(MapType mapType, int completedLevel, boolean won) {
        if (mapType == null || preferences == null) {
            return;
        }
        int currentHighest = getHighestCompletedLevel(mapType);
        if (completedLevel > currentHighest) {
            preferences.putInteger(HIGHEST_LEVEL_PREFIX + mapType.getIndex(), completedLevel);
        }

        if (won || completedLevel >= 1) {
            int nextIndex = mapType.getIndex() + 1;
            if (nextIndex < MapType.values().length) {
                unlockMap(nextIndex);
            }
        }

        preferences.flush();
    }

    public void saveProgress() {
        if (preferences != null) {
            preferences.flush();
        }
    }

    public void resetProgress() {
        if (preferences != null) {
            preferences.clear();
            preferences.putBoolean(UNLOCKED_PREFIX + 0, true);
            preferences.flush();
        }
    }
}
