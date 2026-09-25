package com.hongbao.bloons;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class SoundManagerTest {

    private SoundManager soundManager;

    @Before
    public void setUp() {
        soundManager = new SoundManager();
    }

    @Test
    public void testDisposableLifecycleAndCaching() {
        AtomicBoolean soundDisposed = new AtomicBoolean(false);
        AtomicBoolean musicDisposed = new AtomicBoolean(false);

        Sound mockSound = new Sound() {
            @Override public long play() { return 0; }
            @Override public long play(float volume) { return 0; }
            @Override public long play(float volume, float pitch, float pan) { return 0; }
            @Override public long loop() { return 0; }
            @Override public long loop(float volume) { return 0; }
            @Override public long loop(float volume, float pitch, float pan) { return 0; }
            @Override public void stop() {}
            @Override public void pause() {}
            @Override public void resume() {}
            @Override public void dispose() { soundDisposed.set(true); }
            @Override public void stop(long soundId) {}
            @Override public void pause(long soundId) {}
            @Override public void resume(long soundId) {}
            @Override public void setLooping(long soundId, boolean looping) {}
            @Override public void setPitch(long soundId, float pitch) {}
            @Override public void setVolume(long soundId, float volume) {}
            @Override public void setPan(long soundId, float pan, float volume) {}
        };

        Music mockMusic = new Music() {
            @Override public void play() {}
            @Override public void pause() {}
            @Override public void stop() {}
            @Override public boolean isPlaying() { return false; }
            @Override public void setLooping(boolean isLooping) {}
            @Override public boolean isLooping() { return false; }
            @Override public void setVolume(float volume) {}
            @Override public float getVolume() { return 0; }
            @Override public void setPan(float pan, float volume) {}
            @Override public void setPosition(float position) {}
            @Override public float getPosition() { return 0; }
            @Override public void dispose() { musicDisposed.set(true); }
            @Override public void setOnCompletionListener(OnCompletionListener listener) {}
        };

        soundManager.getSoundCache().put("test_sound.mp3", mockSound);
        soundManager.getMusicCache().put("test_music.mp3", mockMusic);

        assertEquals(1, soundManager.getSoundCache().size());
        assertEquals(1, soundManager.getMusicCache().size());

        soundManager.dispose();

        assertTrue("Sound should be disposed upon SoundManager disposal", soundDisposed.get());
        assertTrue("Music should be disposed upon SoundManager disposal", musicDisposed.get());
        assertEquals(0, soundManager.getSoundCache().size());
        assertEquals(0, soundManager.getMusicCache().size());
    }

    @Test
    public void testHeadlessSafetyWhenGdxAudioIsNull() {
        // Must not throw NPE when Gdx.audio is null in headless environments
        soundManager.onTowerPlaced(null);
        soundManager.onTowerUpgraded(null);
        soundManager.onTowerSold(null);
        soundManager.onSpellActivated(null);
        soundManager.onBloonDamaged(null, 1);
        soundManager.onBloonPopped(null, 1);
        soundManager.onBulletFired(null);
        soundManager.playSound("non_existent.mp3", 0.5f);

        assertNull(soundManager.getSound("non_existent.mp3"));
        assertNull(soundManager.getMusic("non_existent.mp3"));
    }
}
