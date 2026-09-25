package com.hongbao.bloons.score;

import com.hongbao.bloons.entities.Bloon;

public class ScoreEvent {

    public enum Type {
        BLOON_POPPED,
        BLOON_DAMAGED,
        LEVEL_COMPLETED,
        GAME_OVER,
        GAME_WON
    }

    private final Type type;
    private final int points;
    private final Bloon bloon;
    private final int cash;

    public ScoreEvent(Type type, int points) {
        this(type, points, null, 0);
    }

    public ScoreEvent(Type type, int points, Bloon bloon, int cash) {
        this.type = type;
        this.points = points;
        this.bloon = bloon;
        this.cash = cash;
    }

    public Type getType() {
        return type;
    }

    public int getPoints() {
        return points;
    }

    public Bloon getBloon() {
        return bloon;
    }

    public int getCash() {
        return cash;
    }
}
