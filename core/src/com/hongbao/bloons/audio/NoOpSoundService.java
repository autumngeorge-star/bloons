package com.hongbao.bloons.audio;

/**
 * No-op stub implementation of {@link SoundService} for headless unit testing environments.
 */
public class NoOpSoundService implements SoundService {
    @Override
    public void playPopSound() {
        // No-op for headless environment
    }

    @Override
    public void playSound(String soundFilePath, float volume) {
        // No-op for headless environment
    }

    @Override
    public void dispose() {
        // No-op for headless environment
    }
}
