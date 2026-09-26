package com.hongbao.bloons.audio;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AudioServiceTest {

    @Test
    public void testNoOpSoundServiceExecution() {
        SoundService soundService = new NoOpSoundService();
        assertNotNull("NoOpSoundService instance should not be null", soundService);

        // Verify that calls to sound service methods execute cleanly without exceptions in headless mode
        soundService.playPopSound();
        soundService.playSound("music/pop.mp3", 0.5f);
        soundService.dispose();
    }

    @Test
    public void testNoOpMusicServiceExecution() {
        MusicService musicService = new NoOpMusicService();
        assertNotNull("NoOpMusicService instance should not be null", musicService);

        // Verify that calls to music service methods execute cleanly without exceptions in headless mode
        musicService.playTitleMusic();
        musicService.playStageMusic();
        musicService.playFinalBossMusic();
        musicService.pause();
        musicService.resume();
        musicService.stopMusic();
        musicService.toggleMusic();
    }
}
