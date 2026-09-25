package com.hongbao.bloons.audio;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AudioProcessorTest {

    private static class MockAudioDriver implements AudioDriver {

        public static class SoundCall {
            public String soundPath;
            public float volume;
            public float pitch;
            public float pan;

            public SoundCall(String soundPath, float volume, float pitch, float pan) {
                this.soundPath = soundPath;
                this.volume = volume;
                this.pitch = pitch;
                this.pan = pan;
            }
        }

        public static class MusicCall {
            public String musicPath;
            public float volume;
            public boolean looping;
            public String action;

            public MusicCall(String action, String musicPath, float volume, boolean looping) {
                this.action = action;
                this.musicPath = musicPath;
                this.volume = volume;
                this.looping = looping;
            }
        }

        public final List<SoundCall> soundCalls = new ArrayList<>();
        public final List<MusicCall> musicCalls = new ArrayList<>();

        @Override
        public void playSound(String soundPath, float volume, float pitch, float pan) {
            soundCalls.add(new SoundCall(soundPath, volume, pitch, pan));
        }

        @Override
        public void playMusic(String musicPath, float volume, boolean looping) {
            musicCalls.add(new MusicCall("PLAY", musicPath, volume, looping));
        }

        @Override
        public void pauseMusic() {
            musicCalls.add(new MusicCall("PAUSE", null, 0, false));
        }

        @Override
        public void resumeMusic() {
            musicCalls.add(new MusicCall("RESUME", null, 0, false));
        }

        @Override
        public void stopMusic() {
            musicCalls.add(new MusicCall("STOP", null, 0, false));
        }

        @Override
        public void toggleMusic() {
            musicCalls.add(new MusicCall("TOGGLE", null, 0, false));
        }

        @Override
        public void dispose() {
        }
    }

    @Test
    public void testMassBloonPopSoundDeduplication() {
        AudioCommandQueue queue = new AudioCommandQueue(100, new AudioCommandPool());
        MockAudioDriver mockDriver = new MockAudioDriver();
        AudioProcessor processor = new AudioProcessor(queue, mockDriver, 1, 0.02f);

        for (int i = 0; i < 30; i++) {
            queue.enqueueSound("music/pop.mp3", 0.5f);
        }

        Assert.assertEquals(30, queue.size());

        processor.processFrame();

        Assert.assertEquals(0, queue.size());
        Assert.assertEquals(1, mockDriver.soundCalls.size());

        MockAudioDriver.SoundCall call = mockDriver.soundCalls.get(0);
        Assert.assertEquals("music/pop.mp3", call.soundPath);
        Assert.assertEquals(0.6f, call.volume, 0.001f);
    }

    @Test
    public void testMusicCommandsProcessing() {
        AudioCommandQueue queue = new AudioCommandQueue(10, new AudioCommandPool());
        MockAudioDriver mockDriver = new MockAudioDriver();
        AudioProcessor processor = new AudioProcessor(queue, mockDriver);

        queue.enqueueMusicPlay("music/stage1.mp3", 0.5f, true);
        queue.enqueueMusicControl(AudioCommandType.MUSIC_TOGGLE);

        processor.processFrame();

        Assert.assertEquals(2, mockDriver.musicCalls.size());
        Assert.assertEquals("PLAY", mockDriver.musicCalls.get(0).action);
        Assert.assertEquals("music/stage1.mp3", mockDriver.musicCalls.get(0).musicPath);
        Assert.assertEquals("TOGGLE", mockDriver.musicCalls.get(1).action);
    }
}
