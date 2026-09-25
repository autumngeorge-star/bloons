package com.hongbao.bloons;

import org.junit.Assert;
import org.junit.Test;

public class BloonManagerAudioTest {

    private static class TestAudioController implements AudioController {
        boolean playTitleMusicCalled = false;
        boolean playStageMusicCalled = false;
        boolean playFinalBossMusicCalled = false;
        boolean pauseCalled = false;
        boolean resumeCalled = false;
        boolean stopMusicCalled = false;
        boolean toggleMusicCalled = false;

        @Override
        public void playTitleMusic() {
            playTitleMusicCalled = true;
        }

        @Override
        public void playStageMusic() {
            playStageMusicCalled = true;
        }

        @Override
        public void playFinalBossMusic() {
            playFinalBossMusicCalled = true;
        }

        @Override
        public void pause() {
            pauseCalled = true;
        }

        @Override
        public void resume() {
            resumeCalled = true;
        }

        @Override
        public void stopMusic() {
            stopMusicCalled = true;
        }

        @Override
        public void toggleMusic() {
            toggleMusicCalled = true;
        }
    }

    @Test
    public void testMusicPlayerImplementsAudioController() {
        Assert.assertTrue("MusicPlayer must implement AudioController interface",
                AudioController.class.isAssignableFrom(MusicPlayer.class));
    }

    @Test
    public void testBloonManagerAcceptsAudioControllerAndTriggersStageMusic() {
        TestAudioController audioController = new TestAudioController();
        BloonManager bloonManager = new BloonManager(null, null, audioController);

        Assert.assertEquals(0, bloonManager.getLevel());
        Assert.assertFalse(audioController.playStageMusicCalled);

        // Advance to level 1
        bloonManager.nextLevel();

        Assert.assertEquals(1, bloonManager.getLevel());
        Assert.assertTrue("playStageMusic should be called when transitioning to level 1",
                audioController.playStageMusicCalled);
        Assert.assertFalse("playFinalBossMusic should not be called at level 1",
                audioController.playFinalBossMusicCalled);
    }

    @Test
    public void testBloonManagerTriggersFinalBossMusicOnLevel40() {
        TestAudioController audioController = new TestAudioController();
        BloonManager bloonManager = new BloonManager(null, null, audioController);

        // Advance to level 39
        for (int i = 0; i < 39; i++) {
            bloonManager.nextLevel();
        }
        Assert.assertEquals(39, bloonManager.getLevel());

        audioController.playStageMusicCalled = false;
        audioController.playFinalBossMusicCalled = false;

        // Advance from level 39 to level 40
        bloonManager.nextLevel();

        Assert.assertEquals(40, bloonManager.getLevel());
        Assert.assertTrue("playFinalBossMusic should be called when transitioning to level 40",
                audioController.playFinalBossMusicCalled);
    }
}
