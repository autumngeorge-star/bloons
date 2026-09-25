package com.hongbao.bloons.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager implements ScoreEventListener {

    private final ScoreRepository repository;
    private List<ScoreEntry> scoreHistory;
    private int currentScore;
    private int highScore;
    private boolean sessionSaved;

    public ScoreManager() {
        this(new ScoreRepository());
    }

    public ScoreManager(ScoreRepository repository) {
        this.repository = repository;
        this.scoreHistory = new ArrayList<>();
        this.currentScore = 0;
        this.highScore = 0;
        this.sessionSaved = false;
        loadData();
    }

    public void loadData() {
        this.scoreHistory = repository.loadScores();
        updateHighScore();
    }

    private void updateHighScore() {
        if (scoreHistory != null && !scoreHistory.isEmpty()) {
            this.highScore = scoreHistory.get(0).getScore();
        } else {
            this.highScore = 0;
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    public List<ScoreEntry> getScoreHistory() {
        return new ArrayList<>(scoreHistory);
    }

    public void resetSession() {
        this.currentScore = 0;
        this.sessionSaved = false;
    }

    public synchronized void saveSessionScore(boolean victory, int level) {
        if (sessionSaved) {
            return;
        }
        sessionSaved = true;

        ScoreEntry entry = new ScoreEntry("Player", currentScore, level, System.currentTimeMillis(), victory);
        scoreHistory.add(entry);
        repository.saveScores(scoreHistory);
        updateHighScore();
    }

    @Override
    public void onScoreEvent(ScoreEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getType()) {
            case BLOON_POPPED:
            case BLOON_DAMAGED:
            case LEVEL_COMPLETED:
                this.currentScore += event.getPoints();
                if (this.currentScore > this.highScore) {
                    this.highScore = this.currentScore;
                }
                break;
            case GAME_OVER:
                saveSessionScore(false, event.getPoints());
                break;
            case GAME_WON:
                saveSessionScore(true, event.getPoints());
                break;
        }
    }
}
