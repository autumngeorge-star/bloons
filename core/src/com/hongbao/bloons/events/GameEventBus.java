package com.hongbao.bloons.events;

public interface GameEventBus {
    void publish(GameEvent event);
    void subscribe(GameEventListener listener);
    void unsubscribe(GameEventListener listener);
}
