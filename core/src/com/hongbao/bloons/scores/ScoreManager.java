package com.hongbao.bloons.scores;

import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameOverEvent;
import com.hongbao.bloons.events.GameplayEvent;
import com.hongbao.bloons.events.GameplayEventListener;
import com.hongbao.bloons.events.LevelCompletedEvent;

import java.util.ArrayList;
import java.util.List;

public class ScoreManager implements GameplayEventListener {
    public static final long COMBO_WINDOW_MS = 2000L; // 2 seconds combo window

    private final ScorePersistence persistence;
    private final LeaderboardData leaderboardData;
    private final List<ScoreChangeListener> listeners;

    private int currentScore;
    private int currentLevel;
    private int comboCount;
    private long lastPopTimestamp;
    private boolean sessionArchived;

    public ScoreManager() {
        this(new ScorePersistence());
    }

    public ScoreManager(ScorePersistence persistence) {
        this.persistence = persistence;
        this.leaderboardData = persistence.load();
        this.listeners = new ArrayList<>();
        resetSession();
    }

    public void resetSession() {
        this.currentScore = 0;
        this.currentLevel = 0;
        this.comboCount = 0;
        this.lastPopTimestamp = 0L;
        this.sessionArchived = false;
        notifyListeners();
    }

    public void addListener(ScoreChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            listener.onScoreChanged(currentScore, comboCount, getRank());
        }
    }

    public void removeListener(ScoreChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        int rank = getRank();
        for (ScoreChangeListener listener : listeners) {
            listener.onScoreChanged(currentScore, comboCount, rank);
        }
    }

    @Override
    public void onGameplayEvent(GameplayEvent event) {
        if (event instanceof BloonPoppedEvent) {
            handleBloonPopped((BloonPoppedEvent) event);
        } else if (event instanceof LevelCompletedEvent) {
            handleLevelCompleted((LevelCompletedEvent) event);
        } else if (event instanceof GameOverEvent) {
            handleGameOver((GameOverEvent) event);
        }
    }

    private void handleBloonPopped(BloonPoppedEvent event) {
        long now = event.getTimestamp();
        if (lastPopTimestamp > 0 && (now - lastPopTimestamp) <= COMBO_WINDOW_MS) {
            comboCount++;
        } else {
            comboCount = 1;
        }
        lastPopTimestamp = now;

        int baseValue = event.getCashGenerated() > 0 ? event.getCashGenerated() : event.getDamage();
        if (baseValue <= 0) {
            baseValue = 1;
        }
        int basePoints = baseValue * 10;
        int comboBonus = (comboCount - 1) * 5;
        int totalPoints = basePoints + comboBonus;

        currentScore += totalPoints;
        notifyListeners();
    }

    private void handleLevelCompleted(LevelCompletedEvent event) {
        this.currentLevel = Math.max(this.currentLevel, event.getLevel());
        int levelBonus = event.getLevel() * 100;
        currentScore += levelBonus;
        notifyListeners();
    }

    private void handleGameOver(GameOverEvent event) {
        if (event.getFinalLevel() > 0) {
            this.currentLevel = Math.max(this.currentLevel, event.getFinalLevel());
        }
        archiveSession();
    }

    public synchronized void archiveSession() {
        if (sessionArchived) {
            return;
        }
        sessionArchived = true;
        if (currentScore > 0) {
            LeaderboardEntry entry = new LeaderboardEntry(currentScore, currentLevel, System.currentTimeMillis());
            leaderboardData.addEntry(entry);
            persistence.save(leaderboardData);
            notifyListeners();
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getComboCount() {
        return comboCount;
    }

    public int getRank() {
        return leaderboardData.getRankForScore(currentScore);
    }

    public LeaderboardData getLeaderboardData() {
        return leaderboardData;
    }

    public List<LeaderboardEntry> getTopEntries(int limit) {
        return leaderboardData.getTopEntries(limit);
    }
}
