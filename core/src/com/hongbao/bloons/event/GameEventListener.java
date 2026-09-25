package com.hongbao.bloons.event;

@FunctionalInterface
public interface GameEventListener<T> {
	void onEvent(T event);
}
