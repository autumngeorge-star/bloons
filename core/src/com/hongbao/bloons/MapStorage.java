package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.factories.MapFactory;

public class MapStorage {

    private static final String PREFS_NAME = "bloons_touhou_defense_prefs";
    private static final String KEY_SELECTED_MAP = "selected_map";
    private static final String KEY_HIGHEST_LEVEL_PREFIX = "highest_level_";

    private static Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    public static String getSelectedMapKey() {
        if (Gdx.app == null) {
            return MapFactory.DEFAULT_MAP_KEY;
        }
        return getPreferences().getString(KEY_SELECTED_MAP, MapFactory.DEFAULT_MAP_KEY);
    }

    public static void setSelectedMapKey(String mapKey) {
        if (Gdx.app == null || mapKey == null) return;
        Preferences prefs = getPreferences();
        prefs.putString(KEY_SELECTED_MAP, mapKey);
        prefs.flush();
    }

    public static int getHighestLevel(String mapKey) {
        if (Gdx.app == null || mapKey == null) return 0;
        return getPreferences().getInteger(KEY_HIGHEST_LEVEL_PREFIX + mapKey, 0);
    }

    public static void updateHighestLevel(String mapKey, int level) {
        if (Gdx.app == null || mapKey == null || mapKey.trim().isEmpty()) return;
        int currentHighest = getHighestLevel(mapKey);
        if (level > currentHighest) {
            Preferences prefs = getPreferences();
            prefs.putInteger(KEY_HIGHEST_LEVEL_PREFIX + mapKey, level);
            prefs.flush();
        }
    }
}
