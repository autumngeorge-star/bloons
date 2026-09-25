package com.hongbao.bloons;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AudioLifecycleTest {

    private Audio mockAudio;
    private Files mockFiles;
    private FileHandle mockFileHandle;

    @Before
    public void setUp() {
        mockAudio = mock(Audio.class);
        mockFiles = mock(Files.class);
        mockFileHandle = mock(FileHandle.class);

        Gdx.audio = mockAudio;
        Gdx.files = mockFiles;
        when(mockFiles.internal(any())).thenReturn(mockFileHandle);
        when(mockFileHandle.readString()).thenReturn("1 100 red\nEND\n");
    }

    @Test
    public void testMusicPlayerImplementsDisposable() {
        assertTrue("MusicPlayer must implement com.badlogic.gdx.utils.Disposable",
                Disposable.class.isAssignableFrom(MusicPlayer.class));
    }

    @Test
    public void testBloonManagerImplementsDisposable() {
        assertTrue("BloonManager must implement com.badlogic.gdx.utils.Disposable",
                Disposable.class.isAssignableFrom(BloonManager.class));
    }

    @Test
    public void testMusicPlayerFreesOldTrackBeforeLoadingNewTrack() {
        Music oldMusic = mock(Music.class);
        Music newMusic = mock(Music.class);
        when(oldMusic.isPlaying()).thenReturn(true);

        when(mockAudio.newMusic(any(FileHandle.class)))
                .thenReturn(oldMusic)
                .thenReturn(newMusic);

        MusicPlayer musicPlayer = new MusicPlayer();

        // Load initial track (title)
        musicPlayer.playTitleMusic();

        // Transition to stage track
        musicPlayer.playStageMusic();

        // Old music must be stopped and disposed
        verify(oldMusic).stop();
        verify(oldMusic).dispose();
    }

    @Test
    public void testMusicPlayerDispose() throws Exception {
        Music music = mock(Music.class);
        when(mockAudio.newMusic(any(FileHandle.class))).thenReturn(music);

        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.playTitleMusic();

        musicPlayer.dispose();

        verify(music).stop();
        verify(music).dispose();

        Field bgMusicField = MusicPlayer.class.getDeclaredField("backgroundMusic");
        bgMusicField.setAccessible(true);
        assertNull("backgroundMusic field should be set to null after dispose", bgMusicField.get(musicPlayer));
    }

    @Test
    public void testBloonManagerDispose() throws Exception {
        Sound popSound = mock(Sound.class);
        when(mockAudio.newSound(any(FileHandle.class))).thenReturn(popSound);

        Stage mockStage = mock(Stage.class);
        Map mockMap = mock(Map.class);

        BloonManager bloonManager = new BloonManager(mockStage, mockMap);

        bloonManager.dispose();

        verify(popSound).dispose();

        Field popSoundField = BloonManager.class.getDeclaredField("popSound");
        popSoundField.setAccessible(true);
        assertNull("popSound field should be set to null after dispose", popSoundField.get(bloonManager));
    }

    @Test
    public void testBloonsTouhouDefenseDispose() throws Exception {
        MusicPlayer mockMusicPlayer = mock(MusicPlayer.class);
        BloonManager mockBloonManager = mock(BloonManager.class);
        Map mockMap = mock(Map.class);
        Stage mockStage = mock(Stage.class);

        when(mockMap.getBloonManager()).thenReturn(mockBloonManager);

        BloonsTouhouDefense game = new BloonsTouhouDefense();

        Field mpField = BloonsTouhouDefense.class.getDeclaredField("musicPlayer");
        mpField.setAccessible(true);
        mpField.set(game, mockMusicPlayer);

        Field mapField = BloonsTouhouDefense.class.getDeclaredField("map");
        mapField.setAccessible(true);
        mapField.set(game, mockMap);

        Field stageField = BloonsTouhouDefense.class.getDeclaredField("stage");
        stageField.setAccessible(true);
        stageField.set(game, mockStage);

        game.dispose();

        verify(mockMusicPlayer).dispose();
        verify(mockBloonManager).dispose();
        verify(mockStage).dispose();
    }
}
