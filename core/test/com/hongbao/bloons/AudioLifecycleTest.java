package com.hongbao.bloons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AudioLifecycleTest {

    private Audio mockAudio;
    private Files mockFiles;

    @Before
    public void setUp() {
        mockAudio = mock(Audio.class);
        mockFiles = mock(Files.class);
        FileHandle mockFileHandle = mock(FileHandle.class);

        Gdx.audio = mockAudio;
        Gdx.files = mockFiles;

        when(mockFiles.internal(anyString())).thenReturn(mockFileHandle);
        when(mockFileHandle.readString()).thenReturn("1 100 red\nEND\n");
    }

    @Test
    public void testMusicPlayerImplementsDisposable() {
        assertTrue("MusicPlayer must implement Disposable", Disposable.class.isAssignableFrom(MusicPlayer.class));
    }

    @Test
    public void testMusicPlayerPlayMusicDisposesPreviousMusic() throws Exception {
        Music mockMusic1 = mock(Music.class);
        Music mockMusic2 = mock(Music.class);

        when(mockAudio.newMusic(any())).thenReturn(mockMusic1).thenReturn(mockMusic2);
        when(mockMusic1.isPlaying()).thenReturn(true);

        MusicPlayer player = new MusicPlayer();

        // Play first music track
        player.playTitleMusic();
        verify(mockAudio, times(1)).newMusic(any());

        Field bgMusicField = MusicPlayer.class.getDeclaredField("backgroundMusic");
        bgMusicField.setAccessible(true);
        assertEquals(mockMusic1, bgMusicField.get(player));

        // Play second music track - should dispose first track
        player.playStageMusic();
        verify(mockMusic1, times(1)).stop();
        verify(mockMusic1, times(1)).dispose();
        assertEquals(mockMusic2, bgMusicField.get(player));
    }

    @Test
    public void testMusicPlayerDispose() throws Exception {
        Music mockMusic = mock(Music.class);
        when(mockAudio.newMusic(any())).thenReturn(mockMusic);

        MusicPlayer player = new MusicPlayer();
        player.playTitleMusic();

        Field bgMusicField = MusicPlayer.class.getDeclaredField("backgroundMusic");
        bgMusicField.setAccessible(true);
        assertNotNull(bgMusicField.get(player));

        player.dispose();

        verify(mockMusic, times(1)).stop();
        verify(mockMusic, times(1)).dispose();
        assertNull("backgroundMusic should be null after dispose()", bgMusicField.get(player));

        // Subsequent dispose call should not throw NPE
        player.dispose();
    }

    @Test
    public void testBloonManagerImplementsDisposableAndDisposesPopSound() throws Exception {
        assertTrue("BloonManager must implement Disposable", Disposable.class.isAssignableFrom(BloonManager.class));

        Sound mockSound = mock(Sound.class);
        when(mockAudio.newSound(any())).thenReturn(mockSound);

        Stage mockStage = mock(Stage.class);
        Map mockMap = mock(Map.class);

        BloonManager manager = new BloonManager(mockStage, mockMap);

        Field popSoundField = BloonManager.class.getDeclaredField("popSound");
        popSoundField.setAccessible(true);
        assertEquals(mockSound, popSoundField.get(manager));

        manager.dispose();

        verify(mockSound, times(1)).dispose();
        assertNull("popSound should be null after dispose()", popSoundField.get(manager));

        // Subsequent dispose call should not throw NPE
        manager.dispose();
    }

    @Test
    public void testBloonsTouhouDefenseDisposePropagates() throws Exception {
        MusicPlayer mockMusicPlayer = mock(MusicPlayer.class);
        Map mockMap = mock(Map.class);
        BloonManager mockBloonManager = mock(BloonManager.class);
        Stage mockStage = mock(Stage.class);

        when(mockMap.getBloonManager()).thenReturn(mockBloonManager);

        BloonsTouhouDefense game = new BloonsTouhouDefense();

        Field musicPlayerField = BloonsTouhouDefense.class.getDeclaredField("musicPlayer");
        musicPlayerField.setAccessible(true);
        musicPlayerField.set(game, mockMusicPlayer);

        Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(game, mockMap);

        Field stageField = BloonsTouhouDefense.class.getDeclaredField("stage");
        stageField.setAccessible(true);
        stageField.set(game, mockStage);

        game.dispose();

        verify(mockStage, times(1)).dispose();
        verify(mockMusicPlayer, times(1)).dispose();
        verify(mockBloonManager, times(1)).dispose();
    }
}
