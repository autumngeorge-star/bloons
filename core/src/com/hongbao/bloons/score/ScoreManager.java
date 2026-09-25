package com.hongbao.bloons.score;

import com.hongbao.bloons.storage.GameStorageService;
import com.hongbao.bloons.storage.PreferencesStorageServiceImpl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScoreManager implements ScoreListener {

    private int currentScore;
    private int highScore;
    private final GameStorageService storageService;
    private final List<ScoreListener> listeners = new CopyOnWriteArrayList<>();

    public ScoreManager() {
        this(new PreferencesStorageServiceImpl());
    }

    public ScoreManager(GameStorageService storageService) {
        this.storageService = storageService;
        this.currentScore = 0;
        this.highScore = storageService != null ? storageService.loadHighScore() : 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public void addScoreListener(ScoreListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeScoreListener(ScoreListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onScoreEvent(ScoreEvent event) {
        if (event == null) {
            return;
        }

        int points = event.getPoints();
        if (points > 0) {
            currentScore += points;
            boolean isNewHighScore = false;
            if (currentScore > highScore) {
                highScore = currentScore;
                isNewHighScore = true;
                if (storageService != null) {
                    storageService.saveHighScore(highScore);
                }
            }

            ScoreEvent updateEvent = new ScoreEvent(
                    points,
                    isNewHighScore ? ScoreEvent.Type.HIGH_SCORE_UPDATED : ScoreEvent.Type.SCORE_UPDATED,
                    event.getBloon(),
                    currentScore,
                    highScore
            );
            notifyListeners(updateEvent);
        }
    }

    public void resetScore() {
        currentScore = 0;
        notifyListeners(new ScoreEvent(0, ScoreEvent.Type.GAME_RESET, null, currentScore, highScore));
    }

    private void notifyListeners(ScoreEvent event) {
        for (ScoreListener listener : listeners) {
            listener.onScoreEvent(event);
        }
    }
}
