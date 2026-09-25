package com.hongbao.bloons.events;

/**
 * Event for lifecycle/control actions on audio (play title, pause, resume, stop).
 */
public enum AudioControlEvent implements GameEvent {
    PLAY_TITLE,
    PAUSE,
    RESUME,
    STOP
}
