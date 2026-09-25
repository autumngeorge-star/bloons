package com.hongbao.bloons;

import com.badlogic.gdx.audio.Music;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class MusicPlayerTest {

    private MusicPlayer musicPlayer;

    @Before
    public void setUp() {
        musicPlayer = new MusicPlayer();
    }

    @Test
    public void testDisposableAndStopMusic() {
        musicPlayer.dispose();
        assertNull(musicPlayer.getBackgroundMusic());
    }

    @Test
    public void testMusicDisposalOnStop() {
        AtomicBoolean disposed = new AtomicBoolean(false);
        AtomicBoolean stopped = new AtomicBoolean(false);

        Music mockMusic = new Music() {
            @Override public void play() {}
            @Override public void pause() {}
            @Override public void stop() { stopped.set(true); }
            @Override public boolean isPlaying() { return false; }
            @Override public void setLooping(boolean isLooping) {}
            @Override public boolean isLooping() { return false; }
            @Override public void setVolume(float volume) {}
            @Override public float getVolume() { return 0; }
            @Override public void setPan(float pan, float volume) {}
            @Override public void setPosition(float position) {}
            @Override public float getPosition() { return 0; }
            @Override public void dispose() { disposed.set(true); }
            @Override public void setOnCompletionListener(OnCompletionListener listener) {}
        };

        // Inject mock music
        try {
            java.lang.reflect.Field field = MusicPlayer.class.getDeclaredField("backgroundMusic");
            field.setAccessible(true);
            field.set(musicPlayer, mockMusic);
        } catch (Exception e) {
            fail("Failed to set mock backgroundMusic: " + e.getMessage());
        }

        musicPlayer.stopMusic();

        assertTrue("Music should be stopped when stopMusic() is called", stopped.get());
        assertTrue("Music handle should be disposed when stopMusic() is called", disposed.get());
        assertNull("backgroundMusic reference should be cleared", musicPlayer.getBackgroundMusic());
    }
}
