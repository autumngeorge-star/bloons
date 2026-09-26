package com.hongbao.bloons.audio;

/**
 * Service interface for sound effect playback.
 */
public interface SoundService {
    /**
     * Plays the default bloon pop sound effect.
     */
    void playPopSound();

    /**
     * Plays a sound effect from the specified file path at the given volume.
     *
     * @param soundFilePath relative file path to the sound asset
     * @param volume volume level between 0.0 and 1.0
     */
    void playSound(String soundFilePath, float volume);

    /**
     * Disposes of underlying sound resources.
     */
    void dispose();
}
