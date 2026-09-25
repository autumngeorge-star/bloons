package com.hongbao.bloons.events;

/**
 * Interface for listening to specific types of game domain events.
 *
 * @param <T> the type of GameEvent handled by this listener
 */
@FunctionalInterface
public interface GameEventListener<T extends GameEvent> {
    void onEvent(T event);
}
