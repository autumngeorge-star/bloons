package com.hongbao.bloons.score;

public class ScoreEntry {

    private String playerName;
    private int score;
    private int level;
    private long timestamp;
    private boolean victory;

    public ScoreEntry() {
        this.playerName = "Player";
        this.score = 0;
        this.level = 1;
        this.timestamp = System.currentTimeMillis();
        this.victory = false;
    }

    public ScoreEntry(String playerName, int score, int level, long timestamp, boolean victory) {
        this.playerName = playerName;
        this.score = score;
        this.level = level;
        this.timestamp = timestamp;
        this.victory = victory;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isVictory() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    @Override
    public String toString() {
        return "ScoreEntry{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                ", level=" + level +
                ", timestamp=" + timestamp +
                ", victory=" + victory +
                '}';
    }
}
