package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.events.BloonEventListener;
import com.hongbao.bloons.helpers.BloonPoppedResult;

/**
 * ScoreManager listens to Bloon events, calculates score dynamically,
 * and handles high score persistence using LibGDX Preferences.
 */
public class ScoreManager implements BloonEventListener {

    public static final String PREF_NAME = "bloons_preferences";
    public static final String KEY_HIGH_SCORE = "highScore";

    private int score;
    private int highScore;
    private Preferences preferences;

    public ScoreManager() {
        this.score = 0;
        this.highScore = 0;
        if (Gdx.app != null) {
            this.preferences = Gdx.app.getPreferences(PREF_NAME);
            loadHighScore();
        }
    }

    public ScoreManager(Preferences preferences) {
        this.score = 0;
        this.highScore = 0;
        this.preferences = preferences;
        loadHighScore();
    }

    private void loadHighScore() {
        if (preferences != null) {
            highScore = preferences.getInteger(KEY_HIGH_SCORE, 0);
        }
    }

    public void saveHighScore() {
        if (preferences != null) {
            preferences.putInteger(KEY_HIGH_SCORE, highScore);
            preferences.flush();
        }
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void addScore(int points) {
        if (points <= 0) {
            return;
        }
        this.score += points;
        if (this.score > this.highScore) {
            this.highScore = this.score;
            saveHighScore();
        }
    }

    @Override
    public void onBloonDamaged(BloonActor bloonActor, int damage) {
        int points = damage * 10;
        if (bloonActor != null && bloonActor.getBloon() != null && bloonActor.getBloon().isBlimp()) {
            points += 20; // Blimp hit bonus
        }
        addScore(points);
    }

    @Override
    public void onBloonPopped(BloonActor bloonActor, BloonPoppedResult result) {
        int points = 10;
        if (result != null) {
            points = Math.max(10, result.getCashGenerated() * 10);
        }
        if (bloonActor != null && bloonActor.getBloon() != null && bloonActor.getBloon().isBlimp()) {
            points += 100; // Blimp pop bonus
        }
        addScore(points);
    }

    @Override
    public void onLevelCleared(int level) {
        int levelBonus = level * 100;
        addScore(levelBonus);
    }
}
