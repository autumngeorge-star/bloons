package com.hongbao.bloons.events;

/**
 * Event published when the user requests toggling audio / background music playback.
 */
public class AudioToggleEvent implements GameEvent {

    public static final AudioToggleEvent INSTANCE = new AudioToggleEvent();

    public AudioToggleEvent() {
    }
}
