package com.hongbao.bloons.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central publish-subscribe game event bus.
 * Synchronously dispatches events to subscribed listeners.
 */
public class GameEventBus {

    private static GameEventBus instance = new GameEventBus();

    public static GameEventBus getInstance() {
        return instance;
    }

    public static void setInstance(GameEventBus newInstance) {
        instance = (newInstance != null) ? newInstance : new GameEventBus();
    }

    private final Map<Class<? extends GameEvent>, List<GameEventListener<?>>> listeners = new HashMap<>();

    public GameEventBus() {
    }

    /**
     * Subscribe a listener to a specific event class.
     */
    public <T extends GameEvent> void subscribe(Class<T> eventClass, GameEventListener<T> listener) {
        if (eventClass == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Unsubscribe a listener from a specific event class.
     */
    public <T extends GameEvent> void unsubscribe(Class<T> eventClass, GameEventListener<T> listener) {
        if (eventClass == null || listener == null) {
            return;
        }
        List<GameEventListener<?>> eventListeners = listeners.get(eventClass);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    /**
     * Publish an event synchronously to all registered listeners.
     */
    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void publish(T event) {
        if (event == null) {
            return;
        }
        Class<?> eventClass = event.getClass();
        List<GameEventListener<?>> eventListeners = listeners.get(eventClass);
        if (eventListeners != null && !eventListeners.isEmpty()) {
            for (GameEventListener<?> listener : eventListeners) {
                ((GameEventListener<T>) listener).onEvent(event);
            }
        }
    }

    /**
     * Clears all subscriptions on this event bus.
     * Useful for resetting test state between unit test runs.
     */
    public void resetSubscriptions() {
        listeners.clear();
    }

    /**
     * Convenience method to reset subscriptions.
     */
    public void reset() {
        resetSubscriptions();
    }

    /**
     * Check if there are any active listeners for the given event class.
     */
    public <T extends GameEvent> boolean hasSubscribers(Class<T> eventClass) {
        List<GameEventListener<?>> eventListeners = listeners.get(eventClass);
        return eventListeners != null && !eventListeners.isEmpty();
    }
}
