package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashSet;
import java.util.Set;

public class PreferenceManager {

	public static final String PREF_NAME = "bloons_preferences";

	public static final String KEY_HIGH_SCORE = "high_score";
	public static final String KEY_UNLOCKED_MAPS = "unlocked_maps";
	public static final String KEY_MUSIC_ENABLED = "music_enabled";
	public static final String KEY_VOLUME = "volume";
	public static final String KEY_AUTO_CONTINUE = "auto_continue";
	public static final String KEY_TRIPLE_SPEED = "triple_speed";

	public static final int DEFAULT_HIGH_SCORE = 0;
	public static final String DEFAULT_UNLOCKED_MAPS = "heater.png,basic_map.png,map_with_turn.png";
	public static final boolean DEFAULT_MUSIC_ENABLED = true;
	public static final float DEFAULT_VOLUME = 0.5f;
	public static final boolean DEFAULT_AUTO_CONTINUE = false;
	public static final boolean DEFAULT_TRIPLE_SPEED = false;

	private final Preferences preferences;

	public PreferenceManager() {
		this(null);
	}

	public PreferenceManager(Preferences preferences) {
		this.preferences = preferences;
	}

	private Preferences getPrefs() {
		if (preferences != null) {
			return preferences;
		}
		if (Gdx.app != null) {
			return Gdx.app.getPreferences(PREF_NAME);
		}
		return null;
	}

	public int getHighScore() {
		Preferences prefs = getPrefs();
		if (prefs == null) return DEFAULT_HIGH_SCORE;
		try {
			return prefs.getInteger(KEY_HIGH_SCORE, DEFAULT_HIGH_SCORE);
		} catch (Exception e) {
			return DEFAULT_HIGH_SCORE;
		}
	}

	public void setHighScore(int score) {
		Preferences prefs = getPrefs();
		if (prefs == null) return;
		try {
			prefs.putInteger(KEY_HIGH_SCORE, score);
		} catch (Exception ignored) {
		}
	}

	public boolean updateHighScore(int score) {
		if (score > getHighScore()) {
			setHighScore(score);
			return true;
		}
		return false;
	}

	public Set<String> getUnlockedMaps() {
		Preferences prefs = getPrefs();
		String raw = null;
		if (prefs != null) {
			try {
				raw = prefs.getString(KEY_UNLOCKED_MAPS, DEFAULT_UNLOCKED_MAPS);
			} catch (Exception e) {
				raw = DEFAULT_UNLOCKED_MAPS;
			}
		}
		if (raw == null || raw.trim().isEmpty()) {
			raw = DEFAULT_UNLOCKED_MAPS;
		}
		String[] parts = raw.split(",");
		Set<String> maps = new HashSet<>();
		for (String p : parts) {
			if (!p.trim().isEmpty()) {
				maps.add(p.trim());
			}
		}
		return maps;
	}

	public boolean isMapUnlocked(String mapName) {
		return getUnlockedMaps().contains(mapName);
	}

	public void unlockMap(String mapName) {
		if (mapName == null || mapName.trim().isEmpty()) return;
		Set<String> maps = getUnlockedMaps();
		if (!maps.contains(mapName)) {
			maps.add(mapName);
			StringBuilder sb = new StringBuilder();
			for (String map : maps) {
				if (sb.length() > 0) sb.append(",");
				sb.append(map);
			}
			Preferences prefs = getPrefs();
			if (prefs != null) {
				try {
					prefs.putString(KEY_UNLOCKED_MAPS, sb.toString());
				} catch (Exception ignored) {
				}
			}
		}
	}

	public boolean isMusicEnabled() {
		Preferences prefs = getPrefs();
		if (prefs == null) return DEFAULT_MUSIC_ENABLED;
		try {
			return prefs.getBoolean(KEY_MUSIC_ENABLED, DEFAULT_MUSIC_ENABLED);
		} catch (Exception e) {
			return DEFAULT_MUSIC_ENABLED;
		}
	}

	public void setMusicEnabled(boolean enabled) {
		Preferences prefs = getPrefs();
		if (prefs == null) return;
		try {
			prefs.putBoolean(KEY_MUSIC_ENABLED, enabled);
		} catch (Exception ignored) {
		}
	}

	public float getVolume() {
		Preferences prefs = getPrefs();
		if (prefs == null) return DEFAULT_VOLUME;
		try {
			return prefs.getFloat(KEY_VOLUME, DEFAULT_VOLUME);
		} catch (Exception e) {
			return DEFAULT_VOLUME;
		}
	}

	public void setVolume(float volume) {
		Preferences prefs = getPrefs();
		if (prefs == null) return;
		try {
			prefs.putFloat(KEY_VOLUME, volume);
		} catch (Exception ignored) {
		}
	}

	public boolean isAutoContinue() {
		Preferences prefs = getPrefs();
		if (prefs == null) return DEFAULT_AUTO_CONTINUE;
		try {
			return prefs.getBoolean(KEY_AUTO_CONTINUE, DEFAULT_AUTO_CONTINUE);
		} catch (Exception e) {
			return DEFAULT_AUTO_CONTINUE;
		}
	}

	public void setAutoContinue(boolean autoContinue) {
		Preferences prefs = getPrefs();
		if (prefs == null) return;
		try {
			prefs.putBoolean(KEY_AUTO_CONTINUE, autoContinue);
		} catch (Exception ignored) {
		}
	}

	public boolean isTripleSpeed() {
		Preferences prefs = getPrefs();
		if (prefs == null) return DEFAULT_TRIPLE_SPEED;
		try {
			return prefs.getBoolean(KEY_TRIPLE_SPEED, DEFAULT_TRIPLE_SPEED);
		} catch (Exception e) {
			return DEFAULT_TRIPLE_SPEED;
		}
	}

	public void setTripleSpeed(boolean tripleSpeed) {
		Preferences prefs = getPrefs();
		if (prefs == null) return;
		try {
			prefs.putBoolean(KEY_TRIPLE_SPEED, tripleSpeed);
		} catch (Exception ignored) {
		}
	}

	public void flush() {
		Preferences prefs = getPrefs();
		if (prefs != null) {
			try {
				prefs.flush();
			} catch (Exception ignored) {
			}
		}
	}
}
