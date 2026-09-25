package com.hongbao.bloons.audio;

import org.junit.Assert;
import org.junit.Test;

public class AudioPerformanceAndDeduplicationTest {

    private static class RecordingAudioDriver implements AudioDriver {
        public int playSoundCalls = 0;
        public float lastVolume = 0f;

        @Override
        public void playSound(String soundPath, float volume, float pitch, float pan) {
            playSoundCalls++;
            lastVolume = volume;
        }

        @Override
        public void playMusic(String musicPath, float volume, boolean looping) {}

        @Override
        public void pauseMusic() {}

        @Override
        public void resumeMusic() {}

        @Override
        public void stopMusic() {}

        @Override
        public void toggleMusic() {}

        @Override
        public void dispose() {}
    }

    @Test
    public void testExplosionAttackMassPoppingDeduplicationAndZeroAllocations() {
        AudioCommandPool pool = new AudioCommandPool(200, 512);
        AudioCommandQueue queue = new AudioCommandQueue(512, pool);
        RecordingAudioDriver driver = new RecordingAudioDriver();
        AudioProcessor processor = new AudioProcessor(queue, driver, 1, 0.02f);
        AudioSystem system = new AudioSystem(queue, processor);

        for (int frame = 0; frame < 5; frame++) {
            for (int i = 0; i < 50; i++) {
                system.playSound("music/pop.mp3", 0.5f);
            }
            system.processFrame();
        }

        driver.playSoundCalls = 0;

        long startTime = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            system.playSound("music/pop.mp3", 0.5f);
        }
        long physicsEnqueueDuration = System.nanoTime() - startTime;

        Assert.assertEquals(100, queue.size());

        long frameEndStart = System.nanoTime();
        system.processFrame();
        long frameEndDuration = System.nanoTime() - frameEndStart;

        Assert.assertEquals(0, queue.size());
        Assert.assertEquals(1, driver.playSoundCalls);
        Assert.assertEquals(0.6f, driver.lastVolume, 0.001f);

        Assert.assertTrue("Physics enqueue duration should be < 1ms", physicsEnqueueDuration < 1_000_000);
        Assert.assertTrue("Frame end processing duration should be < 16ms", frameEndDuration < 16_000_000);
    }
}
