package com.hongbao.bloons;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class SoundManagerTest {

	private SoundManager soundManager;

	@Before
	public void setUp() {
		soundManager = new SoundManager();
	}

	@Test
	public void testDefaultVolumes() {
		assertEquals(1.0f, soundManager.getMasterVolume(), 0.001f);
		assertEquals(0.5f, soundManager.getSfxVolume(), 0.001f);
		assertEquals(0.5f, soundManager.getMusicVolume(), 0.001f);
		assertEquals(0.5f, soundManager.getEffectiveSfxVolume(), 0.001f);
		assertEquals(0.5f, soundManager.getEffectiveMusicVolume(), 0.001f);
	}

	@Test
	public void testVolumeCalculations() {
		soundManager.setMasterVolume(0.8f);
		soundManager.setSfxVolume(0.6f);
		soundManager.setMusicVolume(0.7f);

		assertEquals(0.8f, soundManager.getMasterVolume(), 0.001f);
		assertEquals(0.6f, soundManager.getSfxVolume(), 0.001f);
		assertEquals(0.7f, soundManager.getMusicVolume(), 0.001f);
		assertEquals(0.48f, soundManager.getEffectiveSfxVolume(), 0.001f);
		assertEquals(0.56f, soundManager.getEffectiveMusicVolume(), 0.001f);
	}

	@Test
	public void testVolumeClamping() {
		soundManager.setMasterVolume(-0.5f);
		assertEquals(0.0f, soundManager.getMasterVolume(), 0.001f);

		soundManager.setMasterVolume(1.5f);
		assertEquals(1.0f, soundManager.getMasterVolume(), 0.001f);

		soundManager.setSfxVolume(-1.0f);
		assertEquals(0.0f, soundManager.getSfxVolume(), 0.001f);

		soundManager.setSfxVolume(2.0f);
		assertEquals(1.0f, soundManager.getSfxVolume(), 0.001f);

		soundManager.setMusicVolume(-0.1f);
		assertEquals(0.0f, soundManager.getMusicVolume(), 0.001f);

		soundManager.setMusicVolume(1.2f);
		assertEquals(1.0f, soundManager.getMusicVolume(), 0.001f);
	}

	@Test
	public void testAudioAssetConstants() {
		assertEquals("music/pop.mp3", SoundManager.POP_SOUND_PATH);
		assertEquals("music/title.mp3", SoundManager.TITLE_MUSIC_PATH);
		assertEquals("music/demystify_feast.mp3", SoundManager.STAGE_MUSIC_PATH);
		assertEquals("music/night_falls.mp3", SoundManager.FINAL_BOSS_MUSIC_PATH);
	}

	@Test
	public void testSemanticMusicTriggers() {
		soundManager.playTitleMusic();
		assertEquals(SoundManager.TITLE_MUSIC_PATH, soundManager.getCurrentMusicPath());

		soundManager.playStageMusic();
		assertEquals(SoundManager.STAGE_MUSIC_PATH, soundManager.getCurrentMusicPath());

		soundManager.playFinalBossMusic();
		assertEquals(SoundManager.FINAL_BOSS_MUSIC_PATH, soundManager.getCurrentMusicPath());
	}

	@Test
	public void testLevelMusicTriggers() {
		soundManager.playLevelMusic(1);
		assertEquals(SoundManager.STAGE_MUSIC_PATH, soundManager.getCurrentMusicPath());

		soundManager.playLevelMusic(40);
		assertEquals(SoundManager.FINAL_BOSS_MUSIC_PATH, soundManager.getCurrentMusicPath());
	}

	@Test
	public void testSoundManagerInjectionInBloonManager() {
		BloonManager bloonManager = new BloonManager(null, null, soundManager);
		assertNotNull(bloonManager.getSoundManager());
		assertSame(soundManager, bloonManager.getSoundManager());
	}
}
