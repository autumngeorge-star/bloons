package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AudioDisposalTest {

    private DummyMusic currentMusic;
    private DummySound currentSound;

    static class DummyMusic implements Music {
        boolean playing = true;
        boolean stopped = false;
        boolean disposed = false;

        @Override public void play() { playing = true; }
        @Override public void pause() { playing = false; }
        @Override public void stop() { playing = false; stopped = true; }
        @Override public boolean isPlaying() { return playing; }
        @Override public void setLooping(boolean isLooping) {}
        @Override public boolean isLooping() { return false; }
        @Override public void setVolume(float volume) {}
        @Override public float getVolume() { return 0.5f; }
        @Override public void setPan(float pan, float volume) {}
        @Override public void setPosition(float position) {}
        @Override public float getPosition() { return 0; }
        @Override public void dispose() { disposed = true; }
        @Override public void setOnCompletionListener(OnCompletionListener listener) {}
    }

    static class DummySound implements Sound {
        boolean disposed = false;

        @Override public long play() { return 0; }
        @Override public long play(float volume) { return 0; }
        @Override public long play(float volume, float pitch, float pan) { return 0; }
        @Override public long loop() { return 0; }
        @Override public long loop(float volume) { return 0; }
        @Override public long loop(float volume, float pitch, float pan) { return 0; }
        @Override public void stop() {}
        @Override public void pause() {}
        @Override public void resume() {}
        @Override public void dispose() { disposed = true; }
        @Override public void stop(long soundId) {}
        @Override public void pause(long soundId) {}
        @Override public void resume(long soundId) {}
        @Override public void setLooping(long soundId, boolean looping) {}
        @Override public void setPitch(long soundId, float pitch) {}
        @Override public void setVolume(long soundId, float volume) {}
        @Override public void setPan(long soundId, float pan, float volume) {}
    }

    @Before
    public void setUp() {
        Gdx.files = new Files() {
            @Override public FileHandle getFileHandle(String path, Files.FileType type) { return null; }
            @Override public FileHandle classpath(String path) { return null; }
            @Override public FileHandle internal(String path) { return null; }
            @Override public FileHandle external(String path) { return null; }
            @Override public FileHandle absolute(String path) { return null; }
            @Override public FileHandle local(String path) { return null; }
            @Override public String getExternalStoragePath() { return null; }
            @Override public boolean isExternalStorageAvailable() { return false; }
            @Override public String getLocalStoragePath() { return null; }
            @Override public boolean isLocalStorageAvailable() { return false; }
        };

        Gdx.audio = new Audio() {
            @Override public AudioDevice newAudioDevice(int samplingRate, boolean isMono) { return null; }
            @Override public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) { return null; }
            @Override public Sound newSound(FileHandle fileHandle) {
                currentSound = new DummySound();
                return currentSound;
            }
            @Override public Music newMusic(FileHandle fileHandle) {
                currentMusic = new DummyMusic();
                return currentMusic;
            }
        };
    }

    @Test
    public void testMusicPlayerImplementsDisposable() {
        assertTrue(Disposable.class.isAssignableFrom(MusicPlayer.class));
    }

    @Test
    public void testBloonManagerImplementsDisposable() {
        assertTrue(Disposable.class.isAssignableFrom(BloonManager.class));
    }

    @Test
    public void testMusicPlayerDisposesReplacingTrack() {
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playTitleMusic();
        DummyMusic firstTrack = currentMusic;
        assertNotNull(firstTrack);
        assertFalse(firstTrack.disposed);

        // Switch track
        musicPlayer.playStageMusic();
        assertTrue("Previous track must be stopped before disposal", firstTrack.stopped);
        assertTrue("Previous track must be disposed when replacing track", firstTrack.disposed);

        DummyMusic secondTrack = currentMusic;
        assertNotNull(secondTrack);
        assertNotSame(firstTrack, secondTrack);
        assertFalse(secondTrack.disposed);

        // Switch to final boss music
        musicPlayer.playFinalBossMusic();
        assertTrue("Second track must be disposed when replacing track", secondTrack.disposed);

        DummyMusic thirdTrack = currentMusic;
        assertNotNull(thirdTrack);

        // Dispose player
        musicPlayer.dispose();
        assertTrue("Active track must be disposed when MusicPlayer is disposed", thirdTrack.disposed);
    }
}
