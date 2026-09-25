package com.hongbao.bloons.events;

/**
 * Domain event published when the game level advances or changes.
 */
public class LevelChangedEvent implements GameEvent {

    private final int level;

    public LevelChangedEvent(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
