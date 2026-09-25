package com.hongbao.bloons.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central publish-subscribe event dispatcher for game domain events.
 */
public class GameEventManager {

    private static final GameEventManager instance = new GameEventManager();

    private final Map<Class<? extends GameEvent>, List<GameEventListener<?>>> listenerMap = new ConcurrentHashMap<>();

    public static GameEventManager getInstance() {
        return instance;
    }

    /**
     * Subscribe a listener to a specific event type.
     */
    public <T extends GameEvent> void subscribe(Class<T> eventClass, GameEventListener<T> listener) {
        if (eventClass == null || listener == null) return;
        listenerMap.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Unsubscribe a listener from a specific event type.
     */
    public <T extends GameEvent> void unsubscribe(Class<T> eventClass, GameEventListener<T> listener) {
        if (eventClass == null || listener == null) return;
        List<GameEventListener<?>> listeners = listenerMap.get(eventClass);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Publish a domain event to all subscribers registered for its class type.
     */
    @SuppressWarnings("unchecked")
    public <T extends GameEvent> void publish(T event) {
        if (event == null) return;
        List<GameEventListener<?>> listeners = listenerMap.get(event.getClass());
        if (listeners != null) {
            for (GameEventListener<?> listener : listeners) {
                ((GameEventListener<T>) listener).onEvent(event);
            }
        }
    }

    /**
     * Clear all registered listeners (useful for test resets).
     */
    public void clearListeners() {
        listenerMap.clear();
    }
}
