package com.hongbao.bloons.events;

public class LevelChangedEvent implements GameEvent {
    private final int level;

    public LevelChangedEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
