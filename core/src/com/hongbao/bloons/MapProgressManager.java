package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MapProgressManager {

	public static final String PREFERENCE_NAME = "BloonsTouhouDefense";
	private static final String UNLOCKED_KEY_PREFIX = "map_unlocked_";
	private static final String HIGHEST_LEVEL_KEY_PREFIX = "map_highest_level_";

	public static Preferences getPreferences() {
		return Gdx.app.getPreferences(PREFERENCE_NAME);
	}

	public static void init() {
		Preferences prefs = getPreferences();
		if (!prefs.contains(UNLOCKED_KEY_PREFIX + MapType.BASIC_MAP.name())) {
			prefs.putBoolean(UNLOCKED_KEY_PREFIX + MapType.BASIC_MAP.name(), true);
			prefs.flush();
		}
	}

	public static boolean isMapUnlocked(MapType mapType) {
		if (mapType == null) {
			return false;
		}
		if (mapType.getRequiredMap() == null) {
			return true;
		}
		Preferences prefs = getPreferences();
		return prefs.getBoolean(UNLOCKED_KEY_PREFIX + mapType.name(), false);
	}

	public static void unlockMap(MapType mapType) {
		if (mapType == null) {
			return;
		}
		Preferences prefs = getPreferences();
		prefs.putBoolean(UNLOCKED_KEY_PREFIX + mapType.name(), true);
		prefs.flush();
	}

	public static int getHighestLevel(MapType mapType) {
		if (mapType == null) {
			return 0;
		}
		Preferences prefs = getPreferences();
		return prefs.getInteger(HIGHEST_LEVEL_KEY_PREFIX + mapType.name(), 0);
	}

	public static void updateHighestLevel(MapType mapType, int level) {
		if (mapType == null) {
			return;
		}
		Preferences prefs = getPreferences();
		int current = prefs.getInteger(HIGHEST_LEVEL_KEY_PREFIX + mapType.name(), 0);
		if (level > current) {
			prefs.putInteger(HIGHEST_LEVEL_KEY_PREFIX + mapType.name(), level);
			prefs.flush();
		}
	}

	public static void saveMapWin(MapType mapType) {
		if (mapType == null) {
			return;
		}
		Preferences prefs = getPreferences();
		// Mark current map as unlocked
		unlockMap(mapType);

		// Unlock next map(s) that require this map
		for (MapType nextMap : MapType.values()) {
			if (nextMap.getRequiredMap() == mapType) {
				unlockMap(nextMap);
			}
		}
	}
}
