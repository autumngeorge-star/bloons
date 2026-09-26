package com.hongbao.bloons.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DefaultGameEventBus implements GameEventBus {
    private final List<GameEventListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void publish(GameEvent event) {
        if (event == null) {
            return;
        }
        for (GameEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    @Override
    public void subscribe(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unsubscribe(GameEventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
