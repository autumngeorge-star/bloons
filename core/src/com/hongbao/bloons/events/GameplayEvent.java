package com.hongbao.bloons.events;

public abstract class GameplayEvent {
    private final long timestamp;

    public GameplayEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
