package com.hongbao.bloons.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameStateChangedEvent {

	public enum GameState {
		TITLE,
		PLAYING,
		PAUSED,
		WON,
		GAME_OVER
	}

	private final GameState previousState;
	private final GameState newState;
	private final Map<String, Object> metadata;

	public GameStateChangedEvent(GameState previousState, GameState newState, Map<String, Object> metadata) {
		this.previousState = previousState;
		this.newState = newState;
		this.metadata = metadata != null ? new HashMap<>(metadata) : Collections.emptyMap();
	}

	public GameStateChangedEvent(GameState previousState, GameState newState) {
		this(previousState, newState, null);
	}

	public GameStateChangedEvent(GameState newState) {
		this(null, newState, null);
	}

	public GameState getPreviousState() {
		return previousState;
	}

	public GameState getNewState() {
		return newState;
	}

	public GameState getState() {
		return newState;
	}

	public Map<String, Object> getMetadata() {
		return metadata;
	}
}
