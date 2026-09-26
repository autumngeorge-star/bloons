package com.hongbao.bloons.audio;

/**
 * Service interface for background music track management.
 */
public interface MusicService {
    /**
     * Plays the title screen background music.
     */
    void playTitleMusic();

    /**
     * Plays the main stage background music.
     */
    void playStageMusic();

    /**
     * Plays the final boss background music.
     */
    void playFinalBossMusic();

    /**
     * Pauses the currently playing background music.
     */
    void pause();

    /**
     * Resumes the paused background music.
     */
    void resume();

    /**
     * Stops the background music playback.
     */
    void stopMusic();

    /**
     * Toggles between playing and pausing background music.
     */
    void toggleMusic();
}
