package com.hongbao.bloons.entities;

/**
 * Interface for listening to state change events on a Bloon.
 */
public interface BloonStateChangeListener {
    /**
     * Called when a Bloon's health, color, speed, or sprite image changes.
     * 
     * @param bloon the Bloon instance whose state changed
     */
    void onBloonStateChanged(Bloon bloon);

    /**
     * Default alias for onBloonStateChanged.
     * 
     * @param bloon the Bloon instance whose state changed
     */
    default void onStateChanged(Bloon bloon) {
        onBloonStateChanged(bloon);
    }
}
