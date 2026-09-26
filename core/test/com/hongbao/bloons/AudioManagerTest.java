package com.hongbao.bloons;

import com.badlogic.gdx.Application;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AudioManagerTest {

	private Files filesMock;
	private Audio audioMock;
	private Application appMock;
	private FileHandle fileHandleMock;
	private Music musicMock;
	private Sound soundMock;

	@Before
	public void setUp() {
		filesMock = mock(Files.class);
		audioMock = mock(Audio.class);
		appMock = mock(Application.class);
		fileHandleMock = mock(FileHandle.class);
		musicMock = mock(Music.class);
		soundMock = mock(Sound.class);

		Gdx.files = filesMock;
		Gdx.audio = audioMock;
		Gdx.app = appMock;

		when(filesMock.internal(anyString())).thenReturn(fileHandleMock);
		when(fileHandleMock.exists()).thenReturn(true);
		when(audioMock.newMusic(any(FileHandle.class))).thenReturn(musicMock);
		when(audioMock.newSound(any(FileHandle.class))).thenReturn(soundMock);
	}

	@Test
	public void testGetMusicCaching() {
		AudioManager audioManager = new AudioManager();

		Music firstCall = audioManager.getMusic("music/title.mp3");
		Music secondCall = audioManager.getMusic("music/title.mp3");

		assertNotNull(firstCall);
		assertSame(firstCall, secondCall);
		verify(audioMock, times(1)).newMusic(fileHandleMock);
	}

	@Test
	public void testGetSoundCaching() {
		AudioManager audioManager = new AudioManager();

		Sound firstCall = audioManager.getSound("music/pop.mp3");
		Sound secondCall = audioManager.getSound("music/pop.mp3");

		assertNotNull(firstCall);
		assertSame(firstCall, secondCall);
		verify(audioMock, times(1)).newSound(fileHandleMock);
	}

	@Test
	public void testDisposeFreesResourcesAndClearsCache() {
		AudioManager audioManager = new AudioManager();

		Music music = audioManager.getMusic("music/title.mp3");
		Sound sound = audioManager.getSound("music/pop.mp3");
		when(music.isPlaying()).thenReturn(true);

		audioManager.dispose();

		verify(music).stop();
		verify(music).dispose();
		verify(sound).stop();
		verify(sound).dispose();

		// Calling getMusic again after dispose should attempt a new load
		audioManager.getMusic("music/title.mp3");
		verify(audioMock, times(2)).newMusic(fileHandleMock);
	}

	@Test
	public void testHandlesMissingFileGracefully() {
		AudioManager audioManager = new AudioManager();
		when(fileHandleMock.exists()).thenReturn(false);

		Music music = audioManager.getMusic("nonexistent.mp3");
		Sound sound = audioManager.getSound("nonexistent.mp3");

		assertNull(music);
		assertNull(sound);
		verify(appMock, times(2)).error(eq("AudioManager"), anyString());
	}

	@Test
	public void testHandlesNullFileOrExceptionGracefully() {
		AudioManager audioManager = new AudioManager();
		assertNull(audioManager.getMusic(null));
		assertNull(audioManager.getSound(null));

		when(filesMock.internal(anyString())).thenThrow(new RuntimeException("Audio error"));
		assertNull(audioManager.getMusic("music/error.mp3"));
		assertNull(audioManager.getSound("music/error.mp3"));
	}
}
