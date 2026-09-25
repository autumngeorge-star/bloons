package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class LevelProgressionManager {

	public static final String PREFERENCES_NAME = "bloons_level_progression";
	public static final String KEY_MAX_UNLOCKED_LEVEL = "maxUnlockedLevel";

	private final Preferences preferences;
	private final int totalLevels;
	private int maxUnlockedLevel;
	private int currentLevel;

	public LevelProgressionManager(int totalLevels) {
		this(totalLevels, (Gdx.app != null) ? Gdx.app.getPreferences(PREFERENCES_NAME) : null);
	}

	public LevelProgressionManager(int totalLevels, Preferences preferences) {
		this.totalLevels = Math.max(1, totalLevels);
		this.preferences = preferences;
		this.maxUnlockedLevel = loadMaxUnlockedLevel();
		this.currentLevel = 0;
	}

	private int loadMaxUnlockedLevel() {
		if (preferences == null) {
			return 0;
		}
		try {
			int saved = preferences.getInteger(KEY_MAX_UNLOCKED_LEVEL, 0);
			if (saved < 0 || saved >= totalLevels) {
				return 0;
			}
			return saved;
		} catch (Exception e) {
			return 0;
		}
	}

	public int getMaxUnlockedLevel() {
		return maxUnlockedLevel;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public int getTotalLevels() {
		return totalLevels;
	}

	public boolean selectLevel(int level) {
		if (level >= 0 && level <= maxUnlockedLevel && level < totalLevels) {
			this.currentLevel = level;
			return true;
		}
		return false;
	}

	public boolean incrementLevel() {
		if (currentLevel < maxUnlockedLevel && currentLevel + 1 < totalLevels) {
			currentLevel++;
			return true;
		}
		return false;
	}

	public boolean decrementLevel() {
		if (currentLevel > 0) {
			currentLevel--;
			return true;
		}
		return false;
	}

	public void markStageCleared(int clearedLevel) {
		int nextLevel = clearedLevel + 1;
		if (nextLevel < totalLevels && nextLevel > maxUnlockedLevel) {
			maxUnlockedLevel = nextLevel;
			saveMaxUnlockedLevel();
		}
	}

	private void saveMaxUnlockedLevel() {
		if (preferences != null) {
			preferences.putInteger(KEY_MAX_UNLOCKED_LEVEL, maxUnlockedLevel);
			preferences.flush();
		}
	}
}
