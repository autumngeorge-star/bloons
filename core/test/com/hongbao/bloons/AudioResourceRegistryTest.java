package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AudioResourceRegistryTest {

    private Audio mockAudio;
    private Files mockFiles;

    @Before
    public void setUp() {
        mockAudio = mock(Audio.class);
        mockFiles = mock(Files.class);

        Gdx.audio = mockAudio;
        Gdx.files = mockFiles;

        FileHandle dummyHandle = mock(FileHandle.class);
        when(mockFiles.internal(anyString())).thenReturn(dummyHandle);
    }

    @Test
    public void testGetSoundCachesAndTracksSound() {
        Sound mockSound = mock(Sound.class);
        when(mockAudio.newSound(any(FileHandle.class))).thenReturn(mockSound);

        AudioResourceRegistry registry = new AudioResourceRegistry();

        Sound sound1 = registry.getSound("music/pop.mp3");
        Sound sound2 = registry.getSound("music/pop.mp3");

        assertNotNull(sound1);
        assertSame(sound1, sound2);
        // newSound should only be called once due to caching
        verify(mockAudio, times(1)).newSound(any(FileHandle.class));
    }

    @Test
    public void testLoadMusicDisposesPreviousMusicTrack() {
        Music music1 = mock(Music.class);
        Music music2 = mock(Music.class);

        when(mockAudio.newMusic(any(FileHandle.class)))
                .thenReturn(music1)
                .thenReturn(music2);

        AudioResourceRegistry registry = new AudioResourceRegistry();

        Music loadedMusic1 = registry.loadMusic("music/title.mp3");
        assertSame(music1, loadedMusic1);
        assertEquals(music1, registry.getCurrentMusic());

        // Now load a new music track
        Music loadedMusic2 = registry.loadMusic("music/demystify_feast.mp3");
        assertSame(music2, loadedMusic2);
        assertEquals(music2, registry.getCurrentMusic());

        // Verify that music1 was stopped and disposed when music2 was loaded
        verify(music1, times(1)).stop();
        verify(music1, times(1)).dispose();
        verify(music2, never()).dispose();
    }

    @Test
    public void testDisposeFreesAllResources() {
        Sound sound = mock(Sound.class);
        Music music = mock(Music.class);

        when(mockAudio.newSound(any(FileHandle.class))).thenReturn(sound);
        when(mockAudio.newMusic(any(FileHandle.class))).thenReturn(music);

        AudioResourceRegistry registry = new AudioResourceRegistry();
        registry.getSound("music/pop.mp3");
        registry.loadMusic("music/title.mp3");

        registry.dispose();

        verify(sound, times(1)).dispose();
        verify(music, times(1)).stop();
        verify(music, times(1)).dispose();
        assertNull(registry.getCurrentMusic());
    }

    @Test
    public void testMusicPlayerDelegatesToAudioResourceRegistry() {
        Music music1 = mock(Music.class);
        when(mockAudio.newMusic(any(FileHandle.class))).thenReturn(music1);

        AudioResourceRegistry registry = spy(new AudioResourceRegistry());
        MusicPlayer musicPlayer = new MusicPlayer(registry);

        musicPlayer.playTitleMusic();

        verify(registry, times(1)).loadMusic("music/title.mp3");
        verify(music1, times(1)).setLooping(true);
        verify(music1, times(1)).setVolume(0.5f);
    }
}
