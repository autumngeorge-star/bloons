package com.hongbao.bloons;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AudioManagerTest {

	private Files mockFiles;
	private Audio mockAudio;
	private Application mockApp;
	private AudioManager audioManager;

	@Before
	public void setUp() {
		mockFiles = mock(Files.class);
		mockAudio = mock(Audio.class);
		mockApp = mock(Application.class);

		Gdx.files = mockFiles;
		Gdx.audio = mockAudio;
		Gdx.app = mockApp;

		audioManager = new AudioManager();
	}

	@After
	public void tearDown() {
		if (audioManager != null) {
			audioManager.dispose();
		}
		Gdx.files = null;
		Gdx.audio = null;
		Gdx.app = null;
	}

	@Test
	public void testGracefulHandlingOfMissingSoundFile() {
		FileHandle missingHandle = mock(FileHandle.class);
		when(missingHandle.exists()).thenReturn(false);
		when(mockFiles.internal("missing/sound.mp3")).thenReturn(missingHandle);

		long result = audioManager.playSound("missing/sound.mp3", 0.5f);

		assertEquals(-1, result);
		assertEquals(0, audioManager.getCachedSoundCount());
		verify(mockAudio, never()).newSound(any());
	}

	@Test
	public void testSoundCachingAndReuse() {
		FileHandle soundHandle = mock(FileHandle.class);
		when(soundHandle.exists()).thenReturn(true);
		when(mockFiles.internal("music/pop.mp3")).thenReturn(soundHandle);

		Sound mockSound = mock(Sound.class);
		when(mockSound.play(0.5f)).thenReturn(100L);
		when(mockAudio.newSound(soundHandle)).thenReturn(mockSound);

		// First call - should load and cache
		long id1 = audioManager.playSound("music/pop.mp3", 0.5f);
		assertEquals(100L, id1);
		assertEquals(1, audioManager.getCachedSoundCount());

		// Second call - should reuse cached sound
		long id2 = audioManager.playSound("music/pop.mp3", 0.5f);
		assertEquals(100L, id2);
		assertEquals(1, audioManager.getCachedSoundCount());

		// Verify newSound was only called once
		verify(mockAudio, times(1)).newSound(soundHandle);
		verify(mockSound, times(2)).play(0.5f);
	}

	@Test
	public void testMusicTrackTransitionDisposesPreviousMusic() {
		FileHandle track1Handle = mock(FileHandle.class);
		when(track1Handle.exists()).thenReturn(true);
		when(mockFiles.internal("music/title.mp3")).thenReturn(track1Handle);

		FileHandle track2Handle = mock(FileHandle.class);
		when(track2Handle.exists()).thenReturn(true);
		when(mockFiles.internal("music/demystify_feast.mp3")).thenReturn(track2Handle);

		Music mockMusic1 = mock(Music.class);
		when(mockMusic1.isPlaying()).thenReturn(true);
		when(mockAudio.newMusic(track1Handle)).thenReturn(mockMusic1);

		Music mockMusic2 = mock(Music.class);
		when(mockMusic2.isPlaying()).thenReturn(true);
		when(mockAudio.newMusic(track2Handle)).thenReturn(mockMusic2);

		// Play title music
		audioManager.playMusic("music/title.mp3", 0.5f, true);
		assertEquals("music/title.mp3", audioManager.getCurrentMusicPath());
		verify(mockMusic1).play();

		// Switch to stage music
		audioManager.playMusic("music/demystify_feast.mp3", 0.5f, true);
		assertEquals("music/demystify_feast.mp3", audioManager.getCurrentMusicPath());

		// Verify previous music track was stopped and disposed
		verify(mockMusic1).stop();
		verify(mockMusic1).dispose();
		verify(mockMusic2).play();
	}

	@Test
	public void testSinglePointDisposalOfAllAudioAssets() {
		FileHandle soundHandle = mock(FileHandle.class);
		when(soundHandle.exists()).thenReturn(true);
		when(mockFiles.internal("music/pop.mp3")).thenReturn(soundHandle);

		Sound mockSound = mock(Sound.class);
		when(mockAudio.newSound(soundHandle)).thenReturn(mockSound);

		FileHandle musicHandle = mock(FileHandle.class);
		when(musicHandle.exists()).thenReturn(true);
		when(mockFiles.internal("music/title.mp3")).thenReturn(musicHandle);

		Music mockMusic = mock(Music.class);
		when(mockAudio.newMusic(musicHandle)).thenReturn(mockMusic);

		// Load sound and music
		audioManager.playSound("music/pop.mp3", 0.5f);
		audioManager.playMusic("music/title.mp3", 0.5f, true);

		assertEquals(1, audioManager.getCachedSoundCount());
		assertNotNull(audioManager.getCurrentMusic());

		// Execute single-point disposal
		audioManager.dispose();

		verify(mockSound).dispose();
		verify(mockMusic).stop();
		verify(mockMusic).dispose();
		assertEquals(0, audioManager.getCachedSoundCount());
		assertNull(audioManager.getCurrentMusic());
	}

	@Test
	public void testMusicPlayerDelegationToAudioManager() {
		FileHandle stageHandle = mock(FileHandle.class);
		when(stageHandle.exists()).thenReturn(true);
		when(mockFiles.internal("music/demystify_feast.mp3")).thenReturn(stageHandle);

		Music mockMusic = mock(Music.class);
		when(mockAudio.newMusic(stageHandle)).thenReturn(mockMusic);

		MusicPlayer musicPlayer = new MusicPlayer(audioManager);
		musicPlayer.playStageMusic();

		assertEquals("music/demystify_feast.mp3", audioManager.getCurrentMusicPath());
		verify(mockMusic).setVolume(0.5f);
		verify(mockMusic).setLooping(true);
	}
}
