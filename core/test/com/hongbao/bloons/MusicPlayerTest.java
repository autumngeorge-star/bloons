package com.hongbao.bloons;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MusicPlayerTest {

	private MusicPlayer musicPlayer;

	@Before
	public void setUp() {
		musicPlayer = new MusicPlayer();
	}

	@Test
	public void testCacheInitializationAndRetrieval() {
		assertNotNull("Music cache map should be initialized", musicPlayer.getMusicCache());
		assertNotNull("Sound cache map should be initialized", musicPlayer.getSoundCache());
		assertTrue(musicPlayer.getMusicCache().isEmpty());
		assertTrue(musicPlayer.getSoundCache().isEmpty());

		DummyMusic dummyMusic = new DummyMusic();
		musicPlayer.getMusicCache().put("music/title.mp3", dummyMusic);

		Music cached = musicPlayer.getMusic("music/title.mp3");
		assertSame("getMusic should return the cached Music instance", dummyMusic, cached);

		DummySound dummySound = new DummySound();
		musicPlayer.getSoundCache().put("music/pop.mp3", dummySound);

		Sound cachedSound = musicPlayer.getSound("music/pop.mp3");
		assertSame("getSound should return the cached Sound instance", dummySound, cachedSound);
	}

	@Test
	public void testTrackSwitchingReusesCachedInstances() {
		DummyMusic titleMusic = new DummyMusic();
		DummyMusic stageMusic = new DummyMusic();
		DummyMusic bossMusic = new DummyMusic();

		musicPlayer.getMusicCache().put("music/title.mp3", titleMusic);
		musicPlayer.getMusicCache().put("music/demystify_feast.mp3", stageMusic);
		musicPlayer.getMusicCache().put("music/night_falls.mp3", bossMusic);

		musicPlayer.playTitleMusic();
		assertTrue("Title music should be playing", titleMusic.playing);

		musicPlayer.playStageMusic();
		assertFalse("Title music should be stopped when switching tracks", titleMusic.playing);
		assertTrue("Stage music should be playing", stageMusic.playing);
		assertFalse("Title music should not be disposed during track switch", titleMusic.disposed);

		musicPlayer.playFinalBossMusic();
		assertFalse("Stage music should be stopped when switching to boss track", stageMusic.playing);
		assertTrue("Boss music should be playing", bossMusic.playing);
		assertFalse("Stage music should not be disposed during track switch", stageMusic.disposed);
	}

	@Test
	public void testMissingFileHandlingDoesNotThrow() {
		Music music = musicPlayer.getMusic("non_existent_music_file_path.mp3");
		assertNull("Missing music file should return null gracefully", music);

		Sound sound = musicPlayer.getSound("non_existent_sound_file_path.mp3");
		assertNull("Missing sound file should return null gracefully", sound);
	}

	@Test
	public void testDisposeCleansUpCachedResources() {
		DummyMusic music1 = new DummyMusic();
		DummyMusic music2 = new DummyMusic();
		DummySound sound1 = new DummySound();

		musicPlayer.getMusicCache().put("m1", music1);
		musicPlayer.getMusicCache().put("m2", music2);
		musicPlayer.getSoundCache().put("s1", sound1);

		musicPlayer.playTitleMusic(); // set backgroundMusic to cached music if present
		musicPlayer.dispose();

		assertTrue("Music1 should be disposed", music1.disposed);
		assertTrue("Music2 should be disposed", music2.disposed);
		assertTrue("Sound1 should be disposed", sound1.disposed);

		assertTrue("Music cache should be empty after dispose", musicPlayer.getMusicCache().isEmpty());
		assertTrue("Sound cache should be empty after dispose", musicPlayer.getSoundCache().isEmpty());
	}

	// Mock implementations for testing
	private static class DummyMusic implements Music {
		boolean playing = false;
		boolean disposed = false;
		boolean looping = false;
		float volume = 0f;

		@Override public void play() { playing = true; }
		@Override public void pause() { playing = false; }
		@Override public void stop() { playing = false; }
		@Override public boolean isPlaying() { return playing; }
		@Override public void setLooping(boolean looping) { this.looping = looping; }
		@Override public boolean isLooping() { return looping; }
		@Override public void setVolume(float volume) { this.volume = volume; }
		@Override public float getVolume() { return volume; }
		@Override public void setPan(float pan, float volume) {}
		@Override public void setPosition(float position) {}
		@Override public float getPosition() { return 0; }
		@Override public void dispose() { disposed = true; playing = false; }
		@Override public void setOnCompletionListener(OnCompletionListener listener) {}
	}

	private static class DummySound implements Sound {
		boolean disposed = false;
		boolean played = false;

		@Override public long play() { played = true; return 1; }
		@Override public long play(float volume) { played = true; return 1; }
		@Override public long play(float volume, float pitch, float pan) { played = true; return 1; }
		@Override public long loop() { return 1; }
		@Override public long loop(float volume) { return 1; }
		@Override public long loop(float volume, float pitch, float pan) { return 1; }
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
		@Override public void setPan(long soundId, float volume, float pan) {}
	}
}
