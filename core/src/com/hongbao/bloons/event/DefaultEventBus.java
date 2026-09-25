package com.hongbao.bloons.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultEventBus implements EventBus {

    private static DefaultEventBus defaultInstance;

    private final Map<Class<?>, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public static synchronized DefaultEventBus getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new DefaultEventBus();
        }
        return defaultInstance;
    }

    public static synchronized void setDefault(DefaultEventBus bus) {
        defaultInstance = bus;
    }

    public static synchronized void resetDefault() {
        defaultInstance = null;
    }

    @Override
    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    @Override
    public <T> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        if (eventType == null || listener == null) {
            return;
        }
        List<EventListener<?>> list = listeners.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void publish(Object event) {
        if (event == null) {
            return;
        }
        Class<?> eventType = event.getClass();
        List<EventListener<?>> list = listeners.get(eventType);
        if (list != null) {
            for (EventListener listener : list) {
                listener.onEvent(event);
            }
        }

        for (Map.Entry<Class<?>, List<EventListener<?>>> entry : listeners.entrySet()) {
            if (entry.getKey() != eventType && entry.getKey().isInstance(event)) {
                for (EventListener listener : entry.getValue()) {
                    listener.onEvent(event);
                }
            }
        }
    }
}
