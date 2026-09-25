package com.hongbao.bloons.score;

import com.hongbao.bloons.entities.Bloon;

public class ScoreEvent {

    public enum Type {
        BLOON_DAMAGE,
        BLOON_POPPED,
        SCORE_UPDATED,
        HIGH_SCORE_UPDATED,
        GAME_RESET
    }

    private final int points;
    private final Type type;
    private final Bloon bloon;
    private final int currentScore;
    private final int highScore;

    public ScoreEvent(int points) {
        this(points, Type.SCORE_UPDATED, null, 0, 0);
    }

    public ScoreEvent(int points, Type type) {
        this(points, type, null, 0, 0);
    }

    public ScoreEvent(int points, Type type, Bloon bloon) {
        this(points, type, bloon, 0, 0);
    }

    public ScoreEvent(int points, Type type, Bloon bloon, int currentScore, int highScore) {
        this.points = points;
        this.type = type;
        this.bloon = bloon;
        this.currentScore = currentScore;
        this.highScore = highScore;
    }

    public int getPoints() {
        return points;
    }

    public Type getType() {
        return type;
    }

    public Bloon getBloon() {
        return bloon;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }

    @Override
    public String toString() {
        return "ScoreEvent{" +
                "points=" + points +
                ", type=" + type +
                ", currentScore=" + currentScore +
                ", highScore=" + highScore +
                '}';
    }
}
