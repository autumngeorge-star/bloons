package com.hongbao.bloons.event;

public class LevelChangedEvent {
    private final int level;

    public LevelChangedEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
