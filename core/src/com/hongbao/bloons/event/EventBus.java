package com.hongbao.bloons.event;

public interface EventBus {
    <T> void subscribe(Class<T> eventType, EventListener<T> listener);
    <T> void unsubscribe(Class<T> eventType, EventListener<T> listener);
    void publish(Object event);
}
