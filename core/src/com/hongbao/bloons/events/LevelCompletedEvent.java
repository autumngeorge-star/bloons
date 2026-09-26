package com.hongbao.bloons.events;

public class LevelCompletedEvent extends GameplayEvent {
    private final int level;

    public LevelCompletedEvent(int level) {
        super();
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
