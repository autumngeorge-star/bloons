package com.hongbao.bloons.events;

/**
 * Domain event emitted when the high-level game state changes.
 */
public class GameStateChangedEvent {

    public enum State {
        TITLE,
        PLAYING,
        PAUSED,
        RESUMED,
        GAME_OVER,
        GAME_WON
    }

    private final State previousState;
    private final State newState;

    public GameStateChangedEvent(State newState) {
        this(null, newState);
    }

    public GameStateChangedEvent(State previousState, State newState) {
        this.previousState = previousState;
        this.newState = newState;
    }

    public State getPreviousState() {
        return previousState;
    }

    public State getNewState() {
        return newState;
    }
}
