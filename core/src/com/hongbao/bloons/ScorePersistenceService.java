package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ScorePersistenceService {

	public static final String PREFERENCE_NAME = "bloons_scores";
	public static final String HIGH_SCORE_KEY = "high_score";
	public static final String HIGH_SCORE_ALT_KEY = "highScore";

	private int fallbackHighScore = 0;

	public int getHighScore() {
		try {
			if (Gdx.app != null) {
				Preferences prefs = Gdx.app.getPreferences(PREFERENCE_NAME);
				if (prefs != null) {
					if (prefs.contains(HIGH_SCORE_KEY)) {
						return prefs.getInteger(HIGH_SCORE_KEY, 0);
					} else if (prefs.contains(HIGH_SCORE_ALT_KEY)) {
						return prefs.getInteger(HIGH_SCORE_ALT_KEY, 0);
					}
				}
			}
		} catch (Throwable t) {
			// Fail gracefully if preference storage fails
		}
		return fallbackHighScore;
	}

	public void saveHighScore(int highScore) {
		this.fallbackHighScore = highScore;
		try {
			if (Gdx.app != null) {
				Preferences prefs = Gdx.app.getPreferences(PREFERENCE_NAME);
				if (prefs != null) {
					prefs.putInteger(HIGH_SCORE_KEY, highScore);
					prefs.putInteger(HIGH_SCORE_ALT_KEY, highScore);
					prefs.flush();
				}
			}
		} catch (Throwable t) {
			// Fail gracefully if preference storage fails
		}
	}

	public void clear() {
		this.fallbackHighScore = 0;
		try {
			if (Gdx.app != null) {
				Preferences prefs = Gdx.app.getPreferences(PREFERENCE_NAME);
				if (prefs != null) {
					prefs.clear();
					prefs.flush();
				}
			}
		} catch (Throwable t) {
			// Fail gracefully
		}
	}
}
