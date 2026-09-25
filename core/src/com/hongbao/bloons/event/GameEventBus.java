package com.hongbao.bloons.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameEventBus {

	private static final GameEventBus INSTANCE = new GameEventBus();

	private final Map<Class<?>, List<GameEventListener<?>>> listenersMap = new ConcurrentHashMap<>();

	public GameEventBus() {
	}

	public static GameEventBus getInstance() {
		return INSTANCE;
	}

	public static GameEventBus getDefault() {
		return INSTANCE;
	}

	public <T> void subscribe(Class<T> eventType, GameEventListener<T> listener) {
		if (eventType == null || listener == null) {
			return;
		}
		listenersMap.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
	}

	public <T> void unsubscribe(Class<T> eventType, GameEventListener<T> listener) {
		if (eventType == null || listener == null) {
			return;
		}
		List<GameEventListener<?>> listeners = listenersMap.get(eventType);
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void publish(T event) {
		if (event == null) {
			return;
		}
		List<GameEventListener<?>> listeners = listenersMap.get(event.getClass());
		if (listeners != null) {
			for (GameEventListener<?> listener : listeners) {
				((GameEventListener<T>) listener).onEvent(event);
			}
		}
	}

	public void clear() {
		listenersMap.clear();
	}
}
