package com.hongbao.bloons.events;

/**
 * Functional interface for listening to game domain events.
 *
 * @param <T> the type of GameEvent to handle
 */
@FunctionalInterface
public interface GameEventListener<T extends GameEvent> {
    void onEvent(T event);
}
