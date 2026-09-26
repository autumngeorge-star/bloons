package com.hongbao.bloons.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central event bus supporting strongly-typed domain event subscription and publishing.
 * Dispatching is executed synchronously within the current frame.
 */
public class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    private final Map<Class<?>, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public EventBus() {
    }

    /**
     * Get default singleton instance of EventBus.
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Subscribe a listener for a specific event type.
     */
    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Unsubscribe a listener from a specific event type.
     */
    public <T> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        List<EventListener<?>> list = listeners.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    /**
     * Synchronously publish an event to all subscribed listeners.
     */
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        if (event == null) {
            return;
        }
        List<EventListener<?>> list = listeners.get(event.getClass());
        if (list != null) {
            for (EventListener<?> listener : list) {
                try {
                    ((EventListener<T>) listener).onEvent(event);
                } catch (Exception e) {
                    System.err.println("Error dispatching event " + event.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Clear all registered listeners.
     */
    public void clear() {
        listeners.clear();
    }
}
