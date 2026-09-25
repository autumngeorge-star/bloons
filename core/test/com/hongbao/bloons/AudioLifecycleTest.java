package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AudioLifecycleTest {

    private Audio mockAudio;
    private Files mockFiles;
    private Application mockApp;
    private Music mockMusic1;
    private Music mockMusic2;
    private Sound mockSound;

    @Before
    public void setUp() {
        mockAudio = mock(Audio.class);
        mockFiles = mock(Files.class);
        mockApp = mock(Application.class);

        mockMusic1 = mock(Music.class);
        mockMusic2 = mock(Music.class);
        mockSound = mock(Sound.class);

        FileHandle mockFileHandle = mock(FileHandle.class);
        when(mockFileHandle.readString()).thenReturn("1 100 red\nEND\n");
        when(mockFiles.internal(any(String.class))).thenReturn(mockFileHandle);

        when(mockAudio.newMusic(any(FileHandle.class)))
                .thenReturn(mockMusic1)
                .thenReturn(mockMusic2);
        when(mockAudio.newSound(any(FileHandle.class))).thenReturn(mockSound);

        Gdx.audio = mockAudio;
        Gdx.files = mockFiles;
        Gdx.app = mockApp;
    }

    @Test
    public void testMusicPlayerImplementsDisposable() {
        MusicPlayer musicPlayer = new MusicPlayer();
        assertTrue("MusicPlayer must implement Disposable", musicPlayer instanceof Disposable);
    }

    @Test
    public void testMusicPlayerFreesPreviousTrackBeforeLoadingNewTrack() {
        MusicPlayer musicPlayer = new MusicPlayer();
        
        // Play first track
        musicPlayer.playTitleMusic();
        verify(mockAudio, times(1)).newMusic(any(FileHandle.class));

        // Change track - should dispose first track
        musicPlayer.playStageMusic();
        verify(mockMusic1, times(1)).dispose();
        verify(mockAudio, times(2)).newMusic(any(FileHandle.class));
    }

    @Test
    public void testMusicPlayerDisposeFreesTrack() {
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playTitleMusic();

        musicPlayer.dispose();
        verify(mockMusic1, times(1)).dispose();
    }

    @Test
    public void testBloonManagerImplementsDisposable() {
        // BloonManager requires stage and map null or mock
        BloonManager bloonManager = new BloonManager(null, null);
        assertTrue("BloonManager must implement Disposable", bloonManager instanceof Disposable);
    }

    @Test
    public void testBloonManagerDisposeFreesPopSound() {
        BloonManager bloonManager = new BloonManager(null, null);
        verify(mockAudio, times(1)).newSound(any(FileHandle.class));

        bloonManager.dispose();
        verify(mockSound, times(1)).dispose();
    }

    @Test
    public void testBloonsTouhouDefenseDisposeCallsSubsystemDisposes() {
        // Mock MusicPlayer and BloonManager disposals via BloonsTouhouDefense
        MusicPlayer mockMusicPlayer = mock(MusicPlayer.class);
        BloonManager mockBloonManager = mock(BloonManager.class);
        Map mockMap = mock(Map.class);
        when(mockMap.getBloonManager()).thenReturn(mockBloonManager);

        BloonsTouhouDefense game = spy(new BloonsTouhouDefense());
        
        // Use reflection or package-private setup if fields are private
        try {
            java.lang.reflect.Field mpField = BloonsTouhouDefense.class.getDeclaredField("musicPlayer");
            mpField.setAccessible(true);
            mpField.set(game, mockMusicPlayer);

            java.lang.reflect.Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
            mapField.setAccessible(true);
            mapField.set(game, mockMap);

            java.lang.reflect.Field stageField = BloonsTouhouDefense.class.getDeclaredField("stage");
            stageField.setAccessible(true);
            stageField.set(game, mock(com.badlogic.gdx.scenes.scene2d.Stage.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        game.dispose();

        verify(mockMusicPlayer, times(1)).dispose();
        verify(mockBloonManager, times(1)).dispose();
    }
}
