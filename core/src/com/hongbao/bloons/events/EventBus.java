package com.hongbao.bloons.events;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Application-wide event bus architecture supporting event subscription and publication.
 */
public class EventBus {

    private static final EventBus INSTANCE = new EventBus();

    public static EventBus getInstance() {
        return INSTANCE;
    }

    private final Map<Class<?>, List<EventHandler<?>>> handlersMap = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> eventType, EventHandler<T> handler) {
        if (eventType == null || handler == null) {
            return;
        }
        handlersMap.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    public <T> void unsubscribe(Class<T> eventType, EventHandler<T> handler) {
        if (eventType == null || handler == null) {
            return;
        }
        List<EventHandler<?>> handlers = handlersMap.get(eventType);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        if (event == null) {
            return;
        }
        Class<?> eventClass = event.getClass();
        for (Map.Entry<Class<?>, List<EventHandler<?>>> entry : handlersMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                List<EventHandler<?>> handlers = entry.getValue();
                for (EventHandler<?> handler : handlers) {
                    ((EventHandler<Object>) handler).handle(event);
                }
            }
        }
    }

    public void clear() {
        handlersMap.clear();
    }
}
