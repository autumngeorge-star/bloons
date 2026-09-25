package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.audio.AudioRecorder;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import com.hongbao.bloons.helpers.BloonPoppedResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.junit.Assert.assertEquals;

public class BloonManagerTest {

    private Application originalApp;
    private Files originalFiles;
    private Audio originalAudio;
    private GL20 originalGl;

    @Before
    public void setUp() {
        originalApp = Gdx.app;
        originalFiles = Gdx.files;
        originalAudio = Gdx.audio;
        originalGl = Gdx.gl;

        Gdx.files = new Files() {
            @Override public FileHandle getFileHandle(String path, Files.FileType type) { return internal(path); }
            @Override public FileHandle classpath(String path) { return internal(path); }
            @Override public FileHandle internal(String path) {
                File file = new File("/app/bloons/core/assets/" + path);
                if (!file.exists()) {
                    file = new File(path);
                }
                return new FileHandle(file);
            }
            @Override public FileHandle external(String path) { return internal(path); }
            @Override public FileHandle absolute(String path) { return internal(path); }
            @Override public FileHandle local(String path) { return internal(path); }
            @Override public String getExternalStoragePath() { return ""; }
            @Override public boolean isExternalStorageAvailable() { return false; }
            @Override public String getLocalStoragePath() { return ""; }
            @Override public boolean isLocalStorageAvailable() { return false; }
        };

        Gdx.audio = new Audio() {
            @Override public AudioDevice newAudioDevice(int samplingRate, boolean isMono) { return null; }
            @Override public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) { return null; }
            @Override public Sound newSound(FileHandle fileHandle) {
                return new Sound() {
                    @Override public long play() { return 0; }
                    @Override public long play(float volume) { return 0; }
                    @Override public long play(float volume, float pitch, float pan) { return 0; }
                    @Override public long loop() { return 0; }
                    @Override public long loop(float volume) { return 0; }
                    @Override public long loop(float volume, float pitch, float pan) { return 0; }
                    @Override public void stop() { }
                    @Override public void pause() { }
                    @Override public void resume() { }
                    @Override public void dispose() { }
                    @Override public void stop(long soundId) { }
                    @Override public void pause(long soundId) { }
                    @Override public void resume(long soundId) { }
                    @Override public void setLooping(long soundId, boolean looping) { }
                    @Override public void setPitch(long soundId, float pitch) { }
                    @Override public void setVolume(long soundId, float volume) { }
                    @Override public void setPan(long soundId, float pan, float volume) { }
                };
            }
            @Override public Music newMusic(FileHandle file) { return null; }
        };
    }

    @After
    public void tearDown() {
        Gdx.app = originalApp;
        Gdx.files = originalFiles;
        Gdx.audio = originalAudio;
        Gdx.gl = originalGl;
    }

    @Test
    public void testPopBloonUpdatesPlayerScoreOnDamageAndPop() {
        final Player player = new Player(1000, 100);

        BloonsTouhouDefense mockListener = new BloonsTouhouDefense() {
            @Override
            public Player getPlayer() {
                return player;
            }
        };

        Gdx.app = new Application() {
            @Override public com.badlogic.gdx.ApplicationListener getApplicationListener() { return mockListener; }
            @Override public com.badlogic.gdx.Graphics getGraphics() { return null; }
            @Override public Audio getAudio() { return Gdx.audio; }
            @Override public com.badlogic.gdx.Input getInput() { return null; }
            @Override public Files getFiles() { return Gdx.files; }
            @Override public com.badlogic.gdx.Net getNet() { return null; }
            @Override public void log(String tag, String message) { }
            @Override public void log(String tag, String message, Throwable exception) { }
            @Override public void error(String tag, String message) { }
            @Override public void error(String tag, String message, Throwable exception) { }
            @Override public void debug(String tag, String message) { }
            @Override public void debug(String tag, String message, Throwable exception) { }
            @Override public void setLogLevel(int logLevel) { }
            @Override public int getLogLevel() { return 0; }
            @Override public void setApplicationLogger(com.badlogic.gdx.ApplicationLogger applicationLogger) { }
            @Override public com.badlogic.gdx.ApplicationLogger getApplicationLogger() { return null; }
            @Override public ApplicationType getType() { return ApplicationType.HeadlessDesktop; }
            @Override public com.badlogic.gdx.utils.Clipboard getClipboard() { return null; }
            @Override public int getVersion() { return 0; }
            @Override public long getJavaHeap() { return 0; }
            @Override public long getNativeHeap() { return 0; }
            @Override public void postRunnable(Runnable runnable) { }
            @Override public void exit() { }
            @Override public void addLifecycleListener(com.badlogic.gdx.LifecycleListener listener) { }
            @Override public void removeLifecycleListener(com.badlogic.gdx.LifecycleListener listener) { }
            @Override public Preferences getPreferences(String name) { return null; }
        };

        BloonManager bloonManager = new BloonManager(null, null);

        // Test non-popping damage: Ceramic bloon with health 18 damaged by 5 damage -> willPopBloon(5) is false
        Bloon ceramicBloon = BloonFactory.createCeramicBloon(); // health = 18
        MockBloonActor damagedActor = new MockBloonActor(ceramicBloon);

        assertEquals(0, player.getScore());
        bloonManager.popBloon(damagedActor, 5);
        assertEquals(5, player.getScore());

        // Test popping bloon: Red bloon with health 1 popped by 1 damage -> willPopBloon(1) is true
        Bloon redBloon = BloonFactory.createRedBloon(); // health = 1
        MockBloonActor poppedActor = new MockBloonActor(redBloon);

        bloonManager.popBloon(poppedActor, 1);
        // Red bloon pop result cashGenerated = 1, score increases by 1 to 6
        assertEquals(6, player.getScore());
    }

    private static class MockBloonActor extends BloonActor {
        private Bloon bloon;

        public MockBloonActor(Bloon bloon) {
            super(bloon);
            this.bloon = bloon;
        }

        @Override
        public Bloon getBloon() {
            return bloon;
        }

        @Override
        public BloonPoppedResult pop(int damage) {
            return new BloonPoppedResult(bloon, damage);
        }

        @Override
        public float getCenterX() {
            return 0;
        }

        @Override
        public float getCenterY() {
            return 0;
        }

        @Override
        public void damage(int damage) {
            bloon.damage(damage);
        }
    }

    // Secondary constructor bypass for MockBloonActor testing without Texture creation
    private static abstract class DummyBase {
        protected DummyBase() {}
    }
}
