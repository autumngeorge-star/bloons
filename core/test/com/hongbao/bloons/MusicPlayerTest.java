package com.hongbao.bloons;

import com.badlogic.gdx.audio.Music;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MusicPlayerTest {

	private AudioManager audioManagerMock;
	private Music titleMusicMock;
	private Music stageMusicMock;

	@Before
	public void setUp() {
		audioManagerMock = mock(AudioManager.class);
		titleMusicMock = mock(Music.class);
		stageMusicMock = mock(Music.class);

		when(audioManagerMock.getMusic("music/title.mp3")).thenReturn(titleMusicMock);
		when(audioManagerMock.getMusic("music/demystify_feast.mp3")).thenReturn(stageMusicMock);
	}

	@Test
	public void testPlayTitleMusicDelegatesToAudioManager() {
		MusicPlayer musicPlayer = new MusicPlayer(audioManagerMock);
		musicPlayer.playTitleMusic();

		verify(audioManagerMock).getMusic("music/title.mp3");
		verify(titleMusicMock).setVolume(0.5f);
		verify(titleMusicMock).setLooping(true);
		verify(titleMusicMock).play();
	}

	@Test
	public void testTrackSwitchingStopsPreviousMusic() {
		MusicPlayer musicPlayer = new MusicPlayer(audioManagerMock);
		musicPlayer.playTitleMusic();

		when(titleMusicMock.isPlaying()).thenReturn(true);

		musicPlayer.playStageMusic();

		verify(titleMusicMock).stop();
		verify(audioManagerMock).getMusic("music/demystify_feast.mp3");
		verify(stageMusicMock).play();
	}

	@Test
	public void testPauseResumeAndToggle() {
		MusicPlayer musicPlayer = new MusicPlayer(audioManagerMock);
		musicPlayer.playTitleMusic();

		musicPlayer.pause();
		verify(titleMusicMock).pause();

		musicPlayer.resume();
		verify(titleMusicMock, times(2)).play();

		when(titleMusicMock.isPlaying()).thenReturn(true);
		musicPlayer.toggleMusic();
		verify(titleMusicMock, times(2)).pause();

		when(titleMusicMock.isPlaying()).thenReturn(false);
		musicPlayer.toggleMusic();
		verify(titleMusicMock, times(3)).play();
	}
}
