package com.hongbao.bloons;

/**
 * Listener interface for receiving notifications when game levels change.
 */
public interface LevelEventListener {

    /**
     * Called when the game advances to a new level.
     *
     * @param level the current level number
     */
    void onLevelChanged(int level);
}
