package com.hongbao.bloons;

public class GameState {

    public static final int CURRENT_SCHEMA_VERSION = 1;

    private int schemaVersion;
    private int highScore;
    private int unlockedLevel;
    private int totalBloonsPopped;
    private int totalMoneyEarned;
    private int gamesPlayed;
    private int levelsCompleted;

    public GameState() {
        this.schemaVersion = CURRENT_SCHEMA_VERSION;
        this.highScore = 0;
        this.unlockedLevel = 1;
        this.totalBloonsPopped = 0;
        this.totalMoneyEarned = 0;
        this.gamesPlayed = 0;
        this.levelsCompleted = 0;
    }

    public GameState(int highScore, int unlockedLevel, int totalBloonsPopped, int totalMoneyEarned, int gamesPlayed, int levelsCompleted) {
        this.schemaVersion = CURRENT_SCHEMA_VERSION;
        this.highScore = highScore;
        this.unlockedLevel = Math.max(1, unlockedLevel);
        this.totalBloonsPopped = totalBloonsPopped;
        this.totalMoneyEarned = totalMoneyEarned;
        this.gamesPlayed = gamesPlayed;
        this.levelsCompleted = levelsCompleted;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void updateHighScore(int score) {
        if (score > this.highScore) {
            this.highScore = score;
        }
    }

    public int getUnlockedLevel() {
        return unlockedLevel;
    }

    public void setUnlockedLevel(int unlockedLevel) {
        if (unlockedLevel > this.unlockedLevel) {
            this.unlockedLevel = unlockedLevel;
        }
    }

    public int getTotalBloonsPopped() {
        return totalBloonsPopped;
    }

    public void setTotalBloonsPopped(int totalBloonsPopped) {
        this.totalBloonsPopped = totalBloonsPopped;
    }

    public void addBloonsPopped(int amount) {
        if (amount > 0) {
            this.totalBloonsPopped += amount;
            updateHighScore(this.totalBloonsPopped);
        }
    }

    public int getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public void setTotalMoneyEarned(int totalMoneyEarned) {
        this.totalMoneyEarned = totalMoneyEarned;
    }

    public void addMoneyEarned(int amount) {
        if (amount > 0) {
            this.totalMoneyEarned += amount;
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public int getLevelsCompleted() {
        return levelsCompleted;
    }

    public void setLevelsCompleted(int levelsCompleted) {
        this.levelsCompleted = levelsCompleted;
    }

    public void incrementLevelsCompleted() {
        this.levelsCompleted++;
    }

    public boolean isValid() {
        return schemaVersion > 0 && unlockedLevel >= 1 && highScore >= 0
                && totalBloonsPopped >= 0 && totalMoneyEarned >= 0
                && gamesPlayed >= 0 && levelsCompleted >= 0;
    }
}
