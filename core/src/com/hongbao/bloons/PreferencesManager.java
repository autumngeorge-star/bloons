package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {

	public static final String PREF_NAME = "BloonsTouhouDefense";

	public static final String KEY_MONEY = "money";
	public static final String KEY_HEALTH = "health";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_UNLOCKED_LEVEL = "unlockedLevel";
	public static final String KEY_HIGH_SCORE = "highScore";
	public static final String KEY_MUSIC_ENABLED = "musicEnabled";
	public static final String KEY_MUSIC_VOLUME = "musicVolume";

	private Preferences preferences;

	public PreferencesManager() {
		if (Gdx.app != null) {
			this.preferences = Gdx.app.getPreferences(PREF_NAME);
		}
	}

	public PreferencesManager(Preferences preferences) {
		this.preferences = preferences;
	}

	public Preferences getPreferences() {
		if (preferences == null && Gdx.app != null) {
			preferences = Gdx.app.getPreferences(PREF_NAME);
		}
		return preferences;
	}

	public int getMoney(int defaultMoney) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_MONEY)) {
			try {
				return prefs.getInteger(KEY_MONEY, defaultMoney);
			} catch (Exception e) {
				return defaultMoney;
			}
		}
		return defaultMoney;
	}

	public void saveMoney(int money) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putInteger(KEY_MONEY, money);
			prefs.flush();
		}
	}

	public int getHealth(int defaultHealth) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_HEALTH)) {
			try {
				return prefs.getInteger(KEY_HEALTH, defaultHealth);
			} catch (Exception e) {
				return defaultHealth;
			}
		}
		return defaultHealth;
	}

	public void saveHealth(int health) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putInteger(KEY_HEALTH, health);
			prefs.flush();
		}
	}

	public int getLevel(int defaultLevel) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_LEVEL)) {
			try {
				return prefs.getInteger(KEY_LEVEL, defaultLevel);
			} catch (Exception e) {
				return defaultLevel;
			}
		}
		return defaultLevel;
	}

	public void saveLevel(int level) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putInteger(KEY_LEVEL, level);
			prefs.flush();
		}
	}

	public int getUnlockedLevel(int defaultUnlockedLevel) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_UNLOCKED_LEVEL)) {
			try {
				return prefs.getInteger(KEY_UNLOCKED_LEVEL, defaultUnlockedLevel);
			} catch (Exception e) {
				return defaultUnlockedLevel;
			}
		}
		return defaultUnlockedLevel;
	}

	public void saveUnlockedLevel(int level) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putInteger(KEY_UNLOCKED_LEVEL, level);
			prefs.flush();
		}
	}

	public int getHighScore(int defaultHighScore) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_HIGH_SCORE)) {
			try {
				return prefs.getInteger(KEY_HIGH_SCORE, defaultHighScore);
			} catch (Exception e) {
				return defaultHighScore;
			}
		}
		return defaultHighScore;
	}

	public void saveHighScore(int score) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putInteger(KEY_HIGH_SCORE, score);
			prefs.flush();
		}
	}

	public boolean isMusicEnabled(boolean defaultEnabled) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_MUSIC_ENABLED)) {
			try {
				return prefs.getBoolean(KEY_MUSIC_ENABLED, defaultEnabled);
			} catch (Exception e) {
				return defaultEnabled;
			}
		}
		return defaultEnabled;
	}

	public void saveMusicEnabled(boolean enabled) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putBoolean(KEY_MUSIC_ENABLED, enabled);
			prefs.flush();
		}
	}

	public float getMusicVolume(float defaultVolume) {
		Preferences prefs = getPreferences();
		if (prefs != null && prefs.contains(KEY_MUSIC_VOLUME)) {
			try {
				return prefs.getFloat(KEY_MUSIC_VOLUME, defaultVolume);
			} catch (Exception e) {
				return defaultVolume;
			}
		}
		return defaultVolume;
	}

	public void saveMusicVolume(float volume) {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.putFloat(KEY_MUSIC_VOLUME, volume);
			prefs.flush();
		}
	}

	public void flush() {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.flush();
		}
	}

	public void clear() {
		Preferences prefs = getPreferences();
		if (prefs != null) {
			prefs.clear();
			prefs.flush();
		}
	}
}
