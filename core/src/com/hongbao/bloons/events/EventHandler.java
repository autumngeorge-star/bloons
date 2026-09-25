package com.hongbao.bloons.events;

/**
 * Functional interface for handling published events on an {@link EventBus}.
 *
 * @param <T> Event type
 */
@FunctionalInterface
public interface EventHandler<T> {
    void handle(T event);
}
