package com.hongbao.bloons.events;

/**
 * Generic event listener interface for receiving domain events.
 *
 * @param <T> The event payload type.
 */
@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T event);
}
