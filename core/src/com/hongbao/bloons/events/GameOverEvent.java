package com.hongbao.bloons.events;

public class GameOverEvent extends GameplayEvent {
    private final boolean won;
    private final int finalLevel;

    public GameOverEvent(boolean won, int finalLevel) {
        super();
        this.won = won;
        this.finalLevel = finalLevel;
    }

    public boolean isWon() {
        return won;
    }

    public int getFinalLevel() {
        return finalLevel;
    }
}
