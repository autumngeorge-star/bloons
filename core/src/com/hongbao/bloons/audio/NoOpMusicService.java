package com.hongbao.bloons.audio;

/**
 * No-op stub implementation of {@link MusicService} for headless unit testing environments.
 */
public class NoOpMusicService implements MusicService {
    @Override
    public void playTitleMusic() {
        // No-op for headless environment
    }

    @Override
    public void playStageMusic() {
        // No-op for headless environment
    }

    @Override
    public void playFinalBossMusic() {
        // No-op for headless environment
    }

    @Override
    public void pause() {
        // No-op for headless environment
    }

    @Override
    public void resume() {
        // No-op for headless environment
    }

    @Override
    public void stopMusic() {
        // No-op for headless environment
    }

    @Override
    public void toggleMusic() {
        // No-op for headless environment
    }
}
