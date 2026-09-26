package com.hongbao.bloons.events;

/**
 * Domain event emitted when the game transitions to a new level or stage.
 */
public class LevelChangedEvent {

    private final int previousLevel;
    private final int newLevel;

    public LevelChangedEvent(int newLevel) {
        this(newLevel - 1, newLevel);
    }

    public LevelChangedEvent(int previousLevel, int newLevel) {
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }
}
