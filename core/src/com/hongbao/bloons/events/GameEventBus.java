package com.hongbao.bloons.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class GameEventBus {

    private static final GameEventBus INSTANCE = new GameEventBus();

    private final Map<Class<?>, List<Consumer<Object>>> listeners = new ConcurrentHashMap<>();

    public static GameEventBus getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                .add((Consumer<Object>) listener);
    }

    public <T> void unsubscribe(Class<T> eventType, Consumer<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        List<Consumer<Object>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public void publish(Object event) {
        if (event == null) {
            return;
        }
        Class<?> eventType = event.getClass();
        for (Map.Entry<Class<?>, List<Consumer<Object>>> entry : listeners.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventType)) {
                for (Consumer<Object> listener : entry.getValue()) {
                    try {
                        listener.accept(event);
                    } catch (Exception e) {
                        System.err.println("Error handling event " + eventType.getSimpleName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    public void clear() {
        listeners.clear();
    }
}
