package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hongbao.bloons.factories.MapFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioCachingDisposalTest {

    private static final Map<String, DummyMusic> createdMusicMap = new HashMap<>();
    private static final AtomicInteger newMusicCount = new AtomicInteger(0);
    private static final AtomicInteger newSoundCount = new AtomicInteger(0);
    private static DummySound createdSound;

    static class DummyMusic implements Music {
        boolean playing = false;
        boolean disposed = false;
        boolean stopped = false;
        boolean paused = false;
        float volume = 0f;
        boolean looping = false;
        final String path;

        DummyMusic(String path) {
            this.path = path;
        }

        @Override public void play() { playing = true; stopped = false; }
        @Override public void pause() { playing = false; paused = true; }
        @Override public void stop() { playing = false; stopped = true; }
        @Override public boolean isPlaying() { return playing; }
        @Override public void setLooping(boolean isLooping) { this.looping = isLooping; }
        @Override public boolean isLooping() { return looping; }
        @Override public void setVolume(float volume) { this.volume = volume; }
        @Override public float getVolume() { return volume; }
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

    public static void setUp() {
        createdMusicMap.clear();
        newMusicCount.set(0);
        newSoundCount.set(0);

        Gdx.files = new Files() {
            @Override public FileHandle getFileHandle(String path, Files.FileType type) {
                File f = new File(path);
                if (!f.exists()) {
                    File f1 = new File("core/assets/" + path);
                    if (f1.exists()) {
                        f = f1;
                    } else {
                        File f2 = new File("assets/" + path);
                        if (f2.exists()) {
                            f = f2;
                        }
                    }
                }
                return new FileHandle(f);
            }
            @Override public FileHandle classpath(String path) { return getFileHandle(path, null); }
            @Override public FileHandle internal(String path) { return getFileHandle(path, null); }
            @Override public FileHandle external(String path) { return getFileHandle(path, null); }
            @Override public FileHandle absolute(String path) { return getFileHandle(path, null); }
            @Override public FileHandle local(String path) { return getFileHandle(path, null); }
            @Override public String getExternalStoragePath() { return null; }
            @Override public boolean isExternalStorageAvailable() { return false; }
            @Override public String getLocalStoragePath() { return null; }
            @Override public boolean isLocalStorageAvailable() { return false; }
        };

        Gdx.audio = new Audio() {
            @Override public com.badlogic.gdx.audio.AudioDevice newAudioDevice(int samplingRate, boolean isMono) { return null; }
            @Override public com.badlogic.gdx.audio.AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) { return null; }
            @Override public Sound newSound(FileHandle fileHandle) {
                newSoundCount.incrementAndGet();
                createdSound = new DummySound();
                return createdSound;
            }
            @Override public Music newMusic(FileHandle fileHandle) {
                newMusicCount.incrementAndGet();
                String p = fileHandle.path().replace("core/assets/", "").replace("assets/", "");
                DummyMusic m = new DummyMusic(p);
                createdMusicMap.put(p, m);
                return m;
            }
        };
    }

    private static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected " + expected + " but got " + actual);
        }
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true condition");
        }
    }

    private static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false condition");
        }
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected non-null object");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Running AudioCachingDisposalTest...");

        testMusicPlayerCachingAndDisposal();
        testBloonManagerDisposal();
        testBloonsTouhouDefenseDisposal();

        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
    }

    public static void testMusicPlayerCachingAndDisposal() throws Exception {
        setUp();
        MusicPlayer musicPlayer = new MusicPlayer();

        // Initial play of title music
        musicPlayer.playTitleMusic();
        assertEquals(1, newMusicCount.get());
        assertTrue(createdMusicMap.containsKey("music/title.mp3"));

        // Play stage music
        musicPlayer.playStageMusic();
        assertEquals(2, newMusicCount.get());
        assertTrue(createdMusicMap.containsKey("music/demystify_feast.mp3"));

        // Play title music again - should reuse cached instance
        musicPlayer.playTitleMusic();
        assertEquals(2, newMusicCount.get()); // count did not increase!

        // Play final boss music
        musicPlayer.playFinalBossMusic();
        assertEquals(3, newMusicCount.get());

        // Play title music again - reuse
        musicPlayer.playTitleMusic();
        assertEquals(3, newMusicCount.get());

        // Verify none disposed yet
        for (DummyMusic m : createdMusicMap.values()) {
            assertFalse(m.disposed);
        }

        // Dispose MusicPlayer
        musicPlayer.dispose();

        // Verify all cached Music instances are disposed
        for (DummyMusic m : createdMusicMap.values()) {
            assertTrue(m.disposed);
        }

        // Field check: musicCache should be empty after dispose
        Field cacheField = MusicPlayer.class.getDeclaredField("musicCache");
        cacheField.setAccessible(true);
        Map<?, ?> cache = (Map<?, ?>) cacheField.get(musicPlayer);
        assertTrue(cache.isEmpty());
        System.out.println("  PASSED: testMusicPlayerCachingAndDisposal");
    }

    public static void testBloonManagerDisposal() {
        setUp();
        // BloonManager creates popSound in constructor
        BloonManager bloonManager = new BloonManager(null, null);
        assertNotNull(createdSound);
        assertFalse(createdSound.disposed);

        bloonManager.dispose();
        assertTrue(createdSound.disposed);
        System.out.println("  PASSED: testBloonManagerDisposal");
    }

    public static void testBloonsTouhouDefenseDisposal() throws Exception {
        setUp();
        BloonsTouhouDefense game = new BloonsTouhouDefense();

        MusicPlayer mockMusicPlayer = new MusicPlayer();
        mockMusicPlayer.playTitleMusic();
        DummyMusic titleMusic = createdMusicMap.get("music/title.mp3");
        assertNotNull(titleMusic);
        assertFalse(titleMusic.disposed);

        BloonManager bloonManager = new BloonManager(null, null);
        assertNotNull(createdSound);
        assertFalse(createdSound.disposed);

        Field musicPlayerField = BloonsTouhouDefense.class.getDeclaredField("musicPlayer");
        musicPlayerField.setAccessible(true);
        musicPlayerField.set(game, mockMusicPlayer);

        // Create dummy Map to hold bloonManager without running Map constructor
        Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
        com.hongbao.bloons.Map dummyMap = (com.hongbao.bloons.Map) unsafe.allocateInstance(com.hongbao.bloons.Map.class);

        Field bloonManagerField = com.hongbao.bloons.Map.class.getDeclaredField("bloonManager");
        bloonManagerField.setAccessible(true);
        bloonManagerField.set(dummyMap, bloonManager);

        Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(game, dummyMap);

        // Call dispose on BloonsTouhouDefense
        game.dispose();

        // Verify MusicPlayer was disposed
        assertTrue(titleMusic.disposed);
        // Verify BloonManager (and its popSound) was disposed
        assertTrue(createdSound.disposed);
        System.out.println("  PASSED: testBloonsTouhouDefenseDisposal");
    }
}
