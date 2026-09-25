package com.hongbao.bloons.events;

/**
 * Domain event published when a bloon or bloon layer is popped.
 * Reusable INSTANCE pattern is used to avoid object allocations during collision frames.
 */
public class BloonPoppedEvent implements GameEvent {

    public static final BloonPoppedEvent INSTANCE = new BloonPoppedEvent();

    public BloonPoppedEvent() {
    }
}
