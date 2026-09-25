package com.hongbao.bloons.event;

@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T event);
}
